package es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.EnumDistance;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo04.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo04.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo04.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo04.CBRengine.DistanceComparator;
import es.ucm.fdi.ici.graficas.EpisodeData;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private Integer level;
	private Game game;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo04"; 
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	//para actualizar el game
	public void setGame(Game game) {
		this.game = game;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupo04" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+"level"+level+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		Attribute a;
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(a=new Attribute("posPacman",MsPacManDescription.class), new DistanceComparator(game ,500));
		simConfig.setWeight(a, 8.0);
		simConfig.addMapping(a=new Attribute("BLINKYghostPos",MsPacManDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("PINKYghostPos",MsPacManDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("INKYghostPos",MsPacManDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("SUEghostPos",MsPacManDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("BLINKYghostMove",MsPacManDescription.class), new EnumDistance());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("PINKYghostMove",MsPacManDescription.class), new EnumDistance());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("INKYghostMove",MsPacManDescription.class), new EnumDistance());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("SUEghostMove",MsPacManDescription.class), new EnumDistance());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("lastPacmanDirection",MsPacManDescription.class), new EnumDistance());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("BLINKYghostEdible",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 6.0);
		simConfig.addMapping(a=new Attribute("PINKYghostEdible",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 6.0);
		simConfig.addMapping(a=new Attribute("INKYghostEdible",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 6.0);
		simConfig.addMapping(a=new Attribute("SUEghostEdible",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 6.0);
		simConfig.addMapping(a=new Attribute("ppillsPos1",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 2.0);
		simConfig.addMapping(a=new Attribute("ppillsPos2",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 2.0);
		simConfig.addMapping(a=new Attribute("ppillsPos3",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 2.0);
		simConfig.addMapping(a=new Attribute("ppillsPos4",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 2.0);
		simConfig.addMapping(a=new Attribute("closestPill",MsPacManDescription.class), new DistanceComparator(game ,500));
		simConfig.setWeight(a, 20.0);
		simConfig.addMapping(a=new Attribute("closestPillDist",MsPacManDescription.class), new Interval(40));
		simConfig.setWeight(a, 20.0);
		simConfig.addMapping(a=new Attribute("nextJunctionPills1",MsPacManDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.5);
		simConfig.addMapping(a=new Attribute("nextJunctionPills2",MsPacManDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.5);
		simConfig.addMapping(a=new Attribute("nextJunctionPills3",MsPacManDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.5);
		simConfig.addMapping(a=new Attribute("nextJunctionPills4",MsPacManDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.5);
		simConfig.addMapping(a=new Attribute("closestPPillDist",MsPacManDescription.class), new Interval(400));
		simConfig.setWeight(a, 6.0);
		simConfig.addMapping(a=new Attribute("danger",MsPacManDescription.class), new Interval(0.1));
		simConfig.setWeight(a, 8.0);
		simConfig.addMapping(a=new Attribute("health",MsPacManDescription.class), new Equal());
		simConfig.setWeight(a, 0.2);
		simConfig.addMapping(a=new Attribute("score",MsPacManDescription.class), new Interval(1000));
		simConfig.setWeight(a, 0.1);
		simConfig.addMapping(a=new Attribute("time",MsPacManDescription.class), new Interval(10000));
		simConfig.setWeight(a, 0.1);
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			//si no hay ningun caso guardado, ponemos un movimiento aleatorio que no sea NEUTRAL
			int numPosMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()).length;
			int index = (int)Math.floor(Math.random()* numPosMoves);
			int cont=0;
			do {
				action = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())[index];
				index= (index+1)%numPosMoves;
				cont++;
				
			}while(cont < numPosMoves && action == MOVE.NEUTRAL);
			this.action = MOVE.values()[index];
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
		}
		//Put caseBase Size
		Map<String, Integer> sizeMap = EpisodeData.getCaseBaseSizeMap();
		sizeMap.put(TEAM, caseBase.getCases().size());
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, simConfig);
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		
		//numero de vecinos a considerar
		int Knn=5;
		RetrievalResult[] rResults = new RetrievalResult [Knn];
		CBRCase[] similarCases = new CBRCase [Knn];
		double[] similarities = new double [Knn];
		MsPacManResult [] msPacManResult = new MsPacManResult[Knn];
		MsPacManSolution [] msPacManSolutions = new MsPacManSolution[Knn];
		Collection<RetrievalResult> neighbours = SelectCases.selectTopKRR(eval, Knn); 
		for(int i = 0; i <Knn; ++i ) {
			if(neighbours.isEmpty())
				rResults[i] = rResults[i-1];
			else {
				rResults[i] = neighbours.iterator().next();
				neighbours.remove(rResults[i]);
			}
			
			similarCases[i] = rResults[i].get_case();
			similarities[i] = rResults[i].getEval();
				
			//Meter avg
			Map<String, Double> map = EpisodeData.getAvgMap();
			if(!map.containsKey(TEAM)) {
				map.put(TEAM, 0.5);
			}
			double d=(map.get(TEAM)*0.95)+(similarities[i] *0.05);
			map.put(TEAM, d);
			//
			msPacManResult[i] = (MsPacManResult) similarCases[i].getResult();
			msPacManSolutions[i] = (MsPacManSolution) similarCases[i].getSolution();
		}

		float notSimilarity = 0.4f;
		//Si no se parece o consigues muy pocos puntos, da igual
		if((similarities[0]<notSimilarity) /*|| (msPacManResult[0].getScore()<10)*/) {
			List<MOVE> movesToAvoid = new ArrayList<MOVE>();
			//comprobar si hay algun movimiento a evitar(te han comido haciendo ese movimiento)
			for(int i = 0; i < Knn ; ++i) {
				if(msPacManResult[i].getScore() < 0) {
					movesToAvoid.add(msPacManSolutions[i].action);
				}					
			}
			int numPosMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()).length;
			int index = (int)Math.floor(Math.random()* numPosMoves);
			int cont=0;
			do {
				action = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())[index];
				index= (index+1)%numPosMoves;
				cont++;
				
			}while(movesToAvoid.contains(action) && cont < numPosMoves && action == MOVE.NEUTRAL);

			//quitamos NEUTRAL
			if(action == MOVE.NEUTRAL)
				action = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())[0];
			return action;
		}

		//de los Knn vecinos mas similares, buscar el movimiento con el que se consigue mas puntuacion
		//si deja de parecerse, dejar de buscar
		int[] bestMoves = new int [5];
		for(int i = 0; i < Knn; ++i) {
			if((similarities[i]<notSimilarity)){
				break;
			}
			bestMoves[msPacManSolutions[i].action.ordinal()] += msPacManResult[i].getScore();
		}
		
		//encontrar el indice del movimiento que nos da mas puntuacion
		int index = 0;
		int max = 0;
		for(int i = 0; i < bestMoves.length; ++i) {
			if(bestMoves[i] > max) {
				max = bestMoves[i];
				index = i; 
			}
		}
		
		// Si tu mejor movimiento no consigue puntos, busca otro
		if(max > 0) {
			action = MOVE.values()[index];
		} else 
			action = MOVE.values()[(int)Math.floor(Math.random()* 4)];
		// Hacemos que no pueda ser Neutral el movimiento
		if(action == MOVE.NEUTRAL)
			action = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())[0];
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
		this.storageManager.close( simConfig);
		this.caseBase.close();
	}

}
