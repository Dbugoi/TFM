package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;

public class EatenMsPacManTransition implements Transition{
	
	@Override
	public boolean evaluate(Input in) {	
		GhostInput input = (GhostInput) in;
		return input.wasPacManEaten();
	}

	@Override
	public String toString() {
		return "MsPacMan pierde vida";
	}
}
