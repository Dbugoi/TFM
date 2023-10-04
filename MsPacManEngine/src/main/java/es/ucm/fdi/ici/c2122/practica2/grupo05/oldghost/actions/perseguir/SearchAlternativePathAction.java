package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.perseguir;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchAlternativePathAction implements Action {

    GHOST ghost;

    public SearchAlternativePathAction(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return ghost + " busca ruta alternativa";
    }

    @Override
    public MOVE execute(Game game) { // findAlternativePathTowardsMsPacmanWithFewestGhosts
        int nextJunction = GameUtils.getDestinationJunctionForMsPacman(game);
        if (game.getGhostCurrentNodeIndex(ghost) == nextJunction)
            return GameUtils.getNextMoveTowardsMsPacMan(game, ghost, DM.PATH);

        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMoveG = game.getGhostLastMoveMade(ghost);
        int[] neighbouringNodes = game.getNeighbouringNodes(
                ghostIndex,
                game.getGhostLastMoveMade(ghost));

        List<int[]> shortestPathsFromNeighbouringNodes = Arrays
                .stream(neighbouringNodes)
                .mapToObj(node -> game.getShortestPath(node, nextJunction, lastMoveG))
                .sorted((a, b) -> numberOfGhostsInPath(game, a) - numberOfGhostsInPath(game, b))
                .collect(Collectors.toList());

        if (!shortestPathsFromNeighbouringNodes.isEmpty()
                && shortestPathsFromNeighbouringNodes.get(0).length > 0) {
            int nextNode = shortestPathsFromNeighbouringNodes.get(0)[0];
            return game.getMoveToMakeToReachDirectNeighbour(ghostIndex, nextNode);
        } else
            return MOVE.NEUTRAL; // para que no me explote al cambiar de nivel
    }

    private int numberOfGhostsInPath(Game game, int[] path) {
        int count = 0;
        for (GHOST gh : GHOST.values())
            for (int index : path)
                if (index == game.getGhostCurrentNodeIndex(gh))
                    count++;

        return count;
    }
}
