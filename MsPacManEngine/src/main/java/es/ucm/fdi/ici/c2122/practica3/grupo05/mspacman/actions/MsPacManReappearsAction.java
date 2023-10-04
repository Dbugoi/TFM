package es.ucm.fdi.ici.c2122.practica3.grupo05.mspacman.actions;



import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class MsPacManReappearsAction implements RulesAction {

	public MsPacManReappearsAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MOVE execute(Game game) {
		return MOVE.NEUTRAL;
		
	}

	@Override
	public String getActionId() {
		return "MsPacman Reappears Action";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}