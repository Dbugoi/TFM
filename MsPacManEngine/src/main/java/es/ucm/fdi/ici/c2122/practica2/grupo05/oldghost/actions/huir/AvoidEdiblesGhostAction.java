package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.EnumMultiset;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AvoidEdiblesGhostAction implements Action {

	private static final DM DIST_M = DM.PATH;

	GHOST ghost;

	public AvoidEdiblesGhostAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return ghost + " avoid Edibles and MsPacMan";
	}

	@Override						
	public MOVE execute(Game game) { // moveAwayFromMostOtherTargets
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMoveMade = game.getGhostLastMoveMade(ghost);

		LinkedList<Integer> targetsCurrentIndices = new LinkedList<>();
		List<Integer> otherNearEdibleGhostsCurrentIndices = getOtherNearEdibleGhostsCurrentIndices(game, ghost);

		targetsCurrentIndices.addAll(otherNearEdibleGhostsCurrentIndices);
		// we add mspacman's index twice to give more importance to her position
		// targetsCurrentIndices.add(game.getPacmanCurrentNodeIndex());
		targetsCurrentIndices.add(game.getPacmanCurrentNodeIndex());

		List<MOVE> movesAwayFromEachTarget = getMovesAwayFromEachTarget(game, ghostIndex, lastMoveMade,
				targetsCurrentIndices);

		EnumMultiset movesAwayFromTargetsMultiSet = EnumMultiset.create();
		movesAwayFromTargetsMultiSet.addAll(movesAwayFromEachTarget);

		Optional<MOVE> mostRepeatedMove = getMostRepeatedMove(movesAwayFromTargetsMultiSet);

		return mostRepeatedMove.orElse(GameUtils.getNextMoveAwayFromMsPacMan(game, ghost, DIST_M));
	}
	
	
	private List<Integer> getOtherNearEdibleGhostsCurrentIndices(Game game, GHOST ghost) {

		ArrayList<Integer> aux = new ArrayList<>();
        for (GHOST g : GHOST.values()) 
        	if(g != ghost && game.isGhostEdible(g) && game.getGhostCurrentNodeIndex(g) < GameUtils.getLimitForEdibleGhost())
        		aux.add(game.getGhostCurrentNodeIndex(g));
		
        return aux;
    }

    private List<MOVE> getMovesAwayFromEachTarget(Game game, int ghostIndex, MOVE lastMoveMade,
            LinkedList<Integer> targetsCurrentIndices) {
        return targetsCurrentIndices.stream()
                .map(i -> game.getNextMoveAwayFromTarget(ghostIndex, i, lastMoveMade, DIST_M))
                .collect(Collectors.toList());
    }

    private Optional<MOVE> getMostRepeatedMove(EnumMultiset movesAwayFromTargetsMultiSet) {
        return movesAwayFromTargetsMultiSet.stream()
                .max((a, b) -> movesAwayFromTargetsMultiSet.count(a)
                        - movesAwayFromTargetsMultiSet.count(b));
    }

}
