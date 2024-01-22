package es.ucm.fdi.ici.c2122.practica2.grupo05.minimax.algorithms;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MinimaxBestMoveForMsPacman {
    private MinimaxBestMoveForMsPacman() {}

    private static final int MAX_DEPTH = 5;
    private static final int MAX_NODES = 100000;
    private static final Map<GHOST, MOVE> NEUTRAL_GHOST_MOVES;

    private static int nodesGenerated = 0;

    static {
        NEUTRAL_GHOST_MOVES = new EnumMap<>(GHOST.class);
        for (GHOST ghost : GHOST.values())
            NEUTRAL_GHOST_MOVES.put(ghost, MOVE.NEUTRAL);
    }

    public static MOVE bestMoveForMsPacman(Game game) {
        MOVE bestMove = MOVE.NEUTRAL;

        if (GameUtils.isMsPacmanInJunction(game)) {
            int bestValue = Integer.MIN_VALUE;

            for (MOVE move : MoveCombinationsUtils.getPossibleMovesForMspacman(game)) {

                Game copyGame = game.copy();
                copyGame.updatePacMan(move);

                int value = minValue(copyGame, MAX_DEPTH, bestValue, Integer.MAX_VALUE);
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = move;
                }
            }
        }
        nodesGenerated = 0;

        return bestMove;
    }

    // Ghosts choose their move and try to minimize MsPacman's score
    private static int minValue(Game game, int depthOfSearch, int alpha, int beta) {
        nodesGenerated++;

        List<Map<GHOST, MOVE>> allPossibleMovesForGhosts =
                MoveCombinationsUtils.getPossibleCombinationsOfMovesForGhosts(game);

        for (Map<GHOST, MOVE> moves : allPossibleMovesForGhosts) {
            Game copyGame = game.copy();
            copyGame.updateGhosts(moves);
            copyGame.updateGame();
            runGameUntilSomeoneIsAtJunctionOrMsPacmanEaten(copyGame);

            beta = Integer.min(beta, maxValue(copyGame, depthOfSearch - 1, alpha, beta));

            if (alpha >= beta)
                break;
        }
        return beta;
    }

    // MsPacman chooses her move and tries to maximize her own score
    private static int maxValue(Game game, int depthOfSearch, int alpha, int beta) {
        nodesGenerated++;

        if (game.gameOver() || game.wasPacManEaten() || depthOfSearch == 0
                || nodesGenerated >= MAX_NODES)
            return heuristic(game);

        for (MOVE move : MoveCombinationsUtils.getPossibleMovesForMspacman(game)) {
            Game copyGame = game.copy();
            copyGame.updatePacMan(move);

            alpha = Integer.max(alpha, minValue(copyGame, depthOfSearch, alpha, beta));

            if (alpha >= beta)
                break;
        }
        return alpha;
    }

    private static void runGameUntilSomeoneIsAtJunctionOrMsPacmanEaten(Game game) {
        game.updateGame();
        while (!isSomeoneAtJunction(game) && !game.wasPacManEaten())
            game.advanceGame(MOVE.NEUTRAL, NEUTRAL_GHOST_MOVES);
    }

    private static boolean isSomeoneAtJunction(Game game) {
        if (GameUtils.isMsPacmanInJunction(game))
            return true;
        for (GHOST ghost : GHOST.values())
            if (GameUtils.isGhostInJunction(game, ghost))
                return true;
        return false;
    }

    private static int heuristic(Game game) {
        if (game.gameOver() || game.wasPacManEaten())
            return 0;
        else {
            double heuristic = 0;
            double distGhostHeuristic = distanceToGhostsHeuristic(game);

            heuristic += distGhostHeuristic * 2.05;

            return (int) (game.getScore() + heuristic);
        }
    }

    // Queremos favorecer estados con fantasmas comestibles cerca y atacantes lejos
    private static double distanceToGhostsHeuristic(Game game) {
        double distGhostHeuristic = 0;
        for (GHOST ghost : GHOST.values()) {
            if (GameUtils.couldGhostBeEaten(game, ghost)) {
                // Queremos favorecer los estados en los que los fantasmas comestibles están cerca
                distGhostHeuristic += game.getGhostCurrentEdibleScore() * (1
                        - 0.005 * GameUtils.distanceToMsPacman(game, ghost, DM.PATH));
            } else if (game.getGhostLairTime(ghost) == 0) {
                // Queremos priorizar los estados en los que los fantasmas no comestibles están
                // lejos
                distGhostHeuristic +=
                        0.005 * GameUtils.distanceToMsPacman(game, ghost, DM.PATH);
            }
        }
        return distGhostHeuristic;
    }

    // Queremos favorecer estados en los que los fantasmas esten cerca de la power pill
    // si así puede pacman comérselos al coger la power pill
    // TODO: private static double distanceToPowerPillHeuristic(Game game){}

}
