package es.ucm.fdi.ici.c2122.practica4.grupo05.mspacman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.Moves;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.FuzzyValue;
import es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy.Target;
import es.ucm.fdi.ici.c2122.practica4.grupo05.utils.PacmanMoves;
import es.ucm.fdi.ici.fuzzy.FuzzyInput;

public class MsPacManInput extends FuzzyInput {

	
	private int[] ultimaPos;
	private MOVE[] ultimoMov;
	
	private final MsPacManFuzzyMemory memory;

	public MsPacManInput(Game game , MsPacManFuzzyMemory memory) {
		super(game);
		this.memory = memory;
	}
	
	@Override
	public void parseInput() {

		if(ultimaPos == null)
		{
			
			ultimaPos = new int[]{-1,-1,-1,-1};
			ultimoMov = new MOVE[]{MOVE.NEUTRAL,MOVE.NEUTRAL,MOVE.NEUTRAL,MOVE.NEUTRAL};
	
		}
		
		for(GHOST g: GHOST.values()) {
			int index = g.ordinal();
			int pos = game.getGhostCurrentNodeIndex(g);
						
			if(pos != -1) {	
				ultimaPos[index] = pos;
			} 
		
				
		}
		
		

	}
	


	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		
	
		for (GHOST g : GHOST.values()) {
			Optional<FuzzyValue<Target>> fGhost = memory.getMostLikelyPosition(g);
			double distance;
			double confidence;
			int node;
			MOVE lastM;
			if (fGhost.isPresent()) {
				node = fGhost.get().getValue().getNode();
				confidence = fGhost.get().getConfidence();
				lastM = fGhost.get().getValue().getLastMove();
				ultimoMov[g.ordinal()] = lastM;
				try {
					distance = PathDistance.fromPacmanTo(game, node);
				} catch (IndexOutOfBoundsException e) {
					distance = 500;
				}
			} else {
				node = memory.getLastVisiblePosition(g);	
				lastM = memory.getLastMove(g);
				distance = 500;
				confidence = 0;
			}
			
			vars.put(g.name() + "distance", distance);
			vars.put(g.name() + "confidence", confidence);
			vars.put(g.name() + "edible", (double) memory.getEdibleTime().get(g));
			ultimaPos[g.ordinal()] = node;
			vars.put(g.name() + "ultimaPos", (double) node);
			if(game.getNextMoveTowardsTarget(node, game.getPacmanCurrentNodeIndex(), DM.PATH)!=null) {
			
				if(lastM.equals(game.getNextMoveTowardsTarget(node, game.getPacmanCurrentNodeIndex(), DM.PATH)))
					vars.put(g.name() + "isChasing", (double) 60);
				else
					vars.put(g.name() + "isChasing", (double) 30);
			}
			else
				vars.put(g.name() + "isChasing", (double) 30);
		
			vars.put(g.name() + "CAGEconfidence", memory.getCageConfidence().get(g));
			
			
		}

		return vars;
	}
	
	
	public void  updateUltimaPos(GHOST ghost, int d)
	{
		ultimaPos[ghost.ordinal()]= d;
	}

	public int  getUltimaPos(GHOST ghost)
	{
		return ultimaPos[ghost.ordinal()];
	}
	
	public MOVE  getLasMove(GHOST ghost)
	{
		return ultimoMov[ghost.ordinal()];
	}
	
}
