package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import java.awt.Color;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class ActionChase implements RulesAction {

	GHOST ghost;

	public ActionChase(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost)) // if it requires an action
		{
			// ir a un putno futuro de la trayectoria de pacman

			MOVE dirPac = game.getPacmanLastMoveMade();

			int indexPac = game.getPacmanCurrentNodeIndex();
//        	
//        	
//        	//interta ir a la segunda interseccion predecida de pacman
//        	for(int i = 0; i < 1; i++) {
//        		int auxNode = indexPac;
//        		while(auxNode != -1 && !game.isJunction(indexPac)) {
//        			indexPac = auxNode;
//        			auxNode = game.getNeighbour(indexPac, dirPac);
//        		}
//        		//a donde intentara ir el pacman una vez este en una interseccion?
//        		//2 opciones, powerpill, pill.
//        		//PPill ay esta cubierta, vamos a hacer pill
//
//        		//busca las pildoras mas cercanas a las que ir
//        		int nearestPill = searchNearestPill(game, true,indexPac, dirPac);
//        		if(nearestPill > -1) {
//        			dirPac = game.getApproximateNextMoveTowardsTarget(indexPac, nearestPill, game.getPacmanLastMoveMade(),DM.PATH);
//        		}
//        		
//        		//ahora tenemos la posiciojn futura del pacman
//        		//decirle q vaya a esa posicion, sin cruzarse con otros fatasmas
//        		//o al menos el fantasma q le persigue activamente
//        		
//        		//como accedo al fantasma que esat en chasing?
//        		//lo ignoro de momento porque essoty por arrancarme los pelos
//        		
//        		
			return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), indexPac,
					game.getGhostLastMoveMade(ghost), DM.PATH);

			// }

		}
		return MOVE.NEUTRAL;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}

	@Override
	public String getActionId() {
		return ghost + "chase";
	}

	private int searchNearestPill(Game game, boolean power, int index, MOVE dirPac) {
		int[] activePills;
		if (power)
			activePills = game.getActivePowerPillsIndices();
		else
			activePills = game.getActivePillsIndices();

		int pill = -1;
		int dist = -1;
		for (int i = 0; i < activePills.length; i++) {
			int path = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), activePills[i],
					game.getPacmanLastMoveMade());
			if (dist == -1 || path < dist) {
				dist = path;
				pill = activePills[i];
			}
		}
//		if (pill != -1)
//			GameView.addLines(game, Color.CYAN, game.getPacmanCurrentNodeIndex(), pill);
		return pill;
	}

	// Busca ppil mas cercana
	private int searchNearestPowerPill(Game game, int pacIndex, MOVE pacDir) {

		// Buscamos ppil mas cercana
		int distance = 100;
		int[] activePills = game.getActivePowerPillsIndices();
		int pill = -1;
		for (int i = 0; i < activePills.length; i++) {
			int path = game.getShortestPathDistance(pacIndex, activePills[i], pacDir);
			if (path < distance) {
				distance = path;
				pill = activePills[i];
			}
		}
//			if (pill != -1)
//				GameView.addLines(game, Color.CYAN, game.getPacmanCurrentNodeIndex(), pill);
		return pill;
	}

}