package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class PatrolAction implements Action {
	private int limitForChasingGhost = 65;
	public PatrolAction() {
		// TODO Auto-generated constructor stub
	}

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
	List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,game.getPacmanCurrentNodeIndex());
	
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		if(goodMoves.size()>1 && listChasingGhosts.size()==0 &&indexPowerPill!=-1 )
			goodMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPowerPill, DM.PATH));
		
		
		Map<MOVE, Double> mapBestTowards = new HashMap<>();
		GameUtils.getBestMovesTowardsGhost(game, mapBestTowards);
		TreeMap<MOVE, Double> sorted2 = new TreeMap<>();
		sorted2.putAll(mapBestTowards);
			
			
		for(MOVE m2: sorted2.keySet())
			if(goodMoves.contains(m2))
				return m2;
			
		Map<MOVE, Double> map = new HashMap<>();
		GameUtils.getBestRunAwayGhost(game, limitForChasingGhost, map);
		TreeMap<MOVE, Double> sorted = new TreeMap<>(Collections.reverseOrder());
		sorted.putAll(map);
		
		for(MOVE m3: sorted.keySet())
			if(goodMoves.contains(m3))
				return m3;

	
		return  goodMoves.get(rnd.nextInt(goodMoves.size()));
	}

	@Override
	public String getActionId() {
		return "Patrol Action";
	}

}
