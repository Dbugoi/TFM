package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.NearestEdibleGhostInformation;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class RunAwayCageAction implements Action {

	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 55;
	private int cage = 492;
	public RunAwayCageAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		NearestEdibleGhostInformation edibleGhostsINFO = GameUtils.getNearestEdibleGhosts(game, limitForEdibleGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		if(goodMoves.contains(game.getNextMoveTowardsTarget(mspacman, cage, DM.PATH)))
			goodMoves.remove(game.getNextMoveTowardsTarget(mspacman, cage, DM.PATH));
		
		Random rnd = new Random();
		if(goodMoves.size()==0)
			return MOVE.NEUTRAL; //mejorar con minimax
		else
			return goodMoves.get(rnd.nextInt(goodMoves.size()));
	}

	@Override
	public String getActionId() {
		return "Chase Edible Ghost Action";
	}

}