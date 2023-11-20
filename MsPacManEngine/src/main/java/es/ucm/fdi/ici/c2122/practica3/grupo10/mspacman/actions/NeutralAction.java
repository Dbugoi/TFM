package es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NeutralAction implements RulesAction {

	public NeutralAction() {
	}

	@Override
	public MOVE execute(Game game) {
        return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		
	}

	@Override
	public String getActionId() {
		return "chases";
	}

	

}
