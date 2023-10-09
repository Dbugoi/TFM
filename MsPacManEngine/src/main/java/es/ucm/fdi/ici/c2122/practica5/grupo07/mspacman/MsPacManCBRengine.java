package es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman;

import java.io.File;
import java.util.ArrayList;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ArrayInterval;
import es.ucm.fdi.ici.c2122.practica5.grupo07.DangerSimilarityFunction;
import es.ucm.fdi.ici.c2122.practica5.grupo07.LastMoveSimilarityFunction;
import es.ucm.fdi.ici.c2122.practica5.grupo07.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo07.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo07.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo07";  //Cuidado!! poner el grupo aquí
	
											///src/main/java/es/ucm/fdi/ici/c2122/practica5/grupo07/mspacman/plaintextconfig.xml
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new PacmanCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		//connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		connector.setCaseBaseFile(CASE_BASE_PATH, "general"+".csv");
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		Attribute score = new Attribute("score",MsPacManDescription.class);
		simConfig.addMapping(score, new Interval(15000));
		simConfig.setWeight(score, 0.0);
		Attribute oldscore = new Attribute("oldscore",MsPacManDescription.class);
		simConfig.addMapping(oldscore, new Interval(15000));
		simConfig.setWeight(oldscore, 0.0);
		//score y oldscore tienen peso 0.0 porque no buscamos que tengas importancia en kn
		//solo son atributos usados para otros calculos

		simConfig.addMapping(new Attribute("danger",MsPacManDescription.class), new DangerSimilarityFunction());
		Attribute danger = new Attribute("danger",MsPacManDescription.class);
		simConfig.setWeight(danger, 0.0);
		//simConfig.addMapping(new Attribute("danger34",MsPacManDescription.class), new ArrayInterval(150.0));
		//Attribute danger34 = new Attribute("danger34",MsPacManDescription.class);
		//simConfig.setWeight(danger34, 0.2);
		simConfig.addMapping(new Attribute("edible",MsPacManDescription.class), new ArrayInterval(50.0));
		Attribute edible = new Attribute("edible",MsPacManDescription.class);
		simConfig.setWeight(edible, 2.0);
		simConfig.addMapping(new Attribute("edible34",MsPacManDescription.class), new ArrayInterval(50.0));
		Attribute edible34 = new Attribute("edible34",MsPacManDescription.class);
		simConfig.setWeight(edible34, 1.0);
		
		simConfig.addMapping(new Attribute("lastmove",MsPacManDescription.class), new LastMoveSimilarityFunction());
		Attribute lastmove = new Attribute("lastmove",MsPacManDescription.class);
		simConfig.setWeight(lastmove, 0.0);

		simConfig.addMapping(new Attribute("nearestGhost",MsPacManDescription.class), new Interval(50.0));
		Attribute nearestGhost = new Attribute("nearestGhost",MsPacManDescription.class);
		simConfig.setWeight(nearestGhost, 2.0);
		
		
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			Random rnd=new Random();
			this.action = MOVE.values()[rnd.nextInt(MOVE.values().length-2)]; 
			CBRCase newCase = createNewCase(query);
			this.storageManager.reviseAndRetain(newCase,null);
		}
		else {
			
			if(((PacmanCaseBase) caseBase).getIndexedCases((MsPacManDescription)query.getDescription())==null) {
				Random rnd=new Random();
				this.action = MOVE.values()[rnd.nextInt(MOVE.values().length-2)];
				CBRCase newCase = createNewCase(query);
				this.storageManager.reviseAndRetain(newCase,null);
			}
			else {
				//Compute retrieve
				Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(((PacmanCaseBase) caseBase).getIndexedCases((MsPacManDescription)query.getDescription()), query, simConfig);
				//Compute reuse
				this.action = reuse(eval);
				CBRCase newCase = createNewCase(query);
				this.storageManager.reviseAndRetain(newCase,eval);
			}

		}
		
		
		
		//Compute revise & retain
		//CBRCase newCase = createNewCase(query);
		//this.storageManager.reviseAndRetain(newCase);
		
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();

		MsPacManResult result = (MsPacManResult) mostSimilarCase.getResult();
		MsPacManSolution solution = (MsPacManSolution) mostSimilarCase.getSolution();
		
		//Now compute a solution for the query
		

		
		MOVE action=solution.getAction();
		Collection<CBRCase>cases = SelectCases.selectTopK(eval, 4);
		action = getBestMove(cases);//elegimos el mejor movimiento por votacion 
		
		//System.out.println("simi: "+similarity);
		if(action==null||(similarity<0.7)||(result.getScore()<0.0)) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==action) 
				index= (index+1)%4;
			action = MOVE.values()[index];
		}
		
		return action;
	}
	
	private MOVE getBestMove(Collection<CBRCase>cases) {
		MOVE m=null;
		double []moves= {0.0,0.0,0.0,0.0};
		//a cada movimiento le vamos sumando puntos
		for(CBRCase c:cases) {
			MsPacManResult res=(MsPacManResult)c.getResult();
			MsPacManSolution sol=(MsPacManSolution)c.getSolution();
			
			moves[sol.getAction().ordinal()]+=res.getScore();
			
		}
		
		//elegimos entre los movimientos con mayor puntos
		ArrayList<MOVE>options=new ArrayList<MOVE>();
		double max=-1.0;
		for(int i=0;i<4;i++) {
			if(moves[i]>max) {
				max=moves[i];
				options.clear();
				options.add(MOVE.values()[i]);
			}
			else if(moves[i]==max) {
				options.add(MOVE.values()[i]);
			}
		}
		Random rnd=new Random();
		if(options.size()!=0)m=options.get(rnd.nextInt(options.size()));
		return m;
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
		//int newId = this.caseBase.getCases().size();
		//newId+= storageManager.getPendingCases();
		int newId = ((PacmanCaseBase) caseBase).getNextId();
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
