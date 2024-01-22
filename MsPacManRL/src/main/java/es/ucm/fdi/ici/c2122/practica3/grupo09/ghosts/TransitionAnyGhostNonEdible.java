package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
import es.ucm.fdi.ici.c2122.practica2.grupo09.ghosts.GhostsInput;

public class TransitionAnyGhostNonEdible implements Transition {
	GHOST ghost;

	public TransitionAnyGhostNonEdible(GHOST g) {
		super();
		ghost = g;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;

		boolean iAmEdible = false;

		switch (ghost) {
		case BLINKY:
			iAmEdible = input.isBLINKYedible();
			break;
		case INKY:
			iAmEdible = input.isINKYedible();
			break;
		case PINKY:
			iAmEdible = input.isPINKYedible();
			break;
		case SUE:
			iAmEdible = input.isSUEedible();
			break;
		default:
			break;

		}

		return input.isAnyGhostNonEdible() && iAmEdible;
	}

	@Override
	public String toString() {
		return "Any ghost edible";
	}
}
