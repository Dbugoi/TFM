package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.evitarAtaque;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToCloseToCaseTransition implements Transition{

	GHOST ghost;
	public GoToCloseToCaseTransition( GHOST ghost) {
		super();
		this.ghost = ghost;
	}
	
	@Override
	public boolean evaluate(Input in) {
		GhostInput input = (GhostInput) in;
		GoToBlockPPillsTransition t1 = new GoToBlockPPillsTransition(ghost);
		return !t1.evaluate(input) && !input.isGhostEdible(ghost);
	}

	@Override
	public String toString() {
		return ghost + "goes close to case";
	}
}
