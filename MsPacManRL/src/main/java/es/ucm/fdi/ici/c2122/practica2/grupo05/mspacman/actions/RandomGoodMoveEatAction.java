package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class RandomGoodMoveEatAction implements Action {

	private int limitForChasingGhost = 65;
	public RandomGoodMoveEatAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
			
		if(goodMoves.size()>1 && listChasingGhosts.size()==0)
			goodMoves.remove(game.getNextMoveTowardsTarget(mspacman, indexPowerPill, DM.PATH));
		
		Random rnd = new Random();
	
		return goodMoves.get(rnd.nextInt(goodMoves.size()));

		}

	@Override
	public String getActionId() {
		return "Random GoodMove Eat Action";
	}
	
	
}