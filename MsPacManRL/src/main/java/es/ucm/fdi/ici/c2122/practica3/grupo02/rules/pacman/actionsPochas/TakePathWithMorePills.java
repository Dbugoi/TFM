package es.ucm.fdi.ici.c2122.practica3.grupo02.rules.pacman.actionsPochas;

import java.awt.Color;
import java.util.EmptyStackException;
import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

import es.ucm.fdi.ici.c2122.practica2.grupo02.Tools;

public class TakePathWithMorePills implements Action {

	private MOVE[] allMoves = MOVE.values();
	
	public TakePathWithMorePills() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Take Path With More Pills";
	}

	@Override
	public MOVE execute(Game game){
		int bestWayNode = Tools.getPathWithMorePills(game, game.getPacmanCurrentNodeIndex());
		
		if(bestWayNode != -1) {
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), bestWayNode, game.getPacmanLastMoveMade(), DM.PATH);
		}
		return allMoves[Tools.rnd.nextInt(allMoves.length)];
		
	}

}
