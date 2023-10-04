package es.ucm.fdi.ici.c2122.practica3.grupo05;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.AmbushPacman;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.BlockPowerPill;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.DefendEdibleGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.FollowProtector;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.GoToBestCorner;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.InLair;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.KillPacman;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions.MoveToKillPacman;
import es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.coordinator.GhostsCoordinator;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	private static final String RULES_PATH = "es" + File.separator + "ucm" + File.separator + "fdi"
			+ File.separator + "ici" + File.separator + "c2122" + File.separator + "practica3"
			+ File.separator + "grupo05" + File.separator + "ghost" + File.separator;

	private final GhostsCoordinator coordinator;
	private final HashMap<String, RulesAction> map;
	private final EnumMap<GHOST, RuleEngine> ghostRuleEngines;

	public Ghosts() {
		setName("Ya sabes que rima con 5");
		setTeam("Run fasta, eat pasta");

		coordinator = new GhostsCoordinator();
		map = new HashMap<>();
		// Fill Rules Actions and put in the map
		for (GHOST ghost : GHOST.values()) {
			map.put(ghost + "moveToKill", new MoveToKillPacman(ghost));
			map.put(ghost + "killPacMan", new KillPacman(ghost));
			map.put(ghost + "inLair", new InLair());
			map.put(ghost + "goToBestCorner", new GoToBestCorner(ghost, coordinator));
			map.put(ghost + "followProtector", new FollowProtector(ghost));
			map.put(ghost + "ambushPacman", new AmbushPacman(ghost, coordinator));
			map.put(ghost + "blockPowerPill", new BlockPowerPill(ghost));
			map.put(ghost + "defendEdibleGhost", new DefendEdibleGhost(ghost));
		}

		ghostRuleEngines = new EnumMap<>(GHOST.class);
		for (GHOST ghost : GHOST.values()) {
			String rulesFile =
					String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine = new RuleEngine(ghost.name(), rulesFile, map);
			ghostRuleEngines.put(ghost, engine);

			// add observer to every Ghost
			if (false) {
				ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(),false);
				engine.addObserver(observer);
			}
		}

		// add observer only to BLINKY
		if (false) {
			ConsoleRuleEngineObserver observer =
					new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), false);
			ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
		}
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

		EnumMap<GHOST, MOVE> result = new EnumMap<>(GHOST.class);
		for (GHOST ghost : GHOST.values()) {
			RuleEngine engine = ghostRuleEngines.get(ghost);
			MOVE move = engine.run(game);
			result.put(ghost, move);
		}

		return result;
	}

}
