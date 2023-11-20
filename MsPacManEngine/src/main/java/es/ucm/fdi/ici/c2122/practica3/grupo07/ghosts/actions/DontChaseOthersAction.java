package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DontChaseOthersAction implements RulesAction {

	GHOST ghost;
	MOVE nMOVE;
	public DontChaseOthersAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"dont chase others";
	}

	@Override
	public MOVE execute(Game game) {
		Random rnd = new Random();
		//cambiar
		MOVE[] aux=MOVE.values().clone();
		ArrayList<MOVE>moves=new ArrayList<MOVE>();
		for(MOVE m:aux) {
			if(m!=MOVE.NEUTRAL&&m!=nMOVE) {
				moves.add(m);
			}
		}
		return moves.get(rnd.nextInt(moves.size()));
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("nmove");
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			nMOVE = MOVE.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

}
