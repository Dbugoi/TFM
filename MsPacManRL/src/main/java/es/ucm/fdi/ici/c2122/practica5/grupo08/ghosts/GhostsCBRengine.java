package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import java.io.File;
import java.util.Collection;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo08.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo08.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.EqualPosition;
import es.ucm.fdi.ici.c2122.practica5.grupo08.EqualPositions;
import es.ucm.fdi.ici.c2122.practica5.grupo08.MOVEVector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.VectorBooleanSimilarity;
import es.ucm.fdi.ici.c2122.practica5.grupo08.VectorDistanceSimilarity;
import es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman.CachedLinearCaseBaseMsPacman;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/*Clase encargada de la gestión del CBR de los ghosts*/
public class GhostsCBRengine implements StandardCBRApplication {

	private Random rnd = new Random();
	
	private String opponent;		//oponente
	private MOVEVector actions;		//acciones de cada uno de los fantasmas
	private GhostsStorageManager storageManager;		//guardado de datos para aprendizaje
	private GhostsStorageManager generalStorageManager;	//para la inicializacion de casos que parten de una base ya creada
	
	//Varaibles de CBR
	CustomPlainTextConnector connector;
	CustomPlainTextConnector generalConnector;
	CBRCaseBase caseBase;
	CBRCaseBase generalCaseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo08";  //Cuidado!! poner el grupo aquÃ­
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	/**
	 * Constructora de la clase
	 * @param storageManager	casos de aprendizaje
	 * @param generalStorageManager	casos que parten de una base creada
	 */
	public GhostsCBRengine(GhostsStorageManager storageManager, GhostsStorageManager generalStorageManager)
	{
		this.storageManager = storageManager;
		this.generalStorageManager = generalStorageManager;
	}
	/**
	 * Modifica el oponente
	 * @param opponent nombre del oponente
	 */
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	/**
	 * Inicializa y configura el store manager
	 * */
	public void configure() throws ExecutionException {
		//Inicializacion de variabñs de clase
		this.connector = new CustomPlainTextConnector();
		this.generalConnector = new CustomPlainTextConnector();
		this.caseBase = new CachedLinearCaseBaseGhost();
		this.generalCaseBase = new CachedLinearCaseBaseGhost();
		
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
		//ghostPositions
		Attribute dAtribute = new Attribute("positions",GhostsDescription.class);
		simConfig.addMapping(dAtribute, new EqualPositions());
		simConfig.setWeight(dAtribute, 0.1);
		//ghostsDistances
		dAtribute = new Attribute("distanceToMsPacman",GhostsDescription.class);
		simConfig.addMapping(dAtribute, new VectorDistanceSimilarity(153));
		simConfig.setWeight(dAtribute, 0.6);
		//edibleGhosts
		dAtribute = new Attribute("edible",GhostsDescription.class);
		simConfig.addMapping(dAtribute, new VectorBooleanSimilarity());
		simConfig.setWeight(dAtribute, 0.5);
		//distanceMsPacmanPpill
		dAtribute = new Attribute("distanceMsPacmanPpill",GhostsDescription.class);
		simConfig.addMapping(dAtribute, new EqualPosition());
		simConfig.setWeight(dAtribute, 0.3);
		
		this.storageManager.setSimConfig(this.simConfig);
		this.generalStorageManager.setSimConfig(this.simConfig);
	}

	@Override
	/**
	 * Inicializa el caso base
	 */
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		this.generalCaseBase.init(this.generalConnector);
		return caseBase;
	}

	@Override
	/** 
	 * En caso de no tener casos asigna movimientos aleatorios a los fantasmas.
	 * En caso de tenerlos asigna los valores obtenidos por el cbr
	 */
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			this.actions = new MOVEVector(4);
			for(GHOST g : GHOST.values()) {
				this.actions.values[g.ordinal()] = MOVE.values()[rnd.nextInt(4)];
			}
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.actions = reuse(eval, query);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}
	
	/**
	 * Evalua los casos dado la base de casos de aprendizaj. Para ello comprueba que los resultados sean
	 * mayores que 1. Los más similes del KRR no tienen resultados positivos mira en la base de casos general
	 * @param eval casos sobre los que se va a evaluar la similaridad
	 * @param query	consulta a realizar en la base de casos
	 * @return Vector de 4 movimientos correspondiendose a cada uno de los fantasmas
	 */
	private MOVEVector reuse(Collection<RetrievalResult> eval, CBRQuery query)
	{
		int k = 3;
		//Obtenemos la base de casos
		Collection<RetrievalResult> results = SelectCases.selectTopKRR(eval, k);
		//guarda para fantasma en cada movimiento(up,down,left,right) el valor obtenido de la similitud
		//con el resultado.
		float[][] moves = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
		//guarda para fantasma en cada movimiento(up,down,left,right) el valor si ese movimiento ha sido o no
		//escogido por la base de casos.
		boolean[][] real = {{false, false, false, false}, {false, false, false, false}, 
				{false, false, false, false}, {false, false, false, false}};
		
		//Para cada resultado mira en cada uno de los fantasmas el valor obtenido de la similitud 
		//por el resultado
		for(RetrievalResult r : results) {		
			CBRCase cbrCase = r.get_case();
			double similarity = r.getEval();
			GhostsResult result = (GhostsResult) cbrCase.getResult();
			GhostsSolution solution = (GhostsSolution) cbrCase.getSolution();
			
			for(int i=0; i<4; i++) {
				//solution.actions.values[i].ordinal() -> accion correspondiente convertida a entero
				real[i][solution.actions.values[i].ordinal()] = true;
				moves[i][solution.actions.values[i].ordinal()] += similarity * result.result.values[i];		
			}		
		}
		//Comprobamos si hay algún movimiento para cada fantasma
		//que no se haya dado de manera positiva
		float[] max = {-1,-1,-1,-1};		//maximo valor de sim * resultado hasta el momento por cada fantasma
		int[] moveIndex = {-1,-1,-1,-1};	//movimiento final para cada fantasma
		for(int i=0; i<moves.length; i++) {
			for(int j=0; j<moves[i].length; j++) {
				if(real[i][j] && moves[i][j] > max[i]) {
					max[i] = moves[i][j];
					moveIndex[i] = j;
				}
			}
		}
		MOVE[] ghostMoves = {MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL};
		MOVEVector generalMoves = reuseGeneralCaseBase(query);
		
		//Asignamos los movimientos a cada fantasma
		for(int i=0; i<ghostMoves.length; i++) {
			if(max[i] != -1) {	//Si el valor de sim * result resulta ser positivo
				ghostMoves[i] = MOVE.values()[moveIndex[i]];
			}
			else {	//Sino, miramos en la base de casos general
				ghostMoves[i] = generalMoves.values[i];
			}
		}
		
		MOVEVector aux = new MOVEVector(4);
		aux.values = ghostMoves;
		return aux;
	}
	/**
	 * Evalua los casos dado la base de casos general. Para ello comprueba que los resultados sean
	 * mayores que 1. Si los más similes del KRR no tienen resultados positivos devuelve random
	 * @param eval casos sobre los que se va a evaluar la similaridad
	 * @param query	consulta a realizar en la base de casos
	 * @return Vector de 4 movimientos correspondiendose a cada uno de los fantasmas
	 */
	private MOVEVector reuseGeneralCaseBase(CBRQuery query) {
		int k = 3;

		if(generalCaseBase.getCases().isEmpty()) {
			MOVEVector ghostActions = new MOVEVector(4);
			for(GHOST g : GHOST.values()) {
				ghostActions.values[g.ordinal()] = MOVE.values()[rnd.nextInt(4)];
			}
			return ghostActions;
		}	
		//Compute retrieve
		Collection<RetrievalResult> generalEval = NNScoringMethod.evaluateSimilarity(this.generalCaseBase.getCases(), query, simConfig);
		Collection<RetrievalResult> results = SelectCases.selectTopKRR(generalEval, k);
		//guarda para fantasma en cada movimiento(up,down,left,right) el valor si ese movimiento ha sido o no
				//escogido por la base de casos.
		float[][] moves = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
		boolean[][] real = {{false, false, false, false}, {false, false, false, false}, 
				{false, false, false, false}, {false, false, false, false}};
		//Para cada resultado mira en cada uno de los fantasmas el valor obtenido de la similitud 
		//por el resultado
		for(RetrievalResult r : results) {		
			CBRCase cbrCase = r.get_case();
			double similarity = r.getEval();
			GhostsResult result = (GhostsResult) cbrCase.getResult();
			GhostsSolution solution = (GhostsSolution) cbrCase.getSolution();
			
			for(int i=0; i<4; i++) {
				real[i][solution.actions.values[i].ordinal()] = true;
				moves[i][solution.actions.values[i].ordinal()] += similarity * result.result.values[i];		
			}		
		}
		//Comprobamos si hay algún movimiento para cada fantasma
		//que no se haya dado de manera positiva
		float[] max = {-1,-1,-1,-1};
		int[] moveIndex = {-1,-1,-1,-1};
		for(int i=0; i<moves.length; i++) {
			for(int j=0; j<moves[i].length; j++) {
				if(real[i][j] && moves[i][j] > max[i]) {
					max[i] = moves[i][j];
					moveIndex[i] = j;
				}
			}
		}
		
		MOVE[] ghostMoves = {MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL};
		//Asignamos los movimientos a cada fantasma
		for(int i=0; i<ghostMoves.length; i++) {
			if(max[i] != -1) {	//Si el valor de sim * result resulta ser positivo
				ghostMoves[i] = MOVE.values()[moveIndex[i]];
			}
			else {	//Sino random
				ghostMoves[i] = MOVE.values()[rnd.nextInt(4)];
			}
		}
		
		MOVEVector aux = new MOVEVector(4);
		aux.values = ghostMoves;
		return aux;
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
		newId+= storageManager.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setActions(this.actions);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	public MOVE[] getSolution() {
		return this.actions.values;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
