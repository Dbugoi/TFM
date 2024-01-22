package es.ucm.fdi.ici.c2122.practica3.grupo07;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Random;

import es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController{
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo07"+File.separator+"ghosts"+File.separator+"rules"+File.separator;
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	public Ghosts() {
		this.setName("Artificial Retarded");
		setTeam("Team 07");
		map = new HashMap<String,RulesAction>();
		RulesAction BLINKYinterceptPP = new InterceptPPAction(GHOST.BLINKY);
		RulesAction INKYinterceptPP   = new InterceptPPAction(GHOST.INKY);
		RulesAction PINKYinterceptPP  = new InterceptPPAction(GHOST.PINKY);
		RulesAction SUEinterceptPP    = new InterceptPPAction(GHOST.SUE);
		RulesAction BLINKYeatPacman = new EatPacmanAction(GHOST.BLINKY);
		RulesAction INKYeatPacman   = new EatPacmanAction(GHOST.INKY);
		RulesAction PINKYeatPacman  = new EatPacmanAction(GHOST.PINKY);
		RulesAction SUEeatPacman    = new EatPacmanAction(GHOST.SUE);
		RulesAction BLINKYdontChase  = new DontChaseAction(GHOST.BLINKY);
		RulesAction INKYdontChase    = new DontChaseAction(GHOST.INKY);
		RulesAction PINKYdontChase   = new DontChaseAction(GHOST.PINKY);
		RulesAction SUEdontChase     = new DontChaseAction(GHOST.SUE);
		RulesAction BLINKYgoToYourDefender = new GoToYourDefenderAction(GHOST.BLINKY);
		RulesAction INKYgoToYourDefender   = new GoToYourDefenderAction(GHOST.INKY);
		RulesAction PINKYgoToYourDefender  = new GoToYourDefenderAction(GHOST.PINKY);
		RulesAction SUEgoToYourDefender    = new GoToYourDefenderAction(GHOST.SUE);
		RulesAction BLINKYprotectOthers = new ProtectOthersAction(GHOST.BLINKY);
		RulesAction INKYprotectOthers   = new ProtectOthersAction(GHOST.INKY);
		RulesAction PINKYprotectOthers  = new ProtectOthersAction(GHOST.PINKY);
		RulesAction SUEprotectOthers    = new ProtectOthersAction(GHOST.SUE);
		RulesAction BLINKYdontchaseothers = new DontChaseOthersAction(GHOST.BLINKY);
		RulesAction INKYdontchaseothers   = new DontChaseOthersAction(GHOST.INKY);
		RulesAction PINKYdontchaseothers  = new DontChaseOthersAction(GHOST.PINKY);
		RulesAction SUEdontchaseothers    = new DontChaseOthersAction(GHOST.SUE);
		RulesAction BLINKYenclose = new EncloseAction(GHOST.BLINKY);
		RulesAction INKYenclose   = new EncloseAction(GHOST.INKY);
		RulesAction PINKYenclose  = new EncloseAction(GHOST.PINKY);
		RulesAction SUEenclose    = new EncloseAction(GHOST.SUE);
		RulesAction BLINKYnoPills  = new NoPillsAction(GHOST.BLINKY);
		RulesAction INKYnoPills    = new NoPillsAction(GHOST.INKY);
		RulesAction PINKYnoPills   = new NoPillsAction(GHOST.PINKY);
		RulesAction SUEnoPills     = new NoPillsAction(GHOST.SUE);
		RulesAction BLINKYrun  = new RunAction(GHOST.BLINKY);
		RulesAction INKYrun    = new RunAction(GHOST.INKY);
		RulesAction PINKYrun  = new RunAction(GHOST.PINKY);
		RulesAction SUErun   = new RunAction(GHOST.SUE);
		RulesAction BLINKYdispersarse  = new DispersarseAction(GHOST.BLINKY);
		RulesAction INKYdispersarse   = new DispersarseAction(GHOST.INKY);
		RulesAction PINKYdispersarse  = new DispersarseAction(GHOST.PINKY);
		RulesAction SUEdispersarse   = new DispersarseAction(GHOST.SUE);
		map.put("BLINKYinterceptPP", BLINKYinterceptPP);
		map.put("INKYinterceptPP",   INKYinterceptPP);
		map.put("PINKYinterceptPP",  PINKYinterceptPP);
		map.put("SUEinterceptPP",    SUEinterceptPP);	
		map.put("BLINKYeatPacman", BLINKYeatPacman);
		map.put("INKYeatPacman",   INKYeatPacman);
		map.put("PINKYeatPacman",  PINKYeatPacman);
		map.put("SUEeatPacman",    SUEeatPacman);	
		map.put("BLINKYdontChase",  BLINKYdontChase);
		map.put("INKYdontChase",    INKYdontChase);
		map.put("PINKYdontChase",   PINKYdontChase);
		map.put("SUEdontChase",     SUEdontChase);	
		map.put("BLINKYgoToYourDefender",  BLINKYgoToYourDefender);
		map.put("INKYgoToYourDefender",    INKYgoToYourDefender);
		map.put("PINKYgoToYourDefender",   PINKYgoToYourDefender);
		map.put("SUEgoToYourDefender",     SUEgoToYourDefender);	
		map.put("BLINKYprotectOthers",  BLINKYprotectOthers);
		map.put("INKYprotectOthers",    INKYprotectOthers);
		map.put("PINKYprotectOthers",   PINKYprotectOthers);
		map.put("SUEprotectOthers",     SUEprotectOthers);	
		map.put("BLINKYdontchaseothers",  BLINKYdontchaseothers);
		map.put("INKYdontchaseothers",    INKYdontchaseothers);
		map.put("PINKYdontchaseothers",   PINKYdontchaseothers);
		map.put("SUEdontchaseothers",     SUEdontchaseothers);	
		map.put("BLINKYenclose",  BLINKYenclose);
		map.put("INKYenclose",    INKYenclose);
		map.put("PINKYenclose",   PINKYenclose);
		map.put("SUEenclose",     SUEenclose);	
		map.put("BLINKYnoPills", BLINKYnoPills);
		map.put("INKYnoPills",   INKYnoPills);
		map.put("PINKYnoPills",  PINKYnoPills);
		map.put("SUEnoPills",    SUEnoPills);	
		map.put("BLINKYrun", BLINKYrun);
		map.put("INKYrun",   INKYrun);
		map.put("PINKYrun",  PINKYrun);
		map.put("SUErun",    SUErun);	
		map.put("BLINKYdispersarse", BLINKYdispersarse);
		map.put("INKYdispersarse",   INKYdispersarse);
		map.put("PINKYdispersarse",  PINKYdispersarse);
		map.put("SUEdispersarse",    SUEdispersarse);	
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
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.BLINKY.name(), true);
		//ghostRuleEngines.get(GHOST.BLINKY).addObserver(observer);
	}
	Random rnd=new Random();
	EnumMap<GHOST,MOVE> moves = new EnumMap<GHOST,MOVE>(GHOST.class);		
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
