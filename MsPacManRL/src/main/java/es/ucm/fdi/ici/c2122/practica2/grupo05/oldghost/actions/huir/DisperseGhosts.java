package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.minimax.algorithms.MoveCombinationsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DisperseGhosts implements Action {

    private final GHOST thisGhost;


    public DisperseGhosts(GHOST thisGhost) {
        this.thisGhost = thisGhost;
    }

    @Override
    public String getActionId() {
        return thisGhost.name() + " disperse";
    }

    @Override
    public MOVE execute(Game game) {
        return getMoveThatMaximisesTotalDistanceForEdibleGhosts(game);
        /*
         * Optional<GHOST> ghostInSamePassage = findFirstEdibleGhostInSamePassage(game); if
         * (ghostInSamePassage.isPresent()) { return
         * getAwayFromMsPacmanWithoutFollowingOtherGhost(game, ghostInSamePassage.get()); }
         * 
         * Optional<GHOST> protectorGhost = findGhostToFollowSafely(game); if
         * (protectorGhost.isPresent()) return GameUtils.getNextMoveTowardsGhost(game, thisGhost,
         * protectorGhost.get(), DM.PATH);
         * 
         * else return GameUtils.getNextMoveAwayFromMsPacMan(game, thisGhost, DM.PATH);
         */
    }

    private Optional<GHOST> findFirstEdibleGhostInSamePassage(Game game) {
        List<int[]> pathsToClosestJunctions = GameUtils.getPathsToClosestJunctions(game,
                game.getGhostCurrentNodeIndex(thisGhost),
                game.getGhostLastMoveMade(thisGhost));

        for (GHOST ghost : GHOST.values())
            if (ghost != thisGhost && game.isGhostEdible(ghost))
                for (int[] path : pathsToClosestJunctions)
                    for (int node : path)
                        if (game.getGhostCurrentNodeIndex(ghost) == node)
                            return Optional.of(ghost);

        return Optional.empty();
    }

    private MOVE getAwayFromMsPacmanWithoutFollowingOtherGhost(Game game, GHOST otherGhost) {
        int thisGhostIndex = game.getGhostCurrentNodeIndex(thisGhost);

        // no queremos hacer este movimiento
        MOVE lastMoveMadeByOtherGhost = game.getGhostLastMoveMade(otherGhost);

        int[] ghostNeighbouringNodes = game.getNeighbouringNodes(thisGhostIndex);
        double maxDistance = Double.NEGATIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : ghostNeighbouringNodes)
            if (game.getMoveToMakeToReachDirectNeighbour(thisGhostIndex,
                    node) != lastMoveMadeByOtherGhost) {

                double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), node,
                        game.getPacmanLastMoveMade(), DM.PATH);

                if (maxDistance < distance) {
                    maxDistance = distance;
                    bestMove = game.getMoveToMakeToReachDirectNeighbour(
                            game.getPacmanCurrentNodeIndex(), node);
                }
            }
        return bestMove;
    }

    private MOVE getMoveThatMaximisesTotalDistanceForEdibleGhosts(Game game) {
        /*
         * Vamos a buscar el conjunto de movimientos para los fantasmas que maximiza la distancia
         * entre sí y MsPacman (daremos más relevancia a MsPacman), y de ahí coger el movimiento
         * correspondiente a thisGhost.
         */
        double maxDistance = Double.NEGATIVE_INFINITY;
        MOVE chosenMove = MOVE.NEUTRAL;

        for (Map<GHOST, MOVE> moves : MoveCombinationsUtils
                .getPossibleCombinationsOfMovesForGhosts(game)) {

            Map<GHOST, Integer> ghostToIndex = getNextIndexForEachGhost(game, moves);

            double distance = productOfDistancesBetweenGhosts(game, ghostToIndex);
            // + sumOfDistancesFromMsPacmanToEachGhost(game, ghostToIndex);

            if (distance > maxDistance) {
                maxDistance = distance;
                chosenMove = moves.get(thisGhost);
            }
        }
        return chosenMove;
    }

    private Map<GHOST, Integer> getNextIndexForEachGhost(Game game, Map<GHOST, MOVE> moves) {
        Map<GHOST, Integer> ghostToIndex = new EnumMap<>(GHOST.class);
        for (Map.Entry<GHOST, MOVE> ghToMove : moves.entrySet())
            if (game.isGhostEdible(ghToMove.getKey())
                    && game.getGhostLairTime(ghToMove.getKey()) == 0) {
                int ghostIndex = game.getGhostCurrentNodeIndex(ghToMove.getKey());
                ghostToIndex.put(ghToMove.getKey(),
                        game.getNeighbour(ghostIndex, ghToMove.getValue()));
            }
        return ghostToIndex;
    }

    private double productOfDistancesBetweenGhosts(Game game, Map<GHOST, Integer> ghostToIndex) {
        double distance = 1;
        ArrayList<Integer> indices = new ArrayList<>(ghostToIndex.values());
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastPacmanMove = game.getPacmanLastMoveMade();

        for (int targetGhostIndex = 0; targetGhostIndex < indices.size(); targetGhostIndex++)
            for (int nextTargetGhostIndex = 0; nextTargetGhostIndex < indices
                    .size(); nextTargetGhostIndex++)
                if (targetGhostIndex != nextTargetGhostIndex) {
                    MOVE moveToTarget = game.getNextMoveTowardsTarget(pacmanIndex, targetGhostIndex,
                            lastPacmanMove, DM.PATH);
                    distance *= game.getDistance(targetGhostIndex, nextTargetGhostIndex,
                            moveToTarget, DM.PATH) + 1;
                }

        return distance;
    }

    private double sumOfDistancesFromMsPacmanToEachGhost(Game game,
            Map<GHOST, Integer> ghostToIndex) {
        double distance = 0;
        for (int index : ghostToIndex.values())
            if (index != GameUtils.cage) {
                distance += game.getDistance(game.getPacmanCurrentNodeIndex(), index,
                        game.getPacmanLastMoveMade(), DM.PATH);
            }
        return distance;
    }


    private Optional<GHOST> findGhostToFollowSafely(Game game) {
        int thisGhostIndex = game.getGhostCurrentNodeIndex(thisGhost);
        int msPacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMoveMadeByMsPacman = game.getPacmanLastMoveMade();

        for (GHOST otherGhost : GHOST.values())
            if (otherGhost != thisGhost
                    && game.isGhostEdible(thisGhost)
                    && game.getGhostLairTime(thisGhost) <= 0
                    && game.getGhostLairTime(otherGhost) <= 0
                    && !game.isGhostEdible(otherGhost)) {
                int otherGhostIndex = game.getGhostCurrentNodeIndex(otherGhost);
                MOVE lastMoveMadeByOtherGhost = game.getGhostLastMoveMade(otherGhost);

                boolean otherGhostCloserToThisGhostThanPacmanToThisGhost =
                        game.getDistance(otherGhostIndex, thisGhostIndex, lastMoveMadeByOtherGhost,
                                DM.PATH) < game.getDistance(msPacmanIndex, thisGhostIndex,
                                        lastMoveMadeByMsPacman, DM.PATH);

                boolean pacmanCloserToThisGhostThanPacmanToOtherGhost =
                        game.getDistance(msPacmanIndex, thisGhostIndex, lastMoveMadeByMsPacman,
                                DM.PATH) < game.getDistance(msPacmanIndex, otherGhostIndex,
                                        lastMoveMadeByMsPacman, DM.PATH);

                boolean otherGhostCloserToThisGhostThanOtherGhostToPacman =
                        game.getDistance(otherGhostIndex, thisGhostIndex, lastMoveMadeByOtherGhost,
                                DM.PATH) < game.getDistance(otherGhostIndex, msPacmanIndex,
                                        lastMoveMadeByOtherGhost, DM.PATH);


                if (otherGhostCloserToThisGhostThanPacmanToThisGhost
                        && pacmanCloserToThisGhostThanPacmanToOtherGhost
                        && otherGhostCloserToThisGhostThanOtherGhostToPacman)
                    return Optional.of(otherGhost);
            }
        return Optional.empty();
    }
}
