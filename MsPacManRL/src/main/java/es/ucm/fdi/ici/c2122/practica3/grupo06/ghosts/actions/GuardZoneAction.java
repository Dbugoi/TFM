package es.ucm.fdi.ici.c2122.practica3.grupo06.ghosts.actions;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Accion que dirige el fantasma hacia msPacMan
 */
public class GuardZoneAction implements RulesAction{

	GHOST ghost;
	private Random rnd = new Random();

	public GuardZoneAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
        if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
        	GameUtils.Quadrant q = GameUtils.getInstance().getQuadrantFromNode(game, game.getGhostCurrentNodeIndex(ghost));
        	List<Integer> quadPills = GameUtils.getInstance().getPillsInQuadrant(game, q);
        	
        	return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), quadPills.get(rnd.nextInt(quadPills.size())), 
        			game.getGhostLastMoveMade(ghost), DM.PATH);
        }
        return MOVE.NEUTRAL;
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return ghost + " guarding quadrant with pills";
	}
	
}
