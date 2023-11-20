package es.ucm.fdi.ici.c2122.practica3.grupo04.acciones.pacman;

import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsPacman;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GetCloseToPPillAction implements RulesAction {

	float freedomDegree;
	public GetCloseToPPillAction(float freedomDegree) {
		this.freedomDegree = freedomDegree;
	}

	@Override
	public MOVE execute(Game game) {
        int pacman = game.getPacmanCurrentNodeIndex();
        int closestPPill = CommonMethodsPacman.getClosestPPill(game, pacman);
        int[] juncs = CommonMethodsPacman.getAdjacentJunctions(game, pacman);
        int junc = juncs[0];
        if(closestPPill == -1)
        	System.out.println("F");
        
        // Se guarda las pills de la posicion a una de las siguientes intersecciones, y de ella a la PPill más cercana
        // La idea es acercase a ella, pero no necesariamente por el camino más corto, sino por el que más pills tiene
        int pills = CommonMethodsPacman.getPathNumPills(game, game.getShortestPath(pacman, junc, 
        		game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]));
        int dist = game.getShortestPathDistance(junc, closestPPill, 
        		game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]);
        
        for (int i = 1; i < juncs.length; i++) {
        	// Se mira las pills que hay hasta la próxima intersección y la distancia de ella al objetivo
        	int newPills = CommonMethodsPacman.getPathNumPills(game, game.getShortestPath(pacman, juncs[i], 
        			game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]));
        	int newDist = game.getShortestPathDistance(juncs[i], closestPPill, 
        			game.getPacmanLastMoveMade() != null ? game.getPacmanLastMoveMade() : game.getPossibleMoves(pacman)[0]);
        	// Si hay más pills y no alarga demasiado el camino se guarda como un nuevo mejor
        	if (newDist <= dist * freedomDegree && newPills > pills) {
        		pills = newPills;
        		junc = juncs[i];
        		// Si además mejora la distancia, se actualiza como la mejor
        		if (newDist < dist) {
        			dist = newDist;
        		}
        	}
        }
        if(junc ==-1)
        	System.out.println("MAL EN GETCLOSESPILLACTION");
        
		return CommonMethodsPacman.avoidGhosts( game,  pacman,  junc);
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		// Nothing to parse
		
	}

	@Override
	public String getActionId() {
		return  "PacmanGetsCloseToPPill";
	}
}
