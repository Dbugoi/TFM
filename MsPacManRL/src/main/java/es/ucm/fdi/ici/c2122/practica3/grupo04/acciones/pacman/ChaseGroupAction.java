package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGroupAction implements RulesAction {

	double maxDist;
	public ChaseGroupAction(double maxDist) {
		this.maxDist = maxDist;
	}

	@Override
	public MOVE execute(Game game) {
		// Busca el fantasma comestible mas cercano
		int pacman = game.getPacmanCurrentNodeIndex();
		int numGhosts = GHOST.values().length;
		GHOST[] ghosts = GHOST.values();
		int distances[] = new int[numGhosts];
		
		// Calcula las distancias de pacman a los fantasmas
		for (int i = 0; i < numGhosts; ++i) {
			if(game.getGhostLairTime(ghosts[i]) <= 0)
				distances[i] = game.getShortestPathDistance(pacman, game.getGhostCurrentNodeIndex(ghosts[i]),
						game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]);
		}
		
		// Se ordenan en el vector
		for(int j = 0; j < numGhosts; ++j) {
			for(int i = 0; i < numGhosts - 1; ++i) {
				if(distances[i] > distances[i + 1]) {
					int d = distances[i + 1];
					GHOST g = ghosts[i + 1];
					distances[i + 1] = distances[i];
					ghosts[i + 1] = ghosts[i];
					distances[i] = d;
					ghosts[i] = g;
				}
			}
		}
		
		// Se recorren los fantasmas en orden y se mira si alguno esta cerca de otro
		for (int i = 0; i < numGhosts - 1; ++i) {
			if(game.getGhostLairTime(ghosts[i]) > 0) continue;
			int thisGhost = game.getGhostCurrentNodeIndex(ghosts[i]);
			for (int j = i + 1; j < numGhosts; ++j) {
				if(game.getGhostLairTime(ghosts[i]) > 0) continue;
				int otherGhost = game.getGhostCurrentNodeIndex(ghosts[j]);
				if (game.getDistance(thisGhost, otherGhost, DM.PATH) <= maxDist) {
					return CommonMethodsPacman.avoidGhosts(game, pacman, thisGhost);
				}
			}
		}
		// No deberia llegar aqui
		return game.getNextMoveTowardsTarget(pacman, CommonMethodsPacman.findClosestPill(game, pacman), DM.PATH);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return "ChaseGroupAction";
	}

}