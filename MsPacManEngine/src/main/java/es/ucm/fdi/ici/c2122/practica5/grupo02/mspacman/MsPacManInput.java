package es.ucm.fdi.ici.c2122.practica5.grupo02.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;

import es.ucm.fdi.ici.cbr.CBRInput;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	Integer score;
	Integer time;
	Integer distanceNearestPPill;
	Integer distanceNearestGhost;
	Integer nearestNodeGhost;
	Boolean edible;
	Integer livesLeft;
	
	@Override
	public void parseInput() {
		computeNearestGhost(game);
		computeNearestPPill(game);
		time = game.getTotalTime();
		score = game.getScore();
		livesLeft = game.getPacmanNumberOfLivesRemaining();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setEdibleGhost(edible);
		description.setNearestNodeGhost(nearestNodeGhost);
		description.setDistanceNearestGhost(distanceNearestGhost);
		description.setDistanceNearestPPill(distanceNearestPPill);
		description.setScore(score);
		description.setTime(time);
		description.setLivesLeft(livesLeft);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestGhost(Game game) {
		nearestNodeGhost = -1;
		distanceNearestGhost = Integer.MAX_VALUE;
		edible = false;
		GHOST nearest = null;
		for(GHOST g: GHOST.values()) {
			if(game.getGhostLairTime(g) == 0) {
				int pos = game.getGhostCurrentNodeIndex(g);
				int distance = (int)game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pos);
				
				if(distance < distanceNearestGhost){
					nearestNodeGhost = game.getGhostCurrentNodeIndex(g);
					distanceNearestGhost = distance;
					nearest = g;
					edible = game.isGhostEdible(g);
				}
			}
		}
	}
	
	private void computeNearestPPill(Game game) {
		distanceNearestPPill = Integer.MAX_VALUE;
		for(int pos: game.getActivePowerPillsIndices()) {
			int distance = (int)game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pos);
			if(distance < distanceNearestPPill)
				distanceNearestPPill = distance;
		}
		
		if(distanceNearestPPill == Integer.MAX_VALUE) distanceNearestPPill = -1;
	}
}
