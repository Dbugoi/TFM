package es.ucm.fdi.ici.c2122.practica5.grupo04.common;

import es.ucm.fdi.ici.Input;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public  class CommonMethodsGhosts {
	// Devuelve las intersecciones adyacentes en orden de distancia
	public static int[] getAdjacentJunctions(Game game, int pos, MOVE lastMoveMade) {
		int[] juncs = game.getJunctionIndices();
		MOVE[] posMoves = lastMoveMade == MOVE.NEUTRAL ? game.getPossibleMoves(pos, lastMoveMade) : game.getPossibleMoves(pos);
		int[] closestJuncs = new int[posMoves.length];

		int[] closestJuncDists = new int[posMoves.length]; 
		for(int j = 0; j < closestJuncDists.length; ++j) closestJuncDists[j] = Integer.MAX_VALUE;

		for(int junc : juncs) {
			for(int i = 0; i < posMoves.length; ++i) {
				int dist = -1;
				if(pos != junc && (dist = game.getShortestPathDistance(junc, pos, posMoves[i].opposite())) < closestJuncDists[i]) {
					closestJuncs[i] = junc;
					closestJuncDists[i] = dist;
				}
			}
		}

		return getJuncsOrdered(closestJuncs, closestJuncDists);
	}

	//devuelve el array closestJuncs ordenado de menor a mayor
	public static int[] getJuncsOrdered(int[] closestJuncs, int[] closestJuncDists) {
		for(int i = 0; i < closestJuncs.length; ++i) {
			for(int j = 0; j < closestJuncs.length - 1; ++j) {
				if(closestJuncDists[j] > closestJuncDists[j + 1]) {
					int junc = closestJuncs[j + 1];
					closestJuncs[j + 1] = closestJuncs[j];
					closestJuncs[j] = junc;

					junc = closestJuncDists[j + 1];
					closestJuncDists[j+1] = closestJuncDists[j];
					closestJuncDists[j] = junc;
				}
			}
		}
		return closestJuncs;
	}

	public static float dangerLevel(Game game, int pos, float mult, GHOST whoMe) {
		float danger = 0;
		for (GHOST ghostType : GHOST.values()) {
			if (ghostType == whoMe || game.getGhostLairTime(ghostType) > 0 || game.isGhostEdible(ghostType)) continue;
			danger += 6.0f / Math.pow(game.getDistance(pos, game.getGhostCurrentNodeIndex(ghostType), Constants.DM.EUCLID), mult);
		}
		return danger;
	}

	//devuelve la ppill mas cercana, -1 si no quedan ppills 
	public static int getClosestPPill(Game game, int pos) {

		if(game.getNumberOfActivePowerPills() <= 0) return -1;
		int closestPPillDist = Integer.MAX_VALUE;
		int tempPPill = -1;
		int[] ppills = game.getActivePowerPillsIndices();
		for(int i = 0; i < ppills.length; ++i) {
			int dist = game.getShortestPathDistance(ppills[i], pos);
			if(dist < closestPPillDist) {
				tempPPill = ppills[i];
			}

			closestPPillDist = Math.min(dist, closestPPillDist);
		}
		return tempPPill;
	}


	//devuelve la proxima interseccion de msPacMan
	public static int mspacmanClosestJunction(Game game) {

		int junct =-1;
		int junctDist = Integer.MAX_VALUE;
		int[] juncs = game.getJunctionIndices();
		for(int j:juncs) {
			int tempDist=game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), j, game.getPacmanLastMoveMade());
			if( tempDist < junctDist) {
				junctDist = tempDist;
				junct = j;
			}
		}
		return junct;
	}

	// Mira si esta en un cono respecto al pacman, utilizando look como el vector forward
	public static boolean withinTheCone(Input in, double distance, double angleMax, GHOST ghost, Constants.MOVE look) {
		Game game = in.getGame();
		int posP = game.getPacmanCurrentNodeIndex(), 
				pos = game.getGhostCurrentNodeIndex(ghost),
				dx = game.getCurrentMaze().graph[pos].x - game.getCurrentMaze().graph[posP].x, 
				dy = game.getCurrentMaze().graph[pos].y - game.getCurrentMaze().graph[posP].y;

		if(game.getEuclideanDistance(pos, posP) > distance) return false;

		double angle = Math.atan2(dy, dx);
		angle = Math.toDegrees(angle);
		switch(look) {
		case DOWN:
			return (90 - angleMax/2 > angle && 90 + angleMax/2 < angle );
		case LEFT:
			return (180 - angleMax/2 > angle && -180 + angleMax/2 < angle );
		case NEUTRAL:
			return false;
		case RIGHT:
			return (0 - angleMax/2 > angle && 0 + angleMax/2 < angle );
		case UP:
			return (-90 - angleMax/2 > angle && -90 + angleMax/2 < angle );
		}
		return false;
	}

	// Coge el fantasma que es comestible mas cercano a pacman
	public static Constants.GHOST getClosestEdibleGhost(Game game, int checkDistance) {
		int pos = game.getPacmanCurrentNodeIndex();
		return getNearestGhost(game, pos, checkDistance, true);
	}

	//si todos estan en la carcel, devuelve null, sino devuelve el fantasma mas cercano 
	//si el ultimo parametro es true, devuelve el fantasma comible mas cercano
	public static Constants.GHOST getNearestGhost(Game game, int pos, int limit, Boolean hasToBeEdible) {
		int nearestDistance = Integer.MAX_VALUE;
		Constants.GHOST nearestGhost = null;
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			if(game.getGhostLairTime(g)<=0) {
				int dist = game.getShortestPathDistance(ghost,pos);
				if(dist < nearestDistance && dist < limit) {
					if(!hasToBeEdible || game.isGhostEdible(g) ) {
						nearestGhost = g;
						nearestDistance = dist;
					}
				}
			}
		}
		return nearestGhost;
	}
	
	public static Constants.GHOST getNearestGhostNotEdible(Game game, int pos, int limit) {
		int nearestDistance = Integer.MAX_VALUE;
		Constants.GHOST nearestGhost = null;
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			if(game.getGhostLairTime(g)<=0) {
				int dist = game.getShortestPathDistance(ghost,pos);
				if(dist < nearestDistance && dist < limit) {
					if(!game.isGhostEdible(g) ) {
						nearestGhost = g;
						nearestDistance = dist;
					}
				}
			}
		}
		return nearestGhost;
	}

	// Devuelve true si hay fantasmas entre dos posiciones
	public static boolean edibleGhostInPath(Game game, int currPos, int goalPos, MOVE lastMove) {
		for (GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) > 0 || !game.isGhostEdible(g)) continue;
			int[] path = game.getShortestPath(currPos, goalPos, lastMove);
			for(int i : path) {
				if(i == game.getGhostCurrentNodeIndex(g))
					return true;

			}
		}
		return false;
	}

	public static boolean pacmanInPath(Game game, int currPos, int goalPos, MOVE lastMove) {
		int[] path = game.getShortestPath(currPos, goalPos, lastMove);
		for(int i : path) {
			if(i == game.getPacmanCurrentNodeIndex())
				return true;
		}
		return false;
	}

	public static MOVE escapeToFurthestJunction(Game game, GHOST ghost) {
		int pos = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		int[] juncs = CommonMethodsGhosts.getAdjacentJunctions(game, pos, lastMove);
		int objJunc = -1;
		int i = juncs.length - 1;
		while(i > 0 && (CommonMethodsPacman.ghostInPath(game, pos, juncs[i], lastMove) ||
				pacmanInPath(game, pos, juncs[i], lastMove))) { --i; }	
		if(i > 0) objJunc = juncs[i];
		else {
			//objJunc = juncs[juncs.length - 1];
			while(i > 0 && pacmanInPath(game, pos, juncs[i], lastMove)) { --i; }	
			objJunc = juncs[i];

		}
		

		return game.getNextMoveTowardsTarget(pos, objJunc, Constants.DM.PATH);
	}
}
