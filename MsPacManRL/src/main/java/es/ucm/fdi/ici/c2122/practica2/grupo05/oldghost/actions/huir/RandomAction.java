package es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomAction implements Action{

	private Random rnd = new Random();
	private MOVE[] allMoves = MOVE.values();
	GHOST ghost;
	
    public RandomAction(GHOST ghost) {
    	this.ghost = ghost;
    }
    
	@Override
	public String getActionId() {
		return ghost + " random action";
	}

	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)];
	}

}
