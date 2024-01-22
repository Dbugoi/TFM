package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.perseguir;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToDefendEdibleTransition implements Transition{

	GHOST ghost;
	String origen;
	public GoToDefendEdibleTransition( GHOST ghost, String origen) {
		super();
		this.ghost = ghost;
		this.origen = origen;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostInput input = (GhostInput) in;	
		return !input.isGhostEdible(ghost) && input.isThereEdible();  
	}

	@Override
	public String toString() {
		return ghost + " defiende a otro ghost" + origen;
	}
}
