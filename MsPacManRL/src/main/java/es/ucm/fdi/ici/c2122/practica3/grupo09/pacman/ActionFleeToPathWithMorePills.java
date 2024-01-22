package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeToPathWithMorePills implements RulesAction {

	@Override
	public String getActionId() {
		return "Fleeing towards path with more pills";
	}

	// Busca el camino mas optimo para moverse y lo elige

	@Override
	public MOVE execute(Game game) {
		int msPacman = game.getPacmanCurrentNodeIndex();

		int[] ghostNodes = new int[4];
		int i = 0;
		for (GHOST ghostType : GHOST.values()) {
			ghostNodes[i] = game.getGhostCurrentNodeIndex(ghostType);
			i++;
		}

		int[] path = bestPath(game, msPacman, msPacman, 0, ghostNodes, game.getPacmanLastMoveMade());

//		if (path != null && path[0] != 0) {		
		i = 0;

		while (!game.isJunction(path[i]) && path[i] != ghostNodes[0] && path[i] != ghostNodes[1]
				&& path[i] != ghostNodes[2] && path[i] != ghostNodes[3] && path[i] != 0) {
//				if (path[i]== 0)
//					break;
			i++;
		}

		return game.getApproximateNextMoveTowardsTarget(msPacman, path[i], game.getPacmanLastMoveMade(), DM.PATH);
//		}
//		return MOVE.NEUTRAL;
	}

	// Comprueba nodo a nodo los caminos posibles hasta la proxima interseccion,
	// fantasma, y evalua
	// cual de ellos es el optimo, prioizando las intersecciones, a los fantasmas, y
	// entre los mismos
	// los caminos con mas pildoras.

	private int[] bestPath(Game game, int pacmanNode, int node, int interNum, int[] ghostNodes, MOVE lastMove) {
		int[][] paths = new int[3][1000];
		MOVE[] moves;
		int[] lenghtPaths = new int[3];
		int[] phatsEnd = new int[3]; // 0 JUNCT, 2 Ghost, 1 JUNCTGHOSTDANGER
		moves = getPossibleMoves(game, node, lastMove);
		int nodeAux = node;
		boolean cont = true;
		MOVE currentmove = lastMove;
		int numPills[] = new int[3];
//		if (moves != null) {
//			
		for (int i = 0; i < moves.length; i++) {
			int k = 0;
			nodeAux = node;
			cont = true;
			currentmove = lastMove;
			while (cont) {
				int point = game.getNeighbour(nodeAux, moves[i]);
				if (point == -1) {
					// MOVE[] movesAux = game.getPossibleMoves(nodeAux,
					// game.getPacmanLastMoveMade());
					if (moves[i] != getPossibleMoves(game, nodeAux, currentmove)[0])
						moves[i] = getPossibleMoves(game, nodeAux, currentmove)[0];
					else
						break;
				} else {
					nodeAux = point;
					currentmove = moves[i];
					paths[i][k] = point;
					k++;
					if (game.isJunction(point)) {
						cont = false;
						boolean makeIt = true;
						for (int l = 0; l < ghostNodes.length; l++) {
							if (game.getShortestPathDistance(pacmanNode, point) > game
									.getShortestPathDistance(ghostNodes[l], point)) {
								makeIt = false;
								phatsEnd[i] = 1;
								break;
							}
						}
						if (makeIt) {
							interNum++;
							if (interNum < 3) {
								int[] subPath = bestPath(game, pacmanNode, point, interNum, ghostNodes, currentmove);
//									if (subPath != null) {
								for (int j = 0; j < subPath.length; j++) {
									if (subPath[j] == 0)
										break;
									paths[i][k] = subPath[j];
									k++;
								}
							}
//								}
							phatsEnd[i] = 0;
						}
						lenghtPaths[i] = k;
					} else {
						for (int l = 0; l < ghostNodes.length; l++) {
							if (point == ghostNodes[l]) {
								cont = false;
								lenghtPaths[i] = k;
								phatsEnd[i] = 2;
								break;
							}
						}
					}
					if (game.isPillStillAvailable(point))
						numPills[i]++;
				}
			}
		}
		// RESOLVER
		int bestPath = -1;
		// int lastNode = paths[0][lenghtPaths[0]];
		int bestDec = -1;
		for (int i = 0; i < moves.length; i++) {
			if (bestDec == -1) {
				bestDec = phatsEnd[i];
				bestPath = i;
			} else if (phatsEnd[i] < bestDec) {
				bestDec = phatsEnd[i];
				bestPath = i;
			} else if (phatsEnd[i] == bestDec) {
				if (lenghtPaths[i] != 0 && lenghtPaths[bestPath] != 0) {
					if (numPills[i] / lenghtPaths[i] > numPills[bestPath] / lenghtPaths[bestPath]) {
						bestPath = i;
					}
				}

			}
		}

		return paths[bestPath];
//			}
//		return null;
	}

	// getPossibleMoves, por que el del game me funciona mal por alguna razon

	MOVE[] getPossibleMoves(Game game, int node, MOVE lastMove) {
		MOVE[] aux = { MOVE.UP, MOVE.RIGHT, MOVE.LEFT, MOVE.DOWN };
		MOVE[] possAux = new MOVE[4];
		int j = 0;
		MOVE cant = null;

		switch (lastMove) {
		case DOWN:
			cant = MOVE.UP;
			break;
		case LEFT:
			cant = MOVE.RIGHT;
			break;
		case RIGHT:
			cant = MOVE.LEFT;
			break;
		case UP:
			cant = MOVE.DOWN;
			break;
		default:
			break;
		}

		for (int i = 0; i < 4; i++) {
			int point = game.getNeighbour(node, aux[i]);
			if (point != -1 && aux[i] != cant) {
				possAux[i] = aux[i];
				j++;
			} else
				possAux[i] = MOVE.NEUTRAL;
		}
		MOVE[] poss = new MOVE[j];
		j = 0;
		for (int i = 0; i < 4; i++) {
			if (possAux[i] != MOVE.NEUTRAL) {
				poss[j] = possAux[i];
				j++;
			}
		}
		return poss;
	}

	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse

	}
}