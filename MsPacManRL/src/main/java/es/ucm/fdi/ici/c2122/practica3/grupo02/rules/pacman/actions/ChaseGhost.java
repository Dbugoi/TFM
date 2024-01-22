package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actions;

import es.ucm.fdi.ici.c2122.practica2.grupo02.Tools;
import es.ucm.fdi.ici.c2122.practica3.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica3.grupo02.Utils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseGhost implements RulesAction {
	
	int chaseLimit = 50;
	public ChaseGhost() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "ChaseGhost";
	}

	@Override
	public MOVE execute(Game game) {
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		
		int ghostIndex = Utils.ghostNodeToFleeFrom;
		GHOST ghostType = Utils.ghostFleeFromType;
		
		if(ghostType != null) {
			int edibleTime = game.getGhostEdibleTime(ghostType);
			
			if(game.isGhostEdible(ghostType) && edibleTime > GameConstants.maxEdibleTime * 0.2) {
				return game.getApproximateNextMoveTowardsTarget(pacmanNode,
						ghostIndex, game.getPacmanLastMoveMade(), DM.PATH);
			}
		}
		
		return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
