package es.ucm.fdi.ici.c2122.practica5.grupo08;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManager;		
	GhostsStorageManager generalStorageManager;	//Solo necesario para la inicializaci�n de la base de casos general
	
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	/*Constructora de la clase. Inicializa el motor de CBR*/	
	public Ghosts()
	{	
		setName("Ghosts 08");
		this.storageManager = new GhostsStorageManager();
		this.generalStorageManager = new GhostsStorageManager();
		cbrEngine = new GhostsCBRengine(storageManager, generalStorageManager);
	}
	
	@Override
	/*Configura el motor de CBR*/	
	public void preCompute(String opponent) {
		cbrEngine.setOpponent(opponent);
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
	/*En caso de que alg�n fantasma necesite de un movimiento, realiza un ciclo de
	 * CBR y asigna los movimientos para cada uno de los fantasmas*/
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		moves.clear();
		try {
			//Recogemos la informaci�n y la mandamos al storeManager
			GhostsInput input = new GhostsInput(game);
			input.parseInput();
			storageManager.setGame(game);
			//Inicializamos los movimientos
			MOVE[] move = {MOVE.NEUTRAL, MOVE.NEUTRAL,MOVE.NEUTRAL,MOVE.NEUTRAL};
			//Para cada ghost si requiere de movimiento se computa el cbr
			for(GHOST ghost: GHOST.values()){			
				if(game.doesGhostRequireAction(ghost)){
					cbrEngine.cycle(input.getQuery());
					move = cbrEngine.getSolution();
					break;
				}
			}
			//Asigamos a la variable de clase
			for(GHOST ghost: GHOST.values()){			
				this.moves.put(ghost, move[ghost.ordinal()]);
			}
			
			return this.moves;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//En caso de dar alg�n error asignamos a los ghost neutral.
		moves.clear();
		for(GHOST ghost: GHOST.values()){
			this.moves.put(ghost, MOVE.NEUTRAL);
		}
		return this.moves;
	}

}
