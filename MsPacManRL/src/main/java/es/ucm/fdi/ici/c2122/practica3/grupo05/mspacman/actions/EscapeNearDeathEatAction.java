package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import es.ucm.fdi.ici.c2122.practica3.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EscapeNearDeathEatAction implements RulesAction {

	private int limitForChasingGhost = 200;
	public EscapeNearDeathEatAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);					
		int indexPowerPill = PacmanMoves.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
			
		if(goodMoves.size()>1 && listChasingGhosts.size()==0)
			goodMoves.remove(game.getNextMoveTowardsTarget(mspacman, indexPowerPill, DM.PATH));
		
		Map<MOVE, Double> map2 = new HashMap<>();
		PacmanMoves.getBestRunAwayGhost(game, limitForChasingGhost, map2);
		TreeMap<Double, MOVE> sorted2 = new TreeMap<>(Collections.reverseOrder());
		for(MOVE m: map2.keySet()) {
			sorted2.put(map2.get(m), m);
		}
	
		for(Double i2: sorted2.keySet())
			if(goodMoves.contains(sorted2.get(i2)))
				return sorted2.get(i2);
		
     
       return game.getNextMoveAwayFromTarget(mspacman, game.getGhostCurrentNodeIndex(GhostFinder.getClosest(game)), DM.PATH);
       //return mostRepeatedMove.orElse(game.getNextMoveAwayFromTarget(mspacman, game.getGhostCurrentNodeIndex(GameUtils.getClosestGhost(game,DM.PATH)), DM.PATH));
	
	}

	@Override
	public String getActionId() {
		return "Escape Near Death Eat Action";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
	
	

}
