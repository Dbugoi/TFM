package es.ucm.fdi.ici.c2122.practica3.grupo07;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo07.pacman.actions.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;
public class MsPacMan extends PacmanController{ 
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo07"+File.separator+"pacman"+File.separator+"rules"+File.separator;
	HashMap<String,RulesAction> map;
	RuleEngine engine;
	boolean init=false;
	ArrayList<int[]>neis;
	public MsPacMan(){
		this.setName("Catch me if u can, Ms ghost");
		setTeam("Team 07");
		
		map = new HashMap<String,RulesAction>();
		RulesAction eatPP = new EatPowerPillAction();
		RulesAction eatPill = new EatPillAction();
		RulesAction chaseGhost = new ChaseGhostAction();
		
		
		map.put("eatPP", eatPP);
		map.put("eatP", eatPill);
		map.put("chaseGhost", chaseGhost);
		
		String rulesFile = String.format("%s%s.clp", RULES_PATH, "pacman");
		engine  = new RuleEngine("pacman",rulesFile, map);
		
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("pacman", true);
		//engine.addObserver(observer);
	}
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		
		
		if(game.isJunction(game.getPacmanCurrentNodeIndex())) {
			RulesInput input = new MsPacManInput(game);
			engine.reset();
			engine.assertFacts(input.getFacts());
			MOVE m= engine.run(game);
			return m;
		}
		
		return MOVE.NEUTRAL;
	}

}
