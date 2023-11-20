package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Accion que dirige el fantasma hacia un punto del camino de
 * msPacMan hacia el nodo de la PPill mas cercana. 
 * Este punto es al que llegarian a la vez el fantasma y msPacMan
 */
public class InterceptAction implements RulesAction {
	GHOST ghost;
	public InterceptAction(GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
		{
			int closestPill = -1; int shortestPill = Integer.MAX_VALUE; int path;
			int mspacman = game.getPacmanCurrentNodeIndex();
			// Busca el nodo de PPill mas cercano a MsPacman(aunque ya se halla consumido la PPill)
			for(int ppNode : game.getPowerPillIndices()) {
				path = game.getShortestPathDistance(mspacman, ppNode, game.getPacmanLastMoveMade());
				if(path < shortestPill) {
					shortestPill = path;
					closestPill = ppNode;
				}
			}
			// Busca el nodo al que puede llegar a la vez el fantasma y msPacMan
			int mustBeMin = Integer.MAX_VALUE; int nodeInQuestion = -1;
			// Obtiene el nodo al que pueden llegar mas a la vez MsPacMan como el fantasma
			for(int node : game.getShortestPath(mspacman, closestPill, game.getPacmanLastMoveMade()))	
			{																	
				int distDiff = Math.abs(game.getShortestPathDistance(mspacman, node, game.getPacmanLastMoveMade()) - 
						game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), node, game.getGhostLastMoveMade(ghost)));
				if(distDiff < mustBeMin) {
					mustBeMin = distDiff;
					nodeInQuestion = node;
				}
			}
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), nodeInQuestion, 
					game.getGhostLastMoveMade(ghost), Constants.DM.PATH);		
		}
            
        return MOVE.NEUTRAL;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost + " intercepta";
	}
}
