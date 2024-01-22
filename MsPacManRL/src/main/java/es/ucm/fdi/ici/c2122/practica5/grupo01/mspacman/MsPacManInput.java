package es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.Pair;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.SortedArrayList;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	
	Integer posPacman;
	
	SortedArrayList< Pair<MOVE>> positionGhost;
	SortedArrayList< Pair<Integer>> edibleTimeGhosts;
	
	String posGhost1;
	String posGhost2;
	String posGhost3;
	String posGhost4;
	
	String timeGhost1;
	String timeGhost2;
	String timeGhost3;
	String timeGhost4;
	
	String closestPill;
	
	MOVE lastPacmanMove;
	
	int lives;
	int level;
	
	String nearestPPill;
	Integer score;
	Integer time;
	
	
	/**
	 * Obtiene la posición relativa de un fantasma a pacman
	 * @param pacman
	 * @param ghost
	 * @param m
	 * @param edible
	 * @return
	 */
	public MOVE getMoveRelative(int pacman, int ghost, MOVE m, boolean edible) {
		int nextNode = 0;
		//Si el fantasma no es comestible obtenemos el camino desde el fantasma
		if(!edible) {
			int[] path = game.getShortestPath(ghost, pacman, m);
			
			//Obtenemos el último nodo que concuerda con el primero de pacman para ir hacia el fantasma y miramos su posición relativa
			if(path.length > 1) {
				nextNode = path[path.length -2];
			}else {
				nextNode = ghost;
			}
			
		//Si es comestible devolvemos el movimiento desde MsPacman
		}else {
			MOVE nextMove = game.getApproximateNextMoveTowardsTarget(pacman, ghost, m, DM.PATH);
			nextNode = game.getNeighbour(pacman, nextMove);
		}
		//Si el nextnode no es la posicion de MsPacman devolvemos el movimiento para alcanzarlo
		if(pacman != nextNode) {
			return game.getMoveToMakeToReachDirectNeighbour(pacman, nextNode);
		}else {
			return MOVE.NEUTRAL;
		}
	}
	//Devuelve la distancia hacia la píldora más cercana y el movimiento para alcanzarla -> distancia MOVE
	public String getClosestPill(int pacman, int[] pills, MOVE move) {
		String pill = "";
		
		double min = Integer.MAX_VALUE;
		MOVE m = MOVE.NEUTRAL;
		for(Integer i: pills) {
			double temp = game.getDistance(pacman, i, move, DM.PATH);
			if(temp < min) {
				min = temp;
				m = game.getApproximateNextMoveTowardsTarget(pacman, i, move, DM.PATH);
			}
			
		}
		pill = min + " " + m;
		
		return pill;
	}
	
	@Override
	//parsea la entrada
	public void parseInput() {

		positionGhost = new SortedArrayList< Pair<MOVE>>();
		edibleTimeGhosts = new SortedArrayList< Pair<Integer>>();
		posPacman = game.getPacmanCurrentNodeIndex();
		lastPacmanMove = game.getPacmanLastMoveMade();
		//Inicializar arrayLists
		for(GHOST ghostType: GHOST.values()) {
			//para fantasma en guarida
			if(game.getGhostLairTime(ghostType) >0) {
				
				double distance = Double.MAX_VALUE;
				
				if(!game.isGhostEdible(ghostType)) {
					positionGhost.add(new Pair<MOVE>(distance, MOVE.NEUTRAL));
					
				}else {
					positionGhost.add(new Pair<MOVE>(distance,  MOVE.NEUTRAL));
					
				}
				
				edibleTimeGhosts.add(new Pair<Integer>(distance, 0));
				
			}else {//para fantasma fuera de guarida
				
				
				double distance =  game.getDistance(posPacman, game.getGhostCurrentNodeIndex(ghostType), lastPacmanMove, DM.PATH);
				
				if(!game.isGhostEdible(ghostType)) {
					positionGhost.add(new Pair<MOVE>(distance, getMoveRelative(posPacman, game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType) , false)));
					
				}else {
					positionGhost.add(new Pair<MOVE>(distance, getMoveRelative(posPacman, game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade(), true)));
					
				}
				
				edibleTimeGhosts.add(new Pair<Integer>(distance, game.getGhostEdibleTime(ghostType)));
			
			}
			
		}
		//posiciones -> distancia MOVE
		posGhost1 = positionGhost.get(0).a+" "+positionGhost.get(0).b;
		posGhost2 = positionGhost.get(1).a+" "+positionGhost.get(1).b;
		posGhost3 = positionGhost.get(2).a+" "+positionGhost.get(2).b;
		posGhost4 = positionGhost.get(3).a+" "+positionGhost.get(3).b;
		//tiempoe -> distancia tiempo
		timeGhost1 = edibleTimeGhosts.get(0).a+" "+edibleTimeGhosts.get(0).b;
		timeGhost2 = edibleTimeGhosts.get(1).a+" "+edibleTimeGhosts.get(1).b;
		timeGhost3 = edibleTimeGhosts.get(2).a+" "+edibleTimeGhosts.get(2).b;
		timeGhost4 = edibleTimeGhosts.get(3).a+" "+edibleTimeGhosts.get(3).b;
		
		this.closestPill = getClosestPill(posPacman, game.getActivePillsIndices(), lastPacmanMove);
		
		lives = game.getPacmanNumberOfLivesRemaining();
		level = game.getCurrentLevel();
		
		computeNearestPPill(game, this.posPacman, this.lastPacmanMove);
		time = game.getTotalTime();
		score = game.getScore();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		
		description.setPosPacman(posPacman);
		description.setLastPacmanMove(lastPacmanMove);
		description.setNearestPPill(nearestPPill);
		
		description.setPosGhost1(posGhost1);
		description.setPosGhost2(posGhost2);
		description.setPosGhost3(posGhost3);
		description.setPosGhost4(posGhost4);
		
		description.setTimeGhost1(timeGhost1);
		description.setTimeGhost2(timeGhost2);
		description.setTimeGhost3(timeGhost3);
		description.setTimeGhost4(timeGhost4);
		
		
		description.setScore(score);
		description.setTime(time);
		description.setLevel(level);
		description.setLives(lives);
		description.setClosestPill(closestPill);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	//guarda los datos de la PPill mas cercana(distancia, movimiento para alcanzarla) en la variable nearestPPill -> distancia MOVE
	private void computeNearestPPill(Game game, int pacman, MOVE move) {
		
		int nearestPPill = Integer.MAX_VALUE;
		MOVE m = MOVE.NEUTRAL;
		for(int pos: game.getActivePowerPillsIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, game.getPacmanLastMoveMade(), DM.PATH);
			if(distance < nearestPPill)
				nearestPPill = distance;
				m = game.getApproximateNextMoveTowardsTarget(pacman, pos, move, DM.PATH);
			
		}
		
		this.nearestPPill = nearestPPill + " "+ m;
	}
}
