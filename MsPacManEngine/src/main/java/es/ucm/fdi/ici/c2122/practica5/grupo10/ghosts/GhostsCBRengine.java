package es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts;

import java.io.File;
import java.util.Collection;

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
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.AverageGhosts;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman.MsPacManResult;
import es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman.MsPacManSolution;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsCBRengine implements StandardCBRApplication{

	private double similarity;
	private String opponent;
	private MOVE action;
	private GhostsStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;


	final static String TEAM = "grupo10";  //Cuidado!! poner el grupo aquí


	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/practica5/"+TEAM+"/ghosts/GhostsPlaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator;


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
		caseBase = new CachedLinearCaseBase();

		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));

		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");

		this.storageManager.setCaseBase(caseBase);

		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new AverageGhosts());

		simConfig.addMapping(new Attribute("pos_mspacman",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("pos_ghost",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_mspacman_up",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_mspacman_down",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_mspacman_left",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_mspacman_right",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_ghosts_up",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_ghosts_down",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_ghosts_left",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("distance_to_ghosts_right",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("edible_time_ghost",GhostsDescription.class), new Interval(100));
		simConfig.addMapping(new Attribute("edible_time_ghost_2",GhostsDescription.class), new Interval(100));
		simConfig.addMapping(new Attribute("edible_time_ghost_3",GhostsDescription.class), new Interval(100));
		simConfig.addMapping(new Attribute("edible_time_ghost_4",GhostsDescription.class), new Interval(100));
		simConfig.addMapping(new Attribute("distance_nearest_ppill_mspacman",GhostsDescription.class),  new Interval(200));
		simConfig.addMapping(new Attribute("distance_ghost_to_nearest_ppill",GhostsDescription.class),  new Interval(200));
		//simConfig.addMapping(new Attribute("Score",GhostsDescription.class),  new Interval(1000));
		simConfig.addMapping(new Attribute("lastMovePacman",GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("lastMoveGhost",GhostsDescription.class), new Equal());
		//simConfig.addMapping(new Attribute("MsPacmanDeaths",GhostsDescription.class),new Interval(3));
		
		
		
		//50%
		simConfig.setWeight(new Attribute("pos_mspacman",GhostsDescription.class), (double) 50);
		simConfig.setWeight(new Attribute("pos_ghost",GhostsDescription.class), (double) 50);
		
		//20%
		double distances_weight = 5;
		simConfig.setWeight(new Attribute("distance_to_mspacman_up",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_mspacman_down",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_mspacman_left",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_mspacman_right",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_ghosts_up",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_ghosts_down",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_ghosts_left",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_to_ghosts_right",GhostsDescription.class), distances_weight);
		simConfig.setWeight(new Attribute("distance_nearest_ppill_mspacman",GhostsDescription.class),distances_weight);
		simConfig.setWeight(new Attribute("distance_ghost_to_nearest_ppill",GhostsDescription.class),distances_weight);
		
		//15%
		double edible_weight=10;
		simConfig.setWeight(new Attribute("edible_time_ghost",GhostsDescription.class), (double) edible_weight * 2);
		simConfig.setWeight(new Attribute("edible_time_ghost_2",GhostsDescription.class), edible_weight);
		simConfig.setWeight(new Attribute("edible_time_ghost_3",GhostsDescription.class), edible_weight);
		simConfig.setWeight(new Attribute("edible_time_ghost_4",GhostsDescription.class), edible_weight);
		
		//20%
		simConfig.setWeight(new Attribute("lastMovePacman",GhostsDescription.class), (double) 20);
		simConfig.setWeight(new Attribute("lastMoveGhost",GhostsDescription.class), (double) 30);
		

	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
			this.similarity=0;
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase,this.similarity);

	}

	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostsDescription newDescription = (GhostsDescription) query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
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

	private MOVE reuse(Collection<RetrievalResult> eval) {
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();
		this.similarity = similarity;
		GhostsResult result = (GhostsResult) mostSimilarCase.getResult();
		GhostsSolution solution = (GhostsSolution) mostSimilarCase.getSolution();
		//Now compute a solution for the query
		
		//Here, it simply takes the action of the 1NN
		MOVE action = solution.getAction();
		
		//But if not enough similarity or bad case, choose another move randomly
//		if(similarity >0.8)
//			System.out.println(similarity);
		if((similarity<0.7)) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==action) 
				index= (index+1)%4;
			action = MOVE.values()[index];
		}
		else {
			//System.out.println("Recuperado de bd");
		
		}
		return action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();

	}

	public MOVE getSolution() {
		return this.action;
	}

}
