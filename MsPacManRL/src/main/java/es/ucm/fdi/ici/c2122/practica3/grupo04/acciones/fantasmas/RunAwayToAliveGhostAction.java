package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayToAliveGhostAction implements RulesAction {

	GHOST ghost;
	int maxDist;
	public RunAwayToAliveGhostAction(GHOST ghost, int maxDistance) {
		maxDist = maxDistance;
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
		{
			GHOST g = CommonMethodsGhosts.getNearestGhostNotEdible(game, game.getGhostCurrentNodeIndex(ghost), maxDist);
			if(g != null) {
				return game.getNextMoveTowardsTarget(
						game.getGhostCurrentNodeIndex(ghost),
						game.getGhostCurrentNodeIndex(g),
						Constants.DM.PATH);
			}
			//return CommonMethodsGhosts.escapeToFurthestJunction(game, ghost);
			return game.getNextMoveAwayFromTarget(
					game.getGhostCurrentNodeIndex(ghost),
					game.getPacmanCurrentNodeIndex(),
					Constants.DM.PATH);
		}
		//alejarse de pacman cuando esta muy cerca
		//separarse de otros fantasmas comestibles que esten cerca
		//huir hacia un fantasma no comestible

		return MOVE.NEUTRAL;	
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost+ "goesToPPill";
	}
}
