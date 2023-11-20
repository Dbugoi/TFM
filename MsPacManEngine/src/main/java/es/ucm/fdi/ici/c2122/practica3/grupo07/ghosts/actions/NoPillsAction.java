package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NoPillsAction implements RulesAction {

	GHOST ghost;
	public NoPillsAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"no pills";
	}

	@Override
	public MOVE execute(Game game) {
		int pacman=game.getPacmanCurrentNodeIndex();
		int farthestPill=game.getFarthestNodeIndexFromNodeIndex(pacman, game.getActivePillsIndices(), DM.PATH);	
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), farthestPill, game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		
	}

}
