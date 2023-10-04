package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.actions;

import es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.coordinator.GhostsCoordinator;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToBestCorner implements RulesAction {
    private final GHOST ghost;
    private final GhostsCoordinator coordinator;

    public GoToBestCorner(GHOST ghost, GhostsCoordinator coordinator) {
        this.ghost = ghost;
        this.coordinator = coordinator;
    }

    @Override
    public String getActionId() {
        return ghost.name() + " go to best corner";
    }

    @Override
    public MOVE execute(Game game) {
        return coordinator.requestRunAwayMove(game, ghost);
    }

	@Override
	public void parseFact(Fact actionFact) {
		// do nothing
	}

}
