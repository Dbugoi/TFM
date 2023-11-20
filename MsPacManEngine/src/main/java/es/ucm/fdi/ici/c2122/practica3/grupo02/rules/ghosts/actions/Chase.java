package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo02.GameConstants;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.Value;
import jess.JessException;

import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;

public class Chase implements RulesAction {

	enum CHASE_TYPE { NONE, PRIMARY, SECONDARY }

	CHASE_TYPE chaseType;
	GHOST ghostType;

	public Chase(GHOST ghostType) {
		super();
		this.ghostType = ghostType;
		this.chaseType = CHASE_TYPE.NONE;
	}

	@Override
	public String getActionId() {
		return ghostType + "Chase";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghostType)) {
			int pacmanNode = game.getPacmanCurrentNodeIndex();
			int ghostNode = game.getGhostCurrentNodeIndex(ghostType);
			MOVE lastMove = game.getGhostLastMoveMade(ghostType);

			if (GameConstants.DEBUG)
				GameView.addPoints(game, GameConstants.colours[ghostType.ordinal()],
						game.getShortestPath(ghostNode, pacmanNode));
			
			MOVE nextMove;
			switch(chaseType) {
			case PRIMARY:
				nextMove = chasePrimaryPath(game, ghostNode, pacmanNode, lastMove);
				break;
			case SECONDARY:
				nextMove = chaseSecondaryPath(game, ghostNode, pacmanNode, lastMove);
				break;
			case NONE:
				default:
				nextMove = MOVE.NEUTRAL;
				break;
			}
		
			return nextMove;
		} else
			return MOVE.NEUTRAL;
	}

	private MOVE chasePrimaryPath(Game game, int ghostNode, int pacmanNode, MOVE lastMove) {
		return game.getApproximateNextMoveTowardsTarget(ghostNode, pacmanNode, lastMove, DM.PATH);
	}
	
	private MOVE chaseSecondaryPath(Game game, int ghostNode, int pacmanNode, MOVE lastMove) {
		MOVE[] possibleMoves = game.getPossibleMoves(ghostNode);
    	int numChoices = possibleMoves.length;

    	// if two possible moves, we go the opposite way (away instead of towards)
		if (numChoices == 2) 
			return game.getApproximateNextMoveAwayFromTarget(ghostNode, pacmanNode, lastMove, DM.PATH);
		else { 
			// if three possible moves, we go to the one that isn't the furthest nor the closest to the Pacman
			MOVE optimalMove = game.getApproximateNextMoveTowardsTarget(ghostNode, pacmanNode, lastMove, DM.PATH);

			MOVE awayMove = game.getApproximateNextMoveAwayFromTarget(ghostNode, pacmanNode, lastMove, DM.PATH);

			// we select the one that isn't the furthest nor the closest
			for (MOVE move : possibleMoves) { // i know at least one of them is going to be returned
				if (move != optimalMove && move != awayMove)
					return move;
			}
		}
		
		// never used, just put there because Java is a very clever and special language with 0 problems at all haha
		return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("chaseType");
			if (value == null)
				return;

			Integer chase = value.intValue(null);
			chaseType = CHASE_TYPE.values()[chase];
		} catch (JessException e) {
			e.printStackTrace();
		}
	}
}
