package es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;

import es.ucm.fdi.ici.cbr.CBRInput;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsInput extends CBRInput {

	Integer time;
	Integer score;
	Integer lives;

	Integer mspacmanPos;
	Integer mspacmanLastMove;

	Integer[] nearestGhostsDistance;
	Boolean[] nearestGhostsEdible;
	MOVE[] nearestGhostsLastMoves;

	Integer ghostIndex;
	
	GHOST ghostType;

	public GhostsInput(Game game) {
		super(game);
	}

	public void setGhost(GHOST ghost) {
		this.ghostType = ghost;
	}

	@Override
	public void parseInput() {
		if (ghostType == null)
			return;
		
		time = game.getTotalTime();
		score = game.getScore();
		lives = game.getPacmanNumberOfLivesRemaining();

		mspacmanPos = game.getPacmanCurrentNodeIndex();
		mspacmanLastMove = game.getPacmanCurrentNodeIndex();
		
		Integer nodePacman = game.getPacmanCurrentNodeIndex();
		nearestGhostsDistance = new Integer[4];
		GHOST[] ghosts = new GHOST[4];
		
		for(int ghostIdx = 0; ghostIdx < GHOST.values().length; ghostIdx++) {
			if (game.getGhostLairTime(GHOST.values()[ghostIdx]) <= 0) {
				double dst = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.values()[ghostIdx]), 
						nodePacman, game.getGhostLastMoveMade(GHOST.values()[ghostIdx]));
				nearestGhostsDistance[ghostIdx] = (int) dst;
			}
			else {
				nearestGhostsDistance[ghostIdx] = Integer.MAX_VALUE;
			}
			
			GHOST g = GHOST.values()[ghostIdx];
			ghosts[ghostIdx] = g;
		}
		sort(nearestGhostsDistance, ghosts);
		
		nearestGhostsEdible = new Boolean[4];
		nearestGhostsLastMoves = new MOVE[4];
		for(int ghostIdx = 0; ghostIdx < GHOST.values().length; ghostIdx++) {
			GHOST ghost = ghosts[ghostIdx];
			if (ghost == ghostType) ghostIndex = ghostIdx;
			
			nearestGhostsEdible[ghostIdx] = game.isGhostEdible(ghost);
			nearestGhostsLastMoves[ghostIdx] = game.getGhostLastMoveMade(ghost);
		}
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();

		description.setTime(time);
		description.setScore(score);
		description.setLives(lives);
		
		description.setMspacmanPos(mspacmanPos);
		description.setMspacmanLastMove(mspacmanLastMove);

		description.setNearestGhostsDistance(nearestGhostsDistance);
		description.setNearestGhostsEdible(nearestGhostsEdible);
		description.setNearestGhostsLastMoves(nearestGhostsLastMoves);

		description.setGhostIndex(ghostIndex);

		CBRQuery query = new CBRQuery();
		query.setDescription(description);

		return query;
	}

	private void sort(Integer[] nearestGhostsDistance, GHOST[] ghosts) {
		int n = nearestGhostsDistance.length;
	    for (int i = 0; i < n - 1; i++) {
	         for (int j = 0; j < n - i - 1; j++) {
	             if (nearestGhostsDistance[j] > nearestGhostsDistance[j + 1]){
	                 int tempDist = nearestGhostsDistance[j];
	                 GHOST tempGhost = ghosts[j];
	                
	                 nearestGhostsDistance[j] = nearestGhostsDistance[j + 1];
	                 ghosts[j] = ghosts[j + 1];
	                
	                 nearestGhostsDistance[j + 1] = tempDist;
	                 ghosts[j + 1] = tempGhost;
	             }
	         }
	     }
	}
	
}
