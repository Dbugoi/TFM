package es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions;

import java.util.Optional;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.GhostsFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class KillPacman implements Action {
	private final GhostsFuzzyMemory fuzzyMemory;
	private final GHOST ghost;

    public KillPacman(GhostsFuzzyMemory f, GHOST g) {
		this.fuzzyMemory=f;
		this.ghost=g;
	}

    @Override
    public MOVE execute(Game game) {
    	Optional<Target> pacman = fuzzyMemory.getMostLikelyPosition().map(FuzzyValue::getValue);
    	if (pacman.isPresent()) {
    		return Moves.ghostTowards(game, ghost, pacman.get().getNode());
    	} else {
    		return Moves.getRandomMove();
    	}
    }

    @Override
    public String getActionId() {
        return "Kill MsPacman";
    }
}
