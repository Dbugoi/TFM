package es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.GameUtils;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostInput extends Input {

	public GhostInput(Game game) {
		super(game);
	}

	private double minPacmanDistancePPill;

	@Override
	public void parseInput() {/* does nothing */}



	public boolean isGhostEdible(GHOST ghost) {
		return game.isGhostEdible(ghost);
	}

	public boolean isThereEdible() {
		for (GHOST g : GHOST.values()) {
			if (game.isGhostEdible(g))
				return true;
		}
		return false;
	}



	// PROTECTOR
	public boolean isThereGhostProtector() {
		for (GHOST g : GHOST.values()) {
			if (!game.isGhostEdible(g) && game.getGhostLairTime(g) == 0)
				return true;
		}
		return false;
	}

	public boolean canGhostFollowAProtectorGhostSafely(GHOST ghost) {
		int thisGhostIndex = game.getGhostCurrentNodeIndex(ghost);
		int msPacmanIndex = game.getPacmanCurrentNodeIndex();
		MOVE lastMoveMadeByMsPacman = game.getPacmanLastMoveMade();

		for (GHOST otherGhost : GHOST.values())
			if (otherGhost != ghost
					&& game.isGhostEdible(ghost)
					&& game.getGhostLairTime(ghost) <= 0
					&& game.getGhostLairTime(otherGhost) <= 0
					&& !game.isGhostEdible(otherGhost)) {
				int otherGhostIndex = game.getGhostCurrentNodeIndex(otherGhost);
				MOVE lastMoveMadeByOtherGhost = game.getGhostLastMoveMade(otherGhost);

				boolean otherGhostCloserToThisGhostThanPacmanToThisGhost =
						game.getDistance(otherGhostIndex, thisGhostIndex, lastMoveMadeByOtherGhost,
								DM.PATH) < game.getDistance(msPacmanIndex, thisGhostIndex,
										lastMoveMadeByMsPacman, DM.PATH);

				boolean pacmanCloserToThisGhostThanPacmanToOtherGhost =
						game.getDistance(msPacmanIndex, thisGhostIndex, lastMoveMadeByMsPacman,
								DM.PATH) < game.getDistance(msPacmanIndex, otherGhostIndex,
										lastMoveMadeByMsPacman, DM.PATH);

				boolean otherGhostCloserToThisGhostThanOtherGhostToPacman =
						game.getDistance(otherGhostIndex, thisGhostIndex, lastMoveMadeByOtherGhost,
								DM.PATH) < game.getDistance(otherGhostIndex, msPacmanIndex,
										lastMoveMadeByOtherGhost, DM.PATH);


				if (otherGhostCloserToThisGhostThanPacmanToThisGhost
						&& pacmanCloserToThisGhostThanPacmanToOtherGhost
						&& otherGhostCloserToThisGhostThanOtherGhostToPacman)
					return true;
			}
		return false;
	}

	// DISTANCES
	public double getMinPacmanDistancePPill() {
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for (int ppill : game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		return minPacmanDistancePPill;
	}

	public boolean isPacmanCloseToGhost(GHOST ghost) {
		int indxG = game.getGhostCurrentNodeIndex(ghost);
		int indxP = game.getPacmanCurrentNodeIndex();

		if (indxG == -1 || indxP != -1) // para que no de error
			return false;
		return game.getShortestPathDistance(indxP, indxG, game.getPacmanLastMoveMade()) < GameUtils
				.getLimToMsPacMan();
	}

	public boolean isGhostCloseToPacman(GHOST ghost) {
		int indxG = game.getGhostCurrentNodeIndex(ghost);
		int indxP = game.getPacmanCurrentNodeIndex();

		if (indxG == -1 || indxP == -1) // a diferencia del de arriba nos da igual el ult mov
			return false;
		return game.getDistance(indxG, indxP, DM.PATH) < GameUtils.getLimToMsPacMan();
	}


	public boolean isPacmanCloseToPPill() {
		int mspacman = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		int pPill = getNearestPowerPill(mspacman, lastMove);

		if (pPill != -1) {
			return game.getShortestPathDistance(mspacman, pPill, lastMove) < GameUtils
					.getLimToPPillsPac();
		} else
			return false;
	}

	public boolean isGhostCloseToPPill(GHOST ghost) { // Edibles

		int indxGhost = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		int pPill = getNearestPowerPill(indxGhost, lastMove);

		if (pPill != -1) {
			return game.getShortestPathDistance(indxGhost, pPill, lastMove) < GameUtils
					.getLimToPPillsGhostE();
		} else
			return false;
	}

	public boolean isGhostCloseToGhost(GHOST ghost) {
		int indxGhost = game.getGhostCurrentNodeIndex(ghost);
		for (GHOST g : GHOST.values()) {
			int aux = game.getGhostCurrentNodeIndex(g);
			double d = game.getDistance(indxGhost, aux, DM.PATH);
			if (g != ghost && d < GameUtils.getLimToMsPacMan())
				return true;
		}
		return false;
	}



	// EATEN
	public boolean wasPacManEaten() {
		return game.wasPacManEaten();
	}

	public boolean wasGhostEaten(GHOST ghost) {
		return game.getGhostLairTime(ghost) > 0;
	}

	// JAIL
	public boolean isGhostJustGotOutOfCase(GHOST ghost) { // TODO siempre esta encendida esa
															// transicion
		return game.getGhostLairTime(ghost) == 0;
	}

	public boolean isGhostInCase(GHOST ghost) {
		return game.getGhostLairTime(ghost) > 0;
	}

	// GHOST IN PATH
	public boolean anyOtherChaseGhostIsInPath(GHOST ghost) {
		int[] shortestPath = game.getShortestPath(
				game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(),
				game.getGhostLastMoveMade(ghost));

		for (GHOST otherGhost : GHOST.values())
			if (otherGhost != ghost && !game.isGhostEdible(otherGhost))
				for (int index : shortestPath)
					if (index == game.getGhostCurrentNodeIndex(otherGhost))
						return true;

		return false;
	}

	// PREPARE ATTACK
	public boolean isGhostReadyToAttack(GHOST ghost) {
		if (game.getGhostLairTime(ghost) > 0)
			return false;

		int pacmanIndex = game.getPacmanCurrentNodeIndex();
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);

		int distanceTravelledByGhost = game.getGhostEdibleTime(ghost) / 2;
		double distanceFromMsPacmanToGhost =
				game.getDistance(pacmanIndex, ghostIndex, game.getPacmanLastMoveMade(), DM.PATH);

		// es importante comprobar que pacman no esté cerca de la PP para evitar sustos
		return distanceFromMsPacmanToGhost - distanceTravelledByGhost > game
				.getGhostEdibleTime(ghost) && !isPacmanCloseToPPill();

	}

	// AUX
	private int getNearestPowerPill(int indx, MOVE lastMove) {

		int finalPill = -1;
		double minDistance = Integer.MAX_VALUE;

		for (int ppillIndex : game.getActivePowerPillsIndices()) { // miramos todas las powerpills
			double d = game.getShortestPathDistance(indx, ppillIndex, lastMove);
			if (minDistance > d) {
				minDistance = d;
				finalPill = ppillIndex;
			}
		}
		return finalPill;
	}

	//
	public boolean isGhostCloserThanPacManTo(int indx, GHOST ghost) {
		int indxG = game.getGhostCurrentNodeIndex(ghost);
		int indxP = game.getPacmanCurrentNodeIndex();
		MOVE lastMoveG = game.getGhostLastMoveMade(ghost);
		MOVE lastMoveP = game.getPacmanLastMoveMade();
		return game.getDistance(indxG, indx, lastMoveG, DM.PATH) < game.getDistance(indxP, indx,
				lastMoveP, DM.PATH);
	}

	public int getAlmostLastJunctionToClosestPowerPillToMsPacMan() {
		int lastJunction = -1;
		int almostLastJunction = -1;
		for (int indx : getPathToClosestPowerPillToMsPacMan()) {
			if (game.isJunction(indx)) {
				almostLastJunction = lastJunction;
				lastJunction = indx;
			}
		}
		if (almostLastJunction != -1)
			return almostLastJunction;
		else
			return lastJunction;
	}

	public int[] getPathToClosestPowerPillToMsPacMan() {
		int[] pathClosestPPill = {};
		int pacmanIndex = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();

		int closestPill = getNearestPowerPill(game.getPacmanCurrentNodeIndex(), lastMove);

		if (closestPill != -1)
			pathClosestPPill = game.getShortestPath(pacmanIndex, closestPill, lastMove);

		return pathClosestPPill;
	}

	// BLOQUEAR
	public boolean canGhostBlock(GHOST ghost) {
		int nearestPPill = GameUtils.getClosestPowerPillToMsPacMan(game, DM.PATH);
		if (nearestPPill != -1) {
			int indxG = game.getGhostCurrentNodeIndex(ghost);
			int indxP = game.getPacmanCurrentNodeIndex();
			MOVE lastMoveG = game.getGhostLastMoveMade(ghost);
			MOVE lastMoveP = game.getPacmanLastMoveMade();
			int distGtoPP = game.getShortestPathDistance(indxG, nearestPPill, lastMoveG);
			int distPtoPP = game.getShortestPathDistance(indxP, nearestPPill, lastMoveP);

			// Si ambos movimientos son distintos, es que han venido por caminos diferentes,
			// y por tanto, no ocurre que pacman y el fantasma vayan en fila
			MOVE moveFromPPtoP = game.getNextMoveTowardsTarget(nearestPPill, indxP, DM.PATH);
			MOVE moveFromPPtoG = game.getNextMoveTowardsTarget(nearestPPill, indxG, DM.PATH);

			if (distGtoPP < distPtoPP && moveFromPPtoG != moveFromPPtoP)
				return true;
		}
		return false;
	}

	public boolean canKillPacman(GHOST ghost) {
		/**
		 * MsPacman morirá si: Ghost no comible y fuera de cárcel, MsPacman no se va a comer una PP
		 * en el camino (porque no haya PP o porque el fantasma puede bloquearla), y una de las
		 * siguientes condiciones: Ghost llega al cruce destino de MsPacman antes que ella, o Ghost
		 * está en dicho cruce
		 */
		if (game.isGhostEdible(ghost) || game.getGhostLairTime(ghost) > 0)
			return false;

		int nextJunction = GameUtils.getDestinationJunctionForMsPacman(game);
		int indxP = game.getPacmanCurrentNodeIndex();
		MOVE lastMoveP = game.getPacmanLastMoveMade();
		int distGtoJunction = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost),
				nextJunction, game.getGhostLastMoveMade(ghost));
		int distPtoJunction = game.getShortestPathDistance(indxP, nextJunction, lastMoveP);
		if (distGtoJunction > distPtoJunction)
			return false;

		int[] pathToNextJunction = game.getShortestPath(indxP, nextJunction, lastMoveP);

		for (int node : pathToNextJunction)
			for (int powerpill : game.getActivePowerPillsIndices())
				if (node == powerpill && !canGhostBlock(ghost))
					return false;
		return true;
	}

	public boolean isGhostInPath(int indxG, int[] path) {
		for (int ind : path) {
			if (indxG == ind)
				return true;
		}
		return false;
	}

	public int getGhostLairTime(GHOST ghost) {
		return game.getGhostLairTime(ghost);
	}
}
