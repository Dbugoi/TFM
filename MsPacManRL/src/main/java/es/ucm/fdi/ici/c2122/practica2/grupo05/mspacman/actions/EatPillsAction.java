package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.EnumMultiset;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class EatPillsAction implements Action {

	private int limitForChasingGhost = 65;
	private  int 	limitPill = 10000;
	public EatPillsAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		int indexPill = GameUtils.getNearestPillWithoutChasingGhost(game, goodMoves, this.limitPill);
		
		
		
			
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
			
		InfoMiniMax info = InfoMiniMax.minimax(game,  mspacman,  MOVE.NEUTRAL,  55, true,Integer.MIN_VALUE, Integer.MAX_VALUE, gc);
		/*
		if(goodMoves.size()==0) {
			goodMoves = GameUtils.getGoodMovesBackup(game, listChasingGhosts,mspacman);
		}
		*/
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
			
		if(goodMoves.size()>1 && listChasingGhosts.size()==0)
			goodMoves.remove(game.getNextMoveTowardsTarget(mspacman, indexPowerPill, DM.PATH));
		
		List<MOVE> movesAwayFromEachTarget = GameUtils.getMovesAwayFromEachTarget(game, mspacman, game.getPacmanLastMoveMade(), listChasingGhosts);

		EnumMultiset movesAwayFromTargetsMultiSet = EnumMultiset.create();
        movesAwayFromTargetsMultiSet.addAll(movesAwayFromEachTarget);

	     Optional<MOVE> mostRepeatedMove = GameUtils.getMostRepeatedMove(movesAwayFromTargetsMultiSet);
	     
		Random rnd = new Random();
		
	
		if(indexPill==-1 && goodMoves.size()==0 && listChasingGhosts.size()>0) {  // resumir estas 2 con minimax
			return mostRepeatedMove.orElse(game.getNextMoveAwayFromTarget(mspacman, game.getGhostCurrentNodeIndex(GameUtils.getClosestGhost(game,DM.PATH)), DM.PATH));
		}
		else if(indexPill==-1 && goodMoves.size()==0)
			return info.getMove();
		//else if(indexPill == -1 && goodMoves.size()>0 && goodMoves.contains(info.getMove()))
		//	return info.getMove();
		else if(indexPill == -1 && goodMoves.size()>0)
			return goodMoves.get(rnd.nextInt(goodMoves.size()));
		else if(goodMoves.contains(game.getNextMoveTowardsTarget(mspacman, indexPill, DM.PATH)))
			return game.getNextMoveTowardsTarget(mspacman, indexPill, DM.PATH);
		else if(goodMoves.size()>0)
			return goodMoves.get(rnd.nextInt(goodMoves.size()));
		else
			return MOVE.NEUTRAL;
		}

	@Override
	public String getActionId() {
		return "Eat Pills Action";
	}
	
	/*
	 * GhostController gc = new GhostController() {
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

		InfoMiniMax info = InfoMiniMax.minimax(game,  mspacman,  goodMoves,  20, true,Integer.MIN_VALUE, Integer.MAX_VALUE, gc);
		return info.getMove();
	 */
}