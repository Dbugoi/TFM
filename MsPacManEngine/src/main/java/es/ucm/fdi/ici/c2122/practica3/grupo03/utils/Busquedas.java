package es.ucm.fdi.ici.c2122.practica3.grupo03.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Busquedas {
	
	public static Set<Integer> predecirPacManEnMovs(int movs, Game g){
		
		int pacManNode = g.getPacmanCurrentNodeIndex();
		MOVE lastPacManMove = g.getPacmanLastMoveMade();
		
		int[] junctions = g.getJunctionIndices();
		List<Pair<Integer,Double>> lj = new ArrayList<Pair<Integer,Double>>();
		for(int j : junctions) {
			lj.add(new Pair<Integer,Double>(j, g.getDistance(pacManNode, j, lastPacManMove, DM.PATH)));
		}
		
		lj.sort(new Comparator<Pair<Integer,Double>>(){
			@Override
			public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
				// TODO Auto-generated method stub
				return o1.getSecond().compareTo(o2.getSecond());
			}
		});
		
		int i = 0;
		boolean encontrado = false;
		while(i < lj.size()-1 && !encontrado) {
			encontrado = lj.get(i).getSecond() <= movs &&
						 lj.get(i+1).getSecond() >= movs;
			i++;
		}
		
		int j1 = lj.get(i).getFirst();
		if(i+1 >= lj.size()) {
			i = lj.size() - 2; 
		}
		int j2 = lj.get(i+1).getFirst();
		
		int[] path = g.getShortestPath(j1, j2);
		
		Set<Integer> s = new HashSet<Integer>();
		
		if(path.length > 0) {
			int j = 0;
			boolean distCorrecta = false;
			while(j < path.length && !distCorrecta) {
				distCorrecta = movs == g.getShortestPathDistance(pacManNode, path[j], lastPacManMove);
				j++;
			}
			
			if((j >= path.length) && (path.length != 0)) {
				j = path.length - 1; 
			}
			s.add(path[j]);
		}
		else {
			s.add(j1);
		}
		
		
		return s;
	}
	
	public static Collection<PairMove<Integer,Integer>> nextCloseJunctions(int distance, int node, int limit, MOVE lastMove, Game g){
		
		int[] vecinos = g.getNeighbouringNodes(node, lastMove);
		ArrayList<PairMove<Integer,Integer>> l = new ArrayList<PairMove<Integer,Integer>>();
		
		for(int i : vecinos) {
			l.add(new PairMove<Integer,Integer>(i, distance+1 , g.getMoveToMakeToReachDirectNeighbour(node, i)));
		}
		
		for(PairMove<Integer,Integer> p: l) {
			int actnode = p.getFirst();
			int dist = p.getSecond();
			MOVE last = p.getMove();
			
			//Avanzar por el pasillo hasta
			//    Que la distancia es la que estoy buscando
			//    Que llego a una junction
			int[] vec = g.getNeighbouringNodes(actnode, last);
			while(!(g.isJunction(actnode)) && dist <= limit && vec.length == 1) {
				
				// Sacar el nodo al que llego siguiendo el pasillo
				last = g.getMoveToMakeToReachDirectNeighbour(actnode, vec[0]);
				actnode = vec[0];
				dist++;
				
				vec = g.getNeighbouringNodes(actnode, last);
				
			}
			
			p.setFirst(actnode);
			p.setSecond(dist);
			p.setMove(last);
		}
		
		return l;
	}
	
	public static Set<Integer> predecirFantasmaEnMovs(int movs, GHOST f, Game g){
		
		int ghostNode = g.getGhostCurrentNodeIndex(f);
		MOVE lastGhostMove = g.getGhostLastMoveMade(f);
		
		Set<Integer> posicionesFinales = new HashSet<Integer>();
		
		Queue<PairMove<Integer,Integer>> q = new PriorityQueue<PairMove<Integer,Integer>>(new Comparator<PairMove<Integer,Integer>>(){

			@Override
			public int compare(PairMove<Integer, Integer> arg0, PairMove<Integer, Integer> arg1) {
				// TODO Auto-generated method stub
				int d1 = arg0.getSecond();
				int d2 = arg0.getSecond();
				if(d1 < d2) { return -1; }
				else if (d1 == d2 ) { return 0; }
				else { return 1; }
			}
			
		});
		
		q.add(new PairMove<Integer,Integer>(ghostNode, 0, lastGhostMove));
		
		Set<Integer> cerrados = new HashSet<Integer>();
		
		while(!q.isEmpty()) {
			
			PairMove<Integer, Integer> p = q.remove();
			
			if(p.getSecond() == movs && !posicionesFinales.contains(p.getFirst())) {
				posicionesFinales.add(p.getFirst());
			}
			else {
				
				int[] vecinos = g.getNeighbouringNodes(p.getFirst(), p.getMove());
				
				for(int i = 0; i < vecinos.length; i++) {
					if(!cerrados.contains(vecinos[i])) {
						q.add(new PairMove<Integer,Integer>(vecinos[i], p.getSecond()+1 , g.getMoveToMakeToReachDirectNeighbour(p.getFirst(), 
								vecinos[i])));
					}
				}
				
			}
		}
		
		return posicionesFinales;
		
	}

	public static Pair<Integer, Boolean> getNearestPowerPillToNode(Game g, int node, MOVE lastMove) {
		int[] powerPillNodes = g.getPowerPillIndices();
		
		Pair<Integer,Boolean> ret = new Pair<Integer,Boolean>(-1,false);
		double mindist = Double.MAX_VALUE;
		
		for(int i = 0; i < powerPillNodes.length; i++) {
			double d = g.getDistance(node, powerPillNodes[i], lastMove , DM.PATH);
			if (d < mindist) {
				mindist = d;
				ret.setFirst(powerPillNodes[i]);
				ret.setSecond(true);
			}
		}
		
		return ret;
	}
	
	public static boolean isNodeCloserThan (Game g, int nodeOrigin, int nodeDest, double limit, MOVE lastMove) {
		double dist = g.getDistance(nodeOrigin, nodeDest, lastMove, DM.PATH);
		return dist <= limit;
		
	}
	
	public static MOVE moveToCutPacManPathInNMovements(Game game, GHOST ghost, int movs) {
		
		int ghostNode = game.getGhostCurrentNodeIndex(ghost);
		int pacManNode = game.getPacmanCurrentNodeIndex();
		MOVE ghostLastMove = game.getGhostLastMoveMade(ghost);
		
		Set<Integer> posiciones = Busquedas.predecirPacManEnMovs(movs, game);
		
		Set<Integer> posOrd = new TreeSet<Integer>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				double d1 = game.getDistance(ghostNode, pacManNode, ghostLastMove, DM.PATH);
				double d2 = game.getDistance(ghostNode, pacManNode, ghostLastMove, DM.PATH);
			
				if(d1 < d2) {
					return 1;
				}
				else if(d1 == d2) {
					return 0;
				}
				else {
					return -1;
				}
			}
			
		});
		
		posOrd.addAll(posiciones);
		
		Iterator<Integer> it = posOrd.iterator();
		int furthestNode = it.next();
		
		return game.getApproximateNextMoveTowardsTarget(ghostNode, furthestNode, ghostLastMove, DM.PATH);
	}
	
}
