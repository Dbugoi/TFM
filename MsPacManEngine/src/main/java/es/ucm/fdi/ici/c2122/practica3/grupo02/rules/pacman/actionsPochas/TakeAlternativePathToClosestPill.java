package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actionsPochas;

import es.ucm.fdi.ici.Action;

import es.ucm.fdi.ici.c2122.practica2.grupo02.Tools;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class TakeAlternativePathToClosestPill implements Action {
	
	public TakeAlternativePathToClosestPill() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Take Alternative Path To ClosestPill";
	}

	@Override
	public MOVE execute(Game game) {
		int targetNode = Tools.nodeTarget;//meh
		int pcNode = game.getPacmanCurrentNodeIndex();
		MOVE lastMovePM = game.getPacmanLastMoveMade();
		MOVE moveBlocked = game.getApproximateNextMoveTowardsTarget(pcNode, targetNode,lastMovePM , DM.PATH);
		int oldDistance = game.getShortestPathDistance(pcNode, targetNode, moveBlocked);
		
		//If exists other directions in actual Node, we check if taking those directions we can reach the nodeTarget
		int[] possibleDirs = game.getNeighbouringNodes(pcNode, moveBlocked);
		//We start as the moveBlocked
		MOVE differentMove = moveBlocked;
		
		int shortestDistance = Integer.MAX_VALUE;
		int newActualNode = pcNode;
		if(possibleDirs != null && possibleDirs.length > 1) {
			for(int i = 0; i < possibleDirs.length; i++) {
				//Check different paths distance from surroundings nodes
				MOVE changeMove = game.getMoveToMakeToReachDirectNeighbour(pcNode, possibleDirs[i]);
				int distance = game.getShortestPathDistance(possibleDirs[i], targetNode, changeMove);
				
				if(distance < shortestDistance) {
					shortestDistance = distance;
					//Change the move to the newest
					differentMove = changeMove;
					newActualNode = possibleDirs[i];
				}
			} 
		}

		if(shortestDistance > oldDistance * 1.7) {
			return game.getApproximateNextMoveAwayFromTarget(pcNode, game.getGhostCurrentNodeIndex(Tools.nearestGhost(game)), differentMove, DM.PATH);
		}
		
		return game.getApproximateNextMoveTowardsTarget(newActualNode, targetNode, differentMove, DM.PATH);
	}
}
