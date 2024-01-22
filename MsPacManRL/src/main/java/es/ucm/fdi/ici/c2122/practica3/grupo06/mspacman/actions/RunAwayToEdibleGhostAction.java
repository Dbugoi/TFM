package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;

import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class RunAwayToEdibleGhostAction extends RunAwayAction{
	@Override
	public String getActionId() { return "Ms PacMan runs for dear life towards Edible Ghost."; }

	@Override
	public MOVE execute(Game game) {
		return selectPathToRunAway(game, GameUtils.getInstance().getNearest(game,
				GameUtils.getInstance().getEdibleGhosts(game)));
	}

}
