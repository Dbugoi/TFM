package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class RandomGoodMoveTransition implements Transition{

	private String id;
	public RandomGoodMoveTransition(String string) {
		// TODO Auto-generated constructor stub
		super();
		id=string;
	}
	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput)in;	
		return input.existsNearChasingGhosts() && !input.existsNearEdibleGhosts() && input.randomGoodMove() && !input.pillGoodMove();
	}
	public String toString() {
		return id + " Ghost is not edible";
	}
}
