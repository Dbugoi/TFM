package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GhostsEdibleTransition implements Transition  {

	GHOST ghost;
	String origen;
	public GhostsEdibleTransition(GHOST ghost, String origen) {
		super();
		this.ghost = ghost;
		this.origen = origen;
	}


	@Override
	public boolean evaluate(Input in) {
		GhostInput input = (GhostInput)in;
		return input.isGhostEdible(ghost) && !input.isGhostReadyToAttack(ghost); 
	}


	@Override
	public String toString() {
		return ghost + " is edible " + origen;
	}	
	
}
