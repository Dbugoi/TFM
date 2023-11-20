package es.ucm.fdi.ici.c2122.practica3.grupo04;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.AvoidBeingFarFromPPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.AvoidPPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.ChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.ChaseGroupAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.GetCloseToPPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.GoPathWithMorePillsAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.GoToClosestPPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.GoToClosestPillAction;
import es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman.GoToSafeAreaAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2122"+File.separator+"practica3"+File.separator+"grupo04"+File.separator;
	HashMap<String,RulesAction> map;
	RuleEngine pacmanRuleEngine;
	
	
	public MsPacMan() {
		setName("MsPacMan 04 clipped");
		setTeam("Team 04");
	
		map = new HashMap<String,RulesAction>();

		// VARIABLES
    	int maxPillDist = 10 * 5; // Huye a una zona segura si la pill mas cercana es menor que esta distancia
    	int maxPPillDetectDist = 15*5;//distancia maxima para detectar cerca una PPill. Si se supera no huye a una PPill
    	int avoidPPillDetectDist = 7*5;//distancia maxima para detectar cerca una PPill y esquivarla si hace falta
    	int maxPPillSafeDist = 20*5;//distancia maxima para estar lejos de una PPill. Si se supera se intenta acercar a una PPill
    	
    	//rango de distancias para ir a una zona segura 
    	int minSafeDist = 3*5;
    	int maxSafeDist = 20*5;
    	
    	int maxGhostPPillDist = 8 * 5;//distancia maxima para detectar un grupo de fantasmas cerca de un PPill
    	int groupGhostsDist =  4 * 5;//si la distancia entre dos fantasmas es menor, se considera grupo de fantasma
    	float freedomDegree = 1f;
    	float dangerMult = 1.5f;
		
		//Fill Actions
		RulesAction chaseGhostAction = new ChaseGhostAction();			
		RulesAction chaseGroupAction = new ChaseGroupAction(groupGhostsDist);
		RulesAction avoidBeingFarFromPPillAction = new AvoidBeingFarFromPPillAction(freedomDegree, dangerMult);
		RulesAction getCloseToPPillAction = new GetCloseToPPillAction(freedomDegree);			
		RulesAction avoidPPillAction = new AvoidPPillAction();			
		RulesAction goToClosestPPillAction = new GoToClosestPPillAction();			
		RulesAction goToClosestPillAction = new GoToClosestPillAction();			
		RulesAction goPathWithMorePillsAction = new GoPathWithMorePillsAction();			
		RulesAction goToSafeAreaAction = new GoToSafeAreaAction(dangerMult,minSafeDist,maxSafeDist);
		
		map.put("PACMANchaseGhost", chaseGhostAction);
		map.put("PACMANchaseGroup", chaseGroupAction);
		map.put("PACMANavoidBeingFarFromPPill", avoidBeingFarFromPPillAction);
		map.put("PACMANgetCloseToPPill", getCloseToPPillAction);
		map.put("PACMANavoidPPill", avoidPPillAction);
		map.put("PACMANgoToClosestPPill", goToClosestPPillAction);
		map.put("PACMANgoToClosestPill", goToClosestPillAction);
		map.put("PACMANgoPathWithMorePills", goPathWithMorePillsAction);
		map.put("PACMANgoToSafeArea", goToSafeAreaAction);
		
		
		String rulesFile = String.format("%spacmanrules.clp", RULES_PATH);
		pacmanRuleEngine = new RuleEngine("pacman", rulesFile, map);
		
//		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("pacman", true);
//		pacmanRuleEngine.addObserver(observer);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		//Process input
		RulesInput input = new PacmanInput(game);
		//load facts
		//reset the rule engines
		pacmanRuleEngine.reset();
		pacmanRuleEngine.assertFacts(input.getFacts());
				
		return pacmanRuleEngine.run(game);
	}

}
