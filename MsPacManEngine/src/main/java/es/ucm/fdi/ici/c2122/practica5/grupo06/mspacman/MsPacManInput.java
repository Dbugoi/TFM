package es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.Utils;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.*;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);		
	}
	
	Integer score;
	
	Integer MsPacManNode;
	
	int closest;
	int middlest;
	int farthest;
	
	Integer CloseNode;
	Boolean CloseEdible;
	MOVE CloseMove;
	Integer CloseDistance;
	
	Integer MidNode;
	Boolean MidEdible;
	MOVE MidMove;
	Integer MidDistance;
	
	Integer FarNode;
	Boolean FarEdible;
	MOVE FarMove;
	Integer FarDistance;
	
	Integer nearestPPill;
	Integer nearestPill;

	Integer lives;
	Integer time;
	Integer level;
	MOVE lastMove;
	
	@Override
	public void parseInput() {	
		score = game.getScore();
		lives = game.getPacmanNumberOfLivesRemaining();
		time = game.getCurrentLevelTime();	// se puede utilizar el tiempo total
		level = game.getCurrentLevel();
		lastMove = game.getPacmanLastMoveMade();
		MsPacManNode = game.getPacmanCurrentNodeIndex();
		
		computeClosestGhost(game);
		computeMiddlestGhost(game);
		computeFarthestGhost(game);
		
		computeNearestPPill(game);
		computeNearestPill(game);
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();

		description.setScore(score);
		
		description.setMsPacManNode(MsPacManNode);
		description.setLastMove(lastMove);
		
		description.setCloseNode(CloseNode);
		description.setCloseMove(CloseMove);;
		description.setCloseEdible(CloseEdible);
		description.setCloseDistance(CloseDistance);
		
		description.setMidNode(MidNode);
		description.setMidMove(MidMove);
		description.setMidEdible(MidEdible);
		description.setMidDistance(MidDistance);
		
		description.setFarNode(FarNode);
		description.setFarMove(FarMove);
		description.setFarEdible(FarEdible);
		description.setFarDistance(FarDistance);
		
		description.setNearestPPill(nearestPPill);
		description.setNearestPill(nearestPill);
		
		description.setLives(lives);
		description.setTime(time);
		description.setLevel(level);	

		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeClosestGhost(Game game) {
		closest = Utils.getClosestGhostToPacMan(game);
		
		CloseNode = -1;
		CloseEdible = false;
		CloseMove = MOVE.NEUTRAL;
		CloseDistance = 0;
		
		if(closest != - 1) {
			GHOST g = GHOST.values()[closest];
			CloseNode = game.getGhostCurrentNodeIndex(g);;
			CloseEdible = game.isGhostEdible(g);
			CloseMove = game.getGhostLastMoveMade(g);
			CloseDistance = (int)game.getDistance(closest, game.getPacmanCurrentNodeIndex(), DM.PATH);
		}
	}
	
	private void computeMiddlestGhost(Game game) {
		middlest = Utils.getMiddlestGhostToPacMan(game);
		
		MidNode = -1;
		MidEdible = false;
		MidMove = MOVE.NEUTRAL;
		MidDistance = 0;
		
		if(middlest != - 1) {
			GHOST g = GHOST.values()[middlest];
			MidNode = game.getGhostCurrentNodeIndex(g);;
			MidEdible = game.isGhostEdible(g);
			MidMove = game.getGhostLastMoveMade(g);
			MidDistance = (int)game.getDistance(closest, game.getPacmanCurrentNodeIndex(), DM.PATH);
		}
	}
	
	private void computeFarthestGhost(Game game) {
		farthest = Utils.getFarthestGhostToPacMan(game);
		
		FarNode = -1;
		FarEdible = false;
		FarMove = MOVE.NEUTRAL;
		FarDistance = 0;
		
		if(farthest != - 1) {
			GHOST g = GHOST.values()[farthest];
			FarNode = game.getGhostCurrentNodeIndex(g);;
			FarEdible = game.isGhostEdible(g);
			FarMove = game.getGhostLastMoveMade(g);
			FarDistance = (int)game.getDistance(closest, game.getPacmanCurrentNodeIndex(), DM.PATH);
		}
	}
	
	private void computeNearestPPill(Game game) {
	int npos = -1;
		int min = Integer.MAX_VALUE;
		for(int pos: game.getActivePowerPillsIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < min)
				min = distance;
				npos = pos;
		}
		//nearestPPill = min;
		nearestPPill = npos;
	}
	
	private void computeNearestPill(Game game) {
		int npos = -1;
		int min = Integer.MAX_VALUE;
		for(int pos: game.getActivePillsIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < min)
				min = distance;
				npos = pos;
		}
		//nearestPill = min;
		nearestPill = npos;
	}
}
