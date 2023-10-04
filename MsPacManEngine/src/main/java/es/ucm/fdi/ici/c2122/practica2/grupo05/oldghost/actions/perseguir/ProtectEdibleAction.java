package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.perseguir;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PathDistance;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class ProtectEdibleAction implements Action {

	GHOST ghost;

	public ProtectEdibleAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return ghost + " protege";
	}

	@Override
	public MOVE execute(Game game) {

		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);

		double dist = Integer.MAX_VALUE;
		int indexFinal = -1;
		for (Constants.GHOST g : Constants.GHOST.values()) { // Buscamos el ghost edible mas cerca
																// de pacman
			int gIndex = game.getGhostCurrentNodeIndex(g);
			double d = PathDistance.fromPacmanToGhost(game, g);

			if (game.isGhostEdible(g) && d < dist) {
				dist = d;
				indexFinal = gIndex; // indexFinal nunca deberia ser -1, si ejecutamos esta accion es
									// pq alguno hay
			}
		}

		return game.getNextMoveTowardsTarget(ghostIndex, indexFinal, lastMove, DM.PATH);
	}

}
