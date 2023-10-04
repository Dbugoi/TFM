package es.ucm.fdi.ici.c2122.practica4.grupo05.ghost;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.ToIntFunction;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.PillStatus;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PathDistance;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsFuzzyMemory {
    private int currentLevel = -1;
    private int lastTick = -1;
    private SortedSet<FuzzyValue<Target>> pacmanPossiblePositions;
    private Set<FuzzyValue<PillStatus>> powerPillsStatus;
    private Map<GHOST, Optional<GHOST>> protectorGhostForEachGhost;
    private Optional<Integer> closestPowerPillToMsPacman;

    public GhostsFuzzyMemory() {
        pacmanPossiblePositions = new TreeSet<>(FuzzyValue.fuzzyComparator());
        powerPillsStatus = new TreeSet<>(FuzzyValue.fuzzyComparator());
        protectorGhostForEachGhost = new EnumMap<>(GHOST.class);
    }


    public Optional<FuzzyValue<Target>> getMostLikelyPosition() {
        if (pacmanPossiblePositions.isEmpty())
            return Optional.empty();
        else
            return Optional.of(pacmanPossiblePositions.last());
    }


    public Set<FuzzyValue<Target>> getPacmanPossiblePositions() {
        return Collections.unmodifiableSet(pacmanPossiblePositions);
    }


    public Set<FuzzyValue<PillStatus>> getPowerPillsStatus() {
        return Collections.unmodifiableSet(powerPillsStatus);
    }


    public Optional<GHOST> getProtectorForGhost(GHOST ghost) {
        return protectorGhostForEachGhost.get(ghost);
    }

    public Optional<Integer> getClosestPowerPillToMsPacman() {
        return closestPowerPillToMsPacman;
    }


    public void getInput(GhostsInput input) {
        Game game = input.getGame();
        if (game.getCurrentLevel() != currentLevel) {
            currentLevel = game.getCurrentLevel();
            reset(game);
        } else if (game.getTotalTime() != lastTick) {
            lastTick = game.getTotalTime();
            updateMemory(game);
        }
    }


    private void reset(Game game) {
        pacmanPossiblePositions.clear();

        powerPillsStatus.clear();
        for (int pill : game.getPowerPillIndices()) {
            powerPillsStatus.add(new FuzzyValue<>(
                    new PillStatus(pill, true),
                    FuzzyValue.MAX_CONFIDENCE));
        }

        for (GHOST g : GHOST.values()) {
            protectorGhostForEachGhost.put(g, Optional.empty());
        }
    }


    private void updateMemory(Game game) {
        if (game.wasPowerPillEaten()) {
            if (pacmanPossiblePositions.isEmpty())
                addPacmanPositionsCloseToPPills(game);
            else
                updateWhenPPillHasJustBeenEaten(game);
        } else {
            updateCurrentPacmanIndex(game);
            updatePowerPills(game);
        }
        updateProtectorGhostsAssignment(game);
        updateClosestPowerPillToMsPacman(game);
    }


    private void updateCurrentPacmanIndex(Game game) {
        if (game.isNodeObservable(game.getPacmanCurrentNodeIndex())) {
            updateWhenPacmanIsVisible(game);
        } else if (game.wasPacManEaten()) {
            pacmanPossiblePositions.clear();
        } else {
            advanceAllPossiblePositions(game);
        }
    }


    private void updateWhenPacmanIsVisible(Game game) {
        pacmanPossiblePositions.clear();
        pacmanPossiblePositions.add(new FuzzyValue<>(
                new Target(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()),
                FuzzyValue.MAX_CONFIDENCE));
    }

    private void advanceAllPossiblePositions(Game game) {
        List<FuzzyValue<Target>> newPositions = new LinkedList<>();

        for (FuzzyValue<Target> fuzzyTarget : pacmanPossiblePositions) {
            Target target = fuzzyTarget.getValue();
            int[] neighbours = game.getNeighbouringNodes(target.getNode(), target.getLastMove());
            double newConfidence = fuzzyTarget.getConfidence() / neighbours.length;

            if (nodeIsInteresting(game, target.getNode(), newConfidence))
                for (int node : neighbours) {
                    MOVE moveToNode =
                            game.getMoveToMakeToReachDirectNeighbour(target.getNode(), node);
                    newPositions.add(new FuzzyValue<>(new Target(node, moveToNode), newConfidence));
                }
        }
        pacmanPossiblePositions.clear();
        pacmanPossiblePositions.addAll(newPositions);
        updatePossiblePacmanPositionsConfidences();
    }


    private boolean nodeIsInteresting(Game game, int node, double newConfidence) {
        return newConfidence > 1.0 && !game.isNodeObservable(node);
    }


    private void updatePossiblePacmanPositionsConfidences() {
        double totalConfidence = pacmanPossiblePositions
                .stream()
                .mapToDouble(pacman -> pacman.getConfidence())
                .sum();
        // Reajustamos las confianzas para que vuelvan a sumar 100%
        pacmanPossiblePositions.forEach(pacman -> pacman.setConfidence(
                pacman.getConfidence() * (FuzzyValue.MAX_CONFIDENCE / totalConfidence)));
    }

    private void updatePowerPills(Game game) {
        long availableAndNotObservablePPillsCount =
                powerPillsStatus.stream()
                        .filter(ppill -> ppill.getValue().isAvailable())
                        .filter(ppill -> !game.isNodeObservable(ppill.getValue().getNode()))
                        .count();

        for (FuzzyValue<PillStatus> ppill : powerPillsStatus) {
            if (game.isNodeObservable(ppill.getValue().getNode())) {
                boolean available = game.isPowerPillStillAvailable(
                        game.getPowerPillIndex(ppill.getValue().getNode()));
                ppill.getValue().setAvailable(available);
                ppill.setConfidence(FuzzyValue.MAX_CONFIDENCE);
            }
            // TODO: this is probably wrong!!!
            else if (ppill.getValue().isAvailable()
                    && (ppill.getConfidence() < FuzzyValue.MAX_CONFIDENCE
                            || game.wasPowerPillEaten())) {
                ppill.setConfidence(
                        FuzzyValue.MAX_CONFIDENCE / availableAndNotObservablePPillsCount);
            }
        }
    }

    private void updateWhenPPillHasJustBeenEaten(Game game) {
        if (game.wasPowerPillEaten()) {
            Set<FuzzyValue<PillStatus>> ppillsThatCouldHaveBeenEaten =
                    new TreeSet<>(FuzzyValue.fuzzyComparator());
            SortedSet<FuzzyValue<Target>> newPossiblePositions =
                    new TreeSet<>(FuzzyValue.fuzzyComparator());

            for (FuzzyValue<PillStatus> ppill : powerPillsStatus) {
                for (FuzzyValue<Target> pacman : pacmanPossiblePositions) {
                    if (ppill.getValue().isAvailable()
                            && game.getShortestPathDistance(
                                    pacman.getValue().getNode(),
                                    ppill.getValue().getNode(),
                                    pacman.getValue().getLastMove()) < Constants.EAT_DISTANCE) {
                        ppillsThatCouldHaveBeenEaten.add(ppill);
                        newPossiblePositions.add(pacman);
                    }
                }
            }
            pacmanPossiblePositions = newPossiblePositions;
            updatePossiblePacmanPositionsConfidences();

            // modifications should also be seen in this.powerPillsStatus
            for (FuzzyValue<PillStatus> ppill : ppillsThatCouldHaveBeenEaten) {
                ppill.setConfidence(
                        FuzzyValue.MAX_CONFIDENCE / ppillsThatCouldHaveBeenEaten.size());
                ppill.getValue().setAvailable(false);
            }
        }
    }

    private void addPacmanPositionsCloseToPPills(Game game) {
        SortedSet<FuzzyValue<Target>> newPositions = new TreeSet<>(FuzzyValue.fuzzyComparator());

        for (FuzzyValue<PillStatus> fuzzyppill : powerPillsStatus) {
            if (fuzzyppill.getValue().isAvailable()
                    && fuzzyppill.getConfidence() > FuzzyValue.MIN_CONFIDENCE) {
                int ppill = fuzzyppill.getValue().getNode();
                Set<FuzzyValue<Target>> pos = getPositionsWithinEatingDistanceOfPPill(game, ppill);
                newPositions.addAll(pos);
            }
        }
        pacmanPossiblePositions = newPositions;
    }


    private Set<FuzzyValue<Target>> getPositionsWithinEatingDistanceOfPPill(Game game, int ppill) {
        Set<FuzzyValue<Target>> newPositions = new TreeSet<>(FuzzyValue.fuzzyComparator());

        int[] neighbours = game.getNeighbouringNodes(ppill);
        Set<Integer> exploredNodes = new TreeSet<>();
        Set<Integer> nodesToExplore = new TreeSet<>();

        exploredNodes.add(ppill);
        for (int n : neighbours)
            nodesToExplore.add(n);

        for (int i = 0; i < Constants.EAT_DISTANCE; i++) {
            Set<Integer> newNodesToExplore = new TreeSet<>();
            for (int neigh : nodesToExplore) {
                int[] nextNeighbours = game.getNeighbouringNodes(neigh);
                for (int nextNeigh : nextNeighbours) {
                    if (!exploredNodes.contains(nextNeigh))
                        newNodesToExplore.add(nextNeigh);
                }
            }
            nodesToExplore = newNodesToExplore;
        }

        for (int node : nodesToExplore) {
            MOVE moveToPPill = Moves.towards(game, node, ppill, MOVE.NEUTRAL);
            newPositions.add(new FuzzyValue<>(
                    new Target(node, moveToPPill),
                    FuzzyValue.MAX_CONFIDENCE));
        }
        return newPositions;
    }


    private void updateProtectorGhostsAssignment(Game game) {
        for (GHOST ghost : GHOST.values()) {
            Optional<GHOST> protector = getMostLikelyPosition()
                    .map(FuzzyValue::getValue)
                    .flatMap(p -> assignProtectorGhost(game, ghost, p.getNode(), p.getLastMove()));

            protectorGhostForEachGhost.put(ghost, protector);
        }
    }

    private Optional<GHOST> assignProtectorGhost(Game game, GHOST ghost, int indexPacman,
            MOVE lastMovePac) {
        for (GHOST otherGhost : GHOST.values())
            if (otherGhost != ghost
                    && game.isGhostEdible(ghost)
                    && game.getGhostLairTime(ghost) <= 0
                    && game.getGhostLairTime(otherGhost) <= 0
                    && !game.isGhostEdible(otherGhost)) {

                boolean otherGhostCloserToThisGhostThanPacmanToThisGhost =
                        PathDistance.fromGhostToGhost(game, otherGhost, ghost) < PathDistance
                                .toGhost(game, indexPacman, lastMovePac, ghost);

                boolean pacmanCloserToThisGhostThanPacmanToOtherGhost =
                        PathDistance.toGhost(game, indexPacman, lastMovePac,
                                ghost) < PathDistance
                                        .toGhost(game, indexPacman, lastMovePac, otherGhost);

                boolean otherGhostCloserToThisGhostThanOtherGhostToPacman =
                        PathDistance.fromGhostToGhost(game, otherGhost, ghost) < PathDistance
                                .fromGhostTo(game, otherGhost, indexPacman);

                if (otherGhostCloserToThisGhostThanPacmanToThisGhost
                        && pacmanCloserToThisGhostThanPacmanToOtherGhost
                        && otherGhostCloserToThisGhostThanOtherGhostToPacman)
                    return Optional.of(otherGhost);
            }
        return Optional.empty();
    }


    private void updateClosestPowerPillToMsPacman(Game game) {
        Optional<Target> pacman = getMostLikelyPosition().map(FuzzyValue::getValue);

        if (pacman.isPresent()) {
            int origin = pacman.get().getNode();
            MOVE lastMoveOrigin = pacman.get().getLastMove();

            ToIntFunction<PillStatus> distToPPill =
                    pp -> PathDistance.fromXtoX(game, origin, lastMoveOrigin, pp.getNode());

            Comparator<PillStatus> comparator =
                    (a, b) -> distToPPill.applyAsInt(a) - distToPPill.applyAsInt(b);

            closestPowerPillToMsPacman = getPowerPillsStatus().stream()
                    .map(FuzzyValue::getValue)
                    .filter(PillStatus::isAvailable)
                    .min(comparator)
                    .map(PillStatus::getNode);
        } else {
            closestPowerPillToMsPacman = Optional.empty();
        }

    }
}
