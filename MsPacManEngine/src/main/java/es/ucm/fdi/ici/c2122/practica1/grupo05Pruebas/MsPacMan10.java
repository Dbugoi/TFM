package es.ucm.fdi.ici.c2122.practica1.grupo05Pruebas;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
 
public final class MsPacMan10 extends PacmanController {
	private int cage=492;
	private int limitForChasingGhost = 50;
	private int limitForEdibleGhost = 40;
	private int limitPPill = 30;

	
	@Override
	public MOVE getMove(Game game, long timeDue) {

		//Encontrar todos los ghost de cada tipo cercanos (dentro de un limite)
		List<Integer> listNearestChasingGhost = getNearestChasingGhosts(game, this.limitForChasingGhost);
		List<Integer> listNearestEdibleGhost = getNearestEdibleGhosts(game);
		
		int mspacman = game.getPacmanCurrentNodeIndex();
		//Se analiza que movimientos pueden llevar a un Chasing Ghost
		List<MOVE> goodMoves = getGoodMoves(game,listNearestChasingGhost, mspacman);

		if (!listNearestEdibleGhost.isEmpty() && !getBestMoveTowardsGhost(game, listNearestEdibleGhost, listNearestChasingGhost, goodMoves).equals(MOVE.NEUTRAL))// hay ghost comibles cerca
		{	//Usar los movimientos calculados arriba (good Moves) y ver cuales nos llevan mas cerca a un grupo de ghosts
			return getBestMoveTowardsGhost(game, listNearestEdibleGhost, listNearestChasingGhost, goodMoves);
		}

		else { //Si no hay ghost comibles cerca, se trataria de escapar de los ghost pero tratando de 
			//comer pills en el camino y si conviene, una powerPill
			
			//Ver si hay una PowePill cerca con la que se puede llegar usando goodMoves y analizar
			//si vale la pena ir a comerla (hay mas de 1 ghost potencial alrededor para comer)
			int pPill = getNearestPowerPill(game, mspacman, goodMoves);
			
			//Buscamos los ghost no comibles que estan muy cerca
			List<Integer> listNearestChasingGhost2 = getNearestChasingGhosts(game, 26);
			MOVE m=MOVE.NEUTRAL;
			if(pPill!=-1) //Movimiento para ir la powerPill
				m = game.getNextMoveTowardsTarget(mspacman, pPill, DM.PATH);
			
			//Se cumplen condiciones para comer la PowerPill
			if(pPill!=-1 && game.getShortestPathDistance(mspacman, pPill)<this.limitPPill && listNearestChasingGhost2.size()>=2 && !m.equals(MOVE.NEUTRAL))
				return m;
			else { //Si no, eliminar ese movimiento
				if(goodMoves.contains(m))
					goodMoves.remove(m);
			}
			
			//Ir por las pills
			int pill = getNearestPillWithoutChasingGhost(game, listNearestChasingGhost, goodMoves);
			if(pill!=-1) //Hay pills cerca, ir por ella
				return game.getNextMoveTowardsTarget(mspacman, pill, DM.PATH);
			else { //No hay nada, movimiento aleatorio
			Random rdn = new Random();
			if(!goodMoves.isEmpty())
				return goodMoves.get(rdn.nextInt(goodMoves.size()));
			else
				return MOVE.NEUTRAL;
			}
		}
			
				
	}
	
	
	
	//funcion que busca la powerpill mas cercana
		private int getNearestPowerPill(Game game,  int mspacman,  List<MOVE> goodMoves) {
			
			Random rnd = new Random();
			int finalPill=-1;
	
			double distance = Integer.MAX_VALUE;
		
			for(int ppillIndex: game.getActivePowerPillsIndices()) {  //miramos todas las powerpills 

				double d = game.getShortestPathDistance(mspacman, ppillIndex);
				if((distance > d ||(distance == d && rnd.nextFloat()<0.5) )&& d>0 && d<this.limitPPill&& goodMoves.contains(game.getNextMoveTowardsTarget(mspacman, ppillIndex, DM.PATH)) )  //si esta mas cerca(distancia no negativa) lo elegimos. Si esta a igual distancia elegimos al azar
				{	
					distance =d;
					finalPill = ppillIndex;
				}
			}
			
				return finalPill;
		}
	
		
	
	//Buscar los ghost no comibles problematicos
	private ArrayList<Integer> getNearestChasingGhosts(Game game, int limit) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		ArrayList<Integer> listIndex = new ArrayList<Integer>();
		for(Constants.GHOST g: Constants.GHOST.values()) { //miramos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(!game.isGhostEdible(g) && d<= this.limitForChasingGhost && game.getGhostLastMoveMade(g) .equals( game.getNextMoveTowardsTarget(ghost, mspacman, DM.PATH))) //GHOST ME PERSIGUE
				listIndex.add(ghost);
			if(game.getGhostLairTime(g)<game.getShortestPathDistance(mspacman, cage) && game.getShortestPathDistance(mspacman, cage)<=this.limitForChasingGhost)
				listIndex.add(cage);
		}
		
		return listIndex;
	}
	
	private ArrayList<Integer> getNearestEdibleGhosts( Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		ArrayList<Integer> listIndex = new ArrayList<Integer>();
		for(Constants.GHOST g: Constants.GHOST.values()) { //miramos entre todos los ghosts
			int ghost = game.getGhostCurrentNodeIndex(g);
			double d = game.getShortestPathDistance(mspacman, ghost);
			if(game.isGhostEdible(g) && (game.getGhostEdibleTime(g)/2 + d<  game.getGhostEdibleTime(g)) && d<=this.limitForEdibleGhost)
				listIndex.add(ghost);
		}
		
		return listIndex;
	}
	
	private List<MOVE> getGoodMoves(Game game, List<Integer> listChasing , int mspacman){
		List<MOVE> goodMoves = new ArrayList<>();
		for(MOVE m: game.getPossibleMoves(mspacman))
			goodMoves.add(m);
		for (int index : listChasing) {
			MOVE m = game.getNextMoveTowardsTarget(mspacman, index, DM.PATH);
			if(goodMoves.contains(m))
				goodMoves.remove(m);
		}
		return goodMoves;
	}
	
	//metodo que nos devuelve la pill mas cercana  sin acercanos a un ghost
		private int getNearestPillWithoutChasingGhost(Game game,  List<Integer> listChasing, List<MOVE> goodMoves) {
			double distance = Integer.MAX_VALUE;
			int mspacman = game.getPacmanCurrentNodeIndex();
			int finalPill=-1;
			
			
			for(int pillIndex: game.getActivePillsIndices()) {
				double d = game.getShortestPathDistance(mspacman, pillIndex);

				if(distance > d && d>0 && goodMoves.contains(game.getNextMoveTowardsTarget(mspacman, pillIndex, DM.PATH) ) )
				{	
					distance =d;
					finalPill = pillIndex;
				}
				
			}
			
			return finalPill;
		}

	
	//Elegir el mejor movimiento hacia los ghost en un rango
	private MOVE getBestMoveTowardsGhost(Game game, List<Integer> listEdible, List<Integer> listChasing, List<MOVE> goodMoves) {
		
		int mspacman = game.getPacmanCurrentNodeIndex(); 
		double distance = Integer.MAX_VALUE;
		MOVE finalMove = MOVE.NEUTRAL;
		int indexNearestGhost = -1;
		int distMin = Integer.MAX_VALUE;

		for(MOVE nextMove: goodMoves) {  //recorremos los posibles movimientos del mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=0;
			for(Constants.GHOST g: Constants.GHOST.values()) {   //buscamos las distancias desde el mspacman hacia los ghosts
				int ghost = game.getGhostCurrentNodeIndex(g);
				int dist=game.getShortestPathDistance(newNodeMsPacMan, ghost);
				if(game.isGhostEdible(g) &&dist <=limitForEdibleGhost && goodMoves.contains(nextMove)) {
					d += dist;

					if(distMin>dist) {
						distMin = dist;
						indexNearestGhost = ghost;
					}
				}

			}
			//Si este movimiento nos acerca mas a todos los ghost comibles cercanos, hacer este movimiento. Si nos acerca a una PPill, revisar si valdria la pena
			if((distance > d ||(distance == d && nextMove.equals(game.getNextMoveTowardsTarget(mspacman, indexNearestGhost, DM.PATH))))&& d>=0 && goodMoves.contains(nextMove))// Distance que nos acerca mas a los ghost comibles
			{						//dentro de un rango
				distance =d;
				finalMove = nextMove;
			}

		}

		return finalMove;
	}
	
	
	
}