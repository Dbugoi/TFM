package es.ucm.fdi.ici.c2122.practica3.grupo07;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Vector;
import java.awt.Color;
import es.ucm.fdi.ici.c2122.practica3.grupo07.Util.resul;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class GhostsInput extends RulesInput{
	
	private final int TOO_FAR_FROM_PM = 100;	//GHOSTS farther away from pacman than this distance will be ignored for protecting purpouses
	
	private EnumMap<GHOST,Boolean> interceptPP;
	private EnumMap<GHOST,MOVE> eatPacman;
	private EnumMap<GHOST,Boolean> edible;
	private EnumMap<GHOST,GHOST> defender;
	private EnumMap<GHOST,MOVE> dontChaseOthers;
	private EnumMap<GHOST,Integer> protect;
	private EnumMap<GHOST,Boolean> offensivebusy;
	private EnumMap<GHOST,Integer> enclosePacman;
	
	private int pm_node;
	private MOVE pm_move;
	
	int closestpp;
	boolean someoneEdible;
	public GhostsInput(Game game) {
		super(game);
		
		parseInput();
	}
	

	@Override
	public void parseInput() {
		init();
		if(game.getNumberOfActivePowerPills()>0){
			standard_attack();
		}
		protect_edibles();
		pick_defenders();
		dont_chase_others();
	}
	
	private void init() {
		someoneEdible=false;
		pm_node = game.getPacmanCurrentNodeIndex();
		pm_move = game.getPacmanLastMoveMade();
		
		interceptPP = new EnumMap<GHOST,Boolean>(GHOST.class);
		eatPacman = new EnumMap<GHOST,MOVE>(GHOST.class);
		edible = new EnumMap<GHOST,Boolean>(GHOST.class);
		defender = new EnumMap<GHOST,GHOST>(GHOST.class);
		dontChaseOthers = new EnumMap<GHOST,MOVE>(GHOST.class);
		offensivebusy = new EnumMap<GHOST,Boolean>(GHOST.class);
		protect = new EnumMap<GHOST,Integer>(GHOST.class);
		enclosePacman = new EnumMap<GHOST,Integer>(GHOST.class);
		
		for(GHOST g:GHOST.values()) {
			dontChaseOthers.put(g, MOVE.NEUTRAL);
			eatPacman.put(g, Util.canEatPacman(game, g));
			interceptPP.put(g, false);
			protect.put(g, -1);
			enclosePacman.put(g, -1);
			
			edible.put(g, game.isGhostEdible(g));
			
			if(game.isGhostEdible(g)) {
				someoneEdible = true;
			}
			if(game.getGhostLairTime(g) > 0 || game.isGhostEdible(g)) {
				offensivebusy.put(g, true);
			}
			else {
				offensivebusy.put(g, false);
			}
		}
	}
	
	private void standard_attack() {
		closestpp = game.getClosestNodeIndexFromNodeIndex(pm_node, game.getActivePowerPillsIndices(), Util.path_measure);
		ArrayList<Integer>junctions = new ArrayList<Integer>();
		int path[] = game.getShortestPath(pm_node, closestpp, pm_move);
		//GameView.addPoints(game, Color.CYAN, path);
		for(int p:path) {
			if(game.isJunction(p)) {
				junctions.add(p);
			}
		}
		boolean chk = false;
		lALL:
		for(int j:junctions) {
			for(GHOST g:GHOST.values()) {
				int ghost=game.getGhostCurrentNodeIndex(g);
				MOVE ghostLastMove=game.getGhostLastMoveMade(g);
				if(!chk) {
					double ghostDistance=game.getDistance(ghost, j, ghostLastMove, Util.path_measure);
					double pacmanDistance=game.getDistance(pm_node, j, pm_move, Util.path_measure);
					if(pacmanDistance>ghostDistance) {
						interceptPP.put(g, true);
						offensivebusy.put(g, true);
						chk=true;
						break lALL;
					}
				}
			}
		}
		enclose_pacman();
		
	}
	//Aux class to represent a ghost that is running away from pacman. pacman is at a "distance" distance from the ghost and 
	private class running_ghost{
		private GHOST ghost;
		private int node;	//Next location of the ghost
		private int distance;

		public running_ghost(GHOST g, int n, int d) {ghost = g;node = n;distance = d;}
		
		public GHOST getGhost() {return ghost;}
		public int getNode() {return node;}
		public int getDistance() {return distance;}
	}
	
	private ArrayList<running_ghost> order_edible_ghosts() {
		ArrayList<running_ghost> edible_ghosts = new ArrayList<running_ghost>();
		for(GHOST g: GHOST.values()) {
			if(game.isGhostEdible(g)) {
				int g_node = game.getGhostCurrentNodeIndex(g);
				MOVE g_move = game.getGhostLastMoveMade(g);
				int next_node = g_node;
				if(!game.isJunction(g_node))	//Pequeña ineficiencia porque aun no sabemos hacia donde va a huir en caso de que este en un cruce
					next_node =  Util.getNextLocation(game, g_node, g_move);
				
				int pm_g_distance = (int) game.getDistance(pm_node,next_node, pm_move, Util.path_measure);
				
				edible_ghosts.add(new running_ghost(g,next_node,pm_g_distance));
			}
		}
		ArrayList<running_ghost> edible_ghosts_left = new ArrayList<running_ghost>(edible_ghosts);
		ArrayList<running_ghost> edible_ghosts_ordered = new ArrayList<running_ghost>();
		for(int i = 0; i < edible_ghosts.size();i++) {
			running_ghost closest_ghost = null;
			for(running_ghost run : edible_ghosts_left) {
				if(closest_ghost == null || closest_ghost.getDistance() > run.getDistance())
					closest_ghost = run;
			}
			edible_ghosts_left.remove(closest_ghost);
			edible_ghosts_ordered.add(i, closest_ghost);
		}
		return edible_ghosts_ordered;
	}
	private void protect_edibles() {
		
		ArrayList<running_ghost> edible_ghosts = order_edible_ghosts();
		ArrayList<GHOST> protectors = new ArrayList<GHOST>();
		for(GHOST g: GHOST.values()) {
			if(!game.isGhostEdible(g))
				protectors.add(g);
		}
		
		for(running_ghost run : edible_ghosts) {
			int intercept_node = -1;
			GHOST best_ghost = null;
			
			for(GHOST g_protecc: protectors) {
				int g_prot_node = game.getGhostCurrentNodeIndex(g_protecc);
				if(game.getGhostLairTime(g_protecc) > 0)
					g_prot_node = game.getGhostInitialNodeIndex();
				
				MOVE g_prot_move = game.getGhostLastMoveMade(g_protecc);
	
				int g_run_pm_distance = game.getShortestPathDistance(pm_node, game.getGhostCurrentNodeIndex(run.getGhost()), pm_move);
				if(g_run_pm_distance <= TOO_FAR_FROM_PM) {
					int[] path = game.getShortestPath(pm_node, run.getNode(), pm_move);
					for(int n : path) {
						int pm_distance;
						int g_distance;
						boolean pp_available = false;
						
						int idx = game.getPowerPillIndex(n);
						if(idx != -1 && game.isPowerPillStillAvailable(idx))
							pp_available = true;
						
						pm_distance = game.getShortestPathDistance(pm_node,n,pm_move);
						g_distance = game.getShortestPathDistance(g_prot_node,n, g_prot_move);
						
						if(g_distance < pm_distance) {
							intercept_node = n;
							best_ghost = g_protecc;;
							break;
						}
						if(pp_available)
							break;
	
					}
				}
			}
			if(intercept_node != -1) {
				protect.put(best_ghost,intercept_node);
				protectors.remove(best_ghost);
			}
		}
	}
	private void pick_defenders() {
		for(GHOST g:GHOST.values()) {
			if(game.getGhostLairTime(g)<=0&&game.isGhostEdible(g)) {
				int ghost=game.getGhostCurrentNodeIndex(g);
				MOVE ghostLastMove=game.getGhostLastMoveMade(g);
				ArrayList<int[]>paths=new ArrayList<int[]>();
				ArrayList<GHOST>ghosts=new ArrayList<GHOST>();
				for(GHOST pg:GHOST.values()) {
					if(game.getGhostLairTime(pg)<=0&&!game.isGhostEdible(pg)) {
						int pghost=game.getGhostCurrentNodeIndex(pg);
						paths.add(game.getShortestPath(ghost, pghost, ghostLastMove));
						ghosts.add(pg);
					}
				}
				GHOST selectedGHOST=null;
				for(int i=0;i<paths.size();i++) {
					int[]path=paths.get(i);
					for(int p:path) {
						if(game.isJunction(p)) {
							double ghostDistance=game.getDistance(ghost, p, ghostLastMove, Util.path_measure);
							double pacmanDistance=game.getDistance(pm_node, p, pm_move, Util.path_measure);
							if(pacmanDistance<ghostDistance) {
								break;
							}
						}
						if(p==path[path.length-1]) {
							selectedGHOST=ghosts.get(i);
						}
					}
				}
				if(selectedGHOST!=null) {
					defender.put(g, selectedGHOST);
					if(g == GHOST.BLINKY)
						protect.put(g, 50);	
				}
				else {
					//cambiar por algo mejor
					
				}
				
			}
			
		}
	}
	

	
	private void dont_chase_others() {
		for(GHOST g:GHOST.values()) {
			if(game.getGhostLairTime(g)<=0) {
				int ghost = game.getGhostCurrentNodeIndex(g);
				MOVE ghostLastMove = game.getGhostLastMoveMade(g);
				int nextLocation = Util.getNextLocation(game,ghost,ghostLastMove.opposite());
				for(GHOST g2:GHOST.values()) {
					if(!((game.isGhostEdible(g)&&!game.isGhostEdible(g2))||(game.isGhostEdible(g2)&&!game.isGhostEdible(g)))&&(g!=g2&&game.doesGhostRequireAction(g2)&&game.getGhostLairTime(g)<=0)) {
						int ghost2 = game.getGhostCurrentNodeIndex(g2);
						MOVE ghostLastMove2 = game.getGhostLastMoveMade(g2);
						if(nextLocation==ghost2) {
							this.dontChaseOthers.put(g2, game.getApproximateNextMoveTowardsTarget(ghost2, ghost, ghostLastMove2, Util.path_measure));
						}
					}
					
				}
			}
			
		}
	}
	//Function that prepare the data to use divide_nodes to enclose all available junctions to pacman
	private void enclose_pacman() {
		int[] nodes;
		int pm_node = game.getPacmanCurrentNodeIndex();
		MOVE pm_move = game.getPacmanLastMoveMade();
		int pm_nextJunction = Util.getNextLocation(game, pm_node, pm_move);
		
		int[]nearby_nodes = game.getNeighbouringNodes(pm_nextJunction,pm_move);
		
		if(nearby_nodes == null) 
			return;
		
		nodes = new int[nearby_nodes.length];
		
		for(int i = 0;i<nearby_nodes.length;i++) {
			nodes[i]= Util.getNextLocation(game,nearby_nodes[i], game.getMoveToMakeToReachDirectNeighbour(pm_nextJunction, nearby_nodes[i]));
		}
		//Nodes hold the info of the junctions available to pacman once he reach his direct junction in front of him

		if(nodes != null && nodes.length > 1) {
			ArrayList<GHOST> g_free = new ArrayList<GHOST>();
			for(GHOST g: GHOST.values()) {
				if(!game.isGhostEdible(g))
					g_free.add(g);
			}
			ArrayList<Integer> nodes_free = new ArrayList<Integer>();
			for(int n: nodes) {
				nodes_free.add(n);
			}
			
			ghost_order resul = divide_nodes(game,g_free,nodes_free);
			
			GHOST[] g_order = resul.getGhosts();
			int[] n_order = resul.getNodes();
			for(int i = 0; i < g_order.length;i++) {
				enclosePacman.put(g_order[i], n_order[i]);
			}
		}
	}
	
	private class ghost_order{
		int distance;
		GHOST[] order;
		int[] nodes;
		ghost_order(GHOST[] ghosts,int dist,int[] n){distance = dist;order=ghosts;nodes = n;}
		public int getDistance() { return distance;}
		public int[] getNodes() { return nodes;}
		public GHOST[] getGhosts() { return order;}
	}
	//Apply recursion to divide the nodes array between the ghost array to minimize the distance between ghosts and nodes.
	private ghost_order divide_nodes(Game game,ArrayList<GHOST> g_free,ArrayList<Integer> nodes_free) {
		GHOST[] ghost;
		int[] nodes = null;
		//Base Case where theres only one node left. We pick the closest ghost to that node
		if(nodes_free.size() == 1 && g_free.size() >= 1) { 
			int min_distance = Integer.MAX_VALUE;
			GHOST g_closest = null;
			for(GHOST g: g_free) {
				int g_distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nodes_free.get(0), game.getGhostLastMoveMade(g));
				if(g_distance < min_distance) {
					min_distance = g_distance;
					g_closest = g;
				}
			}
			
			ghost = new GHOST[1]; ghost[0] = g_closest;
			nodes = new int[1]; nodes[0] = nodes_free.get(0);
			
			return new ghost_order(ghost,min_distance,nodes);
		}
		//Base case where theres only one ghost left. We pick the closest node to that ghost
		if(g_free.size() == 1 && nodes_free.size() > 1) { 
			int min_distance = Integer.MAX_VALUE;
			GHOST g_closest = g_free.get(0);
			int node = -2;
			for(int n : nodes_free) {
				int g_distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g_closest), n, game.getGhostLastMoveMade(g_closest));
				if(g_distance < min_distance) {
					min_distance = g_distance;
					node = n;
				}
			}
			
			ghost = new GHOST[1]; ghost[0] = g_closest;
			nodes = new int[1]; nodes[0] = node;
			return new ghost_order(ghost,min_distance,nodes);
		}
		//Recursive case. We pick a ghost and a node, divide the nodes left between the ghosts left and check if the new distance calculated is better
		ghost = new GHOST[g_free.size()];
		int min_distance = Integer.MAX_VALUE;
		for(GHOST g: g_free) {
			
			GHOST[] cur_ghost = new GHOST[1];
			cur_ghost[0] = g;
			
			ArrayList<GHOST> cur_g_free = new ArrayList<GHOST>(g_free);
			cur_g_free.remove(g);
			
			for(int n : nodes_free) {
				
				int[] cur_node = new int[1];
				cur_node[0] = n;
				
				ArrayList<Integer> cur_nodes_free = new ArrayList<Integer>(nodes_free);
				cur_nodes_free.remove((Integer)n);
				
				int cur_distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), n, game.getGhostLastMoveMade(g));
				
				ghost_order resul = divide_nodes(game,cur_g_free,cur_nodes_free);
				cur_distance += resul.getDistance();
				
				if(cur_distance < min_distance) {
					min_distance = cur_distance;
					ghost = Util.concatenate(cur_ghost, resul.getGhosts());
					nodes = Util.concatenate(cur_node, resul.getNodes());
				}
			}

		}
		return new ghost_order(ghost,min_distance,nodes);
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(BLINKY (interceptPP %s)(eatPacman %s)(edible %s)(defender %s)(dontchaseothers %s)(protect_node %d)(enclose_node %d))", 
				interceptPP.get(GHOST.BLINKY),eatPacman.get(GHOST.BLINKY), edible.get(GHOST.BLINKY), defender.get(GHOST.BLINKY), dontChaseOthers.get(GHOST.BLINKY), protect.get(GHOST.BLINKY), enclosePacman.get(GHOST.BLINKY)));
		
		facts.add(String.format("(INKY (interceptPP %s)(eatPacman %s)(edible %s)(defender %s)(dontchaseothers %s)(protect_node %d)(enclose_node %d))", 	 
				interceptPP.get(GHOST.INKY),  eatPacman.get(GHOST.INKY),   edible.get(GHOST.INKY),   defender.get(GHOST.INKY),   dontChaseOthers.get(GHOST.INKY),   protect.get(GHOST.INKY),  enclosePacman.get(GHOST.INKY)));
		
		facts.add(String.format("(PINKY (interceptPP %s)(eatPacman %s)(edible %s)(defender %s)(dontchaseothers %s)(protect_node %d)(enclose_node %d))",  
				interceptPP.get(GHOST.PINKY), eatPacman.get(GHOST.PINKY),  edible.get(GHOST.PINKY),  defender.get(GHOST.PINKY),  dontChaseOthers.get(GHOST.PINKY),  protect.get(GHOST.PINKY), enclosePacman.get(GHOST.PINKY)));
		
		facts.add(String.format("(SUE (interceptPP %s)(eatPacman %s)(edible %s)(defender %s)(dontchaseothers %s)(protect_node %d)(enclose_node %d))",	 
				interceptPP.get(GHOST.SUE),   eatPacman.get(GHOST.SUE),    edible.get(GHOST.SUE), 	  defender.get(GHOST.SUE), 	  dontChaseOthers.get(GHOST.SUE),    protect.get(GHOST.SUE),  enclosePacman.get(GHOST.SUE)));
		
		facts.add(String.format("(MSPACMAN (closestPP %d)(numPP %d)(someoneedible %s))", 
				closestpp,game.getNumberOfActivePowerPills(),someoneEdible));

		return facts;
	}
}
