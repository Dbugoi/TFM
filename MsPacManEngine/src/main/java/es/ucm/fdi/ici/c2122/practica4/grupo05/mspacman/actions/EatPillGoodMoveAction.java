package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.CheckIfGhost;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class EatPillGoodMoveAction implements Action {


	private MsPacManFuzzyMemory fuzzyMemory;
	
	 private Random rnd = new Random();
	 private MOVE[] allMoves = MOVE.values();
	public EatPillGoodMoveAction(MsPacManFuzzyMemory fuzzyMemory) {
		// TODO Auto-generated constructor stub
		this.fuzzyMemory=fuzzyMemory;
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		

		//int indexPill = PacmanMoves.getNearestPillWithoutChasingGhost(game,Arrays.asList(MOVE.values()));
		int indexPill = fuzzyMemory.getNearestPill();
		/*
	
		if(indexPill!=-1)
			return game.getNextMoveTowardsTarget(mspacman, indexPill,  game.getPacmanLastMoveMade(), DM.PATH);
		else {
			allMoves = game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade());
			return allMoves[rnd.nextInt(allMoves.length)];
		}
		*/
		//return game.getNextMoveTowardsTarget(mspacman, indexPill,  game.getPacmanLastMoveMade(), DM.PATH);
		return Moves.pacmanTowards(game, indexPill);
		}

	@Override
	public String getActionId() {
		return "EatPillGoodMove";
	}

	
	
	
}