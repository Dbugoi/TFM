package es.ucm.fdi.ici.c2122.practica3.grupo08;


import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;

import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController  {
	
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+
			File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo08"+File.separator;
	
	HashMap<String,RulesAction> map;
	RuleEngine pacmanRuleEngine;
	
	double dist = 55.;
	double pillMinimumDistance = 50.;
	double minDistToGroup = 60;
	
	public MsPacMan() {
		setName("PacWO(RBIS)man");
		setTeam("Team 08");
		
		map = new HashMap<String,RulesAction>();
		
		RulesAction PACMANFleeToEdibleGhost = new ActionFleeToEdibleGhost(dist);
		RulesAction PACMANFleeToPPill = new ActionFleeToClosestPPill();
		RulesAction PACMANFleeToPill = new ActionFleeToClosestPill(dist);
		RulesAction PACMANFleeToFurthestGhost = new ActionFleeToFurthestGhost(dist);
		RulesAction PACMANGoToEdibleGhostGroup = new ActionChaseGhostsGroup(minDistToGroup);
		RulesAction PACMANGoToEdibleGhost = new ActionChaseClosestGhost();
		RulesAction PACMANGoToPillAvoidingPowerPill = new ActionGoToPillAvoidingPPill();
		
		map.put("PACMANFleeToEdibleGhost", PACMANFleeToEdibleGhost);
		map.put("PACMANFleeToPPill", PACMANFleeToPPill);
		map.put("PACMANFleeToPill", PACMANFleeToPill);
		map.put("PACMANFleeToFurthestGhost", PACMANFleeToFurthestGhost);
		map.put("PACMANGoToEdibleGhostGroup", PACMANGoToEdibleGhostGroup);
		map.put("PACMANGoToEdibleGhost", PACMANGoToEdibleGhost);
		map.put("PACMANGoToPillAvoidingPowerPill", PACMANGoToPillAvoidingPowerPill);
		
		String rulesFile = String.format("%s%srules.clp", RULES_PATH, "pacman");
		pacmanRuleEngine  = new RuleEngine("PACMAN",rulesFile, map);
		
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("PACMAN", true);
		//pacmanRuleEngine.addObserver(observer);
	}
	
	
	public void preCompute(String opponent) {
    		
    }
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	//Process input
		RulesInput input = new MsPacManInput(game);
		//load facts
		//reset the rule engines
		pacmanRuleEngine.reset();
		pacmanRuleEngine.assertFacts(input.getFacts());
		
		return pacmanRuleEngine.run(game);
    }
	
}
