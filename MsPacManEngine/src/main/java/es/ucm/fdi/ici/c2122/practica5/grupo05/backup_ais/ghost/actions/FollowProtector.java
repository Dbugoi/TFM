package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.actions;

import java.util.Optional;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FollowProtector implements RulesAction {
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
        try {
            GHOST toFollow = findGhostToFollowSafely(game).orElseThrow(RuntimeException::new);
            return Moves.ghostTowardsGhost(game, ghost, toFollow);
        } catch (Exception e) {
            return Moves.getRandomMove();
        }
    }

    private Optional<GHOST> findGhostToFollowSafely(Game game) {
        for (GHOST otherGhost : GHOST.values())
            if (otherGhost != ghost
                    && game.isGhostEdible(ghost)
                    && game.getGhostLairTime(ghost) <= 0
                    && game.getGhostLairTime(otherGhost) <= 0
                    && !game.isGhostEdible(otherGhost)) {

                boolean otherGhostCloserToThisGhostThanPacmanToThisGhost =
                        PathDistance.fromGhostToGhost(game, otherGhost, ghost) < PathDistance
                                .fromPacmanToGhost(game, ghost);

                boolean pacmanCloserToThisGhostThanPacmanToOtherGhost =
                        PathDistance.fromPacmanToGhost(game, ghost) < PathDistance
                                .fromPacmanToGhost(game, otherGhost);

                boolean otherGhostCloserToThisGhostThanOtherGhostToPacman =
                        PathDistance.fromGhostToGhost(game, otherGhost, ghost) < PathDistance
                                .fromGhostToPacman(game, otherGhost);

                if (otherGhostCloserToThisGhostThanPacmanToThisGhost
                        && pacmanCloserToThisGhostThanPacmanToOtherGhost
                        && otherGhostCloserToThisGhostThanOtherGhostToPacman)
                    return Optional.of(otherGhost);
            }
        return Optional.empty();
    }

    @Override
    public void parseFact(Fact actionFact) {
        // TODO Auto-generated method stub

    }

}
