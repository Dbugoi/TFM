package es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;

import es.ucm.fdi.ici.c2122.practica5.grupo02.GameConstants;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class GhostsStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private final static int TIME_WINDOW = 4;

	public GhostsStorageManager() {
		this.buffer = new Vector<CBRCase>();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setCaseBase(CBRCaseBase caseBase) {
		this.caseBase = caseBase;
	}

	public void reviseAndRetain(CBRCase newCase) {
		this.buffer.add(newCase);

		// Buffer not full yet.
		if (this.buffer.size() < TIME_WINDOW)
			return;

		CBRCase bCase = this.buffer.remove(0);

		reviseCase(bCase);
		retainCase(bCase);
	}

	private void reviseCase(CBRCase bCase) {
		GhostsDescription description = (GhostsDescription) bCase.getDescription();
		
		int oldScore = description.getScore(), currentScore = game.getScore();
		int oldTime = description.getTime(), currentTime = game.getTotalTime();
		int oldLives = description.getLives(), currentLives = game.getPacmanNumberOfLivesRemaining();
		
		boolean currentLair = game.getGhostLairTime(GHOST.values()[description.getGhostIndex()]) > 0;
		boolean isNowEdible = game.isGhostEdible(GHOST.values()[description.getGhostIndex()]);
		int ghostDst = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.values()[description.getGhostIndex()]), 
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.values()[description.getGhostIndex()]));
		
		int resultScore = (currentScore - oldScore)/(oldTime - currentTime + 1);
		int livesLost = oldLives - currentLives;
		boolean hasBeenEaten = currentLair;

		GhostsResult result = (GhostsResult) bCase.getResult();
		result.setScoreWeight(resultScore);
		result.setLivesLost(livesLost);
		result.setHasGhostBeenEaten(hasBeenEaten);
		result.setIsGhostEdible(isNowEdible);
		result.setGhostDst(ghostDst);
	}

	// Store the old case right now into the case base
	// Alternatively we could store all them when game finishes in close() method
	private void retainCase(CBRCase bCase) {
		// Checks if the case in hand should be retained in the data base, based
		// on the result conditions
		if (!shouldBeRetained(bCase))
			return;

		if(GameConstants.DEBUG) System.out.print("Storage\n");
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}

	private boolean shouldBeRetained(CBRCase bCase) {
		GhostsResult result = (GhostsResult) bCase.getResult();
		
		if (result.getLivesLost() > 0) return true;
		
		// if the score is too high, we don't want that case
		// TODO: 50 as a variable
		if(GameConstants.DEBUG) System.out.print(GameConstants.MAX_SCORE_WEIGHT + "\n");
		if (result.getScoreWeight() >= GameConstants.MAX_SCORE_WEIGHT) {
			if(GameConstants.DEBUG) System.out.print("Not Score\n");
			return false;
		}
		if (result.getHasGhostBeenEaten()) {
			if(GameConstants.DEBUG) System.out.print("Eaten\n");
			return false;
		}
		
		return true;
	}

	public void close() {
		for (CBRCase oldCase : this.buffer) {
			reviseCase(oldCase);
			retainCase(oldCase);
		}

		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}

}
