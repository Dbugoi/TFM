package es.ucm.fdi.ici.c2122.practica3.grupo05.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.IntUnaryOperator;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Clase con métodos para buscar pills y power pills que sigan ciertas condiciones.
 */
public class Pills {
    private Pills() {}

    /**
     * Comprueba si MsPacman está a una distancia menor que 'limit' de la power pill más cercana.
     * 
     * @param game
     * @param limit
     * @return
     */
    public static boolean isMsPacManCloseToPowerPill(Game game, int limit) {
        return Arrays
                .stream(game.getActivePowerPillsIndices())
                .anyMatch(i -> PathDistance.fromPacmanTo(game, i) < limit);
    }

    /**
     * @param game
     * @return Index of the closest power pill to MsPacMan. If there are no power pills, it returns
     *         a -1
     */
    public static int getClosestPowerPillToMsPacMan(Game game) {
        IntUnaryOperator distToMsPacman = i -> PathDistance.fromPacmanTo(game, i);

        Optional<Integer> closestPill = Arrays
                .stream(game.getActivePowerPillsIndices())
                .boxed()
                .min((p1, p2) -> distToMsPacman.applyAsInt(p1) - distToMsPacman.applyAsInt(p2));

        return closestPill.orElse(-1);
    }

    /**
     * Busca la pill más cercana a MsPacman de modo que el movimiento que nos lleve a dicha pill por
     * el camino más cercano esté en {@code availableMoves}.
     * 
     * @param game
     * @param availableMoves
     * @param i 
     * @return
     */
    public static int getClosestPillUsingAvailableMoves(Game game, List<MOVE> availableMoves) {
        double minDistance = Integer.MAX_VALUE;
        int finalPill = -1;

        for (int pillIndex : game.getActivePillsIndices()) {
            double dist = PathDistance.fromPacmanTo(game, pillIndex);

            if (minDistance > dist
                    && dist > 0
                    && availableMoves.contains(Moves.pacmanTowards(game, pillIndex))) {
                minDistance = dist;
                finalPill = pillIndex;
            }
        }
        return finalPill;
    }

}
