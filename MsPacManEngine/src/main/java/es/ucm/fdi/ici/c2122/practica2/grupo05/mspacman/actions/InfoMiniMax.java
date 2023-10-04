package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.List;

import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class InfoMiniMax {

	private int p;
	private MOVE finalMove;
	private static int limitForChasingGhost = 50;
	
	public InfoMiniMax(int points, MOVE m) {
		this.p =points;
		this.finalMove = m;
	}
	
	public int getPoints() {
		return p;
	}
	
	public MOVE getMove() {
		return finalMove;
	}
	public void changeMove(MOVE m) {
		finalMove = m;;
	}
	
	public static InfoMiniMax minimax(Game game, int mspacman, MOVE mf, int depth, boolean maximizing, int alpha, int beta, GhostController ghosts) {
		InfoMiniMax info = new InfoMiniMax(game.getScore(), mf);
		int timeLimit = 40;
		if (game.gameOver() || depth ==0) {
			return info;
		} 
 
		if (maximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (MOVE m : game.getPossibleMoves(mspacman)) {
				if(!m.equals(GameUtils.opossiteMove(game, mspacman))) {
				Game newGame = game.copy();
				newGame.advanceGame(m, ghosts.getMove(game, System.currentTimeMillis() + timeLimit));
				
				info = minimax(newGame, newGame.getPacmanCurrentNodeIndex(), m, depth - 1, false, alpha, beta, ghosts);
				
				if(bestScore<info.getPoints())
				{ bestScore = Math.max(bestScore, info.getPoints());
				  info.changeMove(m);
				}
				alpha = Math.max(info.getPoints(), alpha);
				if (beta <= alpha)
					break;
				}
				
			}
			
			return info;
		} 
		else {
			int bestScore = Integer.MAX_VALUE;
			for (MOVE m :  game.getPossibleMoves(mspacman)) {
				if(!m.equals(GameUtils.opossiteMove(game, mspacman))) {
				Game newGame = game.copy();
				newGame.advanceGame(m, ghosts.getMove(game, System.currentTimeMillis() + timeLimit));
				
				info = minimax(newGame, newGame.getPacmanCurrentNodeIndex(), m, depth - 1, true, alpha, beta, ghosts);
					

				bestScore = Math.min(bestScore, info.getPoints());
				beta = Math.min(info.getPoints(), beta);
				if (beta <= alpha)
					break;
				}
				
			}
		return info;
		}
	}
	
	

}
