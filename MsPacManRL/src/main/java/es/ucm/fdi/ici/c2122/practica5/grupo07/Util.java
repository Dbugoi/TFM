package es.ucm.fdi.ici.c2122.practica5.grupo07;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class Util {
	public static List<?> convertObjectToList(Object obj) {
	    List<?> list = new ArrayList<>();
	    if (obj.getClass().isArray()) {
	        list = Arrays.asList((Object[])obj);
	    } else if (obj instanceof Collection) {
	        list = new ArrayList<>((Collection<?>)obj);
	    }
	    return list;
	}
	public static Double getDanger(Game game, int idx, MOVE move) {
		Double danger=0.0;
		
		int nei=game.getNeighbour(idx, move);
		ArrayList<Integer> path = Util.get2MergedPath(game, idx, move);
		//for(Integer t:path) {
		//	GameView.addPoints(game, Color.RED, t);
		//}
		HashMap<GHOST,Boolean>inP=new HashMap<GHOST,Boolean>();
		for(GHOST g:GHOST.values()) {inP.put(g, false);}
		boolean safe=true;
		for(int p:path) {
			//double pacmanDistance=game.getDistance(idx, p, move, DM.PATH);
			double pacmanDistance=game.getDistance(nei, p, move, DM.PATH)+1;

			for(GHOST g:GHOST.values()) {
				if(inP.get(g))continue;
				if(game.getGhostLairTime(g)==0) {
					
					int ghost=game.getGhostCurrentNodeIndex(g);
					MOVE ghostLastMove = game.getGhostLastMoveMade(g);
					double ghostDistance=game.getDistance(ghost, p, ghostLastMove, DM.PATH);
					
					if(p==ghost) {
						inP.put(g, true);
						if(areFacingEachOther(game,nei,move,ghost,ghostLastMove)) {
							if(!game.isGhostEdible(g)||((2*ghostDistance)>=game.getGhostEdibleTime(g))) {
								danger+=100.0;
								safe=false;
								break;
							}
							
						}
					}
					if(game.isGhostEdible(g)) {
						double enc=(2*pacmanDistance)/3;
						if(game.getGhostEdibleTime(g)<=enc) {
							danger+=100.0;
							safe=false;
							break;
						}
						
					}
					else {
						if(Math.abs(ghostDistance-pacmanDistance)<=Constants.EAT_DISTANCE) {
							danger+=100.0;
							safe=false;
							break;
						}
					}
					
			
					
				}
				
			}
			int pp=game.getPowerPillIndex(p);
			if(pp!=-1&&game.isPowerPillStillAvailable(pp)) {
				danger=0.0;
				break;
			}
			if(!safe)break;
		}
		return danger;
	}
	public static Double getDanger34(Game game, int idx, MOVE move) {
		Double danger=0.0;
		if(!game.isJunction(idx))return danger;
		int nei=game.getNeighbour(idx, move);
		ArrayList<Integer> path = Util.getFourPathsMerged(game, idx, MOVE.NEUTRAL).get(move);
		if(path==null) {
			int a=0;
		}
		//for(Integer t:path) {
		//	GameView.addPoints(game, Color.RED, t);
		//}
		HashMap<GHOST,Boolean>inP=new HashMap<GHOST,Boolean>();
		for(GHOST g:GHOST.values()) {inP.put(g, false);}
		boolean safe=true;
		for(int p:path) {
			//double pacmanDistance=game.getDistance(idx, p, move, DM.PATH);
			double pacmanDistance=game.getDistance(nei, p, move, DM.PATH)+1;

			for(GHOST g:GHOST.values()) {
				if(inP.get(g))continue;
				if(game.getGhostLairTime(g)==0) {
					
					int ghost=game.getGhostCurrentNodeIndex(g);
					MOVE ghostLastMove = game.getGhostLastMoveMade(g);
					double ghostDistance=game.getDistance(ghost, p, ghostLastMove, DM.PATH);
					
					if(p==ghost) {
						inP.put(g, true);
						if(areFacingEachOther(game,nei,move,ghost,ghostLastMove)) {
							if(!game.isGhostEdible(g)||((2*ghostDistance)>=game.getGhostEdibleTime(g))) {
								danger+=50.0;
								safe=false;
								break;
							}
							
						}
					}
					if(game.isGhostEdible(g)) {
						double enc=(2*pacmanDistance)/3;
						if(game.getGhostEdibleTime(g)<=enc) {
							danger+=50.0;
							safe=false;
							break;
						}
						
					}
					else {
						if(Math.abs(ghostDistance-pacmanDistance)<=Constants.EAT_DISTANCE) {
							danger+=50.0;
							safe=false;
							break;
						}
					}
					
			
					
				}
				
			}
			int pp=game.getPowerPillIndex(p);
			if(pp!=-1&&game.isPowerPillStillAvailable(pp)) {
				danger=0.0;
				break;
			}
			if(!safe)break;
		}
		return danger;
	}
	public static boolean inPath(ArrayList<Integer>path, int idx) {
		for(Integer p:path) {
			if(p==idx)return true;
		}
		return false;
	}
	public static Double getEdible(Game game, int idx, MOVE move) {
		Double edible=0.0;
		
		int nei=game.getNeighbour(idx, move);
		ArrayList<Integer> path = Util.get2MergedPath(game, idx, move);

		
		for(GHOST g:GHOST.values()) {
			if(game.getGhostLairTime(g)==0) {
				
				int ghost=game.getGhostCurrentNodeIndex(g);
				MOVE ghostLastMove = game.getGhostLastMoveMade(g);
				
				if(game.getGhostEdibleTime(g)>=((game.getDistance(idx, ghost, move, DM.PATH))/3)) {
					if(Util.inPath(path, ghost)&&areFacingEachOther(game,nei,move,ghost,ghostLastMove)) {
						
						edible+=50;
					}
				}
			}
		}
		for(int p:path) {
			int pp=game.getPowerPillIndex(p);
			if(pp!=-1&&game.isPowerPillStillAvailable(pp)) {
				edible+=20.0;
			}
			int pl=game.getPillIndex(p);
			if(pl!=-1&&game.isPillStillAvailable(pl)) {
				edible+=5.0;
			}
		}
		return edible;
	}
	public static ArrayList<Integer> get2MergedPath(Game game, int idx, MOVE move) {
		
		
		ArrayList<Integer>idxs=new ArrayList<Integer>();
		
		if(!game.isJunction(idx))return idxs;
		
		int nei=game.getNeighbour(idx, move);
		int nextLocation=Util.getNextLocation(game, nei, move);
		int[]path = game.getShortestPath(nei, nextLocation, move);
		idxs.add(nei);
		for(int p:path) {
			idxs.add(p);
		}
		
		MOVE newLastMove=game.getApproximateNextMoveTowardsTarget(path[path.length-2], nei,game.getMoveToMakeToReachDirectNeighbour(path[path.length-1], path[path.length-2]), DM.PATH).opposite();
		
		for(MOVE m:game.getPossibleMoves(nextLocation, newLastMove)) {
			int nei2=game.getNeighbour(nextLocation, m);
			int nextLocation2=Util.getNextLocation(game, nei2, m);
			int[]path2 = game.getShortestPath(nei2, nextLocation2, m);
			idxs.add(nei2);
			for(int p:path2) {
				idxs.add(p);
			}
		}
		return idxs;
	}
	public static ArrayList<MOVE> getSafeMoves(Game game){
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
	public static Double getEdible34(Game game, int idx, MOVE move) {
		Double edible=0.0;
		if(!game.isJunction(idx))return edible;

		int nei=game.getNeighbour(idx, move);
		ArrayList<Integer> path = Util.getFourPathsMerged(game, idx, MOVE.NEUTRAL).get(move);

		
		for(GHOST g:GHOST.values()) {
			if(game.getGhostLairTime(g)==0) {
				
				int ghost=game.getGhostCurrentNodeIndex(g);
				MOVE ghostLastMove = game.getGhostLastMoveMade(g);
				
				if(game.getGhostEdibleTime(g)>=((game.getDistance(idx, ghost, move, DM.PATH))/3)) {
					if(Util.inPath(path, ghost)&&areFacingEachOther(game,nei,move,ghost,ghostLastMove)) {
						
						edible+=50;
					}
				}
			}
		}
		for(int p:path) {
			int pp=game.getPowerPillIndex(p);
			if(pp!=-1&&game.isPowerPillStillAvailable(pp)) {
				edible+=20.0;
			}
			int pl=game.getPillIndex(p);
			if(pl!=-1&&game.isPillStillAvailable(pl)) {
				edible+=5.0;
			}
		}
		return edible;
	}
	public static EnumMap<MOVE,ArrayList<Integer>>getFourPathsMerged(Game game, int idx, MOVE lastMove){
		
		
		//create paths
		EnumMap<MOVE,ArrayList<Integer>>twopaths=new EnumMap<MOVE,ArrayList<Integer>>(MOVE.class);
		EnumMap<MOVE,ArrayList<Integer>>fourpaths=new EnumMap<MOVE,ArrayList<Integer>>(MOVE.class);
		
		
		//init 2paths and 4paths
		int node1=idx;
		for(MOVE m:game.getPossibleMoves(node1, lastMove)) {
			fourpaths.put(m, new ArrayList<Integer>());
			twopaths.put(m, new ArrayList<Integer>());
			int nei=game.getNeighbour(node1, m);
			int nextLocation = Util.getNextLocation(game, nei, m);
			int[] path1 = game.getShortestPath(nei, nextLocation, m);
			twopaths.get(m).add(nei);
			for(int p:path1) {
				twopaths.get(m).add(p);
			}
			//System.out.println(path1.length);
			MOVE newLastMove=game.getApproximateNextMoveTowardsTarget(path1[path1.length-2], node1,game.getMoveToMakeToReachDirectNeighbour(path1[path1.length-1], path1[path1.length-2]), DM.PATH).opposite();
			//ArrayList<Integer>tmp=get1dPath(game,nextLocation,newLastMove);
			//twopaths.get(m).addAll(tmp);
			for(MOVE m2:game.getPossibleMoves(nextLocation, newLastMove)) {
				int nei2=game.getNeighbour(nextLocation, m2);
				int nextLocation2 = Util.getNextLocation(game, nei2, m2);
				int[] path2 = game.getShortestPath(nei2, nextLocation2, m2);
				twopaths.get(m).add(nei2);
				for(int p:path2) {
					twopaths.get(m).add(p);
				}
				MOVE newLastMove2=game.getApproximateNextMoveTowardsTarget(path2[path2.length-2], nextLocation2,game.getMoveToMakeToReachDirectNeighbour(path2[path2.length-1], path2[path2.length-2]), DM.PATH).opposite();

				for(MOVE m3:game.getPossibleMoves(nextLocation2, newLastMove2)) {
					int nei3=game.getNeighbour(nextLocation2, m3);
					int nextLocation3 = Util.getNextLocation(game, nei3, m3);
					int[] path3 = game.getShortestPath(nei3, nextLocation3, m3);
					fourpaths.get(m).add(nei3);
					for(int p:path3) {
						fourpaths.get(m).add(p);
					}
					MOVE newLastMove3=game.getApproximateNextMoveTowardsTarget(path3[path3.length-2], nextLocation3,game.getMoveToMakeToReachDirectNeighbour(path3[path3.length-1], path3[path3.length-2]), DM.PATH).opposite();
					for(MOVE m4:game.getPossibleMoves(nextLocation3, newLastMove3)) {
						int nei4=game.getNeighbour(nextLocation3, m4);
						int nextLocation4 = Util.getNextLocation(game, nei4, m4);
						int[] path4 = game.getShortestPath(nei4, nextLocation4, m4);
						fourpaths.get(m).add(nei4);
						for(int p:path4) {
							fourpaths.get(m).add(p);
						}

					}
				}
			}
		}
		
		//4paths-2paths
		for(MOVE m:game.getPossibleMoves(node1, lastMove)) {
			/*ArrayList<Integer>toDelete=new ArrayList<Integer>();
			for(Integer two:twopaths.get(m)) {
				for(Integer four:fourpaths.get(m)) {
					if(two==four) {
						toDelete.add(two);
						break;
					}
				}
			}*/
			for(MOVE m2:game.getPossibleMoves(node1, lastMove)) {
				fourpaths.get(m).removeAll(twopaths.get(m2));
			}
			
			/*for(Integer d:toDelete) {
				if(fourpaths.get(m).contains(d)) {
					fourpaths.get(m).remove(d);
					
				}
			}*/
		}
		for(MOVE m:game.getPossibleMoves(node1, lastMove)) {
			fourpaths.put(m, removeDuplicates(fourpaths.get(m)));
		}
		
		/*for(MOVE m:game.getPossibleMoves(node1, lastMove)) {
			for(Integer p:fourpaths.get(m)) {
				GameView.addPoints(game, Color.RED, p);
			}
		}*/
		
		return fourpaths;
		
		
	}
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list){
		ArrayList<T> newlist=new ArrayList<T>();
		for(T e:list) {
			if(!newlist.contains(e)) {
				newlist.add(e);
			}
		}
		return newlist;
	}
}
