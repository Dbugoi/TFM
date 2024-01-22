package es.ucm.fdi.ici.c2122.practica3.grupo07.pacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {
	private int closestPP;
	private double closestPPDistance;
	private GHOST closestGHOST;
	private double closestGHOSTDistance;
	public MsPacManInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void parseInput() {
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();
		
		closestPP=game.getClosestNodeIndexFromNodeIndex(pacman, game.getActivePowerPillsIndices(), DM.PATH);
		if(closestPP!=-1)closestPPDistance=game.getDistance(pacman, closestPP, pacmanLastMove, DM.PATH);
		
		double minDist=Double.MAX_VALUE;
		for(GHOST g:GHOST.values()) {
			int ghost = game.getGhostCurrentNodeIndex(g);
			MOVE ghostLastMove=game.getGhostLastMoveMade(g);
			if(game.getGhostLairTime(g)<=0) {
				double dist=game.getDistance(ghost, pacman, ghostLastMove, DM.PATH);
				if(dist<minDist) {
					minDist=dist;
					closestGHOST=g;
				}
			}
		}
		closestGHOSTDistance=minDist;
	}

	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(CLOSESTGHOST (name %s) (distance %d))", closestGHOST,(int)closestGHOSTDistance));
		facts.add(String.format("(CLOSESTPP (index %d) (distance %d))", closestPP,(int)closestPPDistance));
		System.out.println(String.format("(CLOSESTGHOST (name %s distance %d))", closestGHOST,(int)closestGHOSTDistance));
		System.out.println(String.format("(CLOSESTPP (index %d distance %d))", closestPP,(int)closestPPDistance));

		return facts;
	}

}
