package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts;

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
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.CBRengine.AverageEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.CBRengine.CachedLinearCaseBaseEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.CBRengine.CustomPlainTextConnectorEX;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;

public class GhostsCBRengineEX implements StandardCBRApplication {
	private int level;

	private String opponent;
	private MOVE action;
	private GhostsStorageManagerEX storageManager;

	CustomPlainTextConnectorEX[] connector;
	CBRCaseBase[] caseBase;
	NNConfig simConfig;
	
	final boolean DEBUG = false;
	final double MIN_SIMILARITY = 0.95;
	
	final double MAX_SCORE_TO_PASS = 130;
	final double MIN_DIST_TO_FLEE = -55;
	final double MIN_DIST_TO_CHASE = 70;

	final static String TEAM = "SMMQMPM";  
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/grupo09/CBREX/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	
	public GhostsCBRengineEX(GhostsStorageManagerEX storageManager)
	{
		this.storageManager = storageManager;
	}
	public void setLevel(int i) {
		this.level = i;
	}
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	public void changeLevel(Integer level) {
		this.level = level;
		this.storageManager.close();
		this.storageManager.setCaseBase(caseBase[level]);
	}
	
	@Override
	public void configure() throws ExecutionException {
		//Configuramos todos los conectores para cada nivel
		connector = new CustomPlainTextConnectorEX[Constants.NUM_MAZES];
		for(int i = 0; i < Constants.NUM_MAZES; i++) {
			connector[i] = new CustomPlainTextConnectorEX();
			connector[i].initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
			connector[i].setCaseBaseFile(CASE_BASE_PATH, i + opponent +".csv");
		}
		
		//Y creamos las bases correspondientes
		caseBase = new CachedLinearCaseBaseEX[Constants.NUM_MAZES];
		for(int i = 0; i < Constants.NUM_MAZES; i++) {
			caseBase[i] = new CachedLinearCaseBaseEX();
		}
		
		//Empieza siempre en el primer nivel
		this.storageManager.setCaseBase(caseBase[0]);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new AverageEX());
		
		simConfig.addMapping(new Attribute("score",GhostsDescriptionEX.class), new Interval(15000));
		
		simConfig.addMapping(new Attribute("msPacmanPos",GhostsDescriptionEX.class), new Interval(25));
		simConfig.addMapping(new Attribute("msPacmanMove",GhostsDescriptionEX.class), new Equal());
		
		simConfig.addMapping(new Attribute("ghostDistance",GhostsDescriptionEX.class), new Interval(35));
		
		simConfig.addMapping(new Attribute("wasEaten",GhostsDescriptionEX.class), new Equal());
		simConfig.addMapping(new Attribute("isEdible",GhostsDescriptionEX.class), new Equal());

	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		for(int i = 0; i < Constants.NUM_MAZES; i++)
			caseBase[i].init(connector[i]);

		return caseBase[level];
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		boolean [] createCase = new boolean[] {true};

		if(caseBase[level].getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase[level].getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval, createCase);
		}
		
		if(createCase[0]) {
			//Compute revise & retain
			CBRCase newCase = createNewCase(query);
			this.storageManager.reviseAndRetain(newCase);
		}
	}

	private MOVE reuse(Collection<RetrievalResult> eval, boolean[] createCase)
	{
		//Tomamos solo 1, hay 4 veces m�s info en caso de fantasmas 
		//asi que no necesitamos tanta comparaci�n
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();

		GhostsResultEX result = (GhostsResultEX) mostSimilarCase.getResult();
		GhostsSolutionEX solution = (GhostsSolutionEX) mostSimilarCase.getSolution();
		
		MOVE action = solution.getAction();
		
		createCase[0] = false;
		
		//Movimiento random distinto de la solucion
		int index = (int)Math.floor(Math.random()*4);
		if(MOVE.values()[index]==action) 
			index= (index+1)%4;
		action = MOVE.values()[index];
		
		
		//Eliminaci�n por similitud
		if((similarity < MIN_SIMILARITY)) {
			createCase[0] = true;
			if(DEBUG)  System.out.print("No suficiente similar\n");
			return action;
		}
		
		//Eliminaci�n por comido y puntuacion
		if(result.getWasEaten() && result.getScore() > MAX_SCORE_TO_PASS) {
			createCase[0] = true;
			if(DEBUG)  System.out.print("Ha sido comido\n");
			return action;
		}
		
		//Comprobamos distancia
		if(result.getIsEdible() && result.getGhostDist() >= MIN_DIST_TO_FLEE) {
			createCase[0] = true;
			if(DEBUG)  System.out.print("Comestible y demasiado cerca del pacman: " + result.getGhostDist() + "\n");
			return action;
		}
		
		//Si estamos muy lejos del pacman
		if(!result.getIsEdible() && result.getGhostDist() >= MIN_DIST_TO_CHASE) {
			createCase[0] = true;
			if(DEBUG)  System.out.print("No comestible y demasiado lejos del pacman" + result.getGhostDist() + "\n");
			return action;
		}
		
		action = solution.getAction();
		if(DEBUG)  System.out.print("Accion buena: " + similarity + "\n");

		return action;
	}
	
	
	

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostsDescriptionEX newDescription = (GhostsDescriptionEX) query.getDescription();
		GhostsResultEX newResult = new GhostsResultEX();
		GhostsSolutionEX newSolution = new GhostsSolutionEX();
		int newId = this.caseBase[level].getCases().size();
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
		for(int i = 0; i < Constants.NUM_MAZES; i++) {
			caseBase[i].close();
		}
	}



}
