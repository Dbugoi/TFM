package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;

public class GhostsInput extends RulesInput {

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	private double minPacmanDistancePPill;

	// private final GHOST ghostPriority[] = { GHOST.BLINKY, GHOST.PINKY,
	// GHOST.INKY, GHOST.SUE};

	// Distancia a la que consideramos que está un pacman lejos de la pildora(PATH)
	private final double farDistance = 2;
	// Array de distancias al Pacman de cada fantasma en orden: BLINKY, PINKI, INKY,
	// SUE
	double[] distancesToPacman = { 0, 0, 0, 0 };// new double[4];
	
	
	int nearestPacmanPPill = -1;
	
	public GhostsInput(Game game) {
		super(game);

	}

	@Override
	public void parseInput() {
		nearestPacmanPPill = -1;
		
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);

		int pacman = game.getPacmanCurrentNodeIndex();

		// Distancia a ppill
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for (int ppill : game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}

		// Distancias a pacman
		int i = 0;
		double[] distances = { 0, 0, 0, 0 };

		for (GHOST ghostType : GHOST.values()) {
			double distance = game.getDistance(pacman, game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
			distances[i] = distance;
			i++;
		}

		distancesToPacman = distances;
	}

	public boolean isBLINKYedible() {
		return BLINKYedible;
	}

	public boolean isINKYedible() {
		return INKYedible;
	}

	public boolean isPINKYedible() {
		return PINKYedible;
	}

	public boolean isSUEedible() {
		return SUEedible;
	}

	public boolean isEveryGhostEdible() {
		return BLINKYedible && INKYedible && INKYedible && SUEedible;
	}

	public boolean isAnyGhostEdible() {
		return BLINKYedible || INKYedible || INKYedible || SUEedible;
	}

	public boolean isAnyGhostNonEdible() {
		return !BLINKYedible || !INKYedible || !INKYedible || !SUEedible;
	}

	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}

	public final double[] distancesToPacman() {
		return distancesToPacman;
	}

	// busca fantasma mas cercano
	public int getGhostClosestToPacman() {
		int closestGhost = 0;
		double minDist = 100;
		for (int i = 0; i < GHOST.values().length; i++) {
			if (distancesToPacman[i] < minDist) {
				minDist = distancesToPacman[i];
				closestGhost = i;
			}
		}
		return closestGhost;
	}

	// Busca fantasma mas lejano
	public int getGhostFurthestFromPacman() {
		int furthestGhost = 0;
		double maxDist = 0;
		for (int i = 0; i < GHOST.values().length; i++) {
			if (distancesToPacman[i] > maxDist) {
				maxDist = distancesToPacman[i];
				furthestGhost = i;
			}
		}
		return furthestGhost;
	}

	// Busca ppil mas cercana
	private int searchNearestPowerPill(Game game, int pacIndex, MOVE pacDir) {

		// Buscamos ppil mas cercana
		int distance = 100;
		int[] activePills = game.getActivePowerPillsIndices();
		int pill = -1;
		for (int i = 0; i < activePills.length; i++) {
			int path = game.getShortestPathDistance(pacIndex, activePills[i], pacDir);
			if (path < distance) {
				distance = path;
				pill = activePills[i];
			}
		}
//		if (pill != -1)
//			GameView.addLines(game, Color.CYAN, game.getPacmanCurrentNodeIndex(), pill);
		return pill;
	}

	public int getGhostClosestPacmanPpill() {
		int Ppill = searchNearestPowerPill(game, game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());

		int furthestGhost = 0;
		int Dist = 0;
		for (int i = 0; i < GHOST.values().length; i++) {
			int auxDist = game.getShortestPathDistance(i, Ppill);

		}
		return furthestGhost;
	}

	public boolean pacmanCloseToPPill(GHOST g) {

		double distance = 100, lastD = 100;
		if (game.isGhostEdible(g))
			return true;
		else {

			int pacmanNode = game.getPacmanCurrentNodeIndex();

			for (int ppill : game.getActivePowerPillsIndices()) {
				distance = Math.min(game.getDistance(game.getPacmanCurrentNodeIndex(), ppill,
						game.getPacmanLastMoveMade(), DM.PATH), distance);
				
				//Nos quedamos con la pildora mas cercana al pacman
				if(distance != lastD) {
					lastD = distance;
					nearestPacmanPPill = ppill;
				}
			}
		}

		return distance < farDistance;
	}

	public boolean pacmanCloseToPPill() {

		double distance = 100, lastD = 100;

		int pacmanNode = game.getPacmanCurrentNodeIndex();

		for (int ppill : game.getActivePowerPillsIndices()) {
			distance = Math.min(game.getDistance(game.getPacmanCurrentNodeIndex(), ppill,
					game.getPacmanLastMoveMade(), DM.PATH), distance);
			//Nos quedamos con la pildora mas cercana al pacman
			if(distance != lastD) {
				lastD = distance;
				nearestPacmanPPill = ppill;
			}
		}
		
		minPacmanDistancePPill = distance;
		
		return distance < farDistance;
	}
	
	public boolean thereIsPPill() {
		return game.getActivePowerPillsIndices().length > 0;
	}
	
	public GHOST getGhostClosestToPPill() {
		
		pacmanCloseToPPill();
	
		
		if(nearestPacmanPPill == -1) 
			return null;
		
		
		double distance = 100, lastD = 100;
		
		GHOST nearestGhost = null;
		
		for (GHOST g: GHOST.values()) {
			distance = Math.min(game.getDistance(game.getGhostCurrentNodeIndex(g), nearestPacmanPPill,
					game.getGhostLastMoveMade(g), DM.PATH), distance);
			
			if(lastD != distance) {
				lastD = distance;
				nearestGhost = g;
			}
		}
		
		if(distance < minPacmanDistancePPill)
			return nearestGhost;
		
		return null;
	}
	
	public boolean canKillPacman(GHOST g) {
		if(game.doesGhostRequireAction(g)) {
			int pacman = game.getPacmanCurrentNodeIndex();
			int preNode = pacman;
			double ghostDistance = 0, pacmanDistance = 0;
			
			MOVE pacmanMove = game.getPacmanLastMoveMade();
			
			while(pacman != -1 && !game.isJunction(pacman)){
				preNode = pacman;
				pacman = game.getNeighbour(pacman, pacmanMove);
				pacmanDistance++;
			}
			
			if(pacman == -1)
				ghostDistance = game.getDistance(preNode, game.getGhostCurrentNodeIndex(g), game.getGhostLastMoveMade(g), DM.PATH);
			else
				ghostDistance = game.getDistance(pacman, game.getGhostCurrentNodeIndex(g), game.getGhostLastMoveMade(g), DM.PATH);
			
			return ghostDistance < pacmanDistance;
		}
		return false;
	}
	
	@Override
	public Collection<String> getFacts() {
		
		GHOST closerToPPill = getGhostClosestToPPill();
		
		Vector<String> facts = new Vector<String>();
		
		facts.add(String.format("(BLINKY (edible %s) (everyoneEdible %s) (closestGhost %s) (thereIsPPill %s) (closerToPill %s) (canKillPacman %s))", 
this.BLINKYedible || this.pacmanCloseToPPill(GHOST.BLINKY), this.isEveryGhostEdible(), this.getGhostClosestToPacman() == GHOST.BLINKY.ordinal(), this.thereIsPPill(), closerToPPill == GHOST.BLINKY, this.canKillPacman(GHOST.BLINKY)));
		facts.add(String.format("(INKY (edible %s) (everyoneEdible %s) (closestGhost %s) (thereIsPPill %s) (closerToPill %s) (canKillPacman %s))", 
this.INKYedible || this.pacmanCloseToPPill(GHOST.INKY), this.isEveryGhostEdible(), this.getGhostClosestToPacman() == GHOST.INKY.ordinal(), this.thereIsPPill(), closerToPPill == GHOST.INKY, this.canKillPacman(GHOST.INKY)));
		facts.add(String.format("(PINKY (edible %s) (everyoneEdible %s) (closestGhost %s) (thereIsPPill %s) (closerToPill %s) (canKillPacman %s))", 
this.PINKYedible || this.pacmanCloseToPPill(GHOST.PINKY), this.isEveryGhostEdible(), this.getGhostClosestToPacman() == GHOST.PINKY.ordinal(), this.thereIsPPill(), closerToPPill == GHOST.PINKY, this.canKillPacman(GHOST.PINKY)));
		facts.add(String.format("(SUE (edible %s) (everyoneEdible %s) (closestGhost %s) (thereIsPPill %s) (closerToPill %s) (canKillPacman %s))", 
this.SUEedible || this.pacmanCloseToPPill(GHOST.SUE), this.isEveryGhostEdible(), this.getGhostClosestToPacman() == GHOST.SUE.ordinal(), this.thereIsPPill(), closerToPPill == GHOST.SUE, this.canKillPacman(GHOST.SUE)));
		
		
		facts.add(String.format("(MSPACMAN (closeToPill %s))", this.pacmanCloseToPPill()));
		
		
		return facts;
	}
}
