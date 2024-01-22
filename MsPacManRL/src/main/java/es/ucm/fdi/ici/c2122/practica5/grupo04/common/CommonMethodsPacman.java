package es.ucm.fdi.ici.c2122.practica5.grupo04.common;


import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class CommonMethodsPacman {

	//float dangerThreshold = 0.5f;
	static float dangerMult = 1.65f;
	//int edibleVision = 25 * 5;
	//int pPillCloseDistance = 15 * 5;


	// Una vez decidida la siguiente accion y su camino, se comprueba que no haya
	// fantasmas en medio, y en caso de que haya, se cambia de ruta para esquivarlos.
	public static MOVE avoidGhosts(Game game, int currPos, int goalPos) {
		MOVE lastMoveMade = game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())[0];
		if( !game.isJunction(currPos)) return lastMoveMade;
		//Busca la siguiente interseccion de una posicion a su destino
		int[] path = game.getShortestPath(currPos, goalPos, lastMoveMade);
		int nextInter = -1;
		for (int i = 0; i < path.length; i++) {
			if (game.isJunction(path[i])) {
				nextInter = path[i];
				break;
			}
		}

		int[] adjs = getAdjacentJunctions(game, currPos);
		//buscamos la junction a la que llegaremos pasando por el goalpos
		int i = 0;
		if(!game.isJunction(goalPos)) {
			outer:
				for(; i < adjs.length; ++i) {
					//si encontramos el destino entre una de las adyacentes hemos encontrado la junction destino
					int[] adjsAux = game.getShortestPath(currPos, adjs[i]);
					for(int j = 0; j < adjsAux.length; ++j) {
						if(adjsAux[j] == goalPos) {
							break outer;
						}
					}
				}
		}
		if (nextInter >= 0 && !ghostInPathOrJunction(game, currPos, nextInter, lastMoveMade)) 
			return game.getNextMoveTowardsTarget(currPos, nextInter, Constants.DM.PATH);
		if(nextInter < 0 && i != adjs.length && !ghostInPathOrJunction(game, currPos, adjs[i], lastMoveMade))
			return game.getNextMoveTowardsTarget(currPos, adjs[i], Constants.DM.PATH);

		int dirs = game.getCurrentMaze().graph[currPos].allPossibleMoves.get(lastMoveMade).length;
		int pos_juncts[][] = new int [dirs][2];//posicion a la que nos moveremos y la junct a la que llegaremos
		for( i = 0; i < dirs; i++) {
			MOVE lastMove = game.getCurrentMaze().graph[currPos].allPossibleMoves.get(lastMoveMade)[i];
			int newPos = game.getCurrentMaze().graph[currPos].allNeighbouringNodes.get(lastMoveMade)[i];
			int[] junts = getAdjacentJunctions(game,newPos, lastMove);
			int nextJunc = (junts[0] == currPos) ? junts[1] : junts[0];	
			pos_juncts[i][0]=newPos;
			pos_juncts[i][1]=nextJunc;

		}
		
		//ordenamos las junctions por peligro
		for( i = 0; i < dirs -1; i++) {
			for(int j = 0; j < dirs - 1; ++j) {

				if(pos_juncts[j][1] != adjs[j] ) {
					int aux = pos_juncts[j][0];
					pos_juncts[j][0] = pos_juncts[j+1][0];
					pos_juncts[j+1][0] = aux;
					
					aux = pos_juncts[j][1];
					pos_juncts[j][1] = pos_juncts[j+1][1];
					pos_juncts[j+1][1] = aux;
				}
			}
		}
		
		for( i = 0; i < dirs; i++) {			
			MOVE lastMove = game.getNextMoveTowardsTarget(pos_juncts[i][0], pos_juncts[i][1], DM.PATH);
			if(!ghostInPathOrJunction(game, pos_juncts[i][0], pos_juncts[i][1], lastMove)) {
				return game.getNextMoveTowardsTarget(currPos, pos_juncts[i][0], Constants.DM.PATH);
			}

		}

		for( i = 0; i < dirs; i++) {
			
			MOVE lastMove = game.getNextMoveTowardsTarget(pos_juncts[i][0], pos_juncts[i][1], DM.PATH);

			if(!ghostInPath(game, pos_juncts[i][0], pos_juncts[i][1], lastMove)) {
				return game.getNextMoveTowardsTarget(currPos, pos_juncts[i][0], Constants.DM.PATH);
			}

		}
		return game.getNextMoveTowardsTarget(currPos, CommonMethodsPacman.getClosestPill(game, currPos), Constants.DM.PATH);
	}

	// Devuelve true si hay fantasmas entre dos posiciones
	public static boolean ghostInPath(Game game, int currPos, int goalPos, MOVE lastMove) {
		for (GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) > 0 || game.isGhostEdible(g)) continue;
			int[] path = game.getShortestPath(currPos, goalPos, lastMove);
			for(int i : path) {
				if(i == game.getGhostCurrentNodeIndex(g))
					return true;

			}
		}
		return false;
	}

	// Detecta si hay fantasmas entre dos posiciones o pueden llegar antes a la posicion destino
	// Devuelve true si hay peligro
	public static boolean ghostInPathOrJunction(Game game, int currPos, int goalPos, MOVE lastMove) {
		for (GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) > 0 || game.isGhostEdible(g)) continue;
			MOVE pathToFollow = game.getApproximateNextMoveTowardsTarget(currPos, goalPos, lastMove, Constants.DM.PATH);
			int distanceGhost = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), goalPos, game.getGhostLastMoveMade(g));
			int distanceJunct = game.getShortestPathDistance(currPos, goalPos, lastMove);
			//int distanceGP = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), currPos, game.getGhostLastMoveMade(g));

			if( (distanceGhost < distanceJunct && game.getGhostLastMoveMade(g) != pathToFollow)|| //llega antes a la interseccion y no va por delante de pacman
					ghostInPath(game,currPos,goalPos,lastMove)) {//esta entre la interseccion y pacma
				return true;
			}
		}
		return false;
	}

	public static int getPathNumPills(Game game, int[] path) {
		int numPills = 0;
		for(int i : path) {
			if(game.getPillIndex(i) != -1 && game.isPillStillAvailable(i)) {
				++numPills;
			}
		}
		return numPills;
	}

	public static int[] getPathPills(Game game, int[] path) {
		int numPills = getPathNumPills(game, path);
		int[] pills = new int[numPills];
		int count = 0;
		for(int i : path) {
			if(game.getPillIndex(i) != -1 && game.isPillStillAvailable(i)) {
				pills[count++] = path[i];
			}
		}
		return pills;
	}
	
	//Devuelve el indice de la pill activa mas cercana
	public static int getClosestPill(Game game, int pos) {
		int closestPillDist = Integer.MAX_VALUE;
		int tempPill = -1;
		if(game.getNumberOfActivePills() <= 0) return -1;
		
		int[] pills = game.getActivePillsIndices();
		for(int i = 0; i < pills.length; ++i) {
			int dist = game.getShortestPathDistance(pills[i], pos);
			if(dist < closestPillDist) {
				tempPill = pills[i];
			}

			closestPillDist = Math.min(dist, closestPillDist);
		}
		return tempPill;
	}
	
	

	//Devuelve el indice de la Ppill activa mas cercana
	public static int getClosestPPill(Game game, int pos) {
		int closestPPillDist = Integer.MAX_VALUE;
		int tempPPill = -1;
		if(game.getNumberOfActivePowerPills() <= 0) return -1;
		
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

	public static float dangerLevel(Game game, int pos, float mult) {
		float danger = 0;
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) > 0 ) continue;
			if(!game.isGhostEdible(ghostType))
				danger += 120.0f / Math.pow(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pos, game.getGhostLastMoveMade(ghostType)), mult);
			else if(game.getGhostEdibleTime(ghostType) < 20)
				danger += 60.0f / Math.pow(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pos, game.getGhostLastMoveMade(ghostType)), mult);
			else
				danger -= 20.0f / Math.pow(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pos, game.getGhostLastMoveMade(ghostType)), mult);

		}
		return danger;
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
//DOWN 1122 false LEFT
	public static int[] getAdjacentJunctions(Game game, int pos) {
		MOVE m = game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : MOVE.NEUTRAL;
		return getAdjacentJunctions(game, pos, m);
	}
	
	public static int[] getAdjacentJunctions(Game game, int pos, MOVE lastMoveMade) {
		if(game.getCurrentMaze().graph[pos].allPossibleMoves.get(lastMoveMade) == null) {
			MOVE[] posMoves = game.getPossibleMoves(pos);
			lastMoveMade = posMoves[0].opposite();
		}
		int dirs = game.getCurrentMaze().graph[pos].allPossibleMoves.get(lastMoveMade).length;
		float[] closestJuncDangs = new float[dirs];
		int[] res = new int[dirs];
		for(int i = 0; i < dirs; i++) {
			MOVE lastMove = game.getCurrentMaze().graph[pos].allPossibleMoves.get(lastMoveMade)[i];
			int newPos = game.getCurrentMaze().graph[pos].allNeighbouringNodes.get(lastMoveMade)[i];
			while(game.getCurrentMaze().graph[newPos].allPossibleMoves.get(lastMove).length < 2) {
				int auxPos = newPos;
				newPos = game.getCurrentMaze().graph[newPos].allNeighbouringNodes.get(lastMove)[0];
				lastMove = game.getCurrentMaze().graph[auxPos].allPossibleMoves.get(lastMove)[0];
			}
			res[i] = newPos;
			closestJuncDangs[i] = dangerLevel(game, res[i], dangerMult);
		}
		return getJuncsOrdered(res, closestJuncDangs);
	}
	
	public static int[] getAdjacentJunctionsNaturalOrder(Game game, int pos, MOVE lastMoveMade) {
		if(game.getCurrentMaze().graph[pos].allPossibleMoves.get(lastMoveMade) == null) {
			MOVE[] posMoves = game.getPossibleMoves(pos);
			lastMoveMade = posMoves[0].opposite();
		}
		int[] res = {-1, -1, -1, -1};
		int dirs = game.getCurrentMaze().graph[pos].allPossibleMoves.get(lastMoveMade).length;
		for(int i = 0; i < dirs; i++) {
			MOVE lastMove = game.getCurrentMaze().graph[pos].allPossibleMoves.get(lastMoveMade)[i];
			int newPos = game.getCurrentMaze().graph[pos].allNeighbouringNodes.get(lastMoveMade)[i];
			while(game.getCurrentMaze().graph[newPos].allPossibleMoves.get(lastMove).length < 2) {
				int auxPos = newPos;
				newPos = game.getCurrentMaze().graph[newPos].allNeighbouringNodes.get(lastMove)[0];
				lastMove = game.getCurrentMaze().graph[auxPos].allPossibleMoves.get(lastMove)[0];
			}
			res[lastMove.ordinal()] = newPos;
		}
		return res;
	}


	//devuelve el array closestJuncs ordenado de menor a mayor
	public static int[] getJuncsOrdered(int[] closestJuncs, float[] closestJuncDists) {
		for(int i = 0; i < closestJuncs.length; ++i) {
			for(int j = 0; j < closestJuncs.length - 1; ++j) {
				if(closestJuncDists[j] > closestJuncDists[j + 1]) {
					int junc = closestJuncs[j + 1];
					closestJuncs[j + 1] = closestJuncs[j];
					closestJuncs[j] = junc;

					float juncf = closestJuncDists[j + 1];
					closestJuncDists[j+1] = closestJuncDists[j];
					closestJuncDists[j] = juncf;
				}
			}
		}

		return closestJuncs;
	}
}
