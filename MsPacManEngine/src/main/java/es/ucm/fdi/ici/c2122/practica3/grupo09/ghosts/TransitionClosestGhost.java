package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class TransitionClosestGhost implements Transition {
	private GHOST ghost;
	int numero;

	public TransitionClosestGhost(GHOST ghost, int n) {
		super();
		this.ghost = ghost;
		numero = n;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getGhostClosestToPacman() == ghost.ordinal();
	}

	@Override
	public String toString() {
		return "Closest ghost" + numero;
	}
}
