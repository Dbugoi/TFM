package es.ucm.fdi.ici.c2122.practica3.grupo05.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Clase con métodos para caminos (<i>paths</i>), cruces (<i>junctions</i>) y cualquier cosa
 * relacionada directamente con el tablero de juego.
 */
public class Paths {

    // Estos parámetros sirven para el primer nivel, pero igual no sirven para el 2º
    public static final int CAGE = 492; //TODO: reemplazar por llamadas a game.ghostInitialIndex()
    public static final int TOP_LEFT_CORNER = 0;
    public static final int TOP_RIGHT_CORNER = 77;
    public static final int BOTTOM_LEFT_CORNER = 1191;
    public static final int BOTTOM_RIGHT_CORNER = 1291;

    private static final Map<Integer, Integer> mazeIndexToRealCage;

    static {
        // datos conseguidos creando un Main en pacman.game.internal y buscando
        // qué nodos no tenían cruces al lado.
        mazeIndexToRealCage = new TreeMap<>();
        mazeIndexToRealCage.put(0, 1292);
        mazeIndexToRealCage.put(1, 1318);
        mazeIndexToRealCage.put(2, 1379);
        mazeIndexToRealCage.put(3, 1308);
    }

    private Paths() {}

    /**
     * Devuelve {@code true} si MsPacman está en {@code path}.
     * 
     * @param game
     * @param path
     * @return
     */
    public static boolean isMsPacmanInPath(Game game, int[] path) {
        for (int i : path)
            if (i == game.getPacmanCurrentNodeIndex())
                return true;
        return false;
    }

    /**
     * Devuelve {@code true} si el fantasma está en {@code path}.
     * 
     * @param game
     * @param ghost
     * @param path
     * @return
     */
    public static boolean isGhostInPath(Game game, GHOST ghost, int[] path) {
        for (int i : path)
            if (i == game.getGhostCurrentNodeIndex(ghost))
                return true;
        return false;
    }

    /**
     * Busca si hay una power pill activa en 'path' y devuelve un {@link Optional} con su índice si
     * existe. Si no, devuelve un {@link Optional#empty()}.
     * 
     * @param game
     * @param path
     * @return
     */
    public static Optional<Integer> powerPillInPath(Game game, List<Integer> path) {
        for (Integer indexPP : game.getActivePowerPillsIndices())
            if (path.contains(indexPP))
                return Optional.of(indexPP);
        return Optional.empty();
    }

    /**
     * Comprueba si MsPacman está o no en un cruce.
     * 
     * @param game
     * @return
     */
    public static boolean isMsPacmanInJunction(Game game) {
        for (int junction : game.getJunctionIndices())
            if (junction == game.getPacmanCurrentNodeIndex())
                return true;
        return false;
    }

    /**
     * Comprueba si el fantasma 'ghost' está o no en un cruce.
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static boolean isGhostInJunction(Game game, GHOST ghost) {
        for (int junction : game.getJunctionIndices())
            if (junction == game.getGhostCurrentNodeIndex(ghost))
                return true;
        return false;
    }

    /**
     * Busca el cruce o <i>junction</i> más cercano a MsPacman. Si ya está en un cruce devuelve la
     * posición de MsPacman.
     * 
     * @param game
     * @return
     */
    public static int getDestinationJunctionForMsPacman(Game game) {
        int pacmanNode = game.getPacmanCurrentNodeIndex();
        if (game.isJunction(pacmanNode))
            return pacmanNode;

        // buscamos el cruce más cercano a mspacman teniendo en cuenta su último movimiento
        int closestJunction = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int junction : game.getJunctionIndices()) {
            double distance = PathDistance.fromPacmanTo(game, junction);

            if (distance < minDistance) {
                closestJunction = junction;
                minDistance = distance;
            }
        }
        return closestJunction;
    }

    /**
     * Busca el cruce o <i>junction</i> más cercano al fantasma dado. Si ya está en un cruce
     * devuelve su posición.
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static int getDestinationJunctionForGhost(Game game, GHOST ghost) {
        int ghostNode = game.getGhostCurrentNodeIndex(ghost);
        if (game.isJunction(ghostNode))
            return ghostNode;

        int closestJunction = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int junction : game.getJunctionIndices()) {
            double distance = PathDistance.fromGhostTo(game, ghost, junction);

            if (distance < minDistance) {
                closestJunction = junction;
                minDistance = distance;
            }
        }
        return closestJunction;
    }

    /**
     * Devuelve los caminos a los cruces contiguos (es decir, aquellos a los que se puede llegar sin
     * pasar por otro cruce).
     * 
     * @param game
     * @param currentIndex
     * @param lastMove Si no es {@link MOVE#NEUTRAL}, no considera el cruce que queda atrás.
     * @return
     */
    public static List<int[]> getPathsToClosestJunctions(Game game, int currentIndex,
            MOVE lastMove) {
        return getClosestJunctionsInfo(game, currentIndex, lastMove)
                .stream()
                .map(info -> info.path)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve los cruces contiguos (es decir, aquellos a los que se puede llegar sin pasar por
     * otro cruce).
     * 
     * @param game
     * @param currentIndex
     * @param lastMove Si no es {@link MOVE#NEUTRAL}, no considera el cruce que queda atrás.
     * @return
     */
    public static List<Integer> getClosestJunctions(Game game, int currentIndex,
            MOVE lastMove) {
        return getClosestJunctionsInfo(game, currentIndex, lastMove)
                .stream()
                .map(info -> info.junction)
                .collect(Collectors.toList());
    }

    /**
     * Busca los cruces contiguos (aquellos a los que se puede llegar sin pasar por otro cruce) y
     * devuelve una lista de {@link JunctionInfo}s que contiene cada uno tanto el cruce como el
     * camino a seguir para llegar al mismo.
     * 
     * @param game
     * @param currentIndex
     * @param lastMove Si no es {@link MOVE#NEUTRAL}, no considera el cruce que queda atrás.
     * @return
     */
    private static List<JunctionInfo> getClosestJunctionsInfo(Game game, int currentIndex,
            MOVE lastMove) {
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
        int[] junctionIndices = game.getJunctionIndices();

        // Caminos a los cruces del tablero ordenados de menor a mayor longitud
        List<JunctionInfo> sortedPathsToAllJunctions = Arrays.stream(junctionIndices).boxed()
                .map(j -> new JunctionInfo(j, game.getShortestPath(currentIndex, j, lastMove)))
                .sorted((a, b) -> a.path.length - b.path.length)
                .collect(Collectors.toList());


        List<JunctionInfo> closestJunctionsInfo = new LinkedList<>();
        // El camino más corto de todos siempre lleva al cruce más próximo, así que
        // forma parte de la solución
        closestJunctionsInfo.add(sortedPathsToAllJunctions.get(0));
        sortedPathsToAllJunctions.remove(0);

        for (JunctionInfo jInfo : sortedPathsToAllJunctions)
            if (closestJunctionsInfo.stream()
                    .allMatch(info -> info.path.length == 0 || info.path[0] != jInfo.path[0]))
                closestJunctionsInfo.add(jInfo);

        return closestJunctionsInfo;
    }

    private static class JunctionInfo {
        final int junction;
        final int[] path;

        public JunctionInfo(int junction, int[] path) {
            this.junction = junction;
            this.path = path;
        }
    }

    /**
     * Devuelve el nodo correspondiente a la cárcel o <i>lair</i> del nivel actual.
     * 
     * @param game
     * @return
     */
    public static int lairNode(Game game) {
        return mazeIndexToRealCage.get(game.getMazeIndex());
    }
}
