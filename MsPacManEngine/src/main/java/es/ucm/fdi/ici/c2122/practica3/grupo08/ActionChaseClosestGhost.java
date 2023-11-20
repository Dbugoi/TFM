package es.ucm.fdi.ici.c2122.practica3.grupo08;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ActionChaseClosestGhost implements RulesAction {
	
	public ActionChaseClosestGhost() {
	}
	
	@Override
	public MOVE execute(Game game) {
		GHOST nearestEdibleGhost = getNearestGhost(game, true);
		//Sedevuelve el mvto hacia el fantasma comestible mas cercano
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(nearestEdibleGhost), game.getPacmanLastMoveMade(), DM.PATH);
	}
	
	//Busca el fantasma mas cercano (buscar comestibles o no comestibles)
		private GHOST getNearestGhost(Game game, boolean edible) {
			GHOST nearestGhost = null;
			int minDistance = 9999999;
			
			//Se recorren los fantasmas
			for (GHOST ghostType : GHOST.values()) {
				//Si el fantasma es comestible o no (segun el parametro) y esta activo
				if(game.isGhostEdible(ghostType) == edible && game.getGhostLairTime(ghostType) <= 0) {
					//Se calcula su distancia con MsPacman
					int actualDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade());
					//Si es el mas cercano se guarda
					if(actualDistance < minDistance) {
						nearestGhost = ghostType;
						minDistance = actualDistance;
					}
				}
			}
			return nearestGhost;
		}

	@Override
	public String getActionId() {
		return "Chase Closest Ghost";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
