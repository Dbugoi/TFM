package es.ucm.fdi.ici.c2122.practica3.grupo08;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionGoToPillAvoidingPPill implements RulesAction{
	
	public ActionGoToPillAvoidingPPill() {
		
	}
	
	@Override
	public MOVE execute(Game game) {
		
		MOVE move = MOVE.DOWN;
		
		//Inicializamos variables con valores altos para las comparaciones de distancia
		int[] closestPills = new int[2];
		double[] closestPillsDistances = new double[2];
		closestPillsDistances[0] = 999999.;
		closestPillsDistances[1] = 999999.;
		
		//Vemos cuales son las dos pills mas cercanas a MsPacMan
		for(int pill : game.getActivePillsIndices()) {
			double pillDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade());
			
			if(pillDistance < closestPillsDistances[0]) {
				closestPillsDistances[0] = pillDistance;
				closestPills[0] = pill;
			}
			else if(pillDistance < closestPillsDistances[1]) {
				closestPillsDistances[1] = pillDistance;
				closestPills[1] = pill;
			}
		}
		
		//Si hay PowerPills activas intentaremos no coger caminos que lleven a la mas cercana si es posible elegir
		if(game.getActivePowerPillsIndices().length > 0) {
			int[] activePowerPills = game.getActivePowerPillsIndices();
			double minDistance = 999999.;
			int closestPowerPill = 0;
			
			for(int powerPill : activePowerPills) {
				double powerPillDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), powerPill, game.getPacmanLastMoveMade());
				
				if(powerPillDistance < minDistance) {
					minDistance = powerPillDistance;
					closestPowerPill = powerPill;
				}
			}
			
			double firstDistance = game.getDistance(closestPowerPill, closestPills[0], DM.PATH);
			double secondDistance = game.getDistance(closestPowerPill, closestPills[1], DM.PATH);
			
			//Filtrar el camino para procurar no coger el camino que lleva a una PowerPill
			if(firstDistance < secondDistance)
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPills[1], DM.PATH);
			else
				move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPills[0], DM.PATH);
				
		}
		//Si no quedan PowerPills activas vamos a comer la PowerPill mas cercana
		else move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), closestPills[0], DM.PATH);
		
		//GameView.addLines(game, Color.yellow, game.getPacmanCurrentNodeIndex(), closestPills[0]);
		//GameView.addLines(game, Color.yellow, game.getPacmanCurrentNodeIndex(), closestPills[1]);
		
		return move;
	}
	
	@Override
	public String getActionId() {
		return "Go to Pill Avoiding Power Pill";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}	
}
