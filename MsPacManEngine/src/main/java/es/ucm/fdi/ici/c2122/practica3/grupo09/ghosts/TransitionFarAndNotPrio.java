package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.c2122.practica2.grupo09.ghosts.GhostsInput;
import pacman.game.Constants.GHOST;

public class TransitionFarAndNotPrio implements Transition {
	private GHOST ghost;

	public TransitionFarAndNotPrio(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;

		return input.getGhostClosestToPacman() != ghost.ordinal() && ghost.ordinal() >= GHOST.values().length / 2;
	}

	@Override
	public String toString() {
		return "Far and not prio";
	}
}
