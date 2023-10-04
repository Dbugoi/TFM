package es.ucm.fdi.ici.c2122.practica4.grupo05;
import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica1.grupo05.GameViewUtils;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.AvoidPowerPillAction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.ChaseAction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.EatPillGoodMoveAction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.EatPowerPillAction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.PatrolAction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.RunAwayAction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman.actions.RunAwayCageAction;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan extends PacmanController {
	private static final String RULES_PATH =
			"src_main_java_es_ucm_fdi_ici_c2122_practica4_grupo05_mspacman_"
					.replace("_", File.separator);
	FuzzyEngine fuzzyEngine;
	MsPacManFuzzyMemory fuzzyMemory;


	public MsPacMan() {
		setName("WrongDirection");
		setTeam("Run fasta, eat pasta");

		fuzzyMemory = new MsPacManFuzzyMemory();
		Action[] actions = {
				//chase ghost
						new ChaseAction(fuzzyMemory, GHOST.BLINKY),
						new ChaseAction(fuzzyMemory,  GHOST.PINKY),
						new ChaseAction(fuzzyMemory, GHOST.INKY),
						new ChaseAction(fuzzyMemory, GHOST.SUE),

						//Cage
						new RunAwayCageAction(fuzzyMemory),
						//Run Away
						new RunAwayAction(fuzzyMemory, GHOST.BLINKY),
						new RunAwayAction(fuzzyMemory,  GHOST.PINKY),
						new RunAwayAction(fuzzyMemory, GHOST.INKY),
						new RunAwayAction(fuzzyMemory, GHOST.SUE),
						//Eat PP y Pills
						new EatPowerPillAction(fuzzyMemory),
						new EatPillGoodMoveAction(fuzzyMemory),
						new AvoidPowerPillAction(fuzzyMemory),
						//Patrol
						new PatrolAction(),
						
						
						
				};

		ActionSelector actionSelector = new MaxActionSelector(actions);

		ConsoleFuzzyEngineObserver observer =
				new ConsoleFuzzyEngineObserver("MsPacMan", "MsPacManRules");
		fuzzyEngine = new FuzzyEngine("MsPacMan", RULES_PATH + "mspacman.fcl", "FuzzyMsPacMan",
				actionSelector);
		// fuzzyEngine.addObserver(observer);

		
	}


	@Override
	public MOVE getMove(Game game, long timeDue) {
		int i=0;
		if(game.isJunction(game.getPacmanCurrentNodeIndex()))
			i=1;
		MsPacManInput input = new MsPacManInput(game, fuzzyMemory);
		input.parseInput();
		fuzzyMemory.getInput(input);

		HashMap<String, Double> fvars = input.getFuzzyValues();
		fvars.putAll(fuzzyMemory.getFuzzyValues());

		// GHOST g = GHOST.BLINKY;
		// for(FuzzyValue<Target> t : fuzzyMemory.getPositions(g))
		// 	GameView.addPoints(game, GameViewUtils.getGhostColor(g), t.getValue().getNode());
		
		
		// for(Integer i1 : fuzzyMemory.getPillsVisible().values())
		// 	GameView.addPoints(game, Color.CYAN, i1);
		
		// for(Integer i1 : fuzzyMemory.getPowerPillsVisible().values())
		// 	GameView.addPoints(game, Color.RED, i1);
		/*
		for(Integer i2 : fuzzyMemory.getPills().keySet())
			GameView.addPoints(game, Color.CYAN, i2);
*/
		
		return fuzzyEngine.run(fvars, game);
	}

}
