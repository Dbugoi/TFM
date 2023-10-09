package es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 4; //number of intersections before revising
	private final static int POINTS = 300;  //min of points to add new case if it's not similar
	private final static int POINTS_HIGH = 700; //min of points to add new case if it's very similar
	
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
	
	public void reviseAndRetain(CBRCase newCase, double sim)
	{	
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase, sim);
		
	}
	
	private void reviseCase(CBRCase bCase) {
		//score = game.getScore() - description.getScore() +
		//500 * (game.getPacmanNumberOfLivesRemaining() - description.getlives())
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = currentScore - oldScore;
		
		int new_mspacman_lifes = game.getPacmanNumberOfLivesRemaining();
		//difference_lifes -> always <= 0
		int difference_lifes = new_mspacman_lifes - description.getLives();
		
		resultValue += 500*(difference_lifes);
		
		if(resultValue < 0) resultValue=0;
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase, double similarity)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		Integer value = result.getScore();
		
		if(value > POINTS && similarity < 0.95) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		else if(similarity > 0.95 && value > POINTS_HIGH) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase, 0);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
