package es.ucm.fdi.ici.c2122.practica3.grupo02;

import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.MsPacManInput;
import es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actions.*;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;

import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.controllers.PacmanController;

public class MsPacMan extends PacmanController {
	
	private static final String PACMAN_RULES_PATH = GameConstants.RULES_PATH + "mspacmanrules.clp";
	private static final String PACMAN_NAME = "MsPacMan";
	
	HashMap<String,RulesAction> actionsMap;
	RuleEngine mspacmanRuleEngine;
	
	public MsPacMan() {
		setName("Definitely Not MsPacman");
		setTeam("G2_ICIsports");
		
		actionsMap = new HashMap<String, RulesAction>();
    	
		//ACTIONS------------------------		
		RulesAction Runaway = new RunawayFromClosestGhost();  
		actionsMap.put(Runaway.getActionId(), Runaway);
		
		RulesAction catchC_PP = new ReachClosestPowerPill();  
		actionsMap.put(catchC_PP.getActionId(), catchC_PP);
		
		RulesAction catchC_P = new ReachClosestPill();  
		actionsMap.put(catchC_P.getActionId(), catchC_P);
		
		RulesAction chase = new ChaseGhost();  
		actionsMap.put(chase.getActionId(), chase);
		
		//-------------------------------
		String fileRule = PACMAN_RULES_PATH;
		mspacmanRuleEngine = new RuleEngine(PACMAN_NAME, fileRule, actionsMap);
		if(GameConstants.DEBUG) {
			ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(PACMAN_NAME, true);
			mspacmanRuleEngine.addObserver(observer);
		}
	}

	public void preCompute(String opponent) {
//    		fsm.reset();
    }
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	//Creamos el input
       	RulesInput in = new MsPacManInput(game); 
       	//Reseteamos sus valores a saber por qué
       	mspacmanRuleEngine.reset();
       	//Obtenemos los resultados de cada iteracion
       	mspacmanRuleEngine.assertFacts(in.getFacts());
       	//En funcion de los resultados, habra devuelto un MOVE
       	return mspacmanRuleEngine.run(game);
    }
}
