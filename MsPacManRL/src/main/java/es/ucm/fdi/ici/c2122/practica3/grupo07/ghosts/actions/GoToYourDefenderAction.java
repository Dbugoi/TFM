package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToYourDefenderAction implements RulesAction {

	GHOST ghost;
	GHOST defender;
	public GoToYourDefenderAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"go to your defender";
	}

	@Override
	public MOVE execute(Game game) {
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(defender), game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("defender");
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			defender = GHOST.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

}
