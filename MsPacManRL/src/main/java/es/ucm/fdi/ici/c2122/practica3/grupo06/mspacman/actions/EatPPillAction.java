package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;
import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatPPillAction implements RulesAction{

	@Override
	public String getActionId() { return "Ms PacMan looks for nearest power pill."; }

	@Override
	public MOVE execute(Game game) {
		return game.getApproximateNextMoveTowardsTarget(
			game.getPacmanCurrentNodeIndex(), 
			GameUtils.getInstance().getNearestPowerPillIndex(game), 
			game.getPacmanLastMoveMade(), 
			DM.PATH
		);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
}
