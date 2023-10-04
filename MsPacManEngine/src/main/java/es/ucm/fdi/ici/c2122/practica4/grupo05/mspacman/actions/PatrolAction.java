package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.Random;
import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class PatrolAction  implements Action {
	private int limitForChasingGhost = 65;
	public PatrolAction() {
		// TODO Auto-generated constructor stub
	}

	   private Random rnd = new Random();
	    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
		allMoves= game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
		
		return  allMoves[rnd.nextInt(allMoves.length)];
	}

	@Override
	public String getActionId() {
		return "Patrol";
	}

	

}
