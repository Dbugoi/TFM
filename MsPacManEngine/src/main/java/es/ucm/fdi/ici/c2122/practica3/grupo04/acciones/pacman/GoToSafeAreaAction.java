package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToSafeAreaAction implements RulesAction {

	float dangerMult;
	int minDist, maxDist;
	public GoToSafeAreaAction(float dangerMult, int minDist, int maxDist) {
		this.dangerMult = dangerMult;
		this.minDist = minDist;
		this.maxDist = maxDist;
	}

	@Override
	public MOVE execute(Game game) {
		int pacman = game.getPacmanCurrentNodeIndex();
        int[] juncs = game.getJunctionIndices();
        Vector<Integer> visited = new Vector<Integer>();
        int dist;
        //guardamos las intersecciones en un rango de distancia
        for (int i = 0; i < juncs.length; ++i) {
        	dist = game.getShortestPathDistance(pacman, juncs[i], 
        			game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]);
        	if(dist > minDist && dist < maxDist)
        		visited.add(juncs[i]);        	
        }     
        Collections.sort(visited, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
            	float dangerA = CommonMethodsPacman.dangerLevel(game, a, dangerMult);
     	        float dangerB = CommonMethodsPacman.dangerLevel(game, b, dangerMult);
     	        return Float.compare(dangerA, dangerB);
            }
        });
   
        return CommonMethodsPacman.avoidGhosts( game,  pacman,  visited.firstElement());
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "GoToSafeArea";
	}
}
