package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class EatPillTransition implements Transition  {

	
	public EatPillTransition() {
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput)in;	
		return (input.existsNearPill() && !input.existsNearEdibleGhosts2());
		
	}



	@Override
	public String toString() {
		return "Ghost is edible";
	}

	
	
}

