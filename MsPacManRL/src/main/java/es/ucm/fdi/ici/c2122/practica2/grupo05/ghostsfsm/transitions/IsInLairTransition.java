package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class IsInLairTransition implements Transition {
    private final GHOST ghost;
    private final String id;

    public IsInLairTransition(GHOST ghost, String id) {
        this.ghost = ghost;
        this.id = id;
    }

    @Override
    public boolean evaluate(Input in) {
        return in.getGame().getGhostLairTime(ghost) > 0;
    }

    @Override
    public String toString() {
        return "Is in lair (" + id + ")";
    }

}
