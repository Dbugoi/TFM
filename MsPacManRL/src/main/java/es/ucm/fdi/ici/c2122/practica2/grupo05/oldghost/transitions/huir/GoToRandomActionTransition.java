package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToRandomActionTransition implements Transition {

	GHOST ghost;
	String origen;
	public GoToRandomActionTransition(GHOST ghost, String origen) {
		super();
		this.ghost = ghost;
		this.origen = origen;
	}

	@Override
	public boolean evaluate(Input in) {	
		GhostInput input = (GhostInput) in;
		GoToGhostProtectorTransition t1 = new GoToGhostProtectorTransition(ghost, "FromRandom");
		GoToAvoidPPillsTransition t2 = new GoToAvoidPPillsTransition(ghost, "FromRandom");
		GoToAvoidEdiblesTransition t3 = new GoToAvoidEdiblesTransition(ghost, "FromRandom");
		
		return !t1.evaluate(input) && !t2.evaluate(input) && !t3.evaluate(input);
	}

	@Override
	public String toString() {
		return "Ghost safe, random move " + origen;
	}
}
