package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class NoLongerInLairTransition implements Transition {
    private final GHOST ghost;

    public NoLongerInLairTransition(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public boolean evaluate(Input in) {
        return in.getGame().getGhostLairTime(ghost) == 0;
    }

    @Override
    public String toString() {
        return "No longer in lair";
    }

}
