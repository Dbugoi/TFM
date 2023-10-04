package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;


public class BestMoveToEatGoForEdibleGhostTransition implements Transition  {

	private String id;
	
	public BestMoveToEatGoForEdibleGhostTransition(String idFrom) {
		super();
		id = idFrom;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput)in;	
		return input.existsNearEdibleGhosts() && !input.noGoodMoves() ; //||(input.existsNearEdibleGhosts2() && !input.existsNearChasingGhosts());
		
	}



	@Override
	public String toString() {
		return id + ": Ghost is edible";
	}

	
	
}

