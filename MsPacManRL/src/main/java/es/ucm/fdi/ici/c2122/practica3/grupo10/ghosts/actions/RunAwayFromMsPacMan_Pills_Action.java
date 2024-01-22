package es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayFromMsPacMan_Pills_Action implements RulesAction {

	GHOST g;
	DM dm =  DM.PATH;
	int threshold = 90;
	public RunAwayFromMsPacMan_Pills_Action(GHOST ghost) {
		this.g = ghost;
	}



	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(g))        //if it requires an action
		{

			MOVE runaway_pacman_move = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(g),
					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), dm);

			GHOST nearest_ghost = getNearestGhost(game);
			if(nearest_ghost != null) {
				MOVE runaway_ghost_move = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(g),
						game.getGhostCurrentNodeIndex(nearest_ghost), game.getGhostLastMoveMade(g), dm);
				int distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g),game.getPacmanLastMoveMade());

				return calculaMovimientoSeguro(distance,runaway_pacman_move,runaway_ghost_move, null);
			}
			else
				return runaway_pacman_move;
			
		}

		return MOVE.NEUTRAL;	
	}

	private GHOST getNearestGhost(Game game) {
		GHOST ret =null;
		double min_dist = Double.MAX_VALUE;
		for(GHOST ghost: GHOST.values()) {
			if(ghost!=g && game.getGhostLairTime(ghost)<=0) {
				double dist = game.getDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(g),dm);
				if(dist < min_dist) {
					min_dist = dist;
					ret=ghost;
				}
			}
		}
		return ret;

	}

	//Posible moves tiene movimientos posibles que hacer en orden decreciente de prioridad
	private MOVE calculaMovimientoSeguro(int distance_to_pacman,MOVE runaway_pacman_move, MOVE runaway_ghost_move,  MOVE runaway_pills_move) {
		if(runaway_pills_move == null) {
			if(runaway_pacman_move!= runaway_ghost_move) {
				if(distance_to_pacman > threshold)
					return runaway_ghost_move;
			}
			else {
				return runaway_pacman_move;
			}
		}
		else {//-----------------------------------********************************************************************TODO FAÑTA HACER QUE HUUYA DE PILSS

		}
		return runaway_pacman_move;
	}

	@Override
	public String getActionId() {
		return g+ "runsAway from MsPacMan,other ghosts and pills";
	}



	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
