package es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.actions;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseAction implements RulesAction {
	
	GHOST[] ghosts = GHOST.values();
	ArrayList<MOVE> possibleMoves;
	public ChaseAction() {
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = null;
		if (anyGhostEdible(game)) {
			int distance = 999999;
			for (int i = 0; i < ghosts.length; i++) {
				if(game.isGhostEdible(ghosts[i])) {
					int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghosts[i]), game.getPacmanLastMoveMade());
					if (aux < distance && isSafe(game, ghosts[i], aux)) {
						
						MOVE resMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghosts[i]), game.getPacmanLastMoveMade(), DM.PATH);
						if (possibleMoves.contains(resMove)) {
							distance = aux;
							nextMove = resMove;
						}
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
     * checks if a ghost can be reached before other dangerous ghosts get there firsts
     *
     * @param game 
     * @param ghost Target ghost being tested
     * @param distance Distance from MsPacman to the ghost
     */
	//
	//this method 
	private boolean isSafe(Game game, GHOST ghost, int distance) {
		boolean result = true;
		if(2*distance > game.getGhostEdibleTime(ghost)) {
			//System.out.println(ghost + " TIME " + game.getGhostEdibleTime(ghost) + " DIST " + 2 * distance);
			result = false;
		}
		for (GHOST g: ghosts) {
			if (g != ghost && !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
				if (distance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(g))) result = false;
			}
		}
		return result;
	}

}
