package es.ucm.fdi.ici.c2122.practica5.grupo04;


import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setTimeLimit(200)
                .setGhostPO(false)
                .setPacmanPO(false)
                .setVisual(true)
                .setScaleFactor(3.0)
                .build();

        //PacmanController pacMan = new HumanController(new KeyBoardInput());

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();
        //GhostController ghosts = new GhostsRandom();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 40)
        );
        
    }
}
