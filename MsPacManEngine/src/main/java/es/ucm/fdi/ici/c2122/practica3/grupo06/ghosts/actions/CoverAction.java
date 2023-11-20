package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.*;
import pacman.game.Constants;
import pacman.game.Game;

/**
 * Accion que dirige el fantasma hacia el fantasma vivo mas cercano (si lo hay)
 */
public class CoverAction implements RulesAction{
	GHOST ghost;
	public CoverAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		GHOST ng = null;
        if (game.doesGhostRequireAction(ghost) && (ng = getNearestGhostAlive(ghost,game)) != null)        //if it requires an action and 
        {
                return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                		game.getGhostCurrentNodeIndex(ng), game.getGhostLastMoveMade(ghost), DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost+ "runs towards other ghost to cover himself";
	}
	
	/** 
	 * Obtener al fantasma mas cercano a otro fantasma
	 * 
	 * @param yourself : El fantasma respecto al cual se calcula el fantasma mas cercano
	 * @param game : Referencia a game
	 * 
	 * TODO: Poner en un Utils todos estos metodos
	 * 
	 */
	private GHOST getNearestGhostAlive(GHOST yourself, Game game) {
        GHOST nGhost = null; int minDist = Integer.MAX_VALUE;
        for(Constants.GHOST g: Constants.GHOST.values()) {
        	if(g == yourself || game.isGhostEdible(g)) continue;
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
