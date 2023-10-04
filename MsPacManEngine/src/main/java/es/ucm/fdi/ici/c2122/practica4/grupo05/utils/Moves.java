package es.ucm.fdi.ici.c2122.practica4.grupo05.utils;

import java.util.Random;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Métodos básicos para calcular el mejor movimiento en cada caso.
 * 
 * @author Erik Karlgren Domercq
 */
public final class Moves {
    private Moves() {}

    private static final Random rnd = new Random();

    /**
     * Move MsPacman towards target
     * 
     * @param game
     * @param target
     * @return
     */
    public static MOVE pacmanTowards(Game game, int target) {
        return towards(game, game.getPacmanCurrentNodeIndex(), target,
                game.getPacmanLastMoveMade());
    }

    /**
     * Move MsPacman away from target
     * 
     * @param game
     * @param target
     * @return
     */
    public static MOVE pacmanAwayFrom(Game game, int target) {
        return awayFrom(game, game.getPacmanCurrentNodeIndex(), target,
                game.getPacmanLastMoveMade(), MOVE.NEUTRAL);
    }

    /**
     * Move ghost towards target
     * 
     * @param game
     * @param ghost
     * @param target
     * @return
     */
    public static MOVE ghostTowards(Game game, GHOST ghost, int target) {
        return towards(game, game.getGhostCurrentNodeIndex(ghost), target,
                game.getGhostLastMoveMade(ghost));
    }

    /**
     * Move ghost away from target
     * 
     * @param game
     * @param ghost
     * @param target
     * @return
     */
    public static MOVE ghostAwayFrom(Game game, GHOST ghost, int target) {
        return awayFrom(game, game.getGhostCurrentNodeIndex(ghost), target,
                game.getGhostLastMoveMade(ghost), MOVE.NEUTRAL);
    }

    /**
     * Retorna el movimiento que más acerque a MsPacman al fantasma ghost
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static MOVE pacmanTowardsGhost(Game game, GHOST ghost) {
        return towards(game, game.getPacmanCurrentNodeIndex(),
                game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
    }

    /**
     * Retorna el movimiento que más aleje a MsPacman del fantasma ghost
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static MOVE pacmanAwayFromGhost(Game game, GHOST ghost) {
        return awayFrom(game, game.getPacmanCurrentNodeIndex(),
                game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(),
                game.getGhostLastMoveMade(ghost));
    }

    /**
     * Retorna el movimiento que más acerque al fantasma ghost a MsPacman
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static MOVE ghostTowardsPacman(Game game, GHOST ghost) {
        return towards(game, game.getGhostCurrentNodeIndex(ghost),
                game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost));
    }

    /**
     * Retorna el movimiento que más aleje al fantasma ghost de MsPacman
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static MOVE ghostAwayFromPacman(Game game, GHOST ghost) {
        return awayFrom(game, game.getGhostCurrentNodeIndex(ghost),
                game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost),
                game.getPacmanLastMoveMade());
    }

    /**
     * Retorna el movimiento que más acerque al fantasma first al fantasma second
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static MOVE ghostTowardsGhost(Game game, GHOST first, GHOST second) {
        return towards(game, game.getGhostCurrentNodeIndex(first),
                game.getGhostCurrentNodeIndex(second), game.getGhostLastMoveMade(first));
    }

    /**
     * Retorna el movimiento que más aleje al fantasma first del fantasma second
     * 
     * @param game
     * @param ghost
     * @return
     */
    public static MOVE ghostAwayFromGhost(Game game, GHOST first, GHOST second) {
        return awayFrom(game, game.getGhostCurrentNodeIndex(first),
                game.getGhostCurrentNodeIndex(second), game.getGhostLastMoveMade(first),
                game.getGhostLastMoveMade(second));
    }

    /**
     * Retorna el movimiento que más nos acerque desde 'origin' a MsPacman.
     * 
     * @param game
     * @param origin
     * @param lastMove
     * @return
     */
    public static MOVE towardsPacman(Game game, int origin, MOVE lastMove) {
        return towards(game, origin, game.getPacmanCurrentNodeIndex(), lastMove);
    }

    /**
     * Retorna el movimiento que más nos acerque desde 'origin' al fantasma.
     * 
     * @param game
     * @param origin
     * @param lastMove
     * @param ghost
     * @return
     */
    public static MOVE towardsGhost(Game game, int origin, MOVE lastMove, GHOST ghost) {
        return towards(game, origin, game.getGhostCurrentNodeIndex(ghost), lastMove);
    }

    /**
     * Retorna el movimiento que más nos aleje de MsPacman desde 'origin'.
     * 
     * @param game
     * @param origin
     * @param lastMove
     * @return
     */
    public static MOVE awayFromPacman(Game game, int origin, MOVE lastMove) {
        return awayFrom(game, origin, game.getPacmanCurrentNodeIndex(), lastMove,
                game.getPacmanLastMoveMade());
    }

    /**
     * Retorna el movimiento que más nos aleje del fantasma desde 'origin'.
     * 
     * @param game
     * @param origin
     * @param lastMove
     * @param ghost
     * @return
     */
    public static MOVE awayFromGhost(Game game, int origin, MOVE lastMove, GHOST ghost) {
        return awayFrom(game, origin, game.getGhostCurrentNodeIndex(ghost), lastMove,
                game.getGhostLastMoveMade(ghost));
    }

    /**
     * Retorna el movimiento que más acerca desde origin hasta target
     * 
     * @param game
     * @param current Posición actual
     * @param target Posición a la que queremos acercarnos
     * @param originLastMove Último movimiento desde current
     * @return
     */
    public static MOVE towards(Game game, int current, int target, MOVE originLastMove) {
    	if(originLastMove!=null)
    		return towards(game, current, target, originLastMove, DM.PATH);
    	return MOVE.NEUTRAL;
    }

    /**
     * Retorna el movimiento que, desde origin, más nos aleja de target
     * 
     * @param game
     * @param current Posición actual
     * @param target Posición de la que alejarse
     * @param originLastMove Último movimiento desde current
     * @param targetLastMove Último movimiento de quien esté en target
     * @return
     */
    public static MOVE awayFrom(Game game, int current, int target, MOVE originLastMove,
            MOVE targetLastMove) {
        return awayFrom(game, current, target, originLastMove, targetLastMove, DM.PATH);
    }

    /**
     * Retorna el movimiento que más acerca desde origin hasta target
     * 
     * @param game
     * @param current Posición actual
     * @param target Posición a la que queremos acercarnos
     * @param originLastMove Último movimiento desde current
     * @param distanceMeasure Unidad de medida
     * @return
     */
    public static MOVE towards(Game game, int current, int target, MOVE originLastMove,
            DM distanceMeasure) {
        if (target == Paths.lairNode(game)) {
            //System.err.println("Warning: trying to move inside lair");
            return MOVE.NEUTRAL;
        }
        if (current == Paths.lairNode(game)) {
            //System.err.println("Warning: trying to move from lair");
            return MOVE.NEUTRAL;
        }
        int[] neighbouringNodes = game.getNeighbouringNodes(current, originLastMove);
        if (neighbouringNodes == null)
            return MOVE.NEUTRAL;

        double minDistance = Double.POSITIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : neighbouringNodes) {
            MOVE moveToNode = game.getMoveToMakeToReachDirectNeighbour(current, node);
            double distance =
                    game.getDistance(node, target, moveToNode, distanceMeasure);
            if (minDistance > distance) {
                minDistance = distance;
                bestMove = moveToNode;
            }
        }
        return bestMove;
    }

    /**
     * Retorna el movimiento que, desde origin, más nos aleja de target
     * 
     * @param game
     * @param current Posición actual
     * @param target Posición de la que alejarse
     * @param originLastMove Último movimiento desde current
     * @param targetLastMove Último movimiento de quien esté en target
     * @param distanceMeasure Unidad de medida
     * @return
     */
    public static MOVE awayFrom(Game game, int current, int target,
            MOVE originLastMove, MOVE targetLastMove, DM distanceMeasure) {
        if (target == Paths.lairNode(game)) {
            //System.err.println("Warning: trying to move inside lair");
            return MOVE.NEUTRAL;
        }
        if (current == Paths.lairNode(game)) {
            //System.err.println("Warning: trying to move from lair");
            return MOVE.NEUTRAL;
        }

        int[] neighbouringNodes = game.getNeighbouringNodes(current, originLastMove);
        if (neighbouringNodes == null)
            return MOVE.NEUTRAL;

        double maxDistance = Double.NEGATIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : neighbouringNodes) {
            double distance =
                    game.getDistance(target, node, targetLastMove, distanceMeasure);
            if (maxDistance < distance) {
                maxDistance = distance;
                bestMove = game.getMoveToMakeToReachDirectNeighbour(current, node);
            }
        }
        return bestMove;
    }

    /**
     * Devuelve un {@link MOVE} aleatorio.
     * 
     * @return
     */
    public static MOVE getRandomMove() {
        MOVE[] allMoves = MOVE.values();
        return allMoves[rnd.nextInt(allMoves.length)];
    }

    /**
     * Devuelve el movimiento en la dirección contraria.
     * 
     * @param move
     * @return
     */
    public static MOVE oppositeMove(MOVE move) {
        switch (move) {
            case UP:
                return MOVE.DOWN;
            case DOWN:
                return MOVE.UP;
            case LEFT:
                return MOVE.RIGHT;
            case RIGHT:
                return MOVE.LEFT;
            default:
                return MOVE.NEUTRAL;
        }
    }
}
