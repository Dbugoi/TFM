package es.ucm.fdi.ici.c2122.practica5.grupo07;


import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman.MsPacManCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman.MsPacManStorageManager;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {


	MsPacManCBRengine cbrEngine;
	MsPacManStorageManager storageManager;
	
	public MsPacMan(){
		this.setName("Catch me if u can, Ms ghost");
		this.storageManager = new MsPacManStorageManager();
		cbrEngine = new MsPacManCBRengine(storageManager);
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
		long startTime = System.currentTimeMillis();
		
		storageManager.pacmanEaten(game);
		if(game.isJunction(game.getPacmanCurrentNodeIndex())) {
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
		}
		
		
		//System.out.println(System.currentTimeMillis()-startTime);
		return MOVE.NEUTRAL;
		
	}

}
