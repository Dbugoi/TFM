package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;
import java.awt.Color;

public class EncloseAction implements RulesAction {
	int target;
	GHOST ghost;
	public EncloseAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"enclose";
	}

	@Override
	public MOVE execute(Game game) {
		if(Util.DEBUG)
			GameView.addLines(game, Color.PINK, game.getGhostCurrentNodeIndex(ghost), target);
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), target, game.getGhostLastMoveMade(ghost), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("node_info");
			//System.out.println("v: "+value);
			if(value == null)
				return;
			target = value.intValue(null);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

}
