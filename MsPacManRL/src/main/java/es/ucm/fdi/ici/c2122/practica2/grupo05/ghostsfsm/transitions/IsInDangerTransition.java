package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class IsInDangerTransition implements Transition {
    private final GHOST ghost;

    public IsInDangerTransition(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostInput input = (GhostInput) in;
        boolean notInLair = input.getGhostLairTime(ghost) == 0;
        boolean isEdible = input.isGhostEdible(ghost);
        boolean isReadyToAttack = input.isGhostReadyToAttack(ghost);
        boolean isPacmanCloseToPPill = input.isPacmanCloseToPPill();
        boolean canKillPacman = input.canKillPacman(ghost);
        return notInLair
                && ((isEdible && !isReadyToAttack)
                        || (!isEdible && isPacmanCloseToPPill
                                && !canKillPacman));

    }

    @Override
    public String toString() {
        return "Is in danger";
    }

}
