package es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman;

import java.util.ArrayList;

import es.ucm.fdi.ici.c2122.practica5.grupo07.Util;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacmanHeuristicFunction {
	public static double getValue(Game game, double oldscore) {
		double v=0.0;
		ArrayList<MOVE>safeMoves=Util.getSafeMoves(game);
		//v+=safeMoves.size();
	
		if(safeMoves.size()==0)v-=50.0;
		if(safeMoves.size()>1)v+=1.0;
		//System.out.println(v);
		//System.out.println((double)game.getScore()+"  "+oldscore);
		if(oldscore!=0.0) {
			v+=(((double)game.getScore()-oldscore)/500.0);
			//System.out.println(v);
		}
		return v;
	}
}
