package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.PillStatus;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Paths;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManFuzzyMemory {
	HashMap<String,Double> mem;
	
	double[] confidence = {0,0,0,0};
	double[] edible = {0,0,0,0};
	double[] ultimaPos = {-1,-1,-1,-1};
	double[] CAGEconfidence = {-1,-1,-1,-1};
	double[] pillConfidence ={0,0,0,0};
	private  TreeMap<Double, Integer> pillPos =new TreeMap<>();
	private  Map<Integer, Double> pillPosMap = new HashMap<>();
	
	private  MsPacManInput input;
	private static final Map<GHOST, Integer> initialLairTime;

	static {
		initialLairTime = new EnumMap<>(GHOST.class);
		initialLairTime.put(GHOST.BLINKY, 40);
		initialLairTime.put(GHOST.PINKY, 60);
		initialLairTime.put(GHOST.INKY, 80);
		initialLairTime.put(GHOST.SUE, 100);
	}


	private int currentLevel = -1;
	private int lastTick = -1;
	private final Map<GHOST, SortedSet<FuzzyValue<Target>>> ghostsPositions;
	private  TreeMap<Double, Integer> power_pillPos =new TreeMap<>();
	private  Map<Integer, Double> power_pillPosMap = new HashMap<>();
	private Map<GHOST, Integer> edibleTime;
	private Map<GHOST, Integer> ultimaPosVisible;
	private Map<GHOST, Double> cageConfidence;
	private Map<GHOST, MOVE> ultimoMov;
	
	public MsPacManFuzzyMemory() {
		mem = new HashMap<String,Double>();
		
		ghostsPositions = new EnumMap<>(GHOST.class);
		edibleTime = new EnumMap<>(GHOST.class);
		ultimaPosVisible = new EnumMap<>(GHOST.class);
		cageConfidence = new EnumMap<>(GHOST.class);
		ultimoMov = new EnumMap<>(GHOST.class);
		for (GHOST ghost : GHOST.values()) {
			ghostsPositions.put(ghost, new TreeSet<>(FuzzyValue.fuzzyComparator()));
			edibleTime.put(ghost, 0);
			cageConfidence.put(ghost, FuzzyValue.MAX_CONFIDENCE);
		}
		
		
	}
	
	public void getInput(MsPacManInput input)
	{
		this.input=input;
		for(GHOST g: GHOST.values()) {
			
			Game game = input.getGame();
			if (game.getCurrentLevel() != currentLevel) {
				currentLevel = game.getCurrentLevel();
				lastTick = game.getTotalTime();
				reset(game);
			} else if (game.getTotalTime() != lastTick) {
				lastTick = game.getTotalTime();
				updateMemory(game);
			}
			
			/*
			double conf = confidence[g.ordinal()];
			if(input.isVisible(g))
				conf = 100;
			else
				conf = Double.max(0, conf-5);
			mem.put(g.name()+"confidence", conf);	
			
			
			mem.put(g.name()+"edible", input.getEdible(g));
			mem.put(g.name()+"ultimaPos", Double.max(-1, input.getUltimaPos(g)));
			
			if(CAGEconfidence[g.ordinal()]!=-1 && input.getCageConfidence(g)==-1) {
				CAGEconfidence[g.ordinal()] -= 1;
				input.updateCageConfidence(g, CAGEconfidence[g.ordinal()]);
			}
			else if(input.getCageConfidence(g)!=-1)
				CAGEconfidence[g.ordinal()]= input.getCageConfidence(g);
			
	
			mem.put(g.name()+"CAGEconfidence", CAGEconfidence[g.ordinal()]);
			*/
		}
		
		
			
		getNearestPills(input);
		if(!pillPos.isEmpty())
			mem.put("nearestPill", Double.valueOf( pillPos.firstKey()));
		else
			mem.put("nearestPill", Double.valueOf( 1000));
		
		getNearestPowerPills(input);
		if(!power_pillPos.isEmpty())
			mem.put("nearestPowerPill", Double.valueOf( power_pillPos.firstKey()));
		else
			mem.put("nearestPowerPill", Double.valueOf( 1000));

	}
	
	public Map<String, Double> getFuzzyValues() {
		return mem;
	}
	
	private void getNearestPills(MsPacManInput input) {

		pillPos.clear();
		Game game = input.getGame();
        for (int pillIndex : game.getActivePillsIndices()) {
        	
        	if(pillIndex!=-1) {
	            double dist = PathDistance.fromPacmanTo(game, pillIndex);
	            
	           pillPosMap.put(pillIndex, dist);
	           pillPos.put(dist, pillIndex);
	            
        	}

        }
        
        Set<Integer> s= new HashSet<>();
        s.addAll(pillPosMap.keySet());
        
        for(int pillIndex : s) {
        	double dist;
        	try {
        		if(pillIndex!=1293) {
               	 dist = PathDistance.fromPacmanTo(game, pillIndex);
               	if(game.wasPillEaten()) {
               		if(!pillPos.containsValue(pillIndex) && game.isNodeObservable(pillIndex))
               				pillPosMap.remove(pillIndex);
               	}
               	if(!pillPos.containsValue(pillIndex) && pillPosMap.containsKey(pillIndex))
               		pillPos.put(dist, pillIndex);
               }
			} catch (IndexOutOfBoundsException e) {
				dist = 500;
				if(game.wasPillEaten()) {
               		if(!pillPos.containsValue(pillIndex) && game.isNodeObservable(pillIndex))
               				pillPosMap.remove(pillIndex);
               	}
               	if(!pillPos.containsValue(pillIndex) && pillPosMap.containsKey(pillIndex))
               		pillPos.put(dist, pillIndex);
               }
			}
        	
        
       
	}
	
	private void getNearestPowerPills(MsPacManInput input) {

		power_pillPos.clear();
		Game game = input.getGame();
        for (int powerPillIndex : game.getActivePowerPillsIndices()) {
        	
        	if(powerPillIndex!=-1) {
	            double dist = PathDistance.fromPacmanTo(game, powerPillIndex);
	            
	            power_pillPosMap.put(powerPillIndex, dist);
	           power_pillPos.put(dist, powerPillIndex);
	            
        	}

        }
        
        Set<Integer> s= new HashSet<>();
        s.addAll(power_pillPosMap.keySet());
        
        for(int powerPillIndex : s) {
        	 double dist = PathDistance.fromPacmanTo(game, powerPillIndex);
        	if(game.wasPowerPillEaten()) {
        		if(!power_pillPos.containsValue(powerPillIndex) && game.isNodeObservable(powerPillIndex))
        			power_pillPosMap.remove(powerPillIndex);
        	}
        	if(!power_pillPos.containsValue(powerPillIndex) && power_pillPosMap.containsKey(powerPillIndex))
        		power_pillPos.put(dist, powerPillIndex);
        	
        	
        }
        
       
	}
	
	public Optional<FuzzyValue<Target>> getMostLikelyPosition(GHOST ghost) {
		if (ghostsPositions.get(ghost).isEmpty())
			return Optional.empty();
		else
			return Optional.of(ghostsPositions.get(ghost).last());
	}

	public int getLastVisiblePosition(GHOST ghost) {
		if(input.getUltimaPos(ghost)==-1)
			return ultimaPosVisible.get(ghost);
		return input.getUltimaPos(ghost);
		
	}


	public MOVE getLastMove(GHOST ghost) {
		
		return ultimoMov.get(ghost);		
	}
	
	public SortedSet<FuzzyValue<Target>> getPositions(GHOST ghost) {
		return Collections.unmodifiableSortedSet(ghostsPositions.get(ghost));
	}

	public Map<Double, Integer> getPillsVisible() {
		return Collections.unmodifiableMap(pillPos);
	}
	
public Integer getNearestPill() {
	
		return pillPos.firstEntry().getValue();
	}
	public Map<Double, Integer> getPowerPillsVisible() {
		return Collections.unmodifiableMap(power_pillPos);
	}

	public Integer getNearestPP() {
		
		return power_pillPos.firstEntry().getValue();
	}
	public Map<Integer, Double> getPills() {
		return Collections.unmodifiableMap(pillPosMap);
	}

	public Map<GHOST, Integer> getEdibleTime() {
		return Collections.unmodifiableMap(edibleTime);
	}


	public Map<GHOST, Double> getCageConfidence() {
		return Collections.unmodifiableMap(cageConfidence);
	}

	private void reset(Game game) {
		for (GHOST ghost : GHOST.values()) {
			ghostsPositions.get(ghost).clear();
			ghostsPositions.get(ghost).add(new FuzzyValue<>(
					new Target(Paths.lairNode(game), MOVE.NEUTRAL),
					FuzzyValue.MIN_CONFIDENCE));
			ultimoMov.put(ghost,  MOVE.NEUTRAL);
			ultimaPosVisible.put(ghost, Paths.lairNode(game));
			cageConfidence.put(ghost, initialLairTime.get(ghost) * 2.5);
			edibleTime.put(ghost, 0);
		}
		
		// reset powerpills
		for(int indexPP: game.getPowerPillIndices())
		{  double dist = PathDistance.fromPacmanTo(game, indexPP);
			power_pillPos.put(dist, indexPP);
			power_pillPosMap.put(indexPP, dist);
			
		}
		
		// reset pills
	}


	private void updateMemory(Game game) {
		for (GHOST ghost : GHOST.values()) {
			int node = game.getGhostCurrentNodeIndex(ghost);

			// fantasma visible
			if (isNodeVisible(node)) {
				ghostsPositions.get(ghost).clear();
				ghostsPositions.get(ghost).add(new FuzzyValue<>(
						new Target(node, game.getGhostLastMoveMade(ghost)),
						FuzzyValue.MAX_CONFIDENCE));
				ultimoMov.put(ghost,  game.getGhostLastMoveMade(ghost));
				edibleTime.put(ghost, game.getGhostEdibleTime(ghost));
				cageConfidence.put(ghost, FuzzyValue.MIN_CONFIDENCE);
				ultimaPosVisible.put(ghost, node);
			}

			// fantasma comido y por alguna razón no visible, o pacman comido
			else if (game.wasGhostEaten(ghost) || game.wasPacManEaten()) {
				ghostsPositions.get(ghost).clear();
				ghostsPositions.get(ghost).add(new FuzzyValue<>(
						new Target(Paths.lairNode(game), MOVE.NEUTRAL),
						FuzzyValue.MAX_CONFIDENCE));
				edibleTime.put(ghost, 0);
				cageConfidence.put(ghost, FuzzyValue.MAX_CONFIDENCE);
				ultimaPosVisible.put(ghost, Paths.lairNode(game));
			}

			// fantasma no está en la cárcel
			else if (!isInCage(ghost)) {
				if (game.wasPowerPillEaten()) {
					edibleTime.put(ghost, 200);
				} else if (edibleTime.get(ghost) > 0) {
					edibleTime.put(ghost, edibleTime.get(ghost) - 1);
				}

				if (doesRequireAction(ghost)) {
					advanceAllPossiblePositions(game, ghost);
				}
			}

			// fantasma sí está en la cárcel
			else {
				cageConfidence.put(ghost, Math.max(cageConfidence.get(ghost) - 2.5, 0.0));
				if (!isInCage(ghost)) {
					ghostsPositions.get(ghost).clear();
					ghostsPositions.get(ghost).add(new FuzzyValue<>(
							new Target(Paths.CAGE, MOVE.NEUTRAL),
							FuzzyValue.MAX_CONFIDENCE));
					ultimaPosVisible.put(ghost, Paths.CAGE);
					ultimoMov.put(ghost,  MOVE.NEUTRAL);
				}
			}
		}
	}

	private boolean doesRequireAction(GHOST ghost) {
		return !isInCage(ghost) && (edibleTime.get(ghost) == 0 || edibleTime.get(ghost) % 2 != 0);
	}

	private boolean isInCage(GHOST ghost) {
		return cageConfidence.get(ghost) > 0;
	}

	private boolean isNodeVisible(int node) {
		return node != -1;
	}


	private void advanceAllPossiblePositions(Game game, GHOST ghost) {
		List<FuzzyValue<Target>> newPositions = new LinkedList<>();

		for (FuzzyValue<Target> fuzzyTarget : ghostsPositions.get(ghost)) {
			Target target = fuzzyTarget.getValue();
			int[] neighbours = game.getNeighbouringNodes(target.getNode(), target.getLastMove());
			double newConfidence = fuzzyTarget.getConfidence() / neighbours.length;

			if (nodeIsInteresting(game, target.getNode(), newConfidence))
				for (int neigh : neighbours) {
					MOVE moveToNode =
							game.getMoveToMakeToReachDirectNeighbour(target.getNode(), neigh);
					newPositions
							.add(new FuzzyValue<>(new Target(neigh, moveToNode), newConfidence));
				}
		}
		int i;
		ghostsPositions.get(ghost).clear();
		ghostsPositions.get(ghost).addAll(newPositions);
		if(!ghostsPositions.get(ghost).isEmpty())
			ultimoMov.put(ghost,  ghostsPositions.get(ghost).first().getValue().getLastMove());
		else
			i=0;
		updateConfidences(ghost);
	}

	private boolean nodeIsInteresting(Game game, int node, double newConfidence) {
		return newConfidence > 1.0 && !game.isNodeObservable(node);
	}


	private void updateConfidences(GHOST ghost) {
		double totalConfidence = ghostsPositions.get(ghost)
				.stream()
				.mapToDouble(FuzzyValue::getConfidence)
				.sum();
		// Reajustamos las confianzas para que vuelvan a sumar 100%
		ghostsPositions.get(ghost).forEach(ghPos -> ghPos.setConfidence(
				ghPos.getConfidence() * (FuzzyValue.MAX_CONFIDENCE / totalConfidence)));
	}
}
