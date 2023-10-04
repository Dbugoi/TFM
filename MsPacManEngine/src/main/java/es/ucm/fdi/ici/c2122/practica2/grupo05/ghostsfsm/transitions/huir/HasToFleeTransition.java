package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.huir;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class HasToFleeTransition implements Transition {
    private final GHOST ghost;

    public HasToFleeTransition(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public boolean evaluate(Input in) {
        GhostInput input = (GhostInput) in;
        Game game = in.getGame();
        
        return !(game.isGhostEdible(ghost) 
            && input.isThereGhostProtector()
            && input.canGhostFollowAProtectorGhostSafely(ghost));
    }

    @Override
    public String toString() {
        return "Has to flee";
    }

}
