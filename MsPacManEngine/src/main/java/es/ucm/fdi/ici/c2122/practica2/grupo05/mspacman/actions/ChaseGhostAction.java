package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.NearestEdibleGhostInformation;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ChaseGhostAction implements Action {

	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 10000000;
	public ChaseGhostAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman)) {
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		
	
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);

	 	Map<MOVE, Double> mapBestTowards = new HashMap<>();
		GameUtils.getBestMovesTowardsGhost(game, mapBestTowards);
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

}