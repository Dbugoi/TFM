package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import es.ucm.fdi.ici.c2122.practica3.grupo08.Nodo;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

//Accion para dirigir al fantasma a la siguiente interseccion de MsPacman
public class ActionCloseWay implements RulesAction {

	GHOST ghost;
	Ghosts _ghostsClass;
	boolean DEBUG = false;
	
	public ActionCloseWay(GHOST ghost, Ghosts ghostsClass) {
		this.ghost = ghost;
		this._ghostsClass = ghostsClass;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MOVE execute(Game game) {
		MOVE move = MOVE.DOWN;
		int [] junctions = game.getJunctionIndices();
		double minDistance = 99999;
		int nextJunction = junctions[0];
		//Buscamos el cruce mas cercano a MsPacman teniendo en cuenta 
		for(int i : junctions) {
			double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), i, game.getPacmanLastMoveMade());
			double distanceGhost = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(this.ghost), i, game.getGhostLastMoveMade(this.ghost));
			//Se guarda el cruce mas cercano a MsPacman al cual llega antes pinky
			if(distance < minDistance &&  distance > distanceGhost ) {
				nextJunction = i;
				minDistance = distance;
			}
		}
		
		//Si pinky se encuentra en ese nodo va a por el pacman, sino se mueve al nodo
		if(game.getGhostCurrentNodeIndex(this.ghost) == nextJunction)	
			move = nextMoveTowardsIndex(game, this.ghost, game.getPacmanCurrentNodeIndex());
		else
			move = nextMoveTowardsIndex(game, this.ghost, nextJunction);
		
		return move;
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
	
	//Devuelve el siguiente mvto del fantasma hacia una posicion
	private MOVE nextMoveTowardsIndex(Game game, GHOST ghostType, int destination) {
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), destination,
				game.getGhostLastMoveMade(ghostType), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
