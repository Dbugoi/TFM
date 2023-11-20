package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionGhostDoesntBlockPathToPill implements Transition {

	public double thresold = 60;

	public TransitionGhostDoesntBlockPathToPill() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return !input.isGhostBlockingPathToPill();
	}

	@Override
	public String toString() {
		return "Ghost doesn't block or threaten actual path";
	}
}