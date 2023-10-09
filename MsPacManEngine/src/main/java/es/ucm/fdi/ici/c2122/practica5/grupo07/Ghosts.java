package es.ucm.fdi.ici.c2122.practica5.grupo07;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController{
	
	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManager;
	public Ghosts(){
		setName("Artificially retarded");
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
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE> (GHOST.class);
		for(GHOST g:GHOST.values()) {
			storageManager.pacmanEaten(game);
			if(game.getGhostLairTime(g)==0&&game.isJunction(game.getGhostCurrentNodeIndex(g))) {
				try {
					GhostsInput input = new GhostsInput(game,g);
					input.parseInput();
					storageManager.setGame(game,g);
					cbrEngine.cycle(input.getQuery());
					MOVE move = cbrEngine.getSolution();
					moves.put(g, move);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		//System.out.println(System.currentTimeMillis()-startTime);
		return moves;
	}

}
