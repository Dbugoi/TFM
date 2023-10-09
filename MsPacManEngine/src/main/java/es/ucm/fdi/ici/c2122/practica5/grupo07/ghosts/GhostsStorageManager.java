package es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsStorageManager {

	Game game;
	GHOST ghost;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	Vector<Collection<RetrievalResult>>evalbuffer;
	private final static int TIME_WINDOW = 8;
	
	public GhostsStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
		this.evalbuffer = new Vector<Collection<RetrievalResult>>();
	}
	
	public void setGame(Game game,GHOST ghost) {
		this.ghost = ghost;
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, Collection<RetrievalResult> evals)
	{			
		this.buffer.add(newCase);
		this.evalbuffer.add(evals);
		
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		Collection<RetrievalResult> eval = this.evalbuffer.remove(0);
		
		reviseCase(bCase);
		if(eval==null)StoreCasesMethod.storeCase(this.caseBase, bCase);
		else retainCase(bCase,eval);
		
	}
	public void pacmanEaten(Game game) {
		if(game.wasPacManEaten()) {
			for(int i=0;i<buffer.size();i++) {
				if(evalbuffer.get(i)==null) {
					GhostsResult result = (GhostsResult)buffer.get(i).getResult();
					result.setScore(-100.0);
					StoreCasesMethod.storeCase(this.caseBase, buffer.get(i));
				}
				else {
					RetrievalResult first = SelectCases.selectTopKRR(evalbuffer.get(i), 1).iterator().next();
					if(first.getEval()<0.95 ) {
						GhostsResult result = (GhostsResult)buffer.get(i).getResult();
						result.setScore(-100.0);
						StoreCasesMethod.storeCase(this.caseBase, buffer.get(i));
					}
				}
				
			}
			this.buffer.removeAllElements();
			this.evalbuffer.removeAllElements();
		}
		
	}
	private void reviseCase(CBRCase bCase) {
		GhostsDescription description = (GhostsDescription)bCase.getDescription();
		double oldScore = description.getScore();
		//double currentScore = game.getScore();
		double currentScore = GhostsHeuristicFunction.getValue(game,ghost);
		double resultValue = currentScore - oldScore;
		//System.out.println(resultValue);
		GhostsResult result = (GhostsResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase, Collection<RetrievalResult> eval)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method

		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		GhostsResult result = (GhostsResult)bCase.getResult();
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		
		//System.out.println(first.getEval());
		if(first.getEval()<0.95 ) {

			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		
		if(result.getScore()>0.0) {
			
			
			/*if(first.getEval()<0.8 ) {
				
			}
			else {

				
				CBRCase firstCase = SelectCases.selectTopK(eval, 1).iterator().next();
				double oldscore=((MsPacManDescription) firstCase.getDescription()).getScore();
				double newscore=((MsPacManDescription) bCase.getDescription()).getScore();
				
				if(newscore>oldscore) {
					this.forgetCase(firstCase);
					StoreCasesMethod.storeCase(this.caseBase, bCase);
				}
		
			}*/
			
			
			
		}
	}
	public void forgetCase(CBRCase oldCase) {
		List<CBRCase> cases = new ArrayList<CBRCase>();
		cases.add(oldCase);
		this.caseBase.forgetCases(cases);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			//retainCase(oldCase);
		}
		this.buffer.removeAllElements();
		this.evalbuffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
