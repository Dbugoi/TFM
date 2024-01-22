package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class InterceptPPAction implements RulesAction {
	GHOST ghost;
	int closestPP;
	public InterceptPPAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"interceptPP";
	}

	@Override
	public MOVE execute(Game game) {
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();
		int g = game.getGhostCurrentNodeIndex(ghost);
		MOVE ghostLastMove=game.getGhostLastMoveMade(ghost);
		
		int[]path=game.getShortestPath(pacman, closestPP, pacmanLastMove);
		for(int p:path) {
			if(g==p) {//si ya se encuentra en el camino de pacman y la pp
				return game.getApproximateNextMoveTowardsTarget(g, pacman, ghostLastMove, DM.PATH);
			}
		}
		int j = Util.interceptJunction(game, pacman, pacmanLastMove, closestPP, g, ghostLastMove);
		//GameView.addLines(game, Color.BLUE, g, j);
		return game.getApproximateNextMoveTowardsTarget(g, j, ghostLastMove, DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("node_info");
			//System.out.println("v: "+value);
			if(value == null)
				return;
			closestPP = value.intValue(null);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

}
