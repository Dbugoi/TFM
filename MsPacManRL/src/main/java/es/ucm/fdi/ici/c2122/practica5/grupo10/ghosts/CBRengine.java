package es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
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
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengineGhosts.AverageGhosts;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengineGhosts.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengineGhosts.CustomPlainTextConnector;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class CBRengine implements StandardCBRApplication{

	private double similarity;
	private String opponent;
	private MOVE action;
	private GhostsStorageManager storageManager;
	private int CASES_LIMIT = 2000;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;


	final static String TEAM = "grupo10";  //Cuidado!! poner el grupo aquí


	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2122/practica5/"+TEAM+"/ghosts/GhostsPlaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator;


	public CBRengine(GhostsStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();

		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));

		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");

		this.storageManager.setCaseBase(caseBase);

		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new AverageGhosts());

		int interval = 500;

		simConfig.addMapping(new Attribute("distance_to_mspacman_up",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_mspacman_down",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_mspacman_left",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_mspacman_right",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_ghosts_up",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_ghosts_down",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_ghosts_left",GhostsDescription.class), new Interval(interval));
		simConfig.addMapping(new Attribute("distance_to_ghosts_right",GhostsDescription.class), new Interval(interval));		
		simConfig.addMapping(new Attribute("edible_time_ghost",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("edible_time_ghost_2",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("edible_time_ghost_3",GhostsDescription.class), new Interval(200));
		simConfig.addMapping(new Attribute("edible_time_ghost_4",GhostsDescription.class), new Interval(200));		
		simConfig.addMapping(new Attribute("distance_nearest_ppill_mspacman",GhostsDescription.class),  new Interval(interval));
		simConfig.addMapping(new Attribute("distance_ghost_to_nearest_ppill",GhostsDescription.class),  new Interval(interval));
		simConfig.addMapping(new Attribute("lastMovePacman",GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("lastMoveGhost",GhostsDescription.class), new Equal());

//		simConfig.setWeight(new Attribute("lastMoveGhost",GhostsDescription.class), (double)15);
		

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
			this.similarity=0;
		}
		else {
			//Compute retrieve
//			System.out.println(query.toString());
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
		}

		//Compute revise & retain
		if(this.caseBase.getCases()== null || this.caseBase.getCases().size() <CASES_LIMIT) {
			CBRCase newCase = createNewCase(query);
			this.storageManager.reviseAndRetain(newCase,this.similarity);
		}
		//		else
		//			System.out.println("Base de casos completada, no se pudo almacenar caso");


	}

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

	private MOVE reuse(Collection<RetrievalResult> eval) {
	
		//using kNNs with majority voting
	
		double prob_for_up=0,prob_for_down=0,prob_for_left=0,prob_for_right=0;


		Collection<RetrievalResult> retrievals = SelectCases.selectTopKRR(eval,5);
		RetrievalResult first = retrievals.iterator().next();
		this.similarity = first.getEval();
		MOVE final_action = ((GhostsSolution) first.get_case().getSolution()).getAction();
		Iterator<RetrievalResult> it = retrievals.iterator();


		while(it.hasNext()) {
			RetrievalResult elem = it.next();
			CBRCase similarCase = elem.get_case();
			double similarity = elem.getEval();
		
			if(similarity >= 0.8) {
				GhostsResult result = (GhostsResult) similarCase.getResult();
				GhostsSolution solution = (GhostsSolution) similarCase.getSolution();
				double score = result.getScore();

				switch(solution.getAction()) {
				case UP:prob_for_up+=(similarity*score);break;
				case DOWN:prob_for_down+=(similarity*score);break;
				case LEFT:prob_for_left+=(similarity*score);break;
				case RIGHT:prob_for_right+=(similarity*score);break;
				default:break;
				}
			}

		}

		if(similarity<0.8) {
			int index = (int)Math.floor(Math.random()*4);
			if(MOVE.values()[index]==final_action) 
				index= (index+1)%4;
			final_action = MOVE.values()[index];
		}
		else {
			double max = prob_for_up;
			final_action=MOVE.UP;

			if(prob_for_down > max) {
				max = prob_for_down;
				final_action=MOVE.DOWN;
			}

			if(prob_for_left > max) {
				max = prob_for_left;
				final_action=MOVE.LEFT;
			}

			if(prob_for_right > max) {
				max = prob_for_right;
				final_action=MOVE.RIGHT;
			}
		}


		return final_action;

	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBase.close();

	}

	public MOVE getSolution() {
		return this.action;
	}

}
