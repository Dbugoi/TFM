package es.ucm.fdi.ici.c2122.practica5.grupo01;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman.MsPacManCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman.MsPacManStorageManager;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	MsPacManCBRengine cbrEngine;
	MsPacManStorageManager storageManager;
		
	public MsPacMan()
	{		
		setName("MsPacMan 01");
		this.storageManager = new MsPacManStorageManager();
		cbrEngine = new MsPacManCBRengine(storageManager);
	}
	
	@Override
	public void preCompute(String opponent) {
		cbrEngine.setOpponent(opponent);
		try {
			cbrEngine.configure();
			cbrEngine.preCycle(); // leemos y organizamos base de datos
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void postCompute() {
		try {
			cbrEngine.postCycle(); // cerramos conector
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// Esta implementacion obtiene nueva accion solo cuando MsPacman esta en una junction
		if(!game.isJunction(game.getPacmanCurrentNodeIndex())) // si no esta en junction, pacman sigue
			return MOVE.NEUTRAL;
		try {
			MsPacManInput input = new MsPacManInput(game);
			input.parseInput(); // obtenemos las variables
			storageManager.setGame(game);

			cbrEngine.cycle(input.getQuery()); // ejecutamos ciclo de CBR
			MOVE move = cbrEngine.getSolution(); // obtenemos solucion y lo devolvemos.
			return move;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}

}
