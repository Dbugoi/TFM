package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionDoesntExistPowerPill implements Transition {
	public TransitionDoesntExistPowerPill() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return (input.PPillsRemaining() <= 0);
	}

	@Override
	public String toString() {
		return "No PowerPills remaining";
	}
}