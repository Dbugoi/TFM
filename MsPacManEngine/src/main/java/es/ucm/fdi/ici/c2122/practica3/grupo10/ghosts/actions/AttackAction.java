package es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AttackAction implements RulesAction {

	GHOST ghost;

	public AttackAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost)) // if it requires an action
		{
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
		}
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "ataca";
	}

	@Override
	public void parseFact(Fact actionFact) {
		

	}
}
