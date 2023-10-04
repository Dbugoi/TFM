package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.nousadas;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class LoopToKillPacmanTransition implements Transition {
    private final GHOST ghost;

    public LoopToKillPacmanTransition(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public boolean evaluate(Input in) {
        Game game = in.getGame();
        if (game.isJunction(game.getGhostCurrentNodeIndex(ghost)))
            throw new IllegalStateException("Should have killed Pacman or changed state");
        return true;
    }

    @Override
    public String toString() {
        return "Loop to kill pacman";
    }

}
