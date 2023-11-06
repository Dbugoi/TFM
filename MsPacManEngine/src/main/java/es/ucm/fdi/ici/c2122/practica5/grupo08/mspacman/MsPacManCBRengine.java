package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo08.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo08.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.EqualPosition;
import es.ucm.fdi.ici.c2122.practica5.grupo08.VectorDistanceSimilarity;
import es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman.MsPacmanCaseFilter.Filter;
import es.ucm.fdi.ici.graficas.EpisodeData;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private Random rnd = new Random();
	
	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;
	private MsPacManStorageManager generalStorageManager;

	CustomPlainTextConnector connector;
	CustomPlainTextConnector generalConnector;
	CBRCaseBase caseBase;
	CBRCaseBase generalCaseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo08";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
	public MsPacManCBRengine(MsPacManStorageManager storageManager, MsPacManStorageManager generalStorageManager)
	{
		this.storageManager = storageManager;
		this.generalStorageManager = generalStorageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	
	
	@Override
	public void configure() throws ExecutionException {
		this.connector = new CustomPlainTextConnector();
		this.generalConnector = new CustomPlainTextConnector();
		this.caseBase = new CachedLinearCaseBaseMsPacman();
		this.generalCaseBase = new CachedLinearCaseBaseMsPacman();
		
		this.connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		this.generalConnector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		this.connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		this.generalConnector.setCaseBaseFile(CASE_BASE_PATH, "general.csv");
		
		this.storageManager.setCaseBase(caseBase);
		this.generalStorageManager.setCaseBase(generalCaseBase);
			
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		//ghostDistances
		Attribute dAtribute = new Attribute("ghostDistances",MsPacManDescription.class);
		simConfig.addMapping(dAtribute, new VectorDistanceSimilarity(153));
		simConfig.setWeight(dAtribute, 0.4);
		//powerPillDistances
		dAtribute = new Attribute("powerPillDistances",MsPacManDescription.class);
		simConfig.addMapping(dAtribute, new VectorDistanceSimilarity(153));
		simConfig.setWeight(dAtribute, 0.1);
		//pillDistances
		dAtribute = new Attribute("pillDistances",MsPacManDescription.class);
		simConfig.addMapping(dAtribute, new VectorDistanceSimilarity(153));
		simConfig.setWeight(dAtribute, 0.1);
		//edibleGhosts
		dAtribute = new Attribute("edibleGhostsDistances",MsPacManDescription.class);
		simConfig.addMapping(dAtribute, new VectorDistanceSimilarity(153));
		simConfig.setWeight(dAtribute, 0.4);
		
		dAtribute = new Attribute("pos",MsPacManDescription.class);
		simConfig.addMapping(dAtribute, new EqualPosition());
		simConfig.setWeight(dAtribute, 0.0);
		
		this.storageManager.setSimConfig(this.simConfig);
		this.generalStorageManager.setSimConfig(this.simConfig);
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		this.generalCaseBase.init(this.generalConnector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			this.action = MOVE.values()[rnd.nextInt(4)];
		}
		else {
			
			MsPacmanCaseFilter casesFilter = new MsPacmanCaseFilter(selectFilterForQuery(query));
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(casesFilter), query, simConfig);
			//Compute reuse
			this.action = reuse(eval, query);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval, CBRQuery query)
	{
		int k = 3;

		Collection<RetrievalResult> results = SelectCases.selectTopKRR(eval, k);
		
		float[] moves = {0, 0, 0, 0};
		boolean[] real = {false, false, false, false};
		
		for(RetrievalResult r : results) {		
			CBRCase cbrCase = r.get_case();
			double similarity = r.getEval();
			MsPacManResult result = (MsPacManResult) cbrCase.getResult();
			MsPacManSolution solution = (MsPacManSolution) cbrCase.getSolution();
			
			//Meter avg
			Map<String, Double> map = EpisodeData.getAvgMap();
			if(!map.containsKey(TEAM)) {
				map.put(TEAM, 0.5);
			}
			double d=(map.get(TEAM)*0.95)+(similarity *0.05);
			map.put(TEAM, d);
			
			
			real[solution.action.ordinal()] = true;
			moves[solution.action.ordinal()] += similarity * result.result;
		}

		float max = -1;
		int moveIndex = -1;
		for(int i=0; i<moves.length; i++) {
			if(real[i] && moves[i] > max) {
				max = moves[i];
				moveIndex = i;
			}
		}
		MOVE action = MOVE.NEUTRAL;
		if(moveIndex != -1) {
			action = MOVE.values()[moveIndex];
		}
		else{
			action = MOVE.values()[rnd.nextInt(4)];
			if(generalCaseBase.getCases().isEmpty()) {
				action = MOVE.values()[rnd.nextInt(4)];
			}
			else {
				action = MOVE.values()[rnd.nextInt(4)];
				//Compute retrieve
				Collection<RetrievalResult> generalEval = NNScoringMethod.evaluateSimilarity(generalCaseBase.getCases(), query, simConfig);

				results = SelectCases.selectTopKRR(generalEval, k);
				
				float[] movesG = {0, 0, 0, 0};
				boolean[] realG = {false, false, false, false};
				
				for(RetrievalResult r : results) {		
					CBRCase cbrCase = r.get_case();
					double similarity = r.getEval();
					MsPacManResult result = (MsPacManResult) cbrCase.getResult();
					MsPacManSolution solution = (MsPacManSolution) cbrCase.getSolution();
					
					realG[solution.action.ordinal()] = true;
					movesG[solution.action.ordinal()] += similarity * result.result;
				}

				max = -1;
				moveIndex = -1;
				for(int i=0; i<movesG.length; i++) {
					if(realG[i] && movesG[i] > max) {
						max = movesG[i];
						moveIndex = i;
					}
				}
				
				if(moveIndex != -1) {
					action = MOVE.values()[moveIndex];
				}
				else action = MOVE.values()[rnd.nextInt(4)];
			}
		}
		//if(moves[moveIndex] < 0.1) Buscar en la base de casos general
		
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
		int newId = this.caseBase.getCases().size();
		newId+= storageManager.getPendingCases();
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
	
	private MsPacmanCaseFilter.Filter selectFilterForQuery(CBRQuery query) {
		CaseComponent caseComp = query.getDescription();
		MsPacManDescription msPacmanDesc = (MsPacManDescription)caseComp;
		
		for(int i = 0; i < 4; ++i) {
			if(msPacmanDesc.edibleGhostsDistances.values[i] != -1) {
				return Filter.EDIBLE;
			}
		}
		
		return Filter.NO_EDIBLE;
	}

}
