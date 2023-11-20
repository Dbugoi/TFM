package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.ghosts;

import es.ucm.fdi.ici.c2122.practica3.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica3.grupo02.Utils;
import es.ucm.fdi.ici.rules.RulesInput;

import java.util.Collection;
import java.util.Vector;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostInput extends RulesInput {

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;

	private int BLINKYedTimeLeft;
	private int INKYedTimeLeft;
	private int PINKYedTimeLeft;
	private int SUEedTimeLeft;

	private boolean BLINKYoutOfLair;
	private boolean INKYoutOfLair;
	private boolean PINKYoutOfLair;
	private boolean SUEoutOfLair;

	private int BLINKYposition;
	private int INKYposition;
	private int PINKYposition;
	private int SUEposition;

	private int distanceBLINKYToPacman;
	private int distancePINKYToPacman;
	private int distanceINKYToPacman;
	private int distanceSUEToPacman;

	private double pacmanDistancePowerPill;

	private int nextPillPacManBySeer;

	private boolean BLINKYanotherGhostInPath;
	private boolean INKYanotherGhostInPath;
	private boolean PINKYanotherGhostInPath;
	private boolean SUEanotherGhostInPath;

	private int chaseCountBLINKY;
	private int chaseCountINKY;
	private int chaseCountPINKY;
	private int chaseCountSUE;

	public GhostInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		int pacmanNode = game.getPacmanCurrentNodeIndex();

		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);

		this.BLINKYedTimeLeft = game.getGhostEdibleTime(GHOST.BLINKY);
		this.INKYedTimeLeft = game.getGhostEdibleTime(GHOST.INKY);
		this.PINKYedTimeLeft = game.getGhostEdibleTime(GHOST.PINKY);
		this.SUEedTimeLeft = game.getGhostEdibleTime(GHOST.SUE);

		this.BLINKYoutOfLair = !(game.getGhostLairTime(GHOST.BLINKY) > 0);
		this.PINKYoutOfLair = !(game.getGhostLairTime(GHOST.PINKY) > 0);
		this.INKYoutOfLair = !(game.getGhostLairTime(GHOST.INKY) > 0);
		this.SUEoutOfLair = !(game.getGhostLairTime(GHOST.SUE) > 0);

		this.BLINKYposition = game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		this.PINKYposition = game.getGhostCurrentNodeIndex(GHOST.PINKY);
		this.INKYposition = game.getGhostCurrentNodeIndex(GHOST.INKY);
		this.SUEposition = game.getGhostCurrentNodeIndex(GHOST.SUE);

		this.distanceBLINKYToPacman = game.getShortestPathDistance(pacmanNode, BLINKYposition);
		this.distanceINKYToPacman = game.getShortestPathDistance(pacmanNode, INKYposition);
		this.distancePINKYToPacman = game.getShortestPathDistance(pacmanNode, PINKYposition);
		this.distanceSUEToPacman = game.getShortestPathDistance(pacmanNode, SUEposition);

		this.pacmanDistancePowerPill = Double.MAX_VALUE;
		for (int ppill : game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacmanNode, ppill, DM.PATH);
			this.pacmanDistancePowerPill = Math.min(distance, this.pacmanDistancePowerPill);
		}

		for (GHOST ghost : GHOST.values()) {
			// Seer prediction
			if (game.doesGhostRequireAction(ghost)) {
				if (ghost == GHOST.BLINKY)
					seerPill(pacmanNode);
				
				if (game.isGhostEdible(ghost) || game.getGhostLairTime(ghost) > 0 ||
						game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), pacmanNode) >= GameConstants.ghostChaseDistance) 
					resetCount(ghost);
				else
					addCount(ghost);

				checkGhostsInPath(ghost);
			}
		}
	}

	public void resetCount(GHOST ghost) {
		switch (ghost) {
		case BLINKY: chaseCountBLINKY = 0; break;
		case INKY: chaseCountINKY = 0; break;
		case PINKY: chaseCountPINKY = 0; break;
		case SUE: chaseCountSUE = 0; break;
		}
	}
	
	public void addCount(GHOST ghost) {
		switch (ghost) {
		case BLINKY: chaseCountBLINKY++; break;
		case INKY: chaseCountINKY++; break;
		case PINKY: chaseCountPINKY++; break;
		case SUE: chaseCountSUE++; break;
		}
	}

	private void seerPill(int pacmanNode) {
		int[] activePills = game.getActivePillsIndices();
		int nearestPillNode = -1;
		int shortestDistance = Integer.MAX_VALUE;

		// We get the next possible destination of the Pacman
		for (int activePill : activePills) {
			int distance = game.getShortestPathDistance(pacmanNode, activePill, game.getPacmanLastMoveMade());
			if (distance > GameConstants.minPredictionDistance && distance < shortestDistance) {
				nearestPillNode = activePill;
				shortestDistance = distance;
			}
		}

		Utils.seerPill = nextPillPacManBySeer = nearestPillNode;
	}

	private void checkGhostsInPath(GHOST ghost) {
		int ghostNode = game.getGhostCurrentNodeIndex(ghost);

		boolean auxAnotherGhostInPath;
		switch(ghost) {
		case BLINKY: auxAnotherGhostInPath = BLINKYanotherGhostInPath = false; break;
		case PINKY: auxAnotherGhostInPath = PINKYanotherGhostInPath = false; break;
		case INKY: auxAnotherGhostInPath = INKYanotherGhostInPath = false; break;
		case SUE: auxAnotherGhostInPath = SUEanotherGhostInPath = false; break;
		default: auxAnotherGhostInPath = false; break;
		}

		for (GHOST ghostToEvade : GHOST.values()) {
			if (auxAnotherGhostInPath)
				return;

			if (ghostToEvade == ghost || game.getGhostLairTime(ghostToEvade) > 0)
				continue;

			int ghostEvNode = game.getGhostCurrentNodeIndex(ghostToEvade);
			int[] path = game.getShortestPath(ghostNode, ghostEvNode, game.getGhostLastMoveMade(ghost));

			int node = 0;
			while(node < path.length && !game.isJunction(node)) 
				node++;
				
			if (node == path.length) {
				switch(ghost) {
				case BLINKY: auxAnotherGhostInPath = BLINKYanotherGhostInPath = true; break;
				case PINKY: auxAnotherGhostInPath = PINKYanotherGhostInPath = true; break;
				case INKY: auxAnotherGhostInPath = INKYanotherGhostInPath = true; break;
				case SUE: auxAnotherGhostInPath = SUEanotherGhostInPath = true; break;
				}
			}
		}
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		
		//BLINKY
		facts.add(String.format("(BLINKY (edible %b) "
				+ "(edTimeLeft %d) "
				+ "(outOfLair %b) "
				+ "(position %d) "
				+ "(distanceToPacman %d) "
				+ "(anotherGhostInPath %b) "
				+ "(chaseCount %d))",
				(boolean)this.BLINKYedible, (int)this.BLINKYedTimeLeft, (boolean)this.BLINKYoutOfLair, (int)this.BLINKYposition, 
				(int)this.distanceBLINKYToPacman, (boolean)this.BLINKYanotherGhostInPath, (int)this.chaseCountBLINKY));
		
		//INKY
		facts.add(String.format("(INKY (edible %b) "
				+ "(edTimeLeft %d) "
				+ "(outOfLair %b) "
				+ "(position %d) "
				+ "(distanceToPacman %d) "
				+ "(anotherGhostInPath %b) "
				+ "(chaseCount %d))",
				(boolean)this.INKYedible, (int)this.INKYedTimeLeft, (boolean)this.INKYoutOfLair, (int)this.INKYposition, 
				(int)this.distanceINKYToPacman, (boolean)this.INKYanotherGhostInPath, (int)this.chaseCountINKY));
		
		//PINKY
		facts.add(String.format("(PINKY (edible %b) "
				+ "(edTimeLeft %d) "
				+ "(outOfLair %b) "
				+ "(position %d) "
				+ "(distanceToPacman %d) "
				+ "(anotherGhostInPath %b) "
				+ "(chaseCount %d))",
				(boolean)this.PINKYedible, (int)this.PINKYedTimeLeft, (boolean)this.PINKYoutOfLair, (int)this.PINKYposition, 
				(int)this.distancePINKYToPacman, (boolean)this.PINKYanotherGhostInPath, (int)this.chaseCountPINKY));
		
		//SUE
		facts.add(String.format("(SUE (edible %b) "
				+ "(edTimeLeft %d) "
				+ "(outOfLair %b) "
				+ "(position %d) "
				+ "(distanceToPacman %d) "
				+ "(anotherGhostInPath %b) "
				+ "(chaseCount %d))",
				(boolean)this.SUEedible, (int)this.SUEedTimeLeft, (boolean)this.SUEoutOfLair, (int)this.SUEposition, 
				(int)this.distanceSUEToPacman, (boolean)this.SUEanotherGhostInPath, (int)this.chaseCountSUE));
		
		//MSPACMAN
		facts.add(String.format("(MSPACMAN "
				+ "(pacmanDistancePowerPill %d) "
				+ "(nextPillPacManBySeer %d))",
				 (int)this.pacmanDistancePowerPill, (int)this.nextPillPacManBySeer));
		
		//CONSTANTS
		facts.add(String.format("(CONSTANTS (ghostChaseDistance %d) "
				+ "(mindistancePPill %d) "
				+ "(minPredictionDistance %d) "
				+ "(minIntersectionsBeforeChange %d))",
				(int)GameConstants.ghostChaseDistance, (int)GameConstants.minRangePacmanPowerPill, 
				(int)GameConstants.minPredictionDistance, (int)GameConstants.numIntersectionsBeforeChange));

		return facts;
	}
}
