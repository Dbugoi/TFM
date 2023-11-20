package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ReachClosestPowerPill implements RulesAction {

	public ReachClosestPowerPill() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "ReachClosestPowerPill";
	}

	@Override
	public MOVE execute(Game game) {
		int pcNode = game.getPacmanCurrentNodeIndex();
		int[] activePowerPills = game.getActivePowerPillsIndices();
		int nearestPP = -1, shortestDist = -1;

		for (int activePill : activePowerPills) {
			int distance = game.getShortestPathDistance(pcNode, activePill, game.getPacmanLastMoveMade());
			//For every PPill we check which one is the nearest and whose path is not
			//blocked.
			if (nearestPP == -1 || distance < shortestDist) {
					nearestPP = activePill;
					shortestDist = distance;
			}
		}
		
		//If there's not PP, we use "move neutral" as a save movement, so we can change and ran away 
		if (nearestPP == -1)
			return MOVE.NEUTRAL;
		else
			return game.getApproximateNextMoveTowardsTarget(pcNode,	nearestPP, game.getPacmanLastMoveMade(), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
