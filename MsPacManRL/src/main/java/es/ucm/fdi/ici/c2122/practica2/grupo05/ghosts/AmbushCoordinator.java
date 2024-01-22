package es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.CoordinatorHelper.MoveToNodeInfo;
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
        int pacmanNextJunction = GameUtils.getDestinationJunctionForMsPacman(game);
        List<Integer> junctions =
                new ArrayList<>(GameUtils.getClosestJunctions(game, pacmanNextJunction,
                        game.getPacmanLastMoveMade()));
        // getClosestJunctions() también incluye cruces a distancia 0, pero no
        // queremos de esos
        junctions.remove((Integer) pacmanNextJunction);
        return junctions;
    }

}
