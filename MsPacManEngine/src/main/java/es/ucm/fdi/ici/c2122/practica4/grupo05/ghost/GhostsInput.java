package es.ucm.fdi.ici.c2122.practica4.grupo05.ghost;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.PillStatus;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsInput extends FuzzyInput {
	private final GhostsFuzzyMemory memory;

	public GhostsInput(Game game, GhostsFuzzyMemory memory) {
		super(game);
		this.memory = memory;
	}

	@Override
	public void parseInput() {
		/* do nothing */
	}


	@Override
	public HashMap<String, Double> getFuzzyValues() {
		throw new UnsupportedOperationException("Call this function with a GHOST");
	}

	public HashMap<String, Double> getFuzzyValues(GHOST g) {
		memory.getInput(this);
		HashMap<String, Double> vars = new HashMap<>();
		Optional<FuzzyValue<Target>> pacman = memory.getMostLikelyPosition();

		if (pacman.isPresent()) {
			Target target = pacman.get().getValue();
			Optional<FuzzyValue<PillStatus>> closestPPill = closestPowerPillToPacman(target);
			Optional<GHOST> closestEdible = closestEdibleGhostToPacman(target);

			if (game.getGhostLairTime(g) == 0) {
				vars.put("distancePacToGh",
						(double) PathDistance.toGhost(game, target.getNode(), target.getLastMove(),
								g));
				vars.put("distanceGhToPac",
						(double) PathDistance.fromGhostTo(game, g, target.getNode()));
			} else {
				vars.put("distancePacToGh", Double.POSITIVE_INFINITY);
				vars.put("distanceGhToPac", Double.POSITIVE_INFINITY);
			}

			vars.put("distancePacToPPill", (closestPPill.isPresent())
					? game.getDistance(target.getNode(),
							closestPPill.get().getValue().getNode(),
							target.getLastMove(), DM.PATH)
					: Double.POSITIVE_INFINITY);
			vars.put("distanceGhToEdi", (closestEdible.isPresent() && game.getGhostLairTime(g)==0)
					? PathDistance.fromGhostToGhost(game, g, closestEdible.get())
					: Double.POSITIVE_INFINITY);
			vars.put("distanceEdiToGh", (closestEdible.isPresent() && game.getGhostLairTime(g)==0)
					? PathDistance.fromGhostToGhost(game, closestEdible.get(), g)
					: Double.POSITIVE_INFINITY);
			vars.put("pacConfidence", pacman.get().getConfidence());
			vars.put("ppillConfidence", (closestPPill.isPresent())
					? closestPPill.get().getConfidence()
					: 0.0);
			vars.put("distanceGhToPPill", (closestPPill.isPresent() && game.getGhostLairTime(g)==0)
					? (double) PathDistance.fromGhostTo(game, g,
							closestPPill.get().getValue().getNode())
					: Double.POSITIVE_INFINITY);
		}

		else {
			vars.put("distancePacToGh", Double.POSITIVE_INFINITY);
			vars.put("distanceGhToPac", Double.POSITIVE_INFINITY);
			vars.put("distancePacToPPill", Double.POSITIVE_INFINITY);
			vars.put("distanceGhToEdi", Double.POSITIVE_INFINITY);
			vars.put("distanceEdiToGh", Double.POSITIVE_INFINITY);
			vars.put("pacConfidence", 0.0);
			vars.put("ppillConfidence", 0.0);
			vars.put("distanceGhToPPill", Double.POSITIVE_INFINITY);
		}

		vars.put("edibleTime", (double) game.getGhostEdibleTime(g));
		vars.put("existsProtector", (double) existsGhostProtector(g));

		return vars;
	}

	private Optional<FuzzyValue<PillStatus>> closestPowerPillToPacman(Target pacman) {
		return memory.getPowerPillsStatus()
				.stream()
				.filter(pp -> pp.getValue().isAvailable())
				.map(pp -> new ValueAndDistance<FuzzyValue<PillStatus>>(
						pp,
						(int) game.getDistance(pacman.getNode(), pp.getValue().getNode(),
								pacman.getLastMove(), DM.PATH)))
				.min((a, b) -> a.distance - b.distance)
				.flatMap(inner -> Optional.of(inner.v));
	}

	private Optional<GHOST> closestEdibleGhostToPacman(Target pacman) {
		return Arrays.stream(GHOST.values())
				.filter(game::isGhostEdible)
				.map(g -> new ValueAndDistance<GHOST>(
						g,
						PathDistance.toGhost(game, pacman.getNode(), pacman.getLastMove(), g)))
				.min((a, b) -> a.distance - b.distance)
				.flatMap(inner -> Optional.of(inner.v));
	}

    public int existsGhostProtector(GHOST ghost) {
		for(GHOST g: GHOST.values())
			if(g != ghost
                    && game.isGhostEdible(ghost)
                    && game.getGhostLairTime(ghost) <= 0
                    && game.getGhostLairTime(g) <= 0
                    && !game.isGhostEdible(g))
				return 1;
						
		return 0;
	}
    
	private class ValueAndDistance<T> {
		final T v;
		final int distance;

		ValueAndDistance(T v, int distance) {
			this.v = v;
			this.distance = distance;
		}
	}

}
