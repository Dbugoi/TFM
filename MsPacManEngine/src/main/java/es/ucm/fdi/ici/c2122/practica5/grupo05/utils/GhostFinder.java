package es.ucm.fdi.ici.c2122.practica5.grupo05.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import es.ucm.fdi.ici.c2122.practica3.grupo05.NearestEdibleGhostInformation;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

/**
 * Clase para buscar fantasmas que cumplan ciertas condiciones.
 */
public class GhostFinder {

    private GhostFinder() {}
    public static final int cage = 492;
    /**
     * Busca el fantasma más cercano de todos.
     * 
     * @param game
     * @return
     */
    public static GHOST getClosest(Game game) {
        ToIntFunction<GHOST> distToMsPacman =
                (GHOST g) -> (int) PathDistance.fromGhostToPacman(game, g);
        Optional<GHOST> optGhost = Arrays.stream(GHOST.values())
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));

        if (!optGhost.isPresent())
            throw new IllegalStateException("There are no ghosts in game");

        return optGhost.get();
    }

    /**
     * Busca el fantasma más cercano que sigue el filtro dado por el predicado 'ghostFilter'.
     * 
     * @param game
     * @param distanceMeasure
     * @param ghostFilter
     * @return
     */
    public static Optional<GHOST> findClosestThatFollowsFilter(Game game, DM distanceMeasure,
            Predicate<GHOST> ghostFilter) {
        ToIntFunction<GHOST> distToMsPacman =
                gh -> (int) Distance.fromGhostToPacman(game, gh, distanceMeasure);
        return Arrays.stream(GHOST.values())
                .filter(ghostFilter::test)
                .min((g1, g2) -> distToMsPacman.applyAsInt(g1) - distToMsPacman.applyAsInt(g2));
    }

    /**
     * Busca el fantasma más cercano a distancia menor que 'limit' que no sea comestible y esté
     * fuera de la cárcel.
     * 
     * @param game
     * @param distanceMeasure
     * @param limit
     * @return
     */
    public static Optional<GHOST> findClosestChasing(Game game, DM distanceMeasure, int limit) {
        return findClosestThatFollowsFilter(game, distanceMeasure,
                gh -> CheckIfGhost.isChasing(game, gh)
                        && Distance.fromGhostToPacman(game, gh, distanceMeasure) < limit);
    }

    /**
     * Busca el fantasma comestible más cercano
     * 
     * @param game
     * @param distanceMeasure
     * @return
     */
    public static Optional<GHOST> findClosestEdible(Game game, DM distanceMeasure) {
        return findClosestThatFollowsFilter(game, distanceMeasure, game::isGhostEdible);
    }

    /**
     * Busca los índices de todos los fantasmas comibles dentro de un rango especificado por
     * {@link AIParameters#LIMIT_FOR_EDIBLE_GHOST}.
     * 
     * @param game
     * @return
     */
    public static List<GHOST> getNearestEdibleGhosts(Game game) {
        return getNearestEdibleGhosts(game, AIParameters.LIMIT_FOR_EDIBLE_GHOST).getListNearestEdibleGhost();
    }

    /**
     * Busca los índices de todos los fantasmas comibles dentro de un rango especificado por
     * 'limit'.
     * 
     * @param game
     * @param limit
     * @return
     */
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
                    && d*1.5<=game.getGhostEdibleTime(g)) {
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

    /**
     * Busca los índices de todos los fantasmas no comestibles que estén a una distancia menor a la
     * especificada por 'limit', pues se consideran peligrosos.
     * 
     * @param game
     * @param limit
     * @return
     */
    public static List<Integer> getNearestChasingGhosts(Game game, double limit) {
    	ArrayList<Integer> listIndex = new ArrayList<Integer>();
    	int mspacman = game.getPacmanCurrentNodeIndex();
        for (GHOST g : GHOST.values()) {
            // GHOST ME PERSIGUE
            if (CheckIfGhost.isChasing(game, g)
                    && PathDistance.fromPacmanToGhost(game, g) <= limit
                    && game.getGhostLastMoveMade(g)
                            .equals(Moves.ghostTowardsPacman(game, g))) {
                listIndex.add( game.getGhostCurrentNodeIndex(g));
            }
            if ((game.getGhostLairTime(g) != 0 && !listIndex.contains(cage)
                    && game.getGhostLairTime(g) < game.getShortestPathDistance(mspacman, cage)
                    && game.getShortestPathDistance(mspacman, cage) <= limit)|| CheckIfPacman.isNearDangerousCage(game))
                listIndex.add(cage);
        }
        return listIndex;
    }
    public static int getMinDistanceChasingGhosts(Game game) {
		double distance = Integer.MAX_VALUE;
		int mspacman = game.getPacmanCurrentNodeIndex();
		for(Constants.GHOST g: Constants.GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			
		if( CheckIfGhost.isChasing(game, g)){
			double d = game.getShortestPathDistance(mspacman, ghost, game.getPacmanLastMoveMade());
			if(distance > d && d>0)	
				distance =d;
			
		}
		 if ((game.getGhostLairTime(g) != 0 
                 && game.getGhostLairTime(g) < game.getShortestPathDistance(mspacman, cage)
                 && game.getShortestPathDistance(mspacman, cage) <= 65)|| CheckIfPacman.isNearDangerousCage(game))
             distance = game.getShortestPathDistance(mspacman, cage);
		}
		return (int) distance;
    }
    


    /**
     * Busca todos los fantasmas que podría llegar a comerse MsPacman.
     * 
     * @param game
     * @param limit
     * @return
     */
    public static NearestEdibleGhostInformation getNearestEdibleGhostsInfo(Game game, int limit) {
        double minD = Double.MAX_VALUE;
        double minEdibleTime = Double.MAX_VALUE;
        List<GHOST> listGhost = new ArrayList<>();

        for (GHOST ghost : GHOST.values()) {
            if (game.getGhostLairTime(ghost) == 0
                    && game.isGhostEdible(ghost)
                    && PathDistance.fromPacmanToGhost(game, ghost) <= limit
                    && PathDistance.fromPacmanToGhost(game, ghost) * 2 <= game
                            .getGhostEdibleTime(ghost)) {

                double dist = PathDistance.fromPacmanToGhost(game, ghost);

                listGhost.add(ghost);
                if (minD > dist) {
                    minD = dist;
                    minEdibleTime = game.getGhostEdibleTime(ghost);
                }
            }
        }
        updateEdibleGhostsList(game, listGhost);

        return new NearestEdibleGhostInformation(listGhost, minD, minEdibleTime);
    }

    private static void updateEdibleGhostsList(Game game, List<GHOST> listGhost) {
        for (GHOST edible : new ArrayList<>(listGhost)) // Erase if chasingGhost in path
            for (GHOST chasing : GHOST.values()) {
                if (CheckIfGhost.isChasing(game, chasing) && !chasing.equals(edible)) {
                    double chasingToEdibleDist =
                            PathDistance.fromGhostToGhost(game, chasing, edible);
                    double pacmanToEdibleDist =
                            PathDistance.fromPacmanToGhost(game, edible);

                    if (chasingToEdibleDist > pacmanToEdibleDist)
                        listGhost.remove(edible);
                }
            }
    }
}
