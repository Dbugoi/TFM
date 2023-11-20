package es.ucm.fdi.ici.c2122.practica3.grupo07;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {
	private int closestPP;
	boolean anyedible;
	boolean anysleeping;
	public MsPacManInput(Game game) {
		super(game);
		parseInput();
	}

	@Override
	public void parseInput() {
		anyedible=false;
		anysleeping=false;
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();

		closestPP=Util.getMinDistanceNode(game, pacman, game.getActivePowerPillsIndices(), pacmanLastMove);		

		for(GHOST g:GHOST.values()) {
			if(game.isGhostEdible(g))anyedible=true;
			if(game.getGhostLairTime(g)>0)anysleeping=true;
		}
	}

	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(CLOSESTPP (index %d))", closestPP));
		facts.add(String.format("(REACHABLEGHOST (anyedible %s) (anysleeping %s))",anyedible,anysleeping));
		return facts;
	}

}



