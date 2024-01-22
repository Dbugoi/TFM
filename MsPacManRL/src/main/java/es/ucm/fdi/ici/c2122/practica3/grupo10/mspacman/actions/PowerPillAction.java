package es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PowerPillAction implements RulesAction {
	
	GHOST[] ghosts = GHOST.values();
	ArrayList<MOVE> possibleMoves;
	public PowerPillAction() {
	}

	@Override
	public MOVE execute(Game game) {
		int posPower[] = game.getActivePowerPillsIndices();
		MOVE nextMove = null;
		if (posPower.length > 0 && allGhostsOut(game) && !anyGhostEdible(game)) {
			int distance = 999999;
			for (int i = 0; i < posPower.length; i++) {
				int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), posPower[i], game.getPacmanLastMoveMade());
				if (aux < distance && isSafe(game, posPower[i], aux)) {
					MOVE resMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), posPower[i], game.getPacmanLastMoveMade(), DM.PATH);
					if (possibleMoves.contains(resMove)) {
						distance = aux;
						nextMove = resMove;
					}
				}
			}
		}
		return nextMove;
	}

	@Override
	public void parseFact(Fact actionFact) {
		//get the list of possible moves from the fact slot
		try {
			possibleMoves = new ArrayList<MOVE>();
			String[] parts = actionFact.getSlotValue("moves").toString().split("-");
			for (String s: parts) {
				possibleMoves.add(MOVE.valueOf(s));
			}
		} catch (JessException e) {
			System.err.println(" :Exception parsing fact");
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return "chases";
	}

	/**
     * checks if all ghosts are out of the lair
     *
     * @param game 
     */
	//
	private boolean allGhostsOut(Game game) {
		boolean res = true;
		for (GHOST g: ghosts) {
			if(game.getGhostLairTime(g) != 0) res = false;
		}
		return res;
	}
	
	/**
     * checks if there are any edible ghosts
     *
     * @param game 
     */
	//
	private boolean anyGhostEdible(Game game) {
		boolean result = false;
		for (GHOST g : GHOST.values()) {
			if(game.isGhostEdible(g)) result = true;
		}
		return result;
	}
	
	/**
     * Checks if a index can be reached before other dangerous ghosts get there first
     *
     * @param game 
     * @param i Index to be reached
     * @param distance Distance to index
     */
	//
	private boolean isSafe(Game game, int i, int distance) {
		boolean result = true;
		for (GHOST g: ghosts) {
			if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
				if (distance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), i, game.getGhostLastMoveMade(g))) result = false;
			}
		}
		return result;
	}

}
