package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class CageTransition implements Transition{
	
	public CageTransition() {
		super();
	}
	@Override
	public boolean evaluate(Input in) {
		
		MsPacManInput input = (MsPacManInput)in;
		return input.nearDangerousCage();
	}
	
	@Override
	public String toString() {
		return "MsPacman near dangerous cage";
	}

}
