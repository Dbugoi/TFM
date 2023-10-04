package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica3.grupo05.NearestEdibleGhostInformation;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class BestMoveToEatChaseGhostAction  implements RulesAction {

	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 10000000;
	public BestMoveToEatChaseGhostAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman)) {
			
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, 100);
		NearestEdibleGhostInformation edibleGhostsINFO = GhostFinder.getNearestEdibleGhosts(game, 10000000);
		List<GHOST> listEdibleGhosts= new ArrayList<>(edibleGhostsINFO.getListNearestEdibleGhost());
		
	
		TreeMap<Double, MOVE> sorted = new TreeMap<>();
		List<MOVE> movesToEat = new ArrayList<>();
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);
		
		
		if(listEdibleGhosts.size()>0)
		{
			
			for(GHOST ghost: listEdibleGhosts) {
				MOVE m =game.getApproximateNextMoveTowardsTarget(mspacman, game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(),DM.PATH);
				if(goodMoves.contains(m));
					sorted.put((double) game.getShortestPathDistance(mspacman, game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade()),m);
			}
		

		
		return sorted.firstEntry().getValue();
		}
		
		}
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Chase Edible Ghost Action";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}