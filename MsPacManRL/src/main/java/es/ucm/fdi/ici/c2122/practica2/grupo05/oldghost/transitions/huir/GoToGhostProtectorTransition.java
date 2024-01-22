package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToGhostProtectorTransition implements Transition{

	GHOST ghost;
	String origen;
	public GoToGhostProtectorTransition(GHOST ghost, String origen) {
		super();
		this.ghost = ghost;
		this.origen = origen;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostInput input = (GhostInput) in;
		return input.isGhostEdible(ghost) && input.isPacmanCloseToGhost(ghost) && input.isThereGhostProtector();
	}

	@Override
	public String toString() {
		return "Ghost Protector available " + origen;
	}
}
