package es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPowerPill implements RulesAction{
	
	public GoToPowerPill() {
	}
	
	@Override
	public String getActionId() {
		return "GoToPowerPill";
	}

	@Override
	public MOVE execute(Game game) {
		// TODO Auto-generated method stub
		int pacman= game.getPacmanCurrentNodeIndex();
		int [] ppills= game.getActivePowerPillsIndices();
		int [] pills= game.getActivePillsIndices();
		
		if(ppills.length>0) {
			int pill= game.getClosestNodeIndexFromNodeIndex(pacman, ppills, DM.PATH);
			return game.getNextMoveTowardsTarget(pacman, pill, DM.PATH);
		}
		else {
			int pill= game.getClosestNodeIndexFromNodeIndex(pacman, pills, DM.PATH);
			return game.getNextMoveTowardsTarget(pacman, pill, DM.PATH);
		}
		
		
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
	
	
}
