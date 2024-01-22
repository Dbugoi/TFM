package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToAvoidPPillsTransition implements Transition {

	GHOST ghost;
	String origen;
	public GoToAvoidPPillsTransition(GHOST ghost, String origen) {
		super();
		this.ghost = ghost;
		this.origen = origen;
	}
	
	@Override	
	public boolean evaluate(Input in) { 
		GhostInput input = (GhostInput) in;	
        return input.isGhostEdible(ghost) && !input.isThereGhostProtector() 
        		&& input.isGhostCloseToPPill(ghost) && input.isPacmanCloseToGhost(ghost); 
	}

	@Override
	public String toString() {
		return "Ghost close to PPill" + origen;
	}
}
