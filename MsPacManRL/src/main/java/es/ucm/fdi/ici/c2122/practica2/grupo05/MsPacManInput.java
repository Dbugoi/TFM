package es.ucm.fdi.ici.c2122.practica2.grupo05;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.ici.Input;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacManInput extends Input {

	private boolean nearEdibleGhost; // si existe un Ghost cercano comestible
	private boolean nearEdibleGhost2; // si existe un Ghost cercano comestible
	private boolean nearChasingGhost; // si existe un Ghost cercano NO comestible
	//private boolean nearPPill; // si existe una PPill cercana
	private boolean nearDangerousCage;
	private boolean nearPill; // si existe una Pill cercana
	private boolean nearPPill; // si existe una Pill cercana
	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 10000000;
	private int limitForEdibleGhost2 = 55;
	private int 	limitPill = 4000000;
	private boolean edibleGhostIsToGoingChange;
	
	//
	private boolean nearDeath;
	private boolean noPillNoGoodMove;
	private boolean noPillGoodMove;
	private boolean pillGoodMove;
	private boolean randomGoodMove;
	
	//
	private boolean noGoodMoves;
	private boolean movesToEatGhosts;
	
	//
	private boolean eatPPill;
	
	//
	private boolean doLatterMoves;
	public MsPacManInput(Game game) {
		super(game);
		nearChasingGhost =false;
		nearEdibleGhost =false;
		nearPill=false;
		nearPPill=false;
		edibleGhostIsToGoingChange = false;
		nearEdibleGhost2=false;
		nearDeath=false;
		noPillNoGoodMove=false;
		noPillGoodMove=false;
		noGoodMoves=false;
		movesToEatGhosts=false;
		eatPPill = false;
		doLatterMoves=false;
		
		NearestEdibleGhostInformation edibleGhostsINFO = GameUtils.getNearestEdibleGhosts(game, this.limitForEdibleGhost);
		int mspacman = game.getPacmanCurrentNodeIndex();
		List<Integer> listChasingGhosts = GameUtils.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
	
		List<GHOST> listEdibleGhosts= new ArrayList<>(edibleGhostsINFO.getListNearestEdibleGhost());
		
	
		List<MOVE> movesToEat = new ArrayList<>();
		
		
		if(listEdibleGhosts.size()>0)
		{
			
			for(GHOST ghost: listEdibleGhosts) {
				MOVE m =game.getApproximateNextMoveTowardsTarget(mspacman, game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(),DM.PATH);
				if(goodMoves.contains(m));
					movesToEat.add(m);
			}
		}
		if(movesToEat.size()>0)
			movesToEatGhosts=true;
		
		if(goodMoves.size()==0) 
			noGoodMoves=true;
		int indexPowerPill1 = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		MOVE m= game.getNextMoveTowardsTarget(mspacman, indexPowerPill1, DM.PATH);
		if(goodMoves.contains(m))
			eatPPill = true;
		
		if(GameUtils.getNearestChasingGhosts(game, limitForChasingGhost).size()>0)
			nearChasingGhost =true;

		if(edibleGhostsINFO.getListNearestEdibleGhost().size()>0)
			nearEdibleGhost =true;
	
		if(GameUtils.getNearestPillWithoutChasingGhost(game, goodMoves,limitPill )!=-1)
			nearPill =true;
		
		if(GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves)!=-1 &&!existsGhostInLair(game))
			nearPPill =true;

		if(edibleGhostsINFO.getMinDistanceNearestGhost()*1.85>= edibleGhostsINFO.getMinEdibleTimeNearestGhost() )
			edibleGhostIsToGoingChange = true;
		
		List<MOVE> movesEscalera = new ArrayList<>();
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
		if(((x<=50 && y >=80) ||(x>50 && y >=80)||(x<=50 && y <=40) ||(x>50 && y <=40)) &&
				(listChasingGhosts.size()>=2)
				&& (goodMoves.contains(movesEscalera.get(0))
						|| goodMoves.contains(movesEscalera.get(1) )
						|| goodMoves.contains(movesEscalera.get(2))))
			doLatterMoves= true;
	
		nearDangerousCage=GameUtils.nearDangerousCage(game);
		
		edibleGhostsINFO = GameUtils.getNearestEdibleGhosts(game, this.limitForEdibleGhost2);
		if(edibleGhostsINFO.getListNearestEdibleGhost().size()>0)
			nearEdibleGhost2 =true;
			

		
		List<MOVE> goodMoves1 = GameUtils.getGoodMoves(game, listChasingGhosts,mspacman);
		
		
		int indexPill = GameUtils.getNearestPillWithoutChasingGhost(game, goodMoves1, this.limitPill);
		int indexPowerPill = GameUtils.getNearestPowerPillWithoutChasingGhost(game, goodMoves1);
			
		if(goodMoves1.size()>1 && listChasingGhosts.size()==0)
			goodMoves1.remove(game.getNextMoveTowardsTarget(mspacman, indexPowerPill, DM.PATH));
		
		if(indexPill==-1 && goodMoves1.size()==0 && listChasingGhosts.size()>0) {  // resumir estas 2 con minimax
			nearDeath=true;		
			//return mostRepeatedMove.orElse(game.getNextMoveAwayFromTarget(mspacman, game.getGhostCurrentNodeIndex(GameUtils.getClosestGhost(game,DM.PATH)), DM.PATH));
		}
		else if(indexPill==-1 && goodMoves1.size()==0) {
			noPillNoGoodMove=true;
		}
		else if(indexPill == -1 && goodMoves1.size()>0) {
			noPillGoodMove=true;
		}
			
		else if(goodMoves1.contains(game.getNextMoveTowardsTarget(mspacman, indexPill, DM.PATH))) {
			pillGoodMove=true;
		}
			
		else{
			randomGoodMove=true;
		}
		
		
			
	}

	private boolean existsGhostInLair(Game game) {
		for(GHOST g: GHOST.values())
			if(game.getGhostLairTime(g)>0)
				return true;
						
		return false;
	}
	@Override
	public void parseInput() {
		// does nothing.

		
		
	}
	
	public boolean existsNearEdibleGhosts() {
		return nearEdibleGhost;
	}
	public boolean existsNearEdibleGhosts2() {
		return nearEdibleGhost2;
	}
	public boolean existsNearChasingGhosts() {
		return nearChasingGhost;
	}
	public boolean existsNearPill() {
		return nearPill;
	}
	public boolean existsNearPowerPill() {
		return nearPPill;
	}
	public boolean eatPowerPill() {
		return eatPPill;
	}
	public boolean dangerEdibleGhost() {
		return edibleGhostIsToGoingChange;
	}
	public boolean doLatterMoves() {
		return doLatterMoves;
	}
	public boolean nearDangerousCage() {
		return nearDangerousCage;
	}
	public boolean nearDeath() {
		return nearDeath;
	}
	public boolean noPillNoGoodMove() {
		return noPillNoGoodMove;
	}
	public boolean noPillGoodMove() {
		return noPillGoodMove;
	}
	public boolean pillGoodMove() {
		return pillGoodMove;
	}
	public boolean randomGoodMove() {
		return randomGoodMove;
	}
	public boolean noGoodMoves() {
		return noGoodMoves;
	}
	public boolean movesToEatGhosts() {
		return movesToEatGhosts;
	}
	
}
