package es.ucm.fdi.ici.c2122.practica4.grupo05.ghost.actions;


import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Paths;
import java.util.EnumMap;
import java.util.Map;
import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


public class BestCorner implements Action {
	private static final Map<GHOST, Integer> assignedCorners;
	
	static {
		assignedCorners = new EnumMap<>(GHOST.class);
		assignedCorners.put(GHOST.BLINKY, Paths.BOTTOM_LEFT_CORNER);
		assignedCorners.put(GHOST.INKY, Paths.TOP_LEFT_CORNER);
		assignedCorners.put(GHOST.PINKY, Paths.BOTTOM_RIGHT_CORNER);
		assignedCorners.put(GHOST.SUE, Paths.TOP_RIGHT_CORNER);
	}

	private final GHOST ghost;
	private final int corner;

	public BestCorner(GHOST g) {
		this.ghost = g;
		this.corner = assignedCorners.get(ghost);
	}

	@Override
	public MOVE execute(Game game) {
		return Moves.ghostTowards(game, ghost, corner);
	}

	@Override
	public String getActionId() {
		return "BestCorner";
	}

}
