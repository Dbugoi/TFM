package es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.c2122.practica5.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends CBRInput {

	public GhostsInput(Game game) {
		super(game);
		
	}

	//atributos a guardar	
	Integer ghost;//para saber que ghost eres
	Integer id;
	Integer posPacman;
	Integer[] ghostsPos;
	MOVE[] ghostsMove;
	MOVE lastPacmanDirection;
	Boolean[] ghostsEdible;
	Integer[] pPillsPos;//-1 si ya se ha comido
	Integer closestPill;
	Integer closestPillDist;
	Integer[] nextJunctionPills;
	Integer closestPPillDist;
	Float[] danger;  
	Integer health;
	Integer score;
	Integer time;
	
	final float dangerMult = 1.65f;
	
	@Override
	public void parseInput() {
		ghostsPos = new Integer[4];
		ghostsMove = new MOVE[4];
		ghostsEdible = new Boolean[4];
		pPillsPos = new Integer[4];
		nextJunctionPills = new Integer[4];
		danger = new Float[4];
		for(int k = 0; k < 4; ++k) {
			nextJunctionPills[k] = -1;
		}

		posPacman = game.getPacmanCurrentNodeIndex();
		lastPacmanDirection = game.getPacmanLastMoveMade();
		for(GHOST g : GHOST.values()) {
			ghostsPos[g.ordinal()] = game.getGhostCurrentNodeIndex(g);
			ghostsMove[g.ordinal()] = game.getGhostLastMoveMade(g);
			ghostsEdible[g.ordinal()] = game.isGhostEdible(g);
			danger[g.ordinal()] = CommonMethodsGhosts.dangerLevel(game, posPacman, dangerMult, g);
		}
		int[] ppils = game.getPowerPillIndices();
		int i=0;
		for(i = 0; i < ppils.length; ++i) {
			if(game.isPowerPillStillAvailable(i))
				pPillsPos[i] = ppils[i];
			else
				pPillsPos[i]=-1;
		}
		
		
		closestPill = CommonMethodsPacman.getClosestPill(game, posPacman);
		closestPillDist = game.getShortestPathDistance(posPacman, closestPill, lastPacmanDirection);
		int[] juncs = CommonMethodsPacman.getAdjacentJunctionsNaturalOrder(game, posPacman, lastPacmanDirection);
		MOVE[] allMoves = MOVE.values();
		for ( i = 0; i < juncs.length; ++i) {
			if(juncs[i] != -1)
				nextJunctionPills[i] = CommonMethodsPacman.getPathNumPills(game, game.getShortestPath(posPacman, juncs[i], allMoves[i]));
		}
		if(game.getNumberOfActivePowerPills() > 0) {
			closestPPillDist = game.getShortestPathDistance(posPacman, CommonMethodsPacman.getClosestPPill(game, posPacman), lastPacmanDirection);
		}else 
			closestPPillDist = -1;
		
		health = game.getPacmanNumberOfLivesRemaining();
		score=game.getScore();
		time = game.getTotalTime();
	}
	
	//ordena los fantasmas en funcion a su posicion, para descartar casos iguales con fantasmas en otro orden
	void sortGhosts(int[] ghostsPos, boolean[] ghostsEdible, MOVE[] ghostsMove) {
		int n = ghostsPos.length;
		for(int i = 0; i < n-1; i++)
			for(int j = 0; j < n - i -1; j++)
				if(ghostsPos[j] > ghostsPos[j+1]) {
					int temp = ghostsPos[j];
					ghostsPos[j] = ghostsPos[j+1];
					ghostsPos[j+1] = temp;
					
					boolean tempE = ghostsEdible[j];
					ghostsEdible[j] = ghostsEdible[j+1];
					ghostsEdible[j+1] = tempE;
					
					MOVE tempM = ghostsMove[j];
					ghostsMove[j] = ghostsMove[j+1];
					ghostsMove[j+1] = tempM;
				}
	}
	
	//para saber que fantasma eres
	public void setGhost(int ghost) { this.ghost = ghost; }
	
	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();
		int[] otherGhostsPos = new int[3];
		boolean[] otherGhostsEdible = new boolean[3];
		MOVE[] otherGhostsMove = new MOVE[3];
		int j = 0;
		for(int i = 0 ; i< GHOST.values().length; ++i) {
			if(ghost != i) {
				otherGhostsPos[j] = ghostsPos[i];
				otherGhostsEdible[j] = ghostsEdible[i];
				otherGhostsMove[j++] = ghostsMove[i];
			}
		}
		sortGhosts(otherGhostsPos, otherGhostsEdible, otherGhostsMove);
		description.setGhostEdible(ghostsEdible[ghost]);
		description.setGhostMove(ghostsMove[ghost]);
		description.setGhostPos(ghostsPos[ghost]);
		description.setGhostEdible1(otherGhostsEdible[0]);
		description.setGhostMove1(otherGhostsMove[0]);
		description.setGhostPos1(otherGhostsPos[0]);
		description.setGhostEdible2(otherGhostsEdible[1]);
		description.setGhostMove2(otherGhostsMove[1]);
		description.setGhostPos2(otherGhostsPos[1]);
		description.setGhostEdible3(otherGhostsEdible[2]);
		description.setGhostMove3(otherGhostsMove[2]);
		description.setGhostPos3(otherGhostsPos[2]);
		description.setClosestPill(closestPill);
		description.setClosestPPillDist(closestPPillDist);
		description.setHealth(health);
		description.setLastPacmanMove(lastPacmanDirection);
		description.setPacmanPos(posPacman);
		description.setPpillsPos1(pPillsPos[0]);
		description.setPpillsPos2(pPillsPos[1]);
		description.setPpillsPos3(pPillsPos[2]);
		description.setPpillsPos4(pPillsPos[3]);
		description.setClosestPillDist(closestPillDist);
		description.setNextJunctionPills1(nextJunctionPills[0]);
		description.setNextJunctionPills2(nextJunctionPills[1]);
		description.setNextJunctionPills3(nextJunctionPills[2]);
		description.setNextJunctionPills4(nextJunctionPills[3]);
		description.setDanger(danger[ghost]);
		description.setScore(score);
		description.setTime(time);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
}
