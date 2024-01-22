package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatPillGoodMoveAction implements Action {

	private int limitForChasingGhost = 65;
	 private int limitPill = 100000;
	public EatPillGoodMoveAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		int indexPill = GameUtils.getNearestPillWithoutChasingGhost(game, goodMoves, this.limitPill);
	
	
		
			return game.getNextMoveTowardsTarget(mspacman, indexPill, DM.PATH);
		
		}

	@Override
	public String getActionId() {
		return "Eat Pill GoodMove Action";
	}
	
	
}