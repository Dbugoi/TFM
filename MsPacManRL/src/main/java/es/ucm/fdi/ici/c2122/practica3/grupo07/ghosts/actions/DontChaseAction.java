package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DontChaseAction implements RulesAction {

	GHOST ghost;
	public DontChaseAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+" dontchase";
	}

	@Override
	public MOVE execute(Game game) {
		return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		
	}
}
