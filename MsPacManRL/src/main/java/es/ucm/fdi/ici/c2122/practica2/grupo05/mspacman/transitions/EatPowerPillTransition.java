package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class EatPowerPillTransition implements Transition  {

	private String id;
	public EatPowerPillTransition(String Idfrom) {
		super();
		id=Idfrom;
	}

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput input = (MsPacManInput)in;	
		return (!input.existsNearEdibleGhosts() || input.dangerEdibleGhost())&&(input.existsNearPowerPill()&& input.eatPowerPill() &&(input.existsNearChasingGhosts()))&&! input.movesToEatGhosts();  // || input.dangerEdibleGhost()
		
	}



	@Override
	public String toString() {
		return id + ": Ghost is edible";
	}

	
	
}

