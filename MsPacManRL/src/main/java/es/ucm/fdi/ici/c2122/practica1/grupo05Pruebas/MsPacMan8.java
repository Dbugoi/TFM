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
 
public final class MsPacMan8 extends PacmanController {

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 65;
		GHOST nearestChasingGhost = getNearestChasingGhost(limit, game);
		GHOST nearestEdibleGhost = getNearestEdibleGhost(limit, game);
		
		int i =0;
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman))
			i=1;
		
		int indexPpill = getNearestPowerPill(game);
		int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
		int dangerCage = okCage(game);
		
		int indexChasingGhost=-1, distanceChasingGhost_MsP=-1,distancePPill_ChasingGhost=-1, indexEdibleGhost=-1, distanceEdibleGhost_MsP=-1, distanceEdibleGhost_ChasingPacman=-1;
		if(nearestChasingGhost!=null) {
		 indexChasingGhost = game.getGhostCurrentNodeIndex(nearestChasingGhost);
		 distanceChasingGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexChasingGhost);
		 distancePPill_ChasingGhost = game.getShortestPathDistance(indexChasingGhost, indexPpill);
		}
		if(nearestEdibleGhost!=null) {
		 indexEdibleGhost = game.getGhostCurrentNodeIndex(nearestEdibleGhost);
		 distanceEdibleGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexEdibleGhost);
		}
		if(nearestEdibleGhost!=null&&nearestChasingGhost!=null) 
		  distanceEdibleGhost_ChasingPacman = game.getShortestPathDistance(indexChasingGhost, indexEdibleGhost);
	
		
		
		if (nearestChasingGhost!=null && nearestEdibleGhost!=null)// hay ghost comibles y no comibles cerca
		{
			if(dangerCage!=-1) 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 492, DM.PATH);
			else if(isPosibleToChaseEdibleGhost(game,nearestEdibleGhost, distanceEdibleGhost_MsP,distanceEdibleGhost_ChasingPacman, distanceChasingGhost_MsP, limit, dangerCage ))
					return getBestMoveTowardsGhost(game, limit,indexEdibleGhost );//return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexEdibleGhost, DM.PATH);
			else if (mustRunAwayFromChasingGhost(distanceEdibleGhost_MsP, distanceChasingGhost_MsP,distanceEdibleGhost_ChasingPacman, limit ))
				return getBestRunAwayGhost(game, limit*0.85, distancePPill_MsP, indexChasingGhost); //game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), indexChasingGhost,DM.EUCLID);
			else
				return getBestMoveTowardsGhost(game, limit,indexEdibleGhost );
			
			
		}
		else if (nearestChasingGhost!=null ) //hay un ghost no comible cerca
		{

			if(dangerCage!=-1) 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 492, DM.PATH);
			else if(moveTowardsPpill(indexPpill, distancePPill_MsP, distanceChasingGhost_MsP, distancePPill_ChasingGhost))
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPpill, DM.PATH);
			else 
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexChasingGhost);
			
		}
		else if (nearestEdibleGhost!=null){  //hay un ghost comible cerca
			indexChasingGhost = -1;
			if(dangerCage!=-1) //la carcel es peligrosa 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 492, DM.PATH);
			else if (game.getGhostEdibleTime(nearestEdibleGhost)>= distanceEdibleGhost_MsP && dangerCage==-1) 
				return getBestMoveTowardsGhost(game, limit,indexEdibleGhost ); 
			else 
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexChasingGhost);	
		}
		else {
			//ir a por las pills sin una powerpill cerca
			int indexpill = getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1);
			if(dangerCage!=-1) 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 492, DM.PATH);
			else
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexpill, DM.PATH);
		}
			
				
	}
	
	private boolean isPosibleToChaseEdibleGhost(Game game, GHOST nearestEdibleGhost, int distanceEdibleGhost_MsP, int distanceEdibleGhost_ChasingPacman, int distanceChasingGhost_MsP,int limit, int dangerCage) {
		return (game.getGhostEdibleTime(nearestEdibleGhost)>= distanceEdibleGhost_MsP&&(distanceEdibleGhost_MsP<distanceEdibleGhost_ChasingPacman  || distanceEdibleGhost_MsP< distanceChasingGhost_MsP) && distanceChasingGhost_MsP >limit && dangerCage==-1);
	}
	private boolean mustRunAwayFromChasingGhost(int distanceEdibleGhost_MsP, int distanceChasingGhost_MsP, int distanceEdibleGhost_ChasingPacman,int limit ) {
		return distanceEdibleGhost_MsP>distanceChasingGhost_MsP || distanceEdibleGhost_MsP< distanceEdibleGhost_ChasingPacman || distanceChasingGhost_MsP < limit*0.85;
	}
	
	private boolean moveTowardsPpill(int indexPpill, int distancePPill_MsP, int distanceChasingGhost_MsP, int distancePPill_ChasingGhost) {
		return indexPpill!=-1&&(distancePPill_MsP < distanceChasingGhost_MsP) && distancePPill_ChasingGhost<= distancePPill_MsP*4;}
	
	//buscar el ghost mas cercano que no sea comible
	private GHOST getNearestChasingGhost(double e, Game game) {
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) { //miramos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(distance > d && d>0 && d<=e&& !game.isGhostEdible(g)) //menor distancia no negativa y fantasma no comible
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
	//metodo que busca el ghost comible mas cercano
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
	
	//metodo que nos devuelve la pill mas cercana sin powerpill cercana y sin acercanos a un ghost
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
	
	//metodo que nos devuelve la pill mas cercana  sin acercanos a un ghost
	private int getNearestPillWithoutChasingGhost(Game game,  int indexChasing) {
		double distance = Integer.MAX_VALUE;
		int [] pillInd = game.getActivePillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		MOVE badMove= MOVE.NEUTRAL;
		
		if(indexChasing!=-1)
			badMove = game.getMoveToMakeToReachDirectNeighbour(mspacman, indexChasing);
		for(int pillIndex: pillInd) {

			double d = game.getShortestPathDistance(mspacman, pillIndex);

			
			if(distance > d && d>0 &&  indexChasing==-1)
			{	
				distance =d;
				finalPill = pillIndex;
			}
			else if(distance > d && d>0 && !game.getApproximateNextMoveTowardsTarget(mspacman, pillIndex, game.getPacmanLastMoveMade(), DM.PATH).equals(badMove))
			{	
				distance =d;
				finalPill = pillIndex;
			}
		}
		
		return finalPill;
	}
	
	//funcion que busca la powerpill mas cercana
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
	
	//metodo que mira si la distancia de la carcel y el pacman es menor que el tiempo de los fantasmas en la carcel 
	private int okCage(Game game) {
		int limit =20;
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int timeCage = game.getGhostLairTime(g);
			if(timeCage > game.getDistance(game.getPacmanCurrentNodeIndex(), 492, DM.EUCLID) && timeCage!=0 && timeCage<limit)
				return 492;
		}
		return -1;
	}
	
	//Elegir el mejor movimiento huyendo de los ghosts dentro de un rango (limit)
	private MOVE getBestRunAwayGhost( Game game, double limit, int distasncePPill, int indexNearestChasingP) {
		
		int mspacman = game.getPacmanCurrentNodeIndex();   //posicion actual Mspacman
		double distance = Integer.MIN_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		MOVE[] m = game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade()); //posibles movimientos del Mspacman
		boolean good_move=false;
		int index_pill=  getNearestPillWithoutChasingGhost(game, indexNearestChasingP); 
		MOVE mov = game.getNextMoveTowardsTarget(mspacman, index_pill, DM.EUCLID);
		
		for(MOVE nextMove: m) {   //recorremos los posibles movimientos del Mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(!game.isGhostEdible(g) && game.getShortestPathDistance(newNodeMsPacMan, ghost)<=limit) //buscamos las distancias desde el mspacman hacia los ghosts
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if(distance < d && d>0 )  //si la distancia es mayor (y no negativa) que la que teniamos hacemos ese movimiento, nos aleja mas de todos los ghosts
			{	
				distance =d;
				finalMove = nextMove;
			}
			
			if((game.getActivePowerPillsIndices().length==0 ||distance == Integer.MIN_VALUE || distance <30) && nextMove.equals(mov)) {
				good_move=true;
				
			}
			
		}
		if(indexNearestChasingP!=-1)
			if((mspacman==153 || mspacman==237) && game.isJunction(mspacman) && distasncePPill>limit && game.getNextMoveTowardsTarget(mspacman, indexNearestChasingP, DM.PATH)!=game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),492, DM.PATH) )
				finalMove = MOVE.DOWN;
		if(good_move)finalMove = mov;
	
		
		return finalMove;
	}
	
	//Elegir el mejor movimiento hacia los ghost en un rango
	private MOVE getBestMoveTowardsGhost(Game game, int limit, int indexNearestEdible) {
		int mspacman = game.getPacmanCurrentNodeIndex(); 
		double distance = Integer.MAX_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		MOVE[] m =  game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade());
		for(MOVE nextMove: m) {  //recorremos los posibles movimientos del mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {   //buscamos las distancias desde el mspacman hacia los ghosts
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(game.isGhostEdible(g) &&game.getShortestPathDistance(newNodeMsPacMan, ghost) <=limit*1.5)
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if(distance > d && d>=0) //si la distancia d es menor (y no negativa) que la que teniamos, hacemos ese movimiento. Nos acerca mas a los ghost comibles
			{						//dentro de un rango
				distance =d;
				finalMove = nextMove;
			}
		}
		if(distance == Integer.MAX_VALUE)
			finalMove = game.getNextMoveTowardsTarget(mspacman, indexNearestEdible, DM.PATH);
		return finalMove;
	}
	
	
	
}