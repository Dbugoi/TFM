package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Esquiva una PPill, se hace cuando se está seguro y no es necesario comer la PPill
public class AvoidPPillAction implements RulesAction {

	public AvoidPPillAction() {
	}

	@Override
	public MOVE execute(Game game) {
		int pacman = game.getPacmanCurrentNodeIndex();
		if(!game.isJunction(pacman)) 
			return game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0];
		
		float bestPills = -1;
		int[] junctions = CommonMethodsPacman.getAdjacentJunctions(game, pacman);
		int juncToTake = junctions[0];
		int closestPPill = CommonMethodsPacman.getClosestPPill(game, pacman);
		
		MOVE moveToAvoid = game.getApproximateNextMoveTowardsTarget(pacman, closestPPill,
				game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0], DM.PATH);
		for (int junc : junctions) {
			MOVE moveToConsider = game.getApproximateNextMoveTowardsTarget(pacman, junc,
					game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0], DM.PATH);
			if (moveToAvoid == moveToConsider) continue;
			int[] path = game.getShortestPath(pacman, junc);
			float pillRatio = CommonMethodsPacman.getPathNumPills(game, path) / path.length;
			if (pillRatio > bestPills) {
				juncToTake = junc;
				bestPills = pillRatio;        	
			}
		}
		return CommonMethodsPacman.avoidGhosts(game, pacman, juncToTake);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "PacmanAvoidsPPill";
	}


}
