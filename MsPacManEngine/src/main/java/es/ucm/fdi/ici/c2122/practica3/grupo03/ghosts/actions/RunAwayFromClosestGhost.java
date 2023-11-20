package es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import es.ucm.fdi.ici.c2122.practica3.grupo03.utils.Pair;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayFromClosestGhost implements RulesAction {

    GHOST ghost;
	public RunAwayFromClosestGhost(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		
		if(game.doesGhostRequireAction(ghost)) {
			List<Pair<GHOST,Integer>> gn = new ArrayList<Pair<GHOST,Integer>>();
			for(GHOST gt : GHOST.values()) {
				if((gt != ghost) && game.getGhostLairTime(gt) <= 0) {
					gn.add(new Pair<GHOST,Integer>(gt, game.getGhostCurrentNodeIndex(gt)));				
				}
			}
			
			Set<Pair<GHOST,Double>> set = new TreeSet<Pair<GHOST,Double>>(
				new Comparator<Pair<GHOST,Double>>(){
	
					@Override
					public int compare(Pair<GHOST, Double> o1, Pair<GHOST, Double> o2) {
						// TODO Auto-generated method stub
						if(o1.getSecond() < o2.getSecond()) { return -1; }
						else if (o1.getSecond() == o2.getSecond()) { return 0; }
						else { return 1; }
					}
					
					
				});
			
			int ghostnode = game.getGhostCurrentNodeIndex(ghost);
			
			for(Pair<GHOST,Integer> p : gn) {
				set.add(new Pair<GHOST,Double>(p.getFirst(), game.getDistance(
						ghostnode, game.getGhostCurrentNodeIndex(p.getFirst()), 
						game.getGhostLastMoveMade(p.getFirst()), DM.PATH)));
			}
			
			if(set.isEmpty()) {
				return MOVE.NEUTRAL;
			}
			else {
				return game.getApproximateNextMoveAwayFromTarget(
						ghostnode, game.getGhostCurrentNodeIndex(set.iterator().next().getFirst()), 
						game.getGhostLastMoveMade(set.iterator().next().getFirst()), DM.PATH);
			}
			
		}
		else {
			return MOVE.NEUTRAL;
		}
	}

	@Override
	public String getActionId() {
		return ghost+ "runsAwayFromGhost";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
