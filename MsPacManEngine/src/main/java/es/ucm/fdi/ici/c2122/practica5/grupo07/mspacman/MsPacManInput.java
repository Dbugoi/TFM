package es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ArrayTypeAdaptor;
import es.ucm.fdi.ici.c2122.practica5.grupo07.Util;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	Double score;	
	Double oldscore;

	Integer time;
	
	ArrayTypeAdaptor danger;
	ArrayTypeAdaptor edible;
	ArrayTypeAdaptor edible34;
	Double nearestGhost;
	MOVE lastmove;
	
	@Override
	public void parseInput() {
		//computeNearestGhost(game);
		//computeNearestPPill(game);
		//time = game.getTotalTime();
		oldscore = (double) game.getScore();
		score=PacmanHeuristicFunction.getValue(game,0.0);
		computeDanger(game);
		computeEdible(game);
		//computeDanger34(game);
		computeEdible34(game);
		lastmove=game.getPacmanLastMoveMade();
		computeNearestGhost(game);
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();

		description.setScore(score);
		description.setOldscore(oldscore);
		description.setDanger(danger);
		description.setEdible(edible);
		description.setEdible34(edible34);
		description.setLastmove(lastmove);
		description.setNearestGhost(nearestGhost);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	private void computeNearestGhost(Game game) {
		double minDist=200.0;
		for(GHOST g:GHOST.values()) {
			if(game.getGhostLairTime(g)==0) {
				minDist=Math.min(minDist, game.getDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), DM.PATH));
			}
			else {
				minDist=Math.min(minDist, game.getDistance(game.getGhostInitialNodeIndex(), game.getPacmanCurrentNodeIndex(), MOVE.NEUTRAL, DM.PATH));
			}
		}
		nearestGhost=minDist;
	}
	private void computeDanger(Game game) {
		ArrayList<MOVE> safeMoves=Util.getSafeMoves(game);
		ArrayList<Double>aux2=new ArrayList<Double>();
		for(MOVE m:MOVE.values()) {
			if(m==MOVE.NEUTRAL)continue;
			aux2.add(0.0);
			for(MOVE m2:safeMoves) {
				if(m==m2) {
					aux2.remove(aux2.size()-1);
					aux2.add(100.0);
					break;
				}
			}
		}
		danger= new ArrayTypeAdaptor(aux2);
		
	}

	private void computeEdible(Game game) {
		
		//ArrayList<MOVE> safeMoves=Util.getSafeMoves(game);
		ArrayList<Double>aux2=new ArrayList<Double>();
		for(MOVE m:MOVE.values()) {
			if(m==MOVE.NEUTRAL)continue;
			aux2.add(0.0);
			for(MOVE m2:game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
				if(m==m2) {
					aux2.remove(aux2.size()-1);
					aux2.add(Util.getEdible(game, game.getPacmanCurrentNodeIndex(), m));
					break;
				}
			}
		}
		/*Double[] aux=new Double[4];
		for(MOVE m:MOVE.values()) {
			if(m.ordinal()==4)continue;
			if(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m)!=-1) {
				aux[m.ordinal()]=Util.getEdible(game, game.getPacmanCurrentNodeIndex(), m);
			}
			else {
				aux[m.ordinal()]=0.0;
			}
		}
		ArrayList<Double>aux2=new ArrayList<Double>();
		for(Double a:aux) {
			aux2.add(a);
		}*/
		edible= new ArrayTypeAdaptor(aux2);
		
	}

	private void computeEdible34(Game game) {
		Double[] aux=new Double[4];
		for(MOVE m:MOVE.values()) {
			if(m.ordinal()==4)continue;
			if(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m)!=-1) {
				aux[m.ordinal()]=Util.getEdible34(game, game.getPacmanCurrentNodeIndex(), m);
			}
			else {
				aux[m.ordinal()]=0.0;
			}
		}
		ArrayList<Double>aux2=new ArrayList<Double>();
		for(Double a:aux) {
			aux2.add(a);
		}
		edible34= new ArrayTypeAdaptor(aux2);
		
	}
	/*private void computeNearestGhost(Game game) {
		nearestGhost = Integer.MAX_VALUE;
		edible = false;
		GHOST nearest = null;
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			int distance; 
			if(pos != -1) 
				distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			else
				distance = Integer.MAX_VALUE;
			if(distance < nearestGhost)
			{
				nearestGhost = distance;
				nearest = g;
			}
		}
		if(nearest!=null)
			edible = game.isGhostEdible(nearest);
	}
	
	private void computeNearestPPill(Game game) {
		nearestPPill = Integer.MAX_VALUE;
		for(int pos: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestGhost)
				nearestPPill = distance;
		}
	}*/
}
