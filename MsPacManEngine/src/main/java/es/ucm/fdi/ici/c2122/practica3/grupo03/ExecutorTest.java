package es.ucm.fdi.ici.c2122.practica3.grupo03;

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
                .build();

        PacmanController pacMan = new es.ucm.fdi.ici.c2122.practica3.grupo03.MsPacMan();
        GhostController ghosts = new es.ucm.fdi.ici.c2122.practica3.grupo03.Ghosts();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 20)
        );
        
    }
}