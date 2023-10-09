package es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman;

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
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.*;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManCBRengine implements StandardCBRApplication {

	// Referncia al game para calculos en reutilizacion y funciones de similitud
	Game game;
	
	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	Collection<RetrievalResult> mostSimilarCases;

	final static String TEAM = "grupo06";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"mspacman"+File.separator;
	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
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
		caseBase = new MsPacmanTreeCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		Attribute attr;
		
		attr = new Attribute("score",MsPacManDescription.class);
		simConfig.addMapping(attr, new Difference(4000));
		simConfig.setWeight(attr, 0.0);
				
		attr = new Attribute("CloseEdible",MsPacManDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("CloseMove",MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("CloseDistance",MsPacManDescription.class), new Difference(50));
		
		attr = new Attribute("MidEdible",MsPacManDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("MidMove",MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("MidDistance",MsPacManDescription.class), new Difference(50));
		
		attr = new Attribute("FarEdible",MsPacManDescription.class);
		simConfig.addMapping(attr, new Equal());
		simConfig.setWeight(attr, 5.0);
		simConfig.addMapping(new Attribute("FarMove",MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("FarDistance",MsPacManDescription.class), new Difference(50));
		
		simConfig.addMapping(new Attribute("lives",MsPacManDescription.class), new Difference(3));
		attr = new Attribute("time",MsPacManDescription.class);
		simConfig.addMapping(attr, new Interval(4000));
		simConfig.setWeight(attr, 0.25);
		simConfig.addMapping(new Attribute("level",MsPacManDescription.class), new Equal());
		
	}
	
	/**
	 * Configura los atributos para los que son necesarios tener el game.
	 * Solo se hace una vez, pero no podemos coger el game en el configure,
	 * asi que hacemos este metodo solo una vez en el getMove
	 */
	public void configureWithGame() {
		Attribute attr;
		
		attr = new Attribute("score",MsPacManDescription.class);
		simConfig.addMapping(attr, new Interval(15000));
		simConfig.setWeight(attr, 0.0);
		
		simConfig.addMapping(new Attribute("nearestPPill",MsPacManDescription.class), new GameDistance(game, 75));
		simConfig.addMapping(new Attribute("nearestPill",MsPacManDescription.class), new GameDistance(game, 75));
		
		simConfig.addMapping(new Attribute("MsPacManNode",MsPacManDescription.class), new GameDistance(game, 75));
		simConfig.addMapping(new Attribute("CloseNode",MsPacManDescription.class),  new GameDistance(game, 75));
		simConfig.addMapping(new Attribute("MidNode",MsPacManDescription.class),  new GameDistance(game, 75));
		simConfig.addMapping(new Attribute("FarNode",MsPacManDescription.class),  new GameDistance(game, 75));

	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		MsPacmanCaseBaseFilter filter = new MsPacmanCaseBaseFilter(query);
		if(caseBase.getCases(filter).isEmpty()) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(filter), query, simConfig);
			
			//Compute reuse
			this.action = reuse(eval);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.setSimilarCases(mostSimilarCases);
		this.storageManager.reviseAndRetain(newCase);
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		if(eval.size() == 0) return MOVE.NEUTRAL;
		// This simple implementation uses 3NN
		// Consider using kNNs with majority voting
		mostSimilarCases = SelectCases.selectTopKRR(eval, 3);
		Iterator<RetrievalResult> krrCases =  mostSimilarCases.iterator();
		
		RetrievalResult res = krrCases.next();
		while(krrCases.hasNext()) {
			RetrievalResult raux = krrCases.next();
			//Comparar si es mejor
			MsPacManResult re = (MsPacManResult)res.get_case().getResult();
			double reFitness = re.getFitness();
			MsPacManResult ra = (MsPacManResult)raux.get_case().getResult();
			double raFitness = ra.getFitness();
			// Es mejor y lo suficientemente similar, se guarda
			if(raFitness > reFitness && raux.getEval() > 0.75)  
				res = raux;
		}
		
		CBRCase bestcase = res.get_case();
		MsPacManResult result = (MsPacManResult) bestcase.getResult();
		MsPacManSolution solution = (MsPacManSolution) bestcase.getSolution();
		
		//Here, it simply takes the action of the 1NN
		MOVE action = solution.getAction();
		
		//But if not enough similarity or bad case, choose another move randomly
		if(res.getEval() < 0.7 || result.getFitness() > 0) {
			int index = (int)Math.floor(Math.random() * 4);
			if(MOVE.values()[index] == action) 
				index = (index + 1) % 4;
			action = MOVE.values()[index];
		}
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
		this.storageManager.close();
		this.caseBase.close();
	}

}
