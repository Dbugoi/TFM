package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class OutOfCaseTransition implements Transition{

	GHOST ghost;
	public OutOfCaseTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {	
		GhostInput input = (GhostInput) in;
		return input.isGhostJustGotOutOfCase(ghost);
	}

	@Override
	public String toString() {
		return ghost + " sale de la carcel";
	}
}
