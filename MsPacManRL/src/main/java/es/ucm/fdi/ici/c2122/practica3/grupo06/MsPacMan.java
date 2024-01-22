package es.ucm.fdi.ici.c2122.practica3.grupo06;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.MsPacManFlags;
import es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class NearestPillPacMan.
 */
public class MsPacMan extends PacmanController {
	private static final String RULES_PATH = "es"+File.separator+
			"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+
			"practica3"+File.separator+"grupo06"+File.separator+"mspacman"+File.separator+"rules"+File.separator;
	
	MsPacManFlags mpf;
	HashMap<String,RulesAction> map;
	RuleEngine mspacmanRuleEngine;
	
	public MsPacMan() {
		setName("Lord of the Null 06");
		setTeam("Team 06");
		
		mpf = new MsPacManFlags();
		map = new HashMap<String,RulesAction>();
		
		RulesAction eatGhost              = new EatGhostAction();
		RulesAction eatPillsRegion        = new EatPillsInRegionAction();
		RulesAction eatPillsOtherRegion   = new EatPillsOtherRegionAction();
		RulesAction eatPPill              = new EatPPillAction();
		RulesAction runAwayToEdibleGhost  = new RunAwayToEdibleGhostAction();
		RulesAction runAwayToPoPill       = new RunAwayToPPillAction();
		RulesAction runAwayToPill         = new RunAway1Ghost();
		
		map.put("runAwayToEdibleGhost", runAwayToEdibleGhost);
		map.put("runAwayToPoPill", runAwayToPoPill);
		map.put("runAwayToPill", runAwayToPill);
		map.put("eatGhost", eatGhost);
		map.put("eatPillsRegion", eatPillsRegion);
		map.put("eatPillsOtherRegion", eatPillsOtherRegion);
		map.put("eatPPill", eatPPill);

		mspacmanRuleEngine  = new RuleEngine("mspacmanRuleEngine", String.format(RULES_PATH+"pacmanrules.clp"), map);
		//ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("pacman", true);
		//mspacmanRuleEngine.addObserver(observer);
	}
	
	
	public void preCompute(String opponent) { /*fsm.reset();*/ }
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
		//Process input
		RulesInput input = new MsPacManInput(game, mpf);
		//load facts
		//reset the rule engines
		mspacmanRuleEngine.reset();
		mspacmanRuleEngine.assertFacts(input.getFacts());
    	return mspacmanRuleEngine.run(game);
    } 
}