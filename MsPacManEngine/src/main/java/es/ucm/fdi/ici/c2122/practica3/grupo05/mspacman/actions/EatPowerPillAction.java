package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

import java.util.List;
import java.util.Optional;
import java.util.Random;


import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatPowerPillAction  implements RulesAction {

	private int limitForChasingGhost = 65;
	public EatPowerPillAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);
		
		int indexPowerPill = PacmanMoves.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		
		
		MOVE m= game.getNextMoveTowardsTarget(mspacman, indexPowerPill, DM.PATH);
		return m;
	
	}

	@Override
	public String getActionId() {
		return "EatPowerPill";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}