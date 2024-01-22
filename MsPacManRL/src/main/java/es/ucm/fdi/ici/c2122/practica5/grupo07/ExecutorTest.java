package es.ucm.fdi.ici.c2122.practica5.grupo07;


import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
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

       // PacmanController pacMan = new MsPacMan();        
        PacmanController pacMan = new es.ucm.fdi.ici.c2122.practica5.grupo07.MsPacMan();

        //GhostController ghosts = new AggressiveGhosts();
        GhostController ghosts = new es.ucm.fdi.ici.c2122.practica5.grupo07.Ghosts();
        
       
        //executor.runGame(pacMan, ghosts, 20);
        executor.runGameTimedSpeedOptimised(pacMan, ghosts,!true,"CBR test");
        System.out.println(executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0].getAverage());
        System.out.println(executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0].getAverage());
        System.out.println(executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0].getAverage());
        System.out.println(executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0].getAverage());
        System.out.println(executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0].getAverage());
        System.out.println(executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName())[0].getAverage());

        //Time benchmark
		//long time = System.currentTimeMillis();
        //executor.runExperiment(pacMan, ghosts, 100, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName());
		//System.out.println(System.currentTimeMillis()-time);

        
    }
}
