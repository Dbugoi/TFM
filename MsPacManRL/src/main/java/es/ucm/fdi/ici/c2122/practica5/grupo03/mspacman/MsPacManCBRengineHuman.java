package es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman;

import java.io.File;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine.Average;
import es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine.CacheLinearCaseBaseByLevel;
import es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.AbsSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.AverageMoveSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.CosineSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.MapSim;
import es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions.MoveSim;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengineHuman implements StandardCBRApplication {
	
	private MOVE action;
	private MsPacManStorageManagerHuman storageManagerGeneric;

	CustomPlainTextConnector connectorGeneric;
	Collection<RetrievalResult> evalGeneric;
	
	CacheLinearCaseBaseByLevel caseBaseGeneric;
	
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo03";  //Cuidado!! poner el grupo aquí
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;

	
	public MsPacManCBRengineHuman(MsPacManStorageManagerHuman storageManagerGeneric){
		this.storageManagerGeneric = storageManagerGeneric;
	}
	
	@Override
	public void configure() throws ExecutionException {
		
		connectorGeneric = new CustomPlainTextConnector();
		caseBaseGeneric = new CacheLinearCaseBaseByLevel();
		
		connectorGeneric.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connectorGeneric.setCaseBaseFile(CASE_BASE_PATH, "generic.csv");
		
		this.storageManagerGeneric.setCaseBase(caseBaseGeneric);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());

		simConfig.addMapping(new Attribute("level",MsPacManDescription.class), new MapSim());
		
		simConfig.addMapping(new Attribute("DistanciaClosestGhostDirecction",MsPacManDescription.class), new CosineSim());
		simConfig.addMapping(new Attribute("DistanciaGhosts",MsPacManDescription.class), new CosineSim());
		simConfig.addMapping(new Attribute("DistanciaClosestPPillDirecction",MsPacManDescription.class), new CosineSim());
		simConfig.addMapping(new Attribute("DistanciaGhostsClosestPPill",MsPacManDescription.class), new CosineSim());
		
		simConfig.addMapping(new Attribute("PacManLastMove",MsPacManDescription.class), new MoveSim());
		
		simConfig.addMapping(new Attribute("GhostLastMove",MsPacManDescription.class), new AverageMoveSim());
		simConfig.addMapping(new Attribute("EdibleTimeGhosts",MsPacManDescription.class), new  CosineSim());
		simConfig.addMapping(new Attribute("JailTimeGhosts",MsPacManDescription.class), new  CosineSim());
		
		simConfig.addMapping(new Attribute("score",MsPacManDescription.class), new AbsSim());
		simConfig.addMapping(new Attribute("lives",MsPacManDescription.class), new AbsSim());
		simConfig.addMapping(new Attribute("PillsLeft",MsPacManDescription.class), new AbsSim());
		
		
		//simConfig.addMapping(new Attribute("time",MsPacManDescription.class), new Interval(4000));
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBaseGeneric.init(connectorGeneric);
		return caseBaseGeneric; //cualquiera de los dos
	}

	public void cycle(CBRQuery query, MOVE move) throws ExecutionException {
		if(caseBaseGeneric.getCases().isEmpty()) {
			this.action = move;
			CBRCase newCase = createNewCaseGeneric(query);
			this.storageManagerGeneric.reviseAndRetain(newCase);
		}
		else {
			//Compute retrieve
			this.action = move;
			
			evalGeneric = NNScoringMethod.evaluateSimilarity(caseBaseGeneric.getCases(Character.getNumericValue(query.toString().split("Mapid=")[1].charAt(0))), query, simConfig);
			
			CBRCase newCase = createNewCaseGeneric(query);
			
			this.storageManagerGeneric.reviseAndRetain(newCase, evalGeneric);
		}
		
	}

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */

	private CBRCase createNewCaseGeneric(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
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
	
	public MOVE getSolution() {
		return this.action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManagerGeneric.close(this.evalGeneric);
		this.caseBaseGeneric.close();
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		// TODO Auto-generated method stub
		
	}

}
