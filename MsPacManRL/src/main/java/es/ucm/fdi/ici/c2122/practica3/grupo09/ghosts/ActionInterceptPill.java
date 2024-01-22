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

public class ActionInterceptPill implements RulesAction {

	GHOST ghost;

	public ActionInterceptPill(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost)) // if it requires an action
		{

			int pacman = game.getPacmanCurrentNodeIndex();

			int pPill = searchNearestPowerPill(game, pacman, game.getPacmanLastMoveMade());

			if (pPill != -1) {
				int[] pPath = game.getShortestPath(pacman, pPill, game.getPacmanLastMoveMade());

				// calcular la distancia de todos los nodos de pPath al fantasma
				// i 0 es distancia 0;

				int distancia;
				for (int i = 0; i < pPath.length; i++) {
					distancia = game.getShortestPathDistance(pPath[i], game.getGhostCurrentNodeIndex(ghost),
							game.getGhostLastMoveMade(ghost));
					if (distancia < i) {
						// el movimiento
						return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), pPath[i],
								game.getGhostLastMoveMade(ghost), DM.PATH);
					}

				}
			}

			// que no existe?
			// entonces no es el tipo de comportamineto que deberia estar haciendo
			// que hago lo que quiera

		}
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "intercept";
	}

	// Busca ppil mas cercana
	private int searchNearestPowerPill(Game game, int pacIndex, MOVE pacDir) {

		// Buscamos ppil mas cercana
		int distance = 100;
		int[] activePills = game.getActivePowerPillsIndices();
		int pill = -1;
		for (int i = 0; i < activePills.length; i++) {
			int path = game.getShortestPathDistance(pacIndex, activePills[i], pacDir);
			if (path < distance) {
				distance = path;
				pill = activePills[i];
			}
		}
//					if (pill != -1)
//						GameView.addLines(game, Color.CYAN, game.getPacmanCurrentNodeIndex(), pill);
		return pill;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}
}
