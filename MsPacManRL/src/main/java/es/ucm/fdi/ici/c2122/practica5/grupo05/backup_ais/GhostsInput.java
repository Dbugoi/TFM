package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.AIParameters;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.CheckIfGhost;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Paths;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Pills;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

class GhostsInput extends RulesInput {

	private Map<GHOST, Boolean> mapEdible;
	private Map<GHOST, Boolean> mapGhostLairTimeEqualTo;
	private Map<GHOST, Boolean> canKillInNextMoveTransition;
	private Map<GHOST, Boolean> canGhostFollowAProtectorGhostSafely;
	private Map<GHOST, Boolean> canKillPacman;
	private Map<GHOST, Boolean> isReadyToAttack;
	private Map<GHOST, Boolean> canBlockClosestPPToPacman;
	private boolean isPacmanCloseToPPill;
	private boolean thereIsAProtectorGhost;
	private List<String> facts = null;

	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		mapEdible = new EnumMap<>(GHOST.class);
		mapGhostLairTimeEqualTo = new EnumMap<>(GHOST.class);
		canKillInNextMoveTransition = new EnumMap<>(GHOST.class);
		canGhostFollowAProtectorGhostSafely = new EnumMap<>(GHOST.class);
		canKillPacman = new EnumMap<>(GHOST.class);
		isReadyToAttack = new EnumMap<>(GHOST.class);
		canBlockClosestPPToPacman = new EnumMap<>(GHOST.class);

		int closestPPToPacman = Pills.getClosestPowerPillToMsPacMan(game);
		int nextJunctionPacman = Paths.getDestinationJunctionForMsPacman(game);

		for (GHOST ghost : GHOST.values()) {
			mapEdible.put(ghost, game.isGhostEdible(ghost));
			mapGhostLairTimeEqualTo.put(ghost, game.getGhostLairTime(ghost) == 0);
			canKillInNextMoveTransition.put(ghost, nextJunctionPacman == game
					.getGhostCurrentNodeIndex(ghost));

			canGhostFollowAProtectorGhostSafely.put(ghost,
					canGhostFollowAProtectorGhostSafely(ghost));
			canKillPacman.put(ghost, canKillPacman(ghost));
			isReadyToAttack.put(ghost, isGhostReadyToAttack(ghost));
			canBlockClosestPPToPacman.put(ghost, canGhostBlockPowerPill(ghost, closestPPToPacman));
		}

		isPacmanCloseToPPill = isPacmanCloseToPPill();
		thereIsAProtectorGhost = isThereAProtectorGhost();
	}

	@Override
	public Collection<String> getFacts() {
		if (facts == null) {
			facts = new ArrayList<>();

			for (GHOST ghost : GHOST.values()) {
				facts.add(String.format("(%s (inLair %s))", ghost.name(),
						isInLair(ghost)));

				facts.add(String.format("(%s (goToBestCorner %s))", ghost.name(),
						!isInLair(ghost)
								&& isInDanger(ghost)
								&& !edibleMayFollowProtectorSafely(ghost)));
				// && hasToFlee(ghost)));

				facts.add(String.format("(%s (followProtector %s))", ghost.name(),
						!isInLair(ghost)
								&& isInDanger(ghost)
								&& edibleMayFollowProtectorSafely(ghost)));
				// && !hasToFlee(ghost)));

				facts.add(String.format("(%s (ambushPacman %s))", ghost.name(),
						!isInLair(ghost)
								&& !isInDanger(ghost)
								&& !canKill(ghost)));

				facts.add(String.format("(%s (moveToKill %s))", ghost.name(),
						!isInLair(ghost) && !isInDanger(ghost) && canKill(ghost)
								&& !canKillInNextMove(ghost)));

				facts.add(String.format("(%s (killPacMan %s))", ghost.name(),
						!isInLair(ghost)
								&& !isInDanger(ghost)
								&& canKill(ghost)
								&& canKillInNextMove(ghost)));

				facts.add(String.format("(%s (blockPowerPill %s))", ghost.name(),
						!isInLair(ghost)
								&& !isInDanger(ghost)
								&& !canKill(ghost)
								&& canBlockClosestPPToPacman.get(ghost)));

				facts.add(String.format("(%s (defendEdibleGhost %s))", ghost.name(),
						!isInLair(ghost)
								&& !isInDanger(ghost)
								&& !canKill(ghost)
								&& ghostCanDefendClosestEdibleGhostToPacman(ghost)));
			}
		}
		return facts;
	}


	////////// TRANSICIONES Y FUNCIONES AUXILIARES //////////
	private boolean isInLair(GHOST ghost) {
		return !mapGhostLairTimeEqualTo.get(ghost);
	}

	private boolean isInDanger(GHOST ghost) {
		return (mapEdible.get(ghost) && !isReadyToAttack.get(ghost)
				|| (!mapEdible.get(ghost) && isPacmanCloseToPPill && !canKill(ghost)));
	}

	private boolean edibleMayFollowProtectorSafely(GHOST ghost) {
		return mapEdible.get(ghost) && thereIsAProtectorGhost
				&& canGhostFollowAProtectorGhostSafely.get(ghost);
	}

	private boolean canKill(GHOST ghost) {
		return canKillPacman.get(ghost);
	}

	private boolean canKillInNextMove(GHOST ghost) {
		return canKillInNextMoveTransition.get(ghost);
	}

	public boolean isPacmanCloseToPPill() {
		return Pills.isMsPacManCloseToPowerPill(game, AIParameters.LIMIT_TO_PPILLS_PAC);
	}

	public boolean isThereAProtectorGhost() {
		for (GHOST g : GHOST.values()) {
			if (CheckIfGhost.isChasing(game, g))
				return true;
		}
		return false;
	}

	public boolean canGhostFollowAProtectorGhostSafely(GHOST ghost) {
		for (GHOST otherGhost : GHOST.values())
			if (otherGhost != ghost
					&& game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) <= 0
					&& !game.isGhostEdible(otherGhost) && game.getGhostLairTime(otherGhost) <= 0) {

				boolean otherGhostCloserToThisGhostThanPacmanToThisGhost =
						PathDistance.fromGhostToGhost(game, otherGhost, ghost) < PathDistance
								.fromPacmanToGhost(game, ghost);

				boolean pacmanCloserToThisGhostThanPacmanToOtherGhost =
						PathDistance.fromPacmanToGhost(game, ghost) < PathDistance
								.fromPacmanToGhost(game, otherGhost);

				boolean otherGhostCloserToThisGhostThanOtherGhostToPacman =
						PathDistance.fromGhostToGhost(game, otherGhost, ghost) < PathDistance
								.fromGhostToPacman(game, otherGhost);


				if (otherGhostCloserToThisGhostThanPacmanToThisGhost
						&& pacmanCloserToThisGhostThanPacmanToOtherGhost
						&& otherGhostCloserToThisGhostThanOtherGhostToPacman)
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

		int nextJunction = Paths.getDestinationJunctionForMsPacman(game);
		int indxP = game.getPacmanCurrentNodeIndex();
		MOVE lastMoveP = game.getPacmanLastMoveMade();
		int distGtoJunction = PathDistance.fromGhostTo(game, ghost, nextJunction);
		int distPtoJunction = PathDistance.fromPacmanTo(game, nextJunction);

		if (distGtoJunction > distPtoJunction)
			return false;

		int[] pathToNextJunction = game.getShortestPath(indxP, nextJunction, lastMoveP);

		for (int node : pathToNextJunction)
			for (int powerpill : game.getActivePowerPillsIndices())
				if (node == powerpill && !canGhostBlockPowerPill(ghost, powerpill))
					return false;
		return true;
	}

	public boolean canGhostBlockPowerPill(GHOST ghost, int powerPill) {
		if (powerPill != -1
				&& game.getGhostLairTime(ghost) == 0
				&& !CheckIfGhost.couldBeEaten(game, ghost)) {
			int distGtoPP = PathDistance.fromGhostTo(game, ghost, powerPill);
			int distPtoPP = PathDistance.fromPacmanTo(game, powerPill);

			// Si ambos movimientos son distintos, es que han venido por caminos diferentes,
			// y por tanto, no ocurre que pacman y el fantasma vayan en fila
			MOVE moveFromPPtoP = Moves.towardsPacman(game, powerPill, MOVE.NEUTRAL);
			MOVE moveFromPPtoG = Moves.towardsGhost(game, powerPill, MOVE.NEUTRAL, ghost);

			if (distGtoPP < distPtoPP && moveFromPPtoG != moveFromPPtoP)
				return true;
		}
		return false;
	}

	public boolean isGhostReadyToAttack(GHOST ghost) {
		return !CheckIfGhost.couldBeEaten(game, ghost);
	}

	public boolean ghostCanDefendClosestEdibleGhostToPacman(GHOST defender) {
		if (game.doesGhostRequireAction(defender)
			|| !CheckIfGhost.isChasing(game, defender) 
			|| !mapEdible.containsValue(true)) {
			return false;
		}
        Optional<GHOST> optEdible = 
            //GhostFinder.findClosestEdible(game, DM.PATH);
            GhostFinder.findClosestThatFollowsFilter(game, DM.PATH, g -> CheckIfGhost.couldBeEaten(game, g));
		
		if(optEdible.isPresent()){
			GHOST edible = optEdible.get();
			Game copy = game.copy();
            Map<GHOST, MOVE> ghostMoves = new EnumMap<>(GHOST.class);

            while (copy.isGhostEdible(edible)
                    && !copy.wasGhostEaten(edible)
                    && !copy.wasPacManEaten()) {
                copy.updatePacMan(Moves.pacmanTowardsGhost(copy, edible));

                for (GHOST g : GHOST.values()) {
					MOVE move = CheckIfGhost.isInLair(game, g)
						? MOVE.NEUTRAL
						: Moves.ghostAwayFromPacman(copy, g);
                    ghostMoves.put(g, move);
                }
                ghostMoves.put(edible, Moves.ghostTowardsGhost(copy, edible, defender));
                ghostMoves.put(defender, Moves.ghostTowardsGhost(copy, defender, edible));

                copy.updateGhosts(ghostMoves);

                // En la copia solo actualizamos los tiempos en los que los fantasmas
                // son comestibles y el que sean comidos por pacman.
                // Si un fantasma se come a pacman a pesar de intentar alejarse
                // podemos suponer que es imposible que pacman se coma a 'ghost'
                copy.updateGame(true, false, false, false, false);
            }
            return copy.wasGhostEaten(edible);
		}
		else{
			return false;
		}
	}
}
