package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.nousadas;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.minimax.algorithms.MoveCombinationsUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Scatter implements Action {
    private final GHOST ghost;

    public Scatter(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Scatter";
    }

    @Override
    public MOVE execute(Game game) {
        /*
         * Vamos a buscar el conjunto de movimientos para los fantasmas que maximiza la distancia
         * entre sí y MsPacman (daremos más relevancia a MsPacman), y de ahí coger el movimiento
         * correspondiente a ghost.
         */
        double maxDistance = Double.NEGATIVE_INFINITY;
        MOVE chosenMove = MOVE.NEUTRAL;

        for (Map<GHOST, MOVE> moves : MoveCombinationsUtils
                .getPossibleCombinationsOfMovesForGhosts(game)) {

            Map<GHOST, Integer> ghostToIndex = getNextIndexForEachGhost(game, moves);

            /*
             * Multiplicamos las distancias porque favorece que se separen todos los fantasmas entre
             * sí. Si simplemente las sumáramos puede que también favoreciéramos que unos fantasmas
             * se junten mucho pero estén muy separados de otros.
             */
             double distance = productOfDistancesBetweenGhosts(game, ghostToIndex);
            // double distance = productOfEucledianDistancesBetweenGhosts(game, ghostToIndex);
            //double distance = maxDistanceToCorners(game, ghostToIndex);
            //+productOfDistancesFromMsPacmanToEachGhost(game, ghostToIndex);

            if (distance > maxDistance) {
                maxDistance = distance;
                chosenMove = moves.get(ghost);
            }
        }
        return chosenMove;
    }

    private Map<GHOST, Integer> getNextIndexForEachGhost(Game game, Map<GHOST, MOVE> moves) {
        Map<GHOST, Integer> ghostToIndex = new EnumMap<>(GHOST.class);
        for (Map.Entry<GHOST, MOVE> ghToMove : moves.entrySet())
            // if (game.isGhostEdible(ghToMove.getKey()) &&
            if (game.getGhostLairTime(ghToMove.getKey()) == 0) {
                int ghostIndex = game.getGhostCurrentNodeIndex(ghToMove.getKey());
                int node;
                if (ghToMove.getValue() != MOVE.NEUTRAL)
                    node = game.getNeighbour(ghostIndex, ghToMove.getValue());
                else
                    node = game.getGhostCurrentNodeIndex(ghToMove.getKey());

                ghostToIndex.put(ghToMove.getKey(), node);
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

    private double productOfEucledianDistancesBetweenGhosts(Game game,
            Map<GHOST, Integer> ghostToIndex) {
        double distance = 1;
        ArrayList<Integer> indices = new ArrayList<>(ghostToIndex.values());

        for (int targetIdx = 0; targetIdx < indices.size() - 1; targetIdx++)
            for (int nextTargetIdx = targetIdx + 1; nextTargetIdx < indices
                    .size(); nextTargetIdx++)
                if (targetIdx != nextTargetIdx) {
                    double d = game.getDistance(targetIdx, nextTargetIdx, DM.EUCLID) + 1;
                    distance *= transformDistance(d);
                }

        return distance;
    }

    private double transformDistance(double d) {
        // return 1.0 / (1 + Math.exp(-d));
        double first = 100;
        double exp = (d - 20.0) / 10.0;
        // return first / (1 + Math.exp(-exp));
        // return Math.exp(d);
        return d;
    }

    private double productOfDistancesFromMsPacmanToEachGhost(Game game,
            Map<GHOST, Integer> ghostToIndex) {
        double distance = 1;
        for (Map.Entry<GHOST, Integer> entry : ghostToIndex.entrySet())
            if (game.getGhostLairTime(entry.getKey()) == 0 && entry.getValue() != -1) {
                distance *= game.getDistance(game.getPacmanCurrentNodeIndex(), entry.getValue(),
                        game.getPacmanLastMoveMade(), DM.PATH) + 1;
            }
        return distance;
    }

    private double maxDistanceToCorners(Game game, Map<GHOST, Integer> ghostToIndex) {
        LinkedList<Integer> indices = new LinkedList<>(ghostToIndex.values());
        double maxDist = 0;
        int[] corners = {GameUtils.BOTTOM_LEFT_CORNER,
                GameUtils.TOP_RIGHT_CORNER,
                GameUtils.BOTTOM_RIGHT_CORNER,
                GameUtils.TOP_LEFT_CORNER};

        for (int corner : corners) {
            Optional<Integer> closestToCorner = indices.stream()
                    .min((a, b) -> (int) (game.getEuclideanDistance(a, corner)
                            - game.getEuclideanDistance(b, corner)));
            if (closestToCorner.isPresent()) {
                maxDist = Double.max(maxDist,
                /*maxDist+=*/        game.getEuclideanDistance(closestToCorner.get(), corner)
                )
                ;
                indices.remove(closestToCorner.get());
            }
        }

        return maxDist;
    }
}
