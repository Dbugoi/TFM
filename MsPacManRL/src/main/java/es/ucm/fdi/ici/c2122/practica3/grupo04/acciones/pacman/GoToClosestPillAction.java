package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToClosestPillAction implements RulesAction {
	
	public GoToClosestPillAction() {
	}

	@Override
	public MOVE execute(Game game) {
        int pacman = game.getPacmanCurrentNodeIndex();
        int nearestPill = CommonMethodsPacman.findClosestPill(game, pacman);
		return CommonMethodsPacman.avoidGhosts(game,  pacman,  nearestPill);        
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "PacmanGoesToClosesPill";
	}
}
