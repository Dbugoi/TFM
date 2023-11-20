package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Maze;

public class EatPillQuadAction implements RulesAction {

	float freedomDegree;
	public EatPillQuadAction(float freedomDegree) {
		this.freedomDegree = freedomDegree;
	}

	@Override
	public MOVE execute(Game game) {
        int pacman = game.getPacmanCurrentNodeIndex();
        Maze a = game.getCurrentMaze();
        
		return CommonMethodsPacman.avoidGhosts( game,  pacman, 0);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "PacmanEatInQuad";
	}
}
