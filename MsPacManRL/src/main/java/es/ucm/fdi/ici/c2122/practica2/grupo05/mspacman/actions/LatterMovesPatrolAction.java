package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class LatterMovesPatrolAction implements Action {
	private int limitForChasingGhost = 65;
	public LatterMovesPatrolAction() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public MOVE execute(Game game) {
		int mspacman = game.getPacmanCurrentNodeIndex();
	List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,game.getPacmanCurrentNodeIndex());
		

			
		
		
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		if(goodMoves.size()>1 && listChasingGhosts.size()==0 &&indexPowerPill!=-1 )
			goodMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexPowerPill, DM.PATH));

		List<MOVE> movesEscalera = new ArrayList<>();
		//escalera 
		int x= game.getNodeXCood(mspacman);
		int y= game.getNodeYCood(mspacman);
		
		if(x<=50 && y >=80)//abajo iz
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 960, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 972, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 776, game.getPacmanLastMoveMade(), DM.PATH));
		}
		else if(x>50 && y >=80)//abajo dr
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 996, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 980, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 728, game.getPacmanLastMoveMade(), DM.PATH));
		}
		else if(x<=50 && y <=40)//arriba iz
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 484, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 728, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 492, game.getPacmanLastMoveMade(), DM.PATH));
		}
		else if(x>50 && y <=40)//arriba dr
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 516, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 716, game.getPacmanLastMoveMade(), DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 492, game.getPacmanLastMoveMade(), DM.PATH));
		}
			
		
		if(goodMoves.contains(movesEscalera.get(0)))
				return movesEscalera.get(0);
		else if(goodMoves.contains(movesEscalera.get(1)))
				return movesEscalera.get(1);
		else if(goodMoves.contains(movesEscalera.get(2)))
				return movesEscalera.get(2);
		return MOVE.NEUTRAL;
		
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}
