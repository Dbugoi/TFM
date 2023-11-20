package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPacmanInterAction implements RulesAction {

	GHOST ghost;
	public GoToPacmanInterAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
		{
			return game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost),
					CommonMethodsGhosts.mspacmanClosestJunction(game),
					game.getGhostLastMoveMade(ghost),
					Constants.DM.PATH);
		}

		return MOVE.NEUTRAL;	
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost+ "goesToIntersectionClosestToPacman";
	}
	
	
}
