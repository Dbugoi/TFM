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

public class Ghosts5variacion extends GhostController {

    private static final DM DIST_M = DM.PATH;

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);
        final int limit = 50;

        for (GHOST ghost : GHOST.values()) {
            MOVE move;
            
            // The current ghost is edible
            if (game.isGhostEdible(ghost)) {
            	
            	// There is some ghost that is not edible that can protect us
                if (isThereANonEdibleGhost(game))
                    move = moveTowardsClosestNonEdibleGhost(game, ghost);

                // If there is no protective ghosts and we are close to MsPacMac, we flee
                else if (distanceFromMsPacmanToGhost(game, ghost) < limit)
                    move = GameUtils.getNextMoveAwayFromMsPacMan(game, ghost, DIST_M);

                // If we are edible but we are far from MsPacMac we try to keep our distance with any being
                else
                    move = moveAwayFromMostOtherTargets(game, ghost);
            }
            
            // If the ghost is not edible and MsPacMac is closer to a PP than the ghost of MsPacMac, we better run away
            else if (isMsPacManCloserToPowerPillThanGhostToMsPacMan(game, ghost))
                move = moveAwayFromMostOtherTargets(game, ghost);
            
            // If the ghost is not edible and MsPacMac is far from PP
            else
                move = GameUtils.getNextMoveTowardsMsPacMan(game, ghost, DM.EUCLID);

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

    private double distanceFromMsPacmanToGhost(Game game, GHOST ghost) {
        return game.getDistance(game.getPacmanCurrentNodeIndex(),
                game.getGhostCurrentNodeIndex(ghost),
                game.getGhostLastMoveMade(ghost), DIST_M);
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
