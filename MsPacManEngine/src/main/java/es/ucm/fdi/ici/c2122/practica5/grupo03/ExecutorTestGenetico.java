package es.ucm.fdi.ici.c2122.practica5.grupo03;

import java.util.Vector;

import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.util.Stats;

public class ExecutorTestGenetico {

    public static void main(String args[]) {
        Vector<Double> d = new Vector<Double>();
        for(int i = 0; i < 12; i++) {
        	d.add((double)1/12);
        }
        for(int i = 12; i < 24; i++) {
        	d.add((double)1/12);
        }
        
    	new ExecutorTestGenetico().ejecutar(100, d);
        
    }
    
    public double ejecutar(int sv, Vector<Double> args) {
    	
    	Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(false)
                .setPacmanPO(false)
                .setScaleFactor(3.0)
                .setTimeLimit(150)
                .build();
        
        Vector<Double> coefsPacMan = new Vector<Double>();
        Vector<Double> coefsGhosts = new Vector<Double>();
        
        int numcoefs = args.size()/2;
        int safevalue = sv;
        
        for(int i = 0; i < numcoefs; i++) {
        	coefsPacMan.add(args.get(i));        	
        }
        
        for(int i = numcoefs; i < args.size(); i++) {
        	coefsGhosts.add(args.get(i));        	
        }
        
        PacmanController pacMan = new MsPacManGenetico(coefsPacMan, safevalue);
        GhostController ghosts = new GhostsGenetico(coefsGhosts, safevalue);
        
        return executor.runGameTimedSpeedOptimised(pacMan, ghosts,false,"CBR test").getAverage();
		
    	
    }
    
}