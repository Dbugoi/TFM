package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Accion para realizar un movimiento aleatorio mientras se encuentra a una distancia prudencial de MsPacMan
public class ActionRandom implements RulesAction {
	private MOVE[] allMoves = MOVE.values();
	private Random rnd = new Random();
	private int sueLimit = 60; //Limite para el comportamiento de Sue
	GHOST ghost;
	public ActionRandom(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public String getActionId() {
		return "Action Random: " + ghost;
	}

	@Override
	public MOVE execute(Game game) {
		//Si la distancia entre sue y MsPacman es menor que el limite sue se mueve de manera aleatoria
		if(game.getDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), DM.PATH) < sueLimit)
			return allMoves[rnd.nextInt(allMoves.length)];
		//Si no esta suficientemente cerca se mueve hacia MsPacman
		else return nextMoveTowardsIndex(game, ghost, game.getPacmanCurrentNodeIndex());
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
