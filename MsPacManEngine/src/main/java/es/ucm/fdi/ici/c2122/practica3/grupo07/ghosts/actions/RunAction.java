package es.ucm.fdi.ici.c2122.practica3.grupo07.ghosts.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo07.Util;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAction implements RulesAction {
	
	GHOST ghost;
	public RunAction(GHOST ghost) {
		this.ghost=ghost;
	}
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return ghost+"run";
	}

	@Override
	public MOVE execute(Game game) {
		return runaway(game,this.ghost);
	}

	@Override
	public void parseFact(Fact actionFact) {
	
	}
	
	private MOVE runaway(Game game, GHOST g) {
		MOVE new_move = MOVE.NEUTRAL;
		
		if(game.getGhostLairTime(g) > 0 )
			return new_move;
		
		int pm_node = game.getPacmanCurrentNodeIndex();
		MOVE pm_move = game.getPacmanLastMoveMade();
		
		int g_node = game.getGhostCurrentNodeIndex(g);
		MOVE g_move = game.getGhostLastMoveMade(g);
		
		int[] nearby_nodes = game.getNeighbouringNodes(g_node, g_move);
		int[] junctions = new int[nearby_nodes.length];
		int max_distance = Integer.MIN_VALUE;
		
		for(int i = 0;i<nearby_nodes.length;i++) {
			junctions[i]= Util.getNextLocation(game,nearby_nodes[i], game.getMoveToMakeToReachDirectNeighbour(g_node, nearby_nodes[i]));
			int[] path = game.getShortestPath(g_node, junctions[i], g_move);
			if(Util.pathWithPowerPill(game, path) == -1) {
				boolean pm_is_here = false;
				for(int p: path) {
					if(p == pm_node) {
						pm_is_here = true;
						break;
					}
				}
				int run_distance = game.getShortestPathDistance(pm_node, junctions[i], pm_move);
				if(pm_is_here) {
					int pm_to_g = game.getShortestPathDistance(pm_node,g_node, pm_move);
					int g_to_pm = game.getShortestPathDistance(pm_node,g_node, pm_move);
					
					if(pm_to_g == g_to_pm) //PacMan is going to eat the ghost
						run_distance = pm_to_g;
					else {	//Ghost is behind PacMan
						run_distance = run_distance + game.getShortestPathDistance(junctions[i],path[path.length-2], game.getMoveToMakeToReachDirectNeighbour(path[path.length-2], path[path.length-1]));
					}
				}
				if(run_distance > max_distance) {
					max_distance = run_distance;
					new_move = game.getMoveToMakeToReachDirectNeighbour(g_node, nearby_nodes[i]);
				}
			}
		}
		return new_move;
		
	}
}
