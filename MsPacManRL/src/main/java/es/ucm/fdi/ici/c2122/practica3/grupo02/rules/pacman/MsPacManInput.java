package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman;

import java.util.Collection;
import java.util.Random;
import java.util.Vector;


import es.ucm.fdi.ici.c2122.practica3.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica3.grupo02.Utils;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacManInput extends RulesInput {
	
	private boolean canEatGhost;
	
	private boolean ghostOutsideLair;

	private int nearestGhostDistance;
	
	private boolean caminoBloqueado;
	
	private boolean pPillNear;
	
	private Random rnd = new Random();
	
	public MsPacManInput(Game game) {
		super(game);	
	}

	@Override
	public void parseInput() {
		
		caminoBloqueado = PathMayBeObstructed();
		
		canEatGhost = game.isGhostEdible(GHOST.BLINKY) || game.isGhostEdible(GHOST.PINKY)
				|| game.isGhostEdible(GHOST.INKY) || game.isGhostEdible(GHOST.SUE);
				
//		
		pPillNear = pPillsInRange(4*10);
		
		ghostOutsideLair = !(game.getGhostLairTime(GHOST.BLINKY) > 0) || !(game.getGhostLairTime(GHOST.PINKY) > 0)
				|| !(game.getGhostLairTime(GHOST.INKY) > 0) || !(game.getGhostLairTime(GHOST.SUE) > 0);
//		
//		GHOST g = Tools.nearestGhost(game); 
//		
//		if(g != null) {
//			nearestGhostDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), 
//					game.getGhostCurrentNodeIndex(g), game.getPacmanLastMoveMade());
//		}
		
	}

	public boolean ExistingBottomPills() {
		int[] activePills = game.getActivePillsIndices();
		for (int activePill : activePills)
			if(game.getNodeYCood(activePill) <= game.getNodeYCood(game.getPacManInitialNodeIndex()))
				return true;
		
		return false;
	}
	
	public boolean pillsInRange() {
		int pcNode = game.getPacmanCurrentNodeIndex();
		MOVE[] possibleMoves = game.getPossibleMoves(pcNode);
		for(MOVE moves : possibleMoves){
			int nodeDir = game.getNeighbour(pcNode, moves);
			while(nodeDir != -1 && !game.isJunction(nodeDir)) {
				if(game.isPillStillAvailable(nodeDir)) return true;
				nodeDir = game.getNeighbour(nodeDir, moves);
			}	
		}
		return false;
	}
	
	public boolean pPillsInRange(int range) {
		int pcNode = game.getPacmanCurrentNodeIndex();
		int[] activeP_Pills = game.getActivePowerPillsIndices();
		for (int activePill : activeP_Pills) {
			int distance = game.getShortestPathDistance(pcNode, activePill, game.getPacmanLastMoveMade());
			if(distance <= range) {
				return true;
			}
		}
		
		return false;
	}

	
	public boolean MoreThanOnePillsInRange() {
		int pcNode = game.getPacmanCurrentNodeIndex();
		MOVE[] possibleMoves = game.getPossibleMoves(pcNode);
		int minPills = 1;
		for(MOVE moves : possibleMoves){
			int nPills = 0;
			int nodeDir = game.getNeighbour(pcNode, moves);
			while(nodeDir != -1 && !game.isJunction(nodeDir)) {
				if(game.isPillStillAvailable(nodeDir)) {
					nPills++;
					if(nPills > minPills) return true;
				}
				nodeDir = game.getNeighbour(nodeDir, moves);
			}	
		}
		return false;
	}
	
	public boolean PathMayBeObstructed() {
		int pcNode = game.getPacmanCurrentNodeIndex();
		int nodeTarget = Utils.nodePacManTarget;
		if(nodeTarget != -1) {
			//We get all the nodes that take MsPacMan to the pill
			//This array doesn't include the currentNode pcNode, but it does include the targetNode.
			int[] path = game.getShortestPath(pcNode, nodeTarget, game.getPacmanLastMoveMade());
			
//			GameView.addPoints(game, GameConstants.colours[3], path);
			//For each Ghost we check if blocks up the MsPacMan path or may block it up.
			
			//Min distance to predict if the nearest Ghost can block up MsPacMan's path 
			int obstructionRange = 6 * 10;
			for(int i = 0; i < path.length; i++) {
				//We make a deep search iterate over the adjacent  paths in a range of obstructionRange
				int i_node = 0;
				boolean blocked = deepSearchPathBlocked(i_node,obstructionRange, i, path[i], path, game.getPacmanLastMoveMade());
				if(blocked) 
					return true;
			}
		}
		Utils.ghostNodeToFleeFrom = -1;
		Utils.ghostFleeFromType = null;
		return false;
	}
	
	private boolean deepSearchPathBlocked(int i_node_left,int obstructionRange, int i_rootNodePath, int rootNode, int[] mainPath, MOVE lastMoveMade) {
		int assumedPM_NewPos, assumedPM_LastPos = rootNode;
		int[] adjacentPathsNode = game.getNeighbouringNodes(assumedPM_LastPos, lastMoveMade);
		MOVE assumedPM_Move;
		int i_nodeIterator;
		if(adjacentPathsNode == null)
			return false;
		
		for(int n : adjacentPathsNode) {
			//if it's any node other than the pathNodes or the last node of the path, we search
			if(n == mainPath[mainPath.length - 1 ] || n != mainPath[(i_rootNodePath < mainPath.length - 1) ?  i_rootNodePath + 1 : i_rootNodePath]) {	
				//Deep Search
				assumedPM_NewPos = n;
				assumedPM_LastPos = rootNode;
				i_nodeIterator = i_node_left;
				while( i_nodeIterator < obstructionRange) {
					for(GHOST ghostType: GHOST.values()) {
						if(game.getGhostLairTime(ghostType) <= 0) {
							int ghostIndex = game.getGhostCurrentNodeIndex(ghostType);
							if(rootNode == ghostIndex) { //exists a ghost in current path
								Utils.ghostNodeToFleeFrom = ghostIndex;
								Utils.ghostFleeFromType = ghostType;
								return true;
							}
							int distance = game.getShortestPathDistance(ghostIndex, mainPath[i_rootNodePath], game.getGhostLastMoveMade(ghostType));;
							int maxSurroundingsDistance = game.getShortestPathDistance( rootNode,mainPath[i_rootNodePath]);
							if(distance < maxSurroundingsDistance + obstructionRange) {
								Utils.ghostNodeToFleeFrom = ghostIndex;
								Utils.ghostFleeFromType = ghostType;
								return true;
							}
						}
					}	
					if(GameConstants.DEBUG)
						GameView.addPoints(game, java.awt.Color.lightGray, assumedPM_NewPos);
					
					assumedPM_Move = game.getMoveToMakeToReachDirectNeighbour(assumedPM_LastPos, assumedPM_NewPos);
					i_nodeIterator++;
					if(assumedPM_Move!= null) {
						if(!game.isJunction(assumedPM_NewPos)) {
							assumedPM_LastPos = assumedPM_NewPos;
							//We advance on the assumed path of the adjacent paths of the main path
							//It can never be 0 since there are not Dead Ends
							assumedPM_NewPos = game.getNeighbouringNodes(assumedPM_NewPos, assumedPM_Move)[0];
						}
						else {
							boolean blocked = deepSearchPathBlocked(i_nodeIterator, obstructionRange, i_rootNodePath ,assumedPM_NewPos, mainPath,assumedPM_Move);
							if(blocked) return true;
							else  break;
						}
					}else break;
				}
			}
		}
		return false;
	}


	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(MSPACMAN (caminoBloqueado %s) (puedeComer %s) (tiempoComerFantasma %s) (pPillMasCercana %s))", 
							  this.caminoBloqueado, this.canEatGhost, Utils.ghostFleeFromType != null ? game.getGhostEdibleTime(Utils.ghostFleeFromType) : 0 , this.pPillNear));
		facts.add(String.format("(CONSTANTS (tiempoMinimoComerFantasma %s))", GameConstants.maxEdibleTime * 0.2));
//		facts.add(String.format("(INKY (edible %s))", this.INKYedible));
//		facts.add(String.format("(PINKY (edible %s))", this.PINKYedible));
//		facts.add(String.format("(SUE (edible %s))", this.SUEedible));
//		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", 
//								(int)this.minPacmanDistancePPill));
		return facts;
	}
}

