package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.Pair;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.SortedArrayList;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends CBRInput {

	public GhostsInput(Game game, GHOST ghostType) {
		super(game);
		this.ghostType = ghostType;
	}
	
	Integer posGhosts;

	SortedArrayList< Pair<MOVE>> positionGhost;
	SortedArrayList< Pair<Integer>> edibleTimeGhosts;
	
	String posGhost1;
	String posGhost2;
	String posGhost3;
	String posPacman;
	
	String timeGhost1;
	String timeGhost2;
	String timeGhost3;
	
	Integer timeGhost;
	GHOST ghostType;
	
	MOVE lastGhostsMove;
	
	int lives;
	int level;
	
	String nearestPPill;
	Integer score;
	Integer time;
	
	
	/**
	 * Obtiene la posición relativa de un fantasma a pacman, si el fantasma no es comestible la posición relativa va de fantasma-pacman 
	 * Si el fantasma es comestible entonces es de pacman-fantasma
	 * Devuelve el movimiento desde pacman que habría que hacer para ir en la dirección del fantasma 
	 * @param pacman
	 * @param ghost
	 * @param move
	 * @param edible
	 * @return movimiento
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
		//si el nextNode no coincide con la posicion de pacman devolvemos el movimiento para alcanzarlo
		if(pacman != nextNode) {
			return game.getMoveToMakeToReachDirectNeighbour(pacman, nextNode);
		}else {
			return MOVE.NEUTRAL;
		}
	}
	
	public void setGhost(GHOST ghostType) {
		this.ghostType = ghostType;
	}
	
	@Override
	//parsea la entrada
	public void parseInput() {

		if(ghostType !=  null) {
			positionGhost = new SortedArrayList< Pair<MOVE>>();
			edibleTimeGhosts = new SortedArrayList< Pair<Integer>>();
			
			//La junction sobre la que se encuentra un fantasma
			posGhosts = game.getGhostCurrentNodeIndex(ghostType);
			
			//Ultimo movimiento del fantasma que estamos considerando
			lastGhostsMove = game.getGhostLastMoveMade(ghostType);
			
			int pacman = game.getPacmanCurrentNodeIndex();
			MOVE lastPacmanMove = game.getPacmanLastMoveMade();
			//inicializamos los arrayList
			for(GHOST ghostType: GHOST.values()) {

				if(ghostType != this.ghostType) {
					if(game.getGhostLairTime(ghostType) >0) { // si no es este fantasma y esta en la guarida lo reflejamos en las variables
						double distance = Double.MAX_VALUE;
						positionGhost.add(new Pair<MOVE>(distance,  MOVE.NEUTRAL));
						edibleTimeGhosts.add(new Pair<Integer>(distance, 0));
						
					}else {
						
						double distance =  game.getDistance(pacman, game.getGhostCurrentNodeIndex(ghostType), lastPacmanMove, DM.PATH);
						if(!game.isGhostEdible(ghostType)) {
							positionGhost.add(new Pair<MOVE>(distance, getMoveRelative(pacman, game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType) , false)));
						}else {
							positionGhost.add(new Pair<MOVE>(distance, getMoveRelative(pacman, game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade(), true)));	
						}
						edibleTimeGhosts.add(new Pair<Integer>(distance, game.getGhostEdibleTime(ghostType)));
					
					}
				}else {//si es este fantasma no tenemos que guardarlo en la sorted arrayList
					
					timeGhost = game.getGhostEdibleTime(ghostType);
					double distPacman =  game.getDistance(pacman, game.getGhostCurrentNodeIndex(ghostType), lastPacmanMove, DM.PATH);
					
					if(!game.isGhostEdible(ghostType)) {
						posPacman = distPacman + " " + getMoveRelative(pacman, game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType) , false);
						
					}else {
						posPacman =  distPacman + " " +getMoveRelative(pacman, game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade(), true);
						
					}
					
				}
				
			}
			
			// obtenemos valores de atributos
			//distancia MOVE
			posGhost1 = positionGhost.get(0).a+" "+positionGhost.get(0).b;
			posGhost2 = positionGhost.get(1).a+" "+positionGhost.get(1).b;
			posGhost3 = positionGhost.get(2).a+" "+positionGhost.get(2).b;
			
			//distancia Tiempo
			timeGhost1 = edibleTimeGhosts.get(0).a+" "+edibleTimeGhosts.get(0).b;
			timeGhost2 = edibleTimeGhosts.get(1).a+" "+edibleTimeGhosts.get(1).b;
			timeGhost3 = edibleTimeGhosts.get(2).a+" "+edibleTimeGhosts.get(2).b;
			
			lives = game.getPacmanNumberOfLivesRemaining();
			level = game.getCurrentLevel();
			
			computeNearestPPill(game, game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
			time = game.getTotalTime();
			score = game.getScore();
		}
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();
		
		description.setPosGhost(posGhosts);
		description.setLastGhostMove(lastGhostsMove);
		description.setNearestPPill(nearestPPill);
		
		description.setPosGhost1(posGhost1);
		description.setPosGhost2(posGhost2);
		description.setPosGhost3(posGhost3);
		description.setPosPacman(posPacman);
		
		description.setTimeGhost1(timeGhost1);
		description.setTimeGhost2(timeGhost2);
		description.setTimeGhost3(timeGhost3);
		description.setTimeGhost(timeGhost);
		description.setGhostType(ghostType);
		
		description.setScore(score);
		description.setTime(time);
		description.setLevel(level);
		description.setLives(lives);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	//actualiza el valor del atributo nearestPPill -> distancia MOVE
	private void computeNearestPPill(Game game, int pacman, MOVE move) {
		int nearestPPill = Integer.MAX_VALUE;
		MOVE m = MOVE.NEUTRAL;
		for(int pos: game.getActivePowerPillsIndices()) { // miramos indices de power pills y obtenemos la mas cercana
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, game.getPacmanLastMoveMade(), DM.PATH);
			if(distance < nearestPPill)
				nearestPPill = distance;
				m = game.getApproximateNextMoveTowardsTarget(pacman, pos, move, DM.PATH);
			
		}
		
		this.nearestPPill = nearestPPill + " "+ m;
	}
}
