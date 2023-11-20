package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionNearPowerPill implements Transition {
	public double thresold = 30, threshGhost = 150;

	public TransitionNearPowerPill() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return (input.PPillsRemaining() > 0 && input.minDistanceFromPacmanToPPill() <= thresold
				&& input.minDistanceFromGhostToPacman() < threshGhost);
	}

	@Override
	public String toString() {
		return "MsPacman close to PowerPill";
	}
}