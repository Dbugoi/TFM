package es.ucm.fdi.ici.c2122.practica5.grupo02.mspacman;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import pacman.game.Constants;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	final int scoreValue = Constants.PILL * 4;
	
	boolean liveHaveBeenLostFactor;
	boolean greatScoreFactor;
	
	
	private final static int TIME_WINDOW = 4;
	
	public MsPacManStorageManager(){
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		if(!liveHaveBeenLostFactor && greatScoreFactor) {
			retainCase(bCase);
		}
	}
	
	private void reviseCase(CBRCase bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		int oldScore = description.getScore();
		
		int currentScore = game.getScore();
		
		int resultValue = currentScore - oldScore;
		
		int currLives = game.getPacmanNumberOfLivesRemaining();
		
		int livesResultLives = Math.abs(currLives - description.getLivesLeft());
		
		//Ahora hay que actualizar los nuevos valores para saber si ha ido bien
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);
		result.setLives(livesResultLives);
		
		liveHaveBeenLostFactor = currLives < description.getLivesLeft(); 
		greatScoreFactor = resultValue > scoreValue;
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			if(!liveHaveBeenLostFactor && greatScoreFactor) {
				retainCase(oldCase);
			}
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
