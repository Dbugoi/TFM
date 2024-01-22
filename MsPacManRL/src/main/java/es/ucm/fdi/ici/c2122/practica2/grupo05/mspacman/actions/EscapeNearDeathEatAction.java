package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EscapeNearDeathEatAction implements Action {

	private int limitForChasingGhost = 65;
	public EscapeNearDeathEatAction() {
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
		
		List<MOVE> movesAwayFromEachTarget = GameUtils.getMovesAwayFromEachTarget(game, mspacman, game.getPacmanLastMoveMade(), listChasingGhosts);

		EnumMultiset movesAwayFromTargetsMultiSet = EnumMultiset.create();
		movesAwayFromTargetsMultiSet.addAll(movesAwayFromEachTarget);

       Optional<MOVE> mostRepeatedMove = GameUtils.getMostRepeatedMove(movesAwayFromTargetsMultiSet);
   	
     
       
       return mostRepeatedMove.orElse(game.getNextMoveAwayFromTarget(mspacman, game.getGhostCurrentNodeIndex(GameUtils.getClosestGhost(game,DM.PATH)), DM.PATH));
	
	}

	@Override
	public String getActionId() {
		return "Escape Near Death Eat Action";
	}
	
	

}
