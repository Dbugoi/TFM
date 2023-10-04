package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AvoidPPillsAction implements Action {

	GHOST ghost;
	private static final Random rnd = new Random();

	public AvoidPPillsAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return ghost + " avoid PPill";
	}

	@Override
	public MOVE execute(Game game) {
		// Calculamos la PPill mas cerccana y eliminamos ese movimiento

		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		double distance = Integer.MAX_VALUE;
		int finalPill = -1;

		for (int pillIndex : game.getActivePillsIndices()) {
			double d = game.getDistance(ghostIndex, pillIndex, lastMove, DM.PATH);

			if (distance > d) {
				distance = d;
				finalPill = pillIndex;
			}

		}

		List<MOVE> goodMoves = new ArrayList<>(Arrays.asList(game.getPossibleMoves(ghostIndex)));

		MOVE m = game.getNextMoveTowardsTarget(ghostIndex, finalPill, DM.PATH);
		if (goodMoves.contains(m))
			goodMoves.remove(m);

		return goodMoves.get(rnd.nextInt(goodMoves.size()));
	}

}
