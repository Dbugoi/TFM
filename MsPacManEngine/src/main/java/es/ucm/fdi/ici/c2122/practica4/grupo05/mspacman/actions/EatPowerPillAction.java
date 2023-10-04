package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PacmanMoves;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatPowerPillAction  implements Action {


	private MsPacManFuzzyMemory fuzzyMemory;
	public EatPowerPillAction(MsPacManFuzzyMemory fuzzyMemory) {
		// TODO Auto-generated constructor stub
		this.fuzzyMemory=fuzzyMemory;
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();

		//int indexPowerPill = PacmanMoves.getNearestPowerPill(game);
		int indexPowerPill = fuzzyMemory.getNearestPP();
		//MOVE m= game.getNextMoveTowardsTarget(mspacman, indexPowerPill,  game.getPacmanLastMoveMade(),DM.PATH);
		//return m;
		return Moves.pacmanTowards(game, indexPowerPill);
	}

	@Override
	public String getActionId() {
		return "EatPowerPill";
	}

	
}