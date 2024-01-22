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
 
public final class MsPacMan6 extends PacmanController {

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 55;
		GHOST nearestChasingGhost = getNearestChasingGhost(limit, game);
		GHOST nearestEdibleGhost = getNearestEdibleGhost(limit, game);
		
		
		//Mejorar luego con la cantidad de Ghosts
		//HAY CERCA UN CHASING
		int i;
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman))
			i=1;
		
		if (nearestChasingGhost!=null && nearestEdibleGhost!=null)
		{
			int indexChasingGhost = game.getGhostCurrentNodeIndex(nearestChasingGhost);
			int distanceChasingGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexChasingGhost);
			int indexEdibleGhost = game.getGhostCurrentNodeIndex(nearestEdibleGhost);
			int distanceEdibleGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexEdibleGhost);
			int distanceEdibleGhost_ChasingPacman = game.getShortestPathDistance(indexChasingGhost, indexEdibleGhost);
		
			int indexPpill = getNearestPowerPill(game);
			int distancePPill_ChasingGhost = game.getShortestPathDistance(indexChasingGhost, indexPpill);
			int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
			
			int dangerCage = okCage(game);	
			if(distanceEdibleGhost_MsP<distanceEdibleGhost_ChasingPacman &&distanceEdibleGhost_ChasingPacman*2<= distanceChasingGhost_MsP)
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexEdibleGhost, DM.PATH);
			else if(dangerCage!=-1) 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), dangerCage, DM.PATH);
			else if (distanceEdibleGhost_MsP>distanceEdibleGhost_ChasingPacman)
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexPpill,getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1), distanceChasingGhost_MsP, indexChasingGhost );//return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), indexChasingGhost, DM.PATH);
			else
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexPpill, getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1), distanceChasingGhost_MsP, indexChasingGhost );
		}
		else if (nearestChasingGhost!=null )
		{
			int indexChasingGhost = game.getGhostCurrentNodeIndex(nearestChasingGhost);
			int distanceChasingGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexChasingGhost);
			
			int indexPpill = getNearestPowerPill(game);
			int distancePPill_ChasingGhost = game.getShortestPathDistance(indexChasingGhost, indexPpill);
			int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
			
			if((distancePPill_MsP < distanceChasingGhost_MsP) && distancePPill_ChasingGhost>distancePPill_MsP) //PP + cerca, capaz de comer Chasing
				return getBestRunAwayGhost(game, limit,distancePPill_MsP, indexPpill, getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1), distanceChasingGhost_MsP, indexChasingGhost ); //return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPpill, DM.PATH);
			else
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexPpill, getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1), distanceChasingGhost_MsP, indexChasingGhost );
			
		}
		else if (nearestEdibleGhost!=null){
			 int indexEdibleGhost = game.getGhostCurrentNodeIndex(nearestEdibleGhost);
			int distanceEdibleGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexEdibleGhost);
			
			int indexPpill = getNearestPowerPill(game);
			int distancePPill_EdibleGhost = game.getShortestPathDistance(indexEdibleGhost, indexPpill);
			int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);

			int dangerCage = okCage(game);	
			
			if (game.getGhostEdibleTime(nearestEdibleGhost)>= distanceEdibleGhost_MsP && dangerCage==-1) {
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestEdibleGhost), DM.PATH);	
				
			}
			else if(dangerCage!=-1) {
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), dangerCage, DM.PATH);
			}
			else {
				return getBestMoveTowardsGhost(game);
			}
		}
		else {
			int indexPpill = getNearestPowerPill(game);
			int indexpill = getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1);
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexpill, DM.PATH);
			
		}
			
				
		}
	
	private GHOST getNearestChasingGhost(double e, Game game) {
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(distance > d && d>0 && d<=e&& !game.isGhostEdible(g))
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
	
	private int getNearestPillWithoutPPillAndWithoutChasingGhost(Game game, int indexPP, int indexChasing) {
		int limit=10;
		double distance = Integer.MAX_VALUE;
		int [] pillInd = game.getActivePillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		MOVE badMove= MOVE.NEUTRAL;
		
		if(indexChasing!=-1)
			badMove = game.getMoveToMakeToReachDirectNeighbour(mspacman, indexChasing);
		for(int pillIndex: pillInd) {

			double d = game.getShortestPathDistance(mspacman, pillIndex);
			double distancePP = game.getShortestPathDistance(pillIndex, indexPP);
			
			if(distance > d && d>0 && distancePP>limit && indexChasing==-1)
			{	
				distance =d;
				finalPill = pillIndex;
			}
			else if(distance > d && d>0 && distancePP>limit&& !game.getApproximateNextMoveTowardsTarget(mspacman, pillIndex, game.getPacmanLastMoveMade(), DM.PATH).equals(badMove))
			{	
				distance =d;
				finalPill = pillIndex;
			}
		}
		
		return finalPill;
	}
	
	private int getNearestPowerPill(Game game) {
		double distance = Integer.MAX_VALUE;
		int [] powerPillInd = game.getActivePowerPillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		
		for(int ppillIndex: powerPillInd) {

			double d = game.getShortestPathDistance(mspacman, ppillIndex);
			if(distance > d && d>0  )
			{	
				distance =d;
				finalPill = ppillIndex;
			}
		}
		
		return finalPill;
	}
	
	private int okCage(Game game) {
		int limit =20;
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int timeCage = game.getGhostLairTime(g);
			if(timeCage > game.getDistance(game.getPacmanCurrentNodeIndex(), 492, DM.EUCLID) && timeCage!=0 && timeCage<limit)
				return 492;
		}
		return -1;
	}
	
	private MOVE getBestRunAwayGhost( Game game, int limit, int distancePPill, int indexPPill, int indexPill, int distanceNearestChasing, int indexNearestChasing) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		double distance = Integer.MIN_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		MOVE[] m = game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade());
		int distancePill = game.getShortestPathDistance(mspacman, indexPill);
		for(MOVE nextMove: m) {
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(game.getGhostLairTime(g)<=0&&!game.isGhostEdible(g) && game.getShortestPathDistance(newNodeMsPacMan, ghost)<=limit*1.5 /*/2+20*/)
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if(distancePPill<40 &&distancePPill>0)
				finalMove = game.getApproximateNextMoveTowardsTarget(mspacman, indexPPill, game.getPacmanLastMoveMade(), DM.PATH);
			else if(distanceNearestChasing>limit-5 && distancePill< 15)
				finalMove = game.getNextMoveTowardsTarget(mspacman, indexPill,  game.getPacmanLastMoveMade(), DM.PATH);
			else if(distance <d && d>0 )
			{	
				distance =d;
				finalMove = nextMove;
			}
			else if(distance ==d && d>0 && (game.getNodeYCood(mspacman)<20|| game.getNodeYCood(mspacman)>100)&&game.getNodeXCood(mspacman)>15)
			{	
				distance =d;
				finalMove = nextMove;
			}
		}
		//if(distanceNearestChasing<20)
		//	finalMove = game.getNextMoveAwayFromTarget(mspacman, indexNearestChasing, game.getPacmanLastMoveMade(), DM.PATH);
		return finalMove;
	}
	
	private MOVE getBestMoveTowardsGhost(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		double distance = Integer.MAX_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		MOVE[] m = game.getPossibleMoves(mspacman);
		for(MOVE nextMove: m) {
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(game.isGhostEdible(g))
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if(distance > d && d>=0)
			{	
				distance =d;
				finalMove = nextMove;
			}
		}
		return finalMove;
	}
	
}