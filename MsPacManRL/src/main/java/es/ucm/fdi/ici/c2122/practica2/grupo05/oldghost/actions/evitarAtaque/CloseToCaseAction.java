package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.evitarAtaque;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica1.grupo05Pruebas.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class CloseToCaseAction implements Action{

	GHOST ghost;

	public CloseToCaseAction(GHOST ghost) {
		this.ghost = ghost;
	}
	@Override
	public String getActionId() {
		return ghost + "is going to case";
	}

	@Override
	public MOVE execute(Game game) { //findAlternativePathTowardsJailWithFewestGhosts		
        return GameUtils.getNextMoveAwayFromMsPacMan(game, ghost, DM.PATH);
        /*
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMoveG = game.getGhostLastMoveMade(ghost);
		
        int[] neighbouringNodes = game.getNeighbouringNodes(
                ghostIndex,
                game.getGhostLastMoveMade(ghost));

        List<int[]> shortestPathsFromNeighbouringNodes = Arrays
                .stream(neighbouringNodes)
                .mapToObj(node -> game.getShortestPath(node, game.getGhostInitialNodeIndex(),lastMoveG))
                .sorted((a, b) -> numberOfGhostsInPath(game, a) - numberOfGhostsInPath(game, b))
                .collect(Collectors.toList());
        
        if(!shortestPathsFromNeighbouringNodes.isEmpty() && shortestPathsFromNeighbouringNodes.get(0).length >= 1 && ghostIndex != -1) {
        	int nextNode = shortestPathsFromNeighbouringNodes.get(0)[0];
        	return game.getMoveToMakeToReachDirectNeighbour(ghostIndex, nextNode);
        }else
        	return MOVE.NEUTRAL;*/
	}
	
	private int numberOfGhostsInPath(Game game, int[] path) {
        int count = 0;
        for (GHOST ghost : GHOST.values())
            for (int index : path)
                if (index == game.getGhostCurrentNodeIndex(ghost))
                    count++;

        return count;
    }

}
