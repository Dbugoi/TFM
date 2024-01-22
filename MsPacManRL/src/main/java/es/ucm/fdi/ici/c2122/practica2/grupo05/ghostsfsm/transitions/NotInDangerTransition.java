package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class NotInDangerTransition implements Transition {
    private final GHOST ghost;

    public NotInDangerTransition(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostInput input = (GhostInput) in;
        boolean inLair = input.getGhostLairTime(ghost) > 0;
        boolean isEdible = input.isGhostEdible(ghost);
        boolean pacmanCloseToPPill = input.isPacmanCloseToPPill();
        boolean canKillPacman = input.canKillPacman(ghost);
        boolean ghostReadyToAttack = input.isGhostReadyToAttack(ghost);//false;
        return inLair
                || (!isEdible && !(pacmanCloseToPPill && !canKillPacman))
                || (isEdible && ghostReadyToAttack);
    }

    @Override
    public String toString() {
        return "Not in danger";
    }

}
