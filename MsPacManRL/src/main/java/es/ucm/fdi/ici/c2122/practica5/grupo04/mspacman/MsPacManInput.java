package es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	//atributos a guardar
	private Integer id;
	
	private Integer posPacman;
	private Integer[] ghostsPos;
	private MOVE[] ghostsMove;
	private MOVE lastPacmanDirection;
	private Boolean[] ghostsEdible;
	private Integer[] pPillsPos;//-1 si ya se ha comido
	private Integer closestPill;
	private Integer closestPillDist;
	private Integer[] nextJunctionPills;
	private Integer closestPPillDist;
	private Float danger;  
	private Integer health;
	private Integer score;
	private Integer time;
	
	private final float dangerMult = 1.65f;
	
	@Override
	public void parseInput() {
		ghostsPos = new Integer[4];
		ghostsMove = new MOVE[4];
		ghostsEdible = new Boolean[4];
		pPillsPos = new Integer[4];
		nextJunctionPills = new Integer[4];
		for(int k = 0; k < 4; ++k) {
			nextJunctionPills[k] = -1;
		}

		posPacman = game.getPacmanCurrentNodeIndex();
		lastPacmanDirection = game.getPacmanLastMoveMade();
		for(GHOST g : GHOST.values()) {
			ghostsPos[g.ordinal()] = game.getGhostCurrentNodeIndex(g);
			ghostsMove[g.ordinal()] = game.getGhostLastMoveMade(g);
			ghostsEdible[g.ordinal()] = game.isGhostEdible(g);
		}
		int i=0;
		int[] ppills = game.getPowerPillIndices();
		for(i = 0; i < ppills.length; ++i) {
			if(game.isPowerPillStillAvailable(i))
				pPillsPos[i] = ppills[i];
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
		danger = CommonMethodsPacman.dangerLevel(game, posPacman, dangerMult);
		
		health = game.getPacmanNumberOfLivesRemaining();
		score=game.getScore();
		time = game.getTotalTime();
		sortGhosts();
	}
	
	//ordena los fantasmas en funcion a su posicion, para descartar casos iguales con fantasmas en otro orden
	void sortGhosts() {
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
	

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		

		description.setBLINKYghostEdible(ghostsEdible[GHOST.BLINKY.ordinal()]);
		description.setBLINKYghostMove(ghostsMove[GHOST.BLINKY.ordinal()]);
		description.setBLINKYghostPos(ghostsPos[GHOST.BLINKY.ordinal()]);
		description.setClosestPill(closestPill);
		description.setClosestPPillDist(closestPPillDist);
		description.setDanger(danger);
		description.setHealth(health);
		description.setINKYghostEdible(ghostsEdible[GHOST.INKY.ordinal()]);
		description.setINKYghostMove(ghostsMove[GHOST.INKY.ordinal()]);
		description.setINKYghostPos(ghostsPos[GHOST.INKY.ordinal()]);
		description.setLastPacmanDirection(lastPacmanDirection);
		description.setPINKYghostEdible(ghostsEdible[GHOST.PINKY.ordinal()]);
		description.setPINKYghostMove(ghostsMove[GHOST.PINKY.ordinal()]);
		description.setPINKYghostPos(ghostsPos[GHOST.PINKY.ordinal()]);
		description.setPosPacman(posPacman);
		description.setPpillsPos1(pPillsPos[0]);
		description.setPpillsPos2(pPillsPos[1]);
		description.setPpillsPos3(pPillsPos[2]);
		description.setPpillsPos4(pPillsPos[3]);
		description.setClosestPillDist(closestPillDist);
		description.setNextJunctionPills1(nextJunctionPills[0]);
		description.setNextJunctionPills2(nextJunctionPills[1]);
		description.setNextJunctionPills3(nextJunctionPills[2]);
		description.setNextJunctionPills4(nextJunctionPills[3]);
		description.setScore(score);
		description.setSUEghostEdible(ghostsEdible[GHOST.SUE.ordinal()]);
		description.setSUEghostMove(ghostsMove[GHOST.SUE.ordinal()]);
		description.setSUEghostPos(ghostsPos[GHOST.SUE.ordinal()]);
		description.setTime(time);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
}
