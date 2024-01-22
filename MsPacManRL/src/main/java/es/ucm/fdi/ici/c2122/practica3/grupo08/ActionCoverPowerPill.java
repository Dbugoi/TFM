package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.awt.Color;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

//Accion para cubrir una PPill cercana a MsPacman si el fantasma esta mas cerca de ella
public class ActionCoverPowerPill implements RulesAction {

	private int limitGhostToPill; 
	GHOST ghost;
	boolean DEBUG = true;
	
	public ActionCoverPowerPill(GHOST ghost) {
		this.ghost = ghost;
		this.limitGhostToPill = 60;
	}

	@Override
	public String getActionId() {
		return "Action cover PP " + ghost;
	}

	@Override
	public MOVE execute(Game game) {
		int nearestPowerPillPacman = pacmanClosestPillIndex(game, limitGhostToPill);
		
		/*if(DEBUG)
			GameView.addPoints(game, Color.blue, game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), nearestPowerPillPacman, game.getGhostLastMoveMade(ghost)));
		*/	
		return nextMoveTowardsIndex(game, ghost, nearestPowerPillPacman);
	}
	
	//Devuelve la posicion de la power pill mas cercana a MsPacman
	private int pacmanClosestPillIndex(Game game, double limit) {
		int[] activePowerPills = game.getActivePowerPillsIndices();
		double minDist = limit;
		int index = -1;
		//Se recorren las power pills
		for (int i = 0; i < activePowerPills.length; i++) {
			//Se calcula la distancia de MsPacman a la power pill
			double actualDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), 
					activePowerPills[i], DM.PATH);
			//Si es la mas cercana o la primera dentro del limite se guarda
			if(actualDistance < minDist) {
				minDist = actualDistance;
				index = activePowerPills[i];
			}
		}
		
		return index;
	}
	//Devuelve el siguiente mvto del fantasma hacia una posicion
	private MOVE nextMoveTowardsIndex(Game game, GHOST ghostType, int destination) {
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), destination,
														 game.getGhostLastMoveMade(ghostType), DM.PATH);
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
