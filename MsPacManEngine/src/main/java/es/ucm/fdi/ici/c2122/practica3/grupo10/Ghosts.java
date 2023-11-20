package es.ucm.fdi.ici.c2122.practica3.grupo10;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.AttackAction;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.ChaseAction;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.CortarPPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.PreAttackInterseccion;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.RunAwayAction;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.RunAwayFromMsPacManAction;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.RunAwayFromMsPacMan_GhostsAction;
import es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts.actions.RunAwayFromMsPacMan_Pills_Action;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	private static final String RULES_PATH = "es" + File.separator + "ucm" + File.separator + "fdi" + File.separator
			+ "ici" + File.separator + "c2122" + File.separator + "practica3" + File.separator + "grupo10" + File.separator;

	HashMap<String, RulesAction> map;

	EnumMap<GHOST, RuleEngine> ghostRuleEngines;

	public Ghosts() {
		setName("Ghosts 10");
		setTeam("Team 10");

		map = new HashMap<String, RulesAction>();
		// Fill Actions
		RulesAction BLINKYchases = new ChaseAction(GHOST.BLINKY);
		RulesAction INKYchases = new ChaseAction(GHOST.INKY);
		RulesAction PINKYchases = new ChaseAction(GHOST.PINKY);
		RulesAction SUEchases = new ChaseAction(GHOST.SUE);
		RulesAction BLINKYrunsAway = new RunAwayAction(GHOST.BLINKY);
		RulesAction INKYrunsAway = new RunAwayAction(GHOST.INKY);
		RulesAction PINKYrunsAway = new RunAwayAction(GHOST.PINKY);
		RulesAction SUErunsAway = new RunAwayAction(GHOST.SUE);

		map.put("BLINKYchases", BLINKYchases);
		map.put("INKYchases", INKYchases);
		map.put("PINKYchases", PINKYchases);
		map.put("SUEchases", SUEchases);
		map.put("BLINKYrunsAway", BLINKYrunsAway);
		map.put("INKYrunsAway", INKYrunsAway);
		map.put("PINKYrunsAway", PINKYrunsAway);
		map.put("SUErunsAway", SUErunsAway);

		ghostRuleEngines = new EnumMap<GHOST, RuleEngine>(GHOST.class);
		for (GHOST ghost : GHOST.values()) {
			RulesAction GHOSTrunsAwayFromGhost = new RunAwayFromMsPacMan_GhostsAction(ghost);
			RulesAction GHOSTrunsAwayFromMsPacMan = new RunAwayFromMsPacManAction(ghost);
			RulesAction GHOSTpreAttackInterseccion_1 = new PreAttackInterseccion(ghost, 2);
			RulesAction GHOSTpreAttackInterseccion_2 = new PreAttackInterseccion(ghost, 3);
			RulesAction GHOSTpreAttackInterseccion_3 = new PreAttackInterseccion(ghost, 5);
			RulesAction GHOSTpreAttackInterseccion_4 = new PreAttackInterseccion(ghost, 8);
			RulesAction GHOSTpreAttackInterseccion_5 = new PreAttackInterseccion(ghost, 10);
			RulesAction GHOSTcortarPPill = new CortarPPillAction(ghost);
	

			map.put("GHOSTrunsAwayFromMsPacManGhosts", GHOSTrunsAwayFromGhost);
			map.put("GHOSTrunsAwayFromMsPacMan", GHOSTrunsAwayFromMsPacMan);
			map.put("GHOSTpreAttackInterseccion_1", GHOSTpreAttackInterseccion_1);
			map.put("GHOSTpreAttackInterseccion_2", GHOSTpreAttackInterseccion_2);
			map.put("GHOSTpreAttackInterseccion_3", GHOSTpreAttackInterseccion_3);
			map.put("GHOSTpreAttackInterseccion_4", GHOSTpreAttackInterseccion_4);
			map.put("GHOSTpreAttackInterseccion_5", GHOSTpreAttackInterseccion_5);
			map.put("GHOSTcortarPPill", GHOSTcortarPPill);
			
			String rulesFile = String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine = new RuleEngine(ghost.name(), rulesFile, map);

			ghostRuleEngines.put(ghost, engine);

			// add observer to every Ghost
			// ConsoleRuleEngineObserver observer = new
			// ConsoleRuleEngineObserver(ghost.name(), true);
			// engine.addObserver(observer);
		}

		// add observer only to BLINKY
//		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
//		ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
		

	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {

		// Process input
		RulesInput input = new GhostsInput(game);
		// load facts
		// reset the rule engines
		for (RuleEngine engine : ghostRuleEngines.values()) {
			engine.reset();
			engine.assertFacts(input.getFacts());
		}

		EnumMap<GHOST, MOVE> result = new EnumMap<GHOST, MOVE>(GHOST.class);
		for (GHOST ghost : GHOST.values()) {
			RuleEngine engine = ghostRuleEngines.get(ghost);
			MOVE move = engine.run(game);
			result.put(ghost, move);
		}

		return result;
	}

}
