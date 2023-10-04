package es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions;



import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class MsPacManReappearsAction implements Action {

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

}