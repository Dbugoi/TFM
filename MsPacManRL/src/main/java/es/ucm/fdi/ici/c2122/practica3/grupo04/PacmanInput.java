package es.ucm.fdi.ici.c2122.practica3.grupo04;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica2.grupo04.MsPacmanInput;
import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class PacmanInput extends RulesInput {
	
	final float dangerMult = 1.65f;
	final float dangerThreshold = 0.7f;
	final int edibleGhostMaxDist = 10 * 5;
	final int ghostGroupMaxDist = 4 * 5;
	
	boolean alreadyDead,
			fleeing,
			ghostEdible,
			canEatGroup,
			groupNearbyClosestPPill;
	int closestPillDist,
		closestPPillDist,
		groupNearbyPPill,
		closestGhostDist;
	
	public PacmanInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		int pacman = game.getPacmanCurrentNodeIndex();
		alreadyDead = shouldBeDead();
		fleeing = CommonMethodsPacman.dangerLevel(game, pacman, dangerMult) >= dangerThreshold;
		ghostEdible = CommonMethodsPacman.getNearestGhost(game, pacman, 9000, true) != null;
		canEatGroup = canGoForGroup();
		groupNearbyClosestPPill = groupNearbyClosestPPill(); 
		closestPillDist = game.getShortestPathDistance(pacman, CommonMethodsPacman.getClosestPill(game, pacman));
		closestPPillDist = game.getShortestPathDistance(pacman, CommonMethodsPacman.getClosestPPill(game, pacman));
		groupNearbyPPill = groupNearbyPPill();
		GHOST g = CommonMethodsPacman.getNearestGhost(game, pacman, 9000, false);
		closestGhostDist = g != null ? game.getShortestPathDistance(pacman, game.getGhostCurrentNodeIndex(g)) : -1;
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(MSPACMAN (alreadyDead %s) (fleeing %s) (ghostEdible %s) (canEatGroup %s) (groupNearbyClosestPPill %s) (closestPillDist %s) (closestPPillDist %s) (groupNearbyPPill %s) (closestGhostDist %s))",
				this.alreadyDead,
				this.fleeing,
				this.ghostEdible,
				this.canEatGroup,
				this.groupNearbyClosestPPill,
				this.closestPillDist,
				this.closestPPillDist,
				this.groupNearbyPPill,
				this.closestGhostDist
				));
		return facts;
	}
	
	public GHOST[] ghostsOrderedByDist() {
		GHOST[] ghostsOrdered = GHOST.values();
		int[] ghostsDist = new int[4];
		int pacman = game.getPacmanCurrentNodeIndex();
		int numGhosts = GHOST.values().length;

		// Calcula las distancias de pacman a los fantasmas
		for (int i = 0; i < numGhosts; ++i) {
			if(game.getGhostLairTime(ghostsOrdered[i])<=0) 
				ghostsDist[i] = game.getShortestPathDistance(pacman, game.getGhostCurrentNodeIndex(ghostsOrdered[i]), game.getPacmanLastMoveMade());
		}

		// Se ordenan en el vector
		for(int j = 0; j < numGhosts; ++j) {
			for(int i = 0; i < numGhosts - 1; ++i) {
				if(ghostsDist[i] > ghostsDist[i + 1]) {
					int d = ghostsDist[i + 1];
					GHOST g = ghostsOrdered[i + 1];
					ghostsDist[i + 1] = ghostsDist[i];
					ghostsOrdered[i + 1] = ghostsOrdered[i];
					ghostsDist[i] = d;
					ghostsOrdered[i] = g;
				}
			}
		}
		
		return ghostsOrdered;
	}
	
	public boolean canGoForGroup() {
		GHOST[] ghostsOrdered = ghostsOrderedByDist();
		int pacman = game.getPacmanCurrentNodeIndex();
		int numGhosts = GHOST.values().length;

		// Se recorren los fantasmas en orden y se mira si alguno está cerca de otro
		for (int i = 0; i < numGhosts - 1; ++i) {
			if(game.getGhostLairTime(ghostsOrdered[i])<=0 && game.isGhostEdible(ghostsOrdered[i]) ) {
				int thisGhost = game.getGhostCurrentNodeIndex(ghostsOrdered[i]);
				if (game.getShortestPathDistance(pacman, thisGhost, 
						game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0])
						> edibleGhostMaxDist) break;
				for (int j = i + 1; j < numGhosts; ++j) {
					int otherGhost = game.getGhostCurrentNodeIndex(ghostsOrdered[j]);
					if (game.getDistance(thisGhost, otherGhost, DM.PATH) <= ghostGroupMaxDist) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean groupNearbyClosestPPill() {
		if(game.getNumberOfActivePowerPills() <= 0) return false;
		int cont = 0;
		int PPill = CommonMethodsPacman.getClosestPPill(game, game.getPacmanCurrentNodeIndex());
			for (GHOST g : GHOST.values()) {
				if (game.getGhostLairTime(g) <= 0 && 
					game.getDistance(PPill, game.getGhostCurrentNodeIndex(g), DM.PATH) <= ghostGroupMaxDist) {
					++cont;
					if (cont > 1) return true;					
				}
			cont = 0;
		}
		return false;
	}
	
	public int groupNearbyPPill() {
		if(game.getNumberOfActivePowerPills() <= 0) return -1;
		int cont = 0;
		for (int PPill : game.getActivePowerPillsIndices()) {
			for (GHOST g : GHOST.values()) {
				if (game.getGhostLairTime(g) <= 0 && 
					game.getDistance(PPill, game.getGhostCurrentNodeIndex(g), DM.PATH) <= ghostGroupMaxDist) {
					++cont;
					if (cont > 1) return PPill;					
				}
			}
			cont = 0;
		}
		return -1;
	}
	
	public boolean shouldBeDead() {
		int pacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(pacman)) {
			int[] junctions = CommonMethodsPacman.getAdjacentJunctions(game, pacman);
			//comprueba si hay una ruta que no nos encierre
			for(int junc : junctions) {
				if(!CommonMethodsPacman.ghostInPathOrJunction(game, pacman, junc, 
						game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]))
					return false;
				
			}
		}
		return true;
	}
}
