package es.ucm.fdi.ici.c2122.practica5.grupo03;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts.GhostsCBRengineHuman;
import es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts.GhostsStorageManagerHuman;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsHuman extends GhostController {

	GhostsCBRengineHuman cbrEngine;
	GhostsStorageManagerHuman storageManagerGeneric;
		
	public GhostsHuman()
	{
		this.storageManagerGeneric = new GhostsStorageManagerHuman();
		cbrEngine = new GhostsCBRengineHuman(storageManagerGeneric);
	}
	
	@Override
	public void preCompute(String opponent) {
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
		
		//This implementation only computes a new action when Ghosts is in a junction. 
		//This is relevant for the case storage policy
		
		//GhostController ghosts = new es.ucm.fdi.ici.c2122.practica2.grupo03.Ghosts();
		
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		for(GHOST g: GHOST.values()) {
			if(!game.isJunction(game.getPacmanCurrentNodeIndex())) {
				moves.put(g, MOVE.NEUTRAL);
				continue;
			}
			
			try {
				GhostsInput input = new GhostsInput(game);
				input.parseInput();
				storageManagerGeneric.setGame(game);
				cbrEngine.cycle(input.getQuery());
				MOVE move = cbrEngine.getSolution();
				moves.put(g, move);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
			}
			moves.put(g, MOVE.NEUTRAL);
		}
		
		return moves;
		
	}

}
