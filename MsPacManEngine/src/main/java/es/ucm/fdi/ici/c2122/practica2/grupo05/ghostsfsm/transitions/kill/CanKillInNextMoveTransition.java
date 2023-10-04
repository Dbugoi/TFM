package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.kill;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class CanKillInNextMoveTransition implements Transition {
    private final GHOST ghost;

    public CanKillInNextMoveTransition(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public boolean evaluate(Input in) {
        Game game = in.getGame();
        return GameUtils.getDestinationJunctionForMsPacman(game) == game
                .getGhostCurrentNodeIndex(ghost);
    }

    @Override
    public String toString() {
        return "Can kill in next move";
    }

}
