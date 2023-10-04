package es.ucm.fdi.ici.c2122.practica3.grupo05.utils;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Clase para calcular la distancia de una entidad (fantasma o MsPacman) a otra entidad o a un nodo
 * en concreto del juego. La idea es que en todos los métodos de esta clase se considera
 * implícitamente el último movimiento realizado por la 1ª entidad, que es algo que se suele olvidar
 * al usar los métodos equivalentes de la clase Game.
 */
public class Distance {
    private Distance() {}

    /**
     * Distancia del fantasma a MsPacman
     * 
     * @param game
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static double fromGhostToPacman(Game game, GHOST ghost, DM distanceMeasure) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        return game.getDistance(ghostIndex, pacmanIndex, lastMove, distanceMeasure);
    }

    /**
     * Distancia de MsPacman al fantasma
     * 
     * @param game
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static double fromPacmanToGhost(Game game, GHOST ghost, DM distanceMeasure) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMove = game.getPacmanLastMoveMade();
        return game.getDistance(pacmanIndex, ghostIndex, lastMove, distanceMeasure);
    }

    /**
     * Distancia de un fantasma a otro.
     * 
     * @param game
     * @param first
     * @param second
     * @param distanceMeasure
     * @return
     */
    public static double fromGhostToGhost(Game game, GHOST first, GHOST second,
            DM distanceMeasure) {
        return game.getDistance(
                game.getGhostCurrentNodeIndex(first),
                game.getGhostCurrentNodeIndex(second),
                game.getGhostLastMoveMade(first),
                distanceMeasure);
    }

    /**
     * Distancia de MsPacman a un nodo del juego.
     * 
     * @param game
     * @param target
     * @param distanceMeasure
     * @return
     */
    public static double fromPacmanTo(Game game, int target, DM distanceMeasure) {
        return game.getDistance(
                game.getPacmanCurrentNodeIndex(),
                target,
                game.getPacmanLastMoveMade(),
                distanceMeasure);
    }

    /**
     * Distancia de un fantasma a un nodo del juego.
     * 
     * @param game
     * @param ghost
     * @param target
     * @param distanceMeasure
     * @return
     */
    public static double fromGhostTo(Game game, GHOST ghost, int target, DM distanceMeasure) {
        return game.getDistance(
                game.getGhostCurrentNodeIndex(ghost),
                target,
                game.getGhostLastMoveMade(ghost),
                distanceMeasure);
    }

    /**
     * Distancia desde 'origin' hasta MsPacman
     * 
     * @param game
     * @param origin
     * @param distanceMeasure
     * @return
     */
    public static double toPacman(Game game, int origin, MOVE lastMove, DM distanceMeasure) {
        return game.getDistance(origin,
                game.getPacmanCurrentNodeIndex(),
                lastMove,
                distanceMeasure);
    }

    /**
     * Distancia desde 'origin' hasta el fantasma
     * 
     * @param game
     * @param origin
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static double toGhost(Game game, int origin, MOVE lastMove, GHOST ghost,
            DM distanceMeasure) {
        return game.getDistance(origin,
                game.getGhostCurrentNodeIndex(ghost),
                lastMove,
                distanceMeasure);
    }
}
