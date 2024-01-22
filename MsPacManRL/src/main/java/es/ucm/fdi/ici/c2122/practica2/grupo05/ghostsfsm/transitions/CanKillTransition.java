package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class CanKillTransition implements Transition {
    private final GHOST ghost;
    private final String id;

    public CanKillTransition(GHOST ghost, String id) {
        this.ghost = ghost;
        this.id = id;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostInput input = (GhostInput) in;
        return input.canKillPacman(ghost);
    }

    @Override
    public String toString() {
        return "Can kill (" + id + ")";
    }

}
