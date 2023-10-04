package es.ucm.fdi.ici.c2122.practica4.grupo05;

import java.util.EnumMap;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class RandomGhosts.
 */
public final class GhostsRandom extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        moves.clear();
        for (GHOST ghostType : GHOST.values()) {
            if (game.doesGhostRequireAction(ghostType)) {
                moves.put(ghostType, Moves.getRandomMove());
            }
        }
        return moves;
    }
}