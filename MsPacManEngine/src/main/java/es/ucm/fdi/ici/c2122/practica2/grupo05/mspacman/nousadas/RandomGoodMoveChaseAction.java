package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.nousadas;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.NearestEdibleGhostInformation;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class RandomGoodMoveChaseAction implements Action {

	private int limitForChasingGhost = 55;
	private int limitForEdibleGhost = 50;
	public RandomGoodMoveChaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		NearestEdibleGhostInformation edibleGhostsINFO = GameUtils.getNearestEdibleGhosts(game, this.limitForEdibleGhost);
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		if(edibleGhostsINFO.getListNearestEdibleGhost().size()>0)
		{
			for(GHOST g: edibleGhostsINFO.getListNearestEdibleGhost()) {
				MOVE m =game.getNextMoveTowardsTarget(mspacman, game.getGhostCurrentNodeIndex(g), DM.PATH);
				if(goodMoves.contains(m));
					
			}
		}
		
		Random rnd = new Random();
		
		return goodMoves.get(rnd.nextInt(goodMoves.size()));
	}

	@Override
	public String getActionId() {
		return "Random GoodMove Chase Action";
	}

}