package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class MsPacManDiesTransition implements Transition{
	
	private String id;

	public MsPacManDiesTransition(String fromID) {
		super();
		id = fromID;
	}
	
	@Override
	public boolean evaluate(Input in) {
		return in.getGame().wasPacManEaten();
	}
	
	@Override
	public String toString() {
		return id + ": MsPacman dies";
	}
}
