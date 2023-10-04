package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.huir;

import java.util.Optional;
import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FollowProtector implements Action {
    private final GHOST ghost;

    public FollowProtector(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Follow protector";
    }

    @Override
    public MOVE execute(Game game) {
        GHOST g = findGhostToFollowSafely(game).get();
        return GameUtils.getNextMoveTowardsGhost(game, ghost, g, DM.PATH);
    }

    private Optional<GHOST> findGhostToFollowSafely(Game game) {
        int thisGhostIndex = game.getGhostCurrentNodeIndex(ghost);
        int msPacmanIndex = game.getPacmanCurrentNodeIndex();
        MOVE lastMoveMadeByMsPacman = game.getPacmanLastMoveMade();

        for (GHOST otherGhost : GHOST.values())
            if (otherGhost != ghost
                    && game.isGhostEdible(ghost)
                    && game.getGhostLairTime(ghost) <= 0
                    && game.getGhostLairTime(otherGhost) <= 0
                    && !game.isGhostEdible(otherGhost)) {
                int otherGhostIndex = game.getGhostCurrentNodeIndex(otherGhost);
                MOVE lastMoveMadeByOtherGhost = game.getGhostLastMoveMade(otherGhost);

                boolean otherGhostCloserToThisGhostThanPacmanToThisGhost =
                        game.getDistance(otherGhostIndex, thisGhostIndex, lastMoveMadeByOtherGhost,
                                DM.PATH) < game.getDistance(msPacmanIndex, thisGhostIndex,
                                        lastMoveMadeByMsPacman, DM.PATH);

                boolean pacmanCloserToThisGhostThanPacmanToOtherGhost =
                        game.getDistance(msPacmanIndex, thisGhostIndex, lastMoveMadeByMsPacman,
                                DM.PATH) < game.getDistance(msPacmanIndex, otherGhostIndex,
                                        lastMoveMadeByMsPacman, DM.PATH);

                boolean otherGhostCloserToThisGhostThanOtherGhostToPacman =
                        game.getDistance(otherGhostIndex, thisGhostIndex, lastMoveMadeByOtherGhost,
                                DM.PATH) < game.getDistance(otherGhostIndex, msPacmanIndex,
                                        lastMoveMadeByOtherGhost, DM.PATH);


                if (otherGhostCloserToThisGhostThanPacmanToThisGhost
                        && pacmanCloserToThisGhostThanPacmanToOtherGhost
                        && otherGhostCloserToThisGhostThanOtherGhostToPacman)
                    return Optional.of(otherGhost);
            }
        return Optional.empty();
    }

}
