package es.ucm.fdi.ici.c2122.practica2.grupo05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;


import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public final class GameUtils {
    private static final Random rnd = new Random();
    public static final int cage = 492;
    private static int limitForEdibleGhost = 30;
    private static int limitPill = 300;
    private static int limToEdibles = 60; // ESTAS LAS USO PARA EL GHOST, NO USO LAS OTRAS POR SI
                                          // SON DEL PACMAN
    private static int limToMsPacMan = 20000; // Ponerlas en game utils? por si las uqiero usar
                                              // desde actions
    private static int limToPPillsPac = 19;
    private static int limToPPillsGhostC = 50; // Chase
    private static int limToPPillsGhostE = 45; // Edible
    private static int limToChaseGost = 60;

    public static final int TOP_LEFT_CORNER = 0;
    public static final int TOP_RIGHT_CORNER = 77;
    public static final int BOTTOM_LEFT_CORNER = 1191;
    public static final int BOTTOM_RIGHT_CORNER = 1291;

    public static int getLimToEdibles() {
        return limToEdibles;
    }

    public static int getLimToMsPacMan() {
        return limToMsPacMan;
    }

    public static int getLimToChaseGost() {
        return limToChaseGost;
    }

    public static int getLimToPPillsGhostC() {
        return limToPPillsGhostC;
    }

    public static int getLimToPPillsGhostE() {
        return limToPPillsGhostE;
    }

    public static int getLimToPPillsPac() {
        return limToPPillsPac;
    }

    public static int getLimitForEdibleGhost() {
        return limitForEdibleGhost;
    }

    private GameUtils() {}

    /**
     * Distance from the given {@link GHOST} to MsPacMan.
     * 
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static double distanceToMsPacman(Game game, GHOST ghost, DM distanceMeasure) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        return game.getDistance(ghostIndex, pacmanIndex, distanceMeasure);
    }

    /**
     * Gets next MsPacMan's next move away from the ghost target
     * 
     * @param game
     * @param ghost target
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveAwayFromGhost(Game game, GHOST ghost, DM distanceMeasure) {
        int[] msPacmanNeighbouringNodes =
                game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
        double maxDistance = Double.NEGATIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : msPacmanNeighbouringNodes) {
            double distance = game.getDistance(game.getGhostCurrentNodeIndex(ghost), node,
                    game.getGhostLastMoveMade(ghost), distanceMeasure);
            if (maxDistance < distance) {
                maxDistance = distance;
                bestMove = game.getMoveToMakeToReachDirectNeighbour(
                        game.getPacmanCurrentNodeIndex(), node);
            }
        }
        return bestMove;
    }

    /**
     * Gets next MsPacMan's next move towards the ghost target
     * 
     * @param game
     * @param ghost target
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveTowardsGhost(Game game, GHOST ghost, DM distanceMeasure) {
        int[] msPacmanNeighbouringNodes =
                game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
        double minDistance = Double.POSITIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : msPacmanNeighbouringNodes) {
            double distance = game.getDistance(node, game.getGhostCurrentNodeIndex(ghost),
                    game.getPacmanLastMoveMade(), distanceMeasure);
            if (minDistance > distance) {
                minDistance = distance;
                bestMove = game.getMoveToMakeToReachDirectNeighbour(
                        game.getPacmanCurrentNodeIndex(), node);
            }
        }
        return bestMove;
    }

    /**
     * Returns best move for ghost 'origin' to get to ghost 'destiny'
     */
    public static MOVE getNextMoveTowardsGhost(Game game, GHOST origin, GHOST destiny,
            DM distanceMeasure) {
        int[] ghostNeighbouringNodes =
                game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(origin));
        double minDistance = Double.POSITIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : ghostNeighbouringNodes) {
            double distance = game.getDistance(node, game.getGhostCurrentNodeIndex(destiny),
                    game.getGhostLastMoveMade(origin), distanceMeasure);
            if (minDistance > distance) {
                minDistance = distance;
                bestMove = game.getMoveToMakeToReachDirectNeighbour(
                        game.getPacmanCurrentNodeIndex(), node);
            }
        }
        return bestMove;
    }

    /**
     * Gets next ghost's next move towards MsPacMan
     * 
     * @param game
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveTowardsMsPacMan(Game game, GHOST ghost, DM distanceMeasure) {
        int[] ghostNeighbouringNodes =
                game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(ghost));
        double minDistance = Double.POSITIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : ghostNeighbouringNodes) {
            double distance = game.getDistance(node, game.getPacmanCurrentNodeIndex(),
                    game.getGhostLastMoveMade(ghost), distanceMeasure);
            if (minDistance > distance) {
                minDistance = distance;
                bestMove = game.getMoveToMakeToReachDirectNeighbour(
                        game.getPacmanCurrentNodeIndex(), node);
            }
        }
        return bestMove;
    }

    /**
     * Gets next ghost's next move away from MsPacMan
     * 
     * @param game
     * @param ghost
     * @param distanceMeasure
     * @return
     */
    public static MOVE getNextMoveAwayFromMsPacMan(Game game, GHOST ghost, DM distanceMeasure) {
        int[] ghostNeighbouringNodes =
                game.getNeighbouringNodes(game.getGhostCurrentNodeIndex(ghost));
        double maxDistance = Double.NEGATIVE_INFINITY;
        MOVE bestMove = MOVE.NEUTRAL;

        for (int node : ghostNeighbouringNodes) {
            double distance = game.getDistance(game.getPacmanCurrentNodeIndex(), node,
                    game.getPacmanLastMoveMade(), distanceMeasure);
            if (maxDistance < distance) {
                maxDistance = distance;
                bestMove = game.getMoveToMakeToReachDirectNeighbour(
                        game.getPacmanCurrentNodeIndex(), node);
            }
        }
        return bestMove;
    }

    /**
     * @param game
     * @param distanceMeasure
     * @param limit
     * @return Closest ghost that isn't edible and is at a distance less than limit.
     */
    public static GHOST getClosestGhost(Game game, DM distanceMeasure) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) GameUtils.distanceToMsPacman(game, g, distanceMeasure);
        Optional<GHOST> optGhost = Arrays.stream(GHOST.values())
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));

        if (!optGhost.isPresent())
            throw new IllegalStateException("There are no ghosts in game");

        return optGhost.get();
    }

    /**
     * @param game
     * @param distanceMeasure
     * @param limit
     * @return Closest ghost that isn't edible and is at a distance less than limit.
     */
    public static Optional<GHOST> getClosestChasingGhost(Game game, DM distanceMeasure, int limit) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) GameUtils.distanceToMsPacman(game, g, distanceMeasure);
        return Arrays.stream(GHOST.values()).filter(gh -> !game.isGhostEdible(gh))
                .filter(gh -> distToMsPacman.applyAsInt(gh) < limit)
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));
    }

    /**
     * @param game
     * @return Closest ghost that is edible
     */
    public static Optional<GHOST> getClosestEdibleGhost(Game game, DM distanceMeasure) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) GameUtils.distanceToMsPacman(game, g, distanceMeasure);
        return Arrays.stream(GHOST.values()).filter(game::isGhostEdible)
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));
    }

    public static boolean isMsPacManCloseToPowerPill(Game game, DM distanceMeasure, int limit) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        IntUnaryOperator distToMsPacman =
                (int i) -> (int) game.getDistance(pacmanIndex, i, lastMove, distanceMeasure);

        return Arrays.stream(game.getPowerPillIndices())
                .anyMatch(i -> distToMsPacman.applyAsInt(i) < limit);
    }

    /**
     * @param game
     * @return Index of the closest power pill to MsPacMan. If there are no power pills, it returns
     *         a -1
     */
    public static int getClosestPowerPillToMsPacMan(Game game, DM distanceMeasure) {
        int pacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMove = game.getPacmanLastMoveMade();
        IntUnaryOperator distToMsPacman =
                i -> (int) game.getDistance(pacmanIndex, i, lastMove, distanceMeasure);

        Optional<Integer> closestPill = Arrays
                .stream(game.getActivePowerPillsIndices())
                .boxed()
                .min((p1, p2) -> distToMsPacman.applyAsInt(p1) - distToMsPacman.applyAsInt(p2));

        return closestPill.orElse(-1);

    }

    public static MOVE getRandomMove() {
        MOVE[] allMoves = MOVE.values();
        return allMoves[rnd.nextInt(allMoves.length)];
    }


    public static List<int[]> getPathsToClosestJunctions(Game game, int currentIndex,
            MOVE lastMove) {
        int[] junctionIndices = game.getJunctionIndices();

        /*
         * Consideramos que "los cruces más cercanos" son aquellos a los que se puede llegar
         * directamente desde 'currentIndex' sin tener que pasar por otro cruce.
         * 
         * Para conseguir los caminos a los cruces más cercanos tenemos que tener en cuenta que:
         * 
         * - Dados los caminos a todos los cruces, los cruces más cercanos son los que tienen
         * caminos más cortos
         * 
         * - Para llegar a los cruces más lejanos hay que pasar necesariamente por alguno de los más
         * cercanos
         * 
         * - Los caminos que nos da game.getShortestPath(...) son arrays de índices en los que
         * necesariamente, creo, que el primer indice se corresponde con 'currentIndex', por los que
         * el primer índice de todos esos arrays será siempre el mismo.
         * 
         * - El 2º elemento de los arrays que representan los caminos a los cruces más cercanos será
         * siempre único. Si hay 2 caminos con el mismo 2º elemento y 'currentIndex' se corresponde
         * con un cruce, uno de dichos caminos lleva a un cruce que no es de los más cercanos.
         */

        // Caminos a los cruces del tablero ordenados de menor a mayor longitud
        List<int[]> sortedPathsToAllJunctions = Arrays.stream(junctionIndices).boxed()
                .map(i -> game.getShortestPath(currentIndex, i, lastMove))
                .sorted((a, b) -> a.length - b.length)
                .collect(Collectors.toList());

        List<int[]> pathsToClosestJunctions = new LinkedList<>();
        // El camino más corto de todos siempre lleva al cruce más próximo, así que
        // forma parte de la solución
        pathsToClosestJunctions.add(sortedPathsToAllJunctions.get(0));
        sortedPathsToAllJunctions.remove(0);

        for (int[] sPath : sortedPathsToAllJunctions)
            if (pathsToClosestJunctions.stream()
                    .allMatch(path -> path.length == 0 || path[0] != sPath[0]))
                pathsToClosestJunctions.add(sPath);

        return pathsToClosestJunctions;
    }

    public static List<Integer> getClosestJunctions(Game game, int currentIndex,
            MOVE lastMove) {
        class JunctionInfo {
            final int junction;
            final int[] path;

            public JunctionInfo(int junction, int[] path) {
                this.junction = junction;
                this.path = path;
            }
        }
        int[] junctionIndices = game.getJunctionIndices();

        // Caminos a los cruces del tablero ordenados de menor a mayor longitud
        List<JunctionInfo> sortedPathsToAllJunctions = Arrays.stream(junctionIndices).boxed()
                .map(j -> new JunctionInfo(j, game.getShortestPath(currentIndex, j, lastMove)))
                .sorted((a, b) -> a.path.length - b.path.length)
                .collect(Collectors.toList());


        List<JunctionInfo> closestJunctionsInfo = new LinkedList<>();
        // El camino más corto de todos siempre lleva al cruce más próximo, así que
        // forma parte de la solución
        closestJunctionsInfo.add(sortedPathsToAllJunctions.get(0));
        sortedPathsToAllJunctions.remove(0);

        for (JunctionInfo jInfo : sortedPathsToAllJunctions)
            if (closestJunctionsInfo.stream()
                    .allMatch(info -> info.path.length == 0 || info.path[0] != jInfo.path[0]))
                closestJunctionsInfo.add(jInfo);

        return closestJunctionsInfo.stream().map(i -> i.junction).collect(Collectors.toList());
    }

    // Buscar todos los ghost comibles dentro de un rango
    public static List<Integer> getNearestEdibleGhosts(Game game) {
        int mspacman = game.getPacmanCurrentNodeIndex();
        ArrayList<Integer> listIndex = new ArrayList<Integer>();
        for (Constants.GHOST g : Constants.GHOST.values()) { // miramos entre todos los
                                                             // ghosts
            int ghost = game.getGhostCurrentNodeIndex(g);
            double d = game.getDistance(mspacman, ghost, DM.PATH);
            if (game.isGhostEdible(g)
                    && (game.getGhostEdibleTime(g) / 2 + d < game
                            .getGhostEdibleTime(g))
                    && d <= limitForEdibleGhost)
                listIndex.add(ghost);
        }

        return listIndex;
    }

    // metodo que nos devuelve la pill mas cercana sin acercanos a un Chasing Ghost
    public static MOVE getNearestPillWithoutChasingGhost(Game game, List<MOVE> goodMoves) {
        double distance = Integer.MAX_VALUE;
        int mspacman = game.getPacmanCurrentNodeIndex();
        int finalPill = -1;

        for (int pillIndex : game.getActivePillsIndices()) {
            double d = game.getDistance(mspacman, pillIndex, DM.PATH);

            if (distance > d && d > 0
                    && goodMoves.contains(game.getNextMoveTowardsTarget(
                            mspacman, pillIndex, DM.PATH))
                    && d <= limitPill) {
                distance = d;
                finalPill = pillIndex;
            }

        }

        if (finalPill == -1 && goodMoves.size() == 0)
            return MOVE.NEUTRAL;
        else if (finalPill == -1 && goodMoves.size() > 0)
            return goodMoves.get(rnd.nextInt(goodMoves.size()));
        else
            return game.getNextMoveTowardsTarget(mspacman, finalPill, DM.PATH);
    }

    //msPacMan en Junction
    public static boolean isMsPacmanInJunction(Game game) {
        for (int junction : game.getJunctionIndices())
            if (junction == game.getPacmanCurrentNodeIndex())
                return true;
        return false;
    }
  //Ghost en Junction
    public static boolean isGhostInJunction(Game game, GHOST ghost) {
        for (int junction : game.getJunctionIndices())
            if (junction == game.getGhostCurrentNodeIndex(ghost))
                return true;
        return false;
    }
  //ve si el ghost "ghost" puede ser comido
    public static boolean couldGhostBeEaten(Game game, GHOST ghost) {
        if (!game.isGhostEdible(ghost))
            return false;
        else {
            /*
             * Suponemos que los fantasmas comestibles se mueven a la mitad de la velocidad que se
             * mueven normalmente.
             */
            double distFromMsPacmanToGhost =
                    game.getDistance(game.getPacmanCurrentNodeIndex(),
                            game.getGhostCurrentNodeIndex(ghost),
                            game.getPacmanLastMoveMade(), DM.PATH);
            double distTravelledByEdibleGhost = game.getGhostEdibleTime(ghost) / 2.0;
            double distTravelledByMsPacmanWhileGhostEdible =
                    game.getGhostEdibleTime(ghost);


            return distFromMsPacmanToGhost
                    + distTravelledByEdibleGhost < distTravelledByMsPacmanWhileGhostEdible;
        }
    }



    // Buscar los ghost no comibles problematicos
    public static List<Integer> getNearestChasingGhosts(Game game, double e) {
        int mspacman = game.getPacmanCurrentNodeIndex();
        ArrayList<Integer> listIndex = new ArrayList<Integer>();
        for (Constants.GHOST g : Constants.GHOST.values()) { // miramos entre todos los ghosts

            int ghost = game.getGhostCurrentNodeIndex(g);
            double d = game.getDistance(mspacman, ghost, DM.PATH);

            if (!game.isGhostEdible(g) && d <= e && game.getGhostLastMoveMade(g)
                    .equals(game.getNextMoveTowardsTarget(ghost, mspacman, DM.PATH))) // GHOST ME
                                                                                      // PERSIGUE
                listIndex.add(ghost);
            if (game.getGhostLairTime(g) != 0 && !listIndex.contains(cage)
                    && game.getGhostLairTime(g) < game.getShortestPathDistance(mspacman, cage)
                    && game.getShortestPathDistance(mspacman, cage) <= e)
                listIndex.add(cage);
        }

        return listIndex;
    }


    // Buscar todos los ghost comibles dentro de un rango
    public static NearestEdibleGhostInformation getNearestEdibleGhosts(Game game, int limit) {
        int mspacman = game.getPacmanCurrentNodeIndex();
        double minD = Double.MAX_VALUE;
        double minEdibleTime = Double.MAX_VALUE;
        List<GHOST> listGhost = new ArrayList<GHOST>();
        for (Constants.GHOST g : Constants.GHOST.values()) { // miramos entre todos los ghosts
        	if(game.getGhostLairTime(g)==0 ) {
            int ghost = game.getGhostCurrentNodeIndex(g);
            double d = game.getShortestPathDistance(mspacman, ghost, game.getPacmanLastMoveMade());
            if (game.getGhostEdibleTime(g)>0 && game.isGhostEdible(g) /*&& d <= limit*/
                    && d*2<=game.getGhostEdibleTime(g)) {
            	listGhost.add(g);
                if (minD > d) {
                    minD = d;
                    minEdibleTime = game.getGhostEdibleTime(g);
                }
            }
        	}
        }
        
        for(GHOST edible: new ArrayList<>(listGhost)) //Erase if chasingGhost in path
			for(GHOST g: GHOST.values()) {
				int chasing = game.getGhostCurrentNodeIndex(g);
				if(!g.equals(edible) && !game.isGhostEdible(g) && game.getGhostLairTime(g)==0) {
				int d = game.getShortestPathDistance(chasing, game.getGhostCurrentNodeIndex(edible), game.getGhostLastMoveMade(g));
				if(d> game.getShortestPathDistance(mspacman, game.getGhostCurrentNodeIndex(edible), game.getPacmanLastMoveMade()))
					listGhost.remove(edible);
				}
				
			}
        return new NearestEdibleGhostInformation(listGhost, minD, minEdibleTime);
    }

    //edible ghost a punto de cambiar a chasing
    public static boolean dangerEdibleGhost(Game game, GHOST g, double d) {
        return game.isGhostEdible(g)
                && (d + game.getGhostEdibleTime(g) * 0.5 > game.getGhostEdibleTime(g)) && d <= 30;

    }

    //Un Ghost es un Chasing Ghost
    public static boolean isChasingGhost(Game game, GHOST g) {
        return !game.isGhostEdible(g) && game.getGhostLairTime(g) == 0;
    }

    // Entre los movimientos posibles de MsPacMan, eliminamos aquellos por donde nos encontramos
    // algun Chaising Ghost.Principalmente miramos todos los caminos posibles desde un punto
    //y vemos si en ese path o no hay Chasing Ghost, si hay, no es un camino y se elimina el
    //movimiento que lleva a ese camino
    //Para mayor precision se ve no solo en el siguiente junction, si no los siguientes despues de ese
    //Si al menos existe un camino por los segundos junctions, kuego si es posible tomar ese camino (considerando 
    //que en el primer junction tampoco habia peligro
    public static List<MOVE> getGoodMoves(Game game, List<Integer> listChasing, int mspacman) {
        List<MOVE> goodMoves = new ArrayList<>();
        List<MOVE> finalGoodMoves = new ArrayList<>();
        for (MOVE m : game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade())) {
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

                if (isChasingGhost(game, g) && map.get(m).getPath().contains(ghost)) { // Ghost en
                                                                                       // el path
                   if (map.get(m).hasPP() != -1
                            && (distanceMsPacManToGhost < distanceMsPacManToPPill)
                            && game.getGhostLastMoveMade(g).equals(game.getNextMoveTowardsTarget(
                                    ghost, mspacman, game.getGhostLastMoveMade(g), DM.PATH))) // Hay
                	   finalGoodMoves.remove(m);                                               // Ppill
                	                                                                          // en
                    																		// path que
                    																		// queremos usar,
                    																		// Ghost mas cerca
                    
                        
                      
                    else if (distanceMsPacManToGhost < distanceMsPacManToJunction) // MsPacMan no
                                                                                     // llega a la
                    	finalGoodMoves.remove(m);                                     // junction
                    
                        
                        
                    


                } else if (isChasingGhost(game, g) && !map.get(m).getPath().contains(ghost)) // Ghost
                                                                                             // no
                                                                                             // esta
                                                                                             // en
                                                                                             // el
                                                                                             // path
                    if (map.get(m).hasPP() == -1
                            && distanceGhostToJunction <= distanceMsPacManToJunction) // No hay pp
                    	finalGoodMoves.remove(m);                                      // en el
                                                                                      // camino y el
                                                                                      // Ghost llega
                                                                                      // mas rapido
                                                                                      // o al mismo
                                                                                      // tiempo al
                                                                                      // junction
                    
                        
                        
                    
                    else  if (ghost == currentJunction) // Ghost en junction, no vale la pena ir
                        finalGoodMoves.remove(m);
                        
                    
                    

            }

            //Mismo procedimiento anterior pero en los segundos junctions, solo se ve si al menos hay un camino posible:

            MOVE finalMoveToJunction = map.get(m).getLastMove();
            int junction = map.get(m).getCurrentJ();
            int distanceMsPacManToFirstJunction =
                    game.getShortestPathDistance(game.getNeighbour(mspacman, m), junction, m) + 1;
            boolean EverythingGoodInSecondJunctions = false;


            Map<MOVE, JunctionInformation> map2 = new HashMap<>();

            ArrayList<MOVE> nextPossibleMoves = new ArrayList<MOVE>();
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

                    if ((isChasingGhost(game, g)
                            && (distanceGhostToJunction > distanceMsPacManToJunction)
                            && game.getGhostLastMoveMade(g)
                                    .equals(game.getNextMoveTowardsTarget(ghost, junction,
                                            game.getGhostLastMoveMade(g), DM.PATH))
                            || !isChasingGhost(game, g))) // al menos
                    {
                        EverythingGoodInSecondJunctions = true;

                    }


                }
            }

            if (!EverythingGoodInSecondJunctions) // Encontre un path por donde puedo ir
                finalGoodMoves.remove(m);
        }

        return finalGoodMoves;
    }



    private static int pathWithPowerPill(Game game, List<Integer> path) {

        for (Integer indexPP : game.getActivePowerPillsIndices())
            if (path.contains(indexPP))
                return indexPP;
        return -1;
    }


    //Buscar mejor movimiento que me acerca mas a ls fantasmas comestibles
    public static void getBestMovesTowardsGhost(Game game, Map<MOVE, Double> finalMoves) {

    	int mspacman = game.getPacmanCurrentNodeIndex();
		
		MOVE[] m = game.getPossibleMoves(mspacman);
		for(MOVE nextMove: m) {    						 //recorremos los posibles movimientos del mspacman
			int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
			double d=-1;
			for(Constants.GHOST g: Constants.GHOST.values()) {      //buscamos las distancias desde el mspacman hacia los ghosts
				int ghost = game.getGhostCurrentNodeIndex(g);
				if(game.isGhostEdible(g)) {
					if(d==-1)
						d=0;
					d += game.getShortestPathDistance(newNodeMsPacMan, ghost);
				}
				
				
			}
			if(d!=-1)
				finalMoves.put(nextMove, d);
		}
		
    }

    // metodo que nos devuelve la pill mas cercana sin acercanos a un Chasing Ghost
    public static int getNearestPillWithoutChasingGhost(Game game, List<MOVE> goodMoves,
            int lPill) {
        double distance = Integer.MAX_VALUE;
        int mspacman = game.getPacmanCurrentNodeIndex();
        int finalPill = -1;

        for (int pillIndex : game.getActivePillsIndices()) {
            double d = game.getDistance(mspacman, pillIndex, DM.PATH);

            if (distance > d && d > 0
                    && goodMoves
                            .contains(game.getNextMoveTowardsTarget(mspacman, pillIndex, DM.PATH))
                    && d <= lPill) {
                distance = d;
                finalPill = pillIndex;
            }

        }
        return finalPill;
    }

    // metodo que nos devuelve la pill mas cercana sin acercanos a un Chasing Ghost
    public static int getNearestPowerPillWithoutChasingGhost(Game game, List<MOVE> goodMoves) {
        double distance = Integer.MAX_VALUE;
        int mspacman = game.getPacmanCurrentNodeIndex();
        int finalPill = -1;

        for (int pPillIndex : game.getActivePowerPillsIndices()) {
            double d = game.getDistance(mspacman, pPillIndex, DM.PATH);

            if (distance > d && d > 0
                    && goodMoves
                            .contains(game.getNextMoveTowardsTarget(mspacman, pPillIndex, DM.PATH))
                    && d <= limitPill) {
                distance = d;
                finalPill = pPillIndex;
            }

        }

        return finalPill;
    }


    @Deprecated
    public static MOVE opossiteMove(Game game, int maspacman) {
        if (game.getPacmanLastMoveMade().equals(MOVE.UP))
            return MOVE.DOWN;
        else if (game.getPacmanLastMoveMade().equals(MOVE.DOWN))
            return MOVE.UP;
        else if (game.getPacmanLastMoveMade().equals(MOVE.RIGHT))
            return MOVE.LEFT;
        else
            return MOVE.RIGHT;
    }

    public static MOVE oppositeMove(MOVE move) {
        switch (move) {
            case UP:
                return MOVE.DOWN;
            case DOWN:
                return MOVE.UP;
            case LEFT:
                return MOVE.RIGHT;
            case RIGHT:
                return MOVE.LEFT;
            default:
                return MOVE.NEUTRAL;
        }
    }

    private int numberOfGhostsInPath(Game game, int[] path) {
        int count = 0;
        for (GHOST ghost : GHOST.values())
            for (int index : path)
                if (index == game.getGhostCurrentNodeIndex(ghost))
                    count++;

        return count;
    }

    //Estamos cerca de la carcel y existe peligro
    public static boolean nearDangerousCage(Game game) {

        int mspacman = game.getPacmanCurrentNodeIndex();
        for (Constants.GHOST g : Constants.GHOST.values()) { // miramos entre todos los ghosts
            if (game.getGhostLairTime(g) != 0
                    && game.getGhostLairTime(g) <= game.getShortestPathDistance(mspacman, cage)
                    && game.getShortestPathDistance(mspacman, cage) <= 55)
                return true;
        }
        return false;
    }


    private static void getNearestJunction(Game game, Map<MOVE, JunctionInformation> map,
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

        int ppill = pathWithPowerPill(game, path);
        map.put(move, new JunctionInformation(j, new ArrayList<>(), currentPos, prevMove, move,
                ppill, path));

    }

    public static List<MOVE> getMovesAwayFromEachTarget(Game game, int ghostIndex,
            MOVE lastMoveMade,
            List<Integer> listChasingGhosts) {
        return listChasingGhosts.stream()
                .map(i -> game.getNextMoveAwayFromTarget(ghostIndex, i, lastMoveMade, DM.PATH))
                .collect(Collectors.toList());
    }

    public static Optional<MOVE> getMostRepeatedMove(EnumMultiset movesAwayFromTargetsMultiSet) {
        return movesAwayFromTargetsMultiSet.stream()
                .max((a, b) -> movesAwayFromTargetsMultiSet.count(a)
                        - movesAwayFromTargetsMultiSet.count(b));
    }

    public static int getDestinationJunctionForMsPacman(Game game) {
        int pacmanNode = game.getPacmanCurrentNodeIndex();
        if (game.isJunction(pacmanNode))
            return pacmanNode;

        // buscamos el cruce más cercano a mspacman teniendo en cuenta su último movimiento
        int closestJunction = -1;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int junction : game.getJunctionIndices()) {
            double distance =
                    game.getDistance(pacmanNode, junction, game.getPacmanLastMoveMade(), DM.PATH);
            if (distance < minDistance) {
                closestJunction = junction;
                minDistance = distance;
            }
        }
        return closestJunction;
    }

    //Mejor movimiento que me aleja mas de los ghost
    public static void getBestRunAwayGhost(Game game, double limit,
            Map<MOVE, Double> mapBestMovesAway) {

        int mspacman = game.getPacmanCurrentNodeIndex();
        MOVE[] m = game.getPossibleMoves(mspacman, game.getPacmanLastMoveMade());
      

        for (MOVE nextMove : m) {
            int newNodeMsPacMan = game.getNeighbour(mspacman, nextMove);
            double d = 0;
            for (Constants.GHOST g : Constants.GHOST.values()) {
                int ghost = game.getGhostCurrentNodeIndex(g);
                if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0
                        && game.getShortestPathDistance(newNodeMsPacMan, ghost) <= limit)
                    d += game.getDistance(newNodeMsPacMan, ghost, DM.PATH);

            }

            mapBestMovesAway.put(nextMove, d);
            /*
             * if(distance < d && d>0 ) { distance =d; finalMove = nextMove; }
             */

        }

    }

    public boolean isMsPacmanInPath(Game game, int[] path) {
        for (int i : path)
            if (i == game.getPacmanCurrentNodeIndex())
                return true;
        return false;
    }
}
