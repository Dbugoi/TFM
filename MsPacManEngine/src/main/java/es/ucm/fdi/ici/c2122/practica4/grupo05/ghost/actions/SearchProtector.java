package es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions;


import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.GhostsFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;

import java.util.Optional;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class SearchProtector implements Action {
	private final GhostsFuzzyMemory fuzzyMemory;
	private final GHOST ghost;

	public SearchProtector(GhostsFuzzyMemory f, GHOST g) {
		this.fuzzyMemory = f;
		this.ghost = g;
	}

	@Override
	public MOVE execute(Game game) {
		Optional<GHOST> toFollow = fuzzyMemory.getProtectorForGhost(ghost);
		if (toFollow.isPresent())
			return Moves.ghostTowardsGhost(game, ghost, toFollow.get());
		else
			return Moves.getRandomMove();
	}


	@Override
	public String getActionId() {
		return "SearchProtector";
	}

}
