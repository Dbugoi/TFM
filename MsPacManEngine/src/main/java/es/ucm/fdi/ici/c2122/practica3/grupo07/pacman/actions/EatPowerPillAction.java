package es.ucm.fdi.ici.c2122.practica3.grupo07.pacman.actions;

import java.awt.Color;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class EatPowerPillAction implements RulesAction{

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "pp";
	}

	@Override
	public MOVE execute(Game game) {
		if(Util.getSafeMoves(game).size()==0)return Util.bestPathToDie(game);
		MOVE move=MOVE.NEUTRAL;
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();
	
		double minDist=Integer.MAX_VALUE;
		for(MOVE m:Util.getSafeMoves(game)) {
			int nei=game.getNeighbour(pacman, m);
			double dist=game.getDistance(nei, Util.getMinDistanceNode(game,nei, game.getActivePowerPillsIndices(),m), m, DM.PATH) ;
			//GameView.addPoints(game, Color.CYAN, Util.getMinDistanceNode(game,nei, game.getActivePowerPillsIndices(),m));
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
