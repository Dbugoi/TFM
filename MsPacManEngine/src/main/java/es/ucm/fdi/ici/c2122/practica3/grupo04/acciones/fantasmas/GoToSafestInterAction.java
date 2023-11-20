package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToSafestInterAction implements RulesAction {

	GHOST ghost;
	public GoToSafestInterAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
		{
			return game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost),
					safestJunction(game, game.getGhostCurrentNodeIndex(ghost)),
					game.getGhostLastMoveMade(ghost),
					Constants.DM.PATH);
		}

		return MOVE.NEUTRAL;	
	}

	@Override
	public String getActionId() {
		return ghost+ "goesToSafestIntersection";
	}

	//devuelve la proxima interseccion de msPacMan
	private int safestJunction(Game game, int pos) {

		int mspacman = game.getPacmanCurrentNodeIndex();
		int[] juncs = CommonMethodsGhosts.getAdjacentJunctions(game, pos, game.getGhostLastMoveMade(ghost));

		//buscamos la junction cercana con menor peligro
		float dang = Float.MAX_VALUE;
		int safeJunc = juncs[0];
		float goAway=0.8f;//ratio para acercarse
		for(int j : juncs) {
			float damn = CommonMethodsGhosts.dangerLevel(game, j, 1, ghost);
			int newDist = game.getShortestPathDistance(j, mspacman);
			int oldDist = game.getShortestPathDistance(safeJunc, mspacman);
			if(newDist < oldDist && damn/dang <= goAway) { // Nos acercamos solo si la diferencia de peligros es baja
				dang = damn;								
				safeJunc = j;
			}
		}
		return safeJunc;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	
}
