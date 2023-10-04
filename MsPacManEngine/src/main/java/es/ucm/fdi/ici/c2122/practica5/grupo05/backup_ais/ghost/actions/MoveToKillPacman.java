package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.actions;

import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Paths;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveToKillPacman implements RulesAction {
    private final GHOST ghost;

    public MoveToKillPacman(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Move to kill MsPacman";
    }

    @Override
    public MOVE execute(Game game) {
        int nextJunction = Paths.getDestinationJunctionForMsPacman(game);
        return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), nextJunction,
                DM.PATH);
    }

	@Override
	public void parseFact(Fact actionFact) {
		// do nothing
	}

}
