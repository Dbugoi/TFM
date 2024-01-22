package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman.MsPacManDescriptionEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman.MsPacManResultEX;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsStorageManagerEX {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 3;
	private static final Integer MAX_SCORE_TO_PASS = 400;
	
	public GhostsStorageManagerEX()
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
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		if(needToRetain(bCase))
			retainCase(bCase);
		
	}
	
	private boolean needToRetain(CBRCase bCase) {
		GhostsResultEX result = (GhostsResultEX)bCase.getResult();
		GhostsDescriptionEX description = (GhostsDescriptionEX)bCase.getDescription();
				
		if(description.getLevel() < game.getCurrentLevel()) return false;
		
		if(result.getLife() > 0) return true;
		
		if(result.getScore() > MAX_SCORE_TO_PASS) return false;
		
		return true;
	}

	private void reviseCase(CBRCase bCase) {
		GhostsDescriptionEX description = (GhostsDescriptionEX)bCase.getDescription();
		GHOST ghost = description.getGhostType();
		
		int resultValue = game.getScore() - description.getScore();
		
		boolean isEdible = game.isGhostEdible(ghost);
		boolean wasEaten = game.wasGhostEaten(ghost);
		
		int resultLives = description.getLives() - game.getPacmanNumberOfLivesRemaining(); 
		
		int ghostDist = (int) game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), 
				   				game.getGhostLastMoveMade(ghost), DM.PATH);
		
		int distDiff = description.getGhostDistance() - ghostDist;
		
		GhostsResultEX result = (GhostsResultEX)bCase.getResult();
		result.setScore(resultValue);
		result.setWasEaten(wasEaten);
		result.setIsEdible(isEdible);
		result.setGhostDist(distDiff);
		result.setLife(resultLives);
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
			if(needToRetain(oldCase))
				retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
