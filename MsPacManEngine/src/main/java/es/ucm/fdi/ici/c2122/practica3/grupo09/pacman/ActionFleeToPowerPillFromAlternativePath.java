package es.ucm.fdi.ici.c2122.practica3.grupo09.pacman;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionFleeToPowerPillFromAlternativePath implements RulesAction {
//Sobra
	@Override
	public String getActionId() {
		return "Taking Alternative Path To Pill";
	}

	// Busca el mejor camino posible
	@Override
	public MOVE execute(Game game) {
		int msPacman = game.getPacmanCurrentNodeIndex();

		int[] activePills;
		activePills = game.getActivePowerPillsIndices();

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

		int[] ghostNodes = new int[4];
		int i = 0;
		for (GHOST ghostType : GHOST.values()) {
			ghostNodes[i] = game.getGhostCurrentNodeIndex(ghostType);
			i++;
		}

		int[] path = bestPath(game, msPacman, msPacman, pill, 0, ghostNodes, game.getPacmanLastMoveMade());

//		if (path != null && path[0] != 0) {		
//			i = 0;
//			
		while (path[i] != pill && !game.isJunction(path[i]) && path[i] != ghostNodes[0] && path[i] != ghostNodes[1]
				&& path[i] != ghostNodes[2] && path[i] != ghostNodes[3] && path[i] != -1 && path[i] != 0) {
			i++;
		}

		return game.getApproximateNextMoveTowardsTarget(msPacman, path[i], game.getPacmanLastMoveMade(), DM.PATH);
//		}
//		return MOVE.NEUTRAL;
	}

	// Busca entre varios caminos cual es el optimo, basandose en si encuentra un
	// powerpill, una interseccion, o un fantasma
	// entre varias opciones iguales, coge la mas segura.

	private int[] bestPath(Game game, int pacmanNode, int node, int nearestPill, int interNum, int[] ghostNodes,
			MOVE lastMove) {
		int[][] paths = new int[3][1000];
		MOVE[] moves;
		int[] lenghtPaths = new int[3];
		int[] phatsEnd = new int[3]; // 0 PP, 1 JUNCT, 2 Ghost, 3 JUNCTGHOSTDANGER, 4 PILLDANGER
		moves = getPossibleMoves(game, node, lastMove);
		int nodeAux = node;
		boolean cont = true;
		MOVE currentmove = lastMove;
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
					else if (currentmove != moves[i])
						moves[i] = currentmove;
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
							if (ghostNodes[l] != 1292) {
								if (game.getShortestPathDistance(pacmanNode, point) > game
										.getShortestPathDistance(ghostNodes[l], point)) {
									makeIt = false;
									phatsEnd[i] = 3;
									break;
								}
							}

						}
						if (makeIt) {
							interNum++;
							if (interNum < 3) {
								int[] subPath;
								subPath = bestPath(game, pacmanNode, point, nearestPill, interNum, ghostNodes,
										currentmove);
//									if (subPath != null) {										
								for (int j = 0; j < subPath.length; j++) {
									if (subPath[j] == 0)
										break;
									paths[i][k] = subPath[j];
									k++;
								}
//									}
							}
							phatsEnd[i] = 1;
						}
						lenghtPaths[i] = k;
					} else if (point == nearestPill) {
						cont = false;
						boolean makeIt = true;
						for (int l = 0; l < ghostNodes.length; l++) {
							if (game.getShortestPathDistance(pacmanNode, point) > game
									.getShortestPathDistance(ghostNodes[l], point)) {
								makeIt = false;
								phatsEnd[i] = 2;
								break;
							}
						}
						if (makeIt) {
							phatsEnd[i] = 0;
						}
						lenghtPaths[i] = k;
					} else {
						for (int l = 0; l < ghostNodes.length; l++) {
							if (point == ghostNodes[l]) {
								cont = false;
								lenghtPaths[i] = k;
								phatsEnd[i] = 4;
								break;
							}
						}
					}
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
				if (bestDec == 0 || bestDec == 2) {
					if (lenghtPaths[i] < lenghtPaths[bestPath]) {
						bestPath = i;
					}
				} else if (bestDec == 1) {
					if (paths[i][lenghtPaths[i] - 1] == nearestPill
							&& paths[i][lenghtPaths[bestPath] - 1] != nearestPill) {
						bestPath = i;
					} else if (game.getShortestPathDistance(paths[i][0], nearestPill) < game
							.getShortestPathDistance(paths[bestPath][0], nearestPill)) {
						bestPath = i;
					}
				} else if (bestDec == 3 || bestDec == 4) {
					if (lenghtPaths[i] > lenghtPaths[bestPath]) {
						bestPath = i;
					}
				}
			}
		}
		return paths[bestPath];
	}

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