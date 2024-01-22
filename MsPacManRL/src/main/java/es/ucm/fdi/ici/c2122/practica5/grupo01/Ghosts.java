package es.ucm.fdi.ici.c2122.practica5.grupo01;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Ghosts extends GhostController {

	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManager;
		
	public Ghosts()
	{	
		setName("Ghosts 01");
		this.storageManager = new GhostsStorageManager();
		cbrEngine = new GhostsCBRengine(storageManager);
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
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		// Crea nueva accion cuando ghost esta en una junction.
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);

		for(GHOST g: GHOST.values()) {
			if(game.doesGhostRequireAction(g)) { // si no esta ni en junction ni en guarida
				try {
					GhostsInput input = new GhostsInput(game,g);
					input.parseInput(); // obtenemos atributos
					storageManager.setGame(game);

					cbrEngine.cycle(input.getQuery()); // generamos ciclo
					MOVE m = cbrEngine.getSolution();
					moves.put(g, m);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				moves.put(g, MOVE.NEUTRAL);		
			}
		}
		
		
		return moves;
	}

}
