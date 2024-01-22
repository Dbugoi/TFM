package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SeparatePacmanAction implements RulesAction {

    GHOST ghost;
	public SeparatePacmanAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	int pos = game.getGhostCurrentNodeIndex(ghost);
        	MOVE lastMove = game.getGhostLastMoveMade(ghost);
        	int[] adjs = CommonMethodsGhosts.getAdjacentJunctions(game, pos, lastMove);
        	int endJunc = -1;
        	for(int i = 0; i < adjs.length; ++i) {
        		if(!CommonMethodsGhosts.pacmanInPath(game, pos, adjs[i], lastMove)) {
        			endJunc = adjs[i];
        			break;
        		}
        	}
            return game.getNextMoveTowardsTarget(pos, endJunc, DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	@Override
	public String getActionId() {
		return ghost+ "imitatesPacman";
	}
	
	
}
