package es.ucm.fdi.ici.c2122.practica5.grupo08;


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
                .setScaleFactor(2.0)
                .setTimeLimit(200)
                .build();
    	
    	PacmanController pacMan = new MsPacMan();
    	//PacmanController pacMan = new HumanController(new KeyBoardInput());
        GhostController ghosts = new Ghosts();
        //executor.runGame(pacMan, ghosts, 40);
        executor.runGameTimedSpeedOptimised(pacMan, ghosts,true,"CBR test");
        
        /*int media = 0;
        for(int i = 0; i< 500; i++) {
        	int a = executor.runGame(pacMan, ghosts, 0);
        	media = (a + media) / 2;
        }    
        System.out.print("Media: " + media);*/
       
        //Time benchmark
		//long time = System.currentTimeMillis();
        //executor.runExperiment(pacMan, ghosts, 50, pacMan.getClass().getName()+ " - " + ghosts.getClass().getName());
		//System.out.println(System.currentTimeMillis()-time);

        
    }
}
