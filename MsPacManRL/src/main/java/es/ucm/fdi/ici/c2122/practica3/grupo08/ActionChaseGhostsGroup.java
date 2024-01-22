package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActionChaseGhostsGroup implements RulesAction{
	
	private double _minDistanceToMsPacman;
	
	public ActionChaseGhostsGroup(double minDistanceToMsPacman) {
		_minDistanceToMsPacman = minDistanceToMsPacman;
	}
	
	@Override
	public MOVE execute(Game game) {
		
		ArrayList<GHOST>[] numberGhost = new ArrayList[4];

		//Mirar si hay algun grupo de fantamas al que comer.
		//Para ello vemos para cada fantama a que distancia esta del resto de fantasmas.
		//Dependiendo de si se encuentra cerca o lejos habrá o no grupo.
		for(GHOST ghostType : GHOST.values()) {
			numberGhost[ghostType.ordinal()] = new ArrayList<GHOST>();
			//Si el fantasma es comestible
			if(game.getGhostLairTime(ghostType) <= 0 && game.isGhostEdible(ghostType)) { 
				int[] path  = game.getShortestPath(game.getPacmanCurrentNodeIndex(), 
						game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade());
				//Si esta suficientemente cerca de msPacMan se mira si tiene fantasmas cercanos
				if(path.length < _minDistanceToMsPacman) 
				{
					for(GHOST otherGhost : GHOST.values()) {
						if(otherGhost != ghostType) {
							double distanceToOtherGhost = game.getDistance(game.getGhostCurrentNodeIndex(ghostType)
									,game.getGhostCurrentNodeIndex(otherGhost), DM.EUCLID);
							//Si el otro fantasma esta suficientemente cerca del primero como para considerarlo parte del grupo
							if(distanceToOtherGhost < 15) {
								numberGhost[ghostType.ordinal()].add(otherGhost);
							}
						}
					}			
				}			
			}
		}
		//Miramos si existe algún grupo
		int max = 0;
		GHOST biggestGroup = null;
		for(GHOST ghostType : GHOST.values()) {
			int actualGroup = numberGhost[ghostType.ordinal()].size();
			//Guardamos el fantasma el cual tiene el grupo mayor
			if(actualGroup > max) {
				biggestGroup = ghostType;
				max = actualGroup;
			}			
		}
		
		//Devolvemos el mvto hacia el fantasma con el grupo mayor
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
				game.getGhostCurrentNodeIndex(biggestGroup), game.getPacmanLastMoveMade(), DM.PATH);
	}
	

	@Override
	public String getActionId() {
		return "Chase Ghosts Group";
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
