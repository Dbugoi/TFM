package es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.coordinator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.coordinator.CoordinatorHelper.MoveToNodeInfo;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.Paths;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

class AmbushCoordinator {

    private AmbushCoordinator() {}

    public static Map<GHOST, MOVE> getAmbushMoves(Game game, List<GHOST> ambushers) {
        Map<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

        List<Integer> targetJunctions = findTargetJunctions(game);
        Map<GHOST, MoveToNodeInfo> bestAssignment = CoordinatorHelper
                .assignNodesMinimizingTotalDistance(game, ambushers, targetJunctions);

        for (GHOST ghost : ambushers) {
            moves.put(ghost, bestAssignment.get(ghost).firstMoveTowardsNode);
            // int to = bestGhostToMTJInfo.get(ghost).junction;
            // GameView.addPoints(game, GameViewUtils.getGhostColor(ghost), to);
        }
        return moves;
    }

    private static List<Integer> findTargetJunctions(Game game) {
        int pacmanNextJunction = Paths.getDestinationJunctionForMsPacman(game);
        List<Integer> junctions =
                new ArrayList<>(Paths.getClosestJunctions(game, pacmanNextJunction,
                        game.getPacmanLastMoveMade()));
        // getClosestJunctions() también incluye cruces a distancia 0, pero no
        // queremos de esos
        junctions.remove((Integer) pacmanNextJunction);
        return junctions;
    }

}
