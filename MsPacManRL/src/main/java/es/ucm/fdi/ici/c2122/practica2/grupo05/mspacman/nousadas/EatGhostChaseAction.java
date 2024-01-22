package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.nousadas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.NearestEdibleGhostInformation;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class EatGhostChaseAction implements Action {

	private int limitForChasingGhost = 55;
	private int limitForEdibleGhost = 50;
	public EatGhostChaseAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		NearestEdibleGhostInformation edibleGhostsINFO = GameUtils.getNearestEdibleGhosts(game, this.limitForEdibleGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		List<MOVE> movesToEatGhosts = new ArrayList<>();
		if(edibleGhostsINFO.getListNearestEdibleGhost().size()>0)
		{
	//		MOVE finalMove = GameUtils.getBestMovesTowardsGhost(game, edibleGhostsINFO.getListNearestEdibleGhost(), goodMoves);
		//	movesToEatGhosts.add(finalMove);
			for(GHOST ghost: edibleGhostsINFO.getListNearestEdibleGhost()) {
				MOVE m =game.getNextMoveTowardsTarget(mspacman, game.getGhostCurrentNodeIndex(ghost), DM.PATH);
				if(goodMoves.contains(m));
					movesToEatGhosts.add(m);
			}
		}
		
		Random rnd = new Random();
		
		return movesToEatGhosts.get(rnd.nextInt(movesToEatGhosts.size()));
	}

	@Override
	public String getActionId() {
		return "Chase Edible Ghost Action";
	}

}