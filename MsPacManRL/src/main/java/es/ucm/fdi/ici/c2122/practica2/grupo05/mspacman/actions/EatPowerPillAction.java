package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.List;
import java.util.Optional;
import java.util.Random;


import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatPowerPillAction implements Action {

	private int limitForChasingGhost = 65;
	public EatPowerPillAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		
		
		MOVE m= game.getNextMoveTowardsTarget(mspacman, indexPowerPill, DM.PATH);
		return m;
	
	}

	@Override
	public String getActionId() {
		return "Eat Pills Action";
	}
}