package es.ucm.fdi.ici.c2122.practica3.grupo05.utils;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import java.util.EnumMap;
import java.util.Map;
import pacman.game.Game;

/**
 * Clase para comprobar si un fantasma sigue alguna de varias condiciones que son bastante comunes.
 */
public class CheckIfGhost {
    private CheckIfGhost() {}

    /**
     * Comprueba si un fantasma es <i>chasing</i> o no.
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static boolean isChasing(Game game, GHOST ghost) {
        return (!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0)||edibleChanging(game,ghost);
    }

    public static boolean edibleChanging(Game game, GHOST ghost) {
    	if(game.getGhostLairTime(ghost) != 0)
    		return false;
    	if(game.getGhostLairTime(ghost) ==0 && game.isGhostEdible(ghost)
                    && PathDistance.fromPacmanToGhost(game, ghost) * 2 <= game
                            .getGhostEdibleTime(ghost))
    		return false;
    	return true;
    }
    /**
     * Comprueba si el fantasma está en la cárcel o no.
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static boolean isInLair(Game game, GHOST ghost) {
        return game.getGhostLairTime(ghost) > 0;
    }

    /**
     * Comprueba si el fantasma es comible o no. Comprueba también si está o no en la cárcel (no
     * debería ser comible si está dentro), aunque en realidad no debería alterar el resultado. El
     * método existe por complementar {@link #isChasing(Game, GHOST)}
     * y{@link #isInLair(Game, GHOST)}.
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static boolean isEdible(Game game, GHOST ghost) {
        return game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0;
    }

    /**
     * Comprueba si el fantasma no solo es comestible sino que podría llegar a ser comido por
     * MsPacman en el tiempo que le queda de ser comestible. Devuelve {@code false} si el fantasma
     * no necesita un movimiento todavía.
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static boolean couldBeEaten(Game game, GHOST ghost) {
        if (!isEdible(game, ghost) && game.doesGhostRequireAction(ghost))
            return false;
        else {
            Game copy = game.copy();
            Map<GHOST, MOVE> ghostMoves = new EnumMap<>(GHOST.class);

            while (copy.isGhostEdible(ghost) 
                && !copy.wasGhostEaten(ghost)
                && !copy.wasPacManEaten()) 
            {
                copy.updatePacMan(Moves.pacmanTowardsGhost(copy, ghost));

                for (GHOST g : GHOST.values()) {
                    ghostMoves.put(g, Moves.ghostAwayFromPacman(copy, g));
                }
                ghostMoves.put(ghost, Moves.ghostTowardsPacman(copy, ghost));

                copy.updateGhosts(ghostMoves);

                // En la copia solo actualizamos los tiempos en los que los fantasmas
                // son comestibles y el que sean comidos por pacman.
                // Si un fantasma se come a pacman a pesar de intentar alejarse
                // podemos suponer que es imposible que pacman se coma a 'ghost'
                copy.updateGame(true, false, false, false, false);
            }

            return copy.wasGhostEaten(ghost);
        }
    }

    public static boolean existsGhostInLair(Game game) {
		for(GHOST g: GHOST.values())
			if(game.getGhostLairTime(g)>0)
				return true;
						
		return false;
	}
}
