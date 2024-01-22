package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

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
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.CBRengine.EuclidianSimilarityInterval;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengineEX implements StandardCBRApplication {
	private Integer level;
	
	private String opponent;
	private MOVE action;
	private MsPacManStorageManagerEX storageManager;

	CustomPlainTextConnectorEX[] connector;
	CBRCaseBase[] caseBase;
	NNConfig simConfig;
	
	final int MAX_CASES = 3;
	final int MAX_SCORE_DIFF = 150;
	final int MAX_DISTANCE_GHOST = 80;
	final int MAX_DISTANCE_POWERPILL = 60;
	
	final double MIN_SIMILARITY = 0.6;
	final double MIN_SIMILARITY_TO_DISCARD = 0.45;

	final boolean DEBUG = false;
	
	final static String TEAM = "SMMQMPM";
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/grupo09/CBREX/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
	public MsPacManCBRengineEX(MsPacManStorageManagerEX storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	public void changeLevel(Integer level) {
		this.level = level;
		this.storageManager.close();
		this.storageManager.setCaseBase(caseBase[level]);
	}
	
	public void setLevel(Integer level) {
		this.level = level;
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
		
		simConfig.addMapping(new Attribute("score",MsPacManDescriptionEX.class), new Interval(15000));
		simConfig.addMapping(new Attribute("time",MsPacManDescriptionEX.class), new Interval(5000));
//		simConfig.addMapping(new Attribute("lives",MsPacManDescriptionEX.class), new Interval(2));
		
		simConfig.addMapping(new Attribute("msPacmanPos",MsPacManDescriptionEX.class), new Interval(25));
		simConfig.addMapping(new Attribute("msPacmanMove",MsPacManDescriptionEX.class), new Equal());
		
		simConfig.addMapping(new Attribute("nearestGhost1",MsPacManDescriptionEX.class), new Interval(50));
		simConfig.addMapping(new Attribute("nearestGhost2",MsPacManDescriptionEX.class), new Interval(50));
		simConfig.addMapping(new Attribute("nearestGhost3",MsPacManDescriptionEX.class), new Interval(50));
		simConfig.addMapping(new Attribute("nearestGhost4",MsPacManDescriptionEX.class), new Interval(50));
		
//		simConfig.addMapping(new Attribute("INKYlair",MsPacManDescriptionEX.class), new Interval(3));
//		simConfig.addMapping(new Attribute("PINKYlair",MsPacManDescriptionEX.class), new Interval(3));
//		simConfig.addMapping(new Attribute("BLINKYlair",MsPacManDescriptionEX.class), new Interval(3));
//		simConfig.addMapping(new Attribute("SUElair",MsPacManDescriptionEX.class), new Interval(3));
		
//		simConfig.addMapping(new Attribute("INKYdir",MsPacManDescriptionEX.class), new Equal());
//		simConfig.addMapping(new Attribute("PINKYdir",MsPacManDescriptionEX.class), new Equal());
//		simConfig.addMapping(new Attribute("BLINKYdir",MsPacManDescriptionEX.class), new Equal());
//		simConfig.addMapping(new Attribute("SUEdir",MsPacManDescriptionEX.class), new Equal());
		
		simConfig.addMapping(new Attribute("nearestEdible1",MsPacManDescriptionEX.class), new Equal());
		simConfig.addMapping(new Attribute("nearestEdible2",MsPacManDescriptionEX.class), new Equal());
		simConfig.addMapping(new Attribute("nearestEdible3",MsPacManDescriptionEX.class), new Equal());
		simConfig.addMapping(new Attribute("nearestEdible4",MsPacManDescriptionEX.class), new Equal());

		simConfig.addMapping(new Attribute("remainingPills",MsPacManDescriptionEX.class), new Interval(3000));
		simConfig.addMapping(new Attribute("nearestPPill",MsPacManDescriptionEX.class), new Equal());
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
		if(caseBase[level].getCases().size() < MAX_CASES) {
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
		//C�CULO DE SIMILITUD 
		//COMPROBACION DE SIMILITUD
		//COMPROBACION DE DIFERENCIA EN LA SOLUCION
		//DESCARTE DE PEOR CASO (P�RDIDA DE HP)
		//COMPROBACION DE SCORES
		//COMPROBACION DE POSICIONES Y EDIBLE
		
		//Tomamos 3 casos
		Collection<RetrievalResult> first = SelectCases.selectTopKRR(eval, MAX_CASES);
		
		Iterator<RetrievalResult> it = first.iterator();
		
		CBRCase[] mostSimilarCases = new CBRCase[] {it.next().get_case(), it.next().get_case(), it.next().get_case()};
		
		//Para saber si creamos caso o no
		createCase[0] = false;
		//Reseteamos el iterador
		it = first.iterator();
		//Calculamos la media entre los 3 para comprobar que son suficientemente v�lidos
		double meanSimilarity = 0, maxSimilarity = 0;
		int maxSimCase = 0, z = 3;
		for(int i = 0; i < MAX_CASES; i++) {
			double similarity = it.next().getEval();
			
			if(similarity > 0)
				meanSimilarity += similarity;
			else
				z--;
			
			maxSimilarity = Math.max(maxSimilarity, similarity);
			
			if(maxSimilarity == similarity) 
				maxSimCase = i;
		}
		
		if(z > 0)
			meanSimilarity /= z;
		else
			meanSimilarity = 0;
		
		//Tomamos movimiento random
		MOVE action;
		action = MOVE.values()[(int)Math.floor(Math.random()*4)];
		
		//Descartamos si no hay similitud suficiente en los casos
		if(meanSimilarity < MIN_SIMILARITY) {
			if(maxSimilarity < MIN_SIMILARITY_TO_DISCARD){
				if(DEBUG) System.out.print("No hay similares: " + meanSimilarity + " - " + maxSimilarity + " - " + action + " \n");
				createCase[0] = true;
				return action;
			}else {				
				MsPacManResultEX result = (MsPacManResultEX) mostSimilarCases[maxSimCase].getResult();
				
				if(result.getScore() > MAX_SCORE_DIFF) {
					if(DEBUG) System.out.print("M�s similar y buena: " + action + " \n");
					MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[maxSimCase].getSolution();
					return solution.getAction();
				}else {
					if(DEBUG) System.out.print("M�s similar no es buena: " + action + " \n");
					return action;
				}
			}
		}		
		//Miramos que las soluciones sean distintas
		if(!checkDiffSolution(mostSimilarCases)) {
			//Si no lo son, devolvemos esa accion
			MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[0].getSolution();
			if(DEBUG) System.out.print("Soluciones iguales: " + action + " \n");
			return solution.getAction();
		}
		
		int x = 0;
		boolean[] discarded = new boolean[] {false, false, false};
		it = first.iterator();
		//Descartamos los que pierden la vida y los de menor similitud
		for(int i = 0; i < MAX_CASES; i++) {
			MsPacManResultEX result = (MsPacManResultEX) mostSimilarCases[i].getResult();
			double similarity = it.next().getEval();
			//Ha perdido vida
			if(result.getLife() > 0 || similarity < MIN_SIMILARITY_TO_DISCARD) {
				discarded[i] = true;
				x++;
			}
		}
		
		//Si la pierden todos, nos quedamos con el de mayor score
		if(x == MAX_CASES) {
			MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[getHighestScore(mostSimilarCases)].getSolution();
			if(DEBUG) System.out.print("Todos descartados, mayor score: " + action + " \n");
			return solution.getAction();
		}
		
		//Si solo queda 1, ese es la solucion
		if(x == MAX_CASES-1) {
			int y;
			for(y = 0; discarded[y]; y++);
			MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[y].getSolution();
			if(DEBUG) System.out.print("�nica soluci�n: " + action + " \n");
			return solution.getAction();
		}
		
		//Comprobamos las diferencias de score con los que quedan (2 o 3)
		int medianScore = 0, numNotDiscarded = 0;
		for(int i = 0; i < MAX_CASES; i++) {
			if(!discarded[i]) {
				MsPacManResultEX result = (MsPacManResultEX) mostSimilarCases[i].getResult();
				medianScore += result.getScore();
				numNotDiscarded++;
			}
		}
		medianScore /= numNotDiscarded;
		
		//Si la diferencia es importante, cogemos el que tenga mayor score
		if(medianScore >= MAX_SCORE_DIFF) {
			MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[getHighestScore(mostSimilarCases, discarded)].getSolution();
			if(DEBUG) System.out.print("Soluci�n con m�s puntuaci�n: " + action + " \n");
			return solution.getAction();
		}
		
		//Si son muy similares, comprobamos edibles y distancias
		if(DEBUG) System.out.print("Puntos: ");
		action = getBestDistanceEdibleAction(mostSimilarCases, discarded);
		if(DEBUG) System.out.print("Soluci�n m�s equilibrada: " + action + " \n");

		return action;
	}
	
	
	private MOVE getBestDistanceEdibleAction(CBRCase[] mostSimilarCases, boolean[] discarded) {
		//Calculamos el mejor en base a un peque�o sistema de puntos relativo en Distancia/Edible
		int bestCase = 0;
		double points, maxPoints = 0;
		MOVE action = MOVE.NEUTRAL;
		//MAX_DISTANCE_GHOST = 80;
		//MAX_DISTANCE_POWERPILL = 60;
		for(int i = 0; i < MAX_CASES; i++) {
			points = 0;
			MsPacManResultEX result = (MsPacManResultEX) mostSimilarCases[i].getResult();
			
			for(int e = 0; e < 4; e++) {
				int dist = result.getGhostDist(e);
				if(result.getGhostEdible(e)){
					//M�s cercan�a da m�s puntos
					points += Math.max(MAX_DISTANCE_GHOST - dist * e, 0);
				}else {
					//M�s lejan�a da m�s puntos
					points -= Math.min(MAX_DISTANCE_GHOST - dist * e, 0);
				}
			}
			
			//A mayor distancia, menos puntos
			points *= MAX_DISTANCE_POWERPILL / result.getNearestPPillDist();
			
			if(DEBUG)  System.out.print(points + " ");
			
			//Selecciona caso con m�s puntos
			maxPoints = Math.max(points, maxPoints);
			if(points == maxPoints)
				bestCase = i;
			
		}
		
		if(DEBUG)  System.out.print("\n");
		
		MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[bestCase].getSolution();
		action = solution.getAction();
		return action;
	}

	private int getHighestScore(CBRCase[] mostSimilarCases, boolean[] discarded) {
		int maxScoreCase = 0, maxScore = 0;
		for(int i = 0; i < MAX_CASES; i++) {
			if(!discarded[i]) {
				MsPacManResultEX result = (MsPacManResultEX) mostSimilarCases[0].getResult();
				maxScore = Math.max(maxScore, result.getScore());
				maxScoreCase = maxScore == result.getScore() ? i : maxScoreCase;
			}
		}
		return maxScoreCase;
	}

	private int getHighestScore(CBRCase[] mostSimilarCases) {
		int maxScoreCase = 0, maxScore = 0;
		for(int i = 0; i < MAX_CASES; i++) {
			MsPacManResultEX result = (MsPacManResultEX) mostSimilarCases[0].getResult();
			maxScore = Math.max(maxScore, result.getScore());
			maxScoreCase = maxScore == result.getScore() ? i : maxScoreCase;
		}
		return maxScoreCase;
	}

	boolean checkDiffSolution(CBRCase[] mostSimilarCases) {
		boolean different = false;
		//Cogemos la primera accion
		MsPacManSolutionEX solution = (MsPacManSolutionEX) mostSimilarCases[0].getSolution();
		MOVE action = solution.getAction();
		
		for(int i = 1; i < MAX_CASES && !different; i++) {
			solution = (MsPacManSolutionEX) mostSimilarCases[i].getSolution();
			different = action != solution.getAction();
		}
		
		return different;
	}

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		MsPacManDescriptionEX newDescription = (MsPacManDescriptionEX) query.getDescription();
		MsPacManResultEX newResult = new MsPacManResultEX();
		MsPacManSolutionEX newSolution = new MsPacManSolutionEX();
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
