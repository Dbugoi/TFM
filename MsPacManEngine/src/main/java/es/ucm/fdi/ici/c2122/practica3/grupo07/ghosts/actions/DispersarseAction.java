package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DispersarseAction implements RulesAction {

	GHOST ghost;
	public DispersarseAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"dispersarse";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE move=MOVE.NEUTRAL;
		int g=game.getGhostCurrentNodeIndex(ghost);
		MOVE ghostLastMove=game.getGhostLastMoveMade(ghost);
		MOVE nMOVE=game.getApproximateNextMoveTowardsTarget(g, game.getPacmanCurrentNodeIndex(), ghostLastMove, DM.PATH);
		double minDist=Double.MAX_VALUE;
		for(MOVE m:game.getPossibleMoves(g,ghostLastMove)) {
			if(m!=MOVE.NEUTRAL&&m!=nMOVE) {
				double dist=0.0;
				for(GHOST gg:GHOST.values()) {
					dist+=game.getDistance(game.getNeighbour(g, m), game.getGhostCurrentNodeIndex(gg), m, DM.PATH);
				}
				if(dist<minDist) {
					minDist=dist;
					move=m;
				}
			}
		}
		return move;
	}

	@Override
	public void parseFact(Fact actionFact) {
	
	}

}
