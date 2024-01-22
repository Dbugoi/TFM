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

public class Ghosts8 extends GhostController {
    private static final int LIMIT = 150;
    private static final DM DIST_M = DM.PATH;

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

        for (GHOST ghost : GHOST.values()) {
            MOVE move;

            if (game.isGhostEdible(ghost)) {
                if (isThereANonEdibleGhostThatIsNotInLair(game))
                    // ghost moves towards a non edible ghost to protect itself
                    move = moveTowardsClosestNonEdibleGhostNotInLair(game, ghost);

                //else if (distanceFromMsPacmanToGhost(game, ghost) < LIMIT)
                    // if mspacman is too close, we flee from her
                    //move = GameUtils.getNextMoveAwayFromMsPacMan(game, ghost, DIST_M);

                else
                    // we seek to disperse the ghosts to reduce the number of casualties
                    move = moveAwayFromMostOtherTargets(game, ghost);
            }

            else if (isMsPacManCloserToPowerPillThanGhostToMsPacMan(game, ghost))
                // if mspacman is closer to a PP than our ghost is to her, we don't bother
                // chasing her
                move = moveAwayFromMostOtherTargets(game, ghost);

            else {
                // else, just chase mspacman
                move = attack(game, ghost);
                GameViewUtils.addVisualPathFromGhostToMsPacMan(game, ghost,
                        GameViewUtils.getGhostColor(ghost));
            }

            moves.put(ghost, move);
        }
        return moves;
    }

    private boolean isThereANonEdibleGhostThatIsNotInLair(Game game) {
        return Arrays.stream(GHOST.values())
                .anyMatch(g -> !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0);
    }

    private double distanceFromMsPacmanToGhost(Game game, GHOST ghost) {
        return game.getDistance(game.getPacmanCurrentNodeIndex(),
                game.getGhostCurrentNodeIndex(ghost),
                game.getGhostLastMoveMade(ghost), DIST_M);
    }

    private MOVE moveTowardsClosestNonEdibleGhostNotInLair(Game game, GHOST ghost) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        ToIntFunction<GHOST> distToGhost =
                g -> (int) game.getDistance(ghostIndex, game.getGhostCurrentNodeIndex(g),
                        lastMove, DIST_M);

        Optional<GHOST> closestNonEdibleGhostThatIsNotInLair = Arrays
                .stream(GHOST.values())
                .filter(g -> !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0)
                .sorted((a, b) -> distToGhost.applyAsInt(a) - distToGhost.applyAsInt(b))
                .findFirst();

        if (closestNonEdibleGhostThatIsNotInLair.isPresent())
            return game.getApproximateNextMoveTowardsTarget(ghostIndex,
                    game.getGhostCurrentNodeIndex(closestNonEdibleGhostThatIsNotInLair.get()),
                    lastMove, DIST_M);
        else
            // should not happen if we know already there is a non edible ghost outside
            // the lair, but just in case
            return GameUtils.getRandomMove();
    }

    /*
     * We want to get away from everyone else to minimize the number of losses when 'ghost' is
     * edible or might be soon because MsPacMan is close to a power pill.
     * 
     * Returns the MOVE that moves this ghost away from the most targets (other ghosts and
     * MsPacMan). We give a bit more weight to MsPacMan's position to help escape from her.
     */
    private MOVE moveAwayFromMostOtherTargets(Game game, GHOST ghost) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMoveMade = game.getGhostLastMoveMade(ghost);

        LinkedList<Integer> targetsCurrentIndices = new LinkedList<>();
        List<Integer> otherNotInLairGhostsCurrentIndices =
                getOtherNotInLairGhostsCurrentIndices(game, ghost);

        targetsCurrentIndices.addAll(otherNotInLairGhostsCurrentIndices);
        // we add mspacman's index twice to give more importance to her position
        //targetsCurrentIndices.add(game.getPacmanCurrentNodeIndex());
        targetsCurrentIndices.add(game.getPacmanCurrentNodeIndex());

        List<MOVE> movesAwayFromEachTarget =
                getMovesAwayFromEachTarget(game, ghostIndex, lastMoveMade, targetsCurrentIndices);

        EnumMultiset<MOVE> movesAwayFromTargetsMultiSet = EnumMultiset.create(MOVE.class);
        movesAwayFromTargetsMultiSet.addAll(movesAwayFromEachTarget);

        Optional<MOVE> mostRepeatedMove = getMostRepeatedMove(movesAwayFromTargetsMultiSet);

        return mostRepeatedMove.orElse(GameUtils.getNextMoveAwayFromMsPacMan(game, ghost, DIST_M));
    }

    private List<Integer> getOtherNotInLairGhostsCurrentIndices(Game game, GHOST ghost) {
        return Arrays
                .stream(GHOST.values())
                .filter(g -> g != ghost && game.getGhostLairTime(g) == 0)
                .map(game::getGhostCurrentNodeIndex)
                .collect(Collectors.toList());
    }

    private List<MOVE> getMovesAwayFromEachTarget(Game game, int ghostIndex, MOVE lastMoveMade,
            LinkedList<Integer> targetsCurrentIndices) {
        return targetsCurrentIndices.stream()
                .map(i -> game.getNextMoveAwayFromTarget(ghostIndex, i, lastMoveMade, DIST_M))
                .collect(Collectors.toList());
    }

    private Optional<MOVE> getMostRepeatedMove(EnumMultiset<MOVE> movesAwayFromTargetsMultiSet) {
        return movesAwayFromTargetsMultiSet.stream()
                .max((a, b) -> movesAwayFromTargetsMultiSet.count(a)
                        - movesAwayFromTargetsMultiSet.count(b));
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
                DIST_M) < game.getDistance(ghostIndex, pacmanIndex, lastGhostMove, DIST_M);
    }

    private MOVE attack(Game game, GHOST ghost) {
        int[] shortestPath = game.getShortestPath(
                game.getGhostCurrentNodeIndex(ghost),
                game.getPacmanCurrentNodeIndex(),
                game.getGhostLastMoveMade(ghost));

        double distanceToMsPacman = GameUtils.distanceToMsPacman(game, ghost, DIST_M);

        if (distanceToMsPacman < LIMIT && anyOtherNonEdibleGhostIsInPath(game, shortestPath, ghost))
            return findSecondShortestPathTowardsMsPacman(game, ghost);
        else
            return GameUtils.getNextMoveTowardsMsPacMan(game, ghost, DIST_M);
    }

    private boolean anyOtherNonEdibleGhostIsInPath(Game game, int[] path, GHOST ghost) {
        for (GHOST otherGhost : GHOST.values())
            if (otherGhost != ghost && !game.isGhostEdible(otherGhost))
                for (int index : path)
                    if (index == game.getGhostCurrentNodeIndex(otherGhost))
                        return true;

        return false;
    }

    private MOVE findSecondShortestPathTowardsMsPacman(Game game, GHOST ghost) {
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        int[] neighbouringNodes = game.getNeighbouringNodes(
                ghostIndex,
                game.getGhostLastMoveMade(ghost));

        List<int[]> shortestPathsFromNeighbouringNodes = Arrays
                .stream(neighbouringNodes)
                .mapToObj(node -> game.getShortestPath(node, game.getPacmanCurrentNodeIndex()))
                .sorted((a, b) -> a.length - b.length)
                .collect(Collectors.toList());


        if (shortestPathsFromNeighbouringNodes.size() >= 2)
            shortestPathsFromNeighbouringNodes.remove(0);
        int nextNode =
                shortestPathsFromNeighbouringNodes.get(0)[0];

        return game.getMoveToMakeToReachDirectNeighbour(ghostIndex, nextNode);
    }

}
