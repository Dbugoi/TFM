package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.NearestEdibleGhostInformation;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class BestMoveToEatChaseGhostAction implements Action {

	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 10000000;
	public BestMoveToEatChaseGhostAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman)) {
			
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		NearestEdibleGhostInformation edibleGhostsINFO = GameUtils.getNearestEdibleGhosts(game, this.limitForEdibleGhost);
		List<GHOST> listEdibleGhosts= new ArrayList<>(edibleGhostsINFO.getListNearestEdibleGhost());
		
	
		List<MOVE> movesToEat = new ArrayList<>();
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		
		if(listEdibleGhosts.size()>0)
		{
			
			for(GHOST ghost: listEdibleGhosts) {
				MOVE m =game.getApproximateNextMoveTowardsTarget(mspacman, game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(),DM.PATH);
				if(goodMoves.contains(m));
					movesToEat.add(m);
			}
		}
		

		EnumMultiset movesTowardsFromTargetsMultiSet = EnumMultiset.create();
        movesTowardsFromTargetsMultiSet.addAll(movesToEat); //movesToEat.keySet()
        Optional<MOVE> mostRepeatedMove2 = GameUtils.getMostRepeatedMove(movesTowardsFromTargetsMultiSet);
				
		
		return mostRepeatedMove2.get();
	
		}
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Chase Edible Ghost Action";
	}

}