package es.ucm.fdi.ici.c2122.practica3.grupo04;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.fantasmas.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//es.ucm.fdi.ici.c2122.practica3.grupo04.Ghosts,\

public class Ghosts  extends GhostController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo04"+File.separator;
	
	HashMap<String,RulesAction> map;
	
	EnumMap<GHOST,RuleEngine> ghostRuleEngines;
	
	
	public Ghosts() {
		setName("Ghosts 04 clipped");
		setTeam("Team 04");
		
		
		ghostRuleEngines = new EnumMap<GHOST,RuleEngine>(GHOST.class);
		for(GHOST ghost: GHOST.values())
		{
			map = new HashMap<String,RulesAction>();

			
			int maxDist = 30;
			int fearFleeDistance = 70;
			
			//Fill Actions
			RulesAction chaseAction = new ChasePacmanAction(ghost);			
			RulesAction copyAction = new CopyPacmanAction(ghost);			
			RulesAction goToClosestPPillAction = new GoToClosestPPillAction(ghost);			
			RulesAction goToEdibleGhostAction = new GoToEdibleGhostAction(ghost, maxDist);			
			RulesAction goToPacmanInterAction = new GoToPacmanInterAction(ghost);			
			RulesAction goToSafestInterAction = new GoToSafestInterAction(ghost);			
			RulesAction runAwayFromPacmanAction = new RunAwayFromPacmanAction(ghost);			
			RulesAction runAwayToAliveGhostAction = new RunAwayToAliveGhostAction(ghost, fearFleeDistance);			
			RulesAction separatePacmanAction = new SeparatePacmanAction(ghost);
			//
			map.put(String.format("%schases", ghost.name()), chaseAction);
			map.put(String.format("%scopies", ghost.name()), copyAction);
			map.put(String.format("%sgoesToClosestPPill", ghost.name()), goToClosestPPillAction);
			map.put(String.format("%sgoesToEdibleGhost", ghost.name()), goToEdibleGhostAction);
			map.put(String.format("%sgoToPacmanInter", ghost.name()), goToPacmanInterAction);
			map.put(String.format("%sgoesToSafestInter", ghost.name()), goToSafestInterAction);
			map.put(String.format("%srunAwayFromPacman", ghost.name()), runAwayFromPacmanAction);
			map.put(String.format("%srunAwayToAliveGhost", ghost.name()), runAwayToAliveGhostAction);
			map.put(String.format("%sseparatesPacman", ghost.name()), separatePacmanAction);
			
			String rulesFile = String.format("%s%srules.clp", RULES_PATH, ghost.name().toLowerCase());
			//String rulesFile = String.format("%sGhostrules.clp", RULES_PATH);
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
