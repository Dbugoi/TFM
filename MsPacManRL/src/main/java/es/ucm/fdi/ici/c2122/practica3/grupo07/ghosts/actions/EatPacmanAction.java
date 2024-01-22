package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;


public class EatPacmanAction implements RulesAction {
	MOVE target;
	GHOST ghost;
	public EatPacmanAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"eatPacman";
	}

	@Override
	public MOVE execute(Game game) {
		return target;
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("node_info");
			//System.out.println("v: "+value);
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			target = MOVE.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}
}