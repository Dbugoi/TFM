package es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions;

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
		// TODO Auto-generated method stub
		
	}
    
}
