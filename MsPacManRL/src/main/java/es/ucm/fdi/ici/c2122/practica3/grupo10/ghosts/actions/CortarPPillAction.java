package es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class CortarPPillAction implements RulesAction {

	GHOST g;
	public CortarPPillAction(GHOST ghost) {
		this.g=ghost;
	}


	@Override
	public MOVE execute(Game game) {
		if(game.doesGhostRequireAction(g)) {
			int [] active_ppills = game.getActivePowerPillsIndices();
			int dist_ppill = 30;
			int huir = 0;
			for(int ppill : active_ppills) {
				if (dist_ppill > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), ppill, game.getGhostLastMoveMade(g))) {
					dist_ppill = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), ppill, game.getGhostLastMoveMade(g));
					huir = ppill;
				}
				
//				MOVE last_move =game.getGhostLastMoveMade(g);
//				int ghost_pos = game.getGhostCurrentNodeIndex(g);
//				double dist_ghost_ppill = game.getShortestPathDistance(ghost_pos, ppill, last_move);
//				double dist_pacman_ppill = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),ppill, game.getPacmanLastMoveMade());
//				if(dist_pacman_ppill - dist_ghost_ppill <= 20 && (dist_pacman_ppill - dist_ghost_ppill>= -20) )
//					return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), ppill, game.getGhostLastMoveMade(g), DM.PATH);
			}
			if (huir != 0) {
				return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), huir, game.getGhostLastMoveMade(g), Constants.DM.PATH);
			}
		}
		return MOVE.NEUTRAL;


	}




	@Override
	public String getActionId() {
		return g + "yendo a cortar ppill";
	}


	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
