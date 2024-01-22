package es.ucm.fdi.ici.c2122.practica3.grupo10.mspacman.actions;

import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomMoveAction implements RulesAction {
	
	ArrayList<MOVE> possibleMoves;
	private Random rnd = new Random();
	public RandomMoveAction() {
	}

	@Override
	public MOVE execute(Game game) {
		return possibleMoves.get(rnd.nextInt(possibleMoves.size()));
	}

	@Override
	public void parseFact(Fact actionFact) {
		//get the list of possible moves from the fact slot
		try {
			possibleMoves = new ArrayList<MOVE>();
			String[] parts = actionFact.getSlotValue("moves").toString().split("-");
			for (String s: parts) {
				possibleMoves.add(MOVE.valueOf(s));
			}
		} catch (JessException e) {
			System.err.println(" :Exception parsing fact");
			e.printStackTrace();
		}
	}

	@Override
	public String getActionId() {
		return "chases";
	}

	

}
