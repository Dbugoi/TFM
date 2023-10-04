package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.evitarAtaque;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToBlockPPillsTransition implements Transition {

	GHOST ghost;

	public GoToBlockPPillsTransition(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) { // Nos aseguramos de que siempre haya un cruce y de que
										// llegamos antes que Pacman
		GhostInput input = (GhostInput) in;

		int lastJunction = input.getAlmostLastJunctionToClosestPowerPillToMsPacMan();
		if (lastJunction == -1)
			return false;
		// llego antes que mspacman?

		return input.isGhostCloserThanPacManTo(lastJunction, ghost)
				//&& input.canGhostBlock(ghost)
				&& !input.isGhostEdible(ghost);
	}

	@Override
	public String toString() {
		return ghost + "goes to block ppill";
	}
}
