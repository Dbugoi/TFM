package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ChaseAction  implements Action {


	private GHOST g;
	private MsPacManFuzzyMemory fuzzyMemory;
	
	public ChaseAction(MsPacManFuzzyMemory f, GHOST g) {
		// TODO Auto-generated constructor stub
		this.fuzzyMemory=f;
		this.g=g;
	}

	public MOVE execute(Game game) {
	
		/*
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		
		HashMap<String, Double> fvars = new HashMap<String,Double>() ;
		fvars.putAll(fuzzyMemory.getFuzzyValues());

		TreeMap<Double, MOVE> sorted = new TreeMap<>(Collections.reverseOrder());
		for(GHOST g: GHOST.values()) {
			int pos = (int) Math.round(fvars.get(g.name()+"ultimaPos"));
			if(pos!=-1) {
				double d = game.getShortestPathDistance(mspacman, pos, game.getPacmanLastMoveMade());
				if(d*1.5<=fvars.get(g.name()+"edible")) {
					sorted.put((double) d, 
							game.getNextMoveTowardsTarget(mspacman,pos, game.getPacmanLastMoveMade(), DM.PATH));
				}
			}
		}
		
		return sorted.firstEntry().getValue();
		*/
		int mspacman = game.getPacmanCurrentNodeIndex();

		HashMap<String, Double> fvars = new HashMap<String,Double>() ;
		fvars.putAll(fuzzyMemory.getFuzzyValues());

		
		//int pos = (int) Math.round(fvars.get(g.name()+"ultimaPos"));
		int pos = fuzzyMemory.getLastVisiblePosition(g);
		//return game.getNextMoveTowardsTarget(mspacman, pos, game.getPacmanLastMoveMade(), DM.PATH);
		return Moves.pacmanTowards(game, pos);
	}

	public String getActionId() {
		return "Chase"+ this.g.name();
	}

	

}