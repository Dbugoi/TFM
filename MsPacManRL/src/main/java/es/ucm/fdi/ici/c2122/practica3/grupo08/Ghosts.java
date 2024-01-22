package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo08.Grafo;
import es.ucm.fdi.ici.c2122.practica3.grupo08.Nodo;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts  extends GhostController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo08"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	private Grafo _grafo;
	private int _graphLevel = -1;
	
	public Ghosts() {
		setName("PacmanEaters");
		setTeam("Team 08");
		
		map = new HashMap<String,RulesAction>();
		
		RulesAction BLINKYCheckMate = new ActionCheckMate(this, GHOST.BLINKY);
		RulesAction PINKYCheckMate = new ActionCheckMate(this, GHOST.PINKY);
		RulesAction INKYCheckMate = new ActionCheckMate(this, GHOST.INKY);
		RulesAction SUECheckMate = new ActionCheckMate(this, GHOST.SUE);
		
		RulesAction BLINKYFleeSpread = new ActionFleeSpread(GHOST.BLINKY);
		RulesAction PINKYFleeSpread = new ActionFleeSpread(GHOST.PINKY);
		RulesAction INKYFleeSpread = new ActionFleeSpread(GHOST.INKY);
		RulesAction SUEFleeSpread = new ActionFleeSpread(GHOST.SUE);
		
		RulesAction BLINKYFleeProtecting = new ActionFleeGoingToNearGhost(GHOST.BLINKY);
		RulesAction PINKYFleeProtecting = new ActionFleeGoingToNearGhost(GHOST.PINKY);
		RulesAction INKYFleeProtecting = new ActionFleeGoingToNearGhost(GHOST.INKY);
		RulesAction SUEFleeProtecting = new ActionFleeGoingToNearGhost(GHOST.SUE);
		
		RulesAction BLINKYProtect = new ActionProtectGhost(GHOST.BLINKY);
		RulesAction PINKYProtect = new ActionProtectGhost(GHOST.PINKY);
		RulesAction INKYProtect = new ActionProtectGhost(GHOST.INKY);
		RulesAction SUEProtect = new ActionProtectGhost(GHOST.SUE);
		
		RulesAction BLINKYChase = new ActionChase(GHOST.BLINKY);
		
		RulesAction PINKYCerrarCamino = new ActionCloseWay(GHOST.PINKY, this);
		
		RulesAction INKYPowerPill = new ActionCoverPowerPill(GHOST.INKY);
		RulesAction INKYBehaviourPINKY = new ActionCloseWay(GHOST.INKY, this);
		
		RulesAction SUEChase = new ActionChase(GHOST.SUE);
		RulesAction SUERandom = new ActionRandom(GHOST.SUE);
				
		map.put("BLINKYJaqueMate", BLINKYCheckMate);
		map.put("PINKYJaqueMate", PINKYCheckMate);
		map.put("INKYJaqueMate", INKYCheckMate);
		map.put("SUEJaqueMate", SUECheckMate);
		
		map.put("BLINKYrunsAwaySpread", BLINKYFleeSpread);
		map.put("PINKYrunsAwaySpread", PINKYFleeSpread);
		map.put("INKYrunsAwaySpread", INKYFleeSpread);
		map.put("SUErunsAwaySpread", SUEFleeSpread);
		
		map.put("BLINKYrunsAwayToGhost", BLINKYFleeProtecting);
		map.put("PINKYrunsAwayToGhost", PINKYFleeProtecting);
		map.put("INKYrunsAwayToGhost", INKYFleeProtecting);
		map.put("SUErunsAwayToGhost", SUEFleeProtecting);
		
		map.put("BLINKYProteger", BLINKYProtect);
		map.put("PINKYProteger", INKYProtect);
		map.put("INKYProteger", PINKYProtect);
		map.put("SUEProteger", SUEProtect);
		
		map.put("BLINKYPerseguir", BLINKYChase);
		
		map.put("PINKYCerrarCamino", PINKYCerrarCamino);
		
		map.put("INKYPowerPill", INKYPowerPill);
		map.put("INKYBehaviourPINKY", INKYBehaviourPINKY);
		
		map.put("SUEPerseguir", SUEChase);
		map.put("SUERandom", SUERandom);
		
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
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(GHOST.PINKY.name(), true);
		//ghostRuleEngines.get(GHOST.PINKY).addObserver(observer);
		
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		//Se crea el grafo correspondiente al nivel actual
		if(this._graphLevel != game.getCurrentLevel()) {
			generaGrafo(game);
			this._graphLevel = game.getCurrentLevel();
		}
		
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
	
	public Grafo getGrafo() {
		return this._grafo;
	}
	
	//------------------------------------------- Funciones Grafo -------------------------------------------------------
	
	//Genera el grafo de cruces del nivel actual
	private void generaGrafo(Game game) {
		int[] junctionIndices = game.getCurrentMaze().junctionIndices;
		this._grafo = new Grafo(junctionIndices.length);
				
		for(int i = 0; i < junctionIndices.length; ++i) {
			Nodo node = this._grafo.getNode(i);
			node.setIndex(junctionIndices[i]);
			completeNode(node, game);
		}
	}
			
	//Rellena la informacion de cruces a los que se llegan desde este nodo
	private void completeNode(Nodo junctionNode, Game game) {
		int[] neighbours = game.getNeighbouringNodes(junctionNode.getIndex());
		
		for(int neighbour : neighbours) {
			MOVE moveToDest = game.getMoveToMakeToReachDirectNeighbour(junctionNode.getIndex(), neighbour);
			int destJunction = findJunction(neighbour, moveToDest, game);
			junctionNode.setPath(moveToDest, destJunction);
		}
	}
			
	//Dado un nodo origen y una direccion, devuelve la siguiente interseccion a la que se llega
	private int findJunction(int origin, MOVE lastMove, Game game) {
		int[] neighbours = game.getNeighbouringNodes(origin, lastMove);
		if(neighbours.length > 1) {
			return origin;			
		}
		else {
			return findJunction(neighbours[0], game.getMoveToMakeToReachDirectNeighbour(origin, neighbours[0]), game);
		}
	}

}
