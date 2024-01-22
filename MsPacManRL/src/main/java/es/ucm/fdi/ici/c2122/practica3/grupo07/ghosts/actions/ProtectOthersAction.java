package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
public class ProtectOthersAction implements RulesAction {
	
	private int protected_node;
	GHOST ghost;
	
	public ProtectOthersAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"protect others";
	}

	@Override
	public MOVE execute(Game game) {
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), protected_node, game.getGhostLastMoveMade(ghost), Util.path_measure);
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("node_info");
			//System.out.println("v: "+value);
			if(value == null)
				return;
			protected_node = value.intValue(null);
		} catch (JessException e) {
			e.printStackTrace();
		}	
	}

}
