package es.ucm.fdi.ici.c2122.practica5.grupo02.mspacman;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica5.grupo02.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo02.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo02.CBRengine.CustomPlainTextConnector;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {
	
	private MOVE action;
	private String opponent;
	private double similarityCase;
	private MsPacManStorageManager storageManager;

	private double MAX_SIMILARITY = 0.97;
	private double PROMISING_SIMILARITY = 0.9;
	private double AVARAGE_SIMILARITY = 0.85;
	private double MIN_SIMILARITY = 0.66;
	private int MIN_SCORE_INTERSECTIONS = 250;
	
	private int DISTANCE_PM_DANGER = 60;
		
	NNConfig simConfig;
	CBRCaseBase caseBase;
	CustomPlainTextConnector connector;
	
	private final static String MSPACMAN_FOLDER = "mspacman" + File.separator;

	public MsPacManCBRengine(MsPacManStorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent.substring(opponent.length() - 6, opponent.length()) ;
	}

	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();

		connector.initFromXMLfile(FileIO.findFile(GameConstants.CONNECTOR_FILE_PATH + MSPACMAN_FOLDER + "plaintextconfig.xml"));
		connector.setCaseBaseFile(GameConstants.CASE_BASE_PATH + "mspacman" + File.separator, opponent + ".csv");

		this.storageManager.setCaseBase(caseBase);

		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("score", MsPacManDescription.class), new Interval(1500));
		simConfig.addMapping(new Attribute("time", MsPacManDescription.class), new Interval(4000)); //timeLimit 4000
		simConfig.addMapping(new Attribute("distanceNearestPPill", MsPacManDescription.class), new Interval(900));
		simConfig.addMapping(new Attribute("distanceNearestGhost", MsPacManDescription.class), new Interval(500));
		simConfig.addMapping(new Attribute("nearestNodeGhost", MsPacManDescription.class), new Threshold(50)); //Si esta por debajo la diferencia, se consideran similares
		simConfig.addMapping(new Attribute("edibleGhost", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("livesLeft", MsPacManDescription.class), new Interval(3));
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if (caseBase.getCases().isEmpty()) {
			this.action = MOVE.values()[(int)Math.floor(Math.random()*4)];
			similarityCase = 0;
		} else {
			// Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query,
					simConfig);
			// Compute reuse
			this.action = reuse(query, eval);
		}

		// Compute revise & retain
		CBRCase newCase = createNewCase(query);
		if(similarityCase < MAX_SIMILARITY) //Si no es lo suficientemente similar, lo guardamos
			this.storageManager.reviseAndRetain(newCase);

	}

	private MOVE reuse(CBRQuery query_, Collection<RetrievalResult> eval) {
		int nCases = GameConstants.NUM_CASES;
		// Selecciona el/los mas prioritarios
		Collection<RetrievalResult> collec = SelectCases.selectTopKRR(eval, nCases);
		Iterator<RetrievalResult> it = collec.iterator();

		double similarity = 0.0;
		int[] moves = new int [5];
		
		for (int i = 0; i < collec.size(); i++) {
			RetrievalResult cases = it.next();
			CBRCase mostSimilarCase = cases.get_case();
			similarity += cases.getEval();

			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
			
			moves[solution.getAction().ordinal()]++;
		}
		

		//AVERAGE 
		if(nCases != collec.size()) similarity = similarity / collec.size();
		else similarity = similarity / nCases; 
		
		//En principio esto
		similarityCase = similarity;
		//Si su similitud pondereada pasa del limite
		if(similarity >= MIN_SIMILARITY) {
			//Si existe algun caso cuya similitud sea muy alta, lo vamos a considerar antes
			// de hacer la votacion mayoritaria
			it = collec.iterator();
			RetrievalResult cases = it.next();
			if(cases.getEval() >= PROMISING_SIMILARITY) {
				CBRCase mostSimilarCase = cases.get_case();
				MsPacManSolution sol = (MsPacManSolution) mostSimilarCase.getSolution();
				
				if(GameConstants.DEBUG) System.out.print("1 " + sol.getAction()+ "\n");
				return sol.getAction();
			}
			
			//VOTACION MAYORITARIA, en caso de que la media sea prometedora
			if(similarity >= AVARAGE_SIMILARITY) {
				int index_ = 4; //Neutral
				for(int i = 0 ; i < 5; i++)
					if(moves[i] > moves[index_]) index_ = i; //elegimos por votacion mayoritara
				
				if(GameConstants.DEBUG) System.out.print("2 " + MOVE.values()[index_] + "\n");
				return MOVE.values()[index_]; //La acción más común
			}
			
			//Vamos a comprobar en la query si MsPacMan tiene un fantasma cerca
			MsPacManDescription des = (MsPacManDescription) query_.getDescription();
			if(des.getDistanceNearestGhost() < DISTANCE_PM_DANGER) {
				//El pcMan esta en peligro
				//Buscamos en los casos mas similares si alguno tiene una Power pill cercana
				int[] distancesPowerPills = distancesPowerPill(collec);
				
				int index = getMin(distancesPowerPills);
				
				//Devuelve el movimiento del caso cuya powerPill estaba mas cerca
				if(index != -1) {
					if(GameConstants.DEBUG) System.out.print("3 " + getIndexMove(collec, index, true)+ "\n");
					return getIndexMove(collec, index, true);
				}
				//Si no hay power Pill, va por la opcion cuya perdida de vida sea la menor
				
			}
			//De lo contrario, comenzamos a descartar en funcion de los que han perdido vidas.
			//Lives almacena la diferencias de vidas con cada caso con respecto a las actuales.
			int[] lives = discardLivesLost(collec);
			int index = getMin(lives);
			//Devolvemos aquella accion del indice que caso cuya perdida de vidas a sido la menor
			if(GameConstants.DEBUG) System.out.print("4 " + getIndexMove(collec, index, true)+ "\n");
			return getIndexMove(collec, index, true);
			
		}else {
			//MOVERME ALEATORIAMENTE
			MOVE auxAction = MOVE.values()[(int)Math.floor(Math.random()*4)];
			
			it = collec.iterator();
			RetrievalResult case_ = it.next();
			MsPacManResult result = (MsPacManResult) case_.get_case().getResult();
			MsPacManSolution sol = (MsPacManSolution) case_.get_case().getSolution();
			similarity = case_.getEval();
			//Si el caso más similar, al menos optiene una buena puntuación, se devuelve
			similarityCase = similarity;
			if(result.score > MIN_SCORE_INTERSECTIONS) {
				if(GameConstants.DEBUG) System.out.print("5 " + sol.getAction() + "\n");
				return sol.getAction(); //La solucion del caso más similar
			}else { //Sino, se devuelve una dirección random
				if(GameConstants.DEBUG) System.out.print("6 " + auxAction + "\n");
				return auxAction;
			}
		}
	}
	
	MOVE getIndexMove(Collection<RetrievalResult> collec, int index, boolean saveSimililarity) {
		Iterator<RetrievalResult> it = collec.iterator();
		//Devolvemos aquella accion del index
		for(int i = 0; i < index ; i++) it.next();
		RetrievalResult case_ = it.next();
		CBRCase c = case_.get_case();
		if(saveSimililarity) similarityCase = case_.getEval();
		MsPacManSolution sol = (MsPacManSolution) c.getSolution();
		return sol.getAction();
	}
	
	int getMin(int[] array) {
		int minValue = Integer.MAX_VALUE;
		int index = -1;
		for(int i = 0; i < array.length; i++) {
			//Nos guardamos el indice de cuyo valor sea el que menor vidas haya perdido
			if(array[i] < minValue) {
				minValue = array[i];
				index = i;
			}
		}
		
		return index;
	}
	
	int[] discardLivesLost(Collection<RetrievalResult> collec) {
		Iterator<RetrievalResult> it = collec.iterator();
		
		int[] indexNotLost = new int[5];
		
		for (int i = 0; i < collec.size(); i++) {
			RetrievalResult caso = it.next();
			CBRCase mostSimilarCase = caso.get_case();

			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			
			indexNotLost[i] = result.getLives();
		}
		
		return indexNotLost;
	}
	
	int[] distancesPowerPill(Collection<RetrievalResult> collec) {
		Iterator<RetrievalResult> it = collec.iterator();
		
		int[] distances = new int[5];
		
		for (int i = 0; i < collec.size(); i++) {
			RetrievalResult caso = it.next();
			CBRCase mostSimilarCase = caso.get_case();

			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			
			MsPacManDescription des = (MsPacManDescription) mostSimilarCase.getDescription();
			
			int distance =  des.getDistanceNearestPPill();
			
			if(distance > 0) distances[i] = distance; 
			else distances[i] = Integer.MAX_VALUE;
		}
		
		return distances;
	}
	
	
	
	/*----------------------------------------------------------------------------------------------------
	/----------------------------------------------------------------------------------------------------
	/----------------------------------------------------------------------------------------------------*/
	/**
	 * Creates a new case using the query as description, storing the action into
	 * the solution and setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
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

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}
