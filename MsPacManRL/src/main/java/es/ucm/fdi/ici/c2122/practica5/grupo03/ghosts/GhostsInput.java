package es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.CbrVector;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends CBRInput {

	public GhostsInput(Game game) {
		super(game);
		
	}

	Integer level;
	
	static final MOVE[] ordDirNames = {MOVE.UP, MOVE.DOWN, MOVE.LEFT, MOVE.RIGHT};
	
	static final GHOST[] ordGhostNames = {GHOST.BLINKY, GHOST.PINKY, GHOST.INKY, GHOST.SUE};
	
	CbrVector DistanciaClosestGhostDirecction; // UP(0) , DOWN(1), LEFT(2), RIGHT(3)
	
	CbrVector DistanciaGhosts; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	CbrVector DistanciaClosestPPillDirecction; // UP(0) , DOWN(1), LEFT(2), RIGHT(3)
	
	CbrVector DistanciaGhostsClosestPPill; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	MOVE PacManLastMove;
	
	CbrVector GhostLastMove; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	CbrVector EdibleTimeGhosts; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	CbrVector JailTimeGhosts; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	Integer score;
	Integer lives;
	Integer PillsLeft;
	//Integer time;
	
	@Override
	public void parseInput() {
		level = game.getCurrentLevel();
		computeDistanciaClosestGhostDirecction(game);
		computeDistanciaGhosts(game);
		computeDistanciaClosestPPillDirecction(game);
		computeDistanciaGhostsClosestPPill(game);
		PacManLastMove = game.getPacmanLastMoveMade();
		computeGhostLastMove(game);
		computeEdibleTimeGhosts(game);
		computeJailTimeGhosts(game);
		score = game.getScore();
		PillsLeft = game.getNumberOfActivePills();
		
		//time = game.getTotalTime();
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();
		description.setDistanciaClosestGhostDirecction(DistanciaClosestGhostDirecction);
		description.setDistanciaGhosts(DistanciaGhosts);
		description.setDistanciaClosestPPillDirecction(DistanciaClosestPPillDirecction);
		description.setDistanciaGhostsClosestPPill(DistanciaGhostsClosestPPill);
		description.setPacManLastMove(PacManLastMove);
		description.setGhostLastMove(GhostLastMove);
		description.setEdibleTimeGhosts(EdibleTimeGhosts);
		description.setJailTimeGhosts(JailTimeGhosts);
		description.setScore(score);
		description.setLevel(level);
		description.setPillsLeft(PillsLeft);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	
	private void computeDistanciaClosestGhostDirecction(Game game) {
		int i = 0;
		int temporal;
		
		Integer[] DistanciaClosestGhostDirecction = new Integer[4];
		
		for(GHOST g: ordGhostNames) {
			DistanciaClosestGhostDirecction[i] = Integer.MAX_VALUE;
			for(MOVE m: ordDirNames) {
				 if(game.getGhostLairTime(g) <= 0) {
					 temporal = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), m, DM.PATH);
					 if(temporal < DistanciaClosestGhostDirecction[i]) {
						 DistanciaClosestGhostDirecction[i] = temporal;
					 }
				 }
			}
			if(DistanciaClosestGhostDirecction[i] == Integer.MAX_VALUE) {
				DistanciaClosestGhostDirecction[i] = -1;
			}
			
			i ++;
		}
		
		this.DistanciaClosestGhostDirecction = new CbrVector(DistanciaClosestGhostDirecction);
	}
	
	private void computeDistanciaGhosts(Game game) {
		
		int i = 0;
		
		Integer[] DistanciaGhosts = new Integer[4];
		
		for(GHOST g: ordGhostNames) {
			if(game.getGhostLairTime(g) <= 0) {
				DistanciaGhosts[i] = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), game.getPacmanLastMoveMade(), DM.PATH);
				}
			else {
				DistanciaGhosts[i] = -1;
			}
			i ++;
		}
		
		this.DistanciaGhosts = new CbrVector(DistanciaGhosts);
	}
	
	private void computeDistanciaClosestPPillDirecction(Game game) {
		int i = 0;
		int temporal;
		
		Integer[] DistanciaClosestPPillDirecction = new Integer[4];
		
		for(int p: game.getActivePowerPillsIndices()) {
			DistanciaClosestPPillDirecction[i] = Integer.MAX_VALUE;
			for(MOVE m: ordDirNames) {
				 temporal = (int) game.getDistance(game.getPacmanCurrentNodeIndex(), p, m, DM.PATH);
				 if(temporal < DistanciaClosestPPillDirecction[i]) {
					 DistanciaClosestPPillDirecction[i] = temporal;
				 }
				 
			}
			i ++;
		}
		
		for(int d = game.getNumberOfActivePowerPills(); d < 4; d++) {
			DistanciaClosestPPillDirecction[d] = -1;
		}
		
		this.DistanciaClosestPPillDirecction = new CbrVector(DistanciaClosestPPillDirecction);
	}
	
	private void computeDistanciaGhostsClosestPPill(Game game) {
		int i = 0;
		
		Integer[] DistanciaGhostsClosestPPill = new Integer[4];
		
		for(GHOST g: ordGhostNames) {
			if(game.getGhostLairTime(g) <= 0) {
				DistanciaGhostsClosestPPill[i] = (int) game.getClosestNodeIndexFromNodeIndex(game.getGhostCurrentNodeIndex(g), game.getActivePowerPillsIndices(), DM.PATH);
			}
			else {
				DistanciaGhostsClosestPPill[i] = -1;
			}
			i ++;
			
		}
		
		this.DistanciaGhostsClosestPPill = new CbrVector(DistanciaGhostsClosestPPill);
	}
	
	private void computeGhostLastMove(Game game) {
		int i = 0;
		
		MOVE[] GhostLastMove = new MOVE[4];
		
		for(GHOST g: ordGhostNames) {
			if(game.getGhostLairTime(g) <= 0) {
				GhostLastMove[i] = game.getGhostLastMoveMade(g);
			}
			else {
				GhostLastMove[i] = MOVE.NEUTRAL;
			}
			i ++;
		}
		
		this.GhostLastMove = new CbrVector(GhostLastMove);
	}
	
	private void computeEdibleTimeGhosts(Game game) {
		int i = 0;
		
		Integer[] EdibleTimeGhosts = new Integer[4];
		
		for(GHOST g: ordGhostNames) {
			EdibleTimeGhosts[i] = game.getGhostEdibleTime(g);
			i ++;
		}
		
		this.EdibleTimeGhosts = new CbrVector(EdibleTimeGhosts);
	}
	
	private void computeJailTimeGhosts(Game game) {
		int i = 0;
		
		Integer[] JailTimeGhosts = new Integer[4];
		
		for(GHOST g: ordGhostNames) {
			JailTimeGhosts[i] = game.getGhostLairTime(g);
			i ++;
		}
		
		this.JailTimeGhosts = new CbrVector(JailTimeGhosts);
	}
	
	
}
