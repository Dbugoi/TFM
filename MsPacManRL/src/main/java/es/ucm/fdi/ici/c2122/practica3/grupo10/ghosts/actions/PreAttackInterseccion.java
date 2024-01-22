package es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PreAttackInterseccion implements RulesAction {

	GHOST g;
	int fase;
	int multiplicador = 20;
	public PreAttackInterseccion(GHOST ghost, int fase) {
		this.g=ghost;
		this.fase=fase;
	}


	@Override
	public MOVE execute(Game game) {
		if(game.doesGhostRequireAction(g)) {
			boolean finish = false;
			MOVE ret = null;
			while(!finish) {
				int[] junctions = game.getJunctionIndices();
				for(int i =0; i < junctions.length;i++) {
					int distance_to_junct = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), junctions[i], game.getPacmanLastMoveMade());
					int distance_ghost_junct = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), junctions[i], game.getGhostLastMoveMade(g));
					if(distance_to_junct <= fase * multiplicador) {
						ret= game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), junctions[i], game.getGhostLastMoveMade(g), DM.PATH);
						finish=true;
					}
				}
				multiplicador+=3;
				
			}
			multiplicador=20;
			return ret;
		}
		return MOVE.NEUTRAL;
		
		
	}

	@Override
	public String getActionId() {
		return g + "preatacando a mspacman";
	}


	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
