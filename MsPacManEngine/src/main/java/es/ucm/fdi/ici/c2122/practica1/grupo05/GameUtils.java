package es.ucm.fdi.ici.c2122.practica1.grupo05;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public final class GameUtils {
    private static final Random rnd = new Random();

    private GameUtils() {}

    /**
     * Distance from the given {@link GHOST} to MsPacMan.
     * 
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static double distanceToMsPacman(Game game, GHOST ghost, DM distanceMeasure) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        return game.getDistance(ghostIndex, pacmanIndex, distanceMeasure);
    }

    /**
     * Gets next MsPacMan's next move away from the ghost target
     * 
     * @param game
     * @param ghost target
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveAwayFromGhost(Game game, GHOST ghost, DM distanceMeasure) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        return game.getApproximateNextMoveAwayFromTarget(pacmanIndex, ghostIndex, lastMove,
                distanceMeasure);
    }

    /**
     * Gets next MsPacMan's next move towards the ghost target
     * 
     * @param game
     * @param ghost target
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveTowardsGhost(Game game, GHOST ghost, DM distanceMeasure) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        return game.getApproximateNextMoveTowardsTarget(pacmanIndex, ghostIndex, lastMove,
                distanceMeasure);
    }

    /**
     * Gets next ghost's next move towards MsPacMan
     * 
     * @param game
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveTowardsMsPacMan(Game game, GHOST ghost, DM distanceMeasure) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        return game.getApproximateNextMoveTowardsTarget(ghostIndex, pacmanIndex, lastMove,
                distanceMeasure);
    }

    /**
     * Gets next ghost's next move away from MsPacMan
     * 
     * @param game
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveAwayFromMsPacMan(Game game, GHOST ghost, DM distanceMeasure) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        return game.getApproximateNextMoveAwayFromTarget(ghostIndex, pacmanIndex, lastMove,
                distanceMeasure);
    }

    /**
     * @param game
     * @param distanceMeasure
     * @param limit
     * @return Closest ghost that isn't edible and is at a distance less than limit.
     */
    public static GHOST getClosestGhost(Game game, DM distanceMeasure) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) GameUtils.distanceToMsPacman(game, g, distanceMeasure);
        Optional<GHOST> optGhost = Arrays.stream(GHOST.values())
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));

        if (!optGhost.isPresent())
            throw new IllegalStateException("There are no ghosts in game");

        return optGhost.get();
    }

    /**
     * @param game
     * @param distanceMeasure
     * @param limit
     * @return Closest ghost that isn't edible and is at a distance less than limit.
     */
    public static Optional<GHOST> getClosestChasingGhost(Game game, DM distanceMeasure, int limit) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) GameUtils.distanceToMsPacman(game, g, distanceMeasure);
        return Arrays.stream(GHOST.values()).filter(gh -> !game.isGhostEdible(gh))
                .filter(gh -> distToMsPacman.applyAsInt(gh) < limit)
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));
    }

    /**
     * @param game
     * @return Closest ghost that is edible
     */
    public static Optional<GHOST> getClosestEdibleGhost(Game game, DM distanceMeasure) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) GameUtils.distanceToMsPacman(game, g, distanceMeasure);
        return Arrays.stream(GHOST.values()).filter(game::isGhostEdible)
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));
    }

    public static boolean isMsPacManCloseToPowerPill(Game game, DM distanceMeasure, int limit) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        IntUnaryOperator distToMsPacman =
                (int i) -> (int) game.getDistance(pacmanIndex, i, lastMove, distanceMeasure);

        return Arrays.stream(game.getPowerPillIndices())
                .anyMatch(i -> distToMsPacman.applyAsInt(i) < limit);
    }

    /**
     * @param game
     * @return Index of the closest power pill to MsPacMan. If there are no power pills, it returns
     *         a -1
     */
    public static int getClosestPowerPillToMsPacMan(Game game, DM distanceMeasure) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        IntUnaryOperator distToMsPacman =
                i -> (int) game.getDistance(pacmanIndex, i, lastMove, distanceMeasure);

        Optional<Integer> closestPill = Arrays
                .stream(game.getActivePowerPillsIndices())
                .boxed()
                .min((p1, p2) -> distToMsPacman.applyAsInt(p1) - distToMsPacman.applyAsInt(p2));

        return closestPill.orElse(-1);

    }

    public static MOVE getRandomMove() {
        MOVE[] allMoves = MOVE.values();
        return allMoves[rnd.nextInt(allMoves.length)];
    }

    public static List<int[]> getPathsToClosestJunctions(Game game, int currentIndex,
            MOVE lastMove) {
        int[] junctionIndices = game.getJunctionIndices();

        /*
         * Consideramos que "los cruces más cercanos" son aquellos a los que se puede llegar
         * directamente desde 'currentIndex' sin tener que pasar por otro cruce.
         * 
         * Para conseguir los caminos a los cruces más cercanos tenemos que tener en cuenta que:
         * 
         * - Dados los caminos a todos los cruces, los cruces más cercanos son los que tienen
         * caminos más cortos
         * 
         * - Para llegar a los cruces más lejanos hay que pasar necesariamente por alguno de los más
         * cercanos
         * 
         * - Los caminos que nos da game.getShortestPath(...) son arrays de índices en los que
         * necesariamente, creo, que el primer indice se corresponde con 'currentIndex', por los que
         * el primer índice de todos esos arrays será siempre el mismo.
         * 
         * - El 2º elemento de los arrays que representan los caminos a los cruces más cercanos será
         * siempre único. Si hay 2 caminos con el mismo 2º elemento y 'currentIndex' se corresponde
         * con un cruce, uno de dichos caminos lleva a un cruce que no es de los más cercanos.
         */

        // Caminos a los cruces del tablero ordenados de menor a mayor longitud
        List<int[]> sortedPathsToAllJunctions = Arrays.stream(junctionIndices).boxed()
                .map(i -> game.getShortestPath(currentIndex, i, lastMove))
                .sorted((a, b) -> a.length - b.length)
                .collect(Collectors.toList());

        List<int[]> pathsToClosestJunctions = new LinkedList<>();
        // El camino más corto de todos siempre lleva al cruce más próximo, así que
        // forma parte de la solución
        pathsToClosestJunctions.add(sortedPathsToAllJunctions.get(0));
        sortedPathsToAllJunctions.remove(0);

        for (int[] path : sortedPathsToAllJunctions)
            if (pathsToClosestJunctions.stream().allMatch(p -> p[1] != path[1]))
                pathsToClosestJunctions.add(path);

        return pathsToClosestJunctions;
    }
   
}
