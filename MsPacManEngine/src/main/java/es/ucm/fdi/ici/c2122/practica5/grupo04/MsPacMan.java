package es.ucm.fdi.ici.c2122.practica5.grupo04;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman.MsPacManCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman.MsPacManStorageManager;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	MsPacManCBRengine cbrEngine;
	MsPacManStorageManager storageManager;
	int curLevel = -1;
	Game game;
	
	public MsPacMan()
	{		
		this.storageManager = new MsPacManStorageManager();
		cbrEngine = new MsPacManCBRengine(storageManager);
		setName("MsPacMan 04");
		setTeam("PacmanCBRGrupo04");
		
	}
	
	@Override
	public void preCompute(String opponent) {
		cbrEngine.setOpponent(opponent);
		ReconfigureEngine(0);
	}
	
	
	private void ReconfigureEngine(int level) {
		cbrEngine.setLevel(level);
		cbrEngine.setGame(game);
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
		this.game = game;
		if(game.getCurrentLevel() != curLevel) {
			curLevel = game.getCurrentLevel();
			ReconfigureEngine(curLevel);
		}
		
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy
		if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
			return MOVE.NEUTRAL;
		
		try {
			MsPacManInput input = new MsPacManInput(game);
			input.parseInput();
			cbrEngine.setGame(game);
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
