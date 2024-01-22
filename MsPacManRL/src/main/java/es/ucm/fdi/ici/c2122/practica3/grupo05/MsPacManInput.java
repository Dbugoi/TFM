package es.ucm.fdi.ici.c2122.practica3.grupo05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;


import es.ucm.fdi.ici.c2122.practica3.grupo05.NearestEdibleGhostInformation;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.CheckIfGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.CheckIfPacman;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacManInput extends RulesInput{

	private boolean nearEdibleGhost; // si existe un Ghost cercano comestible
	private boolean nearChasingGhost; // si existe un Ghost cercano NO comestible
	//private boolean nearPPill; // si existe una PPill cercana
	private boolean nearDangerousCage;
	private boolean nearEdibleGhost2; // si existe un Ghost cercano comestible
	private boolean nearPill; // si existe una Pill cercana
	private boolean nearPPill; // si existe una Pill cercana
	private int limitForChasingGhost = 65;
	private int limitForEdibleGhost = 10000000;
	private int limitForEdibleGhost2 = 55;
	private int 	limitPill = 4000000; //Funciona mejor si no limitamos las pills
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
	private boolean doLatterMoves;
	private boolean escape;
	private boolean betterGoToPill;
	private int mindistanceChasingGhost;
	private int mindistancePowerPill;
	
	public MsPacManInput(Game game) {
		super(game);
	}

	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		
		facts.add(String.format("(BASIC_INFORMATION (mindistanceChasingGhostOrChasingGhostInCage %d) (mindistancePowerPill %d))", (int)this.mindistanceChasingGhost, (int)this.mindistancePowerPill));
		//facts.add(String.format("(BASIC_INFORMATION )", (int)this.mindistancePowerPill));
		
		if(game.wasPacManEaten()) {

		facts.add(String.format("(MSPACMAN (Chase %s) (RunAway %s)  (RunAwayFromCage %s) (Patrol %s) (MsPacManReappearsAction %s))",false, false, false, false, true));
		
		facts.add(String.format("(PRECISE_FACT_MSPACMANREAPPEARS (MsPacManReappearsAction %s))", false));
		}
		else if(existsNearEdibleGhosts()) { //Chase
		facts.add(String.format("(MSPACMAN (Chase %s) (RunAway %s)  (RunAwayFromCage %s) (Patrol %s) (MsPacManReappearsAction %s))",true, false, false, false, false));
			
		
		facts.add(String.format("(PRECISE_FACT_CHASE (BestMoveToEatChaseGhost %s))", existsNearEdibleGhosts() && !noGoodMoves()));
		facts.add(String.format("(PRECISE_FACT_CHASE (ChaseGhostAction %s))", existsNearEdibleGhosts() && !noGoodMoves() && !movesToEatGhosts()));
		facts.add(String.format("(PRECISE_FACT_CHASE (EatPowerPillActionChase %s))", (!existsNearEdibleGhosts() || dangerEdibleGhost())&&(existsNearPowerPill()&& eatPowerPill() &&(existsNearChasingGhosts()))&&! movesToEatGhosts()));
		facts.add(String.format("(PRECISE_FACT_CHASE (NoGoodMovesChaseGhostAction %s))",existsNearEdibleGhosts() && noGoodMoves() &&  !movesToEatGhosts() ));
		
		}
		else if(nearDangerousCage()) //cage
		{
		facts.add(String.format("(MSPACMAN (Chase %s) (RunAway %s)  (RunAwayFromCage %s) (Patrol %s) (MsPacManReappearsAction %s))",false, false, true, false, false));
			
			
		facts.add(String.format("(PRECISE_FACT_RUNAWAYCAGE (RunAwayCageAction %s))",nearDangerousCage() ));
		
		
		}
		else if(existsNearChasingGhosts() && !existsNearEdibleGhosts()) //Run Away
		{
		facts.add(String.format("(MSPACMAN (Chase %s) (RunAway %s)  (RunAwayFromCage %s) (Patrol %s) (MsPacManReappearsAction %s))",false, true, false, false, false));
			
			//System.out.println("RunAway");

		facts.add(String.format("(PRECISE_FACT_RUNAWAY (noPillNoGoodMovesEatState %s))",existsNearChasingGhosts() && !existsNearEdibleGhosts() && noPillNoGoodMove() && !pillGoodMove()));
		facts.add(String.format("(PRECISE_FACT_RUNAWAY (eatPowerPillStateRun %s))",  (!existsNearEdibleGhosts() || dangerEdibleGhost())&&(existsNearPowerPill()&& eatPowerPill() &&(existsNearChasingGhosts()))&&! movesToEatGhosts()));
		facts.add(String.format("(PRECISE_FACT_RUNAWAY (EscapeNearDeathEatAction %s))", existsNearChasingGhosts() && !existsNearEdibleGhosts() && nearDeath()));
		facts.add(String.format("(PRECISE_FACT_RUNAWAY (NoPillGoodMovesAction %s))", existsNearChasingGhosts() && !existsNearEdibleGhosts() && noPillNoGoodMove() && !pillGoodMove()));
		facts.add(String.format("(PRECISE_FACT_RUNAWAY (EatPillGoodMoveAction %s))",existsNearChasingGhosts() && !existsNearEdibleGhosts() && pillGoodMove() ));
		facts.add(String.format("(PRECISE_FACT_RUNAWAY (RandomGoodMoveEatAction %s))", existsNearChasingGhosts() && !existsNearEdibleGhosts() && randomGoodMove() && !pillGoodMove()));
		facts.add(String.format("(PRECISE_FACT_RUNAWAY (BetterGoToPill %s))", betterGoToPill&& pillGoodMove() && CheckIfGhost.existsGhostInLair(game)));
		
		}
		else if(!existsNearChasingGhosts() && !existsNearEdibleGhosts() ) //patrol
		{
		facts.add(String.format("(MSPACMAN (Chase %s) (RunAway %s)  (RunAwayFromCage %s) (Patrol %s) (MsPacManReappearsAction %s))",false, false, false, true, false));
			
		//	System.out.println("Patrol");

		facts.add(String.format("(PRECISE_FACT_PATROL (EatPillGoodMoveActionPatrol %s))", !existsNearChasingGhosts() && !existsNearEdibleGhosts() && pillGoodMove()));
		facts.add(String.format("(PRECISE_FACT_PATROL (LatterMovesPatrolAction %s))", !existsNearChasingGhosts() && !existsNearEdibleGhosts() && !noGoodMoves() && doLatterMoves()));
		facts.add(String.format("(PRECISE_FACT_PATROL (PatrolAction %s))", !existsNearChasingGhosts() && !existsNearEdibleGhosts() && !noGoodMoves() && !doLatterMoves() &&!pillGoodMove()));
		facts.add(String.format("(PRECISE_FACT_PATROL (NoGoodMovesPatrolAction %s))",!existsNearChasingGhosts() && !existsNearEdibleGhosts() && noGoodMoves() && !doLatterMoves() ));
		facts.add(String.format("(PRECISE_FACT_PATROL (EscapeNearDeathEatActionPatrol %s))", !existsNearChasingGhosts() && !existsNearEdibleGhosts() && nearDeath()));
		facts.add(String.format("(PRECISE_FACT_PATROL (NoPillNoGoodMovesEatAction %s))",!existsNearChasingGhosts() && !existsNearEdibleGhosts() && noPillNoGoodMove() && !pillGoodMove() ));
		facts.add(String.format("(PRECISE_FACT_PATROL (NoPillGoodMovesActionPatrol %s))",!existsNearChasingGhosts() && !existsNearEdibleGhosts() && noPillGoodMove() && !pillGoodMove() ));
		facts.add(String.format("(PRECISE_FACT_PATROL (RandomGoodMoveEatActionPatrol %s))", !existsNearChasingGhosts() && !existsNearEdibleGhosts() && randomGoodMove() && !pillGoodMove()));
		facts.add(String.format("(PRECISE_FACT_PATROL (BetterGoToPill %s))", betterGoToPill&& pillGoodMove() && CheckIfGhost.existsGhostInLair(game)));
		
		}
		
		return facts;
	}

	@Override
	public void parseInput() {
		limitForChasingGhost = 65;
		limitForEdibleGhost = 10000000;
		 limitForEdibleGhost2 = 55;
		limitPill = 4000000;
		mindistanceChasingGhost= Integer.MAX_VALUE;
		mindistancePowerPill = Integer.MAX_VALUE;
		int mspacman = game.getPacmanCurrentNodeIndex();

		if(game.isJunction(mspacman)) {
			int i=0;
		}
		// a veces da NullPointerException usar game.getPacmanLastMoveMade()
				// creo que ocurre a veces cuando pacman acaba de spawnear
				MOVE lastMove = game.wasPacManEaten() || game.getTotalTime()==0
					? MOVE.NEUTRAL
					: game.getPacmanLastMoveMade();
				
		
		
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

		//int mspacman = game.getPacmanCurrentNodeIndex();
		if(game.isJunction(mspacman)) {
			int i=0;
		}
		
		mindistanceChasingGhost = GhostFinder.getMinDistanceChasingGhosts(game);
		NearestEdibleGhostInformation edibleGhostsINFO = GhostFinder.getNearestEdibleGhosts(game, this.limitForEdibleGhost);
		
		List<Integer> listChasingGhosts = GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost);
		
		List<MOVE> goodMoves = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);
	
		List<GHOST> listEdibleGhosts= new ArrayList<>(edibleGhostsINFO.getListNearestEdibleGhost());
		
	
		List<MOVE> movesToEat = new ArrayList<>();
		
		
		if(listEdibleGhosts.size()>0)
		{
			
			// System.out.println("tick: 1");
			for(GHOST ghost: listEdibleGhosts) {
				MOVE m =game.getApproximateNextMoveTowardsTarget(mspacman, game.getGhostCurrentNodeIndex(ghost), lastMove,DM.PATH);
				if(goodMoves.contains(m));
					movesToEat.add(m);
			}
		}
		if(movesToEat.size()>0)
			movesToEatGhosts=true;
		
		if(goodMoves.size()==0) 
			noGoodMoves=true;
		int indexPowerPill1 = PacmanMoves.getNearestPowerPillWithoutChasingGhost(game, goodMoves);
		MOVE m= game.getNextMoveTowardsTarget(mspacman, indexPowerPill1, DM.PATH);
		if(indexPowerPill1!=-1)
			mindistancePowerPill = game.getShortestPathDistance(mspacman, indexPowerPill1, lastMove);
		if(goodMoves.contains(m))
			eatPPill = true;
		
		if(GhostFinder.getNearestChasingGhosts(game, limitForChasingGhost).size()>0)
			nearChasingGhost =true;

		if(edibleGhostsINFO.getListNearestEdibleGhost().size()>0)
			nearEdibleGhost =true;
		
		if(PacmanMoves.getNearestPillWithoutChasingGhost(game, goodMoves )!=-1)
			nearPill =true;
		
		if(PacmanMoves.getNearestPowerPillWithoutChasingGhost(game, goodMoves)!=-1 &&!CheckIfGhost.existsGhostInLair(game))
			nearPPill =true;

		if(edibleGhostsINFO.getMinDistanceNearestGhost()*1.85>= edibleGhostsINFO.getMinEdibleTimeNearestGhost() )
			edibleGhostIsToGoingChange = true;
		
		List<MOVE> movesEscalera = new ArrayList<>();
		int x= game.getNodeXCood(mspacman);
		int y= game.getNodeYCood(mspacman);
		
		if(x<=50 && y >=80)//abajo iz
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 960, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 972, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 776, lastMove, DM.PATH));
		}
		else if(x>50 && y >=80)//abajo dr
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 996, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 980, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 728, lastMove, DM.PATH));
		}
		else if(x<=50 && y <=40)//arriba iz
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 484, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 728, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 492, lastMove, DM.PATH));
		}
		else if(x>50 && y <=40)//arriba dr
		{
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 516, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 716, lastMove, DM.PATH));
			movesEscalera.add(game.getNextMoveTowardsTarget(mspacman, 492, lastMove, DM.PATH));
		}
		if(((x<=50 && y >=80) ||(x>50 && y >=80)||(x<=50 && y <=40) ||(x>50 && y <=40)) &&
				(listChasingGhosts.size()>=2)
				&& (goodMoves.contains(movesEscalera.get(0))
						|| goodMoves.contains(movesEscalera.get(1) )
						|| goodMoves.contains(movesEscalera.get(2))))
			doLatterMoves= true;
	
		nearDangerousCage=CheckIfPacman.isNearDangerousCage(game);
		
		edibleGhostsINFO = GhostFinder.getNearestEdibleGhosts(game, this.limitForEdibleGhost2);
		if(edibleGhostsINFO.getListNearestEdibleGhost().size()>0)
			nearEdibleGhost2 =true;
			

		
		List<MOVE> goodMoves1 = PacmanMoves.getGoodMoves(game, listChasingGhosts,mspacman);
		
		
		int indexPill = PacmanMoves.getNearestPillWithoutChasingGhost(game, goodMoves1);
		int indexPowerPill = PacmanMoves.getNearestPowerPillWithoutChasingGhost(game, goodMoves1);
			
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
		
		if(pillGoodMove && indexPowerPill==-1)
		{
			double d = game.getDistance(mspacman, indexPill, lastMove, DM.PATH);
			if(d>=50 ) {
				betterGoToPill=true;
			}
		}
			
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
