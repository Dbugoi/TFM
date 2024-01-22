package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class MsPacManInput extends RulesInput {
	// TODO Calibrar los thresholds de las transitions

	private final int safeTime = 15;
	
	private final double thresold = 30, threshGhost = 150;
	
	private int PowerPillsRemaining;

	private double minDistPacmanPPill;
	private double minDistPacmanPill;

	private double minDistPacmanEdibleGhost;
	private double minDistGhostPacman;

	private boolean PPillEaten;

	private boolean ghostBlockingPPill;
	private boolean ghostBlockingPill;
	private boolean ghostBlockingEGhost;

	public MsPacManInput(Game game) {
		super(game);

	}

	@Override
	public void parseInput() {
		minDistPacmanPPill = 100000;
		this.PowerPillsRemaining = game.getNumberOfActivePowerPills();
		this.PPillEaten = searchEdibleGhost(game);

		int pacman = game.getPacmanCurrentNodeIndex();

		if (pacman != -1) {
			// Comprobacion de cercanía a ppills
			if (this.PowerPillsRemaining > 0) {
				int nearestPPill = checkPills(pacman, true);
				if (nearestPPill != -1) {
					int[] indices = game.getShortestPath(pacman, nearestPPill);
					this.ghostBlockingPPill = checkGhostsBlockingPath(pacman, indices);
				}
			}
			// Comprobacion de cercanía a pills
			if (game.getNumberOfActivePills() > 0) {
				int nearestPill = checkPills(pacman, false);
				if (nearestPill != -1) {
					int[] indices = game.getShortestPath(pacman, nearestPill);
					this.ghostBlockingPill = checkGhostsThreateningPath(pacman, indices);
				}
			}
			// Comprobacion de cercanía a edible ghost
			int nearestEghost = checkGhosts(pacman);
			if (nearestEghost != -1) {
				int[] indices = game.getShortestPath(pacman, nearestEghost);
				this.ghostBlockingEGhost = checkGhostsBlockingPath(pacman, indices);
			}
		}
	}

	// Comprueba que todos los fantasmas son comestibles
	private boolean searchEdibleGhost(Game game) {
		boolean thereIsEdible = false;
		int cont = 0;
		for (GHOST ghostType : GHOST.values()) {
			if (game.isGhostEdible(ghostType))
				cont++;
		}
		if (cont > 3)
			return true;
		return false;
	}

	// Comprueba fantasmas cercanos
	private int checkGhosts(int pacman) {
		int nearestEghost = -1;
		minDistGhostPacman = 1000;
		minDistPacmanEdibleGhost = 1000;
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) <= 0) {
				if (game.isGhostEdible(ghostType) && game.getGhostEdibleTime(ghostType) > safeTime) {
					double distance = game.getShortestPathDistance(pacman, game.getGhostCurrentNodeIndex(ghostType),
							game.getPacmanLastMoveMade());
					// double distance = game.getDistance(pacman,
					// game.getGhostCurrentNodeIndex(ghostType), DM.PATH);

					if (distance < this.minDistPacmanEdibleGhost) {
						this.minDistPacmanEdibleGhost = distance;
						nearestEghost = game.getGhostCurrentNodeIndex(ghostType);
					}
					// this.minDistPacmanEdibleGhost = Math.min(distance,
					// this.minDistPacmanEdibleGhost);
				} else {
					double distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pacman,
							game.getGhostLastMoveMade(ghostType));
					this.minDistGhostPacman = Math.min(distance, this.minDistGhostPacman);
				}
			}
		}
		return nearestEghost;
	}

	// Comprueba PPill más cercana
	private int checkPills(int pacman, boolean power) {
		int nearestPPill = -1;
		int[] pillIndices;
		if (power)
			pillIndices = game.getActivePowerPillsIndices();
		else
			pillIndices = game.getActivePillsIndices();

		for (int ppill : pillIndices) {
			double distance = game.getShortestPathDistance(pacman, ppill, game.getPacmanLastMoveMade());
			if (power) {
				// Tener en cuenta si la mindist inicial no es menor
				if (distance < this.minDistPacmanPPill) {
					this.minDistPacmanPPill = distance;
					nearestPPill = ppill;
				}
			} else {
				if (distance < this.minDistPacmanPill) {
					this.minDistPacmanPill = distance;
					nearestPPill = ppill;
				}
			}

			// this.minDistPacmanPPill = Math.min(distance, this.minDistPacmanPPill);
		}
		return nearestPPill;
	}

	// Comprueba si un fantasma bloquea un camino
	private boolean checkGhostsBlockingPath(int pacman, int[] indices) {
		boolean found = false;
		int i = 0;
		while (!found && i < indices.length) {

			for (GHOST ghostType : GHOST.values()) {
				if (game.getGhostCurrentNodeIndex(ghostType) == indices[i] && !game.isGhostEdible(ghostType))
					found = true;
			}

			// Busca el fantasma mas cercano desde la junction y comprueba que no esté más
			// cerca que el pacman
			if (!found && game.isJunction(indices[i]))
				found = checkJunctGhostPacMan(indices[i], pacman);

			i++;
		}
		return found;
	}

	// Comprueba si un fantasma bloquea o amenaza la continuacion de un camino
	private boolean checkGhostsThreateningPath(int pacman, int[] indices) {
		boolean found = false;
		int i = 0;
		while (!found && i < indices.length) {

			for (GHOST ghostType : GHOST.values()) {
				if (game.getGhostCurrentNodeIndex(ghostType) == indices[i] && !game.isGhostEdible(ghostType))
					found = true;
			}

			// Busca el fantasma mas cercano desde la junction y comprueba que no esté más
			// cerca que el pacman
			if (!found && game.isJunction(indices[i]))
				found = checkJunctGhostPacMan(indices[i], pacman);

			i++;
		}
		if (!found && indices.length >= 2) {
			int ant = indices[i - 1];
			if (!game.isJunction(ant)) {
				int nodeToCheck = -1;
				for (int node : game.getNeighbouringNodes(ant)) {
					if (node != indices[i - 2]) {
						nodeToCheck = node;
					}
				}

				while (!game.isJunction(nodeToCheck)) {
					for (GHOST ghostType : GHOST.values()) {
						if (game.getGhostCurrentNodeIndex(ghostType) == nodeToCheck)
							found = true;
					}
					for (int node : game.getNeighbouringNodes(nodeToCheck)) {
						if (node != ant) {
							ant = nodeToCheck;
							nodeToCheck = node;
						}
					}
				}

				if (!found)
					found = checkJunctGhostPacMan(nodeToCheck, pacman);

			}
		}

		return found;
	}

	// Comprueba si hay un fantasma mas cercano a la junction que mspacman
	private boolean checkJunctGhostPacMan(int index, int pacman) {
		double pacDistFromJunc = game.getDistance(index, pacman, DM.PATH);
		double distFromJunc = pacDistFromJunc + 1;
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) <= 0 && !game.isGhostEdible(ghostType)) {
				double distance = game.getDistance(index, game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				distFromJunc = Math.min(distance, distFromJunc);
			}
		}
		if (distFromJunc < pacDistFromJunc + 1)
			return true;
		return false;
	}
	
	public boolean nearPowerPill() {
		
		return (PPillsRemaining() > 0 && minDistanceFromPacmanToPPill() <= thresold
				&& minDistanceFromGhostToPacman() < threshGhost);
	}
	
	public boolean closeToNonEdibleGhost() {
		int radio = 150;
		return minDistanceFromGhostToPacman() <= radio;
	}
	
	public boolean closeToEdibleGhost() {
		int radio = 150;
		return (!isGhostBlockingPathToEGhost() && minDistanceFromPacmanToEGhost() <= radio);
	}

	public int PPillsRemaining() {
		return PowerPillsRemaining;
	}

	public double minDistanceFromPacmanToPPill() {
		return minDistPacmanPPill;
	}

	public double minDistanceFromPacmanToEGhost() {
		return minDistPacmanEdibleGhost;
	}

	public double minDistanceFromGhostToPacman() {
		return minDistGhostPacman;
	}

	public boolean wasPPillEaten() {
		return PPillEaten;
	}

	public boolean isGhostBlockingPathToPPill() {
		return ghostBlockingPPill;
	}

	public boolean isGhostBlockingPathToPill() {
		return ghostBlockingPill;
	}

	public boolean isGhostBlockingPathToEGhost() {
		return ghostBlockingEGhost;
	}

	@Override
	public Collection<String> getFacts() {

		Vector<String> facts = new Vector<String>();

		facts.add(String.format("(MSPACMAN (closeToPowerPill %s) (closeToNonEdibleGhost %s) (closeToEdibleGhost %s) (existPowerPill %s))", 
				this.nearPowerPill(), this.closeToNonEdibleGhost(), this.closeToEdibleGhost(), this.PPillsRemaining() > 0));
		
		
		return facts;
	}
}
