package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.pacman.MsPacManInput;

public class TransitionCloseToNonEdibleGhost implements Transition {
	public double thresold = 150;
	int numero;

	public TransitionCloseToNonEdibleGhost(int number) {
		super();
		numero = number;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput) in;
		return input.minDistanceFromGhostToPacman() <= thresold;
	}

	@Override
	public String toString() {
		return "Non edible Ghost close to MsPacMan" + numero;
	}
}