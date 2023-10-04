package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import pacman.game.Constants.MOVE;

public class AdaptorMoveIntMap extends AdaptorEnumMap<MOVE,Integer> {

    public AdaptorMoveIntMap() {
        super(MOVE.class, Integer::parseInt);
    }
    
}
