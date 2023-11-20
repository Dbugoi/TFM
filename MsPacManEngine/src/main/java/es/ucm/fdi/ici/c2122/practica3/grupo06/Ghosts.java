package es.ucm.fdi.ici.c2122.practica3.grupo06;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts  extends GhostController  {
	private static final String RULES_PATH = "es"+File.separator+
			"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+
			"practica3"+File.separator+"grupo06"+File.separator+"ghosts"+File.separator+"rules"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	
	public Ghosts() {
		setName("Omega Weapon 06");
		setTeam("Team 06");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		RulesAction BLINKYchases = new ChaseAction(GHOST.BLINKY);
		RulesAction INKYcutoff = new CutOffPathAction(GHOST.INKY);
		RulesAction PINKYintercepts = new InterceptAction(GHOST.PINKY);
		RulesAction SUEtoDestinyQuad = new GoToPillZoneAction(GHOST.SUE);
		RulesAction SUEguardsQuad = new GuardZoneAction(GHOST.SUE);
		
		RulesAction BLINKYrunsAway = new FleeAction(GHOST.BLINKY);
		RulesAction INKYrunsAway = new FleeAction(GHOST.INKY);
		RulesAction PINKYrunsAway = new FleeAction(GHOST.PINKY);
		RulesAction SUErunsAway = new FleeAction(GHOST.SUE);
		
		RulesAction BLINKYdisperses = new DisperseAction(GHOST.BLINKY);
		RulesAction INKYdisperses = new DisperseAction(GHOST.INKY);
		RulesAction PINKYdisperses = new DisperseAction(GHOST.PINKY);
		RulesAction SUEdisperses = new DisperseAction(GHOST.SUE);
		
		RulesAction YESkill = new ChaseAction(GHOST.BLINKY);
		
		map.put("BLINKYchases", BLINKYchases);
		map.put("INKYcutoff", INKYcutoff);
		map.put("PINKYintercepts", PINKYintercepts);
		map.put("SUEtoDestinyQuad", SUEtoDestinyQuad);
		map.put("SUEguardsQuad", SUEguardsQuad);

		map.put("BLINKYrunsAway", BLINKYrunsAway);
		map.put("INKYrunsAway", INKYrunsAway);
		map.put("PINKYrunsAway", PINKYrunsAway);
		map.put("SUErunsAway", SUErunsAway);
		
		map.put("BLINKYdisperses", BLINKYdisperses);
		map.put("INKYdisperses", INKYdisperses);
		map.put("PINKYdisperses", PINKYdisperses);
		map.put("SUEdisperses", SUEdisperses);
		
		map.put("BLINKYkills", YESkill);
		map.put("INKYkills", YESkill);
		map.put("PINKYkills", YESkill);
		map.put("SUEkills", YESkill);
		
		ghostRuleEngines = new EnumMap<GHOST,RuleEngine>(GHOST.class);
		for(GHOST ghost: GHOST.values())
		{
			String rulesFile = String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine  = new RuleEngine(ghost.name(), rulesFile, map);
			ghostRuleEngines.put(ghost, engine);
			
			//add observer to every Ghost
			//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(), true);
			//engine.addObserver(observer);
		}
		
		//add observer only to BLINKY
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.INKY.name(), true);
		//ghostRuleEngines.get(GHOST.INKY).addObserver(observer);
		
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		//Process input
		RulesInput input = new GhostsInput(game);
		//load facts
		//reset the rule engines
		for(RuleEngine engine: ghostRuleEngines.values()) {
			engine.reset();
			engine.assertFacts(input.getFacts());
		}
		
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);		
		for(GHOST ghost: GHOST.values())
		{
			RuleEngine engine = ghostRuleEngines.get(ghost);
			MOVE move = engine.run(game);
			result.put(ghost, move);
		}
		
		return result;
	}

}
