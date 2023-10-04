package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.perseguir;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class GoToPerseguirTransition implements Transition {

	GHOST ghost;
	String origen;

	public GoToPerseguirTransition(GHOST ghost, String origen) {
		super();
		this.ghost = ghost;
		this.origen = origen;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostInput input = (GhostInput) in;
		if (!input.isGhostInCase(ghost))
			return !input.isGhostCloseToPacman(ghost) ||
					!input.anyOtherChaseGhostIsInPath(ghost); // podemos ser comestibles y atacar,
																// asique no oner esa condicion aqui
		// o habria que comprobarlo en las 3 transiciones
		else
			return false;
	}

	@Override
	public String toString() {
		return ghost + " va a atacar" + origen;
	}
}
