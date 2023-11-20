package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class ChasePacmanAction implements RulesAction {

    GHOST ghost;
	public ChasePacmanAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	int msPacman = game.getPacmanCurrentNodeIndex();
            return game.getApproximateNextMoveTowardsTarget(
                	game.getGhostCurrentNodeIndex(ghost),
                	msPacman,
                	game.getGhostLastMoveMade(ghost),
                	Constants.DM.PATH);
        }
            
        return MOVE.NEUTRAL;	
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost+ "chasePacman";
	}
	
}
