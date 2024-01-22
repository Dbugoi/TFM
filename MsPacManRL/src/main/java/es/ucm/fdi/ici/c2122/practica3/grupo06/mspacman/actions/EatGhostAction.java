package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;
import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatGhostAction implements RulesAction {

	@Override
	public String getActionId() { return "Ms PacMan chases ghost"; }

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		nextMove = game.getApproximateNextMoveTowardsTarget(
				game.getPacmanCurrentNodeIndex(), 
				GameUtils.getInstance().getNearest(game, GameUtils.getInstance().getEdibleGhosts(game)), 
				game.getPacmanLastMoveMade(), 
				DM.PATH);
		
		/*
		int maxGhosts = 0;
		for (MOVE m : MOVE.values()) {
			int aux = GameUtils.getInstance().getEdibleGhostsInDirection(game, m).size();
			if (maxGhosts < aux) {
				maxGhosts = aux;
				nextMove  = m;
			}
		}
		*/
		return nextMove;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
}
