package es.ucm.fdi.ici.c2122.practica2.grupo05.minimax.testcontrollers;

import es.ucm.fdi.ici.c2122.practica2.grupo05.minimax.algorithms.MinimaxBestMoveForMsPacman;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MiniMaxMsPacman extends PacmanController {

    @Override
    public MOVE getMove(Game game, long timeDue) {
        return MinimaxBestMoveForMsPacman.bestMoveForMsPacman(game);
    }

}
