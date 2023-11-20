package es.ucm.fdi.ici.c2122.practica3.grupo09;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.ActionAmbush;
import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.ActionChase;
import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.ActionInterceptPill;
import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.ActionLureToGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.ActionLureToLiberty;
import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.ActionRunAway;
import es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts.GhostsInput;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo09"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	
	public Ghosts()
	{
		setName("Ghosts SMMQMPM");
		setTeam("Team SMMQMPM");

		map = new HashMap<String,RulesAction>();
		//Fill Actions
		RulesAction BLINKYchases = new ActionChase(GHOST.BLINKY);
		RulesAction INKYchases = new ActionChase(GHOST.INKY);
		RulesAction PINKYchases = new ActionChase(GHOST.PINKY);
		RulesAction SUEchases = new ActionChase(GHOST.SUE);
		
		RulesAction BLINKYrunsAway = new ActionRunAway(GHOST.BLINKY);
		RulesAction INKYrunsAway = new ActionRunAway(GHOST.INKY);
		RulesAction PINKYrunsAway = new ActionRunAway(GHOST.PINKY);
		RulesAction SUErunsAway = new ActionRunAway(GHOST.SUE);
		
		//ActionAmbush
		RulesAction BLINKYambush = new ActionAmbush(GHOST.BLINKY);
		RulesAction INKYambush = new ActionAmbush(GHOST.INKY);
		RulesAction PINKYambush = new ActionAmbush(GHOST.PINKY);
		RulesAction SUEambush = new ActionAmbush(GHOST.SUE);

		//intercepPill
		RulesAction BLINKYinterceptPill = new ActionInterceptPill(GHOST.BLINKY);
		RulesAction INKYinterceptPill = new ActionInterceptPill(GHOST.INKY);
		RulesAction PINKYinterceptPill = new ActionInterceptPill(GHOST.PINKY);
		RulesAction SUEinterceptPill = new ActionInterceptPill(GHOST.SUE);
		
		//LureToGhost
		RulesAction BLINKYLureToghost = new ActionLureToGhost(GHOST.BLINKY);
		RulesAction INKYLureToghost = new ActionLureToGhost(GHOST.INKY);
		RulesAction PINKYLureToghost = new ActionLureToGhost(GHOST.PINKY);
		RulesAction SUELureToghost = new ActionLureToGhost(GHOST.SUE);		
		//LureToLiberty
		RulesAction BLINKYLureToLiberty = new ActionLureToLiberty(GHOST.BLINKY);
		RulesAction INKYLureToLiberty = new ActionLureToLiberty(GHOST.INKY);
		RulesAction PINKYLureToLiberty = new ActionLureToLiberty(GHOST.PINKY);
		RulesAction SUELureToLiberty = new ActionLureToLiberty(GHOST.SUE);
		
		map.put("BLINKYchase", BLINKYchases);
		map.put("INKYchase", INKYchases);
		map.put("PINKYchase", PINKYchases);
		map.put("SUEchase", SUEchases);
		
		map.put("BLINKYrunsAway", BLINKYrunsAway);
		map.put("INKYrunsAway", INKYrunsAway);
		map.put("PINKYrunsAway", PINKYrunsAway);
		map.put("SUErunsAway", SUErunsAway);
		//ambush
		map.put("BLINKYambush", BLINKYambush);
		map.put("INKYambush", INKYambush);
		map.put("PINKYambush", PINKYambush);
		map.put("SUEambush", SUEambush);
		//intercepPill
		map.put("BLINKYintercept", BLINKYinterceptPill);
		map.put("INKYintercept", INKYinterceptPill);
		map.put("PINKYintercept", PINKYinterceptPill);
		map.put("SUEintercept", SUEinterceptPill);
		//lureToGhost
		map.put("BLINKYlureToGhost", BLINKYLureToghost);
		map.put("INKYlureToGhost", INKYLureToghost);
		map.put("PINKYlureToGhost", PINKYLureToghost);
		map.put("SUElureToGhost", SUELureToghost);
		//LureToliberty
		map.put("BLINKYlureToLiberty", BLINKYLureToLiberty);
		map.put("INKYlureToLiberty", INKYLureToLiberty);
		map.put("PINKYlureToLiberty", PINKYLureToLiberty);
		map.put("SUElureToLiberty", SUELureToLiberty);
		
		
		ghostRuleEngines = new EnumMap<GHOST,RuleEngine>(GHOST.class);
		for(GHOST ghost: GHOST.values())
		{
			String rulesFile = String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			RuleEngine engine  = new RuleEngine(ghost.name(),rulesFile, map);
			ghostRuleEngines.put(ghost, engine);
			
			//add observer to every Ghost
			//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ghost.name(), true);
			//engine.addObserver(observer);
		}
		
		//add observer only to BLINKY
//		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
//		ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
		
	}
	
//	public void preCompute(String opponent) {
//    	for(FSM fsm: fsms.values())
//    		fsm.reset();
//    }
	
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
