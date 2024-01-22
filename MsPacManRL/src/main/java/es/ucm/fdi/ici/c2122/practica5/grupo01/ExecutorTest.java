package es.ucm.fdi.ici.c2122.practica5.grupo01;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.util.Stats;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(false)
                .setPacmanPO(false)
                .setVisual(true)
                .setScaleFactor(3.0)
                .setTimeLimit(200)
                .build();


        GhostController ghosts = new Ghosts();
        PacmanController pacMan = new MsPacMan();

        //executor.runGame(pacMan, ghosts, 0);
        
        //executor.runGameTimedSpeedOptimised(pacMan, ghosts,true,"CBR test");
        
        //Time benchmark
        long time = System.currentTimeMillis();
        Stats scores = executor.runExperiment(pacMan, ghosts,10, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0];
        
		System.out.println(scores.getAverage());
		System.out.println(System.currentTimeMillis()-time);
		

        
    }
}
