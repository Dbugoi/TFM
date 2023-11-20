package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.*;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Accion que separa a un fantasma del fantasma mas cercano a el
 */
public class DisperseAction implements RulesAction {

    GHOST ghost;
	public DisperseAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
                return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
                        game.getGhostCurrentNodeIndex(getNearestGhost(ghost, game)) , game.getGhostLastMoveMade(ghost), DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	@Override
	public String getActionId() {
		return ghost+ "runs away from another ghost";
	}
	
	/** 
	 * Obtener al fantasma mas cercano a otro fantasma
	 * 
	 * @param yourself : El fantasma respecto al cual se calcula el fantasma mas cercano
	 * @param game : Referencia a game
	 * 
	 */
	private GHOST getNearestGhost(GHOST yourself, Game game) {
        GHOST nGhost = null; int minDist = Integer.MAX_VALUE;
        for(Constants.GHOST g: Constants.GHOST.values()) {
        	if(g == yourself) continue;
            int ghost = game.getGhostCurrentNodeIndex(g);
            int mspacman = game.getPacmanCurrentNodeIndex();
            int dist = game.getShortestPathDistance(mspacman, ghost);
            if(dist < minDist){
                minDist = dist;
                nGhost = g;
            }
        }
        return nGhost;
    }
}
