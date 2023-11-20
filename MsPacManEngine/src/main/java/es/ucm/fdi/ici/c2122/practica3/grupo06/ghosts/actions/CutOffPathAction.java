package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Accion que dirige el fantasma hacia la interseccion mas cercana a msPacMan
 */
public class CutOffPathAction implements RulesAction {
	GHOST ghost;
	public CutOffPathAction(GHOST ghost) {
		this.ghost = ghost;
	}
	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
		{
			// Obtenemos interseccion mas cercana
		 	int closestDist = Integer.MAX_VALUE; int closestJunc = -1; 
		 	int mspacman = game.getPacmanCurrentNodeIndex();
			for(int junc : game.getJunctionIndices()) {
				if(game.getShortestPathDistance(mspacman, junc, game.getPacmanLastMoveMade()) < closestDist) {
					closestDist = game.getShortestPathDistance(mspacman, junc);
					closestJunc = junc;
				}
			}
			
			Constants.MOVE moveToReturn = game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost), mspacman, game.getGhostLastMoveMade(ghost), Constants.DM.PATH);
			
			// Esta en posicion de cortarle el camino directamente
			if(game.getShortestPathDistance(closestJunc, mspacman) <= 10)  return moveToReturn;
			
			// Si no, primero comprobamos que no pasa directamente por pacman hasta llegar a la siguiente intrsección
			boolean throughPacMan = false;
			int[] proposedPath = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), mspacman, game.getGhostLastMoveMade(ghost));
			for(int i = 1; i < proposedPath.length; ++i) {
				if(game.isJunction(proposedPath[i])) break;	// Ha llgeado a la siguiente junction
				if(mspacman == proposedPath[i]) throughPacMan = true;	// Pasa por Ms PacMan
			}
			
			moveToReturn = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), 
					closestJunc, game.getGhostLastMoveMade(ghost), Constants.DM.PATH);
			
			// Si no va directamente por pacman es valido
			if(!throughPacMan) return moveToReturn;
			else {	// Si no cogemos aquella otra alternativa que nos deje mas cerca de pacman	
				int possibilities[] = game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(ghost));
				closestDist = (int)(proposedPath.length * 1.5f);	// Como mucho puede ser la mitad más grande, que si no nos desviamos mucho
				for(int possi : possibilities) {
					if(possi == proposedPath[0]) continue;	// es la del camino que pasa  atraves de Ms PacMan
					if(game.getShortestPathDistance(possi, closestJunc, game.getGhostLastMoveMade(ghost)) < closestDist) {
						// Calculamos desde esta posibilidad la distancia a la junction y si es menor nos vale
						// El problema es que no calculamos si de esta manera tambien pasa por PacMan pero es un comienzo
						closestDist = game.getShortestPathDistance(possi, closestJunc, game.getGhostLastMoveMade(ghost));
						moveToReturn = game.getMoveToMakeToReachDirectNeighbour(game.getGhostCurrentNodeIndex(ghost), possi);
					}
				}
				return moveToReturn;
			}       
		}
            
        return MOVE.NEUTRAL;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost + " cuts Ms Pac Man off";
	}
}
