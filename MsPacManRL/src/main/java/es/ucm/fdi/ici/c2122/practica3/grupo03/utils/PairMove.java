package es.ucm.fdi.ici.c2122.practica3.grupo03.utils;

import pacman.game.Constants.MOVE;

public class PairMove<T,Z> extends Pair<T, Z> {
	private MOVE m;
	 
	public PairMove(T t, Z z, MOVE v) {
		super(t,z);
		m = v;
	}
	
	public MOVE getMove() {
		return m;
	}
	
	public void setMove(MOVE m) {
		this.m = m;
	}
}
