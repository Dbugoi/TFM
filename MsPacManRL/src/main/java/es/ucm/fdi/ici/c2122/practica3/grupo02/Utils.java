package es.ucm.fdi.ici.c2122.practica3.grupo02;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import java.awt.Color;
import java.util.Random;

import pacman.game.Game;
import pacman.game.GameView;

public final class Utils {
	
	public static int nodePacManTarget = -1;
	public static int ghostNodeToFleeFrom = -1;
	public static GHOST ghostFleeFromType = null;
	
	public static int seerPill = 0;
	
	public static Random rnd = new Random();
	
	public static GHOST nearestGhostInRange(Game game, int chaseLimit) {
		int pcNode = game.getPacmanCurrentNodeIndex();
		GHOST ghost = null;

		int nearestGhostDistance = Integer.MAX_VALUE;
		for (GHOST ghostType : GHOST.values()) {
			int ghostIndex = game.getGhostCurrentNodeIndex(ghostType);
			
			//If the ghost is not into jail, we check if it's near to MsPacMan 
			if (game.getGhostLairTime(ghostType) <= 0) {
				int distance = game.getShortestPathDistance(pcNode, ghostIndex, game.getPacmanLastMoveMade());

				if (distance <= chaseLimit && nearestGhostDistance > distance) {
					ghost = ghostType;
					nearestGhostDistance = distance;
				}
			}
		}

		return ghost;
	}
	
	public static GHOST nearestGhost(Game game) {
		int pcNode = game.getPacmanCurrentNodeIndex();
		GHOST ghost = null;

		int nearestGhostDistance = Integer.MAX_VALUE;
		for (GHOST ghostType : GHOST.values()) {
			int ghostIndex = game.getGhostCurrentNodeIndex(ghostType);
			
			//If the ghost is not into jail, we check if it's near to MsPacMan 
			if (game.getGhostLairTime(ghostType) <= 0) {
				int distance = game.getShortestPathDistance(pcNode, ghostIndex, game.getPacmanLastMoveMade());

				if (nearestGhostDistance > distance) {
					ghost = ghostType;
					nearestGhostDistance = distance;
				}
			}
		}

		return ghost;
	}
	
	public static int getPathWithMorePills(Game game, int pcNode) {
		MOVE[] possibleMoves = game.getPossibleMoves(pcNode, game.getPacmanLastMoveMade());
		
		int bestWayNode = -1;
		int nPills = 0;
		int nodeDir = pcNode;
		for(MOVE moves : possibleMoves){
			int auxNumPills = 0;
			nodeDir = game.getNeighbour(pcNode, moves);
			while(nodeDir != -1 && !game.isJunction(nodeDir)) {
				if(game.isPillStillAvailable(nodeDir)) auxNumPills++;
				nodeDir = game.getNeighbour(nodeDir, moves);
			}
			
			if(auxNumPills > nPills) {
				nPills = auxNumPills;
				bestWayNode = nodeDir;
			}
		}
		
		if (bestWayNode != -1) {
			GameView.addLines(game, Color.CYAN, pcNode, bestWayNode);
		}else {
			nodeDir = pcNode;
			int[] neightbour = game.getNeighbouringNodes(nodeDir, game.getPacmanLastMoveMade());
			bestWayNode = neightbour[rnd.nextInt(neightbour.length)];
		}
		
		nodePacManTarget = bestWayNode;
		return bestWayNode;
	}
	
}
