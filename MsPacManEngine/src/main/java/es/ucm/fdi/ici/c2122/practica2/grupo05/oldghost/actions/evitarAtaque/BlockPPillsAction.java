package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.evitarAtaque;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.IntUnaryOperator;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BlockPPillsAction implements Action { 

	GHOST ghost;
	public BlockPPillsAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return ghost + " block PPill";
	}

	@Override
	public MOVE execute(Game game) { // En la transicion nos aseguramos de que siempre haya un cruce y de que llegamos antes que Pacman
		
		int indxGhost = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastGhostMove = game.getGhostLastMoveMade(ghost);
		
		int[] ppillPath = getPathToClosestPowerPillToMsPacMan(game);		
		int lastJunction = getAlmostLastJunctionToClosestPowerPillToMsPacMan(game, ppillPath);		
		
		return game.getNextMoveTowardsTarget(indxGhost, lastJunction, lastGhostMove, DM.PATH);
	}

    public int[] getPathToClosestPowerPillToMsPacMan(Game game) {
    	int[] pathClosestPPill = null;
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();

        int closestPill = getClosestPowerPillToMsPacMan(game);        

        if(closestPill != -1)
        	pathClosestPPill  = game.getShortestPath(pacmanIndex, closestPill, lastMove);
        	
        return pathClosestPPill;
    }
    
    public int getClosestPowerPillToMsPacMan(Game game) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        IntUnaryOperator distToMsPacman =
                i -> (int) game.getDistance(pacmanIndex, i, lastMove, DM.PATH);

        Optional<Integer> closestPill = Arrays
                .stream(game.getActivePowerPillsIndices())
                .boxed()
                .min((p1, p2) -> distToMsPacman.applyAsInt(p1) - distToMsPacman.applyAsInt(p2));

        return closestPill.orElse(-1);

    }    
   
    public int getAlmostLastJunctionToClosestPowerPillToMsPacMan(Game game, int[] path) {
    	int lastJunction = -1;
    	int almostLastJunction = -1;
    	for(int indx: path) {
    		if(game.isJunction(indx)) {
    			almostLastJunction = lastJunction;
    			lastJunction = indx;
    			}
    	}
    	if(almostLastJunction != -1)
    		return almostLastJunction;    
    	else
    		return lastJunction;   
    }
}
