package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.ghosts.GhostsInput;
import pacman.game.Constants.GHOST;

public class TransitionFarFromPacman implements Transition {
	private GHOST ghost;

	public TransitionFarFromPacman(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.getGhostClosestToPacman() != ghost.ordinal();
	}

	@Override
	public String toString() {
		return "Furthest ghost";
	}
}
