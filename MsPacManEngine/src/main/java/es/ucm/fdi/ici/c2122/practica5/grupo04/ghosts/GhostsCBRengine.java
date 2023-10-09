package es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsCBRengine implements StandardCBRApplication {

	private String opponent;
	private Integer level;
	private Game game;
	private MOVE action;
	private GhostsStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo04"; 
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	
	public GhostsCBRengine(GhostsStorageManager storageManager)
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
		simConfig.addMapping(a=new Attribute("pacmanPos",GhostsDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 8.0);
		simConfig.addMapping(a=new Attribute("ghostPos",GhostsDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 8.0);
		simConfig.addMapping(a=new Attribute("ghostMove",GhostsDescription.class), new EnumDistance());
		simConfig.setWeight(a, 2.0);
		simConfig.addMapping(a=new Attribute("lastPacmanMove",GhostsDescription.class), new EnumDistance());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ghostEdible",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 5.0);
		simConfig.addMapping(a=new Attribute("ghostPos1",GhostsDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ghostMove1",GhostsDescription.class), new EnumDistance());
		simConfig.setWeight(a, 0.6);
		simConfig.addMapping(a=new Attribute("ghostEdible1",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("ghostPos2",GhostsDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ghostMove2",GhostsDescription.class), new EnumDistance());
		simConfig.setWeight(a, 0.6);
		simConfig.addMapping(a=new Attribute("ghostEdible2",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("ghostPos3",GhostsDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ghostMove3",GhostsDescription.class), new EnumDistance());
		simConfig.setWeight(a, 0.6);
		simConfig.addMapping(a=new Attribute("ghostEdible3",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("ppillsPos1",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ppillsPos2",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ppillsPos3",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("ppillsPos4",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 1.0);
		simConfig.addMapping(a=new Attribute("closestPill",GhostsDescription.class), new DistanceComparator(game ,1000));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("closestPillDist",GhostsDescription.class), new Interval(40));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("nextJunctionPills1",GhostsDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.3);
		simConfig.addMapping(a=new Attribute("nextJunctionPills2",GhostsDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.3);
		simConfig.addMapping(a=new Attribute("nextJunctionPills3",GhostsDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.3);
		simConfig.addMapping(a=new Attribute("nextJunctionPills4",GhostsDescription.class), new Interval(3));
		simConfig.setWeight(a, 0.3);
		simConfig.addMapping(a=new Attribute("closestPPillDist",GhostsDescription.class), new Interval(400));
		simConfig.setWeight(a, 3.0);
		simConfig.addMapping(a=new Attribute("danger",GhostsDescription.class), new Interval(0.1));
		simConfig.setWeight(a, 4.0);
		simConfig.addMapping(a=new Attribute("health",GhostsDescription.class), new Equal());
		simConfig.setWeight(a, 0.53);
		simConfig.addMapping(a=new Attribute("score",GhostsDescription.class), new Interval(1000));
		simConfig.setWeight(a, 0.55);
		simConfig.addMapping(a=new Attribute("time",GhostsDescription.class), new Interval(1000));
		simConfig.setWeight(a, 0.3);
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
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
		}
		
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
		GhostsResult [] ghostsResult = new GhostsResult[Knn];
		GhostsSolution [] ghostsSolutions = new GhostsSolution[Knn];
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
			ghostsResult[i] = (GhostsResult) similarCases[i].getResult();
			ghostsSolutions[i] = (GhostsSolution) similarCases[i].getSolution();
		}

		float notSimilarity = 0.4f;
		//Si no se parece o consigues muy pocos puntos, da igual
		if((similarities[0]<notSimilarity) || (ghostsResult[0].getScore()<10)) {
			//comprobar si hay algun movimiento a evitar(te han comido haciendo ese movimiento)
			List<MOVE> movesToAvoid = new ArrayList<MOVE>();
			for(int i = 0; i < Knn ; ++i) {
				if(ghostsResult[i].getScore() < 0) {
					movesToAvoid.add(ghostsSolutions[i].action);
				}					
			}
			int index = (int)Math.floor(Math.random()*4);
			int cont=0;
			do {
				if(MOVE.values()[index]==action) 
					index= (index+1)%4;
				action = MOVE.values()[index];
				cont++;
			}while(movesToAvoid.contains(action) && cont < 4);
			return action;
		}

		//de los 3 vecinos mas similares, buscar el movimiento con el que se consigue mas puntuacion
		//si deja de parecerse, dejar de buscar
		int[] bestMoves = new int [5];
		for(int i = 0; i < Knn; ++i) {
			if((similarities[i]<notSimilarity) ){
				break;
			}
			bestMoves[ghostsSolutions[i].action.ordinal()] += ghostsResult[i].getScore();
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
		
		action = MOVE.values()[index];		
		return action;
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
