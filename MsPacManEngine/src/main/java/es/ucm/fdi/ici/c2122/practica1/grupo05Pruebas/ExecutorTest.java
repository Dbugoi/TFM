package es.ucm.fdi.ici.c2122.practica1.grupo05Pruebas;


import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
            .setTickLimit(4000)
            .setVisual(true)
            .setScaleFactor(3.0)
            .build();

        //PacmanController pacMan = /* */;
        //GhostController ghosts = /* */;
        
        PacmanController pacMan = new MsPacMan11();// new MsPacMan(20, DM.PATH);
        GhostController ghosts = new Ghosts9();
        
        System.out.println(executor.runGame(pacMan, ghosts, 50));

        
    }
}
