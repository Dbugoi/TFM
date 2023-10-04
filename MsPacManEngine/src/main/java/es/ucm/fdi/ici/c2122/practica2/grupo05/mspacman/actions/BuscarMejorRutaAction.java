package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import es.ucm.fdi.ici.c2122.practica2.grupo05.OldGhosts;
import es.ucm.fdi.ici.c2122.practica2.grupo05.NearestEdibleGhostInformation;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Game;

public class BuscarMejorRutaAction implements Action {

	private int limitForChasingGhost = 55;
	
	public BuscarMejorRutaAction() {
		// TODO Auto-generated constructor stub
	}

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
		List<Integer> listNearestChasingGhost = GameUtils.getNearestChasingGhosts(game, this.limitForChasingGhost);
		//List<PATH> listNearestChasingGhost = GameUtils.getNearestChasingGhosts(game, this.limitForChasingGhost);
		
		return allMoves[rnd.nextInt(allMoves.length)];
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}
	
	private InfoMiniMax minimax(Game game, int mspacman, List<MOVE> goodMoves, int depth, boolean maximizing, int alpha, int beta, GhostController ghosts) {
		InfoMiniMax info = new InfoMiniMax(game.getScore(), MOVE.NEUTRAL);
		int timeLimit = 40;
		if (game.gameOver()) {
			return info;
		} 
 
		if (maximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (MOVE m : goodMoves) {
				Game newGame = game.copy();
				newGame.advanceGame(m, ghosts.getMove(game, System.currentTimeMillis() + timeLimit));
				List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(newGame, limitForChasingGhost);
				goodMoves = GameUtils.getGoodMoves(newGame, listChasingGhosts,mspacman);
				
				info = minimax(newGame, mspacman, goodMoves, depth - 1, false, alpha, beta, ghosts);
				
				bestScore = Math.max(bestScore, info.getPoints());
				alpha = Math.max(info.getPoints(), alpha);
				if (beta <= alpha)
						break;
				
			}
			return info;
		} 
		else {
			int bestScore = Integer.MAX_VALUE;
			for (MOVE m : goodMoves) {
				Game newGame = game.copy();
				newGame.advanceGame(m, ghosts.getMove(game, System.currentTimeMillis() + timeLimit));
				List<Integer>  listChasingGhosts = GameUtils.getNearestChasingGhosts(newGame, limitForChasingGhost);
				goodMoves = GameUtils.getGoodMoves(newGame, listChasingGhosts,mspacman);
				
				info = minimax(newGame, mspacman, goodMoves, depth - 1, false, alpha, beta, ghosts);
					

				bestScore = Math.min(bestScore, info.getPoints());
				beta = Math.min(info.getPoints(), beta);
				if (beta <= alpha)
					break;
				
			}
		return info;
		}
	}
}


/*

def minimax(agent, depth, gameState):
 
    # if the game has finished return the value of the state
    if gameState.isLose() or gameState.isWin() or depth == max_depth:
        return evaluationFunction(gameState)
 
    # if it's the maximizer's turn (the player is the agent 0)
    if agent == 0:
        # maximize player's reward and pass the next turn to the first ghost (agent 1)
        return max(minimax(1, depth, gameState.generateSuccessor(agent, action)) for action in getLegalActionsNoStop(0, gameState))
 
    # if it's the minimizer's turn (the ghosts are the agent 1 to num_agents)
    else:
        nextAgent = agent + 1  # get the index of the next agent
        if num_agents == nextAgent:  # if all agents have moved, then the next agent is the player
            nextAgent = 0
        if nextAgent == 0:  # increase depth every time all agents have moved
            depth += 1
        # minimize ghost's reward and pass the next ghost or the player if all ghosts have already moved
        return min(self.minimax(nextAgent, depth, gameState.generateSuccessor(agent, action)) for action in
                   getLegalActionsNoStop(agent, gameState))


*/