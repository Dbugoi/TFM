package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class RunAwayAction  implements Action {
	private int limitForChasingGhost = 65;
	
	private double[] ultimaPos;
	private double[] confidence;
	private GHOST g;

	private MsPacManFuzzyMemory fuzzyMemory;
	public RunAwayAction(MsPacManFuzzyMemory f, GHOST g) {
		// TODO Auto-generated constructor stub
		this.fuzzyMemory= f;
		this.g = g;
	}

	
	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();

		//HashMap<String, Double> fvars = new HashMap<String,Double>() ;
		//fvars.putAll(fuzzyMemory.getFuzzyValues());
		//fvars.putAll(fuzzyMemory.);
		int pos = fuzzyMemory.getLastVisiblePosition(g);
		//int pos = (int) Math.round(fvars.get(g.name()+"ultimaPos"));
		/*
		int pos = fuzzyMemory.getLastVisiblePosition(g);
		int d= game.getShortestPathDistance(mspacman, pos, game.getPacmanLastMoveMade());
		if(pos!=1292)
			return game.getNextMoveAwayFromTarget(mspacman, pos, game.getPacmanLastMoveMade(), DM.PATH);
		else
			return game.getNextMoveAwayFromTarget(mspacman, pos, game.getPacmanLastMoveMade(), DM.MANHATTAN);
			*/
		return Moves.pacmanAwayFrom(game, pos);
	}

	@Override
	public String getActionId() {
		return "RunAway"+ this.g.name();
	}

	

}
