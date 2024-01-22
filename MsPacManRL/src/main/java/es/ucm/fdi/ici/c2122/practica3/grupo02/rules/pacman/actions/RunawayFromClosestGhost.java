package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actions;


import es.ucm.fdi.ici.c2122.practica2.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica2.grupo02.Tools;
import es.ucm.fdi.ici.c2122.practica3.grupo02.Utils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class RunawayFromClosestGhost implements RulesAction {
	
	int runawayDistance = 50;
	public RunawayFromClosestGhost() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "RunawayFromClosestGhost";
	}

	@Override
	public MOVE execute(Game game) {
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		int indexGhost = -1;
		GHOST nearestGhostType = null;
		
		//If exists a ghost blocking the path, we got its index
		if(Utils.ghostNodeToFleeFrom != -1) {
			indexGhost = Utils.ghostNodeToFleeFrom;
			nearestGhostType = Utils.ghostFleeFromType;
 		}
		//if theres a ghost near, we take its type
		else nearestGhostType = Tools.nearestGhostInRange(game, runawayDistance);
		
		//if there isn't ghost blocking but theres is some near, we take its index
		if(indexGhost == -1 && nearestGhostType != null) {
			indexGhost = game.getGhostCurrentNodeIndex(nearestGhostType);
			Utils.ghostFleeFromType = nearestGhostType;
		}
		
		//in case there are not ghost blocking nor near, we runaway randomly.
		if (indexGhost != -1) {
			if(GameConstants.DEBUG) {
				GameView.addPoints(game, java.awt.Color.magenta, game.getShortestPath(indexGhost, pacmanNode));	
			}
			
			MOVE moveAway = game.getApproximateNextMoveAwayFromTarget(pacmanNode,
					indexGhost, game.getPacmanLastMoveMade(), DM.PATH);
			
			Utils.nodePacManTarget = game.getNeighbour(pacmanNode, moveAway);
			return moveAway;
		}
		
		MOVE[] possibleMoves = game.getPossibleMoves(pacmanNode, game.getPacmanLastMoveMade());
		return possibleMoves[Tools.rnd.nextInt(possibleMoves.length)];
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
