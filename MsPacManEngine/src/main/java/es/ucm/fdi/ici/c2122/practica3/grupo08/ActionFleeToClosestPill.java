package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeToClosestPill implements RulesAction{
	
	private double _dist;
	
	public ActionFleeToClosestPill(double limitDistance) {
		_dist = limitDistance;
	}
	
	@Override
	public MOVE execute(Game game) {
		int[] activePills = game.getActivePillsIndices();
		double minDistance = 999999.;
		MOVE nextMove = null;
		ArrayList<MOVE> possibleMoves = checkPossibleMoves(game);
		
		//Se recorren las pills activas
		for(int pill : activePills) {
			//Buscamos dentro de nuestros posibles movimientos el que nos lleve a la pill mas cercana
			int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), pill, game.getPacmanLastMoveMade());
			//Si la pill es mas cercana que la guardada
			if(path.length < minDistance) {
				//Si el mvto hacia la pill potencialmente mas cercana es posible
				MOVE moveToNeighbour = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path[path.length - 1], DM.PATH);
				if(possibleMoves.contains(moveToNeighbour)) {
					//Guardamos el mvto hacia la pill y su distancia
					nextMove = moveToNeighbour;
					minDistance = _dist;
				}					
			}
		}
		
		return nextMove;
	}
	
	private ArrayList<MOVE> checkPossibleMoves(Game game) {
		ArrayList<MOVE> possibleMoves = new ArrayList<MOVE>();
		ArrayList<GHOST> nearChasingGhosts = new ArrayList<GHOST>();
		getAllNearChasingGhosts(game, nearChasingGhosts);
		//Se recogen los posibles movimientos que puede hacer MsPacMan
		for(MOVE possibleM : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			possibleMoves.add(possibleM);
		}
		
		//Se quitan de los posibles movimientos aquellos que conducen a un fantasma cercano
		for(GHOST ghost : nearChasingGhosts) {
			if(game.isGhostEdible(ghost)) continue;
			int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost));
			if(path.length < 55)
				possibleMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH));
		}	
		return possibleMoves;
	}//checkPossibleMoves
	
	//Devuelve una lista de los fantasmas que se encuentran peligrosamente cerca de MsPacman
	private void getAllNearChasingGhosts(Game game, ArrayList<GHOST> ghostList) {
		for (GHOST ghostType : GHOST.values()) {
			//Si el fantasma esta activo y no es comestible
			if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
				//Se calcula su distancia con MsPacman
				double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				//Si la distancia se considera peligrosa segun el limite
				if(ghostDistance <= _dist) {
					ghostList.add(ghostType);
				}
			}
		}
	}	
	
	@Override
	public String getActionId() {
		return "Go to Closest Pill While Flee";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
