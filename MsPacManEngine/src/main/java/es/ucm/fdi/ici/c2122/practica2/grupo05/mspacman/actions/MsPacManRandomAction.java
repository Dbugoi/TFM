package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManRandomAction implements Action {

	public MsPacManRandomAction() {
		// TODO Auto-generated constructor stub
	}

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)]; //mejorar con minimax?
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}
