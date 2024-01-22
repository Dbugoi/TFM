package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.perseguir;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveTowardsPacManAction implements Action {

	GHOST ghost;

	public MoveTowardsPacManAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return ghost + " ataca";
	}

	@Override
	public MOVE execute(Game game) {
		int nextJunction = GameUtils.getDestinationJunctionForMsPacman(game);
		if (game.getGhostCurrentNodeIndex(ghost) == nextJunction)
			return GameUtils.getNextMoveTowardsMsPacMan(game, ghost, DM.PATH);
			
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		return game.getApproximateNextMoveTowardsTarget(ghostIndex, nextJunction, lastMove,
				DM.PATH);
	}

}
