package es.ucm.fdi.ici.c2122.practica5.grupo05.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.ucm.fdi.ici.c2122.practica5.grupo05.JunctionInformation;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 * Clase con métodos que usa MsPacman para moverse por el juego y que son comunes a varios estados.
 */
public class PacmanMoves {
    private PacmanMoves() {}

    private static int limitPill = 300;
    /**
     * Entre los movimientos posibles de MsPacMan, eliminamos aquellos por donde nos encontramos
     * algun Chasing Ghost. Principalmente miramos todos los caminos posibles desde un punto y vemos
     * si en ese path o no hay Chasing Ghost, si hay, no es un camino y se elimina el movimiento que
     * lleva a ese camino.
     * 
     * Para mayor precision se ve no solo en el siguiente junction, si no los siguientes despues de
     * ese. Si al menos existe un camino por los segundos junctions, luego si es posible tomar ese
     * camino (considerando que en el primer junction tampoco habia peligro).
     * 
     * @param game
     * @param listChasingGhosts
     * @param mspacman
     * @return
     */
    public static List<MOVE> getGoodMoves(Game game, List<Integer> listChasingGhosts, int mspacman) {
        List<MOVE> goodMoves = new ArrayList<>();
        List<MOVE> finalGoodMoves = new ArrayList<>();
        MOVE lastMove = game.wasPacManEaten() || game.getTotalTime()==0
    			? MOVE.NEUTRAL
    			: game.getPacmanLastMoveMade();
        
        
        if(game.isJunction(mspacman) ) {
        for (MOVE m : game.getPossibleMoves(mspacman,lastMove)) {
            goodMoves.add(m);
            finalGoodMoves.add(m);
        }

        Map<MOVE, JunctionInformation> map = new HashMap<>();

        for (MOVE m : goodMoves)
            getNearestJunction(game, map, mspacman, m);

        for (MOVE m : goodMoves) {
            for (GHOST g : GHOST.values()) {
                int ghost = game.getGhostCurrentNodeIndex(g);
                int currentJunction = map.get(m).getCurrentJ();
                int newMsPacManNode = game.getNeighbour(game.getPacmanCurrentNodeIndex(), m);
                int distanceGhostToJunction = game.getShortestPathDistance(ghost, currentJunction,
                        game.getGhostLastMoveMade(g));
                int distanceMsPacManToJunction =
                        game.getShortestPathDistance(newMsPacManNode, currentJunction, m) + 2;
                int distanceMsPacManToGhost =
                        game.getShortestPathDistance(ghost, mspacman, game.getGhostLastMoveMade(g));
                int distanceMsPacManToPPill = -1;

                if (map.get(m).hasPP() != -1)
                    distanceMsPacManToPPill =
                            game.getShortestPathDistance(newMsPacManNode, map.get(m).hasPP(), m)
                                    + 1;

                // Ghost en el path
                if (CheckIfGhost.isChasing(game, g) && map.get(m).getPath().contains(ghost)) {
                    // Hay Ppill en path que queremos usar, Ghost mas cerca
                    if (map.get(m).hasPP() != -1
                            && (distanceMsPacManToGhost < distanceMsPacManToPPill)
                            && game.getGhostLastMoveMade(g).equals(game.getNextMoveTowardsTarget(
                                    ghost, mspacman, game.getGhostLastMoveMade(g), DM.PATH)))
                        finalGoodMoves.remove(m);

                    // MsPacman no llega a la junction
                    else if ((distanceMsPacManToGhost < distanceMsPacManToJunction)&&
                    		game.getGhostLastMoveMade(g).equals(game.getNextMoveTowardsTarget(
                            ghost, mspacman, game.getGhostLastMoveMade(g), DM.PATH)))
                        finalGoodMoves.remove(m);



                } else if (CheckIfGhost.isChasing(game, g)
                        && !map.get(m).getPath().contains(ghost))
                    // Ghost no esta en el path
                    if ((map.get(m).hasPP() == -1
                            // No hay pp en el camino y el Ghost llega mas rapido o al mismo tiempo
                            // al junction
                            && distanceGhostToJunction <= distanceMsPacManToJunction)&&
                    		game.getGhostLastMoveMade(g).equals(game.getNextMoveTowardsTarget(
                                    ghost, mspacman, game.getGhostLastMoveMade(g), DM.PATH)))
                        finalGoodMoves.remove(m);

                    // Ghost en junction, no vale la pena ir
                    else if (ghost == currentJunction)
                        finalGoodMoves.remove(m);



            }

            // Mismo procedimiento anterior pero en los segundos junctions, solo se ve si al menos
            // hay un camino posible:

            MOVE finalMoveToJunction = map.get(m).getLastMove();
            int junction = map.get(m).getCurrentJ();
            int distanceMsPacManToFirstJunction =
                    game.getShortestPathDistance(game.getNeighbour(mspacman, m), junction, m) + 1;
            boolean everythingGoodInSecondJunctions = false;


            Map<MOVE, JunctionInformation> map2 = new HashMap<>();

            ArrayList<MOVE> nextPossibleMoves = new ArrayList<>();
            for (MOVE m2 : game.getPossibleMoves(junction, finalMoveToJunction))
                nextPossibleMoves.add(m2);

            for (MOVE m2 : nextPossibleMoves)
                getNearestJunction(game, map2, junction, m2);

            for (MOVE m2 : nextPossibleMoves) {

                int currentJunction2 = map2.get(m2).getCurrentJ();
                int newJunctionNode = game.getNeighbour(junction, m2);

                for (GHOST g : GHOST.values()) {
                    int ghost = game.getGhostCurrentNodeIndex(g);
                    int distanceGhostToJunction = game.getShortestPathDistance(ghost,
                            currentJunction2, game.getGhostLastMoveMade(g));
                    int distanceMsPacManToJunction =
                            game.getShortestPathDistance(newJunctionNode, currentJunction2, m2) + 1
                                    + distanceMsPacManToFirstJunction;

                    if ((CheckIfGhost.isChasing(game, g)
                            && (distanceGhostToJunction > distanceMsPacManToJunction)
                            && game.getGhostLastMoveMade(g)
                                    .equals(game.getNextMoveTowardsTarget(ghost, mspacman,
                                            game.getGhostLastMoveMade(g), DM.PATH))
                            || !CheckIfGhost.isChasing(game, g))) // al menos
                    {
                        everythingGoodInSecondJunctions = true;

                    }
                }
            }
            if (!everythingGoodInSecondJunctions) // Encontre un path por donde puedo ir
                finalGoodMoves.remove(m);
        }
        }
        else
        	finalGoodMoves.add(MOVE.NEUTRAL);
        if(CheckIfPacman.isNearDangerousCage(game)) {
        	finalGoodMoves.remove(game.getNextMoveTowardsTarget(mspacman, 494,lastMove, DM.PATH));
        }
        return finalGoodMoves;
    }

    public static void getNearestJunction(Game game, Map<MOVE, JunctionInformation> map,
            int currentPos, MOVE move) {

        MOVE prevMove = move;
        int j = game.getNeighbour(currentPos, move);
        List<Integer> listPPill = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        for (int indexPpill : game.getActivePowerPillsIndices())
            listPPill.add(indexPpill);


        path.add(currentPos);
        path.add(j);
        while (!game.isJunction(j)) {
            MOVE nextMove = game.getPossibleMoves(j, prevMove)[0];
            j = game.getNeighbour(j, nextMove);
            prevMove = nextMove;
            path.add(j);
        }

        int ppill = Paths.powerPillInPath(game, path).orElse(-1);
        map.put(move, new JunctionInformation(j, new ArrayList<>(), currentPos, prevMove, move,
                ppill, path));

    }

    /**
     * Método que nos devuelve la power pill más cercana sin acercanos a un Chasing Ghost.
     * 
     * @param game
     * @param goodMoves
     * @param limit
     * @return
     */
    public static int getNearestPowerPill(Game game) {
        double distance = Integer.MAX_VALUE;
        int mspacman = game.getPacmanCurrentNodeIndex();
        int finalPill = -1;

        for (int pPillIndex : game.getActivePowerPillsIndices()) {
            double d = game.getShortestPathDistance(mspacman, pPillIndex,  game.getPacmanLastMoveMade());

            if (distance > d && d > 0
                    && d <= limitPill) {
                distance = d;
                finalPill = pPillIndex;
            }

        }

        return finalPill;
    }

    /**
     * Metodo que nos devuelve la pill mas cercana sin acercanos a un Chasing Ghost
     * 
     * @param game
     * @param goodMoves
     * @return
     */
    public static int getNearestPillWithoutChasingGhost(Game game, List<MOVE> goodMoves) {
        double distance = Integer.MAX_VALUE;
        int mspacman = game.getPacmanCurrentNodeIndex();
        int finalPill = -1;

        for (int pillIndex : game.getActivePillsIndices()) {
            double d = game.getShortestPathDistance(mspacman, pillIndex, game.getPacmanLastMoveMade());

            if (distance > d && d > 0
                    && goodMoves
                            .contains(
                                    game.getNextMoveTowardsTarget(mspacman, pillIndex,  game.getPacmanLastMoveMade(), DM.PATH))) {
                distance = d;
                finalPill = pillIndex;
            }

        }
        return finalPill;
    }

    /**
     * Modifica finalMoves de modo que asigne a cada {@link MOVE} la suma de las distancias a los
     * fantasmas comestibles.
     * 
     * @param game
     * @param finalMoves
     */
    public static void getBestMovesTowardsGhost(Game game, Map<MOVE, Double> finalMoves) {
        int mspacman = game.getPacmanCurrentNodeIndex();

        for (MOVE nextMove : game.getPossibleMoves(mspacman)) {
            int nextNode = game.getNeighbour(mspacman, nextMove);
            double distancesSum = 0;
            boolean thereAreEdibleGhosts = false;

            // buscamos las distancias desde el mspacman hacia los ghosts
            for (GHOST ghost : GHOST.values()) {
                if (game.isGhostEdible(ghost)) {
                    thereAreEdibleGhosts = true;
                    distancesSum += PathDistance.toGhost(game, nextNode, nextMove, ghost);
                }
            }
            if (thereAreEdibleGhosts)
                finalMoves.put(nextMove, distancesSum);
        }
    }

    /**
     * Sobrescribe 'movesToTotaldistance' de modo que a cada movimiento se le asigna la distancia
     * total de los fantasmas <i>chasing</i> a MsPacman para aquellos a menor distancia que 'limit'.
     * 
     * @param game
     * @param limit
     * @param movesToTotaldistance
     */
    public static void getBestMoveAwayFromGhosts(Game game, double limit,
            Map<MOVE, Double> movesToTotaldistance) {
        int mspacman = game.getPacmanCurrentNodeIndex();

        for (MOVE nextMove : game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade())) {
            int nextNode = game.getNeighbour(mspacman, nextMove);
            double totaldistance = 0;
            for (GHOST ghost : GHOST.values()) {
                double fromGhostToNextNode = PathDistance.fromGhostTo(game, ghost, nextNode);
                if (CheckIfGhost.isChasing(game, ghost) && fromGhostToNextNode <= limit)
                    totaldistance += fromGhostToNextNode;
            }
            movesToTotaldistance.put(nextMove, totaldistance);
        }
    }

	 //Mejor movimiento que me aleja mas de los ghost
    public static void getBestRunAwayGhost(Game game, double limit,
            Map<MOVE, Double> mapBestMovesAway) {

    	MOVE lastMove = game.wasPacManEaten() || game.getTotalTime()==0
    			? MOVE.NEUTRAL
    			: game.getPacmanLastMoveMade();
        int mspacman = game.getPacmanCurrentNodeIndex();
        MOVE[] m = game.getPossibleMoves(mspacman, lastMove);
      

        for (MOVE nextMove : m) {
            int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
            double d = 0;
            for (Constants.GHOST g : Constants.GHOST.values()) {
                int ghost = game.getGhostCurrentNodeIndex(g);
                if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0
                        && game.getShortestPathDistance(newNodeMsPacMan, ghost, lastMove) <= limit)
                    d += game.getDistance(newNodeMsPacMan, ghost,lastMove, DM.PATH);

            }

            mapBestMovesAway.put(nextMove, d);
          

        }

    }

}
