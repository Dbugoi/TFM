package es.ucm.fdi.ici.c2122.practica5.grupo04;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManager;
	int curLevel = -1;
	Game game;
	
	public Ghosts() {
		this.storageManager = new GhostsStorageManager();
		cbrEngine = new GhostsCBRengine(storageManager);
		setName("Ghosts 04");
		setTeam("GhostsCBRGrupo04");
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
	
	
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		
		this.game = game;
		if(game.getCurrentLevel() != curLevel) {
			curLevel = game.getCurrentLevel();
			ReconfigureEngine(curLevel);
		}
		
		moves.clear();
		for (GHOST ghostType : GHOST.values()) {
			if (game.doesGhostRequireAction(ghostType)) {
				
				
				//This implementation only computes a new action when Ghost is in a junction. 
				//This is relevant for the case storage policy
				if(!game.isJunction(game.getGhostCurrentNodeIndex(ghostType)))
					moves.put(ghostType, MOVE.NEUTRAL);
				
				try {
					GhostsInput input = new GhostsInput(game);
					input.parseInput();
					storageManager.setGame(game);
					input.setGhost(ghostType.ordinal());
					cbrEngine.cycle(input.getQuery());
					MOVE move = cbrEngine.getSolution();
					moves.put(ghostType, move);
				} catch (Exception e) {
					e.printStackTrace();
				}
				moves.put(ghostType, MOVE.NEUTRAL);
			}
		}
		return moves;
	}
}
