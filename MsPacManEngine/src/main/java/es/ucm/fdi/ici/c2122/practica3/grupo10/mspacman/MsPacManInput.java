package es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {

	GHOST[] ghosts = GHOST.values();
	private boolean GHOSTSedibleSafe;
	private boolean POWERPILLSedibleSafe;
	private boolean PILLSedibleSafe;
	private boolean MSPACMANisInJunction;
	private int numberPossibleMoves;
	private String moves;
	private ArrayList<MOVE> possibleMoves;
	
	
	public MsPacManInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		//set the parameters for the facts
		MSPACMANisInJunction = game.isJunction(game.getPacmanCurrentNodeIndex());
		moves();
		ghosts();
		powerPill();
		pill();
	}


	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(GHOSTS (edibleSafe %s))", this.GHOSTSedibleSafe));
		facts.add(String.format("(POWERPILLS (edibleSafe %s))", this.POWERPILLSedibleSafe));
		facts.add(String.format("(PILLS (edibleSafe %s))", this.PILLSedibleSafe));
		facts.add(String.format("(MSPACMAN (numberPossibleMoves %d)(moves %s)(isInJunction %s))", this.numberPossibleMoves,this.moves, this.MSPACMANisInJunction));
		//facts.add(String.format("(MSPACMAN (moves %s))", this.moves));
		return facts;
	}


	private void moves() {
		moves = "";
		possibleMoves = new ArrayList<MOVE>(Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())));
		removeBadMoves(game, possibleMoves);
		numberPossibleMoves = possibleMoves.size();
		if(!possibleMoves.isEmpty()) {
			for (MOVE m: possibleMoves) {
				moves += m.toString() + "-";
			}
			moves = moves.substring(0, moves.length()-1);
		}
		else moves = "-";
	}

	private void ghosts() {
		GHOSTSedibleSafe = false;
		if (anyGhostEdible(game)) {
			int distance = 999999;
			for (int i = 0; i < GHOST.values().length; i++) {
				if(game.isGhostEdible(GHOST.values()[i])) {
					int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.values()[i]), game.getPacmanLastMoveMade());
					if (aux < distance && isSafe(game, GHOST.values()[i], aux)) {	
						MOVE resMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.values()[i]), game.getPacmanLastMoveMade(), DM.PATH);
						if (possibleMoves.contains(resMove)) {
							distance = aux;
							GHOSTSedibleSafe = true;
						}
					}
				}
			}
		}
	}

	private void powerPill() {
		POWERPILLSedibleSafe = false;
		int posPower[] = game.getActivePowerPillsIndices();
		if (posPower.length > 0 && allGhostsOut(game) && !anyGhostEdible(game)) {
			int distance = 999999;
			for (int i = 0; i < posPower.length; i++) {
				int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), posPower[i], game.getPacmanLastMoveMade());
				if (aux < distance && isSafe(game, posPower[i], aux)) {
					MOVE resMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), posPower[i], game.getPacmanLastMoveMade(), DM.PATH);
					if (possibleMoves.contains(resMove)) {
						distance = aux;
						POWERPILLSedibleSafe = true;
					}
				}
			}
		}
		
	}

	private void pill() {
		PILLSedibleSafe = false;
		int posPills[] = game.getActivePillsIndices();
		if (posPills.length > 0) {
			int distance = 999999;
			for (int i = 0; i < posPills.length; i++) {
				int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), posPills[i], game.getPacmanLastMoveMade());
				if (aux < distance && isSafe(game, posPills[i], aux)) {
					MOVE resMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), posPills[i], game.getPacmanLastMoveMade(), DM.PATH);
					if (possibleMoves.contains(resMove)) {
						distance = aux;
						PILLSedibleSafe = true;
					}
				}
			}
		}
	}



	public void removeBadMoves(Game game, ArrayList<MOVE> possibleMoves) {
		ArrayList<MOVE> movesCopy = new ArrayList<MOVE>();
		movesCopy.addAll(possibleMoves);
		int[] nextJunctions = new int[possibleMoves.size()];
		MOVE[] nextMoves = new MOVE[possibleMoves.size()];
		int[] powerPills = new int[possibleMoves.size()];
		int i = 0;
		//for each possible move, we get the next junction that it gets to
		for(MOVE m: possibleMoves) {
			getClosestJunction(game, game.getPacmanCurrentNodeIndex(), m, nextJunctions, nextMoves, powerPills, i);
			i++;
		}
		i = 0;
		//for each possible move, we check if MsPacman will be eaten before getting to the next junction
		for(MOVE m: movesCopy) {
			boolean removed = false;
			for (GHOST g: GHOST.values()) {
				if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
					if (game.getGhostCurrentNodeIndex(g) == nextJunctions[i]) {
						//System.out.println("DANGEROUS J " + m + ", GHOST " + g);
						possibleMoves.remove(m);
						removed = true;
						break ;
					}
					else if(ghostNotBetweenJunctions(game, g, m, game.getPacmanCurrentNodeIndex(), nextJunctions[i])) {
						if(powerPills[i] == -1 && game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextJunctions[i], game.getGhostLastMoveMade(g)) <= game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), nextJunctions[i], m) + 1) {
							//System.out.println("DANGEROUS F " + m + ", GHOST " + g);
							possibleMoves.remove(m);
							removed = true;
							break ;
						}
					}
					else{
						if(powerPills[i] != -1) {
							if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g)) < game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), powerPills[i], m) + 1) {
								//System.out.println("DANGEROUS C " + m + ", GHOST " + g);
								possibleMoves.remove(m);
								removed = true;
								break;
							}
						}
						else {
							if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g)) < game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), nextJunctions[i], m) + 1) {
								//System.out.println("DANGEROUS C " + m + ", GHOST " + g);
								possibleMoves.remove(m);
								removed = true;
								break;
							}
						}
					}
				}
			}
			if(!removed && powerPills[i] == -1 && !isSafeBeyond(game, m, nextMoves[i], nextJunctions[i], game.getShortestPathDistance(game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), nextJunctions[i], m) + 1, true)) possibleMoves.remove(m);
			i++;
		}
	}
	/**
     * checks if the moves after reaching the next junction lead to an inevitable death
     *
     * @param game 
     * @param originalMove Move taken in the original junction
     * @param lastMove Move taken from the next junction
     * @param junction Next junction
     * @param prevDistance Distance from the original junction to the next junction
     */
	//
	private boolean isSafeBeyond(Game game, MOVE originalMove, MOVE lastMove, int junction, int prevDistance, boolean recursive) {
		boolean result = false;
		ArrayList<MOVE> nextPossibleMoves = new ArrayList<MOVE>(Arrays.asList(game.getPossibleMoves(junction, lastMove)));
		int[] nextJunctions2 = new int[nextPossibleMoves.size()];
		int i = 0;
		for(MOVE m: nextPossibleMoves) {
			getClosestJunction(game, junction, m, nextJunctions2, new MOVE[nextPossibleMoves.size()], new int[nextPossibleMoves.size()], i);
			i++;
		}
		i = 0;
		for (MOVE m: nextPossibleMoves) {
			boolean aux = true;
			for (GHOST g: GHOST.values()) {
				if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
					if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextJunctions2[i], game.getGhostLastMoveMade(g)) <= (game.getShortestPathDistance(game.getNeighbour(junction, m), nextJunctions2[i], m) + 1 + prevDistance)) {
						//System.out.println("DANGEROUS BEYOND " + originalMove + " " + m + ", GHOST " + g);
						aux = false;
						break;
					}
				}
			}
			result = aux;
			//if (result && recursive) result = isSafeBeyond(game, lastMove, m, nextJunctions2[i], game.getShortestPathDistance(game.getNeighbour(junction, m), nextJunctions2[i], m) + 1, false);
			if (result) break;
			i++;
		}
		return result;
	}
	
	/**
     * gets the closest junction ,entry move and power pill index; given another junction and an initial move
     *
     * @param game 
     * @param currentPos
     * @param move
     * @param nextJ Array where the closest junction will be stored
     * @param nextM Array where entry move to the closest junction will be stored
     * @param powerP Array where index of power pill will be stored. -1 if there are none
     * @param i
     */
	//
	private void getClosestJunction(Game game, int currentPos, MOVE move, int[] nextJ, MOVE[] nextM, int[] powerP, int i) {
		//System.out.println("CURRENT PACMAN: " + game.getPacmanCurrentNodeIndex());
		boolean found = false;
		powerP[i] = -1;
		MOVE prevMove = move;
		int pos = game.getNeighbour(currentPos, prevMove);
		while (!found) {
			if (contains(game.getActivePowerPillsIndices(), pos)) powerP[i] = pos;
			found = game.isJunction(pos);
			if (!found) {
				MOVE aux = game.getPossibleMoves(pos, prevMove)[0];
				pos = game.getNeighbour(pos, aux);
				prevMove = aux;
			}
			 
		}
		nextJ[i] = pos;
		nextM[i] = prevMove;
	}
	private boolean contains(int[] array, int pos) {
		boolean check = false;
		for (int i : array) {
			if (i == pos) {
				check = true;
				break;
			}
		}
		return check;
	}
	
	/**
     * checks if a ghost is between two junctions
     *
     * @param game 
     * @param g Ghost tested
     * @param m Move that sets the direction of the path between junctions
     * @param j1 Junction 1
     * @param j2 Junction 2
     */
	// 
	private boolean ghostNotBetweenJunctions(Game game, GHOST g, MOVE m, int j1, int j2) {
		boolean result = true;
	    boolean found = false;
		MOVE prevMove = m;
		ArrayList<Integer> path = new ArrayList<Integer>();
		int pos = game.getNeighbour(game.getPacmanCurrentNodeIndex(), prevMove);
		while (!found) {
			found = game.isJunction(pos);
			if (!found) {
				path.add(pos);
				//System.out.print((new ArrayList<MOVE>(Arrays.asList(game.getPossibleMoves(pos, prevMove)))).toString());
				MOVE aux = game.getPossibleMoves(pos, prevMove)[0];
				pos = game.getNeighbour(pos, aux);

				prevMove = aux;
			}
		}
		
		int ghostPos = game.getGhostCurrentNodeIndex(g);
		for (int i : path) {
			if (i == ghostPos) result = false;
		}
		return result;
	}
	
	/**
     * checks if there are any edible ghosts
     *
     * @param game 
     */
	//
	private boolean anyGhostEdible(Game game) {
		boolean result = false;
		for (GHOST g : GHOST.values()) {
			if(game.isGhostEdible(g)) result = true;
		}
		return result;
	}
	
	/**
     * checks if a ghost can be reached before other dangerous ghosts get there firsts
     *
     * @param game 
     * @param ghost Target ghost being tested
     * @param distance Distance from MsPacman to the ghost
     */
	//
	//this method 
	private boolean isSafe(Game game, GHOST ghost, int distance) {
		boolean result = true;
		if(2*distance > game.getGhostEdibleTime(ghost)) {
			//System.out.println(ghost + " TIME " + game.getGhostEdibleTime(ghost) + " DIST " + 2 * distance);
			result = false;
		}
		for (GHOST g: GHOST.values()) {
			if (g != ghost && !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
				if (distance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(g))) result = false;
			}
		}
		return result;
	}
	
	/**
     * Checks if a index can be reached before other dangerous GHOST.values() get there first
     *
     * @param game 
     * @param i Index to be reached
     * @param distance Distance to index
     */
	//
	private boolean isSafe(Game game, int i, int distance) {
		boolean result = true;
		for (GHOST g: GHOST.values()) {
			if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
				if (distance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), i, game.getGhostLastMoveMade(g))) result = false;
			}
		}
		return result;
	}
	
	/**
     * checks if all ghosts are out of the lair
     *
     * @param game 
     */
	//
	private boolean allGhostsOut(Game game) {
		boolean res = true;
		for (GHOST g: GHOST.values()) {
			if(game.getGhostLairTime(g) != 0) res = false;
		}
		return res;
	}
}	
