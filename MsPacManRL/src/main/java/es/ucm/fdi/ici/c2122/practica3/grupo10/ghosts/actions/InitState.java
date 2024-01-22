package es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class InitState implements RulesAction {

	@Override
	public String getActionId() {
		return "Init state";
	}

	@Override
	public MOVE execute(Game game) {
		return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
