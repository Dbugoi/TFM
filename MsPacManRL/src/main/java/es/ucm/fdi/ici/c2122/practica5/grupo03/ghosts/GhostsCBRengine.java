package es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine.CacheLinearCaseBaseByLevel;
import es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.BasedeCasos;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.ContadorSaturado;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.Pair;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManDescription;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.AbsSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.AverageMoveSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.CosineSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.MapSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.MoveSim;
import pacman.game.Constants.MOVE;

public class GhostsCBRengine implements StandardCBRApplication {

	private ContadorSaturado counter;
	private String opponent;
	private MOVE action;
	private GhostsStorageManager storageManagerGeneric;
	private GhostsStorageManager storageManagerSpecific;

	CustomPlainTextConnector connectorGeneric;
	CustomPlainTextConnector connectorSpecific;
	
	Collection<RetrievalResult> evalSpecific;
	Collection<RetrievalResult> evalGeneric;
	
	CacheLinearCaseBaseByLevel caseBaseGeneric;
	CacheLinearCaseBaseByLevel caseBaseSpecific;
	
	NNConfig simConfig;
	
	String toAddCase;
	
	Vector<Double> pesos;
	
	final static String TEAM = "grupo03";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "data"+File.separator+TEAM+File.separator+"ghosts"+File.separator;

	final static String[] NombresAtributos = {"level","DistanciaClosestGhostDirecction", "DistanciaGhosts", "DistanciaClosestPPillDirecction", "DistanciaGhostsClosestPPill", "PacManLastMove",
			"GhostLastMove", "EdibleTimeGhosts", "JailTimeGhosts", "score", "lives", "PillsLeft"};
	
	final static LocalSimilarityFunction[] FuncionesAtributos = {new MapSim(), new CosineSim(), new CosineSim(), new CosineSim(), new CosineSim(), new MoveSim(), new AverageMoveSim(), new CosineSim(),
			new CosineSim(), new AbsSim(), new AbsSim(), new AbsSim()};
	
	public GhostsCBRengine(GhostsStorageManager storageManagerGeneric, GhostsStorageManager storageManagerSpecific, Vector<Double> weights)
	{
		this.storageManagerGeneric = storageManagerGeneric;
		this.storageManagerSpecific = storageManagerSpecific;
		pesos = weights;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		
		counter = new ContadorSaturado();
		
		connectorGeneric = new CustomPlainTextConnector();
		caseBaseGeneric = new CacheLinearCaseBaseByLevel();
		
		connectorSpecific = new CustomPlainTextConnector();
		caseBaseSpecific = new CacheLinearCaseBaseByLevel();
		
		connectorGeneric.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		connectorSpecific.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connectorGeneric.setCaseBaseFile(CASE_BASE_PATH, "generic.csv");
		connectorSpecific.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManagerGeneric.setCaseBase(caseBaseGeneric);
		this.storageManagerSpecific.setCaseBase(caseBaseSpecific);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());

		for(int i = 0; i < NombresAtributos.length; i++) {
			Attribute at = new Attribute(NombresAtributos[i], MsPacManDescription.class);
			simConfig.addMapping(at, FuncionesAtributos[i]);
			simConfig.setWeight(at, pesos.get(i));
		}
		
		//simConfig.addMapping(new Attribute("time",GhostsDescription.class), new Interval(4000));
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBaseGeneric.init(connectorGeneric);
		caseBaseSpecific.init(connectorSpecific);
		return caseBaseGeneric; //cualquiera de los dos
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBaseGeneric.getCases().isEmpty() && caseBaseSpecific.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
			CBRCase newCase = createNewCaseSpecific(query);
			this.storageManagerSpecific.reviseAndRetain(newCase, counter, opponent);
		}
		else {
			//Compute retrieve
			evalGeneric = NNScoringMethod.evaluateSimilarity(caseBaseGeneric.getCases(Character.getNumericValue(query.toString().split("Mapid=")[1].charAt(0))), query, simConfig);
			evalSpecific = NNScoringMethod.evaluateSimilarity(caseBaseSpecific.getCases(Character.getNumericValue(query.toString().split("Mapid=")[1].charAt(0))), query, simConfig);
			//Compute reuse -> aqui es donde tenemos que tener en cuenta el valor del contador
			this.action = reuse(evalGeneric, evalSpecific);
			
			//Compute revise & retain
			/*
			if(toAddCase == "generic") {
				CBRCase newCase = createNewCaseGeneric(query);
				this.storageManagerGeneric.reviseAndRetain(newCase);
			}
			else {
				CBRCase newCase = createNewCaseSpecific(query);
				this.storageManagerSpecific.reviseAndRetain(newCase);
			}*/
			
			// siempre se añade a la especifica o eso creo que deberia
			CBRCase newCase = createNewCaseSpecific(query);
			this.storageManagerSpecific.reviseAndRetain(newCase, evalSpecific, counter, opponent);
		}
		
		
	}
	
	private MOVE reuse(Collection<RetrievalResult> evalGeneric, Collection<RetrievalResult> evalSpecific)
	{	
		
		//Aqui tenemos que consultar el contador para elegir los casos de las bases que van al proceso de seleccion
		Pair<Pair<BasedeCasos,Integer>, Pair<BasedeCasos,Integer>> pair = counter.getcount();
		
		Pair<BasedeCasos,Integer> pair_generic = pair.getFirst();
		Pair<BasedeCasos,Integer> pair_specific = pair.getSecond();

		// De estas dos, teniendo en cuenta el contador, tenemos que quedarnos con 4
		// -> Esta hecho, se hace con el tercer parametro de selectTopKRR                            (este)
		Collection<RetrievalResult> genericRetrival = SelectCases.selectTopKRR(evalGeneric, pair_generic.getSecond());
		Collection<RetrievalResult> specificRetrival = SelectCases.selectTopKRR(evalSpecific, pair_specific.getSecond());
		
		
		Pair<MOVE, Double> par;
		
		
		/* Solo dejar activa una de las tres*/
		
		///////////////////////////////// Mayor similitud  ///////////////////////////////////////////
		
		par = this.MaxSim(genericRetrival, specificRetrival);
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		
		MOVE action = par.getFirst();
		double similarity = par.getSecond();
		
		//But if not enough similarity or bad case, choose another move randomly
		if((similarity<0.7)) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==action) 
				index= (index+1)%4;
			action = MOVE.values()[index];
			toAddCase = "specific";
		}
		
		/*
		if(toAddCase == "generic") {
			counter.up();
		}
		else {
			counter.down();
		}*/
		
		return action;
	}
	

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */	

	private CBRCase createNewCaseGeneric(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostsDescription newDescription = (GhostsDescription) query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
		int newId = this.caseBaseGeneric.getCases().size();
		newId+= storageManagerGeneric.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(this.action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	private CBRCase createNewCaseSpecific(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostsDescription newDescription = (GhostsDescription) query.getDescription();
		GhostsResult newResult = new GhostsResult();
		GhostsSolution newSolution = new GhostsSolution();
		int newId = this.caseBaseSpecific.getCases().size();
		newId+= storageManagerSpecific.getPendingCases();
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
		this.storageManagerGeneric.close(this.evalGeneric, counter, toAddCase);
		this.storageManagerSpecific.close(this.evalSpecific, counter, toAddCase);
		this.caseBaseGeneric.close();
		this.caseBaseSpecific.close();
	}
	
	private Pair<MOVE , Double> MaxSim(Collection<RetrievalResult> genericRetrival, Collection<RetrievalResult> specificRetrival) {
		
		GhostsResult result = new GhostsResult();
		GhostsSolution solution = new GhostsSolution();
		
		double similarity = 0;
		
		//Generic
		for(RetrievalResult r: genericRetrival) {
			if((Math.abs(r.getEval() - similarity) < 0.5) && (similarity != 0)) {
				GhostsResult result2 = (GhostsResult) r.get_case().getResult();
				if(result2.getScore() > result.getScore()) {
					similarity = r.getEval();
					result = result2;
					solution = (GhostsSolution) r.get_case().getSolution();
					toAddCase = "generic";
				}
			}
			else if(r.getEval() > similarity) {
				similarity = r.getEval();
				result = (GhostsResult) r.get_case().getResult();
				solution = (GhostsSolution) r.get_case().getSolution();
				toAddCase = "generic";
			}
		}
		
		
		//Specific
				
		for(RetrievalResult r: specificRetrival) {
			if((Math.abs(r.getEval() - similarity) < 0.5) && (similarity != 0)) {
				GhostsResult result2 = (GhostsResult) r.get_case().getResult();
				if(result2.getScore() > result.getScore()) {
					similarity = r.getEval();
					result = result2;
					solution = (GhostsSolution) r.get_case().getSolution();
					toAddCase = "specific";
				}
			}
			else if(r.getEval() > similarity) {
				similarity = r.getEval();
				result = (GhostsResult) r.get_case().getResult();
				solution = (GhostsSolution) r.get_case().getSolution();
				toAddCase = "specific";
			}
		}
		
		Pair<MOVE , Double> p = new Pair<MOVE , Double> (solution.getAction(), similarity);
		
		return p;
	}
	
	class CaseInfo{
		
		double similitud;
		String procedencia;
		CBRCase caso;
		double acumulada;
		
		public CaseInfo(CBRCase c, double sim, double simAc, String proc) {
			similitud = sim;
			procedencia = proc;
			caso = c;
			acumulada = simAc;
		}
		
		public double getSimilitud() {
			return similitud;
		}

		public String getProcedencia() {
			return procedencia;
		}

		public CBRCase getCaso() {
			return caso;
		}
		
		public double getAcumulada() {
			return acumulada;
		}		
		
	}
	
	private Pair<MOVE , Double> roulete(Collection<RetrievalResult> genericRetrival, Collection<RetrievalResult> specificRetrival) {
		
		GhostsSolution solution = new GhostsSolution();
		
		double similarity = 0;
		
		// Ruleta
		
		double similaritysum = 0; //similitud total
		List<CaseInfo> list = new ArrayList<CaseInfo>();
		
		// lista de probabilidad acumulada
		//lista del los casos sacados en orden con la lista anterior
		
		for(RetrievalResult r: genericRetrival) {
			similaritysum = r.getEval() + similaritysum;
			list.add(new CaseInfo(r.get_case(), r.getEval(), similaritysum, "generic"));
		}
		
		
		for(RetrievalResult r: specificRetrival) {
			similaritysum = r.getEval() + similaritysum;
			list.add(new CaseInfo(r.get_case(), r.getEval(), similaritysum, "specific"));
		}
		
		double rand = Math.floor(Math.random()*(similaritysum-1));
		
		for(CaseInfo c: list) {
			if(rand < c.getAcumulada()) {
				similarity = c.getSimilitud();
				solution = (GhostsSolution) c.getCaso().getSolution();
				toAddCase = c.getProcedencia(); 
				break;
			}
		}
		
		Pair<MOVE , Double> p = new Pair<MOVE , Double> (solution.getAction(), similarity);
		
		return p;
		
	}


	private Pair<MOVE, Double> Ponderada(Collection<RetrievalResult> genericRetrival, Collection<RetrievalResult> specificRetrival){
	
		GhostsSolution solution = new GhostsSolution();
		
		MOVE[] MOVEs = {MOVE.UP, MOVE.DOWN, MOVE.LEFT, MOVE.RIGHT};
		double[] similarityMOVE = new double[4];
		
		double similarity;
		
		//Generic
		
		/*
		 * 1º mirar las similitudes de cada caso
		 * 2º para cada posible resultado, sacar las similitudes acumuladas de todos los casos recuperados
		 *    ejemplo : hay 3 casos con up con similitudes (0.3,0.1 y 0.4) y 1 caso con down de 0.9
		 *    			como para UP hay 0.3+0.1+0.4 = 0.8 < 0.9 , se emplea down
		 */
		
		for(RetrievalResult r: genericRetrival) {
			similarity = r.getEval();
			solution = (GhostsSolution) r.get_case().getSolution();
			
			switch(solution.getAction()) {
				case UP:
					similarityMOVE[0] += similarity;
					break;
					
				case DOWN:
					similarityMOVE[1] += similarity;
					break;
					
				case LEFT:
					similarityMOVE[2] += similarity;
					break;
					
				case RIGHT:
					similarityMOVE[3] += similarity;
					break;
			}
		}
				
				
		//Specific
				
		for(RetrievalResult r: specificRetrival) {
			similarity = r.getEval();
			solution = (GhostsSolution) r.get_case().getSolution();
			
			switch(solution.getAction()) {
				case UP:
					similarityMOVE[0] += similarity;
					break;
					
				case DOWN:
					similarityMOVE[1] += similarity;
					break;
					
				case LEFT:
					similarityMOVE[2] += similarity;
					break;
					
				case RIGHT:
					similarityMOVE[3] += similarity;
					break;
			}
		}
		
		double maxsim = 0;
		MOVE move = MOVE.NEUTRAL;
		
		for(int i = 0; i < 4; i++) {
			if(similarityMOVE[i] > maxsim) {
				maxsim = similarityMOVE[i];
				move = MOVEs[i];
			}
		}
			
		toAddCase = "specific";
		
		Pair<MOVE , Double> p = new Pair<MOVE , Double> (move, maxsim);
		
		return p;
	}

}
