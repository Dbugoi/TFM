package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class TransitionPacmanFarFromPillAndNotEdible implements Transition {
	private GHOST ghost;

	public TransitionPacmanFarFromPillAndNotEdible(GHOST ghost) {
		super();
		this.ghost = ghost;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput input = (GhostsInput) in;

//		boolean edible;
//		
//		switch(ghost) {
//			case BLINKY:
//				edible = input.isBLINKYedible();
//			case INKY:
//				edible = input.isINKYedible();
//			case PINKY:
//				edible = input.isPINKYedible();
//			case SUE:
//				edible = input.isSUEedible();
//			default:
//				edible = false;
//		}
//		
//		return !edible &&  
		return !input.pacmanCloseToPPill(ghost);

	}

	@Override
	public String toString() {
		return "Pacman far and ghost not edible";
	}
}
