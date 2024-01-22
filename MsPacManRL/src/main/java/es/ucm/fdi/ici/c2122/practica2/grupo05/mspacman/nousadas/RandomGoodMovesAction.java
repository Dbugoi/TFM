package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.nousadas;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class RandomGoodMovesAction implements Action {

	public RandomGoodMovesAction() {
		// TODO Auto-generated constructor stub
	}

    private Random rnd = new Random();
	
	@Override
	public MOVE execute(Game game) {
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, 55);
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,game.getPacmanCurrentNodeIndex());
	
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		if(goodMoves.size()>1 && listChasingGhosts.size()==0)
			goodMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPowerPill, DM.PATH));
		
			return goodMoves.get(rnd.nextInt(goodMoves.size()));
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}
