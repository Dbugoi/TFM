package es.ucm.fdi.ici.c2122.practica5.grupo06;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;


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

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();

        executor.runGameTimedSpeedOptimised(pacMan, ghosts,true,"CBR test");
        
        //Time benchmark
		//long time = System.currentTimeMillis();
        //executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName());
		//System.out.println(System.currentTimeMillis()-time);
    }
}
