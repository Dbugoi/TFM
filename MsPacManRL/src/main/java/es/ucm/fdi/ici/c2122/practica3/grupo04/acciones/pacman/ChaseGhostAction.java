package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGhostAction implements RulesAction {

	public ChaseGhostAction() {
	}

	@Override
	public MOVE execute(Game game) {
		int pacman = game.getPacmanCurrentNodeIndex();
		GHOST g = CommonMethodsPacman.getNearestGhost(game, pacman, Integer.MAX_VALUE, true);
		int ghost = game.getGhostCurrentNodeIndex(g);

		return CommonMethodsPacman.avoidGhosts(game, pacman, ghost);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return "ChaseGhostAction";
	}

}