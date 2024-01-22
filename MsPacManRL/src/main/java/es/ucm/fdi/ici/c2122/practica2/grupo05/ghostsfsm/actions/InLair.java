package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class InLair implements Action{

    @Override
    public String getActionId() {
        return "In lair";
    }

    @Override
    public MOVE execute(Game game) {
        return MOVE.NEUTRAL;
    }
    
}
