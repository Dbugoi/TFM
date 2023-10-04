package es.ucm.fdi.ici.c2122.practica4.grupo05;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.PillStatus;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.GhostsFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.GhostsInput;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions.BestCorner;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions.BlockPPill;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions.Chase;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions.KillPacman;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions.Patrol;
import es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions.SearchProtector;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.GameViewUtils;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class Ghosts extends GhostController {
	private static final String RULES_PATH =
			"src_main_java_es_ucm_fdi_ici_c2122_practica4_grupo05_ghost_"
					.replace("_", File.separator);

	private final GhostsFuzzyMemory fuzzyMemory;
	private final Map<GHOST, FuzzyEngine> fuzzyEngines;

	public Ghosts() {
		setName("5mentarios");
		setTeam("Run fasta, eat pasta");

		fuzzyMemory = new GhostsFuzzyMemory();
		fuzzyEngines = new EnumMap<>(GHOST.class);

		for (GHOST ghost : GHOST.values()) {
			Action[] actions = {
					new BestCorner(ghost),
					new BlockPPill(fuzzyMemory, ghost),
					new Chase(fuzzyMemory, ghost),
					new SearchProtector(fuzzyMemory, ghost),
					new Patrol(ghost),
					new KillPacman(fuzzyMemory, ghost)	
			};

			FuzzyEngine fuzzyEngine = new FuzzyEngine(
					"Ghosts",
					RULES_PATH + "Ghosts.fcl",
					"FuzzyGhost",
					new MaxActionSelector(actions));

			fuzzyEngines.put(ghost, fuzzyEngine);

			if (ghost == GHOST.BLINKY) {
				// fuzzyEngine.addObserver(new ConsoleFuzzyEngineObserver("Ghosts", null));
				// fuzzyEngine.addObserver(new ConsoleFuzzyEngineObserver("Ghosts", "GhostsRules"));
			}
		}
	}

	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> moves = new EnumMap<>(GHOST.class);

		GhostsInput input = new GhostsInput(game, fuzzyMemory);
		input.parseInput();
		fuzzyMemory.getInput(input);

		for (GHOST g : GHOST.values()) {
			HashMap<String, Double> fvars = input.getFuzzyValues(g);
			moves.put(g, fuzzyEngines.get(g).run(fvars, game));
		}

		debugGhost(game, GHOST.BLINKY);
		debugPowerPills(game);
		debugPacmanPositions(game);

		return moves;
	}

	private void debugGhost(Game game, GHOST ghost) {
		if (false) {
			GameView.addPoints(game,
					GameViewUtils.getGhostColor(ghost),
					game.getGhostCurrentNodeIndex(ghost));
		}
	}

	private void debugPowerPills(Game game) {
		if (false)
			for (FuzzyValue<PillStatus> ppill : fuzzyMemory.getPowerPillsStatus()) {
				Color c;
				if (!ppill.getValue().isAvailable())
					c = Color.red;
				else if (ppill.getConfidence() >= 0.5)
					c = Color.green;
				else if (ppill.getConfidence() >= 0.3)
					c = Color.yellow;
				else
					c = Color.orange;
				GameView.addPoints(game, c, ppill.getValue().getNode());
			}
	}

	private void debugPacmanPositions(Game game) {
		if (false)
			for (FuzzyValue<Target> target : fuzzyMemory.getPacmanPossiblePositions()) {
				Color c;
				if (target.getConfidence() >= 50)
					c = Color.green;
				else if (target.getConfidence() >= 30)
					c = Color.yellow;
				else if (target.getConfidence() >= 10)
					c = Color.orange;
				else if (target.getConfidence() >= 3)
					c = Color.red;
				else
					c = Color.gray;
				GameView.addPoints(game, c, target.getValue().getNode());
			}
	}

}
