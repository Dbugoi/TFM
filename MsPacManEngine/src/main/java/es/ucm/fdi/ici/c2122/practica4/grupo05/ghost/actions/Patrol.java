package es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Paths;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Patrol implements Action {
    private static final Random rnd = new Random();
    private final GHOST ghost;

    public Patrol(GHOST g) {
        this.ghost = g;
    }

    @Override
    public String getActionId() {
        return "Patrol";
    }

    @Override
    public MOVE execute(Game game) {
        List<int[]> paths = Paths
                .getPathsToClosestJunctions(game, game.getGhostCurrentNodeIndex(ghost),
                        game.getGhostLastMoveMade(ghost))
                .stream()
                .filter(path -> path.length > 0)
                .filter(path -> !Paths.isAnyGhostInPath(game, path))
                .collect(Collectors.toList());

        if (paths.isEmpty())
            return Moves.getRandomMove();
        else {
            int randomPath = rnd.nextInt(paths.size());
            return Moves.ghostTowards(game, ghost, paths.get(randomPath)[0]);
        }
    }

}
