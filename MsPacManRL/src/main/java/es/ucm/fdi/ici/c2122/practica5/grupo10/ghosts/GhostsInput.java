package es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsInput extends CBRInput {

	GHOST ghost;
	
	Integer distance_to_mspacman_up;
	Integer distance_to_mspacman_down;
	Integer distance_to_mspacman_left;
	Integer distance_to_mspacman_right;
	
	Integer distance_to_ghosts_up;
	Integer distance_to_ghosts_down;
	Integer distance_to_ghosts_left;
	Integer distance_to_ghosts_right;
	
	Integer edible_time_ghost;
	Integer edible_time_ghost_2;
	Integer edible_time_ghost_3;
	Integer edible_time_ghost_4;

	Integer distance_nearest_ppill_mspacman;
	Integer distance_ghost_to_nearest_ppill;
	Integer Score;
	MOVE lastMovePacman;
	MOVE lastMoveGhost;
	Integer MsPacmanDeaths;
	Boolean MsPacManEatMe;
	Boolean eatedMsPacman;
	

	public GhostsInput(Game game) {
		super(game);
	}



	@Override
	public void parseInput() {

	}

	public void parseInput(GHOST ghost,boolean msPacManEatMe,boolean eatedMsPacman) {
		this.ghost=ghost;
		this.MsPacManEatMe=msPacManEatMe;
		this.eatedMsPacman=eatedMsPacman;
		computeEdibleTimes(game);
		computeDistances(game);
		this.lastMoveGhost = game.getGhostLastMoveMade(ghost);
		this.lastMovePacman = game.getPacmanLastMoveMade();
		computeNearestPPillDistances(game);
		this.Score = game.getScore();
		this.MsPacmanDeaths = 3 - game.getPacmanNumberOfLivesRemaining();
	}
	

	private void computeEdibleTimes(Game game) {
		Integer [] edible_time = new Integer[] {0,0,0,0};
		Integer i=0;
		for(GHOST g: GHOST.values()) {
			edible_time[i]=game.getGhostEdibleTime(g);
			i++;
		}
		edible_time_ghost = edible_time[0];
		edible_time_ghost_2= edible_time[1];
		edible_time_ghost_3= edible_time[2];
		edible_time_ghost_4= edible_time[3];
	}



	private void computeDistances(Game game) {
		Integer[] distances_to_ghosts = new Integer[] {500,500,500,500};
		Integer[] distances_to_mspacman = new Integer[] {500,500,500,500};


		//compute minimum distances to mspacman in each possible direction
		for(MOVE m: game.getPossibleMoves(game.getGhostCurrentNodeIndex(this.ghost), game.getGhostLastMoveMade(ghost))) {
			if(game.getGhostLairTime(ghost) <= 0) {
				Integer distance = game.getShortestPathDistance(game.getNeighbour(game.getGhostCurrentNodeIndex(ghost), m),game.getPacmanCurrentNodeIndex(), m);
				distances_to_mspacman[m.ordinal()] = distance;
			}

		}
		
		for(MOVE m: game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			for(GHOST g: GHOST.values()) {
				if(!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
					//for each direction compute the shortest distance from PACMAN to GHOST
					int distance = game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), game.getGhostCurrentNodeIndex(g), m);
					//if the ghost can't take the path we ignore it
					int[] path = game.getShortestPath(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), game.getGhostCurrentNodeIndex(g), m);
					if(path.length >= 2)
						if(game.getGhostLastMoveMade(g) == game.getMoveToMakeToReachDirectNeighbour(path[path.length-1], path[path.length-2])) 
							if (distance < distances_to_ghosts[m.ordinal()]) 
								distances_to_ghosts[m.ordinal()] = distance;			
				}
			}
		}

		
		
		distance_to_mspacman_up = distances_to_mspacman[MOVE.UP.ordinal()];
		distance_to_mspacman_down = distances_to_mspacman[MOVE.DOWN.ordinal()];
		distance_to_mspacman_left = distances_to_mspacman[MOVE.LEFT.ordinal()];
		distance_to_mspacman_right = distances_to_mspacman[MOVE.RIGHT.ordinal()];


		distance_to_ghosts_up = distances_to_ghosts[MOVE.UP.ordinal()];
		distance_to_ghosts_down = distances_to_ghosts[MOVE.DOWN.ordinal()];
		distance_to_ghosts_left = distances_to_ghosts[MOVE.LEFT.ordinal()];
		distance_to_ghosts_right = distances_to_ghosts[MOVE.RIGHT.ordinal()];



	}


	private void computeNearestPPillDistances(Game game) {
		Integer min = 500;
		Integer index_min = -1;
		int[] ppills = game.getActivePowerPillsIndices();
		for(Integer ppill : ppills) {
			Integer distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), ppill, this.lastMovePacman);
			if(distance < min) {
				min = distance;
				index_min= ppill;
			}
		}
		this.distance_nearest_ppill_mspacman = min;
		if(index_min!= -1) {
			this.distance_ghost_to_nearest_ppill = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(this.ghost), index_min, this.lastMoveGhost);
		}
		else
			this.distance_ghost_to_nearest_ppill = 500;

	}


	@Override
	public CBRQuery getQuery() {

		GhostsDescription description = new GhostsDescription();
		
		description.setGhost(ghost);
		description.setDistance_to_mspacman_up(distance_to_mspacman_up);
		description.setDistance_to_mspacman_down(distance_to_mspacman_down);
		description.setDistance_to_mspacman_left(distance_to_mspacman_left);
		description.setDistance_to_mspacman_right(distance_to_mspacman_right);
		description.setDistance_to_ghosts_up(distance_to_ghosts_up);
		description.setDistance_to_ghosts_down(distance_to_ghosts_down);
		description.setDistance_to_ghosts_left(distance_to_ghosts_left);
		description.setDistance_to_ghosts_right(distance_to_ghosts_right);
		description.setEdible_time_ghost(edible_time_ghost);
		description.setEdible_time_ghost_2(edible_time_ghost_2);
		description.setEdible_time_ghost_3(edible_time_ghost_3);
		description.setEdible_time_ghost_4(edible_time_ghost_4);
		description.setDistance_nearest_ppill_mspacman(distance_nearest_ppill_mspacman);
		description.setDistance_ghost_to_nearest_ppill(distance_ghost_to_nearest_ppill);
		description.setScore(Score);
		description.setLastMovePacman(lastMovePacman);
		description.setLastMoveGhost(lastMoveGhost);
		description.setMsPacmanDeaths(MsPacmanDeaths);
		description.setMsPacManEatMe(MsPacManEatMe);
		description.setEatedMsPacman(eatedMsPacman);

		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}







}
