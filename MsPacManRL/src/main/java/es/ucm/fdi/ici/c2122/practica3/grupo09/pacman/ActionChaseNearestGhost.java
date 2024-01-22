package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionChaseNearestGhost implements RulesAction {

	GHOST ghost;

	public ActionChaseNearestGhost() {

	}

	@Override
	public MOVE execute(Game game) {
		// Busca al fantasma más cercano
		int ghostNode = -1;
		double distance = 100;
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) <= 0) {
				distance = Math.min(game.getDistance(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade(), DM.PATH), distance);
				ghostNode = game.getGhostCurrentNodeIndex(ghostType);
				ghost = ghostType;
			}
		}

		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), ghostNode,
				game.getPacmanLastMoveMade(), DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Pacman chases " + ghost;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}

}
