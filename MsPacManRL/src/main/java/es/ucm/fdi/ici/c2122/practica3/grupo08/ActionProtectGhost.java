package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.awt.Color;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

//Accion para proteger el fantasma comestible mas cercano
public class ActionProtectGhost implements RulesAction {

	GHOST ghost;
	boolean DEBUG = false;
	
	public ActionProtectGhost(GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return "Action Protect Ghost: " + ghost;
	}

	@Override
	public MOVE execute(Game game) {
		double minDistance = 999999.;
		int closestGhost = -1;
		
		//Buscamos el fantasma comestible mas cercano
		for(GHOST otherGhost : GHOST.values()) {
			if(game.isGhostEdible(otherGhost) && game.getGhostLairTime(otherGhost) <= 0 &&
			   this.ghost != otherGhost) {
				double ghostDistance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
						game.getGhostCurrentNodeIndex(otherGhost), game.getGhostLastMoveMade(ghost));			
				if(ghostDistance < minDistance) {
					minDistance = ghostDistance;
					closestGhost = game.getGhostCurrentNodeIndex(otherGhost);
				}
			}		
		}	
		
		if(DEBUG)
			GameView.addPoints(game, Color.blue, game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), closestGhost, game.getGhostLastMoveMade(ghost)));
		
		//Nos acercamos al fantasma comestible mas cercano para protegerlo
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				closestGhost, game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
