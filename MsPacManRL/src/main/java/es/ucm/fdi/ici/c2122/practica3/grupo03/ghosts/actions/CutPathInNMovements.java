package es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo03.utils.Busquedas;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class CutPathInNMovements implements RulesAction {
	
	int distance;
	GHOST ghost;
	
	public CutPathInNMovements(int movs, GHOST g) {
		distance = movs;
		ghost = g;
	}
	
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost + "goestoPacManin" + distance;
	}

	@Override
	public MOVE execute(Game game) {
		// TODO Auto-generated method stub
		if(game.doesGhostRequireAction(ghost)) {
			return Busquedas.moveToCutPacManPathInNMovements(game, ghost, distance);
		}
		else {
			return MOVE.NEUTRAL;
		}
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub

	}

}
