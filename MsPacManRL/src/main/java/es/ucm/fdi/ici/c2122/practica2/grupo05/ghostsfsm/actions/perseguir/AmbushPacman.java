package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.perseguir;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostsCoordinator;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AmbushPacman implements Action {
    private final GHOST ghost;
    private final GhostsCoordinator coordinator;

    public AmbushPacman(GHOST ghost, GhostsCoordinator coordinator) {
        this.ghost = ghost;
        this.coordinator = coordinator;
    }

    @Override
    public String getActionId() {
        return "Follow MsPacman";
    }

    @Override
    public MOVE execute(Game game) {
        return coordinator.requestAmbushMove(game, ghost);
    }

}
