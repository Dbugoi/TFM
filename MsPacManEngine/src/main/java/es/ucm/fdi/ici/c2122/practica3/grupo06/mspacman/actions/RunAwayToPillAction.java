package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;

import java.util.List;
import es.ucm.fdi.ici.c2122.practica3.grupo06.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class RunAwayToPillAction extends RunAwayAction{
	@Override
	public String getActionId() { return "Ms PacMan runs for dear life towards pill."; }

	@Override
	public MOVE execute(Game game) { return selectPathToRunAway(game, GameUtils.getInstance().getNearest(game,
			GameUtils.getInstance().getPillsInRegion(game))); }
	
	public MOVE selectPathToRunAway(Game game, int destinationNode) {
		MOVE nextMove = MOVE.NEUTRAL, moveToTarget = GameUtils.getInstance().getNextMoveTowardsTarget(game, destinationNode);
        int pacmanNode = game.getPacmanCurrentNodeIndex();
        //debugPrint("Hay 2 o más fantasmas: ");
        List<Integer> ghostsToDestiny = GameUtils.getInstance().getNormalGhostsInDirection(game, moveToTarget);
        if (ghostsToDestiny.size() == 0) { nextMove = moveToTarget; }
        else { caseTwoOrMore(game,pacmanNode,destinationNode,moveToTarget, game.getPacmanLastMoveMade()); }    
        return nextMove;
	}
}
