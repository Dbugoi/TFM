package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actions;

import java.awt.Color;

import es.ucm.fdi.ici.c2122.practica2.grupo02.GameConstants;
import es.ucm.fdi.ici.c2122.practica3.grupo02.Utils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class ReachClosestPill implements RulesAction {
	
	int pcNode = 0;
	public ReachClosestPill() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "ReachClosestPill";
	}

	@Override
	public MOVE execute(Game game) {
		pcNode = game.getPacmanCurrentNodeIndex();
		int[] activePills = game.getActivePillsIndices();
		int nearestPillNode = -1;
		int shortestDistance = -1;
		
		//For each active Pill we check which one is the nearest
		for (int activePill : activePills) {
			int distance = game.getShortestPathDistance(pcNode, activePill, game.getPacmanLastMoveMade());
			if (shortestDistance == -1 || distance < shortestDistance) {
				nearestPillNode = activePill;
				shortestDistance = distance;
			}
		}

		//if there's active pill we draw it's euclidean path
		if (nearestPillNode != -1 && GameConstants.DEBUG)
			GameView.addLines(game, Color.CYAN, pcNode, nearestPillNode);
		
		Utils.nodePacManTarget = nearestPillNode;
		
		return game.getApproximateNextMoveTowardsTarget(pcNode, nearestPillNode, game.getPacmanLastMoveMade(), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
