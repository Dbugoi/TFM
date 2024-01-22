package es.ucm.fdi.ici.c2122.practica3.grupo03;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions.BeNearbyPacman;
import es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions.ChaseAction;
import es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions.CutPathInNMovements;
import es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions.RunAwayFromClosestGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts.actions.RunAwayFromPacman;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts  extends GhostController  {
	
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo03"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	private int firstsearch = 40;
	private int secondsearch = 40;
	private int thirdsearch = 40;
	private int lastsearch = 100;
	
	
	public Ghosts() {
		setName("Ghosts Por favor");
		setTeam("Team socorro");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		RulesAction BLINKYchases = new ChaseAction(GHOST.BLINKY);
		RulesAction INKYchases = new ChaseAction(GHOST.INKY);
		RulesAction PINKYchases = new ChaseAction(GHOST.PINKY);
		RulesAction SUEchases = new ChaseAction(GHOST.SUE);
		RulesAction BLINKYrunsAwayFromGhost = new RunAwayFromClosestGhost(GHOST.BLINKY);
		RulesAction INKYrunsAwayFromGhost = new RunAwayFromClosestGhost(GHOST.INKY);
		RulesAction PINKYrunsAwayFromGhost = new RunAwayFromClosestGhost(GHOST.PINKY);
		RulesAction SUErunsAwayFromGhost = new RunAwayFromClosestGhost(GHOST.SUE);
		RulesAction BLINKYrunsAwayFromPacman = new RunAwayFromPacman(GHOST.BLINKY);
		RulesAction INKYrunsAwayFromPacman = new RunAwayFromPacman(GHOST.INKY);
		RulesAction PINKYrunsAwayFromPacman = new RunAwayFromPacman(GHOST.PINKY);
		RulesAction SUErunsAwayFromPacman = new RunAwayFromPacman(GHOST.SUE);
		RulesAction BLINKYstaysNearbyPacman = new BeNearbyPacman(GHOST.BLINKY);
		RulesAction INKYstaysNearbyPacman = new BeNearbyPacman(GHOST.INKY);
		RulesAction PINKYstaysNearbyPacman = new BeNearbyPacman(GHOST.PINKY);
		RulesAction SUEstaysNearbyPacman = new BeNearbyPacman(GHOST.SUE);
		RulesAction BLINKYgoestoPacManin20 = new CutPathInNMovements(firstsearch, GHOST.BLINKY);
		RulesAction INKYgoestoPacManin20 = new CutPathInNMovements(firstsearch, GHOST.INKY);
		RulesAction PINKYgoestoPacManin20 = new CutPathInNMovements(firstsearch, GHOST.PINKY);
		RulesAction SUEgoestoPacManin20 = new CutPathInNMovements(firstsearch, GHOST.SUE);
		RulesAction BLINKYgoestoPacManin30 = new CutPathInNMovements(secondsearch, GHOST.BLINKY);
		RulesAction INKYgoestoPacManin30 = new CutPathInNMovements(secondsearch, GHOST.INKY);
		RulesAction PINKYgoestoPacManin30 = new CutPathInNMovements(secondsearch, GHOST.PINKY);
		RulesAction SUEgoestoPacManin30 = new CutPathInNMovements(secondsearch, GHOST.SUE);
		RulesAction BLINKYgoestoPacManin40 = new CutPathInNMovements(thirdsearch, GHOST.BLINKY);
		RulesAction INKYgoestoPacManin40 = new CutPathInNMovements(thirdsearch, GHOST.INKY);
		RulesAction PINKYgoestoPacManin40 = new CutPathInNMovements(thirdsearch, GHOST.PINKY);
		RulesAction SUEgoestoPacManin40 = new CutPathInNMovements(thirdsearch, GHOST.SUE);
		RulesAction BLINKYgoestoPacManin50 = new CutPathInNMovements(lastsearch, GHOST.BLINKY);
		RulesAction INKYgoestoPacManin50 = new CutPathInNMovements(lastsearch, GHOST.INKY);
		RulesAction PINKYgoestoPacManin50 = new CutPathInNMovements(lastsearch, GHOST.PINKY);
		RulesAction SUEgoestoPacManin50 = new CutPathInNMovements(lastsearch, GHOST.SUE);
		
		map.put("BLINKYrunsAwayFromPacman", BLINKYrunsAwayFromPacman);
		map.put("BLINKYrunsAwayFromGhost", BLINKYrunsAwayFromGhost);	
		map.put("BLINKYstaysNearbyPacman", BLINKYstaysNearbyPacman);
		map.put("BLINKYchases", BLINKYchases);
		map.put("BLINKYgoestoPacManin20", BLINKYgoestoPacManin20);
		map.put("BLINKYgoestoPacManin30", BLINKYgoestoPacManin30);
		map.put("BLINKYgoestoPacManin40", BLINKYgoestoPacManin40);
		map.put("BLINKYgoestoPacManin50", BLINKYgoestoPacManin50);
		
		// INKYrunsAwayFromPacman.getid();
		
		map.put("INKYrunsAwayFromPacman", INKYrunsAwayFromPacman);
		map.put("INKYrunsAwayFromGhost", INKYrunsAwayFromGhost);
		map.put("INKYstaysNearbyPacman", INKYstaysNearbyPacman);
		map.put("INKYchases", INKYchases);
		map.put("INKYgoestoPacManin20", INKYgoestoPacManin20);
		map.put("INKYgoestoPacManin30", INKYgoestoPacManin30);
		map.put("INKYgoestoPacManin40", INKYgoestoPacManin40);
		map.put("INKYgoestoPacManin50", INKYgoestoPacManin50);
		
		map.put("PINKYrunsAwayFromPacman", PINKYrunsAwayFromPacman);
		map.put("PINKYrunsAwayFromGhost", PINKYrunsAwayFromGhost);
		map.put("PINKYstaysNearbyPacman", PINKYstaysNearbyPacman);
		map.put("PINKYchases", PINKYchases);
		map.put("PINKYgoestoPacManin20", PINKYgoestoPacManin20);
		map.put("PINKYgoestoPacManin30", PINKYgoestoPacManin30);
		map.put("PINKYgoestoPacManin40", PINKYgoestoPacManin40);
		map.put("PINKYgoestoPacManin50", PINKYgoestoPacManin50);
		
		
		map.put("SUErunsAwayFromPacman", SUErunsAwayFromPacman);
		map.put("SUErunsAwayFromGhost", SUErunsAwayFromGhost);
		map.put("SUEstaysNearbyPacman", SUEstaysNearbyPacman);
		map.put("SUEchases", SUEchases);
		map.put("SUEgoestoPacManin20", SUEgoestoPacManin20);
		map.put("SUEgoestoPacManin30", SUEgoestoPacManin30);
		map.put("SUEgoestoPacManin40", SUEgoestoPacManin40);
		map.put("SUEgoestoPacManin50", SUEgoestoPacManin50);
		
		
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
