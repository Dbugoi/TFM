package es.ucm.fdi.ici.c2122.practica3.grupo02;

import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo02.rules.ghosts.GhostInput;
import es.ucm.fdi.ici.c2122.practica3.grupo02.rules.ghosts.actions.*;

import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;

import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.controllers.GhostController;

public class Ghosts extends GhostController {

	EnumMap<GHOST, RuleEngine> ghostsRuleEngines;

	public Ghosts() {

		setName("Definitely Not Ghosts");
		setTeam("G2_ICIsports");

		ghostsRuleEngines = new EnumMap<GHOST, RuleEngine>(GHOST.class);

		for (GHOST ghost : GHOST.values()) {
			// -- ACTIONS --
			HashMap<String, RulesAction> actionsMap = new HashMap<String, RulesAction>();

			RulesAction chase = new Chase(ghost);
			actionsMap.put(chase.getActionId(), chase);
			
			RulesAction runaway = new RunAway(ghost);
			actionsMap.put(runaway.getActionId(), runaway);

			switch (ghost) {
			case BLINKY:
				RulesAction seer = new Seer(ghost);
				actionsMap.put(seer.getActionId(), seer);
				break;
			case PINKY:
				RulesAction mole = new Mole(ghost);
				actionsMap.put(mole.getActionId(), mole);
				break;
			case INKY:
				RulesAction ambush = new Ambush(ghost);
				actionsMap.put(ambush.getActionId(), ambush);
				break;
			case SUE:
				RulesAction agressive = new Agressive(ghost);
				actionsMap.put(agressive.getActionId(), agressive);
				break;
				
			default:
				break;
			}
			
			// -- RULES --
			String rulesFile = String.format("%s%srules.clp", GameConstants.RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine = new RuleEngine(ghost.name(), rulesFile, actionsMap);
			ghostsRuleEngines.put(ghost, engine);

			if (false/*GameConstants.DEBUG*/) {
				// -- RULES OBSERVERS --
				ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(), true);
				ghostsRuleEngines.get(ghost).addObserver(observer);
			}
		}

//		RulesAction BLINKYchases = new ChaseAction(GHOST.BLINKY);
//		RulesAction INKYchases = new ChaseAction(GHOST.INKY);
//		RulesAction PINKYchases = new ChaseAction(GHOST.PINKY);
//		RulesAction SUEchases = new ChaseAction(GHOST.SUE);
//		RulesAction BLINKYrunsAway = new RunAwayAction(GHOST.BLINKY);
//		RulesAction INKYrunsAway = new RunAwayAction(GHOST.INKY);
//		RulesAction PINKYrunsAway = new RunAwayAction(GHOST.PINKY);
//		RulesAction SUErunsAway = new RunAwayAction(GHOST.SUE);

//		map.put("BLINKYchases", BLINKYchases);
//		map.put("INKYchases", INKYchases);
//		map.put("PINKYchases", PINKYchases);
//		map.put("SUEchases", SUEchases);	
//		map.put("BLINKYrunsAway", BLINKYrunsAway);
//		map.put("INKYrunsAway", INKYrunsAway);
//		map.put("PINKYrunsAway", PINKYrunsAway);
//		map.put("SUErunsAway", SUErunsAway);

//		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
//		for(GHOST ghost: GHOST.values()) {
//			FSM fsm = new FSM(ghost.name());
//			fsm.addObserver(new ConsoleFSMObserver(ghost.name()));
//			GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
//			fsm.addObserver(graphObserver);
//
//			
//			FSM fsmChase = new FSM("Chase compound");
//			GraphFSMObserver c1observer = new GraphFSMObserver(fsmChase.toString());
//	    	fsmChase.addObserver(c1observer);
//	    	
//	    	FSM fsmRun = new FSM("Run compound");
//			GraphFSMObserver anotherObserver = new GraphFSMObserver(fsmRun.toString());
//	    	fsmRun.addObserver(anotherObserver);
//			
//			SimpleState chasePrimaryPath = new SimpleState("Chase Primary", new ChasePrimaryPath(ghost));
//			SimpleState chaseSecondaryPath = new SimpleState("Chase Secondary", new ChaseSecondaryPath(ghost));
//			SimpleState runAwayFromPacMan = new SimpleState("Run Away From PacMan", new RunAwayFromPacMan(ghost));
//			SimpleState runTowardsGhost = new SimpleState("Run Towards Ghost", new RunAwayToGhost(ghost));
//			SimpleState runAlternative = new SimpleState("Run Alternate Path", new RunAwayAlternative(ghost));
//			SimpleState lair = new SimpleState("Lair", new Lair());
//			
//			Transition mainRunAwayTransition = new GoToMainRunAway();
//			Transition mainChaseTransition = new GoToMainChase();
//			Transition isMsPacManNearTransition = new IsMsPacManNear(ghost);
//			Transition lairTimeOverTransition = new LairTimeOver(ghost);
//			Transition anotherGhostOnRunAway = new AnotherGhostOnRunAwayPath(ghost);
//			Transition ghostNotEdibleTransition = new AnotherGhostIsntEdible(ghost);
//			Transition startRunAwayGhostTransition = new StartRunAwayGhost(ghost);
//			Transition startRunAwayGhostFromSpecificBehaviourTransition = new RunAwayFromSpecificBehaviourTransition(ghost);
//			Transition stopRunAwayGhostTransition = new StopRunAwayGhost(ghost);
//			Transition stopRunAwayAndChaseTransition = new StopRunAwayAndStartChase(ghost);
//			Transition chaseSecondaryTransition = new ChaseSecondaryPathTransition(ghost);
//			Transition hasBeenEaten = new GhostHasBeenEaten(ghost);
//			Transition hasBeenEaten2 = new GhostHasBeenEaten2(ghost);
//			Transition hasBeenEaten3 = new GhostHasBeenEaten3(ghost);
//			
//			fsmChase.add(chasePrimaryPath, chaseSecondaryTransition, chaseSecondaryPath);
//			fsmChase.add(chaseSecondaryPath, mainChaseTransition, chasePrimaryPath);
//			
//			fsmChase.ready(chasePrimaryPath);
//			
//			CompoundState chase = new CompoundState(ghost + " starts chasing", fsmChase);
//			
//			fsmRun.add(runAwayFromPacMan, ghostNotEdibleTransition, runTowardsGhost);
//			fsmRun.add(runAwayFromPacMan, anotherGhostOnRunAway, runAlternative);
//			fsmRun.add(runAlternative, mainRunAwayTransition, runAwayFromPacMan);
//			
//			fsmRun.ready(runAwayFromPacMan);
//			
//			CompoundState run = new CompoundState(ghost + " starts running", fsmRun);
//			
//			fsm.add(chase, hasBeenEaten, lair);
//			fsm.add(run, hasBeenEaten2, lair);
//			
//			switch(ghost) {
//			case BLINKY:
//				SimpleState seer = new SimpleState("Seer", new Seer(ghost));
//				Transition seerChaseSpecificTransition = new SeerSpecialChaseStart(ghost);
//				fsm.add(lair, lairTimeOverTransition, seer);
//				fsm.add(seer, isMsPacManNearTransition, chase);
//				fsm.add(seer, seerChaseSpecificTransition, chase);
//				fsm.add(seer, startRunAwayGhostFromSpecificBehaviourTransition, run);
//				fsm.add(chase, startRunAwayGhostTransition, run);
//				fsm.add(run, stopRunAwayGhostTransition, seer);
//				fsm.add(run, stopRunAwayAndChaseTransition, chase);
//				fsm.add(seer, hasBeenEaten3, lair);
//				break;
//			case PINKY:
//				SimpleState mole = new SimpleState("Mole", new Mole(ghost));
//				fsm.add(lair, lairTimeOverTransition, mole);
//				fsm.add(mole, isMsPacManNearTransition, chase);
//				fsm.add(mole, startRunAwayGhostFromSpecificBehaviourTransition, run);
//				fsm.add(chase, startRunAwayGhostTransition, run);
//				fsm.add(run, stopRunAwayGhostTransition, mole);
//				fsm.add(run, stopRunAwayAndChaseTransition, chase);
//				fsm.add(mole, hasBeenEaten3, lair);
//				break;
//			case INKY:
//				SimpleState ambush = new SimpleState(new Ambush(ghost));
//				fsm.add(lair, lairTimeOverTransition, ambush);
//				fsm.add(ambush, isMsPacManNearTransition, chase);
//				fsm.add(ambush, startRunAwayGhostFromSpecificBehaviourTransition, run);
//				fsm.add(chase, startRunAwayGhostTransition, run);
//				fsm.add(run, stopRunAwayGhostTransition, ambush);
//				fsm.add(run, stopRunAwayAndChaseTransition, chase);
//				fsm.add(ambush, hasBeenEaten3, lair);
//				break;
//			case SUE:
//				SimpleState agressive = new SimpleState("Agressive", new Agressive(ghost));
//				fsm.add(lair, lairTimeOverTransition, agressive);
//				fsm.add(agressive, isMsPacManNearTransition, chase);
//				fsm.add(agressive, startRunAwayGhostFromSpecificBehaviourTransition, run);
//				fsm.add(chase, startRunAwayGhostTransition, run);
//				fsm.add(run, stopRunAwayGhostTransition, agressive);
//				fsm.add(run, stopRunAwayAndChaseTransition, chase);
//				fsm.add(agressive, hasBeenEaten3, lair);
//				break;
//			}
//			
//			fsm.ready(lair);
//			
//			fsms.put(ghost, fsm);
//			
//			if (GameConstants.DEBUG) {
//				JFrame frame = new JFrame();
//		    	JPanel main = new JPanel();
//		    	main.setLayout(new BorderLayout());
//		    	main.add(graphObserver.getAsPanel(true, null), BorderLayout.NORTH);
//		    	main.add(c1observer.getAsPanel(true, null), BorderLayout.CENTER);
//		    	main.add(anotherObserver.getAsPanel(true, null), BorderLayout.SOUTH);
//		    	frame.getContentPane().add(main);
//		    	frame.pack();
//		    	frame.setVisible(true);
//			}
//		}
	}

	public void preCompute(String opponent) {
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> result = new EnumMap<GHOST, MOVE>(GHOST.class);

		for (GHOST ghost : GHOST.values()) {
			RulesInput inGhost = new GhostInput(game);

			ghostsRuleEngines.get(ghost).reset();
			ghostsRuleEngines.get(ghost).assertFacts(inGhost.getFacts());

			result.put(ghost, ghostsRuleEngines.get(ghost).run(game));
		}

		return result;
	}
}
