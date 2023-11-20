package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeWithPowerPill implements RulesAction {
	// Distancia a la que consideramos que el fantasma está lo suficientemente lejos
	final double maxDistance = 60;
	final double offset = 30;

	int pacmanNode;

	// Nº de posibles direcciones desde la interseccion
	int numDirs;
	MOVE possibleMoves[];
	MOVE notObstructedMoves[] = { MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL };

	// Info de las distancias a los fantasmas en PATH
	double distances[] = { 1000, 1000, 1000, 1000 };

	// Los movimientos necesarios para ir hacia ellos
	MOVE moveToReachGhost[] = { MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL };

	// Y el nº de pills de cada posible camino
	int numPills[] = { 0, 0, 0, 0 };

	MOVE moveToReachNearestPPill = MOVE.NEUTRAL;

	public ActionFleeWithPowerPill() {

	}

	@Override
	public MOVE execute(Game game) {
		pacmanNode = game.getPacmanCurrentNodeIndex();

		possibleMoves = game.getPossibleMoves(pacmanNode, game.getPacmanLastMoveMade());

		numDirs = possibleMoves.length;

		getPathInfo(game);

		return getBestPath(game);
	}

	private MOVE getBestPath(Game game) {
		MOVE bestMove = MOVE.NEUTRAL;

		getNotObstructedMoves();

		// Si hay caminos no obstruidos
		if (notObstructedMoves[0] != MOVE.NEUTRAL) {
			// Si hay 2 caminos no obstruidos (raro)
			if (notObstructedMoves[1] != MOVE.NEUTRAL) {
				// Si hay 3 caminos no obstruidos (imposible)
				if (notObstructedMoves[2] != MOVE.NEUTRAL) {
					bestMove = moveToReachNearestPPill;
					// System.console().printf("------------Esto no debería ocurrir-------------");
				}
				// Tomamos en el que sea más probable obtener más pills
				else {
					// Si uno de los caminos no obstruidos va hacia la powerpill
					if (notObstructedMoves[0] == moveToReachNearestPPill
							|| notObstructedMoves[1] == moveToReachNearestPPill) {
						bestMove = moveToReachNearestPPill;
					} else {
						// Si hay pills en la siguiente casilla, vamos por esa
						int neighbour = game.getNeighbour(pacmanNode, notObstructedMoves[0]);
						if (neighbour != -1 && game.getPillIndex(neighbour) != -1) {
							bestMove = notObstructedMoves[0];
						} else {
							bestMove = notObstructedMoves[1];
						}
					}
				}
			}
			// Tomamos el más seguro, estamos huyendo al fin y al cabo
			else {
				bestMove = notObstructedMoves[0];
			}
		}
		// Todos los caminos están obstruidos, tomamos el camino que tenga más alejado
		// su fantasma
		else {
			int i = 0;
			boolean danger = false;
			double bestDist = distances[0];
			for (int e = 1; e < distances.length; e++) {
				// Si es mejor, lo tomamos
				if (bestDist + offset < distances[e]) {
					// Comprobamos que ese movimiento no sea peligroso
					for (int x = 0; x < e && !danger; x++) {
						danger = moveToReachGhost[x] == moveToReachGhost[e] && distances[x] < maxDistance / 2;
					}
					if (!danger) {
						bestDist = distances[e];
						i = e;
					}
					danger = false;
				}
				// Si no, comprobamos que pueda ser mejor con el offset
				else if (bestDist - offset < distances[e]) {
					// Comprobamos que ese movimiento no sea peligroso
					for (int x = 0; x < e && !danger; x++) {
						danger = moveToReachGhost[x] == moveToReachGhost[e] && distances[x] < maxDistance / 2;
					}
					// Miramos su nº de pills para decidir
					bestDist = numPills[i] < numPills[e] && !danger ? distances[e] : bestDist;

					danger = false;
				}
				// O se queda como estaba
			}
			// Guardamos el mov final
			bestMove = moveToReachGhost[i];
		}

		return bestMove;
	}

	private void getNotObstructedMoves() {
		int e = 0;
		for (MOVE move : possibleMoves) {
			int i = 0;
			boolean blocked = false;

			while (i < moveToReachGhost.length && !blocked) {
				// Comprobamos si el camino está bloqueado y si la distancia es preocupante
				blocked = moveToReachGhost[i] == move && distances[i] < maxDistance;
				i++;
			}

			if (!blocked) {
				notObstructedMoves[e] = move;
				e++;
			}
		}
	}

	private int numPillsInPath(int[] path, Game game) {
		int pills = 0;

		for (int node : path) {
			if (game.getPillIndex(node) != -1) {
				pills++;
			}
		}

		return pills;
	}

	private void getPathInfo(Game game) {
		int i = 0;
		int[] actualPath = { 0 };
		for (GHOST ghostType : GHOST.values()) {
			if (game.getGhostLairTime(ghostType) <= 0) {
				actualPath = game.getShortestPath(pacmanNode, game.getGhostCurrentNodeIndex(ghostType),
						game.getPacmanLastMoveMade());
				moveToReachGhost[i] = game.getApproximateNextMoveTowardsTarget(pacmanNode,
						game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade(), DM.PATH);
			}
			distances[i] = actualPath.length;
			numPills[i] = numPillsInPath(actualPath, game);
		}
		int ppill = getClosestPPillNode(game);
		if (ppill != -1)
			moveToReachNearestPPill = game.getApproximateNextMoveTowardsTarget(pacmanNode, ppill,
					game.getPacmanLastMoveMade(), DM.PATH);
	}

	private int getClosestPPillNode(Game game) {
		int pill = -1;
		int dist = -1;

		int[] powerpills = game.getActivePowerPillsIndices();

		for (int i = 0; i < powerpills.length; i++) {
			int path = game.getShortestPathDistance(pacmanNode, powerpills[i], game.getPacmanLastMoveMade());
			if (dist == -1 || path < dist) {
				dist = path;
				pill = powerpills[i];
			}
		}

		return pill;
	}

	@Override
	public String getActionId() {
		return "Pacman flees path with more pills";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}
}
