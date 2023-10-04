package es.ucm.fdi.ici.c2122.practica5.grupo05.mspacman;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;
	private MsPacManStorageManager storageManagerSpecific;


	CustomPlainTextConnector connectorSpecific;
	CustomPlainTextConnector connectorBase;
	MsPacManCachedLinearCaseBase caseBase;
	CBRCaseBase caseSpecific;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo05";  //Cuidado!! poner el grupo aqu�
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;
	final static String CASE_SPECIFIC_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator+"specific"+File.separator;
	
	private Random rnd = new Random();
	private Game game;
	
	public MsPacManCBRengine(MsPacManStorageManager storageManager, MsPacManStorageManager storageManagerSpecific)
	{
		this.storageManager = storageManager;
		this.storageManagerSpecific = storageManagerSpecific;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		
		connectorBase = new CustomPlainTextConnector();
		//connectorSpecific = new CustomPlainTextConnector();
		
		caseBase = new MsPacManCachedLinearCaseBase(); 
		//caseSpecific = new MsPacManCachedLinearCaseBase();
		
		connectorBase.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		//connectorSpecific.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connectorBase.setCaseBaseFile(CASE_BASE_PATH,"base" +".csv");
		//connectorSpecific.setCaseBaseFile(CASE_SPECIFIC_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		//this.storageManagerSpecific.setCaseBase(caseSpecific);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		Attribute a = new Attribute("distPillUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distPillDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distPillLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distPillRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);

		
		a = new Attribute("distPowerPillUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(15)); simConfig.setWeight(a, (double) 1);
		a = new Attribute("distPowerPillDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(15)); simConfig.setWeight(a, (double) 1);
		a = new Attribute("distPowerPillLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(15)); simConfig.setWeight(a, (double) 1);
		a = new Attribute("distPowerPillRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(15)); simConfig.setWeight(a, (double) 1);
		
		/*
		simConfig.addMapping(new Attribute("distPowerPillUp",MsPacManDescription.class), new Interval(15));
		simConfig.addMapping(new Attribute("distPowerPillDown",MsPacManDescription.class), new Interval(15));
		simConfig.addMapping(new Attribute("distPowerPillLeft",MsPacManDescription.class), new Interval(15));
		simConfig.addMapping(new Attribute("distPowerPillRight",MsPacManDescription.class), new Interval(15));
		*/
		
		a = new Attribute("distEdibleUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		a = new Attribute("distEdibleDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		a = new Attribute("distEdibleLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		a = new Attribute("distEdibleRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		
		/*
		simConfig.addMapping(new Attribute("distEdibleUp",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("distEdibleDown",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("distEdibleLeft",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("distEdibleRight",MsPacManDescription.class), new Interval(10));
		*/
		
		a = new Attribute("distChasingUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		a = new Attribute("distChasingDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		a = new Attribute("distChasingLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		a = new Attribute("distChasingRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(10)); simConfig.setWeight(a, (double) 5);
		
		/*
		simConfig.addMapping(new Attribute("distChasingUp",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("distChasingDown",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("distChasingLeft",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("distChasingRight",MsPacManDescription.class), new Interval(10));
		*/
		
		a = new Attribute("timeEdibleUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(3)); simConfig.setWeight(a, (double) 3);
		a = new Attribute("timeEdibleDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(3)); simConfig.setWeight(a, (double) 3);
		a = new Attribute("timeEdibleLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(3)); simConfig.setWeight(a, (double) 3);
		a = new Attribute("timeEdibleRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(3)); simConfig.setWeight(a, (double) 3);
		
		/*
		simConfig.addMapping(new Attribute("timeEdibleUp",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("timeEdibleDown",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("timeEdibleLeft",MsPacManDescription.class), new Interval(10));
		simConfig.addMapping(new Attribute("timeEdibleRight",MsPacManDescription.class), new Interval(10));
		*/
		
		a = new Attribute("distJunctionUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distJunctionDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distJunctionLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distJunctionRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		
		/*
		
		simConfig.addMapping(new Attribute("distJunctionUp",MsPacManDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("distJunctionDown",MsPacManDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("distJunctionLeft",MsPacManDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("distJunctionRight",MsPacManDescription.class), new Interval(5));
		*/
		
		a = new Attribute("distJunction_ChasingUp",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distJunction_ChasingDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distJunction_ChasingLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		a = new Attribute("distJunction_ChasingRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(5)); simConfig.setWeight(a, (double) 2);
		
		/*
		
		simConfig.addMapping(new Attribute("distJunction_ChasingUp",MsPacManDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("distJunction_ChasingDown",MsPacManDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("distJunction_ChasingLeft",MsPacManDescription.class), new Interval(5));
		simConfig.addMapping(new Attribute("distJunction_ChasingRight",MsPacManDescription.class), new Interval(5));
		*/
		
		a = new Attribute("lastMoveGhostUp",MsPacManDescription.class);
		simConfig.addMapping(a,new Equal()); simConfig.setWeight(a, (double) 4);
		a = new Attribute("lastMoveGhostDown",MsPacManDescription.class);
		simConfig.addMapping(a, new Equal()); simConfig.setWeight(a, (double) 4);
		a = new Attribute("lastMoveGhostLeft",MsPacManDescription.class);
		simConfig.addMapping(a, new Equal()); simConfig.setWeight(a, (double) 4);
		a = new Attribute("lastMoveGhostRight",MsPacManDescription.class);
		simConfig.addMapping(a, new Equal()); simConfig.setWeight(a, (double) 4);
		
		/*
		simConfig.addMapping(new Attribute("lastMoveGhostUp",MsPacManDescription.class),  new Equal()); //o interval 4?
		simConfig.addMapping(new Attribute("lastMoveGhostDown",MsPacManDescription.class),  new Equal());
		simConfig.addMapping(new Attribute("lastMoveGhostLeft",MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("lastMoveGhostRight",MsPacManDescription.class),  new Equal());
		*/
		//simConfig.addMapping(new Attribute("edibleGhost",MsPacManDescription.class), new Equal());
		
		a = new Attribute("score",MsPacManDescription.class);
		simConfig.addMapping(a,new Interval(15000)); simConfig.setWeight(a, (double) 1);
		
		//simConfig.addMapping(new Attribute("score",MsPacManDescription.class), new Interval(15000));
		
		a = new Attribute("time",MsPacManDescription.class);
		simConfig.addMapping(a,new Interval(15000)); simConfig.setWeight(a, (double) 1);
		
		//simConfig.addMapping(new Attribute("time",MsPacManDescription.class), new Interval(3));
		
		a = new Attribute("lastMoveMsPacman",MsPacManDescription.class);
		simConfig.addMapping(a, new Equal()); simConfig.setWeight(a, (double) 5);
		
		//simConfig.addMapping(new Attribute("lastMoveMsPacman",MsPacManDescription.class),  new Equal());
		
		a = new Attribute("level",MsPacManDescription.class);
		simConfig.addMapping(a, new Equal()); simConfig.setWeight(a, (double)2);
		
		//simConfig.addMapping(new Attribute("level",MsPacManDescription.class),  new Equal());
		
		a = new Attribute("numEdible",MsPacManDescription.class);
		simConfig.addMapping(a, new Interval(1)); simConfig.setWeight(a, (double)2);
		
		//simConfig.addMapping(new Attribute("numEdible",MsPacManDescription.class),  new Interval(1));
		}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connectorBase);
		//caseSpecific.init(connectorSpecific);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty() /*&& caseSpecific.getCases().isEmpty()*/) {
			//this.action = this.getPosibleRandomMove();
			MsPacManBasic ms = new MsPacManBasic();
			this.action =Math.random() > 0.5 ? ms.getMove(this.game.copy(), System.currentTimeMillis() +40): getPosibleRandomMove();
		}
		/*
		else if(caseSpecific.getCases().isEmpty()) {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);

			
		}*/
		else
		{
			MsPacManDescription description = (MsPacManDescription) query.getDescription();
			//Compute NN
			Collection<RetrievalResult> eval;
			if (description.getNumEdible()>0)
				 eval = NNScoringMethod.evaluateSimilarity(caseBase.getEdibleCases(), query, simConfig);
			else
				eval = NNScoringMethod.evaluateSimilarity(caseBase.getChasingCases(), query, simConfig);
			
			for(RetrievalResult e : eval) {
				e.getEval();
			}
			//Compute reusess
			this.action = reuse(eval);
			
		
		}
		//Compute revise & retain
		CBRCase newCase = createNewCase(query, caseBase,storageManager);
		this.storageManager.reviseAndRetain(newCase);
		//CBRCase newCaseS = createNewCase(query, caseSpecific, storageManagerSpecific);
		//this.storageManagerSpecific.reviseAndRetain(newCaseS);
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		/*// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();

		MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
		MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
		
		//Now compute a solution for the query
		
		//Here, it simply takes the action of the 1NN
		MOVE action = solution.getAction();
		
		//But if not enough similarity or bad case, choose another move randomly
		if((similarity<0.7)||(result.getScore()<100)) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==action) 
				index= (index+1)%4;
			action = MOVE.values()[index];
		}*/
		
		// 3NN 
		int n=3;
		// Consider using kNNs with majority voting
		Collection<RetrievalResult> cases = SelectCases.selectTopKRR(eval, 5);
		List<RetrievalResult> l = new ArrayList<RetrievalResult>(cases);
		List<CBRCase> listSim = new ArrayList<CBRCase>();
		Collection<CBRCase> listBadExample = new ArrayList<CBRCase>();
		
		double  peso=0;
		CBRCase finalCase = null;
		for(RetrievalResult r : l) {
			CBRCase mostSimilarCase = r.get_case();
			double similarity = r.getEval();
			
			
			MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
			
			
			if(similarity>0.8 && result.getScore()>0)
			{
				listSim.add(r.get_case());
			}
			else if(result.getScore()<0)
				listBadExample.add(r.get_case());
				
				/*
				if(peso <similarity + result.getScore()) {
					peso = similarity + result.getScore();
					finalCase = mostSimilarCase;
				}
			*/
				
		}
		
		caseBase.forgetCases(listBadExample);
		//CBRCase mostSimilarCase = l.get(0).get_case();
		//CBRCase secondmostSimilarCase = l.get(1).get_case();
		//CBRCase thirdmostSimilarCase = l.get(2).get_case();
		///añadir mas cosas segun veamos
		
		//double similarity1 = l.get(0).getEval();
	/*
		if(similarity1<0.7) //no muy bien mov
			this.action = MaxActionSelector.findAction();
		else if(result.getScore()<0) //This was a bad case, ask actionSelector for another one.
			this.action = actionSelector.findAnotherAction(solution.getAction());
		*/
		if(listSim.isEmpty()){
			MsPacManBasic ms = new MsPacManBasic();
			return Math.random() > 0.5 ? ms.getMove(this.game.copy(), System.currentTimeMillis() +40): getPosibleRandomMove();
	
		}
		
		/*
		if(finalCase==null) {
			MsPacManBasic ms = new MsPacManBasic();
			return Math.random() > 0.5 ? ms.getMove(this.game.copy(), System.currentTimeMillis() +40): getPosibleRandomMove();
		}
		*/
		//MsPacManSolution sol = (MsPacManSolution) finalCase.getSolution();
		//return sol.getAction();
		
		return selectMostRepeated(listSim);
	}
	
	
	private MOVE selectMostRepeated(List<CBRCase> listSim) {
		
		Map <MOVE, Integer> mapMove= new HashMap<MOVE, Integer>(); 
		for(MOVE m: MOVE.values()) mapMove.put(m, 0);
		
		MsPacManSolution solution;
		for (CBRCase c : listSim) {
			solution = (MsPacManSolution) c.getSolution();
			mapMove.put(solution.getAction(), mapMove.get(solution.getAction())+1);}
		
		return Collections.max(mapMove.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
	}

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query, CBRCaseBase caseB,  MsPacManStorageManager sm) {
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
		int newId = caseB.getCases().size();
		newId+= sm.getPendingCases();
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
		//this.storageManagerSpecific.close();
		this.caseBase.close();
		//this.caseSpecific.close();
	}
	
	private MOVE getPosibleRandomMove() {
		MOVE[] m = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		return m[rnd.nextInt(m.length)];
	}
	
	public void setGame(Game g) {
		game = g;
	}
}
