package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PacmanMoves;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class AvoidPowerPillAction  implements Action {

	
	private MsPacManFuzzyMemory fuzzyMemory;
	
	public AvoidPowerPillAction(MsPacManFuzzyMemory fuzzyMemory) {
		// TODO Auto-generated constructor stub
		this.fuzzyMemory=fuzzyMemory;
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();

		//int indexPowerPill = PacmanMoves.getNearestPowerPill(game);
		int indexPowerPill = fuzzyMemory.getNearestPP();
		MOVE mf=MOVE.NEUTRAL;
		
		//MOVE m= game.getNextMoveAwayFromTarget(mspacman, indexPowerPill, game.getPacmanLastMoveMade(), DM.PATH);
		if(game.getNodeYCood(indexPowerPill)<20)
			mf= game.getNextMoveTowardsTarget(mspacman, 716, game.getPacmanLastMoveMade(), DM.PATH);
		else
			mf= game.getNextMoveTowardsTarget(mspacman, 189, game.getPacmanLastMoveMade(), DM.PATH);
		return mf;
	
	}

	@Override
	public String getActionId() {
		return "AvoidPowerPill";
	}

	
}