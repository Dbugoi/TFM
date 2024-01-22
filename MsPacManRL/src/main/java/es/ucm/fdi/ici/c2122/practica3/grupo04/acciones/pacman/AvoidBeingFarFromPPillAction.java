package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AvoidBeingFarFromPPillAction implements RulesAction {

	float dangerMult;
    float freedomDegree;
    
	public AvoidBeingFarFromPPillAction(float freedomDegree, float dangerMult) {
		this.freedomDegree = freedomDegree;
		this.dangerMult = dangerMult;
	}

	@Override
	public MOVE execute(Game game) {
		int pacman = game.getPacmanCurrentNodeIndex();

        // Se coge la PPill más segura para ir a ella
        int safestPPill = game.getActivePowerPillsIndices()[0];
        float dangerPPill = Float.MAX_VALUE;
        for (int PPill : game.getActivePowerPillsIndices()) {
        	float danger = CommonMethodsPacman.dangerLevel(game, PPill, dangerMult);
        	if (danger < dangerPPill) {
        		safestPPill = PPill;
        		dangerPPill = danger;
        	}
        }
        
        
        return CommonMethodsPacman.avoidGhosts( game,  pacman,  safestPPill);
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}
	
	@Override
	public String getActionId() {
		return  "PacmanAvoidsBeingFarFromPPill";
	}
}
