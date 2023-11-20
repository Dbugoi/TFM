package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import java.awt.Color;

import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.Action;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class ActionLureToGhost implements RulesAction {

	GHOST ghost;
	int objetivo;

	public ActionLureToGhost(GHOST ghost) {
		this.ghost = ghost;
		objetivo = 0;
	}

	@Override
	public MOVE execute(Game game) {

		if (game.doesGhostRequireAction(ghost)) // if it requires an action
		{

			// siempre va a ver un fantasma objetivo
			int distancia = -1;
			// fantasma not edible ams cercano
			for (GHOST ghost2 : GHOST.values()) {
				if (game.doesGhostRequireAction(ghost2)) {
					if (!game.isGhostEdible(ghost2)) {

						if (distancia == -1) {
							distancia = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
									game.getGhostCurrentNodeIndex(ghost2));
							objetivo = game.getGhostCurrentNodeIndex(ghost2);
						} else {
							int aux = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
									game.getGhostCurrentNodeIndex(ghost2));
							if (aux < distancia) {
								distancia = aux;
								objetivo = game.getGhostCurrentNodeIndex(ghost2);
							}
						}
					}
				}

			}

			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), objetivo,
					game.getGhostLastMoveMade(ghost), DM.PATH);
		}
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "lure to Ghost";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}

}
