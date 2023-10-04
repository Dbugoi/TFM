package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.nousadas;

import java.util.EnumMap;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.InfoMiniMax;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class BestPatrolAction implements Action {

	public BestPatrolAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
	
		
		GhostController gc = new GhostController() {
			 
			EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);
			@Override
			public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
				// TODO Auto-generated method stub
				for(GHOST g : GHOST.values()) 
					if(!game.isGhostEdible(g))
						 moves.put(g, game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), DM.PATH));
					else 
						moves.put(g, game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), DM.PATH));
				return moves;
			}
		};
			
		InfoMiniMax info = InfoMiniMax.minimax(game,game.getPacmanCurrentNodeIndex(),MOVE.NEUTRAL,45,true,Integer.MIN_VALUE,Integer.MAX_VALUE,gc);
		
		return info.getMove();
	}

	@Override
	public String getActionId() {
		return "Best Patrol Action";
	}

}
