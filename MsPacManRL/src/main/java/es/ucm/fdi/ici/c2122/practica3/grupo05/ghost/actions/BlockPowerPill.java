package es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.Pills;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BlockPowerPill implements RulesAction {
    private final GHOST ghost;

    public BlockPowerPill(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Defend power pill";
    }

    @Override
    public MOVE execute(Game game) {
        int powerPill = Pills.getClosestPowerPillToMsPacMan(game);
        return Moves.ghostTowards(game, ghost, powerPill);
    }

    @Override
    public void parseFact(Fact actionFact) {
        // TODO Auto-generated method stub
        
    }
    
}
