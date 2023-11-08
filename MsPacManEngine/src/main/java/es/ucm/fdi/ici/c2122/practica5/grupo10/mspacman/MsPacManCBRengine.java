package es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.DistanceSim;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.MovesFilter;
import es.ucm.fdi.ici.graficas.EpisodeData;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private double sim;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CachedLinearCaseBase caseBase;
	NNConfig simConfig;
	
	final static double SIMILARITY_THRESHOLD = 0.9; //threshold that determines if action is reused
	
	final static double GHOST_WEIGHT = 2;	//weights for attributes
	final static double EDIBLE_WEIGHT = 3;
	final static double PILL_WEIGHT = 2;
	final static double PPILL_WEIGHT = 3;

	
	final static String TEAM = "grupo10";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
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
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("ghostDistance1",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("ghostDistance2",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("ghostDistance3",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("ghostDistance4",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("edibleDistance1",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("edibleDistance2",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("edibleDistance3",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("edibleDistance4",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("pillsDistance1",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("pillsDistance2",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("pillsDistance3",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("pillsDistance4",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("powerPillsDistance1",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("powerPillsDistance2",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("powerPillsDistance3",MsPacManDescription.class), new DistanceSim(150));
		simConfig.addMapping(new Attribute("powerPillsDistance4",MsPacManDescription.class), new DistanceSim(150));
		
		simConfig.setWeight(new Attribute("ghostDistance1",MsPacManDescription.class), GHOST_WEIGHT);
		simConfig.setWeight(new Attribute("ghostDistance2",MsPacManDescription.class), GHOST_WEIGHT);
		simConfig.setWeight(new Attribute("ghostDistance3",MsPacManDescription.class), GHOST_WEIGHT);
		simConfig.setWeight(new Attribute("ghostDistance4",MsPacManDescription.class), GHOST_WEIGHT);
		simConfig.setWeight(new Attribute("edibleDistance1",MsPacManDescription.class), EDIBLE_WEIGHT);
		simConfig.setWeight(new Attribute("edibleDistance2",MsPacManDescription.class), EDIBLE_WEIGHT);
		simConfig.setWeight(new Attribute("edibleDistance3",MsPacManDescription.class), EDIBLE_WEIGHT);
		simConfig.setWeight(new Attribute("edibleDistance4",MsPacManDescription.class), EDIBLE_WEIGHT);
		simConfig.setWeight(new Attribute("pillsDistance1",MsPacManDescription.class), PILL_WEIGHT);
		simConfig.setWeight(new Attribute("pillsDistance2",MsPacManDescription.class), PILL_WEIGHT);
		simConfig.setWeight(new Attribute("pillsDistance3",MsPacManDescription.class), PILL_WEIGHT);
		simConfig.setWeight(new Attribute("pillsDistance4",MsPacManDescription.class), PILL_WEIGHT);
		simConfig.setWeight(new Attribute("powerPillsDistance1",MsPacManDescription.class), PPILL_WEIGHT);
		simConfig.setWeight(new Attribute("powerPillsDistance2",MsPacManDescription.class), PPILL_WEIGHT);
		simConfig.setWeight(new Attribute("powerPillsDistance3",MsPacManDescription.class), PPILL_WEIGHT);
		simConfig.setWeight(new Attribute("powerPillsDistance4",MsPacManDescription.class), PPILL_WEIGHT);
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		//filter cases to match set of valid directions
		Collection<CBRCase> filteredCases = caseBase.getCases(new MovesFilter(((MsPacManDescription) query.getDescription()).getValidDirections()));
		
		//if there are no cases select random valid move
		if(filteredCases.isEmpty()) {
			sim = 0;
			this.action = ((MsPacManDescription) query.getDescription()).getRandomMove();
		}
		//if there are cases try to reuse
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(filteredCases, query, simConfig);
			//Compute reuse
			this.action = reuse(eval, query);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		if(!caseBase.isFull())
			this.storageManager.reviseAndRetain(newCase, sim);
		
		//Put caseBase Size
		Map<String, Integer> sizeMap = EpisodeData.getCaseBaseSizeMap();
		sizeMap.put(TEAM, caseBase.getCases().size());
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval, CBRQuery query)
	{
		// 1NN use to compare the similary
		// Consider using kNNs with majority voting
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		//CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();
		sim = similarity;
		
		//Meter avg  para TFM
		Map<String, Double> map = EpisodeData.getAvgMap();
		if(!map.containsKey(TEAM)) {
			map.put(TEAM, 0.5);
		}
		double d=(map.get(TEAM)*0.95)+(similarity *0.05);
		map.put(TEAM, d);
		
		//
		// Now compute a solution for the query
		action = ((MsPacManSolution) first.get_case().getSolution()).getAction();
		
		// But if not enough similarity or bad case, choose another move randomly
		if ((similarity < SIMILARITY_THRESHOLD) /*|| (result.getScore() < 100)*/) {
			action = ((MsPacManDescription) query.getDescription()).getRandomMove();
		}
		//System.out.println(sim);
		//System.out.println(action);
		//System.out.println(first.get_case().getDescription().toString());

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

}
