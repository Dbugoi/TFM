package es.ucm.fdi.ici.c2122.practica5.grupo03;

import java.util.EnumMap;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManagerGeneric;
	GhostsStorageManager storageManagerSpecific;
	
	static final double[] vecG = {0.04152580592295724, 0.058725211763981953, 0.06208117903465202, 0.07364049442089657, 0.07460189735587602, 0.0748845689125447, 0.08091778606630048, 0.08539465017169023, 0.08741845330918988, 0.11609456478943188, 0.11878431348605785, 0.12593107476642112 };
		
	public Ghosts()
	{	
		Vector<Double> pesos = new Vector<Double>(12);
        
        for(Double d: vecG) {
        	pesos.add(d);
        }
		
		this.storageManagerGeneric = new GhostsStorageManager(100);
		this.storageManagerSpecific = new GhostsStorageManager(100);
		cbrEngine = new GhostsCBRengine(storageManagerGeneric, storageManagerSpecific, pesos);
		
		this.setName("Ghosts 03");
	}
	
	@Override
	public void preCompute(String opponent) {
		cbrEngine.setOpponent(opponent); // bases de casos especializadas 
		try {
			cbrEngine.configure();
			cbrEngine.preCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void postCompute() {
		try {
			cbrEngine.postCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		//This implementation only computes a new action when Ghosts is in a junction. 
		//This is relevant for the case storage policy
		
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		for(GHOST g: GHOST.values()) {
			if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
				moves.put(g, MOVE.NEUTRAL);
			
			try {
				GhostsInput input = new GhostsInput(game);
				input.parseInput();
				storageManagerGeneric.setGame(game);
				storageManagerSpecific.setGame(game);
				cbrEngine.cycle(input.getQuery());
				MOVE move = cbrEngine.getSolution();
				moves.put(g, move);
				continue;
			} catch (Exception e) {
				e.printStackTrace();
			}
			moves.put(g, MOVE.NEUTRAL);
		}
		
		return moves;
		
	}

}
