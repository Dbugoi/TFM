package es.ucm.fdi.ici.c2122.practica3.grupo10;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.actions.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan  extends PacmanController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo10"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	RuleEngine mspacmanRuleEngine;
	
	
	public MsPacMan() {
		setName("MsPacMan 10");
		setTeam("Team 10");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions

		RulesAction MSPACMANchases = new ChaseAction();
		RulesAction MSPACMANpowerPill = new PowerPillAction();
		RulesAction MSPACMANpill = new PillAction();
		RulesAction MSPACMANrandomMove = new RandomMoveAction();
		RulesAction MSPACMANnoMoves = new NeutralAction();
		RulesAction MSPACMANcorridor = new NeutralAction();
		

		map.put("MSPACMANchases", MSPACMANchases);
		map.put("MSPACMANpowerPill", MSPACMANpowerPill);
		map.put("MSPACMANpill", MSPACMANpill);
		map.put("MSPACMANrandomMove", MSPACMANrandomMove);
		map.put("MSPACMANnoMoves", MSPACMANnoMoves);
		map.put("MSPACMANcorridor", MSPACMANcorridor);
	
		String rulesFile = String.format("%smspacmanrules.clp", RULES_PATH);
		mspacmanRuleEngine  = new RuleEngine("mspacman" ,rulesFile, map);
		//add observer to mspacman
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("mspacman", true);
		//mspacmanRuleEngine.addObserver(observer);
		
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		//Process input
		RulesInput input = new MsPacManInput(game);
		//load facts
		//reset the rule engines
		mspacmanRuleEngine.reset();
		mspacmanRuleEngine.assertFacts(input.getFacts());
		MOVE move = mspacmanRuleEngine.run(game);
		
		return move;
	}

}
