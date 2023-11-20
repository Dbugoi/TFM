package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class WanderAction implements RulesAction {
	GHOST ghost;
	private Random rnd = new Random();
	private MOVE[] allMoves = MOVE.values();

	public WanderAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	return allMoves[rnd.nextInt(allMoves.length)];
        }
        return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	@Override
	public String getActionId() {
		return ghost + " wanders";
	}
}
