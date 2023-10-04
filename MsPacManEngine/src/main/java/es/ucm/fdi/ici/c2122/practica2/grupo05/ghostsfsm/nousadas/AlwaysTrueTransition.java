package es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.nousadas;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.fsm.Transition;

public class AlwaysTrueTransition implements Transition {

    @Override
    public boolean evaluate(Input in) {
        return true;
    }

}
