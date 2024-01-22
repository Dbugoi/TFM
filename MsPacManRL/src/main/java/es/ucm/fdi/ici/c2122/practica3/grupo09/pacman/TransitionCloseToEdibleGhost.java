package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionCloseToEdibleGhost implements Transition {

	public double thresold = 150;

	public TransitionCloseToEdibleGhost() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return (!input.isGhostBlockingPathToEGhost() && input.minDistanceFromPacmanToEGhost() <= thresold);
	}

	@Override
	public String toString() {
		return "MsPacMan close to edible ghost";
	}
}