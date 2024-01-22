package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica3.grupo08.Grafo;
import es.ucm.fdi.ici.c2122.practica3.grupo08.Nodo;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsInput extends es.ucm.fdi.ici.rules.RulesInput {

	
	private boolean DEBUG;
	private double minDistanceNearGhost;
	
	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	
	private boolean BLINKYnearbyGhost;
	private boolean INKYnearbyGhost;
	private boolean PINKYnearbyGhost;
	private boolean SUEnearbyGhost;
	
	private boolean anyGhostEdible;
	private boolean anyPPillLeft;
	private double minPacmanDistancePPill;
	private double minBlinkyDistancePPill;
	private double minPinkyDistancePPill;
	private double minInkyDistancePPill;
	private double minSueDistancePPill;
	private double minSueDistanceMsPacman;
	private double minPacmanDistanceToNextJunction;
	private double minBlinkyDistanceToNextJunction;
	private double minPinkyDistanceToNextJunction;
	private double minInkyDistanceToNextJunction;
	private double minSueDistanceToNextJunction;
	
	private boolean isCheckMate;
	
	private ArrayList<Nodo> nodosSubgrafo;
	
	private Grafo grafo;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		DEBUG = false;
		minDistanceNearGhost = 30;
				
		//------------------------------------- Grafo MsPacman -----------------------------------------
		generaGrafo(this.game);
		
		int pacMan = this.game.getPacmanCurrentNodeIndex();
		MOVE lastPacManMove = this.game.getPacmanLastMoveMade();
		//Siguiente interseccion a la que llegara MsPacMan
		int nextPacManJunction = findJunction(pacMan, lastPacManMove);
		
		if(this.grafo != null) {
			crearSubgrafo(nextPacManJunction, this.game.getPacmanLastMoveMade(), 2);	
			
			if(this.DEBUG) {
				dibujar_subgrafo();
			}
			
			//-------------------------------------- Condicion de JaqueMate ---------------------------------
			
			this.isCheckMate = lookforCheckMate();
		}		
		
		//-------------------------------------- Fantasmas que son comestibles --------------------------
		this.BLINKYedible = this.game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = this.game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = this.game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = this.game.isGhostEdible(GHOST.SUE);
		
		if(this.BLINKYedible || this.INKYedible || this.PINKYedible || this.SUEedible) this.anyGhostEdible = true;
		
		//-------------------------------------- Fantasmas cercanos -------------------------------------
		this.BLINKYnearbyGhost = hasNearbyEdibleGhost(GHOST.BLINKY);
		this.INKYnearbyGhost = hasNearbyEdibleGhost(GHOST.INKY);
		this.PINKYnearbyGhost = hasNearbyEdibleGhost(GHOST.PINKY);
		this.SUEnearbyGhost = hasNearbyEdibleGhost(GHOST.SUE);
		
		//Nodo de MsPacMan
		int pacman = this.game.getPacmanCurrentNodeIndex();
		
		//----------------------- Distancia de MsPacman a PPill mas cercana -----------------------------
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		int closestPPillMsPacman = -1;
		for(int ppill: this.game.getActivePowerPillsIndices()) {
			double distance = this.game.getDistance(pacman, ppill, DM.PATH);
			if(distance < this.minPacmanDistancePPill) {
				closestPPillMsPacman = ppill;
				this.minPacmanDistancePPill = distance;
				
			}
		}
		
		//----------------------------------- Particulares de cada fantasma ------------------------------
		this.anyPPillLeft = (pacmanClosestPillIndex(60) != -1);
		
		int pinky = this.game.getGhostCurrentNodeIndex(GHOST.PINKY);
		int blinky = this.game.getGhostCurrentNodeIndex(GHOST.BLINKY);
		int inky = this.game.getGhostCurrentNodeIndex(GHOST.INKY);
		int sue = this.game.getGhostCurrentNodeIndex(GHOST.SUE);
		
		if(closestPPillMsPacman != -1) {
			if(this.game.getGhostLairTime(GHOST.INKY) < 0)
				this.minInkyDistancePPill = this.game.getShortestPathDistance(inky, closestPPillMsPacman);
			
			if(this.game.getGhostLairTime(GHOST.PINKY) < 0)
				this.minPinkyDistancePPill = this.game.getDistance(pinky, closestPPillMsPacman, DM.PATH);
			
			if(this.game.getGhostLairTime(GHOST.BLINKY) < 0)
				this.minBlinkyDistancePPill = this.game.getDistance(blinky, closestPPillMsPacman, DM.PATH);
			
			if(this.game.getGhostLairTime(GHOST.SUE) < 0)
				this.minSueDistancePPill = this.game.getDistance(sue, closestPPillMsPacman, DM.PATH);
		}
		
		this.minSueDistanceMsPacman = this.game.getDistance(sue, pacman, DM.PATH);
		
		this.minPacmanDistanceToNextJunction = game.getDistance(pacman, nextPacManJunction, DM.PATH);
		this.minPinkyDistanceToNextJunction = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), nextPacManJunction, DM.PATH);
		this.minBlinkyDistanceToNextJunction = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), nextPacManJunction, DM.PATH);
		this.minInkyDistanceToNextJunction = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), nextPacManJunction, DM.PATH);
		this.minSueDistanceToNextJunction = game.getDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), nextPacManJunction, DM.PATH);
		
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		
		facts.add(String.format("(BLINKY (edible %s) (nearbyGhost %s))", this.BLINKYedible, this.BLINKYnearbyGhost));
		facts.add(String.format("(INKY (edible %s) (nearbyGhost %s) (minInkyDistancePPill %s))",
				this.INKYedible,
				this.INKYnearbyGhost,
				Double.toString(this.minInkyDistancePPill).replace(',', '.')));
		
		facts.add(String.format("(PINKY (edible %s) (nearbyGhost %s) (minPacmanDistanceToNextJunction %s) (minPinkyDistanceToNextJunction %s))",
				this.PINKYedible,
				this.PINKYnearbyGhost,
				Double.toString(this.minPacmanDistanceToNextJunction).replace(',', '.'),
				Double.toString(this.minPinkyDistanceToNextJunction).replace(',', '.')));
		
		facts.add(String.format("(SUE (edible %s) (nearbyGhost %s) (minSueDistanceMsPacman %s))",
				this.SUEedible,
				this.SUEnearbyGhost, 
				Double.toString(this.minSueDistanceMsPacman).replace(',', '.')));
		
		facts.add(String.format("(COMMON (anyGhostEdible %s) (minPacmanDistancePPill %s) (isCheckMate %s))",
				this.anyGhostEdible,
				Double.toString(this.minPacmanDistancePPill).replace(',', '.'),
				this.isCheckMate));

		return facts;
	}
	
	//Devuelve true si el fantasma tiene a otro fantasma comestible cerca
	private boolean hasNearbyEdibleGhost(GHOST ghost) {
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		
		for(GHOST g : GHOST.values()){
			if(g == ghost || !game.isGhostEdible(g)) continue;
			int distance = game.getShortestPathDistance(ghostIndex, game.getGhostCurrentNodeIndex(g), game.getGhostLastMoveMade(ghost));
			if(distance <= minDistanceNearGhost)
				return true;
		}
		
		return false;
	}
	
	//------------------------------------------- Funcion JaqueMate ----------------------------------------------------
	
	//Comprueba si se puede acorralar a MsPacMan por todas las salidas posobles
	private boolean lookforCheckMate() {		
		int pacManIndex = game.getPacmanCurrentNodeIndex();
		//Mirar para cada fantasma cual es el nodo de menor nivel del subgrafo al que llega antes que MsPacMan
		for(GHOST ghost : GHOST.values()) {
			if(game.getGhostLairTime(ghost) > 0 || game.isGhostEdible(ghost)) continue;
			int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
			for(Nodo nodo : this.nodosSubgrafo) {
				//Si llega antes que MsPacman cerramos el nodo
				if(game.getShortestPathDistance(ghostIndex, nodo.getIndex()) < game.getShortestPathDistance(pacManIndex, nodo.getIndex())) {
					nodo.setClosed(true);
				}
			}
		}
		
		//Ahora se comprueba si MsPacMan puede llegar a algun nodo hoja sin pasar por los nodos que han sido eliminados en la lista
		
		int originJunction = findJunction(pacManIndex, game.getPacmanLastMoveMade());
		int depthLevel = 3;
		
		//Reseteamos valores de visitado del grafo
		for(Nodo node : this.grafo.getGraph()) {
			node.setVisited(false);
		}
		//Calcular el movimiento con el que llegara MsPacMan a la siguiente interseccion para realizar el calculo del grafo
		MOVE moveToNextJunction = MOVE.DOWN; //Default value
		
		//Conseguimos la siguiente interseccion a la que llega MsPacMan
		int nextJunctionMsPacMan = findJunction(this.game.getPacmanCurrentNodeIndex(), this.game.getPacmanLastMoveMade());
		int[] pathToJunction = this.game.getShortestPath(this.game.getPacmanCurrentNodeIndex(), nextJunctionMsPacMan);
		if(pathToJunction.length > 1) {
			MOVE targetMove = this.game.getMoveToMakeToReachDirectNeighbour(pathToJunction[pathToJunction.length - 2], pathToJunction[pathToJunction.length - 1]);
			moveToNextJunction = targetMove;
		}
		
		//Introducimos en la cola el siguiente nodo al que llegara MsPacMan
		Queue<Nodo> colaNodos = new LinkedList<Nodo>();
		Nodo firstNode = this.grafo.getJunction(originJunction);
		firstNode.setVisited(true);
		colaNodos.add(firstNode);
		
		//Mientras que haya nodos en la cola de nodos los expandimos
		while(!colaNodos.isEmpty()) {
			Nodo node = colaNodos.poll();
			
			//Si el nodo esta cerrado no seguimos expandiendo
			if(node.isClosed()) continue;
			
			//Si llega a un nodo hoja significa que puede escapar de la situacion
			if(node.getLevel() == 0) return false;
			
			for(MOVE move : MOVE.values()) {
				int nextJunction = node.getNextJunction(move);
				//Controlamos que no expanda el nodo de donde viene MsPacMan para evitar que el grafo sea incorrecto
				if(nextJunction != -1 && !(node.getLevel() == depthLevel && move == moveToNextJunction.opposite())) {
					Nodo nextNode = this.grafo.getJunction(nextJunction);
					if(!nextNode.isVisited()) {
						nextNode.setVisited(true);
						colaNodos.add(nextNode);
					}
						
				}
			}
		}	
		
		//Si no ha llegado a ningun nodo hoja significa que no tiene escapatoria
		return true;
	}

	//------------------------------------------- Funciones Grafo -------------------------------------------------------
	
	//Genera el grafo de cruces del nivel actual
	private void generaGrafo(Game game) {
		int[] junctionIndices = this.game.getCurrentMaze().junctionIndices;
		this.grafo = new Grafo(junctionIndices.length);
			
		for(int i = 0; i < junctionIndices.length; ++i) {
			Nodo node = grafo.getNode(i);
			node.setIndex(junctionIndices[i]);
			completeNode(node);
		}
	}
		
	//Rellena la informacion de cruces a los que se llegan desde este nodo
	private void completeNode(Nodo junctionNode) {
		int[] neighbours = this.game.getNeighbouringNodes(junctionNode.getIndex());
			
		for(int neighbour : neighbours) {
			MOVE moveToDest = this.game.getMoveToMakeToReachDirectNeighbour(junctionNode.getIndex(), neighbour);
			int destJunction = findJunction(neighbour, moveToDest);
			junctionNode.setPath(moveToDest, destJunction);
		}
	}
		
	//Dado un nodo origen y una direccion, devuelve la siguiente interseccion a la que se llega
	private int findJunction(int origin, MOVE lastMove) {
		int[] neighbours = game.getNeighbouringNodes(origin, lastMove);
		if(neighbours.length > 1) {
			return origin;			
		}
		else {
			return findJunction(neighbours[0], this.game.getMoveToMakeToReachDirectNeighbour(origin, neighbours[0]));
		}
	}
		
	//Crea un subgrafo a partir de una posicion origen y un ultimo movimiento hecho
	private void crearSubgrafo(int originJunction, MOVE lastMoveMade, int depthLevel) {
			
		//------------------------------------------------Preparamos la creacion del grafo -------------------------------------------------------------
		//Reseteamos valores de visitado del grafo
		for(Nodo node : this.grafo.getGraph()) {
			node.setVisited(false);
		}
			
		//Calcular el movimiento con el que llegara MsPacMan a la siguiente interseccion para realizar el calculo del grafo
		MOVE moveToNextJunction = MOVE.DOWN; //Default value
			
		int nextJunctionMsPacMan = findJunction(this.game.getPacmanCurrentNodeIndex(), this.game.getPacmanLastMoveMade());
		int[] pathToJunction = this.game.getShortestPath(this.game.getPacmanCurrentNodeIndex(), nextJunctionMsPacMan);
		if(pathToJunction.length > 1) {
			MOVE targetMove = this.game.getMoveToMakeToReachDirectNeighbour(pathToJunction[pathToJunction.length - 2], pathToJunction[pathToJunction.length - 1]);
			moveToNextJunction = targetMove;
		}
			
		this.nodosSubgrafo = new ArrayList<Nodo>();
		//----------------------------------------------------------------------------------------------------------------------------------------------
			
		//Introducimos en la cola el siguiente nodo al que llegara MsPacMan
		Queue<Nodo> colaNodos = new LinkedList<Nodo>();
		Nodo firstNode = this.grafo.getJunction(originJunction);
		firstNode.setVisited(true);
		firstNode.setLevel(depthLevel);
		colaNodos.add(firstNode);
			
		//Mientras que haya nodos en la cola de nodos los expandimos
		while(!colaNodos.isEmpty()) {
			Nodo node = colaNodos.poll();
			//Si ya hemos visitado todos los niveles dejamos de expandir el subgrafo y registramos el nodo hoja
			nodosSubgrafo.add(node);
			if(node.getLevel() == 0) break;
				
			for(MOVE move : MOVE.values()) {
				int nextJunction = node.getNextJunction(move);
				//Controlamos que no expanda el nodo de donde viene MsPacMan para evitar que el grafo sea incorrecto
				if(nextJunction != -1 && !(node.getLevel() == depthLevel && move == moveToNextJunction.opposite())) {
					Nodo nextNode = this.grafo.getJunction(nextJunction);
					if(!nextNode.isVisited()) {
						nextNode.setVisited(true);
						nextNode.setLevel(node.getLevel() - 1);
						colaNodos.add(nextNode);
					}
							
				}
			}
		}
	}
		
	//Debug de los nodos del subgrafo con colores
	private void dibujar_subgrafo() {
		for(int i = 0; i < this.grafo.getSize(); ++i) {
			Nodo nodo = this.grafo.getNode(i);
			if(!nodo.isVisited())
				GameView.addPoints(this.game, Color.gray, nodo.getIndex());
			else {
				if(nodo.getLevel() == 0)
					GameView.addPoints(this.game, Color.red, nodo.getIndex(), nodo.getIndex());	
				else if(nodo.getLevel() == 1)
					GameView.addPoints(this.game, Color.yellow, nodo.getIndex(), nodo.getIndex());
				else if(nodo.getLevel() == 2)
					GameView.addPoints(this.game, Color.green, nodo.getIndex(), nodo.getIndex());
				else if(nodo.getLevel() == 3)
					GameView.addPoints(this.game, Color.white, nodo.getIndex(), nodo.getIndex());
					
				for(MOVE m : MOVE.values()) {
					if(nodo.getLevel() != 0 && nodo.getNextJunction(m) != -1)
						GameView.addLines(this.game, Color.blue, nodo.getIndex(), nodo.getNextJunction(m));						
				}
			}
		}
	}
		
	//Devuelve la posicion de la power pill mas cercana a MsPacman
	private int pacmanClosestPillIndex(double limit) {
		int[] activePowerPills = this.game.getActivePowerPillsIndices();
		double minDist = limit;
		int index = -1;
		//Se recorren las power pills
		for (int i = 0; i < activePowerPills.length; i++) {
			//Se calcula la distancia de MsPacman a la power pill
			double actualDistance = this.game.getDistance(this.game.getPacmanCurrentNodeIndex(), 
					activePowerPills[i], DM.PATH);
			//Si es la mas cercana o la primera dentro del limite se guarda
			if(actualDistance < minDist) {
				minDist = actualDistance;
				index = activePowerPills[i];
			}
		}
			
		return index;
	}

}
