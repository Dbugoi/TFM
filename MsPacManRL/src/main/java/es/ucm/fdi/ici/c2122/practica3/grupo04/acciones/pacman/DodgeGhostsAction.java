package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// Escoge el path disponible hasta la siguiente interseccion con mayor numero de pills

public class DodgeGhostsAction implements RulesAction {
	
	public DodgeGhostsAction() {
	}

	@Override
	public MOVE execute(Game game) {
        int pacman = game.getPacmanCurrentNodeIndex();
        int adj = CommonMethodsPacman.getAdjacentJunctions(game, pacman)[0];
		return CommonMethodsPacman.avoidGhosts(game,  pacman,  adj);        
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "DodgeGhosts";
	}
}
