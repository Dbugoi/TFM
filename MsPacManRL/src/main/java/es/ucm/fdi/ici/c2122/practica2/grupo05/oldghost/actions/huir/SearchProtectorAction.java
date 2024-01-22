package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.ToIntFunction;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SearchProtectorAction implements Action{
	
	GHOST ghost;
	public SearchProtectorAction( GHOST ghost) {
		this.ghost = ghost;
	}
	
	@Override
	public String getActionId() {
		return ghost + " busca a Ghost Protector";
	}

	@Override
	public MOVE execute(Game game) { // moveTowardsClosestNonEdibleGhostNotInLair
		System.out.println(ghost + " busca a Ghost Protector");
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE lastMove = game.getGhostLastMoveMade(ghost);
        ToIntFunction<GHOST> distToGhost =
                g -> (int) game.getDistance(ghostIndex, game.getGhostCurrentNodeIndex(g),
                        lastMove, DM.PATH);

        Optional<GHOST> closestNonEdibleGhostThatIsNotInLair = Arrays
                .stream(GHOST.values())
                .filter(g -> !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0)
                .sorted((a, b) -> distToGhost.applyAsInt(a) - distToGhost.applyAsInt(b))
                .findFirst();
        
        //MOVIMIENTO PARA IR AL GHOST PROTECTOR
        MOVE aux1 = game.getApproximateNextMoveTowardsTarget(ghostIndex,
                game.getGhostCurrentNodeIndex(closestNonEdibleGhostThatIsNotInLair.get()),
                lastMove, DM.PATH);
        //MOVIMIENTO PARA HUIR DE PACMAN
        MOVE aux2 = game.getNextMoveAwayFromTarget(ghostIndex, game.getPacmanCurrentNodeIndex(),
        		lastMove, DM.PATH);
        
        if (closestNonEdibleGhostThatIsNotInLair.isPresent() && aux1 != aux2)
            return aux1;
        else
            return aux2;
	}

}
