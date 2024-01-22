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
 
public final class MsPacMan9 extends PacmanController {
	private int carcel=492;
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		int limit = 65;
		GHOST nearestChasingGhost = getNearestChasingGhost(limit, game);
		GHOST nearestEdibleGhost = getNearestEdibleGhost(limit, game);
		
		int i =0;
		int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman))
			i=1;
		
		int indexPpill = getNearestPowerPill(game);  //powerpill mas cercana
		int distancePPill_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexPpill);  //distancia powerpill y mspacman
		int dangerCage = okCage(game);  //carcel peligrosa(-1) o no(492)
		
		int indexChasingGhost=-1, distanceChasingGhost_MsP=-1,distancePPill_ChasingGhost=-1, indexEdibleGhost=-1, distanceEdibleGhost_MsP=-1, distancePPill_EdibleGhost=-1, distanceEdibleGhost_ChasingPacman=-1;
		if(nearestChasingGhost!=null) {  //ghost cercano no comible
		 indexChasingGhost = game.getGhostCurrentNodeIndex(nearestChasingGhost);			//index fantasma no comible mas cercano
		 distanceChasingGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexChasingGhost);  //distancia fantasma no comible mas cercano y mspacman
		 distancePPill_ChasingGhost = game.getShortestPathDistance(indexChasingGhost, indexPpill);  		//distancia fantasma no comible mas cercano y powerpill
		}
		if(nearestEdibleGhost!=null) {//ghost cercano  comible
		 indexEdibleGhost = game.getGhostCurrentNodeIndex(nearestEdibleGhost);										 //index fantasma  comible mas cercano
		 distanceEdibleGhost_MsP = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexEdibleGhost); //distancia fantasma  comible mas cercano y mspacman
		 distancePPill_EdibleGhost = game.getShortestPathDistance(indexEdibleGhost, indexPpill); 					//distancia fantasma  comible mas cercano y powerpill
		}
		
		if (nearestChasingGhost!=null && nearestEdibleGhost!=null)// hay ghost comibles y no comibles cerca
		{
			distanceEdibleGhost_ChasingPacman = game.getShortestPathDistance(indexChasingGhost, indexEdibleGhost);
			
			if(dangerCage!=-1)   //peligro con carcel
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), carcel, DM.PATH);  //huir carcel (posicion carcel)
			else if(isPosibleToChaseEdibleGhost(game,nearestEdibleGhost, distanceEdibleGhost_MsP,distanceEdibleGhost_ChasingPacman, distanceChasingGhost_MsP, limit, dangerCage ))  //perseguir si es posible ghost comible mas cercano
					return getBestMoveTowardsGhost(game, limit, distancePPill_MsP,indexEdibleGhost, indexPpill, distancePPill_EdibleGhost );//return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexEdibleGhost, DM.PATH);
			else if (mustRunAwayFromChasingGhost(distanceEdibleGhost_MsP, distanceChasingGhost_MsP,distanceEdibleGhost_ChasingPacman, limit ))  //huir ghost no comible mas cercano pq esta cerca
				return getBestRunAwayGhost(game, limit*0.85, distancePPill_MsP, indexChasingGhost, indexPpill, distancePPill_ChasingGhost); //game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), indexChasingGhost,DM.EUCLID);
			else  //mejor movimiento hacia ghost comible
				return getBestMoveTowardsGhost(game, limit, distancePPill_MsP,indexEdibleGhost, indexPpill, distancePPill_EdibleGhost );
			
			
		}
		else if (nearestChasingGhost!=null ) //hay un ghost no comible cerca
		{

			if(dangerCage!=-1) //peligro carcel
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), carcel, DM.PATH); 		//huir carcel
			else if(moveTowardsPpill(game, mspacman, limit, indexPpill, distancePPill_MsP, distanceChasingGhost_MsP, distancePPill_ChasingGhost))
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPpill, DM.PATH); //ir hacia ppill
			else //huir mejor manera ghost perseguidor
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexChasingGhost, indexPpill, distancePPill_ChasingGhost);
			
		}
		else if (nearestEdibleGhost!=null){  //hay un ghost comible cerca
			indexChasingGhost = -1;
			if(dangerCage!=-1) //la carcel es peligrosa 
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), carcel, DM.PATH);  //huir carcel
			else if (game.getGhostEdibleTime(nearestEdibleGhost)>= distanceEdibleGhost_MsP && dangerCage==-1) //no peligrosa ir hacia fantasma comible
				return getBestMoveTowardsGhost(game, limit, distancePPill_MsP,indexEdibleGhost, indexPpill, distancePPill_EdibleGhost); 
			else //huir fantasma perseguidor
				return getBestRunAwayGhost(game, limit, distancePPill_MsP, indexChasingGhost, indexPpill, distancePPill_ChasingGhost);	
		}
		else {
			//ir a por las pills sin una powerpill cerca
			int indexpill = getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, -1);
			if(dangerCage!=-1)   //peligro carcel entonces huimos de ella
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), carcel, DM.PATH);
			else  //si no vamos a por pill
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexpill, DM.PATH);
		}
			
				
	}
	//funcion que nos dice si es posible perseguir un ghost comible
	private boolean isPosibleToChaseEdibleGhost(Game game, GHOST nearestEdibleGhost, int distanceEdibleGhost_MsP, int distanceEdibleGhost_ChasingPacman, int distanceChasingGhost_MsP,int limit, int dangerCage) {
		return (game.getGhostEdibleTime(nearestEdibleGhost)>= distanceEdibleGhost_MsP&&(distanceEdibleGhost_MsP<distanceEdibleGhost_ChasingPacman  || distanceEdibleGhost_MsP< distanceChasingGhost_MsP) && distanceChasingGhost_MsP >limit && dangerCage==-1);
	}
	//funcion que nos avisa de correr de un fantasma no comible
	private boolean mustRunAwayFromChasingGhost(int distanceEdibleGhost_MsP, int distanceChasingGhost_MsP, int distanceEdibleGhost_ChasingPacman,int limit ) {
		return distanceEdibleGhost_MsP>distanceChasingGhost_MsP || distanceEdibleGhost_MsP< distanceEdibleGhost_ChasingPacman || distanceChasingGhost_MsP < limit*0.85;
	}
	
	//funcion que nos dice si ir hacia la powerpill o no
	private boolean moveTowardsPpill(Game game, int mspacman, double limit, int indexPpill, int distancePPill_MsP, int distanceChasingGhost_MsP, int distancePPill_ChasingGhost) {
		int count=0;
		for(Constants.GHOST g: Constants.GHOST.values()) { //miramos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(d<=limit&& !game.isGhostEdible(g)) //menor distancia no negativa y fantasma no comible
			{	
				count++;
			}
		}
		
		return count>=3&&indexPpill!=-1&&(distancePPill_MsP < distanceChasingGhost_MsP) && distancePPill_ChasingGhost<= distancePPill_MsP*4;}
	
	//buscar el ghost mas cercano que no sea comible
	private GHOST getNearestChasingGhost(double e, Game game) {
		Random rnd = new Random();
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) { //miramos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			
			double d = game.getShortestPathDistance(mspacman, ghost);
			if((distance > d ||(distance == d && rnd.nextFloat()<0.7))&& d>0 && d<=e&& !game.isGhostEdible(g)) //menor distancia no negativa y fantasma no comible
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
		Random rnd =new Random();
		GHOST ghostType = null;
		double distance = Integer.MAX_VALUE;
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(Constants.GHOST g: Constants.GHOST.values()) {  //buscamos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			
		
			double d = game.getShortestPathDistance(mspacman, ghost);
			if((distance > d && d>0 ||(distance == d && rnd.nextFloat()<0.7)) && d<= limit && game.isGhostEdible(g))  //ghost con distancia mas cercana(no negativa)comible 
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
		Random rnd = new Random();
		int limit=25;
		double distance = Integer.MAX_VALUE;
		int [] pillInd = game.getActivePillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		MOVE badMove= MOVE.NEUTRAL;
		
		if(indexChasing!=-1) {
			for(MOVE nextMove: game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade())) {   //recorremos los posibles movimientos del Mspacman
				int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
				
				if(game.getShortestPathDistance(newNodeMsPacMan, indexChasing)< game.getShortestPathDistance(mspacman, indexChasing))
					badMove=nextMove;
			}
		}
			//badMove = game.getNextMoveTowardsTarget(mspacman, indexChasing, game.getPacmanLastMoveMade(), DM.PATH);
		for(int pillIndex: pillInd) {

			double d = game.getShortestPathDistance(mspacman, pillIndex);
			
			double distancePP = game.getShortestPathDistance(pillIndex, indexPP);
			
			if((distance > d && d>0 ||(distance == d && rnd.nextFloat()<0.7))&& distancePP>limit && indexChasing==-1)  //actualizar si distancia es menor o si es igual al azar si distancia ppill es mayor que limite e indexcahsing !=-1
			{	
				distance =d;
				finalPill = pillIndex;
			}
			//actualizar si distancia es menor o si es igual al azar si distancia ppill es mayor que limite y movimiento no es malo
			else if((distance > d ||(distance == d && rnd.nextFloat()<0.4)) && d>0 && distancePP>limit&& !game.getApproximateNextMoveTowardsTarget(mspacman, pillIndex, game.getPacmanLastMoveMade(), DM.PATH).equals(badMove))
			{	
				distance =d;
				finalPill = pillIndex;
			}
		}
		
		return finalPill;
	}
	
	//metodo que nos devuelve la pill mas cercana  sin acercanos a un ghost
	private int getNearestPillWithoutChasingGhost(Game game,  int indexChasing) {
		Random rnd = new Random();
		double distance = Integer.MAX_VALUE;
		int [] pillInd = game.getActivePillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		MOVE badMove= MOVE.NEUTRAL;
		
		if(indexChasing!=-1)
			badMove = game.getMoveToMakeToReachDirectNeighbour(mspacman, indexChasing);
		for(int pillIndex: pillInd) {

			double d = game.getShortestPathDistance(mspacman, pillIndex);

			
			if((distance > d && d>0 ||(distance == d && rnd.nextFloat()<0.7)) &&  indexChasing==-1) //actualizar si distancia es menor o si es igual al azar si distancia ppill es mayor que limite e indexcahsing !=-1
			{	
				distance =d;
				finalPill = pillIndex;
			}
			//actualizar si distancia es menor o si es igual al azar si distancia ppill es mayor que limite y movimiento no es malo
			else if((distance > d ||(distance == d && rnd.nextFloat()<0.7)) && d>0 && !game.getApproximateNextMoveTowardsTarget(mspacman, pillIndex, game.getPacmanLastMoveMade(), DM.PATH).equals(badMove))
			{	
				distance =d;
				finalPill = pillIndex;
			}
		}
		
		return finalPill;
	}
	
	//funcion que busca la powerpill mas cercana
	private int getNearestPowerPill(Game game) {
		Random rnd = new Random();
		double distance = Integer.MAX_VALUE;
		int [] powerPillInd = game.getActivePowerPillsIndices();
		int mspacman = game.getPacmanCurrentNodeIndex();
		int finalPill=-1;
		
		for(int ppillIndex: powerPillInd) {  //miramos todas las powerpills 

			double d = game.getShortestPathDistance(mspacman, ppillIndex);
			if((distance > d ||(distance == d && rnd.nextFloat()<0.5) )&& d>0  )  //si esta mas cerca(distancia no negativa) lo elegimos. Si esta a igual distancia elegimos al azar
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
			if(timeCage > game.getDistance(game.getPacmanCurrentNodeIndex(), carcel, DM.EUCLID) && timeCage!=0 && timeCage<limit)
				return carcel;
		}
		return -1;
	}
	
	//Elegir el mejor movimiento huyendo de los ghosts dentro de un rango (limit)
	private MOVE getBestRunAwayGhost( Game game, double limit, int distancePPill, int indexNearestChasingP, int indexPpill, int distancePPill_ChasingGhost) {
		Random rnd = new Random();
		int mspacman = game.getPacmanCurrentNodeIndex();   //posicion actual Mspacman
		double distance = Integer.MIN_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		int cont=0;
		MOVE[] m = game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade()); //posibles movimientos del Mspacman
		boolean good_move=false;
		int index_pill=  getNearestPillWithoutChasingGhost(game, indexNearestChasingP); 
		MOVE mov = game.getNextMoveTowardsTarget(mspacman, index_pill, DM.EUCLID);
		boolean okGoToPPill = moveTowardsPpill(game, mspacman, limit*1.35, indexPpill, distancePPill, indexNearestChasingP, distancePPill_ChasingGhost);
		for(MOVE nextMove: m) {   //recorremos los posibles movimientos del Mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;cont=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(!game.isGhostEdible(g) && game.getShortestPathDistance(newNodeMsPacMan, ghost)<=limit) {//buscamos las distancias desde el mspacman hacia los ghosts
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
					cont++;
				}
			}
			//actualizar si distancia es menor(no negativa) o si es igual al azar. si distancia ppill es mayor que 30 o menor que 25 y se puede ir a ppill
			if((distance < d ||(distance == d && rnd.nextFloat()<0.6))&& d>0 &&(distancePPill>30 || (distancePPill<=25 && okGoToPPill)) )  //si la distancia es mayor (y no negativa) que la que teniamos hacemos ese movimiento, nos aleja mas de todos los ghosts
			{	
				distance =d;
				finalMove = nextMove;
			}
			//buen movimiento si se cumple las condiciones
			if((game.getActivePowerPillsIndices().length==0 || (distance>0 && distance <30) || distance/cont<=25) && nextMove.equals(mov)) {
				good_move=true;
				
			}
			
		}
		if(indexNearestChasingP!=-1)
			if((mspacman==153 || mspacman==237) && game.isJunction(mspacman) && distancePPill>limit && game.getNextMoveTowardsTarget(mspacman, indexNearestChasingP, DM.PATH)!=game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),carcel, DM.PATH) )
				finalMove = MOVE.DOWN;
		if(good_move)finalMove = mov;
	
		if(finalMove==MOVE.NEUTRAL)
			finalMove = game.getNextMoveTowardsTarget(mspacman,getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, indexNearestChasingP), DM.PATH);
		return finalMove;
	}
	
	//Elegir el mejor movimiento hacia los ghost en un rango
	private MOVE getBestMoveTowardsGhost(Game game, double limit, int distancePPill, int indexNearestEdible,int indexPpill, int distancePPill_EdibleGhost) {
		Random rnd = new Random();
		int mspacman = game.getPacmanCurrentNodeIndex(); 
		double distance = Integer.MAX_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		int index_pill=  getNearestPillWithoutChasingGhost(game, -1); 
		boolean good_move=false;
		MOVE[] m =  game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade());
		MOVE mov = game.getNextMoveTowardsTarget(mspacman, index_pill, DM.EUCLID);
		boolean okGoToPPill = moveTowardsPpill(game, mspacman, limit, indexPpill, distancePPill, indexNearestEdible, distancePPill_EdibleGhost);
		
		
		for(MOVE nextMove: m) {  //recorremos los posibles movimientos del mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {   //buscamos las distancias desde el mspacman hacia los ghosts
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(game.isGhostEdible(g) &&game.getShortestPathDistance(newNodeMsPacMan, ghost) <=limit*1.5)
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				
			}
			if((distance > d ||(distance == d && rnd.nextFloat()<0.7))&& d>=0 &&(distancePPill>30 || (distancePPill<=25 && okGoToPPill))) //si la distancia d es menor (y no negativa) que la que teniamos, hacemos ese movimiento. Nos acerca mas a los ghost comibles
			{						//dentro de un rango
				distance =d;
				finalMove = nextMove;
			}
			if((game.getActivePowerPillsIndices().length==0 || (distance>0 && distance <30)) && nextMove.equals(mov)) {
				good_move=true;
				
			}
		}
		//if(distance == Integer.MAX_VALUE)
		//	finalMove = game.getNextMoveTowardsTarget(mspacman, indexNearestEdible, DM.PATH);
		if(good_move)finalMove = mov;
		if(finalMove==MOVE.NEUTRAL)
			finalMove = game.getNextMoveTowardsTarget(mspacman,getNearestPillWithoutPPillAndWithoutChasingGhost(game,indexPpill, indexNearestEdible), DM.PATH);
		
		return finalMove;
	}
	
	
	
}