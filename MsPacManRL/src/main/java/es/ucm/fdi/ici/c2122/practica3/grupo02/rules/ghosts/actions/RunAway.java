package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo02.GameConstants;
import es.ucm.fdi.ici.rules.RulesAction;

import jess.Fact;
import jess.Value;
import jess.JessException;

import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;

public class RunAway implements RulesAction {

	enum RUN_AWAY_TYPE { NONE, DIRECTLY, TOGHOST, ALTERNATIVE };

	GHOST ghostType;
	RUN_AWAY_TYPE runAwayType;

	public RunAway(GHOST ghostType) {
		super();
		this.ghostType = ghostType;
		this.runAwayType = RUN_AWAY_TYPE.NONE;
	}

	@Override
	public String getActionId() {
		return ghostType + "RunsAway";
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghostType)) {
			int pacmanNode = game.getPacmanCurrentNodeIndex();
			int ghostNode = game.getGhostCurrentNodeIndex(ghostType);
			MOVE lastMove = game.getGhostLastMoveMade(ghostType);

			if (GameConstants.DEBUG)
				GameView.addPoints(game, GameConstants.colours[ghostType.ordinal()],
						game.getShortestPath(ghostNode, pacmanNode));
			
			MOVE runAwayMove;
			switch(runAwayType) {
			case DIRECTLY:
				runAwayMove = runAwayDirectly(game, ghostNode, pacmanNode, lastMove);
				break;
			case TOGHOST:
				runAwayMove = runAwayToGhost(game, ghostNode, pacmanNode, lastMove);
				break;
			case ALTERNATIVE:
				runAwayMove = runAwayAlternatively(game, ghostNode, pacmanNode, lastMove);
				break;
			case NONE:
			default:
				runAwayMove = MOVE.NEUTRAL;
				break;
			}
			
			return runAwayMove;
		}

		return MOVE.NEUTRAL;
	}
	
	private MOVE runAwayDirectly(Game game, int ghostNode, int pacmanNode, MOVE lastMove) {
		return game.getApproximateNextMoveAwayFromTarget(ghostNode, pacmanNode, lastMove, DM.PATH);
	}
	
	private MOVE runAwayToGhost(Game game, int ghostNode, int pacmanNode, MOVE lastMove) {
		int closestGhostNode = Integer.MIN_VALUE, closestDistance = Integer.MAX_VALUE;

		for (GHOST otherGhost : GHOST.values()) {
			int otherGhostNode = game.getGhostCurrentNodeIndex(otherGhost);
			
			if (otherGhostNode != ghostNode && game.getGhostLairTime(otherGhost) == 0
					&& !game.isGhostEdible(otherGhost)) {
				int distanceToOtherGhost = game.getShortestPathDistance(ghostNode, otherGhostNode);
				
				if (distanceToOtherGhost < closestDistance) {
					closestDistance = distanceToOtherGhost;
					closestGhostNode = otherGhostNode;
				}
			}
		}

		if (closestGhostNode != Integer.MIN_VALUE) {
			if (GameConstants.DEBUG)
				GameView.addPoints(game, GameConstants.colours[ghostType.ordinal()], 
						game.getShortestPath(ghostNode, closestGhostNode));
			
			return game.getApproximateNextMoveTowardsTarget(ghostNode, closestGhostNode, lastMove, DM.PATH);
		}
		else
			return game.getApproximateNextMoveAwayFromTarget(ghostNode, pacmanNode, lastMove, DM.PATH);
	}
	
	private MOVE runAwayAlternatively(Game game, int ghostNode, int pacmanNode, MOVE lastMove) {
		int[] neighbouringNodes;
		neighbouringNodes = game.getNeighbouringNodes(ghostNode, lastMove);
		
		int lastNode = ghostNode;
		boolean intersectionFound = false;
		boolean[] ghostInTheWay = {false, false, false};
		int[] destinationNodes = {0,0,0};
		int contador = 0;
		while(!intersectionFound) {
			contador = 0;
			if (neighbouringNodes != null) {
				for (int node : neighbouringNodes) {
					MOVE lastMoveMade = MOVE.NEUTRAL;					
					if (game.getNodeYCood(node) - game.getNodeYCood(lastNode) == 1 || game.getNodeYCood(node) - game.getNodeYCood(lastNode) < -10)
						lastMoveMade = MOVE.DOWN;
					else if (game.getNodeYCood(node) - game.getNodeYCood(lastNode) == -1 || game.getNodeYCood(node) - game.getNodeYCood(lastNode) > 10)
						lastMoveMade = MOVE.UP;
					else if(game.getNodeXCood(node) - game.getNodeXCood(lastNode) == 1 || game.getNodeXCood(node) - game.getNodeXCood(lastNode) < -10)
						lastMoveMade = MOVE.RIGHT;
					else if (game.getNodeXCood(node) - game.getNodeXCood(lastNode) == -1 || game.getNodeXCood(node) - game.getNodeXCood(lastNode) > 10)
						lastMoveMade = MOVE.LEFT;
					
					for(GHOST ghost : GHOST.values()) {
						if (game.getGhostCurrentNodeIndex(ghost) == node)
							ghostInTheWay[contador] = true;
					}
					
					if (game.isJunction(node)) {
						intersectionFound = true;
						destinationNodes[contador] = node;
					}else {
						//We move the node to the next position
						lastNode = node;
						if (game.getNeighbouringNodes(node, lastMoveMade) != null)
							neighbouringNodes[contador] = game.getNeighbouringNodes(node, lastMoveMade)[0];
						else
							return MOVE.NEUTRAL;
					}
					contador++;
				}
			}
		}
		
		int destination = 0;
		int numberOfPossiblePaths = 0;
		contador = 0;
		for (boolean possiblePath : ghostInTheWay) {
			if (possiblePath) {
				numberOfPossiblePaths++;
				if (numberOfPossiblePaths == 1)
					destination = destinationNodes[contador];
				else {
					if (numberOfPossiblePaths > 1) {
						if (game.getShortestPathDistance(destinationNodes[contador], pacmanNode) > game.getShortestPathDistance(destination, pacmanNode)) {
							destination = destinationNodes[contador];
						}
					}
				}
			}
			contador++;
		}
		return game.getApproximateNextMoveTowardsTarget(ghostNode, destination, game.getGhostLastMoveMade(ghostType), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("runAwayType");
			if (value == null)
				return;
			
			Integer runAway = value.intValue(null);
			runAwayType = RUN_AWAY_TYPE.values()[runAway];
		} 
		catch (JessException e) {
			e.printStackTrace();
		}
	}
}
