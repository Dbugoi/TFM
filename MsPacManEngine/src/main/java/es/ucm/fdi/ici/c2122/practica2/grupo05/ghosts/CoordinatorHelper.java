package es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

class CoordinatorHelper {
    private CoordinatorHelper() {}

    public static Map<GHOST, MoveToNodeInfo> assignNodesMinimizingTotalDistance(Game game,
            List<GHOST> ghosts, List<Integer> targetNodes) {
        Map<GHOST, MoveToNodeInfo[]> ghostToMTNInfoArray =
                CoordinatorHelper.createGhostToMTNInfoArray(game, ghosts, targetNodes);

        Map<GHOST, MoveToNodeInfo> bestGhostToMTJInfo =
                CoordinatorHelper.findConfigurationThatMinimizesDistance(ghostToMTNInfoArray,
                        ghosts,
                        targetNodes);
/*
        for (GHOST ghost : ambushers) {
            moves.put(ghost, bestGhostToMTJInfo.get(ghost).firstMoveTowardsNode);
            // int to = bestGhostToMTJInfo.get(ghost).junction;
            // GameView.addPoints(game, GameViewUtils.getGhostColor(ghost), to);
        }*/
        return bestGhostToMTJInfo;
    }

    private static Map<GHOST, MoveToNodeInfo[]> createGhostToMTNInfoArray(Game game,
            List<GHOST> ambushers, List<Integer> targetJunctions) {
        Map<GHOST, MoveToNodeInfo[]> moveToJunctionInfoForEachGhost =
                new EnumMap<>(GHOST.class);

        for (GHOST ghost : ambushers) {
            MoveToNodeInfo[] mtjInfo = new MoveToNodeInfo[targetJunctions.size()];
            int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
            MOVE lastMove = game.getGhostLastMoveMade(ghost);

            for (int i = 0; i < targetJunctions.size(); i++) {
                mtjInfo[i] = getShortestPathDistanceAndMoveWithoutPacmanInPath(game, ghostIndex,
                        targetJunctions.get(i), lastMove);
            }
            moveToJunctionInfoForEachGhost.put(ghost, mtjInfo);
        }
        return moveToJunctionInfoForEachGhost;
    }

    private static Map<GHOST, MoveToNodeInfo> findConfigurationThatMinimizesDistance(
            Map<GHOST, MoveToNodeInfo[]> ghostToMTNInfoArray, List<GHOST> ghosts,
            List<Integer> targetNodes) {
        int numNodes = targetNodes.size();
        GHOST[] ghostsArray = ghosts.toArray(new GHOST[0]);
        int[] nodesArray = targetNodes.stream().mapToInt(i -> i).toArray();

        ToDoubleFunction<int[]> sumDistances = (int[] ghostToNode) -> {
            // ghostToJunction[i] = cruce del fantasma ambushersArray[i]
            double res = 0;
            for (int i = 0; i < ghostToNode.length; i++)
                res += ghostToMTNInfoArray
                        .get(ghostsArray[i])[ghostToNode[i]].distanceToNode;
            return res;
        };

        Predicate<int[]> configIsValid = (int[] ghostToNode) -> {
            // ghostToJunction[i] = cruce del fantasma ambushersArray[i]
            List<Integer> seenJunctions = new ArrayList<>(numNodes);
            for (int junction : ghostToNode)
                if (!seenJunctions.contains(junction))
                    seenJunctions.add(junction);
            return seenJunctions.size() == Integer.min(numNodes, ghosts.size());
        };

        // length of each config: number of ambusher ghosts
        List<int[]> configs = new ArrayList<>();
        createAllPossibleConfigs(ghostsArray, nodesArray, configs);
        Optional<int[]> chosenGhostToNodeConfig = configs.stream()
                .filter(configIsValid)
                .min((a, b) -> (int) (sumDistances.applyAsDouble(a)
                        - sumDistances.applyAsDouble(b)));

        if (chosenGhostToNodeConfig.isPresent()) {
            Map<GHOST, MoveToNodeInfo> bestConfig = new EnumMap<>(GHOST.class);
            for (int gh = 0; gh < ghostsArray.length; gh++) {
                int junctionIndex = chosenGhostToNodeConfig.get()[gh];
                MoveToNodeInfo info =
                        ghostToMTNInfoArray.get(ghostsArray[gh])[junctionIndex];
                bestConfig.put(ghostsArray[gh], info);
            }
            return bestConfig;
        } else
            throw new IllegalStateException("No config found");
    }

    private static void createAllPossibleConfigs(GHOST[] ghosts, int[] targetNodes,
            List<int[]> configs) {
        createAllPossibleConfigs(ghosts, targetNodes, configs, 0, new ArrayList<>());
    }

    private static void createAllPossibleConfigs(GHOST[] ghosts, int[] targetNodes,
            List<int[]> configs, int gIndex, List<Integer> configToBeBuilt) {
        if (gIndex >= ghosts.length) {
            configs.add(configToBeBuilt.stream().mapToInt(i -> i).toArray());
        } else {
            for (int jIndex = 0; jIndex < targetNodes.length; jIndex++) {
                List<Integer> newConfig = new ArrayList<>(configToBeBuilt);
                newConfig.add(jIndex);
                createAllPossibleConfigs(ghosts, targetNodes, configs, gIndex + 1, newConfig);
            }
        }
    }

    private static MoveToNodeInfo getShortestPathDistanceAndMoveWithoutPacmanInPath(Game game,
            int origin, int destiny, MOVE lastMove) {
        MOVE nextMove = game.getNextMoveTowardsTarget(origin, destiny, lastMove, DM.PATH);

        Predicate<int[]> pacmanInPath = path -> {
            for (int i : path)
                if (i == game.getPacmanCurrentNodeIndex())
                    return true;
            return false;
        };

        double minDistance = Double.POSITIVE_INFINITY;
        int[] neighbouringNodes = game.getNeighbouringNodes(origin, lastMove);

        for (int node : neighbouringNodes) {
            MOVE moveToNode = game.getMoveToMakeToReachDirectNeighbour(origin, node);
            int[] otherPath = game.getShortestPath(node, destiny, moveToNode);
            double distance = game.getShortestPathDistance(node, destiny, moveToNode);

            if (distance < minDistance && !pacmanInPath.test(otherPath)) {
                minDistance = distance;
                nextMove = moveToNode;
            }
        }
        return new MoveToNodeInfo(destiny, minDistance, nextMove);

    }

    static class MoveToNodeInfo {
        final int node;
        final double distanceToNode;
        final MOVE firstMoveTowardsNode;

        public MoveToNodeInfo(int junction, double distanceToJunction,
                MOVE firstMoveTowardsJunction) {
            this.distanceToNode = distanceToJunction;
            this.firstMoveTowardsNode = firstMoveTowardsJunction;
            this.node = junction;
        }
    }
}
