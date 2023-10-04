package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class NoPillGoodMovesAction  implements RulesAction {

	private int limitForChasingGhost = 65;
	public NoPillGoodMovesAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);
		
			Map<MOVE, Double> map = new HashMap<>();
			PacmanMoves.getBestRunAwayGhost(game, limitForChasingGhost, map);
			TreeMap<MOVE, Double> sorted = new TreeMap<>();
			sorted.putAll(map);
	
			for(MOVE m: sorted.keySet())
				if(goodMoves.contains(m))
					return m;
	
		return MOVE.NEUTRAL;
		}

	@Override
	public String getActionId() {
		return "No Pill GoodMoves Action";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
	
	
}