package es.ucm.fdi.ici.c2122.practica2.grupo05;

import java.util.List;

import pacman.game.Constants.GHOST;

public class NearestEdibleGhostInformation {

	private List<GHOST> list;
	private double minDistance;
	private double minEdibleTime;
	
	public NearestEdibleGhostInformation(List<GHOST> listNearestEdible, double minD , double minEdible) {
		this.list=listNearestEdible;
		this.minDistance = minD;
		this.minEdibleTime = minEdible;
	}
	
	public List <GHOST>getListNearestEdibleGhost() {
		return this.list;
	}
	
	public double getMinDistanceNearestGhost() {
		return this.minDistance;
	}
	
	public double getMinEdibleTimeNearestGhost() {
		return this.minEdibleTime;
	}
}
