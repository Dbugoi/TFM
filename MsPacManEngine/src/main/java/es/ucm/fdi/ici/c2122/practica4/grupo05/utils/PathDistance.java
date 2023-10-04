package es.ucm.fdi.ici.c2122.practica4.grupo05.utils;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Clase para calcular distancias usando {@link DM#PATH} mediante llamadas a métodos de
 * {@link Distance}.
 */
public class PathDistance {
    private PathDistance() {}

    /**
     * Distancia del fantasma a MsPacman
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static int fromGhostToPacman(Game game, GHOST ghost) {
        return (int) Distance.fromGhostToPacman(game, ghost, DM.PATH);
    }

    /**
     * Distancia de MsPacman al fantasma
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static int fromPacmanToGhost(Game game, GHOST ghost) {
        return (int) Distance.fromPacmanToGhost(game, ghost, DM.PATH);
    }

    /**
     * Distancia de un fantasma a otro.
     * 
     * @param game
     * @param first
     * @param second
     * @return
     */
    public static int fromGhostToGhost(Game game, GHOST first, GHOST second) {
        return (int) Distance.fromGhostToGhost(game, first, second, DM.PATH);
    }

    /**
     * Distancia de MsPacman a un nodo del juego.
     * 
     * @param game
     * @param target
     * @return
     */
    public static int fromPacmanTo(Game game, int target) {
        return (int) Distance.fromPacmanTo(game, target, DM.PATH);
    }

    /**
     * Distancia de un fantasma a un nodo del juego.
     * 
     * @param game
     * @param ghost
     * @param target
     * @return
     */
    public static int fromGhostTo(Game game, GHOST ghost, int target) {
        return (int) Distance.fromGhostTo(game, ghost, target, DM.PATH);
    }

    /**
     * Distancia desde 'origin' hasta MsPacman
     * 
     * @param game
     * @param origin
     * @param lastMove
     * @return
     */
    public static int toPacman(Game game, int origin, MOVE lastMove) {
        return (int) Distance.toPacman(game, origin, lastMove, DM.PATH);
    }

    /**
     * * Distancia desde 'origin' hasta el fantasma
     * 
     * @param game
     * @param origin
     * @param lastMove
     * @param ghost
     * @return
     */
    public static int toGhost(Game game, int origin, MOVE lastMove, GHOST ghost) {
        return (int) Distance.toGhost(game, origin, lastMove, ghost, DM.PATH);
    }
    
    /**
     * 
     */
    public static int fromXtoX(Game game, int origin, MOVE lastMoveOrigin, int dest) {
        return (int) Distance.fromXtoX(game, origin, lastMoveOrigin, dest, DM.PATH);
    }
}
