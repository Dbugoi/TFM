package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class NoPillNoGoodMovesEatAction implements Action{
	private int limitForChasingGhost = 65;
	public NoPillNoGoodMovesEatAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
	
		
		Map<MOVE, Double> map = new HashMap<>();
		GameUtils.getBestRunAwayGhost(game, limitForChasingGhost, map);
		TreeMap<MOVE, Double> sorted = new TreeMap<>(Collections.reverseOrder());
		sorted.putAll(map);
		
		return sorted.firstKey();
		
	}

	public String getActionId() {
		return " No Pill No GoodMoves Eat Action";
	}
}
