package es.ucm.fdi.ici.c2122.practica1.grupo05Pruebas;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import com.google.common.collect.EnumMultiset;
import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts4 extends GhostController {

    private static final DM DIST_M = DM.PATH;

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

        for (GHOST ghost : GHOST.values()) {
            MOVE move;

            if (game.isGhostEdible(ghost)) {
                if (isThereANonEdibleGhost(game))
                    move = moveTowardsClosestNonEdibleGhost(game, ghost);
                else
                    move = moveAwayFromMostOtherTargets(game, ghost);
            }

            else if (isMsPacManCloserToPowerPillThanGhostToMsPacMan(game, ghost))
                move = moveAwayFromMostOtherTargets(game, ghost);

            else
                move = GameUtils.getNextMoveTowardsMsPacMan(game, ghost, DIST_M);

            moves.put(ghost, move);
        }
        return moves;
    }

    private boolean isThereANonEdibleGhost(Game game) {
        return Arrays.stream(GHOST.values())
                .anyMatch(g -> !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0);
    }

    private MOVE moveTowardsClosestNonEdibleGhost(Game game, GHOST ghost) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        ToIntFunction<GHOST> distToGhost =
                g -> (int) game.getDistance(ghostIndex, game.getGhostCurrentNodeIndex(g),
                        lastMove, DIST_M);

        Optional<GHOST> closestNonEdibleGhost = Arrays
                .stream(GHOST.values())
                .filter(g -> !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0)
                .sorted((a, b) -> distToGhost.applyAsInt(a) - distToGhost.applyAsInt(b))
                .findFirst();

        if (closestNonEdibleGhost.isPresent())
            return game.getApproximateNextMoveTowardsTarget(ghostIndex,
                    game.getGhostCurrentNodeIndex(closestNonEdibleGhost.get()), lastMove, DIST_M);
        else
            return GameUtils.getRandomMove();
    }

    /*
     * We want to get away from everyone else to minimize the number of losses when 'ghost' is
     * edible or might be soon because MsPacMan is close to a power pill.
     * 
     * Returns the MOVE that moves this ghost away from the most targets (other ghosts and
     * MsPacMan).
     */
    private MOVE moveAwayFromMostOtherTargets(Game game, GHOST ghost) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMoveMade = game.getGhostLastMoveMade(ghost);

        LinkedList<Integer> targetsCurrentIndices = new LinkedList<>();
        List<Integer> otherGhostsCurrentIndices = Arrays
                .stream(GHOST.values())
                .filter(g -> g != ghost && game.getGhostLairTime(g) == 0)
                .map(game::getGhostCurrentNodeIndex)
                .collect(Collectors.toList());

        targetsCurrentIndices.addAll(otherGhostsCurrentIndices);
        targetsCurrentIndices.add(game.getPacmanCurrentNodeIndex());

        List<MOVE> movesAwayFromEveryTarget = targetsCurrentIndices.stream()
                .map(i -> game.getNextMoveAwayFromTarget(ghostIndex, i, lastMoveMade, DIST_M))
                .collect(Collectors.toList());

        EnumMultiset<MOVE> movesAwayFromTargetsMultiSet = EnumMultiset.create(MOVE.class);
        movesAwayFromTargetsMultiSet.addAll(movesAwayFromEveryTarget);

        Optional<MOVE> mostRepeatedMove = movesAwayFromTargetsMultiSet.stream()
                .max((a, b) -> movesAwayFromTargetsMultiSet.count(a)
                        - movesAwayFromTargetsMultiSet.count(b));

        return mostRepeatedMove.orElse(GameUtils.getNextMoveAwayFromMsPacMan(game, ghost, DIST_M));
    }

    private boolean isMsPacManCloserToPowerPillThanGhostToMsPacMan(Game game, GHOST ghost) {
        if (game.getNumberOfActivePowerPills() == 0)
            return false;

        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMsPacManMove = game.getPacmanLastMoveMade();

        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastGhostMove = game.getGhostLastMoveMade(ghost);
        int closestPowerPillToMsPacMan = GameUtils.getClosestPowerPillToMsPacMan(game, DIST_M);

        return game.getDistance(pacmanIndex, closestPowerPillToMsPacMan, lastMsPacManMove,
                DIST_M) <= game.getDistance(ghostIndex, pacmanIndex, lastGhostMove, DIST_M);
    }
}
