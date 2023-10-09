package es.ucm.fdi.ici.c2122.practica5.grupo09;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts.GhostsCBRengineEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts.GhostsInputEX;
import es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts.GhostsStorageManagerEX;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	GhostsCBRengineEX cbrEngine;
	GhostsStorageManagerEX storageManager;
	int level;
	
	public Ghosts()
	{		
		setName("Ghosts 09");

		this.storageManager = new GhostsStorageManagerEX();
		cbrEngine = new GhostsCBRengineEX(storageManager);
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
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		//level = game.getCurrentLevel();
		//Llamamos 4 veces a la base de datos para que nos devuelva un movimiento por fantasma
		try {
			for(GHOST g: GHOST.values()) {
				if(game.doesGhostRequireAction(g)) {
					GhostsInputEX input = new GhostsInputEX(game);
					input.setGhost(g);
					input.parseInput();
					storageManager.setGame(game);
					
					//Cambio de nivel
					if(level != game.getCurrentLevel()) {
						level = game.getCurrentLevel();
						cbrEngine.changeLevel(level);
					}
					
					cbrEngine.cycle(input.getQuery());
					MOVE move = cbrEngine.getSolution();
					moves.put(g,move);
				}else {
					moves.put(g, MOVE.NEUTRAL);
				}
			}
			
			return moves;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Si falla por lo que sea
		for(GHOST g: GHOST.values()) {
			moves.put(g, MOVE.NEUTRAL);
		}
		
		return moves;
	}

}
