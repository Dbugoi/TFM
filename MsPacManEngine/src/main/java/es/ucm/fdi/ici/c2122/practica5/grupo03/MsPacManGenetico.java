package es.ucm.fdi.ici.c2122.practica5.grupo03;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica5.grupo03.mspacman.MsPacManStorageManager;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManGenetico extends PacmanController {

	MsPacManCBRengine cbrEngine;
	MsPacManStorageManager storageManagerGeneric;
	MsPacManStorageManager storageManagerSpecific;
	
	public MsPacManGenetico(Vector<Double> pesos, int sd)
	{
		this.storageManagerGeneric = new MsPacManStorageManager(100);
		this.storageManagerSpecific = new MsPacManStorageManager(100);
		cbrEngine = new MsPacManCBRengine(storageManagerGeneric, storageManagerSpecific, pesos);
	}

	@Override
	public void preCompute(String opponent) {
		cbrEngine.setOpponent(opponent); // bases de casos especializadas 
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
		
		
		try {
			MsPacManInput input = new MsPacManInput(game);
			input.parseInput();
			storageManagerGeneric.setGame(game);
			storageManagerSpecific.setGame(game);
			cbrEngine.cycle(input.getQuery());
			MOVE move = cbrEngine.getSolution();
			return move;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}

}
