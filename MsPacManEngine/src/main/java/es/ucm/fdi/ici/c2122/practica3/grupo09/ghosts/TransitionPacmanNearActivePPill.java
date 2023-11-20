package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class TransitionPacmanNearActivePPill implements Transition {

	private GHOST ghost;

	public TransitionPacmanNearActivePPill(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;
		return input.pacmanCloseToPPill(ghost);
	}

	@Override
	public String toString() {
		return "Pacman close to ppill";
	}
}
