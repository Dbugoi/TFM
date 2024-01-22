package es.ucm.fdi.ici.c2122.practica2.grupo05.minimax.algorithms;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MoveCombinationsUtils {
    private MoveCombinationsUtils() {}

    /**
     * Clase con una posible combinación del movimiento de MsPacman y los movimientos de los
     * fantasmas.
     */
    public static class MoveCombination {
        private final MOVE msPacmanMove;
        private final Map<GHOST, MOVE> ghostsMoves;

        public MoveCombination(MOVE msPacmanMove, Map<GHOST, MOVE> ghostsMoves) {
            this.msPacmanMove = msPacmanMove;
            this.ghostsMoves = ghostsMoves;
        }

        public Map<GHOST, MOVE> getGhostsMoves() {
            return ghostsMoves;
        }

        public MOVE getMsPacmanMove() {
            return msPacmanMove;
        }
    }

    public static List<MoveCombination> getAllPossibleMoveCombinations(
            Game game) {
        List<MOVE> mspacmanMoves = getPossibleMovesForMspacman(game);
        List<Map<GHOST, MOVE>> ghostsMoveCombinations =
                getPossibleCombinationsOfMovesForGhosts(game);
        List<MoveCombination> finalResult = new LinkedList<>();

        for (MOVE mspacmanMove : mspacmanMoves)
            for (Map<GHOST, MOVE> ghostsMoves : ghostsMoveCombinations)
                finalResult.add(new MoveCombination(mspacmanMove, ghostsMoves));

        return finalResult;
    }

    public static List<MOVE> getPossibleMovesForMspacman(Game game) {
        List<MOVE> moves = new LinkedList<>();
        if (!GameUtils.isMsPacmanInJunction(game))
            moves.add(MOVE.NEUTRAL);
        else
            moves = Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex(),
                    game.getPacmanLastMoveMade()));
        return moves;
    }

    public static List<Map<GHOST, MOVE>> getPossibleCombinationsOfMovesForGhosts(Game game) {
        List<Map<GHOST, MOVE>> result = new LinkedList<>();

        Map<GHOST, List<MOVE>> movesForEachGhost = getPossibleMovesForEachGhost(game);

        for (MOVE blinkyMove : movesForEachGhost.get(GHOST.BLINKY)) {
            for (MOVE inkyMove : movesForEachGhost.get(GHOST.INKY)) {
                for (MOVE pinkyMove : movesForEachGhost.get(GHOST.PINKY)) {
                    for (MOVE sueMove : movesForEachGhost.get(GHOST.SUE)) {
                        Map<GHOST, MOVE> ghostsMoves = new EnumMap<>(GHOST.class);

                        ghostsMoves.put(GHOST.BLINKY, blinkyMove);
                        ghostsMoves.put(GHOST.INKY, inkyMove);
                        ghostsMoves.put(GHOST.PINKY, pinkyMove);
                        ghostsMoves.put(GHOST.SUE, sueMove);

                        result.add(ghostsMoves);
                    }
                }
            }
        }
        return result;
    }

    private static Map<GHOST, List<MOVE>> getPossibleMovesForEachGhost(Game game) {
        Map<GHOST, List<MOVE>> ghostToMoves = new EnumMap<>(GHOST.class);

        for (GHOST ghost : GHOST.values()) {
            List<MOVE> moves = new LinkedList<>();
            if (!GameUtils.isGhostInJunction(game, ghost))
                moves.add(MOVE.NEUTRAL);
            else
                moves = Arrays.asList(game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost),
                        game.getGhostLastMoveMade(ghost)));
            ghostToMoves.put(ghost, moves);
        }

        return ghostToMoves;
    }
}
