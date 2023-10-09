package es.ucm.fdi.ici.c2122.practica5.grupo06;


import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsCBRengine;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;



public final class Ghosts extends GhostController {
    private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);

    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
	GhostsCBRengine cbrEngine;
	GhostsStorageManager storageManager;
		
	public Ghosts()
	{		
		this.storageManager = new GhostsStorageManager();
		cbrEngine = new GhostsCBRengine(storageManager);
		setTeam("Grupo 06");
		setName("Ruby Weapon");
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
	
	
	/*@Override
	public MOVE getMove(Game game, long timeDue) {	
		cbrEngine.configureWithGame(game);
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy
		if(!game.isJunction(game.getPacmanCurrentNodeIndex()))
			return MOVE.NEUTRAL;	
		
		try {
			MsPacManInput input = new MsPacManInput(game);
			input.parseInput();
			storageManager.setGame(game);
			cbrEngine.cycle(input.getQuery());
			MOVE move = cbrEngine.getSolution();
			return move;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MOVE.NEUTRAL;
	}*/
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        myMoves.clear();

        /*for (GHOST ghost : GHOST.values())                //for each ghost
        {
            if (game.doesGhostRequireAction(ghost))        //if it requires an action
            {
                    myMoves.put(ghost, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                            game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH));
                    
            }
            
        }
        cbrEngine.configureWithGame(game);*/
		//This implementation only computes a new action when MsPacMan is in a junction. 
		//This is relevant for the case storage policy

		try {
			cbrEngine.setGame(game);
			storageManager.setGame(game);
			GhostsInput input = new GhostsInput(game);
			input.parseInput();
			cbrEngine.cycle(input.getQuery());
			myMoves.putAll(cbrEngine.getSolution());
		} catch (Exception e) {
			e.printStackTrace();
		}

        return myMoves;
    }
}