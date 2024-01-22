package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.huir;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostsCoordinator;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToBestCorner implements Action {
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

}
