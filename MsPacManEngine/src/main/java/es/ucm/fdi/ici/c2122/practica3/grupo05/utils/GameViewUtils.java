package es.ucm.fdi.ici.c2122.practica3.grupo05.utils;

import java.awt.Color;

import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.GHOST;

public class GameViewUtils {
	private GameViewUtils() {}
	
	
	public static void addLineBetweenMsPacManAndGhost(Game game, GHOST ghost, Color color) {
		GameView.addLines(game, color, game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost));
	}

	public static void addVisualPathFromMsPacManToGhost(Game game, GHOST ghost, Color color) {
		int[] path = game.getShortestPath(
				game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanLastMoveMade());

		GameView.addPoints(game, color, path);
	}

	public static void addVisualPathFromGhostToMsPacMan(Game game, GHOST ghost, Color color) {
		int[] path = game.getShortestPath(
				game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(),
				game.getGhostLastMoveMade(ghost));

		GameView.addPoints(game, color, path);
	}

	public static Color getGhostColor(GHOST ghost) {
		switch (ghost) {
			case BLINKY:
				return Color.RED;
			case INKY:
				return Color.CYAN;
			case PINKY:
				return Color.PINK;
			case SUE:
				return Color.ORANGE;
			default:
				return Color.WHITE;
		}
	}
}
