package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

// Escoge el path disponible hasta la siguiente interseccion con mayor numero de pills

public class GoPathWithMorePillsAction implements RulesAction {
	
	public GoPathWithMorePillsAction() {
	}

	@Override
	public MOVE execute(Game game) {
        int pacman = game.getPacmanCurrentNodeIndex();
        int[] adjs = CommonMethodsPacman.getAdjacentJunctions(game, pacman);
        int goalPos = adjs[0];
        int numPills = CommonMethodsPacman.getPathNumPills(game, game.getShortestPath(pacman, adjs[0]));
        for(int i = 1 ; i < adjs.length; ++i) {
        	int tmp = CommonMethodsPacman.getPathNumPills(game, game.getShortestPath(pacman, adjs[i]));
        	if(tmp > numPills ) {
        		numPills = tmp;
        		goalPos = adjs[i];
        	}        	
        }
        //en goalPos queda la interseccion por la que cogeremos mas pills       
        
		return CommonMethodsPacman.avoidGhosts(game,  pacman,  goalPos);        
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "GoToPathWithMorePills";
	}
}
