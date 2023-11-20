package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica3.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class GhostsInput extends RulesInput {
	// BLINKY -- Persigue a MsPacman
	private int BLINKYdistanceToGhost;
	private int BLINKYdistanceToPacMan;
	private int BLINKYdistanceToIntersection;
	private int BLINKYlairTime;
	private String BLINKYedible;
	private int BLINKYquadrant;
	private String BLINKYcanKill;
	
	// INKY -- Corta el camino de MsPacman
	private int INKYdistanceToGhost;
	private int INKYdistanceToPacMan;
	private int INKYdistanceToIntersection;
	private int INKYlairTime;
	private String INKYedible;
	private int INKYquadrant;
	private String INKYcanKill;

	// PINKY -- Intercepta camino a PPill
	private int PINKYdistanceToGhost;
	private int PINKYdistanceToPacMan;
	private int PINKYdistanceToIntersection;
	private int PINKYlairTime;
	private String PINKYedible;
	private int PINKYquadrant;
	private String PINKYcanKill;

	// SUE -- Dragon en un cuadrante
	private int SUEdistanceToGhost;
	private int SUEdistanceToPacMan;
	private int SUEdistanceToIntersection;
	private int SUElairTime;
	private String SUEedible;
	private int SUEquadrant;
	private int SUEdragonDestiny;
	private String SUEcanKill;
	
	
	private double minPacmanDistancePPill;
	
	private int numPPills;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		// BLINKY
		BLINKYdistanceToGhost = distanceToClosestGhost(GHOST.BLINKY);
		BLINKYdistanceToPacMan = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), 
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.BLINKY));
		BLINKYdistanceToIntersection = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), 
				closestPacManIntersection(GHOST.BLINKY), game.getGhostLastMoveMade(GHOST.BLINKY));	
		BLINKYlairTime = game.getGhostLairTime(GHOST.BLINKY);
		BLINKYedible = game.isGhostEdible(GHOST.BLINKY).toString();
		BLINKYquadrant = GameUtils.getInstance().getQuadrantFromNode(game, game.getGhostCurrentNodeIndex(GHOST.BLINKY)).ordinal();
		BLINKYcanKill = canKillPacMan(GHOST.BLINKY).toString();
		
		//INKY
		INKYdistanceToGhost = distanceToClosestGhost(GHOST.INKY);
		INKYdistanceToPacMan = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), 
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.INKY));
		INKYdistanceToIntersection = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), 
				closestPacManIntersection(GHOST.INKY), game.getGhostLastMoveMade(GHOST.INKY));		
		INKYlairTime = game.getGhostLairTime(GHOST.INKY);
		INKYedible = game.isGhostEdible(GHOST.INKY).toString();
		INKYquadrant = GameUtils.getInstance().getQuadrantFromNode(game, game.getGhostCurrentNodeIndex(GHOST.INKY)).ordinal();
		INKYcanKill = canKillPacMan(GHOST.INKY).toString();
		
		//PINKY
		PINKYdistanceToGhost = distanceToClosestGhost(GHOST.PINKY);
		PINKYdistanceToPacMan = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), 
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.PINKY));
		PINKYdistanceToIntersection = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), 
				closestPacManIntersection(GHOST.PINKY), game.getGhostLastMoveMade(GHOST.PINKY));
		PINKYlairTime = game.getGhostLairTime(GHOST.PINKY);
		PINKYedible = game.isGhostEdible(GHOST.PINKY).toString();
		PINKYquadrant = GameUtils.getInstance().getQuadrantFromNode(game, game.getGhostCurrentNodeIndex(GHOST.PINKY)).ordinal();
		PINKYcanKill = canKillPacMan(GHOST.PINKY).toString();
		
		//SUE
		SUEdistanceToGhost = distanceToClosestGhost(GHOST.SUE);
		SUEdistanceToPacMan = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), 
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.SUE));
		SUEdistanceToIntersection = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), 
				closestPacManIntersection(GHOST.SUE), game.getGhostLastMoveMade(GHOST.SUE));	
		SUElairTime = game.getGhostLairTime(GHOST.SUE);
		SUEedible = game.isGhostEdible(GHOST.SUE).toString();
		SUEquadrant = GameUtils.getInstance().getQuadrantFromNode(game, game.getGhostCurrentNodeIndex(GHOST.SUE)).ordinal();
		SUEdragonDestiny = getDestinyQuadrant();
		minPacmanDistancePPill = distanceClosestPPill(game.getPacmanCurrentNodeIndex());
		numPPills = game.getNumberOfPowerPills();
		SUEcanKill = canKillPacMan(GHOST.SUE).toString();
	}


	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		// BLINKY
		facts.add(String.format("(BLINKY (distanceToGhost %d) "
				+ "(distanceToPacMan %d) "
				+ "(distanceToPacManIntersection %d) "
				+ "(lairTime %d) "
				+ "(edible %s) "
				+ "(quadrant %s)"
				+ "(canKill %s))",
				BLINKYdistanceToGhost, BLINKYdistanceToPacMan, BLINKYdistanceToIntersection, 
				BLINKYlairTime, BLINKYedible, BLINKYquadrant, BLINKYcanKill));
		
		// INKY
		facts.add(String.format("(INKY (distanceToGhost %d) "
				+ "(distanceToPacMan %d) "
				+ "(distanceToPacManIntersection %d) "
				+ "(lairTime %d) "
				+ "(edible %s) "
				+ "(quadrant %s)"
				+ "(canKill %s))",
				INKYdistanceToGhost, INKYdistanceToPacMan, INKYdistanceToIntersection, 
				INKYlairTime, INKYedible, INKYquadrant, INKYcanKill));
		
		// PINKY
		facts.add(String.format("(PINKY (distanceToGhost %d) "
				+ "(distanceToPacMan %d) "
				+ "(distanceToPacManIntersection %d) "
				+ "(lairTime %d) "
				+ "(edible %s) "
				+ "(quadrant %s)"
				+ "(canKill %s))",
				PINKYdistanceToGhost, PINKYdistanceToPacMan, PINKYdistanceToIntersection, 
				PINKYlairTime, PINKYedible, PINKYquadrant, PINKYcanKill));
		
		// SUE
		facts.add(String.format("(SUE (distanceToGhost %d) "
				+ "(distanceToPacMan %d) "
				+ "(distanceToPacManIntersection %d) "
				+ "(lairTime %d) "
				+ "(edible %s) "
				+ "(quadrant %s) "
				+ "(destinyQuadrant %s)"
				+ "(canKill %s))",
				SUEdistanceToGhost, SUEdistanceToPacMan, SUEdistanceToIntersection, 
				SUElairTime, SUEedible, SUEquadrant, SUEdragonDestiny, SUEcanKill));
		
		facts.add(String.format("(MSPACMAN (distanceToPPill %d))", 
								(int)minPacmanDistancePPill));
		
		facts.add(String.format("(GAMEINFO (numPPills %d))", 
				numPPills));
		
		return facts;
	}

	private GHOST nearestGhost(GHOST yourself) {
        GHOST nGhost = null; int minDist = Integer.MAX_VALUE;
        for(Constants.GHOST g: Constants.GHOST.values()) {
        	if(g == yourself || game.getGhostLairTime(g) > 0) continue;
            int ghost = game.getGhostCurrentNodeIndex(g);
            int mspacman = game.getPacmanCurrentNodeIndex();
            int dist = game.getShortestPathDistance(mspacman, ghost);
            if(dist < minDist){
                minDist = dist;
                nGhost = g;
            }
        }
        return nGhost;
    }
	
	private GHOST nearestLivingGhost(GHOST yourself) {
        GHOST nGhost = null; int minDist = Integer.MAX_VALUE;
        for(Constants.GHOST g: Constants.GHOST.values()) {
        	if(g == yourself || game.isGhostEdible(g) || game.getGhostLairTime(g) > 0) continue;
            int ghost = game.getGhostCurrentNodeIndex(g);
            int mspacman = game.getPacmanCurrentNodeIndex();
            int dist = game.getShortestPathDistance(mspacman, ghost);
            if(dist < minDist){
                minDist = dist;
                nGhost = g;
            }
        }
        return nGhost;
    }
	
	private GHOST nearestEdibleGhost(GHOST yourself) {
        GHOST nGhost = null; int minDist = Integer.MAX_VALUE;
        for(Constants.GHOST g: Constants.GHOST.values()) {
        	if(g == yourself || !game.isGhostEdible(g) || game.getGhostLairTime(g) > 0) continue;
            int ghost = game.getGhostCurrentNodeIndex(g);
            int mspacman = game.getPacmanCurrentNodeIndex();
            int dist = game.getShortestPathDistance(mspacman, ghost);
            if(dist < minDist){
                minDist = dist;
                nGhost = g;
            }
        }
        return nGhost;
    }
	
	private int closestPacManIntersection(GHOST yourself) {
		int closestDist = Integer.MAX_VALUE; int closestJunc = -1; 
		for(int junc : game.getJunctionIndices()) {
			if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), junc, game.getPacmanLastMoveMade()) < closestDist) {
				closestDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), junc);
				closestJunc = junc;
			}
		}
		return closestJunc;
	}
	
	private int distanceToClosestGhost(GHOST me8D) {
		GHOST nearG = nearestGhost(me8D);
		if(nearG != null) return game.getShortestPathDistance(game.getGhostCurrentNodeIndex(me8D),
				game.getGhostCurrentNodeIndex(nearG), game.getGhostLastMoveMade(me8D));
		else return Integer.MAX_VALUE;
	}
	
	private int distanceToClosestLivingGhost(GHOST me8D) {
		GHOST nearG = nearestLivingGhost(me8D);
		if(nearG != null) return game.getShortestPathDistance(game.getGhostCurrentNodeIndex(me8D),
				game.getGhostCurrentNodeIndex(nearG), game.getGhostLastMoveMade(me8D));
		else return Integer.MAX_VALUE;
	}
	
	private int distanceToClosestEdibleGhost(GHOST me8D) {
		GHOST nearG = nearestEdibleGhost(me8D);
		if(nearG != null) return game.getShortestPathDistance(game.getGhostCurrentNodeIndex(me8D),
				game.getGhostCurrentNodeIndex(nearG), game.getGhostLastMoveMade(me8D));
		else return Integer.MAX_VALUE;
	}
	
	private int distanceClosestPPill(int node) {
		int dist = Integer.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			int distance = (int)game.getDistance(node, ppill, DM.PATH);
			dist = Math.min(distance, dist);
		}
		return dist;
	}
	
	// DRAGON DESTINY QUADRANT
	private int getDestinyQuadrant() {
		GameUtils.Quadrant[] qs = GameUtils.Quadrant.values();
		int ind = 0;
		int maxPills = GameUtils.getInstance().getPillsNumberInQuadrant(game, qs[ind]);
		int nextQuad = 0;
		for(int i = 1; i < qs.length; i++) {
			nextQuad = GameUtils.getInstance().getPillsNumberInQuadrant(game, qs[ind]);
			if(maxPills <= nextQuad){
				maxPills = nextQuad;
				ind = i;
			}
		}
		return qs[ind].ordinal();
	}
	
	/**
	 * Metodo para saber si un fantasma puede 
	 * encerrar a pacman y matarlo con su siguiente movimiento
	 */
	private Boolean canKillPacMan(GHOST ghost) {
		int ghostIndx = game.getGhostCurrentNodeIndex(ghost);
		MOVE[] moves = game.getPossibleMoves(ghostIndx, game.getGhostLastMoveMade(ghost));
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		int nextNode;		
		boolean pacManBetween = false;
		int[] proposedPath;
		int i = 0, j;
		while(i < moves.length && !pacManBetween) {
			nextNode = game.getNeighbour(ghostIndx, moves[i]);
			proposedPath = game.getShortestPath(nextNode, mspacman, moves[i]);
			j = 0;
			while(j < proposedPath.length && !pacManBetween && !game.isJunction(proposedPath[j])) {
				pacManBetween = mspacman == proposedPath[j];
				j++;
			}
			i++;		
		}
		
		return pacManBetween;
	}
}
