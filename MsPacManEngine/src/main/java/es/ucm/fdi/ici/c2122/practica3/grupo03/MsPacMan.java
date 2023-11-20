package es.ucm.fdi.ici.c2122.practica3.grupo03;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.MsPacmanInput;
import es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.actions.ChaseClosestGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.actions.EatClosestPills;
import es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.actions.FleeFromClosestGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.actions.GoToPowerPill;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class MsPacMan extends PacmanController{

	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo03"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	RuleEngine msPacmanEngine;
	
	public MsPacMan() {
		setName("Pacman Piedad");
		setTeam("Team Socorro");
		
		map = new HashMap<String,RulesAction>();
		
		//Fill Actions
		RulesAction GoToPowerPill = new GoToPowerPill();
		RulesAction FleeFromClosestGhost = new FleeFromClosestGhost();
		RulesAction ChaseClosestGhost = new ChaseClosestGhost();
		//RulesAction ChaseSecond = new ChaseAction();
		RulesAction EatClosestPills = new EatClosestPills();
		
		map.put("GoToPowerPill", GoToPowerPill);
		map.put("FleeFromClosestGhost", FleeFromClosestGhost);	
		map.put("ChaseClosestGhost", ChaseClosestGhost);
		//map.put("ChaseSecond", ChaseSecond);
		map.put("EatClosestPills", EatClosestPills);
		
		String rulesFile = String.format("%s%srules.clp", RULES_PATH, "mspacman");
		msPacmanEngine = new RuleEngine("mspacman", rulesFile, map);
		
		//add observer to mspacman
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("mspacman", true);
		//msPacmanEngine.addObserver(observer);
		
	}
	@Override
	public MOVE getMove(Game game, long timeDue) {
		//Process input
		RulesInput in = new MsPacmanInput(game);
		//load facts
		//reset the rule engines
		msPacmanEngine.reset();
		msPacmanEngine.assertFacts(in.getFacts());
		
		return msPacmanEngine.run(game);
		//return MOVE.NEUTRAL;
	}

}
