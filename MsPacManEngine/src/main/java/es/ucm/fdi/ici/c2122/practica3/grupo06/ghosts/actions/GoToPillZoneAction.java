package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToPillZoneAction implements RulesAction{
	GHOST ghost;
	public GoToPillZoneAction(GHOST ghost) {
		this.ghost = ghost;
		
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
               return getMoveToPillQuad(game);
        }
        return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + " going to region with pills";
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	public MOVE getMoveToPillQuad(Game game) {
		GameUtils.Quadrant[] qs = GameUtils.Quadrant.values();
		int ind = 0;
		int maxPills = GameUtils.getInstance().getPillsNumberInQuadrant(game, qs[ind]);
		int nextQuad = 0;
		for(int i = 1; i < qs.length; i++) {
			nextQuad = GameUtils.getInstance().getPillsNumberInQuadrant(game, qs[ind]);
			if(maxPills <= nextQuad){
				maxPills = nextQuad;
				ind = i;
			}
		}

		return game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost), 
					GameUtils.getInstance().getNearestPillInQuad(	game, 
													game.getGhostCurrentNodeIndex(ghost), 
													qs[ind]), 
					game.getGhostLastMoveMade(ghost), 
					DM.PATH);
	}
	
}
