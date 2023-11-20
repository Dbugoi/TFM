package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.awt.Color;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

//Accion para dirigir al fantasma hacia MsPacman de forma directa
public class ActionChase implements RulesAction {

    GHOST ghost;
    boolean DEBUG = false;
    
	public ActionChase(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return "Action Chase: " + ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if(game.getGhostLairTime(ghost) <= 0 && DEBUG)
			GameView.addPoints(game, Color.red, game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost)));
        
		if (game.doesGhostRequireAction(ghost))  
        {
                return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                        game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
        }
		
        return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
