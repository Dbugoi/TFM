package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import java.awt.Color;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class ActionGoToNearestPowerPill implements RulesAction {

	@Override
	public String getActionId() {
		return "Going to nearest PowerPill";
	}

	@Override
	public MOVE execute(Game game) {
		// BUSCA LA PP MAS CERCANA Y VA A POR ELLA POR EL CAMINO M�S CORTO
		int msPacman = game.getPacmanCurrentNodeIndex();

		int[] activePills;
		activePills = game.getActivePowerPillsIndices();

		int pill = -1;
		int dist = -1;
		for (int i = 0; i < activePills.length; i++) {
			int path = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), activePills[i],
					game.getPacmanLastMoveMade());
			if (dist == -1 || path < dist) {
				dist = path;
				pill = activePills[i];
			}
		}
		if (pill >= 0) {
			// GameView.addLines(game, Color.CYAN, game.getPacmanCurrentNodeIndex(), pill);
			return game.getApproximateNextMoveTowardsTarget(msPacman, pill, game.getPacmanLastMoveMade(), DM.PATH);
		} else {
			return MOVE.NEUTRAL;
		}
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}
}
