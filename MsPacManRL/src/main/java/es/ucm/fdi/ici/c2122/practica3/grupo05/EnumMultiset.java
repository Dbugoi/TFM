package es.ucm.fdi.ici.c2122.practica3.grupo05;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Optional;
import java.util.stream.Stream;
import pacman.game.Constants.MOVE;


public final class EnumMultiset {
  private final EnumMap<MOVE, Integer> map;

  public static EnumMultiset create() {
    return new EnumMultiset();
  }

  private EnumMultiset() {
    map = new EnumMap<>(MOVE.class);
    for (MOVE move : MOVE.values())
      map.put(move, 0);
  }

  public void addAll(Collection<MOVE> moves) {
    for (MOVE move : moves) {
      map.put(move, map.get(move) + 1);
    }
  }

  public int count(MOVE m) {
    return map.get(m);
  }

  public Stream<MOVE> stream() {
    return Arrays.stream(MOVE.values());
  }

  public static Optional<MOVE> getMostRepeatedMove(EnumMultiset movesAwayFromTargetsMultiSet) {
      return movesAwayFromTargetsMultiSet.stream()
              .max((a, b) -> movesAwayFromTargetsMultiSet.count(a)
                      - movesAwayFromTargetsMultiSet.count(b));
  }
}
