package es.ucm.fdi.ici.c2122.practica5.grupo05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pacman.game.Constants.MOVE;

public class JunctionInformation {

	private int currentJ;
	private List<List<Integer>> initialJ;
	private List<Integer> path;
	private MOVE lastMove;
	private MOVE initialMove;
	private Boolean goodPath;
	private int ppillInRoad;
	public JunctionInformation(int currentJ, List<List<Integer>> initialJPREV, int initialJint, MOVE lastMove, MOVE initialMove, int pp, List<Integer>path) {
		
		initialJ =  new ArrayList<>();  
		for(List<Integer> list : initialJPREV) {
			initialJ.add(new ArrayList<>());
			for(Integer i: list)
				initialJ.get(initialJ.size()-1).add(i);
		}
		if(initialJPREV.size()==0)
			{initialJ.add(new ArrayList<Integer>());
			initialJ.get(initialJ.size()-1).add(initialJint);
			}
		else
			if(!initialJ.get(initialJ.size()-1).contains(initialJint))
				initialJ.get(initialJ.size()-1).add(initialJint);
		this.currentJ=currentJ;  this.lastMove=lastMove; this.initialMove=initialMove;
		goodPath=true;
		this.ppillInRoad=pp;
		this.path =path;
	}
	
	public void changeGoodness(Boolean b) {
		this.goodPath=false;
	}
	public int getCurrentJ () {
		return currentJ;
	}
	public List<List<Integer>> getInitialJ () {
		return initialJ;
	}
	
	public List<Integer> getPath(){
		return path;
	}
	
	public void addToInitialJ (List<Integer> newList) {
		initialJ.add(newList);
	}
	public MOVE getLastMove () {
		return lastMove;
	}	
	public MOVE getInitialMove () {
		return initialMove;
	}
	
	public Boolean isAGoodPath () {
		return goodPath;
	}
	public int hasPP() {
		return ppillInRoad;
	}
	public void changePP(int b) {
		ppillInRoad=b;
	}
}
