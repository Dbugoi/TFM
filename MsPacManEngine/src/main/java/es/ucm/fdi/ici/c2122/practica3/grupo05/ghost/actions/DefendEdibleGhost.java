package es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions;

import java.util.Optional;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.CheckIfGhost;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.Moves;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DefendEdibleGhost implements RulesAction {
    private final GHOST ghost;

    public DefendEdibleGhost(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Try to defend edible ghost";
    }

    @Override
    public MOVE execute(Game game) {
        Optional<GHOST> closestEdibleGhostToPacman = 
            //GhostFinder.findClosestEdible(game, DM.PATH);
            GhostFinder.findClosestThatFollowsFilter(game, DM.PATH, g -> CheckIfGhost.couldBeEaten(game, g));

        if (!closestEdibleGhostToPacman.isPresent())
            return Moves.ghostTowardsPacman(game, ghost);
        else
            return Moves.ghostTowardsGhost(game, ghost, closestEdibleGhostToPacman.get());
    }

    @Override
    public void parseFact(Fact actionFact) {
        // TODO Auto-generated method stub
    }

}
