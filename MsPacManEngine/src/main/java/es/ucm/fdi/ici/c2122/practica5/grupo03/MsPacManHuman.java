package es.ucm.fdi.ici.c2122.practica5.grupo03;

import pacman.game.Constants.MOVE;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.game.Game;

import java.awt.event.KeyEvent;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManCBRengineHuman;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManStorageManagerHuman;

/*
 * Allows a human player to play the game using the arrow key of the keyboard.
 */
public class MsPacManHuman extends HumanController {

	MsPacManCBRengineHuman cbrEngine;
	MsPacManStorageManagerHuman storageManager;
	public KeyBoardInput input;

	public MsPacManHuman(KeyBoardInput input) {
		super(input);
        this.input = input;
		this.storageManager = new MsPacManStorageManagerHuman();
		cbrEngine = new MsPacManCBRengineHuman(storageManager);
	}
	
	@Override
	public void preCompute(String opponent) {
		try {
			cbrEngine.configure();
			cbrEngine.preCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void postCompute() {
		try {
			cbrEngine.postCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy
		if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
			return MOVE.NEUTRAL;
		
		MOVE move;
		
		switch (this.input.getKey()) {
        	case KeyEvent.VK_UP:
        		move = MOVE.UP;
        		break;
        		
        	case KeyEvent.VK_RIGHT:
        		move = MOVE.RIGHT;
        		break;
        		
        	case KeyEvent.VK_DOWN:
        		move = MOVE.DOWN;
        		break;
        		
        	case KeyEvent.VK_LEFT:
        		move = MOVE.LEFT;
        		break;
        		
        	default:
        		move = MOVE.NEUTRAL;
        		break;
		}
	
		try {
			MsPacManInput input = new MsPacManInput(game);
			input.parseInput();
 			storageManager.setGame(game);
			cbrEngine.cycle(input.getQuery(), move);
			return move;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}

}
