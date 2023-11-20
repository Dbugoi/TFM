package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;
import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPillsInRegionAction implements RulesAction {

	@Override
	public String getActionId() { return "Ms PacMan looks for the nearest pill in its quadrant"; }

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = game.getApproximateNextMoveTowardsTarget(
			game.getPacmanCurrentNodeIndex(), 
			GameUtils.getInstance().getNearestPillInRegion(game), 
			game.getPacmanLastMoveMade(), 
			DM.PATH
		);

		///*
		//evitar que vaya hacia la power Pill:
		if (GameUtils.getInstance().getPillsInRegion(game).size() > GameUtils.getInstance().getPPillBanLimit() && 
				game.getCurrentLevelTime() <= 400 && game.getActivePowerPillsIndices().length > 0 && 
				GameUtils.getInstance().getNextMoveTowardsTarget(game, GameUtils.getInstance().getNearestPPillInRegion(game)) == nextMove && 
				game.getDistance(game.getPacmanCurrentNodeIndex(),GameUtils.getInstance().getNearestPowerPillIndex(game), 
						game.getPacmanLastMoveMade(),DM.EUCLID) < GameUtils.getInstance().getCloserLimit()) { 
			nextMove = game.getApproximateNextMoveAwayFromTarget(
				game.getPacmanCurrentNodeIndex(),GameUtils.getInstance().getNearestPPillInRegion(game),game.getPacmanLastMoveMade(),DM.PATH);
		}//*/
		return nextMove;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
}
