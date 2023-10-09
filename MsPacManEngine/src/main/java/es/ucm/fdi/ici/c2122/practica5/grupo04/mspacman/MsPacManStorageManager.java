package es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica5.grupo04.common.CommonMethodsPacman;
import pacman.game.Game;


public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, NNConfig simConfig)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase[] bCases = {this.buffer.remove(0), this.buffer.get(0), this.buffer.get(1)};
		reviseCase(bCases);
		retainCase(bCases, simConfig);
		
	}
	
	// f(x)= -1.004^-x + 1 
	// Le llegan los 3 ultimos casos (3 ultimas intersecciones)
	private void reviseCase(CBRCase[] bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase[0].getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int pasos1 = -(currentScore - oldScore);
		double pasos =  -Math.pow(-1.004, pasos1) + 1;
		float resultValue = (float)pasos  * 100;
		
		
		int resultHealth = description.getHealth() - game.getPacmanNumberOfLivesRemaining();
		resultValue -= resultHealth * 50; 
		
		
		float distFactor = 0.2f;
		int pacmanPos = game.getPacmanCurrentNodeIndex();
		resultValue+= (description.getClosestPillDist()-
				game.getShortestPathDistance(pacmanPos, CommonMethodsPacman.getClosestPill(game, pacmanPos), game.getPacmanLastMoveMade())) * distFactor;
		//ponemos la puntuacion a los 3 casos
		for(int i = 0; i < bCase.length; ++i) {
			MsPacManResult result = (MsPacManResult)bCase[i].getResult();
			//score como maximo 100 y como minimo -50 (si mueres sin conseguir ningun punto)
			result.setScore((int)(resultValue));
		}
	}
	
	//guardado de los 3 ultimos casos si no hay alguno similar
	private void retainCase(CBRCase[] bCase, NNConfig simConfig)
	{
		//recorremos todos los casos que nos llegan para guardarlo
		for(int i = 0; i < bCase.length; ++i) {
			//Si se parece mas dfel 80% no lo guardamos
			float tooMuchSimilarity = 0.80f;
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), bCase[i], simConfig);
			if(eval.isEmpty())
				StoreCasesMethod.storeCase(this.caseBase, bCase[i]);		
			else{
				MsPacManResult result = (MsPacManResult)bCase[i].getResult();
				RetrievalResult rResults = SelectCases.selectTopKRR(eval, 1).iterator().next();
				MsPacManResult similarResult =  (MsPacManResult)rResults.get_case().getResult();
				if(rResults.getEval() < tooMuchSimilarity)
					StoreCasesMethod.storeCase(this.caseBase, bCase[i]);
				//guardamos tambien si se parece pero contiene mas score
				else if(similarResult.getScore() + 10 < result.getScore()) {
					StoreCasesMethod.storeCase(this.caseBase, bCase[i]);
				}
			}
		}
	}

	// Cerramos guardando todos los casos que queden
	public void close(NNConfig simConfig) {
		CBRCase[] cases = new CBRCase[buffer.size()];
		for(int i = 0; i < this.buffer.size(); ++i) {
			cases[i] = buffer.get(i);
		}
		reviseCase(cases);
		retainCase(cases, simConfig);
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
