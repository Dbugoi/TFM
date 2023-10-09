package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo08.FVector;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {
	
	public MsPacManInput(Game game) {
		super(game);
		
	}

	//Putuacion de la partida
	Integer score;
	//nivel de juego
	Integer level;
	//Vidas restantes de MsPacman
	Integer HPs;
	//Posicion actual de MsPacman
	Integer pos;

	//Cada vector tiene para cada direccion
	//Distancia a los fantasmas no comestibles mas cercanos en cada direccion
	FVector ghostDistances = new FVector(4);
	//Distancia a la PPill mas cercana en cada direccion
	FVector powerPillDistances = new FVector(4);
	//Distancia a la pill mas cercana en cada direccion
	FVector pillDistances = new FVector(4);
	//Distancia a los fantasmas comestibles mas cercanos en cada direccion
	FVector edibleGhostsDistances = new FVector(4);
	
	@Override
	public void parseInput() {
		
		this.ghostDistances = new FVector(4);
		this.powerPillDistances = new FVector(4);
		this.pillDistances = new FVector(4);
		
		this.edibleGhostsDistances = new FVector(4);
		
		this.score = game.getScore();
		this.level = game.getCurrentLevel();
		this.HPs = game.getPacmanNumberOfLivesRemaining();
		
		for(MOVE m : MOVE.values()) {
			if(m==MOVE.NEUTRAL) continue;
			this.ghostDistances.values[m.ordinal()] = -1;
			this.powerPillDistances.values[m.ordinal()] = -1;
			this.pillDistances.values[m.ordinal()] = -1;
			this.edibleGhostsDistances.values[m.ordinal()] = -1;
		}
		
		MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		for(MOVE m : possibleMoves) {
			lookForGhost(m);
			lookForPowerPill(m);
			lookForPill(m);	
		}
		
		this.pos = game.getPacmanCurrentNodeIndex();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		
		//Seteamos las variables de la descripcion con las recogidas en input
		description.score = this.score;
		description.level = this.level;
		description.HPs = this.HPs;
		description.pos = this.pos;
		
		description.ghostDistances = this.ghostDistances;
		description.powerPillDistances = this.powerPillDistances;
		description.pillDistances = this.pillDistances;
		description.edibleGhostsDistances = this.edibleGhostsDistances;		
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	/**
	 * Rellena el vector de distancias a fantasmas tanto comestibles como no comestibles
	 * buscando el fantasma mas cercano siguiendo la direccion m
	 */
	private void lookForGhost(MOVE m) {
		int minDistNoEdible = 153;
		int minDistEdible = 153;
		int initialMindist = minDistNoEdible;
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) <= 0) {
				int targetNode = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
				int[] path = game.getShortestPath(targetNode, game.getGhostCurrentNodeIndex(g), m);
				int dist = path.length;
				
				if(!game.isGhostEdible(g)) {
					if(dist < minDistNoEdible) minDistNoEdible = dist;					
				}
				else if(dist < minDistEdible) minDistEdible = dist;
			}
		}
		
		if(minDistNoEdible == initialMindist) this.ghostDistances.values[m.ordinal()] = -1;
		else this.ghostDistances.values[m.ordinal()] = minDistNoEdible;
		
		if(minDistEdible == initialMindist) this.edibleGhostsDistances.values[m.ordinal()] = -1;
		else this.edibleGhostsDistances.values[m.ordinal()] = minDistEdible;
	}
	
	/**
	 * Rellena el vector de distancias a las Power Pills
	 * buscando la mas cercana siguiendo la direccion m
	 */
	private void lookForPowerPill(MOVE m) {
		int minDist = 153;
		for(int p : game.getActivePowerPillsIndices()) {
			int targetNode = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
			int[] path = game.getShortestPath(targetNode, p, m);
			int dist = path.length;
			
			if(dist < minDist) minDist = dist;
		}
		this.powerPillDistances.values[m.ordinal()] = minDist;
	}
	
	/**
	 * Rellena el vector de distancias a las pills
	 * buscando la mas cercana siguiendo la direccion m
	 */
	private void lookForPill(MOVE m) {
		int minDist = 153;
		for(int p : game.getActivePillsIndices()) {
			int targetNode = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
			int[] path = game.getShortestPath(targetNode, p, m);
			int dist = path.length;
			
			if(dist < minDist) minDist = dist;
		}
		this.pillDistances.values[m.ordinal()] = minDist;
	}
	
}
