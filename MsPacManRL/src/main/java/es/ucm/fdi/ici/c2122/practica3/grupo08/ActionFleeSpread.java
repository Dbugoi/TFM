package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Accion para dispersar a los fantasmas mientras que huyen de MsPacman
public class ActionFleeSpread implements RulesAction {

	GHOST _ghost;
	public ActionFleeSpread(GHOST ghost) {
		this._ghost = ghost;
	}

	@Override
	public String getActionId() {
		return "Action Flee Spread: " + _ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if(!game.doesGhostRequireAction(_ghost)) return MOVE.NEUTRAL;
		
		ArrayList<MOVE> possibleMoves = new ArrayList<MOVE>();
		
		//Se recogen los posibles movimientos que puede hacer el fantasma
  		for(MOVE possibleM : game.getPossibleMoves(game.getGhostCurrentNodeIndex(_ghost), game.getGhostLastMoveMade(_ghost))) {
			possibleMoves.add(possibleM);
		}
		
		//Se quitan de los posibles movimientos aquellos que conducen a MsPacman
		int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(_ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(_ghost));
		if(path.length < 55) {
			possibleMoves.remove(game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(_ghost),game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(_ghost), DM.PATH));
		}
		
		//Se quitan de los posibles movimientos aquellos que conducen a un fantasma cercano comestible
		for(GHOST otherGhost : GHOST.values()) {
			if(!game.isGhostEdible(otherGhost) || otherGhost == _ghost) continue;
			path = game.getShortestPath(game.getGhostCurrentNodeIndex(_ghost), game.getGhostCurrentNodeIndex(otherGhost), game.getGhostLastMoveMade(_ghost));
			if(path.length < 55) {
				possibleMoves.remove(game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(_ghost),
						game.getGhostCurrentNodeIndex(otherGhost), game.getGhostLastMoveMade(_ghost), DM.PATH));
			}
		}	
		
		//Si para cualquier direccion se topa con un fantasma, elegimos la que lleva al fantasma mas lejano
		if(possibleMoves.isEmpty()) {
			GHOST furthestGhost = getFurthestGhost(game);
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(_ghost), 
					game.getGhostCurrentNodeIndex(furthestGhost), game.getGhostLastMoveMade(_ghost),
					DM.PATH);
		}			
		return possibleMoves.get(0);
	}
	
	//Devuelve el fantasma mas lejano a MsPacman
	private GHOST getFurthestGhost(Game game) {
		double largestDistance = 0;
		GHOST furtherGhost = null;
		//Se recorren los fantasmas
		for (GHOST otherGhost : GHOST.values()) {
			//Si el fantasma esta activo y no es comestible
			if(game.getGhostLairTime(otherGhost) <= 0 && otherGhost != _ghost) {
				//Se calcula su distancia con MsPacman
				double ghostDistance = game.getDistance(game.getGhostCurrentNodeIndex(_ghost), 
						game.getGhostCurrentNodeIndex(otherGhost), game.getPacmanLastMoveMade(), DM.PATH);
				//Si es el mas lejano se guarda
				if(ghostDistance > largestDistance) {
					furtherGhost = otherGhost;
					largestDistance = ghostDistance;
				}
			}
		}
		
		return furtherGhost;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
