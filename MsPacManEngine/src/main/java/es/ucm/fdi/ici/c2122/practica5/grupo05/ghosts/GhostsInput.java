package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.cbr.CBRInput;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.AdaptorEnumMap;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.AdaptorMoveIntMap;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.GhostFinder;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Paths;
import es.ucm.fdi.ici.c2122.practica5.grupo05.utils.Pills;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.Objects;
import java.util.Optional;

public class GhostsInput extends CBRInput {
	private final GHOST ghost;
	private Integer score;
	private Integer time;
	private Integer level;
	private int closestPPToPac;
	private Optional<GHOST> closestEdibleToPac;
	private int nextPacmanJunction;

	public GhostsInput(Game game, GHOST ghost) {
		super(Objects.requireNonNull(game));
		this.ghost = Objects.requireNonNull(ghost);
	}

	@Override
	public void parseInput() {
		score = game.getScore();
		time = game.getTotalTime();
		level = game.getCurrentLevel();
		closestPPToPac = Pills.getClosestPowerPillToMsPacMan(game);
		closestEdibleToPac = GhostFinder.findClosestEdible(game, DM.PATH);
		nextPacmanJunction = Paths.getDestinationJunctionForMsPacman(game);
	}

	@SuppressWarnings("unused")
	@Override
	public CBRQuery getQuery() {
		GhostsDescription desc = new GhostsDescription();

		desc.setScore(score);
		desc.setTime(time);
		desc.setLevel(level);
		desc.setGhost(ghost);

		desc.setEdibleTime(game.getGhostEdibleTime(ghost));

		desc.setDistToPacman(new AdaptorMoveIntMap());
		desc.setDistPacmanToGhost(PathDistance.fromPacmanToGhost(game, ghost));
		desc.setMovePacmanToGhost(Moves.pacmanTowardsGhost(game, ghost));

		desc.setDistToJunction(new AdaptorMoveIntMap());

		desc.setDistToPPill(new AdaptorMoveIntMap());
		desc.setDistPacmanToPPill(PathDistance.fromPacmanTo(game, closestPPToPac));
		desc.setMovePacmanToPPill(Moves.pacmanTowards(game, closestPPToPac));

		desc.setDistToEdible(new AdaptorMoveIntMap());
		if (closestEdibleToPac.isPresent()) {
			desc.setDistPacmanToEdible(
					PathDistance.fromPacmanToGhost(game, closestEdibleToPac.get()));
			desc.setMovePacmanToEdible(Moves.pacmanTowardsGhost(game, closestEdibleToPac.get()));
		} else {
			desc.setDistPacmanToEdible(Integer.MAX_VALUE);
			desc.setMovePacmanToEdible(MOVE.NEUTRAL);
		}

		for (MOVE move : MOVE.values()) {
			if (move.opposite() != game.getGhostLastMoveMade(ghost)) {
				int neigh = game.getNeighbour(game.getGhostCurrentNodeIndex(ghost), move);

				desc.getDistToPacman().put(move,
						PathDistance.toPacman(game, neigh, move));
				desc.getDistToJunction().put(move,
						PathDistance.fromXtoX(game, neigh, move, nextPacmanJunction));
				desc.getDistToPPill().put(move,
						PathDistance.fromXtoX(game, neigh, move, closestPPToPac));

				if (closestEdibleToPac.isPresent())
					desc.getDistToEdible().put(move,
							PathDistance.toGhost(game, neigh, move, closestEdibleToPac.get()));
				else
					desc.getDistToEdible().put(move, Integer.MAX_VALUE);
			} else {
				desc.getDistToPacman().put(move, Integer.MAX_VALUE);
				desc.getDistToJunction().put(move, Integer.MAX_VALUE);
				desc.getDistToPPill().put(move, Integer.MAX_VALUE);
				desc.getDistToEdible().put(move, Integer.MAX_VALUE);
			}
		}

		CBRQuery query = new CBRQuery();
		query.setDescription(desc);
		return query;
	}

}
