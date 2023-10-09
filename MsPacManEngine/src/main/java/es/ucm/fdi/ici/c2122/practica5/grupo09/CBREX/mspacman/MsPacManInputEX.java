package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInputEX extends CBRInput {

	public MsPacManInputEX(Game game) {
		super(game);
		
	}

	Integer id;
	
	Integer level;
	Integer score;
	Integer time;
	Integer lives;
	
	Integer msPacmanPos;
	Integer msPacmanMove;
	
	Integer[] ghostsPos;
	Boolean[] edibleGhost;
	Integer[] ghostLairTime;
	Integer[] ghostsDir;
	Integer[] ghostsDist;
	Integer[] auxGhostsDist;

	Integer nearestGhost;
	
	Integer nearestPPill;
	Integer remainingPills;
	
	@Override
	public void parseInput() {
		score = game.getScore();
		level = game.getCurrentLevel();
		lives = game.getPacmanNumberOfLivesRemaining();
		time = game.getTotalTime();
		
		msPacmanPos = game.getPacmanCurrentNodeIndex();
		
		msPacmanMove = game.getPacmanLastMoveMade().ordinal();
		
		ghostsPos = new Integer[8];
		edibleGhost = new Boolean[4];
		ghostLairTime = new Integer[4];
		ghostsDir = new Integer[4];
		ghostsDist = new Integer[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
		auxGhostsDist = new Integer[4];
		
		int i = 0;
		for (GHOST g : GHOST.values()) {
			ghostsPos[i] = game.getGhostCurrentNodeIndex(g);
			ghostLairTime[i] = game.getGhostLairTime(g);
			edibleGhost[i] = game.isGhostEdible(g);
			ghostsDir[i] = game.getGhostLastMoveMade(g).ordinal();
			ghostsDist[i] = (int) game.getDistance(ghostsPos[i], msPacmanPos, game.getGhostLastMoveMade(g), DM.PATH);
			auxGhostsDist[i] = ghostsDist[i];
			i++;
		}
		
		sort(ghostsDir);

		computeNearestEdible();

		remainingPills = game.getNumberOfActivePills();
		//computeNearestGhost(game);
		computeNearestPPill(game);
	}

	private void computeNearestEdible() {
		Boolean[] auxEdible = edibleGhost.clone();
		
		for(int i = 0; i < 4; i++) {
			int e = 0;
			while(e < 4 && ghostsDist[i] != auxGhostsDist[e]) {
				e++;
			}
			edibleGhost[i] = auxEdible[e];
		}
	}

	private void sort(Integer[] array) {
		int n = array.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (array[j] > array[j+1]){
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescriptionEX description = new MsPacManDescriptionEX();
		description.setScore(score);
		description.setTime(time);
		description.setLives(lives);
		description.setLevel(level);
		
		description.setMsPacmanPos(msPacmanPos);
		description.setMsPacmanMove(msPacmanMove);
		
		description.setGhostsPos(ghostsPos.clone());
		description.setEdibleGhost(edibleGhost.clone());
		description.setGhostLairTime(ghostLairTime.clone());
		description.setGhostsDir(ghostsDir.clone());
		//description.setGhostsDist(ghostsDist.clone());
		
		description.setNearestPPill(nearestPPill);
		description.setRemainingPills(remainingPills);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestGhost(Game game) {
		nearestGhost = -1;
		Integer distance = Integer.MAX_VALUE;
		for (int i = 0; i < 4; i++) {
			if (distance > ghostsDist[i]) {
				distance = ghostsDist[i];
				nearestGhost = i;
			}
		}
	}
	
	//Devuelve el indice de la powerpill mas cercana
	private void computeNearestPPill(Game game) {
		nearestPPill = -1;
		int maxDistance = Integer.MAX_VALUE;;
		for(int node: game.getPowerPillIndices()) {
			if(game.getPowerPillIndex(node) != -1) {
				int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), node, DM.PATH);
				if(distance < maxDistance) {
					maxDistance = distance;
					nearestPPill = game.getPowerPillIndex(node);
				}		
			}
		}
	}
}
