package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.EnumMap;
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
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.GameDistance;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.Utils;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManDescription;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacmanCaseBaseFilter;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacmanTreeCaseBase;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsCBRengine implements StandardCBRApplication {

	// Referncia al game para calculos en reutilizacion y funciones de similitud
	Game game;
	
	private String opponent;
    private EnumMap<GHOST, MOVE> ghostActions = new EnumMap<GHOST, MOVE>(GHOST.class);
	private GhostsStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	Collection<RetrievalResult> mostSimilarCases;
	
	final static String TEAM = "grupo06";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"ghosts"+File.separator;
	
	
	public GhostsCBRengine(GhostsStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	public void setGame(Game game) {
		this.game = game;
		configureWithGame();
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new GhostsTreeCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		Attribute attr;
		
		attr = new Attribute("score",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(15000));
		simConfig.setWeight(attr, 0.0);
		
		// PACMAN
		simConfig.addMapping(new Attribute("PacManLastMove",GhostsDescription.class), new Equal());
		attr = new Attribute("PacManToPPill",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("CloseDistance",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("Closeedible",GhostsDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("CloseMove",GhostsDescription.class), new Equal());
		
		attr = new Attribute("CloseDistanceToGhost",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("MidCDistance",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("MidCedible",GhostsDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("MidCMove",GhostsDescription.class), new Equal());
		
		attr = new Attribute("MidCDistanceToGhost",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("MidFDistance",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("MidFedible",GhostsDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("MidFMove",GhostsDescription.class), new Equal());
		
		attr = new Attribute("MidFDistanceToGhost",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		// SUE
		attr = new Attribute("FarDistance",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));
		simConfig.setWeight(attr, 0.0);
		
		attr = new Attribute("Faredible",GhostsDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("FarMove",GhostsDescription.class), new Equal());
		
		attr = new Attribute("FarDistanceToGhost",GhostsDescription.class);
		simConfig.addMapping(attr, new Interval(650));	
		simConfig.setWeight(attr, 0.0);

		simConfig.addMapping(new Attribute("lives",GhostsDescription.class), new Interval(3));
		simConfig.addMapping(new Attribute("level",GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("time",GhostsDescription.class), new Interval(4000));
		
	}
	
	/**
	 * Configura los atributos para los que son necesarios tener el game.
	 * Solo se hace una vez, pero no podemos coger el game en el configure,
	 * asi que hacemos este metodo solo una vez en el getMove
	 */
	void configureWithGame() {
		simConfig.addMapping(new Attribute("PacManNode",GhostsDescription.class), new GameDistance(game, 150));
		simConfig.addMapping(new Attribute("CloseNode",GhostsDescription.class),  new GameDistance(game, 150));
		simConfig.addMapping(new Attribute("MidCNode",GhostsDescription.class),  new GameDistance(game, 150));
		simConfig.addMapping(new Attribute("MidFNode",GhostsDescription.class),  new GameDistance(game, 150));
		simConfig.addMapping(new Attribute("FarNode",GhostsDescription.class),  new GameDistance(game, 150));

		simConfig.addMapping(new Attribute("nearestPPill",MsPacManDescription.class), new GameDistance(game, 150));
		simConfig.addMapping(new Attribute("nearestPill",MsPacManDescription.class), new GameDistance(game, 150));	
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		GhostsCaseBaseFilter filter = new GhostsCaseBaseFilter(query);
		if(caseBase.getCases(filter).isEmpty()) {
			for(GHOST g: GHOST.values()){
				ghostActions.put(g, MOVE.NEUTRAL);
			}
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(filter), query, simConfig);
			
			//Compute reuse
			ghostActions = reuse(eval);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		
		this.storageManager.setSimilarCases(mostSimilarCases);
		this.storageManager.reviseAndRetain(newCase);
		
	}

	private EnumMap<GHOST, MOVE> reuse(Collection<RetrievalResult> eval)
	{
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		if(eval.size() == 0) return moves;
		// This simple implementation uses 3NN
		// Consider using kNNs with majority voting
		mostSimilarCases = SelectCases.selectTopKRR(eval, 3);
		Iterator<RetrievalResult> krrCases =  mostSimilarCases.iterator();
		//caseBase setSimilarcases()
		RetrievalResult res = krrCases.next();
		while(krrCases.hasNext()) {
			RetrievalResult raux = krrCases.next();
			//Comparar si es mejor
			GhostsResult re = (GhostsResult)res.get_case().getResult();
			double reFitness = re.getFitness();
			GhostsResult ra = (GhostsResult)raux.get_case().getResult();
			double raFitness = ra.getFitness();
			// Es mejor y lo suficientemente similar, se guarda
			if(raFitness < reFitness && raux.getEval() > 0.75)  
				res = raux;
		}
		
		CBRCase bestcase = res.get_case();
		GhostsResult result = (GhostsResult) bestcase.getResult();	
		
		//But if not enough similarity or bad case, choose another move randomly
		if(res.getEval() < 0.7 || result.getFitness() > 0) {
			for(GHOST g : GHOST.values()) {
				int index = (int)Math.floor(Math.random() * 4);
				if(MOVE.values()[index] == moves.get(g)) 
					index = (index + 1) % 4;
				moves.put(g, MOVE.values()[index]);
			}
		}
		else {
			// le damos a cada fantasma su accion correspondiente
			MOVE[] actions = ((GhostsSolution)bestcase.getSolution()).getActions();
			GHOST[] sortedGhosts = Utils.getGhostsSortedByDistanceToPacMan(game);
			for(int i = 0; i < actions.length; i++) {
				moves.put(sortedGhosts[i], actions[i]);
			}
		}
		return moves;
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
		
		GHOST[] sortedGhosts = Utils.getGhostsSortedByDistanceToPacMan(game);
		newSolution.setCloseaction(this.ghostActions.get(sortedGhosts[0]));
		newSolution.setMidCaction(this.ghostActions.get(sortedGhosts[1]));
		newSolution.setMidFaction(this.ghostActions.get(sortedGhosts[2]));
		newSolution.setFaraction(this.ghostActions.get(sortedGhosts[3]));

		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	public EnumMap<GHOST, MOVE> getSolution() {
		return this.ghostActions;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();
	}

}