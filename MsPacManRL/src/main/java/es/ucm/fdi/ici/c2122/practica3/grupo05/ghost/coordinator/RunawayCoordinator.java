package es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.coordinator;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.coordinator.CoordinatorHelper.MoveToNodeInfo;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.Paths;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Coordina a los fantasmas que quieran huir. La intención es que se dirija cada uno a una esquina
 * diferente.
 */
public class RunawayCoordinator {
    private RunawayCoordinator() {}

    private static final int[] CORNERS = {
            Paths.BOTTOM_LEFT_CORNER,
            Paths.BOTTOM_RIGHT_CORNER,
            Paths.TOP_LEFT_CORNER,
            Paths.TOP_RIGHT_CORNER
    };

    public static Map<GHOST, MOVE> getRunawayMoves(Game game, List<GHOST> runaways) {
        Map<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

        // queremos alejarnos de esta esquina
        int closestToPacmanCorner = getClosestToPacmanCorner(game);

        List<Integer> cornersWithoutPacman = Arrays.stream(CORNERS).boxed()
                .collect(Collectors.toList());
        cornersWithoutPacman.remove((Integer) closestToPacmanCorner);

        Map<GHOST, MoveToNodeInfo> bestAssignment = CoordinatorHelper
                .assignNodesMinimizingTotalDistance(game, runaways, cornersWithoutPacman);

        for (GHOST ghost : runaways) {
            moves.put(ghost, bestAssignment.get(ghost).firstMoveTowardsNode);
            // int to = bestAssignment.get(ghost).node;
            // GameView.addPoints(game, GameViewUtils.getGhostColor(ghost), to);
        }

        // NOTA: igual convendría que si varios fantasmas van al mismo sitio, el que
        // quede más lejos vaya a otro lugar

        return moves;
    }

    private static int getClosestToPacmanCorner(Game game) {
        double minDistance = Double.POSITIVE_INFINITY;
        int pacmanIndex = game.getPacmanCurrentNodeIndex();

        // si por algún error CORNERS estuviera vacío, también nos sirve la posición de pacman
        int closestCorner = pacmanIndex;

        for (int corner : CORNERS) {
            double distance = PathDistance.fromPacmanTo(game, corner);
            if (distance < minDistance) {
                closestCorner = corner;
                minDistance = distance;
            }
        }
        return closestCorner;
    }

}
