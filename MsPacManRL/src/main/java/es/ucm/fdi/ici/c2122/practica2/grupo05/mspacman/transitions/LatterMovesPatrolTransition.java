package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class LatterMovesPatrolTransition implements Transition{
	
	private String id;
	public LatterMovesPatrolTransition(String fromId) {
		super();
		id= fromId;
	}
	
	@Override
	public boolean evaluate(Input in) {
		// TODO Auto-generated method stub
		MsPacManInput input = (MsPacManInput)in;
		return !input.existsNearChasingGhosts() && !input.existsNearEdibleGhosts() && !input.noGoodMoves() && input.doLatterMoves() ;// &&!input.existsNearEdibleGhosts2();
	}
	
	@Override
	public String toString() {
		return id + ": MsPacman patroling";
	}
}
