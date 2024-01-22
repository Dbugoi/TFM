package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeToFurthestGhost implements RulesAction {

	private double _dist;
	
	public ActionFleeToFurthestGhost(double limitDistance) {
		_dist = limitDistance;
	}
	
	@Override
	public MOVE execute(Game game) {
		//Guardamos en una lista los fantasmas cercanos no comestibles
		ArrayList<GHOST> ghostList = new ArrayList<GHOST>();		
		getAllNearChasingGhosts(game, ghostList);
		
		GHOST furthestGhost = getFurthestGhost(game, ghostList);
		MOVE move = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
				game.getGhostCurrentNodeIndex(furthestGhost), 
				DM.PATH);
		
		return move;
	}

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
	//Devuelve el fantasma mas lejano a MsPacman
	private GHOST getFurthestGhost(Game game, ArrayList<GHOST> ghostList) {
		double largestDistance = 0;
		GHOST furtherGhost = null;
		//Se recorren los fantasmas
		for (GHOST ghostType : ghostList) {
			//Si el fantasma esta activo y no es comestible
			if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
				//Se calcula su distancia con MsPacman
				int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade());
				//Si es el mas cercano se guarda
				if(path.length > largestDistance) {
					furtherGhost = ghostType;
					largestDistance = path.length;
				}
			}
		}	
		return furtherGhost;
	}
	
	@Override
	public String getActionId() {
		return "Flee to Furthest Ghost";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
