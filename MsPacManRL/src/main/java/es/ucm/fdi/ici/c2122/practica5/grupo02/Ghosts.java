package es.ucm.fdi.ici.c2122.practica5.grupo02;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;

import es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts.GhostsStorageManager;

import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.controllers.GhostController;

public class Ghosts extends GhostController {

	EnumMap<GHOST, GhostsCBRengine> cbrEngines;
	EnumMap<GHOST, GhostsStorageManager> storageManagers;

	public Ghosts()
	{		
		setName("Ghosts 02");
		this.cbrEngines = new EnumMap<GHOST, GhostsCBRengine>(GHOST.class);
		this.storageManagers = new EnumMap<GHOST, GhostsStorageManager>(GHOST.class);
		
		for(GHOST ghost : GHOST.values()) {
			GhostsStorageManager tempStrGhost = new GhostsStorageManager();
					
			this.storageManagers.put(ghost, tempStrGhost);
			this.cbrEngines.put(ghost, new GhostsCBRengine(tempStrGhost));
		}
	}

	@Override
	public void preCompute(String opponent) {
		for (GHOST ghost : GHOST.values()) {
			cbrEngines.get(ghost).setOpponent(opponent);

			try {
				cbrEngines.get(ghost).configure();
				cbrEngines.get(ghost).preCycle();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void postCompute() {
		for (GHOST ghost : GHOST.values()) {
			try {
				cbrEngines.get(ghost).postCycle();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		GhostsInput input = new GhostsInput(game);
		
		for (GHOST ghost : GHOST.values()) {
			// This implementation only computes a new action when Ghost is in a
			// junction. This is relevant for the case storage policy
			if (!game.doesGhostRequireAction(ghost)) {
				ghostMoves.put(ghost, MOVE.NEUTRAL);
				continue;				
			}

			try {
				input.setGhost(ghost);
				input.parseInput();
				
				storageManagers.get(ghost).setGame(game);
				cbrEngines.get(ghost).cycle(input.getQuery());
				
				MOVE move = cbrEngines.get(ghost).getSolution();
				ghostMoves.put(ghost, move);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ghostMoves.put(ghost, MOVE.NEUTRAL);
		}
		
		return ghostMoves;
	}

}
