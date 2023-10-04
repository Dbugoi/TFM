package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.EnumMapInterval;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.MoveSimilarityFunction;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.MyInterval;
import es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.RulesBasedGhosts;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsCBRengine implements StandardCBRApplication {

	private static final double MIN_SIMILARITY = 0.8;
	private static final double RANDOM_MOVE_THRESHOLD = 0.01;
	private static final int NN = 3;
	static final int MAX_DISTANCE = 650;
	private static final int MAX_EDIBLE_TIME = 200;
	private static final Random rnd = new Random();

	static final String TEAM = "grupo05"; // Cuidado!! poner el grupo aqu�
	static final String CONNECTOR_FILE_PATH =
			"main/java/es/ucm/fdi/ici/c2122/practica5/" + TEAM + "/ghosts/plaintextconfig.xml";
	static final String COMMON_CASE_BASE_PATH = "cbrdata/" + TEAM + "/ghosts/";
	static final String SPECIFIC_CASE_BASE_PATH = "cbrdata/" + TEAM + "/ghosts/specific/";

	private final RulesBasedGhosts backupGhosts;
	private int lastBackupGhostsRequestTick;
	private String opponent;
	private Map<GHOST, MOVE> actions;
	private GhostsStorageManager commonStorageManager;
	private GhostsStorageManager specificStorageManager;
	private Game game;

	CustomPlainTextConnector commonConector;
	CustomPlainTextConnector specificConector;
	CBRCaseBase commonCaseBase;
	CBRCaseBase specificCaseBase;
	NNConfig simConfig;

	public GhostsCBRengine(GhostsStorageManager commonStorageManager,
			GhostsStorageManager specificStorageManager) {
		this.commonStorageManager = commonStorageManager;
		this.specificStorageManager = specificStorageManager;
		this.actions = new EnumMap<>(GHOST.class);
		this.backupGhosts = new RulesBasedGhosts();
		this.lastBackupGhostsRequestTick = -1;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}

	@Override
	public void configure() throws ExecutionException {
		int movesNum = MOVE.values().length;

		commonConector = new CustomPlainTextConnector();
		specificConector = new CustomPlainTextConnector();

		commonCaseBase = new CachedLinearCaseBase();
		specificCaseBase = new CachedLinearCaseBase();

		commonConector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		specificConector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));

		commonConector.setCaseBaseFile(COMMON_CASE_BASE_PATH, "base.csv");
		specificConector.setCaseBaseFile(SPECIFIC_CASE_BASE_PATH, opponent + ".csv");

		this.commonStorageManager.setCaseBase(commonCaseBase);
		this.specificStorageManager.setCaseBase(specificCaseBase);

		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		// addMapping("score", new MyInterval(15000));
		// addMapping("time", new MyInterval(4000));
		// addMapping("level", new Equal());

		addMapping("edibleTime", new MyInterval(MAX_EDIBLE_TIME));

		addMapping("distToPacman", new EnumMapInterval<MOVE>(MOVE.class, MAX_DISTANCE), movesNum);
		addMapping("distPacmanToGhost", new MyInterval(MAX_DISTANCE));
		addMapping("movePacmanToGhost", new MoveSimilarityFunction());

		addMapping("distToJunction", new EnumMapInterval<MOVE>(MOVE.class, MAX_DISTANCE), movesNum);

		addMapping("distToPPill", new EnumMapInterval<MOVE>(MOVE.class, MAX_DISTANCE), movesNum);
		addMapping("distPacmanToPPill", new MyInterval(MAX_DISTANCE));
		addMapping("movePacmanToPPill", new MoveSimilarityFunction());// 23

		addMapping("distToEdible", new EnumMapInterval<MOVE>(MOVE.class, MAX_DISTANCE), movesNum);
		addMapping("distPacmanToEdible", new MyInterval(MAX_DISTANCE));
		addMapping("movePacmanToEdible", new MoveSimilarityFunction());

		this.commonStorageManager.setSimConfig(simConfig);
		this.specificStorageManager.setSimConfig(simConfig);
	}

	private void addMapping(String attributeName, LocalSimilarityFunction similFunction) {
		addMapping(attributeName, similFunction, 1.0);
	}

	private void addMapping(String attributeName, LocalSimilarityFunction similFunction,
			double weight) {
		Attribute attr = new Attribute(attributeName, GhostsDescription.class);
		simConfig.addMapping(attr, similFunction);
		simConfig.setWeight(attr, weight);
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		commonCaseBase.init(commonConector);
		specificCaseBase.init(specificConector);
		return commonCaseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		throw new UnsupportedOperationException("Missing GHOST argument");
	}

	public void cycle(GHOST ghost, CBRQuery query) throws ExecutionException {
		Collection<RetrievalResult> commonEval = Collections.emptyList();
		Collection<RetrievalResult> specificEval = Collections.emptyList();
		MOVE action = null;

		if (!specificCaseBase.getCases().isEmpty()) {
			// Compute retrieve
			commonEval = NNScoringMethod.evaluateSimilarity(
					specificCaseBase.getCases(), query, simConfig);
			// compute reuse
			action = reuse(commonEval);
		}

		if (action == null && !commonCaseBase.getCases().isEmpty()) {
			// Compute retrieve
			specificEval = NNScoringMethod.evaluateSimilarity(
					commonCaseBase.getCases(), query, simConfig);
			// compute reuse
			action = reuse(specificEval);
		}

		if (action == null)
			action = getBackupMove(ghost);


		this.actions.put(ghost, action);

		// Compute revise & retain
		CBRCase newCase = createNewCase(ghost, query, commonCaseBase, commonStorageManager);
		this.commonStorageManager.reviseAndAddCase(ghost, newCase, commonEval);

		CBRCase newCaseS = createNewCase(ghost, query, specificCaseBase, specificStorageManager);
		this.specificStorageManager.reviseAndAddCase(ghost, newCaseS, specificEval);
	}

	// TODO CAMBIAR
	private MOVE reuse(Collection<RetrievalResult> eval) {
		// 3NN
		Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, NN);
		double peso = 0;
		CBRCase finalCase = null;

		for (RetrievalResult retResult : cases) {
			CBRCase mostSimilarCase = retResult.get_case();
			double similarity = retResult.getEval();
			GhostsResult ghResult = (GhostsResult) mostSimilarCase.getResult();

			if (similarity > MIN_SIMILARITY
					&& ghResult.getScore() > 0
					&& peso < (similarity + ghResult.getScore())) {
				peso = similarity + ghResult.getScore();
				finalCase = mostSimilarCase;
			}
		}

		if (finalCase == null)
			return null;

		GhostsSolution sol = (GhostsSolution) finalCase.getSolution();
		return sol.getAction();
	}

	/**
	 * Creates a new case using the query as description, storing the action into the solution and
	 * setting the proper id number
	 */
	private CBRCase createNewCase(GHOST ghost, CBRQuery query, CBRCaseBase caseB,
			GhostsStorageManager sm) {
		CBRCase newCase = new CBRCase();
		GhostsDescription newDescription = (GhostsDescription) query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
		int newId = caseB.getCases().size();

		newId += sm.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);

		newSolution.setId(newId);
		newSolution.setAction(this.actions.get(ghost));

		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);

		return newCase;
	}

	public MOVE getSolution(GHOST ghost) {
		return this.actions.get(ghost);
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.commonStorageManager.close();
		this.specificStorageManager.close();
		this.commonCaseBase.close();
		this.specificCaseBase.close();
	}

	private MOVE getPosibleRandomMove(GHOST ghost) {
		MOVE[] m = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost),
				game.getGhostLastMoveMade(ghost));
		return m[rnd.nextInt(m.length)];
	}

	private MOVE getBackupMove(GHOST ghost) {
		if (lastBackupGhostsRequestTick != game.getTotalTime())
			backupGhosts.getMove(game, game.getTotalTime());
		return backupGhosts.getMove().get(ghost);
	}

	public void setGame(Game g) {
		game = g;
	}

	/**
	 * Revisa los últimos casos generados por el fantasma {@code ghost}.
	 * 
	 * @param game
	 * @param ghost
	 */
	public void reviseLastCases(Game game, GHOST ghost) {
		commonStorageManager.setGame(game);
		commonStorageManager.forceRevise(ghost);
		specificStorageManager.setGame(game);
		specificStorageManager.forceRevise(ghost);
	}
}
