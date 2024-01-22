package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionRunAway implements RulesAction {

	GHOST ghost;

	public ActionRunAway(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost)) // if it requires an action
		{
			return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
		}

		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "runsAway";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}
}
