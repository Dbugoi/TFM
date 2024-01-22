package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class RunAwayFromEdibleChangingGhostTransition implements Transition  {

	
	public RunAwayFromEdibleChangingGhostTransition() { //Run away if not edible
		super();
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput)in;	
		return input.dangerEdibleGhost();
		
	}

	@Override
	public String toString() {
		return "Ghost is not edible";
	}

}
