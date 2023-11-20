package es.ucm.fdi.ici.c2122.practica3.grupo08;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeToClosestPPill implements RulesAction {
	
	public ActionFleeToClosestPPill() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public MOVE execute(Game game) {
		int[] activePowerPills = game.getActivePowerPillsIndices();
		double minDistance = 999999.;
		int closestPowerPill = 0;
		
		//Se recorren todas las power pill activas y se guarda la mas cercana
		for(int powerPill : activePowerPills) {
			double powerPillDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), powerPill, game.getPacmanLastMoveMade());
			
			if(powerPillDistance < minDistance) {
				minDistance = powerPillDistance;
				closestPowerPill = powerPill;
			}
		}
		
		//se devuelve el siguiente movimiento 
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				closestPowerPill, game.getPacmanLastMoveMade(), DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Go to Closest Power Pill While Flee";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
