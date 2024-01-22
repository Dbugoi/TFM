package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class InLair implements RulesAction{

    @Override
    public String getActionId() {
        return "In lair";
    }

    @Override
    public MOVE execute(Game game) {
        return MOVE.NEUTRAL;
    }

	@Override
	public void parseFact(Fact actionFact) {
		// do nothing
	}
    
}
