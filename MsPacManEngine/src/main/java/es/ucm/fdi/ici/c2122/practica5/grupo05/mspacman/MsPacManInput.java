package es.ucm.fdi.ici.c2122.practica5.grupo05.mspacman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica2.grupo05.JunctionInformation;
import es.ucm.fdi.ici.c2122.practica5.grupo05.ListaEnteros;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}
	
	private List<Integer> lDistPill;
	private List<Integer> lDistPowerPill;
	private List<Integer> lEdibleGhost;
	private List<Integer> lChasingGhost;
	private List<Integer> lTimeEdibleGhost;
	private List<Integer> lLastMoveGhost;
	private List<Integer> lDistJunction;
	private List<Integer> lDistJunction_Chasing;
	private Set <GHOST> setGhost_chasing;//movimiento realcionado a ese fantasma
	private MOVE lastMove;
	//String currentMove;

	private Integer score;
	private Integer time;
	private Integer level;
	private Integer numEdible;
	private Integer lives;
	
	@Override
	public void parseInput() {
		computeNearestEdibleGhosts(game);
		computeNearestChasingGhosts(game);
		computeNearestPills(game);
		computeNearestPowerPills(game);
		computeNearestJunctions(game);
		
		lastMove = game.getPacmanLastMoveMade();
		level = game.getCurrentLevel();
		time = game.getTotalTime();
		score = game.getScore();
		lives= game.getPacmanNumberOfLivesRemaining();
		//currentMove=game.getPacmanLastMoveMade().toString();// no se si esta bien
	}

	@Override
	public CBRQuery getQuery() {

		MsPacManDescription description = new MsPacManDescription();
		
		/*
		description.setlDistPill(lDistPill.toString());
		description.setlDistPowerPill(lDistPowerPill.toString());
		description.setlEdibleGhost(lEdibleGhost.toString());
		description.setlChasingGhost(lChasingGhost.toString());
		description.setlTimeEdibleGhost(lTimeEdibleGhost.toString());
		description.setlLastMoveGhost(lLastMoveGhost.toString());
		description.setlDistJunction(lDistJunction.toString());
		*/
		description.setDistPillUp(lDistPill.get(MOVE.UP.ordinal()));
		description.setDistPillDown(lDistPill.get(MOVE.DOWN.ordinal()));
		description.setDistPillLeft(lDistPill.get(MOVE.LEFT.ordinal()));
		description.setDistPillRight(lDistPill.get(MOVE.RIGHT.ordinal()));
		
		description.setDistPowerPillUp(lDistPowerPill.get(MOVE.UP.ordinal()));
		description.setDistPowerPillDown(lDistPowerPill.get(MOVE.DOWN.ordinal()));
		description.setDistPowerPillLeft(lDistPowerPill.get(MOVE.LEFT.ordinal()));
		description.setDistPowerPillRight(lDistPowerPill.get(MOVE.RIGHT.ordinal()));
		
		description.setDistEdibleUp(lEdibleGhost.get(MOVE.UP.ordinal()));
		description.setDistEdibleDown(lEdibleGhost.get(MOVE.DOWN.ordinal()));
		description.setDistEdibleLeft(lEdibleGhost.get(MOVE.LEFT.ordinal()));
		description.setDistEdibleRight(lEdibleGhost.get(MOVE.RIGHT.ordinal()));
		
		
		description.setDistChasingUp(lChasingGhost.get(MOVE.UP.ordinal()));
		description.setDistChasingDown(lChasingGhost.get(MOVE.DOWN.ordinal()));
		description.setDistChasingLeft(lChasingGhost.get(MOVE.LEFT.ordinal()));
		description.setDistChasingRight(lChasingGhost.get(MOVE.RIGHT.ordinal()));
		
		description.setTimeEdibleUp(lTimeEdibleGhost.get(MOVE.UP.ordinal()));
		description.setTimeEdibleDown(lTimeEdibleGhost.get(MOVE.DOWN.ordinal()));
		description.setTimeEdibleLeft(lTimeEdibleGhost.get(MOVE.LEFT.ordinal()));
		description.setTimeEdibleRight(lTimeEdibleGhost.get(MOVE.RIGHT.ordinal()));
		
		
		description.setLastMoveGhostUp(lLastMoveGhost.get(GHOST.BLINKY.ordinal()));
		description.setLastMoveGhostDown(lLastMoveGhost.get(GHOST.PINKY.ordinal()));
		description.setLastMoveGhostLeft(lLastMoveGhost.get(GHOST.INKY.ordinal()));
		description.setLastMoveGhostRight(lLastMoveGhost.get(GHOST.SUE.ordinal()));
		
		description.setLastMoveMsPacman(lastMove.ordinal());
		
		description.setDistJunctionUp(lDistJunction.get(MOVE.UP.ordinal()));
		description.setDistJunctionDown(lDistJunction.get(MOVE.DOWN.ordinal()));
		description.setDistJunctionLeft(lDistJunction.get(MOVE.LEFT.ordinal()));
		description.setDistJunctionRight(lDistJunction.get(MOVE.RIGHT.ordinal()));
		
		description.setDistJunction_ChasingUp(lDistJunction_Chasing.get(MOVE.UP.ordinal()));
		description.setDistJunction_ChasingDown(lDistJunction_Chasing.get(MOVE.DOWN.ordinal()));
		description.setDistJunction_ChasingLeft(lDistJunction_Chasing.get(MOVE.LEFT.ordinal()));
		description.setDistJunction_ChasingRight(lDistJunction_Chasing.get(MOVE.RIGHT.ordinal()));
		

		description.setScore(score);
		description.setTime(time);
		description.setLevel(level);
		description.setNumEdible(numEdible);
		description.setLives(lives);
		//current move
	
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	

	private void computeNearestEdibleGhosts(Game game) {  
		numEdible=0;
		 lEdibleGhost = new ArrayList<Integer>();
		 lTimeEdibleGhost = new ArrayList<Integer>();
		 lLastMoveGhost = new ArrayList<Integer>();
		for(int i=0; i<4; i++) {
			lEdibleGhost.add(0);
			lTimeEdibleGhost.add(0);
			lLastMoveGhost.add(MOVE.NEUTRAL.ordinal());
		}
		
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			if(pos!=-1 && game.getGhostLairTime(g)==0)
				lLastMoveGhost.add(g.ordinal(), game.getGhostLastMoveMade(g).ordinal());
			if(pos != -1 && game.isGhostEdible(g) && game.getGhostLairTime(g)==0) {
				numEdible++;
				
				for(MOVE m1: game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
					//MOVE m = Moves.pacmanTowards(game, pos);
					double dist = PathDistance.fromXtoX(game, game.getNeighbour(game.getPacmanCurrentNodeIndex(), m1), m1, pos);
					//double dist = PathDistance.fromPacmanTo(game, game.getNeighbour(pos, m1));
					if(lEdibleGhost.get(m1.ordinal())==0)
						lEdibleGhost.set(m1.ordinal(), (int) dist);
					else {
						int d = (int) Math.min(lEdibleGhost.get(m1.ordinal()), dist);
						lEdibleGhost.set(m1.ordinal(), d);
					}
					int time = (int) Math.max(lTimeEdibleGhost.get(m1.ordinal()),game.getGhostEdibleTime(g));
					lTimeEdibleGhost.add(g.ordinal(), time);
				}
			}
			
		}
	}
	
	private void computeNearestChasingGhosts(Game game) { // agregar los que estan apunto de cambiar?
		lChasingGhost = new ArrayList<Integer>();
		setGhost_chasing = new HashSet<>();
		for(int i=0; i<4; i++) {
			lChasingGhost.add(Integer.MAX_VALUE);
			
		}
		
		
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			if(pos != -1 && !game.isGhostEdible(g) && game.getGhostLairTime(g)==0) {
				MOVE m = Moves.pacmanTowards(game, pos);
				setGhost_chasing.add(g); //chasing ghost mas cercanos en cada mov
				for(MOVE m1: game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
					//MOVE m = Moves.pacmanTowards(game, pos);
					double dist = PathDistance.fromXtoX(game, game.getNeighbour(game.getPacmanCurrentNodeIndex(), m1), m1, pos);
					int d = (int) Math.min(lChasingGhost.get(m1.ordinal()), dist);
					lChasingGhost.set(m1.ordinal(), d);
					
					
				}
				
				//int d = (int) Math.min(lChasingGhost.get(m.ordinal()),  PathDistance.fromPacmanTo(game, pos));
				//lChasingGhost.set(m.ordinal(), d);
				
			}
		}
	}
	
	private void computeNearestPills(Game game) {
		lDistPill = new ArrayList<Integer>();
		for(int i=0; i<4; i++)
			lDistPill.add(Integer.MAX_VALUE);
		
		 for (int pillIndex : game.getActivePillsIndices()) {
			 //double dist = PathDistance.fromPacmanTo(game, pillIndex);
			 for(MOVE m :game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
				 double dist = PathDistance.fromXtoX(game, game.getNeighbour(game.getPacmanCurrentNodeIndex(), m), m, pillIndex);
				 int d = (int) Math.min(lDistPill.get(m.ordinal()), dist);
				 lDistPill.set(m.ordinal(), d);
			 }
			 /*
			 MOVE m = Moves.pacmanTowards(game, pillIndex);	
			 int d = (int) Math.min(lDistPill.get(m.ordinal()), dist);
			 lDistPill.set(m.ordinal(), d);
*/
			 
		}
	}
	private void computeNearestPowerPills(Game game) {
		lDistPowerPill = new ArrayList<Integer>();
		for(int i=0; i<4; i++)
			lDistPowerPill.add(Integer.MAX_VALUE);
		
		for (int powerPillIndex : game.getActivePowerPillsIndices()){
			 //double dist = PathDistance.fromPacmanTo(game, powerPillIndex);
			// MOVE m = Moves.pacmanTowards(game, powerPillIndex);	
			// int d = (int) Math.min(lDistPowerPill.get(m.ordinal()), dist);
			 //lDistPowerPill.set(m.ordinal(), d);
			 for(MOVE m1: game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
					//MOVE m = Moves.pacmanTowards(game, pos);
					double dist = PathDistance.fromXtoX(game, game.getNeighbour(game.getPacmanCurrentNodeIndex(), m1), m1, powerPillIndex);
					int d = (int) Math.min(lDistPowerPill.get(m1.ordinal()), dist);
					lDistPowerPill.set(m1.ordinal(), d);
					
					
				}

			 
		}
	}


	private void computeNearestJunctions(Game game) {
		 lDistJunction = new ArrayList<Integer>();
		 lDistJunction_Chasing = new ArrayList<Integer>();
			for(int i=0; i<4; i++) {
				lDistJunction.add(Integer.MAX_VALUE);
				lDistJunction_Chasing.add(200);
			}
	    int mspacman = game.getPacmanCurrentNodeIndex();
		for(MOVE m: game.getPossibleMoves(mspacman)) {
	        int j = game.getNeighbour(mspacman, m);
	        MOVE prevMove = m;
	        while (!game.isJunction(j)) {
	            MOVE nextMove = game.getPossibleMoves(j, prevMove)[0];
	            j = game.getNeighbour(j, nextMove);
	            prevMove = nextMove;
	        }
	        
	        double dist = PathDistance.fromPacmanTo(game, j);
			int d = (int) Math.min(lDistJunction.get(m.ordinal()), dist);
			lDistJunction.set(m.ordinal(), d);
			for(GHOST g: setGhost_chasing) {
				double distChasingJunction = PathDistance.fromGhostTo(game, g, lDistJunction.get(m.ordinal()));	
				lDistJunction_Chasing.set(m.ordinal(), (int) Math.min(lDistJunction_Chasing.get(m.ordinal()), distChasingJunction));
			}
		}
	}
	
	
}
