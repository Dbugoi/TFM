package es.ucm.fdi.ici.c2122.practica5.grupo10;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts.CBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController{
	CBRengine cbrEngine;
	GhostsStorageManager storageManager;

	private GhostsInput[] last_intersection_game_info; // cada fantasma guarda la informacion de la ultima interseccion
	public Ghosts()
	{		
		setName("Ghosts 10");
		this.storageManager = new GhostsStorageManager();
		this.last_intersection_game_info = new GhostsInput[4];
		cbrEngine = new CBRengine(storageManager);
	}

	@Override
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
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> ghostMoves = new EnumMap<GHOST, MOVE>(GHOST.class);

		//This implementation only computes a new action when Ghost is in a junction. 
		//This is relevant for the case storage policy

		
		for (GHOST g : GHOST.values()) {
			MOVE move = MOVE.NEUTRAL;
			boolean ghost_was_eaten=  game.wasGhostEaten(g);
			boolean ghost_is_going_to_eat =  false;
			if(game.getGhostLairTime(g)<=0) {
				int pos_ghost=game.getGhostCurrentNodeIndex(g);
				int pos_mspacman=game.getPacmanCurrentNodeIndex();
				MOVE lastMoveGhost = game.getGhostLastMoveMade(g);
				MOVE lastMovePacman = game.getPacmanLastMoveMade();
				int distance_to_pac =game.getShortestPathDistance(pos_ghost, pos_mspacman, lastMoveGhost);
				int distance_to_ghost = game.getShortestPathDistance(pos_mspacman ,pos_ghost, lastMovePacman);
				ghost_is_going_to_eat =  GhostIsGoingToEat(game,g,distance_to_ghost,distance_to_pac);
			}
			
			try {
				if(game.isJunction(game.getGhostCurrentNodeIndex(g))) {
					GhostsInput input = new GhostsInput(game);
					input.parseInput(g,ghost_was_eaten,ghost_is_going_to_eat);
					storageManager.setGame(game);
					cbrEngine.cycle(input.getQuery());
					move = cbrEngine.getSolution();
					this.last_intersection_game_info[g.ordinal()] = input;
				}
				if((ghost_is_going_to_eat) || (ghost_was_eaten) || game.wasPacManEaten()) {
					GhostsInput input = last_intersection_game_info[g.ordinal()];
					input.parseInput(g,ghost_was_eaten,ghost_is_going_to_eat);
					storageManager.setGame(game);
					cbrEngine.cycle(input.getQuery());
					
				}
			}
			catch (ExecutionException e) {
				e.printStackTrace();
			}

			ghostMoves.put(g,move);

		}



		return ghostMoves;
	}

	private boolean GhostIsGoingToEat(Game game, GHOST g, int distance_to_ghost, int distance_to_pac) {
		if(!game.isGhostEdible(g) &&  distance_to_pac == distance_to_ghost  && distance_to_pac <=3 )
			return true;
		return false;
	}



}
