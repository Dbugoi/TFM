package es.ucm.fdi.ici.c2122.practica3.grupo07.pacman.actions;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGhostAction implements RulesAction {
	GHOST rGHOST;
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "chase ghost";
	}

	@Override
	public MOVE execute(Game game) {
		if(Util.getSafeMoves(game).size()==0)return Util.bestPathToDie(game);

		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();
		MOVE move=MOVE.NEUTRAL;
		
		//System.out.println(1);
		double minDist=Integer.MAX_VALUE;
		for(MOVE m:Util.getSafeMoves(game)) {
			int nei=game.getNeighbour(pacman, m);
			double dist=Integer.MAX_VALUE;
			for(GHOST g:GHOST.values()) {
				if(game.isGhostEdible(g))dist=Math.min(dist, game.getDistance(nei, game.getGhostCurrentNodeIndex(g), m, DM.PATH));
				
			}
			if(dist<minDist) {
				minDist=dist;
				move=m;
			}
		}
		
		return move;
	}

	@Override
	public void parseFact(Fact actionFact) {
		/*try {
			Value value = actionFact.getSlotValue("rghost");
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			rGHOST = GHOST.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}*/
	}

}
