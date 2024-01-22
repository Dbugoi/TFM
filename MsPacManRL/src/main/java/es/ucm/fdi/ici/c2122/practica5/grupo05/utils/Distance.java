package es.ucm.fdi.ici.c2122.practica5.grupo05.utils;

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
        return fromXtoX(game, ghostIndex, lastMove, pacmanIndex, distanceMeasure);
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
        return fromXtoX(game, pacmanIndex, lastMove, ghostIndex, distanceMeasure);
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
        return fromXtoX(game,
                game.getGhostCurrentNodeIndex(first),
                game.getGhostLastMoveMade(first),
                game.getGhostCurrentNodeIndex(second),
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
        return fromXtoX(game,
                game.getPacmanCurrentNodeIndex(),
                game.getPacmanLastMoveMade(),
                target,
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
        return fromXtoX(game,
                game.getGhostCurrentNodeIndex(ghost),
                game.getGhostLastMoveMade(ghost),
                target,
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
        return fromXtoX(game,
                origin,
                lastMove,
                game.getPacmanCurrentNodeIndex(),
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
        return fromXtoX(game,
                origin,
                lastMove,
                game.getGhostCurrentNodeIndex(ghost),
                distanceMeasure);
    }

    /**
     * Devuelve {@link Double#POSITIVE_INFINITY} si hubiera algún error del tipo
     * {@link ArrayIndexOutOfBoundsException}.
     */
    public static double fromXtoX(Game game, int origin, MOVE lastMoveOrigin, int dest,
            DM distanceMeasure) {
        try {
            return (int) game.getDistance(origin, dest, lastMoveOrigin, distanceMeasure);
        } catch (IndexOutOfBoundsException e) {
            return Double.POSITIVE_INFINITY;
        }
    }
}
