package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.kill;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveToKillPacman implements Action {
    private final GHOST ghost;

    public MoveToKillPacman(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Move to kill MsPacman";
    }

    @Override
    public MOVE execute(Game game) {
        int nextJunction = GameUtils.getDestinationJunctionForMsPacman(game);
        return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), nextJunction,
                DM.PATH);
    }

}
