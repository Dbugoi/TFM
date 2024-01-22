package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.Utils;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsDescription;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class GhostsInput extends CBRInput {
	public GhostsInput(Game game) {
		super(game);
		
	}
	Integer score;
	
	int closest;
	int midclose;
	int midfar;
	int farthest;

	Integer PacManNode;
	MOVE PacManLastMove;
	Integer PacManToPPill;

	Integer CloseNode;
	Integer CloseDistance;
	Boolean Closeedible;
	MOVE CloseMove;
	Integer CloseDistanceToGhost;

	Integer MidCNode;
	Integer MidCDistance;
	Boolean MidCedible;
	MOVE MidCMove;
	Integer MidCDistanceToGhost;

	Integer MidFNode;
	Integer MidFDistance;
	Boolean MidFedible;
	MOVE MidFMove;
	Integer MidFDistanceToGhost;
		
	Integer FarNode;
	Integer FarDistance;
	Boolean Faredible;
	MOVE FarMove;	
	Integer FarDistanceToGhost;
	

	Integer lives;
	Integer level;
	Integer nearestGhost;
	Integer nearestPPill;
	Integer nearestPill;
	Integer time;
	
	@Override
	public void parseInput() {
		PacManNode = game.getPacmanCurrentNodeIndex();
		PacManLastMove = game.getPacmanLastMoveMade();
		PacManToPPill = computePacmanToPPill(game);
		
		computeClosestGhost(game);
		computeMiddlestGhost(game);
		computeMiddlefarGhost(game);
		computeFarthestGhost(game);
		
		computeNearestPPill(game);
		computeNearestPill(game);
		
		time = game.getCurrentLevelTime();
		score = game.getScore();
		lives = game.getPacmanNumberOfLivesRemaining();
		level = game.getCurrentLevel();
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();
		description.setScore(score);
		
		description.setCloseNode(CloseNode);
		description.setCloseDistance(CloseDistance);
		description.setCloseedible(Closeedible);
		description.setCloseMove(CloseMove);
		description.setCloseDistanceToGhost(CloseDistanceToGhost);

		description.setMidCNode(MidCNode);
		description.setMidCDistance(MidCDistance);
		description.setMidCedible(MidCedible);
		description.setMidCMove(MidCMove);
		description.setMidCDistanceToGhost(MidCDistanceToGhost);
		
		description.setMidFNode(MidFNode);
		description.setMidFDistance(MidFDistance);
		description.setMidFedible(MidFedible);
		description.setMidFMove(MidFMove);
		description.setMidFDistanceToGhost(MidFDistanceToGhost);

		description.setFarNode(FarNode);
		description.setFarDistance(FarDistance);
		description.setFaredible(Faredible);
		description.setFarMove(FarMove);
		description.setFarDistanceToGhost(FarDistanceToGhost);
		
		description.setPacManNode(PacManNode);
		description.setPacManLastMove(PacManLastMove);
		description.setPacManToPPill(PacManToPPill);
		
		description.setNearestPPill(nearestPPill);
		description.setNearestPill(nearestPill);
		description.setLives(lives);
		description.setLevel(level);
		description.setTime(time);
		
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private int computeNearestGhostToGhost(Game game, GHOST ghost) {
		int min = Integer.MAX_VALUE;
		//ghosts.
		for(GHOST g: GHOST.values()) {
			if(g == ghost) continue;
			int pos = game.getGhostCurrentNodeIndex(g);
			int pos_ghost = game.getGhostCurrentNodeIndex(ghost);
			int distance; 
			if(pos_ghost != -1 && pos != -1) 
				distance = (int)game.getDistance(pos_ghost, pos, DM.PATH);
			else
				distance = Integer.MAX_VALUE;
			if(distance < min)
				min = distance;
		}
		return min;
	}

	private void computeClosestGhost(Game game) {
		closest = Utils.getClosestGhostToPacMan(game);
		
		CloseNode = -1;
		Closeedible = false;
		CloseMove = MOVE.NEUTRAL;
		CloseDistance = Integer.MAX_VALUE;
		CloseDistanceToGhost = Integer.MAX_VALUE;
		
		if(closest != - 1) {
			GHOST g = GHOST.values()[closest];
			CloseNode = game.getGhostCurrentNodeIndex(g);;
			Closeedible = game.isGhostEdible(g);
			CloseMove = game.getGhostLastMoveMade(g);
			CloseDistance = (int)game.getDistance(PacManNode, CloseNode, DM.PATH);
			CloseDistanceToGhost = computeNearestGhostToGhost(game, g);
		}
	}
	
	private void computeMiddlestGhost(Game game) {
		midclose = Utils.getMiddlestGhostToPacMan(game);
		
		MidCNode = -1;
		MidCedible = false;
		MidCMove = MOVE.NEUTRAL;
		MidCDistance = Integer.MAX_VALUE;
		MidCDistanceToGhost = Integer.MAX_VALUE;
		
		if(midclose != - 1) {
			GHOST g = GHOST.values()[midclose];
			MidCNode = game.getGhostCurrentNodeIndex(g);;
			MidCedible = game.isGhostEdible(g);
			MidCMove = game.getGhostLastMoveMade(g);
			MidCDistance = (int)game.getDistance(PacManNode, MidCNode, DM.PATH);
			MidCDistanceToGhost = computeNearestGhostToGhost(game, g);
		}
	}
	
	private void computeMiddlefarGhost(Game game) {
		midfar = Utils.getMiddlefarGhostToPacMan(game);
		
		MidFNode = -1;
		MidFedible = false;
		MidFMove = MOVE.NEUTRAL;
		MidFDistance = Integer.MAX_VALUE;
		MidFDistanceToGhost = Integer.MAX_VALUE;
		
		if(midfar != - 1) {
			GHOST g = GHOST.values()[midfar];
			MidFNode = game.getGhostCurrentNodeIndex(g);;
			MidFedible = game.isGhostEdible(g);
			MidFMove = game.getGhostLastMoveMade(g);
			MidFDistance = (int)game.getDistance(PacManNode, MidFNode, DM.PATH);
			MidFDistanceToGhost = computeNearestGhostToGhost(game, g);
		}
	}
	
	private void computeFarthestGhost(Game game) {
		farthest = Utils.getFarthestGhostToPacMan(game);
		
		FarNode = -1;
		Faredible = false;
		FarMove = MOVE.NEUTRAL;
		FarDistance = Integer.MAX_VALUE;
		FarDistanceToGhost = Integer.MAX_VALUE;
		
		if(farthest != - 1) {
			GHOST g = GHOST.values()[farthest];
			FarNode = game.getGhostCurrentNodeIndex(g);;
			Faredible = game.isGhostEdible(g);
			FarMove = game.getGhostLastMoveMade(g);
			FarDistance = (int)game.getDistance(PacManNode, FarNode, DM.PATH);
			FarDistanceToGhost = computeNearestGhostToGhost(game, g);
		}
	}
	
	
	private int computePacmanToPPill(Game game) {
		int min = Integer.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), ppill, DM.PATH);
			if(distance < min) 
				min = distance;
		}	
		return min;		
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


