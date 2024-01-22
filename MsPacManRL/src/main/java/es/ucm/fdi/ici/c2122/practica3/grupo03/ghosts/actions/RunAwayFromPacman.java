package es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayFromPacman implements RulesAction {

    GHOST ghost;
	public RunAwayFromPacman(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if(game.doesGhostRequireAction(ghost)) {
			int gn = game.getGhostCurrentNodeIndex(ghost);
			int pacmannode = game.getPacmanCurrentNodeIndex();
			return game.getNextMoveAwayFromTarget(gn, pacmannode, 
					game.getGhostLastMoveMade(ghost), DM.PATH);
		}
		else {
			return MOVE.NEUTRAL;
		}
	}

	@Override
	public String getActionId() {
		return ghost+ "runsAwayFromPacman";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
