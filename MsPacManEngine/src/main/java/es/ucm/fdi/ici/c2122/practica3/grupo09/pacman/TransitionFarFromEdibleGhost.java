package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionFarFromEdibleGhost implements Transition {

	public double thresold = 60, thresoldG = 100;

	public TransitionFarFromEdibleGhost() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.minDistanceFromPacmanToEGhost() > thresold && input.minDistanceFromGhostToPacman() > thresoldG;
	}

	@Override
	public String toString() {
		return "MsPacMan far from edible ghost";
	}
}