package es.ucm.fdi.ici.c2122.practica5.grupo09;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman.MsPacManCBRengineEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman.MsPacManInputEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman.MsPacManStorageManagerEX;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	MsPacManCBRengineEX cbrEngine;
	MsPacManStorageManagerEX storageManager;
	int level;
	public MsPacMan()
	{		
		setName("MsPacMan 09");

		this.storageManager = new MsPacManStorageManagerEX();
		cbrEngine = new MsPacManCBRengineEX(storageManager);
		level = 0;
	}
	
	@Override
	public void preCompute(String opponent) {
		cbrEngine.setLevel(level);
		cbrEngine.setOpponent(opponent);
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
		
		//level = game.getCurrentLevel();
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy
		if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
			return MOVE.NEUTRAL;
		
		
		try {
			MsPacManInputEX input = new MsPacManInputEX(game);
			input.parseInput();
			storageManager.setGame(game);
			
			//Cambio de nivel
			if(level != game.getCurrentLevel()) {
				level = game.getCurrentLevel();
				cbrEngine.changeLevel(level);
			}
			
			cbrEngine.cycle(input.getQuery());
			MOVE move = cbrEngine.getSolution();
			return move;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}

}
