package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.nousadas;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MoveAwayFromCloseEdibleGhosts implements Action {
    private final GHOST ghost;

    public MoveAwayFromCloseEdibleGhosts(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Move away from close edible ghosts";
    }

    @Override
    public MOVE execute(Game game) {
        GHOST closestGhost = findClosestEdibleGhost(game);
        return fleeFromGhostWithoutGoingToPacman(game, closestGhost);
    }

    private GHOST findClosestEdibleGhost(Game game) {
        GHOST closest = ghost; // asignamos un valor por defecto cualquiera
        double minDistance = Double.POSITIVE_INFINITY;
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);

        for (GHOST other : GHOST.values())
            if (other != ghost && game.isGhostEdible(other)) {
                int otherIndex = game.getGhostCurrentNodeIndex(other);
                // no nos importa en qué sentido vaya cada fantasma, así que ignoramos
                // el último movimiento de cada uno
                double distance = game.getShortestPathDistance(ghostIndex, otherIndex);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = other;
                }
            }
        return closest;
    }

    private MOVE fleeFromGhostWithoutGoingToPacman(Game game, GHOST otherGhost) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int otherIndex = game.getGhostCurrentNodeIndex(otherGhost);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        double minDistance = Double.POSITIVE_INFINITY;

        MOVE lastPacmanMove = game.getPacmanLastMoveMade();
        MOVE dontMake = game.getNextMoveTowardsTarget(ghostIndex, otherIndex, DM.PATH);
        MOVE chosenMove = MOVE.NEUTRAL;

        for (int node : game.getNeighbouringNodes(ghostIndex)) {
            MOVE moveToNode = game.getMoveToMakeToReachDirectNeighbour(ghostIndex, node);

            if (moveToNode != dontMake) {
                double distance = game.getDistance(pacmanIndex, node, lastPacmanMove, DM.PATH);
                if (distance < minDistance) {
                    minDistance = distance;
                    chosenMove = moveToNode;
                }
            }
        }

        if (chosenMove == MOVE.NEUTRAL)
            return dontMake;
        else
            return chosenMove;
    }
}
