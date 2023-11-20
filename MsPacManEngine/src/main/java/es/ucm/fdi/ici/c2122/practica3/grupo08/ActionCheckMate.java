package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Accion para dirigir a los fantasmas hacia los nodos del subgrafo de posibles jugadas de MsPacMan para acorralarla
public class ActionCheckMate implements RulesAction {

	GHOST _ghost;
	Ghosts _ghostsClass;
	Grafo _grafo;
	private ArrayList<Nodo> nodosSubgrafo;
	
	public ActionCheckMate(Ghosts ghostsClass, GHOST ghost) {
		this._ghost = ghost;
		this._ghostsClass = ghostsClass;
	}
	
	@Override
	public String getActionId() {
		return "Action CheckMate " + _ghost;
	}

	@Override
	public MOVE execute(Game game) {
		this._grafo = _ghostsClass.getGrafo();
		int pacManIndex = game.getPacmanCurrentNodeIndex();
		
		int nextPacManJunction = findJunction(pacManIndex, game.getPacmanLastMoveMade(), game);
		crearSubgrafo(nextPacManJunction, game.getPacmanLastMoveMade(), 3, game);
		
		Nodo currentGhostJunction = _grafo.getJunction(game.getGhostCurrentNodeIndex(_ghost));
		
		//Si el fantasma se encuentra en la siguiente interseccion de MsPacMan solo tiene que moverse hacia ella
		if(currentGhostJunction != null && currentGhostJunction.getIndex() == this.nodosSubgrafo.get(0).getIndex())
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(_ghost), pacManIndex, game.getGhostLastMoveMade(_ghost), DM.PATH);
		
		//Mirar para este fantasma cual es el nodo de menor nivel del subgrafo al que llega antes que MsPacMan
		for(Nodo nodo : this.nodosSubgrafo) {
			//Si llega antes que MsPacman vamos al nodo
			if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(_ghost), nodo.getIndex(), game.getGhostLastMoveMade(_ghost)) < game.getShortestPathDistance(pacManIndex, nodo.getIndex(), game.getPacmanLastMoveMade())) {
					return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(_ghost), nodo.getIndex(), game.getGhostLastMoveMade(_ghost), DM.PATH);
			}
		}
		
		//Si llega aqui es porque el fantasma no participa en el jaque mate
		return MOVE.NEUTRAL;
	}
	
	//Crea un subgrafo a partir de una posicion origen y un ultimo movimiento hecho
	private void crearSubgrafo(int originJunction, MOVE lastMoveMade, int depthLevel, Game game) {
		
		//------------------------------------------------Preparamos la creacion del grafo -------------------------------------------------------------
		//Reseteamos valores de visitado del grafo
		for(Nodo node : this._grafo.getGraph()) {
			node.setVisited(false);
		}
		
		//Calcular el movimiento con el que llegara MsPacMan a la siguiente interseccion para realizar el calculo del grafo
		MOVE moveToNextJunction = MOVE.DOWN; //Default value
		
		int nextJunctionMsPacMan = findJunction(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade(), game);
		int[] pathToJunction = game.getShortestPath(game.getPacmanCurrentNodeIndex(), nextJunctionMsPacMan);
		if(pathToJunction.length > 1) {
			MOVE targetMove = game.getMoveToMakeToReachDirectNeighbour(pathToJunction[pathToJunction.length - 2], pathToJunction[pathToJunction.length - 1]);
			moveToNextJunction = targetMove;
		}
		
		this.nodosSubgrafo = new ArrayList<Nodo>();
		//----------------------------------------------------------------------------------------------------------------------------------------------
		
		//Introducimos en la cola el siguiente nodo al que llegara MsPacMan
		Queue<Nodo> colaNodos = new LinkedList<Nodo>();
		Nodo firstNode = this._grafo.getJunction(originJunction);
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
					Nodo nextNode = this._grafo.getJunction(nextJunction);
					if(!nextNode.isVisited()) {
						nextNode.setVisited(true);
						nextNode.setLevel(node.getLevel() - 1);
						colaNodos.add(nextNode);
					}
						
				}
			}
		}
	}
	
	//Devuelve la posicion de la power pill mas cercana a MsPacman
	private int pacmanClosestPillIndex(double limit, Game game) {
		int[] activePowerPills = game.getActivePowerPillsIndices();
		double minDist = limit;
		int index = -1;
		//Se recorren las power pills
		for (int i = 0; i < activePowerPills.length; i++) {
			//Se calcula la distancia de MsPacman a la power pill
			double actualDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), 
					activePowerPills[i], DM.PATH);
			//Si es la mas cercana o la primera dentro del limite se guarda
			if(actualDistance < minDist) {
				minDist = actualDistance;
				index = activePowerPills[i];
			}
		}
		
		return index;
	}
	
	//Dado un nodo origen y una direccion, devuelve la siguiente interseccion a la que se llega
	private int findJunction(int origin, MOVE lastMove, Game game) {
		int[] neighbours = game.getNeighbouringNodes(origin, lastMove);
		if(neighbours.length > 1) {
			return origin;			
		}
		else {
			return findJunction(neighbours[0], game.getMoveToMakeToReachDirectNeighbour(origin, neighbours[0]), game);
		}
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
