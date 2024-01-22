package es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsHeuristicFunction {
	public static double getValue(Game game, GHOST ghost) {
		double v=0.0;
		double dist=game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
		if(game.isGhostEdible(ghost)) {
			v+=dist/100;
		}
		else {
			v-=dist/100;
		}
		return v;
	}
}
