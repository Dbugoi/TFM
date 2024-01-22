package es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import es.ucm.fdi.ici.c2122.practica3.grupo03.utils.Pair;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BeNearbyPacman implements RulesAction {

    GHOST ghost;
	public BeNearbyPacman(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if(game.doesGhostRequireAction(ghost)) {
			
			int ghostNode = game.getGhostCurrentNodeIndex(ghost);
			MOVE lastGhostMove = game.getGhostLastMoveMade(ghost);
			int[] vecinos = game.getNeighbouringNodes(ghostNode, lastGhostMove);
			int pacManNode = game.getPacmanCurrentNodeIndex();
			
			double actDist = game.getDistance(ghostNode, game.getPacmanCurrentNodeIndex(), lastGhostMove, DM.PATH);
			
			TreeSet<Pair<Integer,Double>> difDistNeighToActual = new TreeSet<Pair<Integer,Double>>(
					new Comparator<Pair<Integer,Double>>(){
						@Override
						public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
							// TODO Auto-generated method stub
							double dif1 = o1.getSecond();
							double dif2 = o2.getSecond();
							
							if(dif1 < dif2) {return -1;}
							else if (dif1 == dif2) {return 0;}
							else { return 1; }
						}
						
					});
			
			
			for(int i = 0; i < vecinos.length; i++) {
				MOVE moveToNeigh = game.getMoveToMakeToReachDirectNeighbour(ghostNode, vecinos[i]);
				double neighDist = game.getDistance(ghostNode, pacManNode, moveToNeigh, DM.PATH);
				double dif = (actDist - neighDist < 0) ? -1 * actDist - neighDist : actDist - neighDist;
				Pair<Integer, Double> p = new Pair<Integer,Double>(vecinos[i], dif);
				difDistNeighToActual.add(p);
			}
			
			Iterator<Pair<Integer,Double>> it = difDistNeighToActual.iterator();
			
			return game.getMoveToMakeToReachDirectNeighbour(ghostNode, it.next().getFirst());
		}
		else {
			return MOVE.NEUTRAL;
		}
	}

	@Override
	public String getActionId() {
		return ghost+ "staysNearbyPacman";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
