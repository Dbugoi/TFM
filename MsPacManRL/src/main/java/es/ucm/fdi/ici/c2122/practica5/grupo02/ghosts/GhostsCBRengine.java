package es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts;

import java.io.File;
import java.util.Iterator;
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

import es.ucm.fdi.ici.c2122.practica5.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica5.grupo02.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo02.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo02.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo02.mspacman.MsPacManDescription;
import es.ucm.fdi.ici.c2122.practica5.grupo02.mspacman.MsPacManResult;
import es.ucm.fdi.ici.c2122.practica5.grupo02.mspacman.MsPacManSolution;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class GhostsCBRengine implements StandardCBRApplication {

	// our variables
	private MOVE action;
	private String opponent;
	private double similarityCase;
	private GhostsStorageManager storageManager;

	// colibri variables
	NNConfig simConfig;
	CBRCaseBase caseBase;
	CustomPlainTextConnector connector;	
	
	public final static String GHOSTS_FOLDER = "ghosts" + File.separator;

	public GhostsCBRengine(GhostsStorageManager storageManager) {
		this.storageManager = storageManager;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent.substring(opponent.length() - 8, opponent.length()) ;
	}

	@Override
	public void configure() throws ExecutionException {
		caseBase = new CachedLinearCaseBase();
		connector = new CustomPlainTextConnector();

		connector.initFromXMLfile(FileIO.findFile(GameConstants.CONNECTOR_FILE_PATH + GHOSTS_FOLDER + "plaintextconfig.xml"));
		connector.setCaseBaseFile(GameConstants.CASE_BASE_PATH + "ghosts" + File.separator, opponent + ".csv");

		this.storageManager.setCaseBase(caseBase);

		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		simConfig.addMapping(new Attribute("score", GhostsDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("time", GhostsDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("lives", GhostsDescription.class), new Interval(3));

		simConfig.addMapping(new Attribute("mspacmanPos", GhostsDescription.class), new Interval(25));
		simConfig.addMapping(new Attribute("mspacmanLastMove", GhostsDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("nearestGhostsDistance0", GhostsDescription.class), new Interval(50));
		simConfig.addMapping(new Attribute("nearestGhostsDistance1", GhostsDescription.class), new Interval(50));
		simConfig.addMapping(new Attribute("nearestGhostsDistance2", GhostsDescription.class), new Interval(50));
		simConfig.addMapping(new Attribute("nearestGhostsDistance3", GhostsDescription.class), new Interval(50));
								
		simConfig.addMapping(new Attribute("nearestGhostsEdible0", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestGhostsEdible1", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestGhostsEdible2", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestGhostsEdible3", GhostsDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("nearestGhostsLastMoves0", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestGhostsLastMoves1", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestGhostsLastMoves2", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nearestGhostsLastMoves3", GhostsDescription.class), new Equal());
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		this.similarityCase = 0;
		
		if (caseBase.getCases().size() < GameConstants.NUM_CASES) {
			this.action = MOVE.NEUTRAL;
		} else {
			// Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			// Compute reuse
			this.action = reuse(eval);
		}

		// Compute revise & retain
		// Si es demasiado similar, no lo guardamos
		if(GameConstants.DEBUG) System.out.print("SIM: " + similarityCase + "\n");
		if(similarityCase >= GameConstants.SIM_TOO_CLOSE) {
			if(GameConstants.DEBUG) System.out.print("TooSimilar\n");
			return;
		}
		
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
	}

	private MOVE reuse(Collection<RetrievalResult> eval) {
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		
		// Selecciona los 5 mas prioritarios
		Collection<RetrievalResult> collec = SelectCases.selectTopKRR(eval, GameConstants.NUM_CASES);
		Iterator<RetrievalResult> it = collec.iterator();

		double similarity = 0.0;
		MOVE action = MOVE.NEUTRAL;
		int[] moves = new int[5];
		
		for (int i = 0; i < GameConstants.NUM_CASES; i++) {
			RetrievalResult cases = it.next();
			CBRCase mostSimilarCase = cases.get_case();
			
			if(discard(mostSimilarCase))
				continue;
				
			similarity += cases.getEval();

			GhostsSolution solution = (GhostsSolution) mostSimilarCase.getSolution();
			
			moves[solution.getAction().ordinal()]++;
		}
		
		similarity = similarity / GameConstants.NUM_CASES; //AVERAGE
		
		if (similarity > GameConstants.MIN_SIMILARITY) {
			int index_ = 4; //Neutral
			for (int i = 0 ; i < 5; i++)
				if (moves[i] > moves[index_]) 
					index_ = i;
			
			action = MOVE.values()[index_];
			if(GameConstants.DEBUG) System.out.print("MAX_MOVE: " + action + "\n");
		}
		else {
			action = MOVE.values()[(int)Math.floor(Math.random()*4)];
			if(GameConstants.DEBUG) System.out.print("RANDOM_MOVE: " + action + "\n");			
		}
		
		similarityCase = similarity;
		return action;		
	}

	/**
	 * Creates a new case using the query as description, storing the action into
	 * the solution and setting the proper id number
	 */
	private boolean discard(CBRCase mostSimilarCase) {
		GhostsResult result = (GhostsResult) mostSimilarCase.getResult();
		
		return result.getIsGhostEdible() && result.getGhostDst() < GameConstants.MIN_DIST;
	}
	
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		
		GhostsDescription newDescription = (GhostsDescription)query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
		
		int newId = this.caseBase.getCases().size();
		newId += storageManager.getPendingCases();
		
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

}
