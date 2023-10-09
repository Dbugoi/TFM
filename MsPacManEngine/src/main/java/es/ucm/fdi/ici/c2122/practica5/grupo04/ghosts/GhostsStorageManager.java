package es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica5.grupo04.common.CommonMethodsGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	Vector<GHOST> ghostBuffer;

	private final static int TIME_WINDOW = 3;
	
	public GhostsStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
		this.ghostBuffer = new Vector<GHOST>();
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
		int i = 0;
		GhostsDescription description = (GhostsDescription)newCase.getDescription();
		while(i < GHOST.values().length &&
				(game.getGhostCurrentNodeIndex(GHOST.values()[i]) != description.getGhostPos())) ++i;
		this.ghostBuffer.add(GHOST.values()[i]);
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase[] bCase = {this.buffer.remove(0), this.buffer.get(0), this.buffer.get(1)};
		reviseCase(bCase);
		retainCase(bCase, simConfig);
		
	}
	// f(x)= - 2 * tg^-1(x/n)/ PI -> x = dist * danger
	private void reviseCase(CBRCase[] bCase) {
		GhostsDescription description = (GhostsDescription)bCase[0].getDescription();
		float pasos1 = game.getShortestPathDistance(description.getGhostPos(), game.getPacmanCurrentNodeIndex()) *
				CommonMethodsGhosts.dangerLevel(game, description.getGhostPos(), 1.65f, ghostBuffer.remove(0));
		double pasos =  -2 * Math.atan((pasos1 - 200 * 0.4) / 10) / Math.PI;
		float resultValue = (float)pasos  * 100;
		
		
		int resultHealth = description.getHealth() - game.getPacmanNumberOfLivesRemaining();
		resultValue += resultHealth * 50; 
		
		for(int i = 0; i < bCase.length; ++i) {
			GhostsResult result = (GhostsResult)bCase[i].getResult();
			
			//score como maximo 100 y como minimo -50 (si mueres sin conseguir ningun punto)
			//System.out.println(resultValue);
			result.setScore((int)(resultValue));	
		}
	}
	
	private void retainCase(CBRCase[] bCase, NNConfig simConfig)
	{
		for(int i = 0; i < bCase.length; ++i) {
			//Si se parece mas dfel 93% no lo guardamos
			float tooMuchSimilarity = 0.93f;
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), bCase[i], simConfig);
			if(eval.isEmpty())
				StoreCasesMethod.storeCase(this.caseBase, bCase[i]);		
			else{
				GhostsResult result = (GhostsResult)bCase[i].getResult();
				RetrievalResult rResults = SelectCases.selectTopKRR(eval, 1).iterator().next();
				GhostsResult similarResult =  (GhostsResult)rResults.get_case().getResult();
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
