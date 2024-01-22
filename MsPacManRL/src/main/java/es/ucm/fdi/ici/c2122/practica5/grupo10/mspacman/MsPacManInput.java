package es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	Integer score;
	int[] ghostDistance;
	int[] edibleDistance;
	int[] pillsDistance;
	int[] powerPillsDistance;
	String validDirections;
	Integer lives;
	final int MAX = 200;
	
	@Override
	public void parseInput() {
		score = game.getScore();
		computeNearestGhost(game);
		computeNearestPill(game);
		computeNearestPPill(game);
		computeMoves();
		lives = game.getPacmanNumberOfLivesRemaining();

	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setScore(score);
		description.setGhostDistance1(ghostDistance[0]);
		description.setGhostDistance2(ghostDistance[1]);
		description.setGhostDistance3(ghostDistance[2]);
		description.setGhostDistance4(ghostDistance[3]);
		description.setEdibleDistance1(edibleDistance[0]);
		description.setEdibleDistance2(edibleDistance[1]);
		description.setEdibleDistance3(edibleDistance[2]);
		description.setEdibleDistance4(edibleDistance[3]);
		description.setPillsDistance1(pillsDistance[0]);
		description.setPillsDistance2(pillsDistance[1]);
		description.setPillsDistance3(pillsDistance[2]);
		description.setPillsDistance4(pillsDistance[3]);
		description.setPowerPillsDistance1(powerPillsDistance[0]);
		description.setPowerPillsDistance2(powerPillsDistance[1]);
		description.setPowerPillsDistance3(powerPillsDistance[2]);
		description.setPowerPillsDistance4(powerPillsDistance[3]);
		description.setValidDirections(validDirections);
		description.setLives(lives);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeMoves() {
		//Represents valid directions in a String of 4 characters
		//0 -> invalid, 1 -> valid
		String moves = "0000";
		for (MOVE m : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())){
			moves = changeCharInPosition(m.ordinal(), '1', moves);
		}
		validDirections = moves;
	}

	private String changeCharInPosition(int position, char ch, String str){
		//aux method to change char in String
	    char[] charArray = str.toCharArray();
	    charArray[position] = ch;
	    return new String(charArray);
	}
	
	private void computeNearestGhost(Game game) {
		
		//compute shortest distances to Ghosts for each direction
		//MAX represents being too far away
		ghostDistance = new int[] {MAX,MAX,MAX,MAX};
		edibleDistance = new int[] {MAX,MAX,MAX,MAX};
		
		for(GHOST g: GHOST.values()) {
			if(!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
				//for each ghost compute the shortest distance from GHOST to PACMAN, and the direction it will come from
				int distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
				int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
				if(path.length >= 2) {	
					MOVE m = game.getMoveToMakeToReachDirectNeighbour(game.getPacmanCurrentNodeIndex(), path[path.length-2]);
					if (m != game.getPacmanLastMoveMade().opposite() && distance < ghostDistance[m.ordinal()]) {
						ghostDistance[m.ordinal()] = distance;
					}
				}
			}
		}
		for(MOVE m: game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			for(GHOST g: GHOST.values()) {
				if(!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
					//for each direction compute the shortest distance from PACMAN to GHOST
					int distance = game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), game.getGhostCurrentNodeIndex(g), m);
					//if the ghost can't take the path we ignore it
					int[] path = game.getShortestPath(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), game.getGhostCurrentNodeIndex(g), m);
					if(path.length >= 2) {	
						if(game.getGhostLastMoveMade(g) == game.getMoveToMakeToReachDirectNeighbour(path[path.length-1], path[path.length-2])) {
							if (distance < ghostDistance[m.ordinal()]) {
								ghostDistance[m.ordinal()] = distance;
							}
						}
					}
				}
				else if (game.isGhostEdible(g) && game.getGhostLairTime(g) == 0){
					//for each direction compute the shortest distance from PACMAN to EDIBLE
					int distance = game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), game.getGhostCurrentNodeIndex(g), m);
					if (distance < edibleDistance[m.ordinal()]) {
							edibleDistance[m.ordinal()] = distance;
					}
				}
			}
		}
	}
	
	private void computeNearestPill(Game game) {
		//compute shortest distances to Pills for each direction
		//MAX represents being too far away
		pillsDistance = new int[] {MAX,MAX,MAX,MAX};
		for(MOVE m: game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			for(int pos: game.getPillIndices()) {
				int distance = game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), pos, m);
				if(distance < pillsDistance[m.ordinal()])
					pillsDistance[m.ordinal()] = distance;
			}
		}	
	}
	
	private void computeNearestPPill(Game game) {
		//compute shortest distances to Power Pills for each direction
		//MAX represents being too far away
		powerPillsDistance = new int[] {MAX,MAX,MAX,MAX};
		for(MOVE m: game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			for(int pos: game.getPowerPillIndices()) {
				int distance = game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), pos, m);
				if(distance < powerPillsDistance[m.ordinal()])
					powerPillsDistance[m.ordinal()] = distance;
			}
		}
	}
}
