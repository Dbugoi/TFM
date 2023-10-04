package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.NearestEdibleGhostInformation;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PacmanMoves;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class RunAwayCageAction implements Action {

	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 55;
	private int cage = 492;
	private MsPacManFuzzyMemory fuzzyMemory;
	public RunAwayCageAction(MsPacManFuzzyMemory f) {
		// TODO Auto-generated constructor stub
		this.fuzzyMemory= f;
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		return Moves.pacmanAwayFrom(game, 492);
		//return game.getApproximateNextMoveAwayFromTarget(mspacman,cage, game.getPacmanLastMoveMade(), DM.PATH);

	}

	@Override
	public String getActionId() {
		return "RunAwayCage";
	}

	
}