package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.CheckIfGhost;
import pacman.game.Game;

public class GhostCaseJudge {
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public int reviseCase(GhostsDescription description) {
        double score = 0;
        boolean couldBeEaten = CheckIfGhost.couldBeEaten(game, description.getGhost());
        score += 4.5 * scoreForActualScore(description);
        score += 2.0 * scoreForEatingPacman();
        score += 1.5 * scoreForDying(description);
        score += 1.0 * scoreForChasingPacman(description, couldBeEaten);
        score += 1.0 * scoreForRunningAway(description, couldBeEaten);
        score *= 1000;
        return (int) score;
    }

    private double scoreForActualScore(GhostsDescription desc) {
        double score = (game.getScore() - desc.getScore());
        score /= 500;
        return -score;
    }

    private double scoreForEatingPacman() {
        return game.wasPacManEaten() ? 1 : 0;
    }

    private double scoreForDying(GhostsDescription desc) {
        return game.wasGhostEaten(desc.getGhost()) ? -1 : 0;
    }

    private double scoreForChasingPacman(GhostsDescription desc, boolean couldBeEaten) {
        if (!couldBeEaten) {
            int oldDist = desc.getDistToPacman().values()
                    .stream()
                    .min(Integer::compare)
                    .orElseThrow(RuntimeException::new);
            // cuanto más nos hayamos acercado a pacman, mejor
            double score = (double) oldDist - PathDistance.fromGhostToPacman(game, desc.getGhost());
            return score / GhostsCBRengine.MAX_DISTANCE;
        }
        return 0;
    }

    private double scoreForRunningAway(GhostsDescription desc, boolean couldBeEaten) {
        if (couldBeEaten) {
            int oldDist = desc.getDistPacmanToGhost();
            // cuanto más nos hayamos alejado de pacman, mejor
            double score = (double) PathDistance.fromPacmanToGhost(game, desc.getGhost()) - oldDist;
            return score / GhostsCBRengine.MAX_DISTANCE;
        }
        return 0;
    }
}
