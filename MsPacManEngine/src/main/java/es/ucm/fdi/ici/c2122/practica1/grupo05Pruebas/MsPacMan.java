package es.ucm.fdi.ici.c2122.practica1.grupo05Pruebas;

import java.awt.Color;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
 
public final class MsPacMan extends PacmanController {

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 60;
		GHOST nearestGhost = getNearestGhost(limit, game);
		
		//Mejorar luego con la cantidad de Ghosts
		if (nearestGhost!=null)
		{
			if(game.isGhostEdible(nearestGhost)) // ME CONVIENE PERSEGUIRLO?
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
			else {
				int indexGhost = game.getGhostCurrentNodeIndex(nearestGhost);
				int indexPpill = getNearestPowerPill(game, limit);
				int distanceGhost = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexGhost);
				int distancePPill = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
				
				if(distancePPill< distanceGhost)//Me coviene buscar la PPill?
					return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPpill, DM.PATH);
				else
					return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestGhost), DM.PATH);
				
			}
		}
		else {
			
			int indexPpill = getNearestPowerPill(game, Integer.MAX_VALUE);
			int distancePPill = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
			
			nearestGhost = getNearestGhost(distancePPill*2, game);
			
			if (nearestGhost!=null) {
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPpill, DM.PATH);
			}
			else
			{
				int indexpill = getNearestPillWithoutPPill(game,indexPpill);
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexpill, DM.PATH);
			}
				
		}
				
		}
	
	private GHOST getNearestGhost(int limit, Game game) {
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(distance > d && d>0 && d<=limit)
			{	
				distance =d;
				ghostType = g;
			}
		}
		
		Color[] colours = {Color.RED,Color.PINK, Color.CYAN, Color.YELLOW};
		if(ghostType!=null)
			if(game.getGhostLairTime(ghostType)<=0)
				GameView.addPoints(game,colours[ghostType.ordinal()],game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType),mspacman));
		
		
		return ghostType;
	}

	/*
	private GHOST getNearestEdibleGhost(int limit, Game game) {
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			
		
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(distance > d && d>0 && d<= limit && game.isGhostEdible(g))
			{	
				distance =d;
				ghostType = g;
			}
		}
		
		Color[] colours = {Color.RED,Color.PINK, Color.CYAN, Color.YELLOW};
		if(ghostType!=null)
			if(game.getGhostLairTime(ghostType)<=0)
				GameView.addPoints(game,colours[ghostType.ordinal()],game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType),mspacman));
			
		
		return ghostType;
	}
	
	private int getNearestPill(Game game) {
	
		double distance = Integer.MAX_VALUE;
		int [] pillInd = game.getActivePillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		
		for(int pillIndex: pillInd) {

			double d = game.getShortestPathDistance(mspacman, pillIndex);
			if(distance > d && d>0 )
			{	
				distance =d;
				finalPill = pillIndex;
			}
		}
		
		return finalPill;
	}
	*/
	private int getNearestPillWithoutPPill(Game game, int indexPP) {
		int limit=5;
		double distance = Integer.MAX_VALUE;
		int [] pillInd = game.getActivePillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		
		for(int pillIndex: pillInd) {

			double d = game.getShortestPathDistance(mspacman, pillIndex);
			double distancePP = game.getShortestPathDistance(pillIndex, indexPP);
			if(distance > d && d>0 && distancePP>limit )
			{	
				distance =d;
				finalPill = pillIndex;
			}
		}
		
		return finalPill;
	}
	
	private int getNearestPowerPill(Game game,int limit) {
		double distance = Integer.MAX_VALUE;
		int [] powerPillInd = game.getActivePowerPillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		
		for(int ppillIndex: powerPillInd) {

			double d = game.getShortestPathDistance(mspacman, ppillIndex);
			if(distance > d && d>0 && d<=limit )
			{	
				distance =d;
				finalPill = ppillIndex;
			}
		}
		
		return finalPill;
	}
	
	
}