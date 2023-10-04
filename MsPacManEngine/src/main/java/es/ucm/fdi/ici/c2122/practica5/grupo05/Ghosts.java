package es.ucm.fdi.ici.c2122.practica5.grupo05;

import java.util.EnumMap;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts.GhostsStorageManager;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.CheckIfGhost;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManagerBase;
	GhostsStorageManager storageManagerSpecific;

	EnumMap<GHOST, MOVE> moves;

	public Ghosts() {
		setName(NullPointerException.class.getName());

		this.storageManagerBase = new GhostsStorageManager(500);
		this.storageManagerSpecific = new GhostsStorageManager(5000);

		cbrEngine = new GhostsCBRengine(storageManagerBase, storageManagerSpecific);
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
		cbrEngine.setGame(game);
		moves = new EnumMap<>(GHOST.class);
		for (GHOST g : GHOST.values()) {
			if (game.wasGhostEaten(g) || game.wasPacManEaten()) {
				cbrEngine.reviseLastCases(game, g);
			}
			
			if (!game.doesGhostRequireAction(g)) {
				moves.put(g, MOVE.NEUTRAL);
			} else {
				try {
					GhostsInput input = new GhostsInput(game, g);
					input.parseInput();

					storageManagerBase.setGame(game);
					storageManagerSpecific.setGame(game);

					CBRQuery query = input.getQuery();
					cbrEngine.cycle(g, query);
					moves.put(g, cbrEngine.getSolution(g));

				} catch (ExecutionException e) {
					e.printStackTrace();
					moves.put(g, MOVE.NEUTRAL);
				}
			}
		}
		return moves;
	}

}
