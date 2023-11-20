package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToEdibleGhostAction implements RulesAction {

	GHOST ghost;
	int maxDist;
	public GoToEdibleGhostAction(GHOST ghost, int maxDist) {
		this.ghost = ghost;
		this.maxDist = maxDist;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
		{			
			GHOST edibleGhost = CommonMethodsGhosts.getClosestEdibleGhost(game, maxDist);
			if(edibleGhost == null) return MOVE.NEUTRAL;
			return game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost),
					game.getGhostCurrentNodeIndex(edibleGhost),
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
		return ghost+ "goesToClosestEdibleGhost";
	}


}
