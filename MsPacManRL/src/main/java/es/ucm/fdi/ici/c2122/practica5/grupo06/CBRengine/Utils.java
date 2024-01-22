package es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine;

import pacman.game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class Utils {
	public static int getClosestGhostToPacMan(Game game) {
		int c = -1;
		
		int min = Integer.MAX_VALUE;
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			int pos_m = game.getPacmanCurrentNodeIndex();
			int distance = Integer.MAX_VALUE;; 
			if(pos_m != -1 && pos != -1 && game.getGhostLairTime(g) <= 0) 
				distance = (int)game.getDistance(pos, pos_m, DM.PATH);
			if(distance < min) {
				min = distance;
				c = g.ordinal();
			}
		}
		
		return c;
	}
	
	public static int getMiddlestGhostToPacMan(Game game) {
		int m = -1;
		int c = getClosestGhostToPacMan(game);
		
		int min = Integer.MAX_VALUE;
		for(GHOST g: GHOST.values()) {
			if(g.ordinal() == c) continue;
			int pos = game.getGhostCurrentNodeIndex(g);
			int pos_m = game.getPacmanCurrentNodeIndex();
			int distance = Integer.MAX_VALUE;; 
			if(pos_m != -1 && pos != -1 && game.getGhostLairTime(g) <= 0) 
				distance = (int)game.getDistance(pos, pos_m, DM.PATH);
			if(distance < min) {
				min = distance;
				m = g.ordinal();
			}
		}
		
		return m;
	}
	
	public static int getMiddlefarGhostToPacMan(Game game) {
		int m = -1;
		int c = getClosestGhostToPacMan(game);
		int mid = getMiddlestGhostToPacMan(game);
		
		int min = Integer.MAX_VALUE;
		for(GHOST g: GHOST.values()) {
			if(g.ordinal() == c || g.ordinal() == mid) continue;
			int pos = game.getGhostCurrentNodeIndex(g);
			int pos_m = game.getPacmanCurrentNodeIndex();
			int distance = Integer.MAX_VALUE;; 
			if(pos_m != -1 && pos != -1 && game.getGhostLairTime(g) <= 0) 
				distance = (int)game.getDistance(pos, pos_m, DM.PATH);
			if(distance < min) {
				min = distance;
				m = g.ordinal();
			}
		}
		
		return m;
	}
	
	public static int getFarthestGhostToPacMan(Game game) {
		int f = -1;
		
		int min = Integer.MAX_VALUE;
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			int pos_m = game.getPacmanCurrentNodeIndex();
			int distance = Integer.MIN_VALUE; 
			if(pos_m != -1 && pos != -1 && game.getGhostLairTime(g) <= 0) 
				distance = (int)game.getDistance(pos, pos_m, DM.PATH);
			if(distance > min) {
				min = distance;
				f = g.ordinal();
			}
		}
		
		return f;
	}
	
	// La distancia es de fantasma a pacman
	public static GHOST[] getGhostsSortedByDistanceToPacMan(Game game) {
		ArrayList<Integer> distances = new ArrayList<Integer>();
		HashMap<Integer, GHOST> dMap = new HashMap<Integer, GHOST>();
		GHOST[] ghosts = GHOST.values();
		for(int i = 0; i < GHOST.values().length; i++) {
			int pos = game.getGhostCurrentNodeIndex(ghosts[i]);
			int pos_m = game.getPacmanCurrentNodeIndex();
			int distance = Integer.MIN_VALUE; 
			if(pos_m != -1 && pos != -1 && game.getGhostLairTime(ghosts[i]) <= 0) 
				distance = (int)game.getDistance(pos, pos_m, DM.PATH);
			
			distances.add(distance);
			dMap.put(distances.get(i), ghosts[i]);
		}
		
		GHOST[] ret = new GHOST[GHOST.values().length];
		Collections.sort(distances);
		for(int i = 0; i < ghosts.length; i++) {
			ret[i] = dMap.get(distances.get(i));
		}
		
		return ret;
	}
	
	// La distancia es de pacman a fantasmas
	public static GHOST[] getGhostsSortedByDistanceFromPacMan(Game game) {
		ArrayList<Integer> distances = new ArrayList<Integer>();
		HashMap<Integer, GHOST> dMap = new HashMap<Integer, GHOST>();
		GHOST[] ghosts = GHOST.values();
		for(int i = 0; i < GHOST.values().length; i++) {
			int pos = game.getGhostCurrentNodeIndex(ghosts[i]);
			int pos_m = game.getPacmanCurrentNodeIndex();
			int distance = Integer.MIN_VALUE; 
			if(pos_m != -1 && pos != -1 && game.getGhostLairTime(ghosts[i]) <= 0) 
				distance = (int)game.getDistance(pos_m, pos, DM.PATH);
			
			distances.add(distance);
			dMap.put(distances.get(i), ghosts[i]);
		}
		
		GHOST[] ret = new GHOST[GHOST.values().length];
		Collections.sort(distances);
		for(int i = 0; i < ghosts.length; i++) {
			ret[i] = dMap.get(distances.get(i));
		}
		
		return ret;
	}
}
