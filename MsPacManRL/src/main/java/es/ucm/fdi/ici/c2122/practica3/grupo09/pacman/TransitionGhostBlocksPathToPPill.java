package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionGhostBlocksPathToPPill implements Transition {

	public double thresold = 60;

	public TransitionGhostBlocksPathToPPill() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.isGhostBlockingPathToPPill();
	}

	@Override
	public String toString() {
		return "Ghost blocks or threatens actual path";
	}
}