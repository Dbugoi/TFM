package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.c2122.practica2.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class RunAway1Ghost implements RulesAction {

	@Override
	public String getActionId() { return "Ms PacMan runs away from 1 ghost"; }

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		if(!game.isJunction(pacmanNode))
			return MOVE.NEUTRAL;
		List<Integer> ghostList = new ArrayList<>();
			
	
				//crea lista con todos los fantasmas normales en juego:
		for (GHOST g : GHOST.values()) { 
			int node = game.getGhostCurrentNodeIndex(g);
			if (game.getGhostLairTime(g) <= 0 && !game.isGhostEdible(g)) { ghostList.add(node); }
		}
		
		
		int ghostNode = GameUtils.getInstance().getNearest(game, ghostList);
		if(ghostNode != -1) {
			GHOST ghost = GameUtils.getInstance().fromIntToGhost(game, ghostNode);
			MOVE move = game.getApproximateNextMoveAwayFromTarget(pacmanNode, ghostNode, game.getPacmanLastMoveMade(), DM.PATH); 
			GameView.addLines(game, Color.gray, pacmanNode, ghostNode);
		}
		return nextMove;
	}
	
	@Override
	public void parseFact(Fact actionFact) { /* Nothing to parse*/ }
}