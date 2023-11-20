package es.ucm.fdi.ici.c2122.practica3.grupo09;

import java.io.File;

import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo09.pacman.ActionChaseNearestGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo09.pacman.ActionFleeToPathWithMorePills;
import es.ucm.fdi.ici.c2122.practica3.grupo09.pacman.ActionFleeToPowerPillFromAlternativePath;
import es.ucm.fdi.ici.c2122.practica3.grupo09.pacman.ActionGoToNearestPill;
import es.ucm.fdi.ici.c2122.practica3.grupo09.pacman.ActionGoToNearestPowerPill;
import es.ucm.fdi.ici.c2122.practica3.grupo09.pacman.MsPacManInput;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;

import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class NearestPillPacMan.
 */
public class MsPacMan extends PacmanController {

	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo09"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	RuleEngine pacmanEngine;

	public MsPacMan() {
		setName("MsPacMan SMMQMPM");

		map = new HashMap<String,RulesAction>();
		
		RulesAction pacmanChaseNearestGhost = new ActionChaseNearestGhost();
		RulesAction fleeWithNoPowerPill = new ActionFleeToPathWithMorePills();
		RulesAction fleeWithPowerPill = new ActionFleeToPowerPillFromAlternativePath();
		
		RulesAction goToNearestPill = new ActionGoToNearestPill();
		RulesAction goToNearestPowerPill = new ActionGoToNearestPowerPill();
		
		map.put("MSPACMANgoToNearestPowerPill", goToNearestPowerPill);
		map.put("MSPACMANgoToNearestPill", goToNearestPill);
		map.put("MSPACMANchaseNearestGhost", pacmanChaseNearestGhost);
		map.put("MSPACMANfleeToPowerPill", fleeWithPowerPill);
		map.put("MSPACMANfleeToPathWithMorePills", fleeWithNoPowerPill);

		
		String rulesFile = String.format("%s%srules.clp", RULES_PATH, "mspacman");
		pacmanEngine = new RuleEngine("Pacman", rulesFile, map);
		
		//add observer only to BLINKY
//		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
//		ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
		
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
	 */
	@Override
	public MOVE getMove(Game game, long timeDue) {
		//Process input
		RulesInput input = new MsPacManInput(game);
		//load facts
		//reset the rule engines
		pacmanEngine.reset();
		pacmanEngine.assertFacts(input.getFacts());
		
		MOVE move = pacmanEngine.run(game);

		return move;
	}

}