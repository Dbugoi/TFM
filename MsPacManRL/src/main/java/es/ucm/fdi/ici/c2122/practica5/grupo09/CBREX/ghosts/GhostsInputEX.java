package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInputEX extends CBRInput {

	public GhostsInputEX(Game game) {
		super(game);
	}

	Integer id;
	
	Integer level;
	Integer score;
	Integer time;
	Integer lives;
	
	Integer msPacmanPos;
	Integer msPacmanMove;
	
	Integer ghostDistance = -1;
	
	Boolean wasEaten = false;
	Boolean isEdible = false;
	
	GHOST ghost;
	
	@Override
	public void parseInput() {
		score = game.getScore();
		level = game.getCurrentLevel();
		time = game.getTotalTime();
		lives = game.getPacmanNumberOfLivesRemaining();

		msPacmanPos = game.getPacmanCurrentNodeIndex();
		msPacmanMove = game.getPacmanLastMoveMade().ordinal();
		
		//Al construir GhostsInput, se llama a parseInput antes de poder
		//siquiera establecer un fantasma, asi que esto da error, con lo que hay que proteger esta
		//parte de esa primera llamada.
		if(ghost != null) {
			ghostDistance = (int) game.getDistance(game.getGhostCurrentNodeIndex(ghost), msPacmanPos, 
											   game.getGhostLastMoveMade(ghost), DM.PATH);
		
			wasEaten = game.wasGhostEaten(ghost);
			isEdible = game.isGhostEdible(ghost);
		}
	}

	@Override
	public CBRQuery getQuery() {
		GhostsDescriptionEX description = new GhostsDescriptionEX();
		description.setLevel(level);
		description.setScore(score);
		description.setTime(time);
		description.setLives(lives);
		
		description.setMsPacmanPos(msPacmanPos);
		description.setMsPacmanMove(msPacmanMove);
		
		description.setGhostDistance(ghostDistance);
		
		description.setIsEdible(isEdible);
		description.setWasEaten(wasEaten);
		
		description.setGhostType(ghost);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	public void setGhost(GHOST g) {
		ghost = g;
	}
}
