package es.ucm.fdi.ici.c2122.practica4.grupo05;


import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.util.Stats;


public class ExecutorTest {

	public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(true)
                .setPacmanPO(true)
                // .setPacmanPOvisual(true)
                // .setGhostsPOvisual(true)
                .setVisual(true)
                .setScaleFactor(2.0)
                .build();

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();
        
       // System.out.println(executor.runGame(pacMan, ghosts, 5));
        Stats[] s = executor.runExperiment(pacMan, ghosts, 30, "Game");System.out.println(s[0]);
    }
}
