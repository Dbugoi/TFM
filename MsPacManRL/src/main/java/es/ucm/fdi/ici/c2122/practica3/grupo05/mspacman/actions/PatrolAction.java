package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.CheckIfGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.Pills;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class PatrolAction  implements RulesAction {
	private int limitForChasingGhost = 65;
	private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	public PatrolAction() {
		// TODO Auto-generated constructor stub
	}

 
	
	@Override
	public MOVE execute(Game game) {
		/*
		int mspacman = game.getPacmanCurrentNodeIndex();
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,game.getPacmanCurrentNodeIndex());
	
		int indexPowerPill = PacmanMoves.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		if(goodMoves.size()>1 &&CheckIfGhost.existsGhostInLair(game) &&indexPowerPill!=-1 )
			goodMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPowerPill, DM.PATH));
		
		
		Map<MOVE, Double> mapBestTowards = new HashMap<>();
		PacmanMoves.getBestMovesTowardsGhost(game, mapBestTowards);
		TreeMap<MOVE, Double> sorted2 = new TreeMap<>();
		sorted2.putAll(mapBestTowards);
			
		//for(MOVE m2: sorted2.keySet())
		//	if(goodMoves.contains(m2))
		//		return m2;
			
		int indexPill = Pills.getClosestPillUsingAvailableMoves(game, goodMoves);
		MOVE m = game.getNextMoveTowardsTarget(mspacman, indexPill, DM.PATH);
		
		if(indexPill!=-1&& goodMoves.contains(m))
			return m;
		
		 
		Map<MOVE, Double> map = new HashMap<>();
		PacmanMoves.getBestRunAwayGhost(game, limitForChasingGhost, map);
		TreeMap<MOVE, Double> sorted = new TreeMap<>(Collections.reverseOrder());
		sorted.putAll(map);
		
		for(MOVE m3: sorted.keySet())
			if(goodMoves.contains(m3))
				return m3;

	
		if(goodMoves.isEmpty())
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPowerPill, DM.PATH);
		
		return  goodMoves.get(rnd.nextInt(goodMoves.size()));
		*/
		return allMoves[rnd.nextInt(allMoves.length)];
	}

	@Override
	public String getActionId() {
		return "Patrol";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
