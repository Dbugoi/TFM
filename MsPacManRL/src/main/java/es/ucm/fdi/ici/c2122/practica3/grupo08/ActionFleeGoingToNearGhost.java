package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.awt.Color;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

//Accion para huir de MsPacman acercandote a fantasmas cercanos no comestibles para protegerse
public class ActionFleeGoingToNearGhost implements RulesAction {

	GHOST _ghost;
	
	boolean DEBUG = false;
	 
	public ActionFleeGoingToNearGhost(GHOST ghost) {
		this._ghost = ghost;
	}
	@Override
	public String getActionId() {
		return "Action Protect Ghost: " + _ghost;
	}

	@Override
	public MOVE execute(Game game) {
		double minDistance = 999999.;
		int closestGhost = -1;
		
		for(GHOST otherGhost : GHOST.values()) {
			if(!game.isGhostEdible(otherGhost) && game.getGhostLairTime(otherGhost) <= 0 &&
				_ghost != otherGhost) {
				double ghostDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(_ghost),
						game.getGhostCurrentNodeIndex(otherGhost), game.getGhostLastMoveMade(_ghost));			
				if(ghostDistance < minDistance) {
					minDistance = ghostDistance;
					closestGhost = game.getGhostCurrentNodeIndex(otherGhost);
				}
			}		
		}	
		
		if(closestGhost == -1) return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(_ghost),
				game.getPacmanCurrentNodeIndex(),
				game.getGhostLastMoveMade(_ghost),
				DM.PATH);
		
		if(DEBUG && game.getGhostLairTime(_ghost) <= 0 && closestGhost != -1)
			GameView.addPoints(game, Color.white, game.getShortestPath(game.getGhostCurrentNodeIndex(_ghost), closestGhost, game.getGhostLastMoveMade(_ghost)));
		
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(_ghost),
				closestGhost, game.getGhostLastMoveMade(_ghost), DM.PATH);
	}
	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
