package es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.actions;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PillAction implements RulesAction {
	
	GHOST[] ghosts = GHOST.values();
	ArrayList<MOVE> possibleMoves;
	public PillAction() {
	}

	@Override
	public MOVE execute(Game game) {
		int posPills[] = game.getActivePillsIndices();
		MOVE nextMove = null;
		if (posPills.length > 0) {
			int distance = 999999;
			for (int i = 0; i < posPills.length; i++) {
				int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), posPills[i], game.getPacmanLastMoveMade());
				if (aux < distance && isSafe(game, posPills[i], aux)) {
					MOVE resMove = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), posPills[i], game.getPacmanLastMoveMade(), DM.PATH);
					if (possibleMoves.contains(resMove)) {
						distance = aux;
						nextMove = resMove;
					}
				}
			}
		}
		return nextMove;
	}

	@Override
	public void parseFact(Fact actionFact) {
		//get the list of possible moves from the fact slot
		try {
			possibleMoves = new ArrayList<MOVE>();
			String[] parts = actionFact.getSlotValue("moves").toString().split("-");
			for (String s: parts) {
				possibleMoves.add(MOVE.valueOf(s));
			}
		} catch (JessException e) {
			System.err.println(" :Exception parsing fact");
			e.printStackTrace();
		}
		
	}

	@Override
	public String getActionId() {
		return "chases";
	}

	private boolean isSafe(Game game, int i, int distance) {
		boolean result = true;
		for (GHOST g: ghosts) {
			if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0) {
				if (distance > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), i, game.getGhostLastMoveMade(g))) result = false;
			}
		}
		return result;
	}


}
