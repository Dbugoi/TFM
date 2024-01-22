package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.actions;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.fdi.ici.c2122.practica3.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayAction implements RulesAction {
	@Override
	public String getActionId() { return "Ms PacMan runs for dear life."; }

	@Override
	public MOVE execute(Game game) {
		GameUtils.debugPrint("está entrando aquí y no debería.\n");
		return MOVE.NEUTRAL;
	}
	
	public MOVE selectPathToRunAway(Game game, int destinationNode) {
		MOVE nextMove = MOVE.NEUTRAL, moveToTarget = GameUtils.getInstance().getNextMoveTowardsTarget(game, destinationNode);
        int pacmanNode = game.getPacmanCurrentNodeIndex();
        //debugPrint("Hay 2 o mÃ¡s fantasmas: ");
        List<Integer> ghostsToDestiny = GameUtils.getInstance().getNormalGhostsInDirection(game, moveToTarget);
        if (ghostsToDestiny.size() == 0) {
        	GameUtils.debugPrint("COMER target y no hay peligro.\n");
            nextMove = moveToTarget; 
        }
        else {
            int destGhostNode = GameUtils.getInstance().getNearest(game,ghostsToDestiny);
            int ghostToDestDist = game.getShortestPathDistance(destGhostNode, destinationNode, game.getGhostLastMoveMade(GameUtils.getInstance().fromIntToGhost(game, destGhostNode)));
            int pacmanToDestDist = game.getShortestPathDistance(pacmanNode, destinationNode, game.getPacmanLastMoveMade());    
            int pacmanToGhostDist = game.getShortestPathDistance(pacmanNode, destGhostNode, game.getPacmanLastMoveMade());
            
            if (pacmanToDestDist < pacmanToGhostDist && //check ghost is closer to pacman than the target
            	pacmanToDestDist < ghostToDestDist) {   //check ghost is closer to target than pacman to target
            	GameUtils.debugPrint("COMER target.\n");
                nextMove = moveToTarget;
            }
            else { nextMove = caseTwoOrMore(game,pacmanNode,destinationNode,moveToTarget, game.getPacmanLastMoveMade()); }
        }    
        return nextMove;
	}
	
	public MOVE caseTwoOrMore(Game game,int pacmanNode,int destinationNode, MOVE moveToTarget,MOVE lastMoveMade) {
		GameUtils GU = GameUtils.getInstance();
		Random rnd = new Random();
		MOVE nextMove = MOVE.NEUTRAL;
        List<MOVE> validMoves = GU.getListFromArray(game, game.getPossibleMoves(pacmanNode, lastMoveMade));
        int textVar = 0;
        //GameUtils.getInstance().removeElementWithValue(validMoves, moveToTarget);

        do {
             if(validMoves.size() == 1) { nextMove = validMoves.get(0); textVar = 0; GameUtils.debugPrint("Solo hay una única dirección posible.\n"); }
             else {
                List<MOVE> dangerousMoves = new ArrayList<>();
                List<Integer> ghosts = GU.getNormalGhostsInRange(game);
                for (MOVE m : validMoves) {
                    for (GHOST g : GHOST.values()){
                        if (!dangerousMoves.contains(m) && !game.isGhostEdible(g) &&
                        		GU.getNextMoveTowardsTarget(game,game.getGhostCurrentNodeIndex(g)) == m) { 
                            dangerousMoves.add(m);
                            //ghosts.add(game.getGhostCurrentNodeIndex(g));
                            break;
                        }
                    }
                }
                
                switch(dangerousMoves.size()) {
                    case 0: 
                    	textVar = 1;
                    	//GameUtils.debugPrint("A por el target y sin peligro.\n");
                    	nextMove = validMoves.get(rnd.nextInt(validMoves.size())); break;
                    case 1:
                    	textVar = 2;
                    	//GameUtils.debugPrint("A por el target y evitando el único camino peligroso.\n");
                    	GU.removeElementWithValue(validMoves,dangerousMoves.get(0));
                        nextMove = validMoves.get(0);
                        break;
                    default:
                        for (MOVE m : dangerousMoves) { GU.removeElementWithValue(validMoves,m); }
                       
                        switch (validMoves.size()) {
                        	case 0:
                        		textVar = 3;
                        		//GameUtils.debugPrint("Todas las direcciones son peligrosas; coger la menos peligrosa.\n");
		                        int shortestDist = game.getShortestPathDistance(pacmanNode, ghosts.get(0), lastMoveMade);
		                        for (MOVE m : validMoves) {
		                            int g = GU.getNearest(game,GU.getGhostsInDirection(game,m));
		                            int auxDist = game.getShortestPathDistance(pacmanNode, g, lastMoveMade);
		                            if (shortestDist <= auxDist) { 
		                                shortestDist = auxDist;
		                                nextMove = m;
		                            }
		                        }
		                        break;
                        	case 1: nextMove = validMoves.get(0); textVar = 4; /*GameUtils.debugPrint("Solo hay una dirección no peligrosa.\n");*/ break;
                        	default: 
                        		//GameUtils.debugPrint("Hay varias direcciones no peligrosas ");
                        		MOVE auxMove = game.getPacmanLastMoveMade();
                        		GU.removeElementWithValue(validMoves,auxMove);
                        		
                        		if (validMoves.size() == 1) { nextMove = validMoves.get(0); textVar = 5; /*GameUtils.debugPrint("y evito mantener mi rumbo.\n");*/ } //avoid keeping the same direction if possible
                        		else { nextMove = validMoves.get(rnd.nextInt(validMoves.size())); textVar = 6; /*GameUtils.debugPrint("y cojo una random.\n");*/ }
                        }
                        
                }
            }
        } while(isCorridor(game, nextMove) && validMoves.size() > 1);
        
		switch(textVar) {
			case 0: GameUtils.debugPrint("Solo hay una única dirección posible.\n");                           break;
			case 1: GameUtils.debugPrint("A por el target y sin peligro.\n");                                  break;
			case 2: GameUtils.debugPrint("A por el target y evitando el único camino peligroso.\n");           break;
			case 3: GameUtils.debugPrint("Todas las direcciones son peligrosas; coger la menos peligrosa.\n"); break;
			case 4: GameUtils.debugPrint("Solo hay una dirección no peligrosa.\n");                            break;
			case 5: GameUtils.debugPrint("Hay varias direcciones no peligrosas y evito mantener mi rumbo.\n"); break;
			case 6: GameUtils.debugPrint("Hay varias direcciones no peligrosas y cojo una random.\n");         break; 
		}
        return nextMove;
	}
	
	private boolean isCorridor(Game game, MOVE intendedMove) {
		/*NOTA:
		 * 
		 * Dado que la anterior implementación de este método (hasta ahora llamado isCornerWithPills)
		 * era errónea, la hemos hecho desde cero nuevamente. No obstante, dejamos la antigua implementación también,
		 * en caso de que consideres que los cambios hechos son demasiado grandes.
		 */
		
		
		/*  //ANTIGUA IMPLEMENTACIÓN:
		int closestDist = Integer.MAX_VALUE; int closestJunc = -1; 
		for(int junc : game.getJunctionIndices()) {
			int auxDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), junc, intendedMove);
			if(auxDist < closestDist) {
				closestDist = auxDist;
				closestJunc = junc;
			}
		}
		
		int[] neighbours = game.getNeighbouringNodes(closestJunc, intendedMove);
		if(neighbours != null && neighbours.length == 1) {
			int tries = 3; MOVE aux = game.getPacmanLastMoveMade();
			int lastNode = game.getPacmanCurrentNodeIndex();
			int nextNode = game.getNeighbour(lastNode, intendedMove); 
			while(tries > 0) {
				if(game.isPillStillAvailable(nextNode)) return true;
				if(game.isJunction(nextNode)) tries--;
				aux = game.getMoveToMakeToReachDirectNeighbour(lastNode, nextNode);
				lastNode = nextNode;
				nextNode = game.getNeighbouringNodes(lastNode, aux)[0];
			}
		}
		*/
		
		//IMPLEMENTACIÓN CORREGIDA:
		MOVE currentMove = intendedMove;
		int pacmanNode   = game.getPacmanCurrentNodeIndex();
		int currentNode  = game.getNeighbour(pacmanNode, currentMove);
		
		boolean isCorner = true;
		int counter = 0;
		int[] neighbours = currentNode == -1 ? null : game.getNeighbouringNodes(currentNode, currentMove);
		if(neighbours != null) {
			while (counter < 13*GameUtils.tileSize && isCorner) {
				if(neighbours.length > 1 || (game.getPowerPillIndex(currentNode) != -1 && game.isPowerPillStillAvailable(currentNode))) { isCorner = false; }
				else {
					currentMove = game.getPossibleMoves(currentNode, currentMove)[0];
					currentNode = game.getNeighbour(currentNode, currentMove);
					neighbours  = game.getNeighbouringNodes(currentNode, currentMove);
					counter++; 
				}
			}
		}
		else { isCorner = false; }
		
		return isCorner;
	}
	
	@Override
	public void parseFact(Fact actionFact) { /*Nothing to parse*/ }
}
