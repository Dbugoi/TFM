package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica3.grupo05.NearestEdibleGhostInformation;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;

public class RunAwayCageAction implements RulesAction {

	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 55;
	private int cage = 492;
	public RunAwayCageAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		NearestEdibleGhostInformation edibleGhostsINFO = GhostFinder.getNearestEdibleGhosts(game, limitForEdibleGhost);
		
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);
		
		if(goodMoves.contains(game.getNextMoveTowardsTarget(mspacman, cage, DM.PATH)))
			goodMoves.remove(game.getNextMoveTowardsTarget(mspacman, cage, DM.PATH));
		
		
		Random rnd = new Random();
		if(goodMoves.size()==0)
			return MOVE.NEUTRAL; //mejorar con minimax
		else
			return goodMoves.get(rnd.nextInt(goodMoves.size()));
	}

	@Override
	public String getActionId() {
		return "RunAwayCage";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}