package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeToEdibleGhost implements RulesAction {

	private double _dist;
	
	public ActionFleeToEdibleGhost(double limitDistance) {
		_dist = limitDistance;
	}

	@Override
	public MOVE execute(Game game) {
		//Vemos si hay algún camino que lleva a algun fantasma
		MOVE nextMove = null; //Camino devuelto por la accion
		double shortestDistance = 999999.;//Distancia al fantasma comestible mas cercano
		//Fantasmas cercanos comestibles
		ArrayList<GHOST> nearEdibleGhosts = new ArrayList<GHOST>();
		getAllNearEdibleGhosts(game, nearEdibleGhosts);
		//Mvtos posibles
		ArrayList<MOVE> possibleMoves = checkPossibleMoves(game);
		//Se recorren los fantasmas comestibles cercanos
		for(GHOST ghost : nearEdibleGhosts) {
			//Si el camino que nos lleva al fantasma es el mas corto hasta el momento
			int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
			if(path.length < shortestDistance) {
				//Si el mvto hacia el fantasma comestible mas cercano hasta el momento es posible lo guardamos
				MOVE moveToNeighbour = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path[path.length - 1], DM.PATH);
				if(possibleMoves.contains(moveToNeighbour)) {
					shortestDistance = _dist;
					nextMove = moveToNeighbour;
				}					
			}
		}
		return nextMove;
	}
	
	private void getAllNearEdibleGhosts(Game game, ArrayList<GHOST> ghostList) {
		for (GHOST ghostType : GHOST.values()) {
			//Si el fantasma esta activo y es comestible
			if(game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
				//Se calcula su distancia con MsPacman
				double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				//Si la distancia se considera cercana se guarda el fantasma
				if(ghostDistance <= _dist) {
					ghostList.add(ghostType);
				}
			}
		}
	}
	
	private ArrayList<MOVE> checkPossibleMoves(Game game) {
		ArrayList<MOVE> possibleMoves = new ArrayList<MOVE>();
		ArrayList<GHOST> nearChasingGhosts =  new ArrayList<GHOST>();
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
		return "Flee to Edible Ghost";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

	

}