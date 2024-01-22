package es.ucm.fdi.ici.c2122.practica3.grupo09.ghosts;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionLureToLiberty implements RulesAction {

	GHOST ghost;

	public ActionLureToLiberty(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		if (game.doesGhostRequireAction(ghost)) // if it requires an action
		{

			// va a calcualr las distancias de hacia los fantasmas en su siguiente
			// interseccion
			// si la suma de todas esas distancias es la mejor, ira a esa interseccion

			int juntionObj = -1;
			int distanciaBest = 0;
			int distancia = 0;
			int distPac = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
					game.getPacmanCurrentNodeIndex());

			int[] puntuacionBase = { 0, 0, 0, 0 };

			int gAux = 0;
			for (GHOST ghost2 : GHOST.values()) {
				if (ghost2 != ghost && game.doesGhostRequireAction(ghost2)) {
					puntuacionBase[gAux] = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
							game.getGhostCurrentNodeIndex(ghost2));
				}
				gAux++;
			}

			int index = game.getGhostCurrentNodeIndex(ghost);
			MOVE currentMove = game.getGhostLastMoveMade(ghost);
			MOVE bestMove = MOVE.NEUTRAL;
			int casillasDone = 0;
			// interta ir a la segunda interseccion con mejor puntuacion de supervivencia.
			for (int i = 0; i < 2; i++) {
				int auxNode = index;

				while (auxNode != -1 && !game.isJunction(index)) {
					index = auxNode;
					auxNode = game.getNeighbour(index, currentMove);
					casillasDone++;
				}
				// coger las posibles direcciones
				int[] direcciones = game.getNeighbouringNodes(index, currentMove);

				for (int l = 0; l < direcciones.length; l++) {
					int index2 = index;
					int casillasDone2 = 0;
					MOVE currentMove2 = game.getMoveToMakeToReachDirectNeighbour(index2, direcciones[l]);
					auxNode = game.getNeighbour(index2, currentMove2);
					index2 = auxNode;
					while (auxNode != -1 && !game.isJunction(index2)) {
						index2 = auxNode;
						auxNode = game.getNeighbour(index2, currentMove2);
						casillasDone2++;
					}
					// calcular distancias
					// puntuacion con el resto de fantasmas

					// calcular la distancia con el punto auxialiar, index2

					int distPac2 = game.getShortestPathDistance(index2, game.getPacmanCurrentNodeIndex());
					gAux = 0;
					for (GHOST ghost2 : GHOST.values()) {
						if (ghost2 != ghost && game.doesGhostRequireAction(ghost2)) {
							distancia += game.getShortestPathDistance(index2, game.getGhostCurrentNodeIndex(ghost2))
									+ puntuacionBase[gAux] + (distPac + distPac2);
						}
						gAux++;
					}

					if (bestMove == MOVE.NEUTRAL) {
						juntionObj = index2;
						bestMove = currentMove2;
					}

					// calcula ya la distancia base total
					if (distancia >= distanciaBest) {
						// el nodo del sitio
						// los 4 indices de los fantasmas

						// lo recorre ahsta lña siguiente junction y comprueba que no haya ningun
						// fantasma a aprte de el en el, entonces no es el camino ideal
						int index3 = game.getNeighbour(index2, currentMove2);
						;
						boolean hayFantasma = false;
						while (index3 != -1) {

							for (GHOST gLast : GHOST.values()) {
								if (gLast != ghost) {
									if (index3 == game.getGhostCurrentNodeIndex(gLast)) {
										hayFantasma = true;
									}
								}
							}
							index3 = game.getNeighbour(index3, currentMove2);
							;
						}
						if (!hayFantasma) {

							distanciaBest = distancia;
							juntionObj = index2;
							bestMove = currentMove2;
						}
					}

				}
				distancia = 0;

			}

			return bestMove;
			//
			// return
			// game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
			// juntionObj, game.getGhostLastMoveMade(ghost), DM.PATH);
		}

		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return ghost + "lure to liberty";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}

}
