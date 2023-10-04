package es.ucm.fdi.ici.c2122.practica4.grupo05.utils;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public final class CheckIfPacman {

    private CheckIfPacman() {}

    /**
     * Comprueba si MsPacman está cerca de la cárcel y además es peligroso acercarse (un fantasma va
     * a salir de la misma antes de que pudiera llegar ahí MsPacman).
     * 
     * @param game
     * @return
     */
    public static boolean isNearDangerousCage(Game game) {
        double distToCage = PathDistance.fromPacmanTo(game, Paths.CAGE);
        for (GHOST g : GHOST.values()) { // miramos entre todos los ghosts
            if (game.getGhostLairTime(g) != 0
                    && game.getGhostLairTime(g) <= distToCage
                    && distToCage <= AIParameters.LIMIT_DIST_TO_CAGE)
                return true;
        }
        return false;
    }

    /**
     * En el caso de que MsPacman fuera hacia 'ghost', comprueba si lo haría desde atrás o de
     * frente. La idea es usar este método para saber si MsPacman y el fantasma 'ghost' van uno
     * detrás del otro o en direcciones opuestas.
     * 
     * Si el fantasma no necesita un movimiento, devuelve {@code false}.
     * 
     * @param game
     * @param ghost
     * @return {@code true} si MsPacman se acerca por detrás, {@code false} en caso contrario.
     */
    public static boolean wouldApproachGhostFromBehind(Game game, GHOST ghost) {
        if (game.doesGhostRequireAction(ghost))
            return false;

        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        Game copy = game.copy();

        while (copy.getPacmanCurrentNodeIndex() != ghostIndex) {
            copy.updatePacMan(Moves.pacmanTowardsGhost(copy, ghost));
        }
        return game.getGhostLastMoveMade(ghost) == copy.getPacmanLastMoveMade();
    }
}
