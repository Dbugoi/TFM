package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.ghosts.GhostsInput;

public class TransitionEveryGhostEdible implements Transition {
	public TransitionEveryGhostEdible() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.isEveryGhostEdible();
	}

	@Override
	public String toString() {
		return "Every ghost edible";
	}
}
