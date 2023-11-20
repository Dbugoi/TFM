package es.ucm.fdi.ici.c2122.practica3.grupo07.pacman.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPillAction implements RulesAction{

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "eat pill";
	}

	@Override
	public MOVE execute(Game game) {
		if(Util.getSafeMoves(game).size()==0)return Util.bestPathToDie(game);

		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();
		MOVE move=MOVE.NEUTRAL;
		
		double minDist=Integer.MAX_VALUE;
		for(MOVE m:Util.getSafeMoves(game)) {
			int nei=game.getNeighbour(pacman, m);
			double dist=game.getDistance(nei, Util.getMinDistanceNode(game,nei, game.getActivePillsIndices(),m), m, DM.PATH) ;
			
			if(dist<minDist) {
				minDist=dist;
				move=m;
			}
		}
		return move;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
