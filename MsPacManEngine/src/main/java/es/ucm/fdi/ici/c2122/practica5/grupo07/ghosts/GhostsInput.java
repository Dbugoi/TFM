package es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ArrayTypeAdaptor;
import es.ucm.fdi.ici.c2122.practica5.grupo07.Util;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends CBRInput {
	private GHOST ghost;
	public GhostsInput(Game game, GHOST ghost) {
		super(game);
		this.ghost=ghost;
	}

	Double score;	
	Double oldscore;
	Double pacmamppdistance;
	Integer time;
	Boolean edible;
	ArrayTypeAdaptor pacmanProximity;
	
	MOVE lastmove;
	
	@Override
	public void parseInput() {
		//computeNearestGhost(game);
		//computeNearestPPill(game);
		//time = game.getTotalTime();
		if(ghost==null) {
			oldscore = (double) game.getScore();
			score=0.0;
			//computePromximity(game);
			lastmove=MOVE.NEUTRAL;
			edible=false;
			pacmamppdistance=500.0;
			return;
		}
		
		oldscore = (double) game.getScore();
		score=GhostsHeuristicFunction.getValue(game,ghost);
		computePromximity(game);
		lastmove=game.getGhostLastMoveMade(ghost);
		edible=game.isGhostEdible(ghost);
		computepacmamppdistance(game);
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();

		description.setScore(score);
		description.setOldscore(oldscore);
		description.setPacmanProximity(pacmanProximity);
		description.setLastmove(lastmove);
		description.setEdible(edible);
		description.setPacmamppdistance(pacmamppdistance);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	private void computepacmamppdistance(Game game) {
		if(game.getNumberOfActivePowerPills()==0) {
			pacmamppdistance=500.0;
		}
		else {
			double mindist=5000.0;
			for(int pp:game.getActivePowerPillsIndices()) {
				mindist=Math.min(mindist, game.getDistance(game.getPacmanCurrentNodeIndex(), pp, game.getPacmanLastMoveMade(), DM.PATH));
			}
			pacmamppdistance= mindist;
		}
	}
	private void computePromximity(Game game) {
		ArrayList<Double>aux2=new ArrayList<Double>();
		for(MOVE m:MOVE.values()) {
			if(m==MOVE.NEUTRAL)continue;
			int ghostIndex=game.getGhostCurrentNodeIndex(ghost);
			MOVE ghostLastMove=game.getGhostLastMoveMade(ghost);
			aux2.add(100.0);
			for(MOVE m2:game.getPossibleMoves(ghostIndex, ghostLastMove)) {
				if(m!=m2)continue;
				int nextLocation=Util.getNextLocation(game, ghostIndex, ghostLastMove);
				int[] path=game.getShortestPath(ghostIndex, nextLocation, m2);
				
				aux2.remove(aux2.size()-1);
				aux2.add((double) game.getShortestPathDistance(nextLocation, game.getPacmanCurrentNodeIndex()));
				
				for(int p:path) {
					if(game.getPacmanCurrentNodeIndex()==p){
						aux2.remove(aux2.size()-1);
						aux2.add(0.0);
						break;
					}
				}
			}
			
			
			
		}
		pacmanProximity= new ArrayTypeAdaptor(aux2);
		
	}
	
}
