package es.ucm.fdi.ici.c2122.practica3.grupo07;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public final class Util {
	//Rojo -> eatPacman junctions edition
	//Green -> eatPacman common
	//Cyan path -> Pacman escape paths when eatPacman junctions is active
	//Pink -> enclose Pacman
	public final static DM path_measure = DM.PATH;
	public final static boolean DEBUG = true;
	private Util() {
	}
	public static MOVE bestPathToDie(Game game) {
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove=game.getPacmanLastMoveMade();
		MOVE move=MOVE.NEUTRAL;
		int max=0;
		for(MOVE m:game.getPossibleMoves(pacman, pacmanLastMove)) {
			int cont=0;
			int nextLocation = getNextLocation(game,pacman,pacmanLastMove);
			int[] path=game.getShortestPath(pacman, nextLocation, pacmanLastMove);
			for(int p:path) {
				int aux=game.getPillIndex(p);
				if(aux!=-1&&game.isPillStillAvailable(aux)) {
					cont++;
				}
			}
			if(cont>max) {
				max=cont;
				move=m;
			}
		}
		return move;
	}
	/**
	 * 
	 * @param game
	 * @param i1
	 * @param m1
	 * @param objetive
	 * @param i2
	 * @param m2
	 * @return can i2 intercept the path from i1 to objetive
	 */
	public static int interceptJunction(Game game, int i1, MOVE m1, int objetive, int i2, MOVE m2) {

		int []path=game.getShortestPath(i1, objetive, m1);
		for(int p:path) {
			if(game.isJunction(p)) {
				double dist1=game.getDistance(i1, p, m1, path_measure);
				double dist2=game.getDistance(i2, p, m2, path_measure);
				if(dist2<dist1) {
					return p;
				}
			}
		}
		return -1;
	}
	public static int pathWithPowerPill(Game game,int[] path) {
		int pp = -1;
		for(int i = 0;i < path.length && pp == -1  ;i++){
			int idx = game.getPowerPillIndex(path[i]);
			if(idx != -1 && game.isPowerPillStillAvailable(idx)) {
				pp = path[i];
			}
		}	
		return pp;
	}
	public static boolean inTheSameDirection(Game game,GHOST g, int end) {
		boolean same_direction = false;
		int pm_node = game.getPacmanCurrentNodeIndex();
		MOVE pm_move = game.getPacmanLastMoveMade();
		int g_node = game.getGhostCurrentNodeIndex(g);
		MOVE g_move = game.getGhostLastMoveMade(g);
		
		int[] pm_path = game.getShortestPath(pm_node, end, pm_move);
		int[] g_path = game.getShortestPath(g_node, end, g_move);
		int contador = 0;
		for(int pm : pm_path) {
			for(int gp : g_path) {
				if(gp == pm)
					contador++;
			}
			if(contador > 2) {
				same_direction = true;
				break;
			}
		}
	return same_direction;	
	}
	//Check if all ghosts coordined can trap and kill pacman 100%
	public static resul canEatPacmanv2(Game game) {
		int pm_node = game.getPacmanCurrentNodeIndex();
		MOVE pm_move = game.getPacmanLastMoveMade();
		MOVE move_to_enter_junction = pm_move;
		int main_junction;
		if(game.isJunction(pm_node))
			main_junction = pm_node;
		else {
			main_junction = Util.getNextLocation(game, pm_node, pm_move);
			int[] path = game.getShortestPath(pm_node, main_junction, pm_move);
			if(path.length != 1)
				move_to_enter_junction = game.getMoveToMakeToReachDirectNeighbour(path[path.length-2], main_junction);	
		}
		 
		ArrayList<Integer> n_left = new ArrayList<Integer>();
		int[] nearby_nodes = game.getNeighbouringNodes(main_junction, move_to_enter_junction);
		for(int i = 0; i < nearby_nodes.length; i++) {
			n_left.add(Util.getNextLocation(game, nearby_nodes[i], game.getMoveToMakeToReachDirectNeighbour(main_junction, nearby_nodes[i])));
			int path[] = game.getShortestPath(pm_node, nearby_nodes[i], pm_move);
			int pp = pathWithPowerPill(game, path);
			if(pp != -1)
				n_left.set(i, pp);
		}
		
		ArrayList<GHOST> g_left = new ArrayList<GHOST>();
		ArrayList<GHOST> g_in_position = new ArrayList<GHOST>();
		
		for(GHOST ghost: GHOST.values()) {
			if(!game.isGhostEdible(ghost)) {
				int g_node = game.getGhostCurrentNodeIndex(ghost);
				if(n_left.contains(g_node)){
					n_left.remove(n_left.indexOf(g_node));
					g_in_position.add(ghost);
				}else
					g_left.add(ghost);
				
			}
		}
		resul solution = enclose100(game,g_left,n_left);
		if(g_in_position.size() > 0 && solution.getPosible()) {
			for(int i = 0; i < g_in_position.size();i++) {
				GHOST g = g_in_position.get(i);
				GHOST[] ghost_resul = new GHOST[1]; ghost_resul[0] = g;
				int[] nodes_resul = new int[1]; 
				if(game.getGhostCurrentNodeIndex(g) == main_junction)
					nodes_resul[0] = pm_node;
				else
					nodes_resul[0] = main_junction;
				
				ghost_resul = concatenate(ghost_resul, solution.getGhosts());
				nodes_resul = concatenate(nodes_resul, solution.getNodes());
				

			}
			
		}
		return solution;
	}
	public static class resul{
		private boolean possible;
		private GHOST[] ghosts;
		private int[] nodes;
		
		resul(boolean p){possible = p;}
		resul(GHOST[] g, int[] n){possible = true; ghosts = g; nodes = n;}
		
		public boolean getPosible() {return possible;}
		public int[] getNodes() { return nodes;}
		public GHOST[] getGhosts() { return ghosts;}
	}

	public static resul enclose100(Game game, ArrayList<GHOST> g_left, ArrayList<Integer> n_left){
		int pm_node = game.getPacmanCurrentNodeIndex();
		MOVE pm_move = game.getPacmanLastMoveMade();
		GHOST[] ghost_resul;
		int[] nodes_resul;
		
		if(n_left.size() == 1 && g_left.size() >= 1) {	//Solo queda un nodo para encerrar a pacman.
			for(GHOST g : g_left) {
				int g_node = game.getGhostCurrentNodeIndex(g);
				if(game.getGhostLairTime(g) > 0)
					g_node = game.getGhostInitialNodeIndex();
				MOVE g_move = game.getGhostLastMoveMade(g);
				
				int g_distance = (int) game.getDistance(g_node, n_left.get(0), g_move, path_measure) + game.getGhostLairTime(g);
				int pm_distance = (int) game.getDistance(pm_node, n_left.get(0), pm_move, path_measure);
				
				if(g_distance < pm_distance && !inTheSameDirection(game,g,n_left.get(0))) {
					
					ghost_resul = new GHOST[1]; ghost_resul[0] = g;
					nodes_resul = new int[1]; nodes_resul[0] = n_left.get(0);
					
					return new resul(ghost_resul,nodes_resul);
					
				}
			}
		}else if(n_left.size() <= g_left.size()) {
				for(GHOST g : g_left) {
					int g_node = game.getGhostCurrentNodeIndex(g);
					if(game.getGhostLairTime(g) > 0)
						g_node = game.getGhostInitialNodeIndex();
					MOVE g_move = game.getGhostLastMoveMade(g);
					
					GHOST[] cur_ghost = new GHOST[1];
					cur_ghost[0] = g;
					
					ArrayList<GHOST> cur_g_free = new ArrayList<GHOST>(g_left);
					cur_g_free.remove(g);
					
					for(int n : n_left) {
						
						int[] cur_node = new int[1];
						cur_node[0] = n;
						
						ArrayList<Integer> cur_nodes_free = new ArrayList<Integer>(n_left);
						cur_nodes_free.remove((Integer)n);
						
						int g_distance = (int) game.getDistance(g_node, n, g_move, path_measure) + game.getGhostLairTime(g);
						int pm_distance = (int) game.getDistance(pm_node, n, pm_move, path_measure);
						
						if(g_distance < pm_distance && !inTheSameDirection(game,g,n_left.get(0))) {
							resul solution = enclose100(game,cur_g_free,cur_nodes_free);
							if(solution.getPosible()) {
								//ghost_resul = new GHOST[1]; ghost_resul[0] = g;
								//nodes_resul = new int[1]; nodes_resul[0] = n_left.get(0);
								
								ghost_resul = concatenate(cur_ghost, solution.getGhosts());
								nodes_resul = concatenate(cur_node, solution.getNodes());
								
								return new resul(ghost_resul,nodes_resul);
							}
						}
					}
					
					
				}
			}
		
		return new resul(false);
	}
	//Aux functions to concatenate 2 arrays
	public static <T> T[] concatenate(T[] array1, T[] array2) {
		T[] result = Arrays.copyOf(array1, array1.length + array2.length);
	    System.arraycopy(array2, 0, result, array1.length, array2.length);
	    return result;
	}
	
	public static int[] concatenate(int[] array1, int[] array2) {
		int[] result = Arrays.copyOf(array1, array1.length + array2.length);
	    System.arraycopy(array2, 0, result, array1.length, array2.length);
	    return result;
	}
	
	public static MOVE canEatPacman(Game game,GHOST g) {
		MOVE move=MOVE.NEUTRAL;
		if(game.getGhostLairTime(g)>0)return move;
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		int nextLocation= getNextLocation(game,pacman,pacmanLastMove);
		int[]path=game.getShortestPath(pacman, nextLocation, pacmanLastMove);
		for(int p:path) {
			int pp=game.getPowerPillIndex(p);
			if(pp!=-1&&game.isPowerPillStillAvailable(pp)) {
				break;
			}
			int ghost=game.getGhostCurrentNodeIndex(g);
			MOVE ghostLastMove=game.getGhostLastMoveMade(g);
			double ghostDistance=game.getDistance(ghost, p, ghostLastMove, path_measure)+game.getGhostEdibleTime(g);
			double pacmanDistance=game.getDistance(pacman, p, pacmanLastMove, path_measure);
			if(ghostDistance<pacmanDistance) {
				return game.getApproximateNextMoveTowardsTarget(ghost, p, ghostLastMove, path_measure);
			}
		}
		return move;
	}
	public static int getNextIntersection(Game game, int indice, MOVE lastMove) {
		while(true) {
			if(game.getNeighbour(indice, lastMove)==-1||game.isJunction(indice)) {
				return indice;
			}
			indice=game.getNeighbour(indice, lastMove);
		}
	}
	public static int getNextLocation(Game game, int indice, MOVE lastMove) {
		if(lastMove==MOVE.NEUTRAL)return indice;
		while(true) {
			indice=getNextIntersection(game,indice,lastMove);
			if(game.isJunction(indice)) {
				return indice;
			}
			int newidx=game.getNeighbouringNodes(indice, lastMove)[0];
			lastMove=game.getMoveToMakeToReachDirectNeighbour(indice, newidx);
			indice=newidx;
		}
	}

	public static boolean areFacingEachOther(Game game, int n1, MOVE m1, int n2, MOVE m2) {
		boolean ans=true;
		int[] path1=game.getShortestPath(n1, n2, m1);
		int[] path2=game.getShortestPath(n2, n1, m2);
		if(path1.length!=path2.length)return false;
		if(path1.length<2)return true;
		for(int i=0;i<path1.length-1;i++) {
			if(path1[i]!=path2[(path2.length-2)-i])return false;
		}
		return ans;
	}
	public static int getMinDistanceNode(Game game, int node, int[] nodes, MOVE lastMove) {
		double minDistance = Integer.MAX_VALUE;
		int target = -1;

        for (int i = 0; i < nodes.length; i++) {
            double distance = 0;
            distance = game.getDistance(node, nodes[i],lastMove,DM.PATH);

            if (distance < minDistance) {
                minDistance = distance;
                target = nodes[i];
            }
        }

        return target;
	}
	public static boolean inPath(ArrayList<Integer>path,int idx) {
		for(int p:path) {
			if(idx==p)return true;
		}
		return false;
	}
	public static ArrayList<MOVE>getSafeMoves(Game game){
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		if(!game.isJunction(pacman)) {
			return new ArrayList<>();
		}
		ArrayList<MOVE>moves=new ArrayList<>();
		
		//create paths
		ArrayList<ArrayList<Integer>>paths=new ArrayList<ArrayList<Integer>>();
		
		for(MOVE m:game.getPossibleMoves(pacman, pacmanLastMove)) {
			int nei=game.getNeighbour(pacman, m);
			int nextLocation = Util.getNextLocation(game, nei, m);
			int[] path = game.getShortestPath(nei, nextLocation, m);
			ArrayList<Integer>tmp=new ArrayList<Integer>();
			tmp.add(nei);
			for(int p:path) {
				tmp.add(p);
			}
			MOVE newLastMove=game.getApproximateNextMoveTowardsTarget(path[path.length-2], pacman,game.getMoveToMakeToReachDirectNeighbour(path[path.length-1], path[path.length-2]), DM.PATH).opposite();
			for(MOVE m2:game.getPossibleMoves(nextLocation, newLastMove)) {
				int nei2=game.getNeighbour(nextLocation, m2);
				int nextLocation2 = Util.getNextLocation(game, nei2, m2);
				int[] path2 = game.getShortestPath(nei2, nextLocation2, m2);
				ArrayList<Integer>tmp2=new ArrayList<Integer>();
				for(int t:tmp) {
					tmp2.add(t);
				}
				tmp2.add(nei2);
				for(int p:path2) {
					tmp2.add(p);
				}
				paths.add(tmp2);
			}
		}
		
		/*
		//Visualize paths
		for(ArrayList<Integer>pth:paths) {
			int[]aux=new int[pth.size()];
			for(int i=0;i<pth.size();i++) {
				aux[i]=pth.get(i);
			}
			GameView.addPoints(game, Color.RED, aux);
		}*/
		
		//get save moves
		for(ArrayList<Integer>pth:paths) {
			HashMap<GHOST,Boolean>inP=new HashMap<GHOST,Boolean>();
			for(GHOST g:GHOST.values()) {inP.put(g, false);}
			boolean safe=true;
			for(int p:pth) {
				double pacmanDistance=game.getDistance(pacman, p, pacmanLastMove, DM.PATH);
				int pp=game.getPowerPillIndex(p);
				if(pp!=-1&&game.isPowerPillStillAvailable(pp)) {
					moves.add(game.getMoveToMakeToReachDirectNeighbour(pacman, pth.get(0)));
					int[]aux=new int[pth.size()];
					for(int ii=0;ii<pth.size();ii++) {
						aux[ii]=pth.get(ii);
					}
					//GameView.addPoints(game, Color.BLUE, aux);
					break;
				}
				
				for(GHOST g:GHOST.values()) {
					if(inP.get(g))continue;
					if(game.getGhostLairTime(g)==0) {
						
						int ghost=game.getGhostCurrentNodeIndex(g);
						MOVE ghostLastMove = game.getGhostLastMoveMade(g);
						double ghostDistance=game.getDistance(ghost, p, ghostLastMove, DM.PATH);
						
						if(p==ghost) {
							inP.put(g, true);
							if(areFacingEachOther(game,pacman,pacmanLastMove,ghost,ghostLastMove)) {
								if(!game.isGhostEdible(g)||((2*ghostDistance)>=game.getGhostEdibleTime(g))) {
									safe=false;
									break;
								}
								
							}
						}
						if(game.isGhostEdible(g)) {
							double dist= game.getDistance(pacman, p, pacmanLastMove, DM.PATH);
							double enc=(2*dist)/3;
							if(game.getGhostEdibleTime(g)<=enc) {
								safe=false;
								break;
							}
							/*
							//will ghost remain edible at point p
							if((2*ghostDistance)<game.getGhostEdibleTime(g)) {
									
							}
							else {
								safe=false;
								break;
							}
							*/
							
						}
						else {
							if(ghostDistance<=pacmanDistance) {
								safe=false;
								break;
							}
						}
						
				
						
					}
					
				}
				if(!safe)break;
				if(p==pth.get(pth.size()-1)) {
					moves.add(game.getMoveToMakeToReachDirectNeighbour(pacman, pth.get(0)));
					int[]aux=new int[pth.size()];
					for(int ii=0;ii<pth.size();ii++) {
						aux[ii]=pth.get(ii);
					}
					//GameView.addPoints(game, Color.RED, aux);
				}
			}
			
		}
		
		
		ArrayList<MOVE>finalMoves=new ArrayList<MOVE>();
		for(MOVE m:moves) {
			boolean enc=false;
			for(MOVE m2:finalMoves) {
				if(m==m2)enc=true;
			}
			if(!enc)finalMoves.add(m);
		}
		return finalMoves;
	}
	public static boolean isReachable(Game game, GHOST ghost) {
		boolean aux=false;
		int pacman=game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		ArrayList<MOVE>safeMoves=getSafeMoves(game);
		for(MOVE m:safeMoves) {
			int nextLocation = game.getNeighbour(pacman, m);
			for(GHOST g:GHOST.values()) {
				if(game.getGhostLairTime(g)<=0) {
					int ghostIndex=game.getGhostCurrentNodeIndex(g);
					MOVE ghostLastMove = game.getGhostLastMoveMade(g);
					double distancia=game.getDistance(nextLocation, ghostIndex, m, path_measure)+1;
					
					if((distancia*1.0)<game.getGhostEdibleTime(g)) {
						
						//GameView.addLines(game, Color.RED, pacman, ghostIndex);
						aux=true;
					}
				}
				
				
			}
			
		}
		return aux;
	}
	public enum ACTIONPADRE{
		HUNT,RUN,PROTECT,WAIT;
	}
	public enum ACTION{
		INSTAWIN {
			public String toString() {
				return "insta win";
			};
			public ACTIONPADRE getFather() {
                return ACTIONPADRE.HUNT;
            };
		},
		INTERCEPTPP{
            public String toString() {
                return "intercept pp";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.HUNT;
            };
        },
		INTERCEPTP{
            public String toString() {
                return "intercept p";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.HUNT;
            };
        },
		ENCLOSE{
            public String toString() {
                return "enclose pacman";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.HUNT;
            };
  
        },
		CHASE{
            public String toString() {
                return "chase pacman";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.HUNT;
            };
        },
		RUNAWAY{
            public String toString() {
                return "run away";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.RUN;
            };
        },
		BECAUTIOUS{
            public String toString() {
                return "be cautious";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.RUN;
            };
        },
		DISPERSARSE{
            public String toString() {
                return "dispersarse";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.RUN;
            };
        },
		SUICIDE{
            public String toString() {
                return "suicide";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.RUN;
            };
        },
		CUTTHEWAY{
            public String toString() {
                return "cut the way";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.PROTECT;
            };
        },
		GOTOGHOST{
            public String toString() {
                return "go to ghost";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.PROTECT;
            };
        },
		GOTOPACMAN{
            public String toString() {
                return "go to pacman";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.PROTECT;
            };
        },
		SEPARATE{
            public String toString() {
                return "separate";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.HUNT;
            };
        },
		WAIT{
            public String toString() {
                return "separate";
            };
            public ACTIONPADRE getFather() {
                return ACTIONPADRE.WAIT;
            };
        },
		;
		public abstract String toString();
		public abstract ACTIONPADRE getFather();
	
	}
}
