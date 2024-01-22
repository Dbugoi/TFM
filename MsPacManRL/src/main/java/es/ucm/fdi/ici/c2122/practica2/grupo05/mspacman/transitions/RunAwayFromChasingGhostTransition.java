package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class RunAwayFromChasingGhostTransition implements Transition  {

	private String id;
	public RunAwayFromChasingGhostTransition(String idFrom) { //Run away if not edible
		super();
		id= idFrom;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput)in;	
		return input.existsNearChasingGhosts() && !input.existsNearEdibleGhosts();
		
	}

	@Override
	public String toString() {
		return id + " Ghost is not edible";
	}

	
	//&& (game.getGhostEdibleTime(g)/2 + d<  game.getGhostEdibleTime(g)) &&
}
