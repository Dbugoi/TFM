package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class ChaseGhostAction implements RulesAction {

	private int limitForChasingGhost = 65;
	
	public ChaseGhostAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman)) {
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		
		
	
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);

	 	Map<MOVE, Double> mapBestTowards = new HashMap<>();
	 	PacmanMoves.getBestMovesTowardsGhost(game, mapBestTowards);
		TreeMap<MOVE, Double> sorted2 = new TreeMap<>();
		sorted2.putAll(mapBestTowards);
		
		
		for(MOVE m: sorted2.keySet())
			if(goodMoves.contains(m))
				return m;
		
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