package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.kill;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class KillPacman implements Action {
    private final GHOST ghost;

    public KillPacman(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Kill MsPacman";
    }

    @Override
    public MOVE execute(Game game) {
        MOVE chosenMove = MOVE.NEUTRAL;
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE dontMakeMove = game.getPacmanLastMoveMade();
        MOVE[] availableMoves = game.getPossibleMoves(ghostIndex, game.getGhostLastMoveMade(ghost));

        int minDistance = Integer.MAX_VALUE;
        for (MOVE move : availableMoves)
            if (move != dontMakeMove) {
                int neighbour = game.getNeighbour(ghostIndex, move);
                int distance = game.getShortestPathDistance(neighbour, pacmanIndex, move);
                if (distance < minDistance) {
                    minDistance = distance;
                    chosenMove = move;
                }
            }
        if (chosenMove == MOVE.NEUTRAL) {
            // throw new IllegalStateException("No move was chosen");
            //System.out.println("No move was chosen");
            return dontMakeMove;
        }

        return chosenMove;
    }
}
