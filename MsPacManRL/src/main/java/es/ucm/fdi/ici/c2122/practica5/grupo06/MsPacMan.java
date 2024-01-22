package es.ucm.fdi.ici.c2122.practica5.grupo06;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManStorageManager;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	MsPacManCBRengine cbrEngine;
	MsPacManStorageManager storageManager;
		
	public MsPacMan()
	{		
		this.storageManager = new MsPacManStorageManager();
		cbrEngine = new MsPacManCBRengine(storageManager);
		setTeam("Grupo 06");
		setName("Cobalt Weapon");
	}
	
	@Override
	public void preCompute(String opponent) {
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
		cbrEngine.setGame(game);
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy
		if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
			return MOVE.NEUTRAL;	
		
		try {
			MsPacManInput input = new MsPacManInput(game);
			input.parseInput();
			storageManager.setGame(game);
			cbrEngine.cycle(input.getQuery());
			MOVE move = cbrEngine.getSolution();
			return move;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}

}