package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class OutOfCaseAction implements Action{

	GHOST ghost;
	public OutOfCaseAction(GHOST ghost) {
    	this.ghost = ghost;
    }
	@Override
	public String getActionId() {
		return ghost + " ready";
	}

	@Override
	public MOVE execute(Game game) { 
		return game.getNextMoveTowardsTarget(
				game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), DM.PATH);
	}

}
