package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;

import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class RunAwayToPPillAction extends RunAwayAction {
	@Override
	public String getActionId() { return "Ms PacMan runs for dear life towards power pill."; }

	@Override
	public MOVE execute(Game game) {
		int target;
		if (game.getNumberOfActivePowerPills() > 0) { target = GameUtils.getInstance().getNearestPowerPillIndex(game); }
		else { target = GameUtils.getInstance().getNearestPillIndex(game); }
		GameUtils.debugPrint("trying to run towards ppill.\n");
		return selectPathToRunAway(game, target);
	}
}
