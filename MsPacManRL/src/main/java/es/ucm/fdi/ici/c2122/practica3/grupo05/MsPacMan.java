package es.ucm.fdi.ici.c2122.practica3.grupo05;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.BestMoveToEatChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.ChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.EatPillGoodMoveAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.EatPowerPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.EscapeNearDeathEatAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.LatterMovesPatrolAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.MsPacManReappearsAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.NoGoodMovesChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.NoGoodMovesPatrolAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.NoPillGoodMovesAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.NoPillNoGoodMovesEatAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.PatrolAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.RandomGoodMoveEatAction;
import es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions.RunAwayCageAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
	
	HashMap<String,RulesAction> map;
	RuleEngine engine ;
	public  MsPacMan(){
		final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo05"+File.separator;

		setName("WrongDirection");
		setTeam("Run fasta, eat pasta");
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		
		
		//chase ghost
		RulesAction BestMoveToEatChaseGhost= new BestMoveToEatChaseGhostAction();
		RulesAction NoGoodMovesChaseGhostAction =new NoGoodMovesChaseGhostAction();
		RulesAction EatPowerPillActionChase = new EatPowerPillAction();
		RulesAction ChaseGhostAction = new ChaseGhostAction();
		
		//Cage
		RulesAction RunAwayCageAction= new RunAwayCageAction();
		//Run Away
		RulesAction noPillNoGoodMovesEatState= new NoPillNoGoodMovesEatAction();
		RulesAction eatPowerPillStateRun= new EatPowerPillAction();
		RulesAction EscapeNearDeathEatAction= new EscapeNearDeathEatAction();
		RulesAction NoPillGoodMovesAction = new NoPillGoodMovesAction();
		RulesAction EatPillGoodMoveAction= new EatPillGoodMoveAction();
		RulesAction RandomGoodMoveEatAction= new RandomGoodMoveEatAction();
		
		//Patrol
		RulesAction EatPillGoodMoveActionPatrol= new EatPillGoodMoveAction();
		RulesAction LatterMovesPatrolAction = new LatterMovesPatrolAction();
		RulesAction PatrolAction = new PatrolAction();
		RulesAction NoGoodMovesPatrolAction = new NoGoodMovesPatrolAction();
		RulesAction EscapeNearDeathEatActionPatrol = new EscapeNearDeathEatAction();
		RulesAction NoPillNoGoodMovesEatAction = new NoPillNoGoodMovesEatAction();
		RulesAction NoPillGoodMovesActionPatrol = new NoPillGoodMovesAction();
		RulesAction RandomGoodMoveEatActionPatrol = new RandomGoodMoveEatAction();
		
		//MsPacManReappears
		RulesAction MsPacManReappearsAction = new MsPacManReappearsAction();
		//Put in the map
		
		//chase
		map.put("BestMoveToEatChaseGhost", BestMoveToEatChaseGhost);
		map.put("NoGoodMovesChaseGhostAction",NoGoodMovesChaseGhostAction);
		map.put("EatPowerPillActionChase",EatPowerPillActionChase);
		map.put("ChaseGhostAction",ChaseGhostAction);
		
		//cage
		map.put("RunAwayCageAction",RunAwayCageAction);
		
		//RunAway
		map.put("noPillNoGoodMovesEatState", noPillNoGoodMovesEatState);
		map.put("eatPowerPillStateRun",eatPowerPillStateRun);
		map.put("EscapeNearDeathEatAction",EscapeNearDeathEatAction);
		map.put("NoPillGoodMovesAction",NoPillGoodMovesAction);
		map.put("EatPillGoodMoveAction",EatPillGoodMoveAction);
		map.put("RandomGoodMoveEatAction",RandomGoodMoveEatAction);
		
		//patrol
		map.put("EatPillGoodMoveActionPatrol", EatPillGoodMoveActionPatrol);
		map.put("LatterMovesPatrolAction",LatterMovesPatrolAction);
		map.put("PatrolAction",PatrolAction);
		map.put("NoGoodMovesPatrolAction",NoGoodMovesPatrolAction);
		map.put("EscapeNearDeathEatActionPatrol",EscapeNearDeathEatActionPatrol);
		map.put("NoPillNoGoodMovesEatAction",NoPillNoGoodMovesEatAction);
		map.put("NoPillGoodMovesActionPatrol", NoPillGoodMovesActionPatrol);
		map.put("RandomGoodMoveEatActionPatrol",RandomGoodMoveEatActionPatrol);

		//MsPacManReappears
		map.put("MsPacManReappearsAction",MsPacManReappearsAction);

		String rulesFile = String.format("%s%srules.clp", RULES_PATH, "mspacman05");
		engine= new RuleEngine(getName(),rulesFile, map);

		
	}
	@Override
	public MOVE getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		//Process input
		RulesInput input = new MsPacManInput(game);
		//load facts
		//reset the rule engines
		engine.reset();
		engine.assertFacts(input.getFacts());
		
		MOVE move = engine.run(game);
		
		return move;
	}

}
