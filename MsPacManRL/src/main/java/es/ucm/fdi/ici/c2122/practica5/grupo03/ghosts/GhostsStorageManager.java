package es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica5.grupo03.CBRengine.CacheLinearCaseBaseByLevel;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.ContadorSaturado;
import pacman.game.Game;

public class GhostsStorageManager {

	Game game;
	CacheLinearCaseBaseByLevel caseBase;
	Vector<CBRCase> buffer;
	int safe_value;

	private final static int TIME_WINDOW = 3;
	
	public GhostsStorageManager(int vs)
	{
		safe_value = vs;
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CacheLinearCaseBaseByLevel caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, ContadorSaturado counter, String toAddCase) /* add contador , similitud*/
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase, counter, toAddCase);
		retainCase(bCase);
		
	}
	
	public void reviseAndRetain(CBRCase newCase, Collection<RetrievalResult> evalSpecific, ContadorSaturado counter, String toAddCase) /* add contador , similitud*/
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase, counter, toAddCase);
		retainCase(bCase, evalSpecific);
		
	}
	
	private void reviseCase(CBRCase bCase, ContadorSaturado counter, String toAddCase) {
		GhostsDescription description = (GhostsDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = oldScore - currentScore;
		
		if(resultValue > safe_value) {
			if(toAddCase == "generic") {
				counter.up();
			}
			else {
				counter.down();
			}
		}
		
		GhostsResult result = (GhostsResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
	
		StoreCasesMethod.storeCase(this.caseBase, bCase);
		
	}
	
	private void retainCase(CBRCase bCase, Collection<RetrievalResult> evalSpecific)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		GhostsSolution solution = new GhostsSolution();
		GhostsSolution solutionActual = (GhostsSolution) bCase.getSolution();
		GhostsResult resultActual = (GhostsResult) bCase.getResult();
		
		double similarity = 0;
		
		
		//Specific
		Collection<RetrievalResult> specificRetrival = SelectCases.selectTopKRR(evalSpecific, 5);
				
		for(RetrievalResult r: specificRetrival) {
			if(r.getEval() > similarity) {
				similarity = r.getEval();
				solution = (GhostsSolution) r.get_case().getSolution();
			}
		}
				
		if((similarity < 0.6) && (resultActual.getScore() > - safe_value)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		else if((similarity > 0.85) && (solution.getAction() != solutionActual.getAction()) && (resultActual.getScore() > - safe_value)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		
	}

	public void close(Collection<RetrievalResult> evalSpecific, ContadorSaturado counter, String toAddCase) {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase, counter, toAddCase);
			retainCase(oldCase, evalSpecific);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
