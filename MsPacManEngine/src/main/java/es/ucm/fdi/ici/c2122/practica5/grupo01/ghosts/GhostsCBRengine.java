package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.Distance;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.DistancePill;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.DistanceToPacman;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.TimeEdible;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.TimeGhostEdible;
import es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman.MsPacManDescription;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private GhostsStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	
	CBRCaseBase generalCaseBase;
	CustomPlainTextConnector generalConnector;
	
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo01";  //Nombre grupo
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"ghosts"+File.separator;
	
	public GhostsCBRengine(GhostsStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new GhostsMappedCaseBase();
		
		generalConnector = new CustomPlainTextConnector();
		generalConnector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		generalConnector.setCaseBaseFile(CASE_BASE_PATH, "general.csv"); // base de casos general
		
		generalCaseBase = new GhostsMappedCaseBase();
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));

		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv"); // base de casos especifica al oponente
		
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig(); // Empezamos Nearest Neighbour configuration
		simConfig.setDescriptionSimFunction(new Average());

		Attribute at1 = new Attribute("nearestPPill",GhostsDescription.class); // atributo power pill más cercana
		simConfig.addMapping(at1, new DistancePill());
		simConfig.setWeight(at1,6.0);
		
		Attribute at2 =new Attribute("posGhost1",GhostsDescription.class); // posicion del fantasma mas cercano
		simConfig.addMapping(at2, new Distance());
		simConfig.setWeight(at2,2.5);
		Attribute at3 = new Attribute("posGhost2",GhostsDescription.class); // del segundo mas cercano, etc.
		simConfig.addMapping(at3, new Distance());
		simConfig.setWeight(at3,2.5);
		Attribute at4 = new Attribute("posGhost3",GhostsDescription.class);
		simConfig.addMapping(at4, new Distance());
		simConfig.setWeight(at4,2.0);
		Attribute at5 = new Attribute("posPacman",GhostsDescription.class); // posicion de pacman
		simConfig.addMapping(at5, new DistanceToPacman());
		simConfig.setWeight(at5,6.0);
		
		Attribute at6 = new Attribute("timeGhost1", GhostsDescription.class); // tiempo de comestibilidad del fantasma mas cercano
		simConfig.addMapping(at6, new TimeEdible());
		simConfig.setWeight(at6,3.0);
		Attribute at7 = new Attribute("timeGhost2", GhostsDescription.class); // etc.
		simConfig.addMapping(at7, new TimeEdible());
		simConfig.setWeight(at7,2.5);
		Attribute at8 = new Attribute("timeGhost3", GhostsDescription.class);
		simConfig.addMapping(at8, new TimeEdible());
		simConfig.setWeight(at8,2.0);
		Attribute at9 = new Attribute("timeGhost", GhostsDescription.class); // tiempo de comestibilidad de este fantasma
		simConfig.addMapping(at9, new TimeGhostEdible());
		simConfig.setWeight(at9,8.0);
		
		Attribute at10 = new Attribute("lastGhostMove", GhostsDescription.class); // ultimo movimiento del fantasma
		simConfig.addMapping(at10, new Equal());
		simConfig.setWeight(at10,6.0);
		
		storageManager.setSimConfig(simConfig);
		
	}
	

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		
		generalCaseBase.init(this.generalConnector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		GhostsCaseBaseFilter filtro = new GhostsCaseBaseFilter(
				((GhostsDescription) query.getDescription()).getLevel(), // filtro con nivel y la junction del fantasma
				((GhostsDescription) query.getDescription()).getPosGhost()
		);
		
		if(caseBase.getCases(filtro).isEmpty()) {
			 this.action = getGeneralCase(filtro, query);
		}
		else {
			// Evaluamos similitud
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(filtro), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
			//Si no obtenemos una acción de la base de casos específica usamos la general
			if(this.action == null) {
				this.action = getGeneralCase(filtro, query);
			}
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}

	
	/**
	 * We get the 5 most similar cases
	 * @param eval
	 * @return
	 */
	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		Collection<RetrievalResult> first5 = SelectCases.selectTopKRR(eval, 5); // nos quedamos con los 5 mejores casos
		
		Iterator<RetrievalResult> it= first5.iterator();
		
		MOVE action = null;
		Map<MOVE, Double> Possiblesolutions = new HashMap<MOVE, Double>(); // miramos para los 5 mejores casos...
		while(it.hasNext()) {
			
			RetrievalResult res = it.next();
			CBRCase mostSimilarCase = res.get_case();
			double similarity = res.getEval();
			
			GhostsResult result = (GhostsResult) mostSimilarCase.getResult();
			GhostsSolution solution = (GhostsSolution) mostSimilarCase.getSolution();
		

			// Si el caso es lo suficientemente similar

			if(similarity>0.75)  { 
				if(Possiblesolutions.containsKey(solution.getAction())) { // y de serlo, los metemos en las soluciones posibles.
					Possiblesolutions.put(solution.getAction(), Possiblesolutions.get(solution.getAction())+(similarity *result.getScore()/10));
				}
				else{
					Possiblesolutions.put(solution.getAction(), similarity *(result.getScore()/10));
				}
			}
			
			
			
		}
		
		double max = 0;
		for(Entry<MOVE, Double> entry: Possiblesolutions.entrySet()) {
			if(max < entry.getValue()) {// para cada posible solucion nos quedamos con la que tenga mejor score
				max = entry.getValue();
				action = entry.getKey();
			}
		}
		
		return action;

	}
	
	
	private MOVE getGeneralCase(GhostsCaseBaseFilter filtro, CBRQuery query) {
		
		//Si no encontramos un caso nos quedamos con este random
		Random ran = new Random();
		MOVE action = MOVE.values()[ran.nextInt(MOVE.values().length-1)];
		
		if(!generalCaseBase.getCases(filtro).isEmpty()) {
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(this.generalCaseBase.getCases(filtro), query, simConfig);
			Collection<RetrievalResult> first4 = SelectCases.selectTopKRR(eval, 4);
			
			Iterator<RetrievalResult> it= first4.iterator();
			
			Map<MOVE, Double> Possiblesolutions = new HashMap<MOVE, Double>();
			while(it.hasNext()) {
				
				RetrievalResult res = it.next();
				CBRCase mostSimilarCase = res.get_case();
				double similarity = res.getEval();
				
				GhostsResult result = (GhostsResult) mostSimilarCase.getResult();
				GhostsSolution solution = (GhostsSolution) mostSimilarCase.getSolution();
			
				//Si el caso general es lo suficientemente parecido lo añadimos al mapa
				if((similarity>0.7)) {
					if(Possiblesolutions.containsKey(solution.getAction())) {
						Possiblesolutions.put(solution.getAction(), Possiblesolutions.get(solution.getAction())+(similarity *result.getScore()/10));
					}
					else{
						Possiblesolutions.put(solution.getAction(), similarity *(result.getScore()/10));
					}
				}
				
				
				
			}
			
			double max = 0;
			for(Entry<MOVE, Double> entry: Possiblesolutions.entrySet()) {//de las soluciones posibles
				if(max < entry.getValue()) {//nos quedamos con el maximo
					max = entry.getValue();
					action = entry.getKey();
				}
			}
		}
		
		return action;
	}
	
	

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostsDescription newDescription = (GhostsDescription) query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
		
		int newId = this.caseBase.getCases().size();
		//newId+= storageManager.getPendingCases();
		
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		
		newSolution.setAction(action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	public MOVE getSolution() {
		return this.action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
