package es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman;

import java.io.File;
import java.util.Collection;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.Distance;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.DistancePill;
import es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine.TimeEdible;
import pacman.game.Constants.MOVE;


public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;


	CustomPlainTextConnector connector;

	MsPacManMappedCaseBase caseBase;
	NNConfig simConfig;
	
	CBRCaseBase generalCaseBase;
	CustomPlainTextConnector generalConnector;
	
	final static String TEAM = "grupo01";  // Grupo
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"mspacman"+File.separator; // Base casos
	
	
	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new MsPacManMappedCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		generalConnector = new CustomPlainTextConnector();
		generalConnector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		generalConnector.setCaseBaseFile(CASE_BASE_PATH, "general.csv"); // Base casos general
		
		generalCaseBase = new MsPacManMappedCaseBase();
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv"); // Base casos especifica al oponente
		
		this.storageManager.setCaseBase(caseBase);
		
		// Empezamos el Nearest Neighbors para más tarde recuperar k vecinos más cercanos
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());

		Attribute at1 = new Attribute("nearestPPill",MsPacManDescription.class); // Powerpill más cercana
		simConfig.addMapping(at1, new DistancePill()); // Mapping de distancia a pill
		simConfig.setWeight(at1,4.0); // Peso
		
		//Posiciones:
		Attribute at2 =new Attribute("posGhost1",MsPacManDescription.class); // Ghost más cercano
		simConfig.addMapping(at2, new Distance());
		simConfig.setWeight(at2,5.0);
		Attribute at3 = new Attribute("posGhost2",MsPacManDescription.class); // Segundo ghost más cercano...
		simConfig.addMapping(at3, new Distance());
		simConfig.setWeight(at3,3.5);
		Attribute at4 = new Attribute("posGhost3",MsPacManDescription.class); // etc.
		simConfig.addMapping(at4, new Distance());
		simConfig.setWeight(at4,2.0);
		Attribute at5 = new Attribute("posGhost4",MsPacManDescription.class);
		simConfig.addMapping(at5, new Distance());
		simConfig.setWeight(at5,1.0);
		
		//Tiempos de comestibilidad:
		Attribute at6 = new Attribute("timeGhost1", MsPacManDescription.class); // Tiempo de comestibilidad para ghost más cercano
		simConfig.addMapping(at6, new TimeEdible());
		simConfig.setWeight(at6,5.0);
		Attribute at7 = new Attribute("timeGhost2", MsPacManDescription.class); // Idem para segundo más cercano
		simConfig.addMapping(at7, new TimeEdible());
		simConfig.setWeight(at7,3.5);
		Attribute at8 = new Attribute("timeGhost3", MsPacManDescription.class); // etc.
		simConfig.addMapping(at8, new TimeEdible());
		simConfig.setWeight(at8,2.0);
		Attribute at9 = new Attribute("timeGhost4", MsPacManDescription.class);
		simConfig.addMapping(at9, new TimeEdible());
		simConfig.setWeight(at9,1.0);
		
		Attribute at10 = new Attribute("lastPacmanMove", MsPacManDescription.class); // Ultimo movimiento de Pacman
		simConfig.addMapping(at10, new Equal());
		simConfig.setWeight(at10,6.0);
		
		Attribute at11 = new Attribute("closestPill", MsPacManDescription.class); // Pill mas cercana
		simConfig.addMapping(at11, new DistancePill());
		simConfig.setWeight(at10,6.0);

		
		this.storageManager.setSimConfig(simConfig);
		
	}
	

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		generalCaseBase.init(this.generalConnector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		// Filtro basado en el nivel en el que estamos y la posicion de MsPacman
		MsPacManCaseBaseFilter filtro = new MsPacManCaseBaseFilter(
			((MsPacManDescription) query.getDescription()).getLevel(), // cogemos nivel
			((MsPacManDescription) query.getDescription()).getPosPacman() // cogemos junct
		);
		
		if(caseBase.getCases(filtro).isEmpty()) { // Caso base: si base de casos vacia entonces inicializamos con la general
			 this.action = getGeneralCase(filtro, query);
		}
		else {
			// Evaluacion de la similitud mediante NN de la base de casos
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(filtro), query, simConfig);
			// Obtenemos mediante reuse los 5 casos más similares. Devuelve una accion
			this.action = reuse(eval);
			
			if(this.action == null) { // Si da vacio, miramos en base general
				this.action = getGeneralCase(filtro, query);
			}
			
			
		}
		
		// Revisamos y guardamos el caso si es favorable:
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
		Collection<RetrievalResult> first5 = SelectCases.selectTopKRR(eval, 5); // obtenemos los 5 primeros casos
		Iterator<RetrievalResult> it= first5.iterator(); // iteramos cada uno de ellos
		MOVE action = null;

		Map<MOVE, Double> Possiblesolutions = new HashMap<MOVE, Double>();
		while(it.hasNext()) {
			
			RetrievalResult res = it.next();
			CBRCase mostSimilarCase = res.get_case();
			double similarity = res.getEval(); // Obtenemos similitud
			
			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
			
			//But if not enough similarity or bad case, choose another move randomly
			if(similarity>0.75){ // si el caso es favorable lo añadimos a possible solutions.
				if(Possiblesolutions.containsKey(solution.getAction())) {
					Possiblesolutions.put(solution.getAction(), Possiblesolutions.get(solution.getAction()) + (similarity * (result.getScore() / 10)));
				}
				else{
					Possiblesolutions.put(solution.getAction(), similarity * (result.getScore() / 10));
				}
			}			
		}
		
		double max = 0;
		for(Entry<MOVE, Double> entry: Possiblesolutions.entrySet()) {
			if(max < entry.getValue()) { // para cada posible solucion nos quedamos con la que tenga mejor score
				max = entry.getValue();
				action = entry.getKey();
			}
		}
		return action;
	}
	
	

	private MOVE getGeneralCase(MsPacManCaseBaseFilter filtro, CBRQuery query) {
		
		//Si no encontramos un caso nos quedamos con este random
		Random ran = new Random();
		MOVE action = MOVE.values()[ran.nextInt(MOVE.values().length-1)];
		
		if(!generalCaseBase.getCases(filtro).isEmpty()) { // si la base de casos general no esta empty
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(this.generalCaseBase.getCases(filtro), query, simConfig);
			Collection<RetrievalResult> first4 = SelectCases.selectTopKRR(eval, 4);
			// obtenemos los 5 mejores
			Iterator<RetrievalResult> it= first4.iterator();
			
			Map<MOVE, Double> Possiblesolutions = new HashMap<MOVE, Double>();
			while(it.hasNext()) {
				// obtenemos resultados
				RetrievalResult res = it.next();
				CBRCase mostSimilarCase = res.get_case();
				double similarity = res.getEval();
				
				MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
				MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
			
				//Si el caso general es lo suficientemente parecido lo añadimos al mapa
				if((similarity>0.7)) {
					if(Possiblesolutions.containsKey(solution.getAction())) {
						Possiblesolutions.put(solution.getAction(), Possiblesolutions.get(solution.getAction())+(similarity *result.getScore()/10));
					}
					else{
						Possiblesolutions.put(solution.getAction(), similarity * (result.getScore() / 10));
					}
				}
				
				
				
			}
			
			double max = 0;
			for(Entry<MOVE, Double> entry: Possiblesolutions.entrySet()) { // de las soluciones posibles
				if(max < entry.getValue()) { // nos quedamos con el maximo
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
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
		
		int newId = caseBase.getCases().size();
	
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action);
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
