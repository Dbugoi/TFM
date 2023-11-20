package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToClosestPPillAction implements RulesAction {

	public GoToClosestPPillAction() {
	}

	@Override
	public MOVE execute(Game game) {
        int pacman = game.getPacmanCurrentNodeIndex();
        int nearestPPill = CommonMethodsPacman.getClosestPPill(game, pacman);
        if(nearestPPill == -1)//programacion defensiva. no deberia pasar
        	return CommonMethodsPacman.avoidGhosts( game,  pacman,   CommonMethodsPacman.findClosestPill(game, pacman));
		return CommonMethodsPacman.avoidGhosts( game,  pacman,  nearestPPill);
        
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "PacmanEatClosestPPill";
	}
	
	
}
