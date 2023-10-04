package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class SmallEdibleTimeTransition implements Transition{

	GHOST ghost;		
	public SmallEdibleTimeTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostInput input = (GhostInput) in;
		return input.isGhostReadyToAttack(ghost) && !input.isPacmanCloseToPPill();
	}

	@Override
	public String toString() {
		return ghost + " edible time is finishing and pacman is far";
	}
}
