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
 
public final class MsPacMan4 extends PacmanController {

	@Override
	public MOVE getMove(Game game, long timeDue) {

		int limit = 65;
		GHOST nearestChasingGhost = getNearestChasingGhost(limit, game);
		GHOST nearestEdibleGhost = getNearestEdibleGhost(limit, game);
		
	
		if (nearestChasingGhost!=null && nearestEdibleGhost!=null)// hay ghost comibles y no comibles cerca
		{
			int indexChasingGhost = game.getGhostCurrentNodeIndex(nearestChasingGhost);
			int distanceChasingGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexChasingGhost);
			int indexEdibleGhost = game.getGhostCurrentNodeIndex(nearestEdibleGhost);
			int distanceEdibleGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexEdibleGhost);
			int distanceEdibleGhost_ChasingPacman = game.getShortestPathDistance(indexChasingGhost, indexEdibleGhost);
		
			int indexPpill = getNearestPowerPill(game);
			int distancePPill_ChasingGhost = game.getShortestPathDistance(indexChasingGhost, indexPpill);
			int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
			
			if(distanceEdibleGhost_MsP<distanceEdibleGhost_ChasingPacman /* || distanceEdibleGhost_MsP<= distanceChasingGhost_MsP*/)
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexEdibleGhost, DM.PATH);
			else if (distanceEdibleGhost_MsP>distanceEdibleGhost_ChasingPacman)
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), indexChasingGhost, DM.PATH);
			else
				return getBestRunAwayGhost(game);
		}
		else if (nearestChasingGhost!=null )  //hay un ghost no comible cerca
		{
			int indexChasingGhost = game.getGhostCurrentNodeIndex(nearestChasingGhost);
			int distanceChasingGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexChasingGhost);
			
			int indexPpill = getNearestPowerPill(game);
			int distancePPill_ChasingGhost = game.getShortestPathDistance(indexChasingGhost, indexPpill);
			int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);
			
			if((distancePPill_MsP < distanceChasingGhost_MsP) && distancePPill_ChasingGhost>distancePPill_MsP) //PP + cerca, capaz de comer Chasing
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPpill, DM.PATH);
			else
				return getBestRunAwayGhost(game);
			
		}
		else if (nearestEdibleGhost!=null){  //hay un ghost comible cerca
			 int indexEdibleGhost = game.getGhostCurrentNodeIndex(nearestEdibleGhost);
			int distanceEdibleGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexEdibleGhost);
			
			int indexPpill = getNearestPowerPill(game);
			int distancePPill_EdibleGhost = game.getShortestPathDistance(indexEdibleGhost, indexPpill);
			int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);

			int dangerCage = okCage(game);	
			
			if (game.getGhostEdibleTime(nearestEdibleGhost)>= distanceEdibleGhost_MsP && dangerCage==-1) {  //carcel peligrosa pero se puede comer el fantasma
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestEdibleGhost), DM.PATH);	
				
			}
			else if(dangerCage!=-1) {  //la carcel es peligrosa 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), dangerCage, DM.PATH);  //huir de la carcel
			}
			else {
				return getBestMoveTowardsGhost(game);   //ir hacia el mejor ghost 
			}
		}
		else {//ir a por las pills con una powerpill cerca
			int indexPpill = getNearestPowerPill(game);
			int indexpill = getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1);
			return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexpill, DM.PATH);
			
		}
			
				
		}
	//buscar el ghost mas cercano que no sea comible
	private GHOST getNearestChasingGhost(double e, Game game) {
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) { //miramos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(distance > d && d>0 && d<=e&& !game.isGhostEdible(g))   //menor distancia no negativa y fantasma no comible
			{	
				distance =d;
				ghostType = g;
			}
		}
		
		Color[] colours = {Color.RED,Color.PINK, Color.CYAN, Color.YELLOW};
		if(ghostType!=null)
			if(game.getGhostLairTime(ghostType)<=0)   //añadimos puntos
				GameView.addPoints(game,colours[ghostType.ordinal()],game.getShortestPath(game.getGhostCurrentNodeIndex(ghostType),mspacman));
		
		
		return ghostType;
	}
	//funcion que busca el ghost comible mas cercano
	private GHOST getNearestEdibleGhost(int limit, Game game) {
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) {  //miramos entre todos los ghosts el que menor distancia tenga con mspacman
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
	//funcion que nos devuelve la pill mas cercana con  powerpill cercana y no estar persiguiendo al ghost
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
	
	//funcion que busca la powerpill mas cercana
	private int getNearestPowerPill(Game game) {
		double distance = Integer.MAX_VALUE;
		int [] powerPillInd = game.getActivePowerPillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		
		for(int ppillIndex: powerPillInd) {  //buscamos de todas las power pill la que menor distancia tenga con mspacman

			double d = game.getShortestPathDistance(mspacman, ppillIndex);
			if(distance > d && d>0  )
			{	
				distance =d;
				finalPill = ppillIndex;
			}
		}
		
		return finalPill;
	}
	
	//funcion mira si la distancia de la carcel y el pacman es menor que el tiempo de los fantasmas en la carcel 
	private int okCage(Game game) {
		int limit =15;
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int timeCage = game.getGhostLairTime(g);
			if(timeCage > game.getDistance(game.getPacmanCurrentNodeIndex(), 492, DM.EUCLID) && timeCage!=0 && timeCage<limit)
				return 492;
		}
		return -1;  //es peligrosa
	}
	
	//Elegir el mejor movimiento huyendo del ghost
	private MOVE getBestRunAwayGhost( Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();  //posicion actual Mspacman
		double distance = Integer.MIN_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		MOVE[] m = game.getPossibleMoves(mspacman);		//posibles movimientos del Mspacman
		for(MOVE nextMove: m) {                           //recorremos los posibles movimientos del Mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {   //buscamos las distancias desde el mspacman hacia los ghosts
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(!game.isGhostEdible(g))
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if(distance < d && d>0)  //si la distancia d es mayor (y no negativa) que la que teniamos hacemos ese movimiento
			{	
				distance =d;
				finalMove = nextMove;
			}
		}
		return finalMove;
	}
	
	//Elegir el mejor movimiento hacia el ghost
	private MOVE getBestMoveTowardsGhost(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		double distance = Integer.MAX_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		MOVE[] m = game.getPossibleMoves(mspacman);
		for(MOVE nextMove: m) {    						 //recorremos los posibles movimientos del mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {      //buscamos las distancias desde el mspacman hacia los ghosts
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(game.isGhostEdible(g))
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if(distance > d && d>=0)  //si la distancia d es menor (y no negativa) que la que teniamos hacemos ese movimiento
			{	
				distance =d;
				finalMove = nextMove;
			}
		}
		return finalMove;
	}
	
}