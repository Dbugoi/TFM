package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

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
import es.ucm.fdi.ici.c2122.practica3.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class NoGoodMovesPatrolAction  implements RulesAction {

	private int limitForChasingGhost = 65;
	public NoGoodMovesPatrolAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {

		
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman)) {
		
				
			Map<MOVE, Double> map2 = new HashMap<>();
			PacmanMoves.getBestRunAwayGhost(game, limitForChasingGhost, map2);
			TreeMap<MOVE, Double> sorted2 = new TreeMap<>();////???
			sorted2.putAll(map2);
	
		
			return sorted2.firstKey();
		}
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "No good moves, patrol action";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}