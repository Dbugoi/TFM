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

public class EatPillPatrolAction implements Action {
	private int limitForChasingGhost = 65;
	public EatPillPatrolAction() {
		// TODO Auto-generated constructor stub
	}

	private Random rnd = new Random();
	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
	List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,game.getPacmanCurrentNodeIndex());
		
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		if(goodMoves.size()>1 && listChasingGhosts.size()==0 &&indexPowerPill!=-1 )
			goodMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPowerPill, DM.PATH));
		
		int indexPill = GameUtils.getNearestPillWithoutChasingGhost(game, goodMoves, 10000000);
		
		
		MOVE m = game.getNextMoveTowardsTarget(mspacman, indexPill, DM.PATH);
	

		return   m;
	}

	@Override
	public String getActionId() {
		return "Patrol Action";
	}

}
