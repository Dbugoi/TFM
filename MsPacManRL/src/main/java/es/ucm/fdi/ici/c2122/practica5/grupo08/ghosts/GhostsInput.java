package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.c2122.practica5.grupo08.BVector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.FVector;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends CBRInput {
	
	public GhostsInput(Game game) {
		super(game);
		
	}

	Integer HPs;	//vida de mspacman
	Integer score;	//puntuacoin del juego
	Integer level;	//nivel en el que nos encontramos
	
	FVector positions = new FVector(4);	//posiciones de cada uno de los fantasmas
	
	FVector distanceToMsPacman = new FVector(4);	//distancias de fantasmas a mspacman
	BVector edible = new BVector(4);				//si son comestibles o no los fantasmas
	BVector actives = new BVector(4);				//si están los fantasmas o no en la caja
	
	Integer distanceMsPacmanPpill;		//distancia de mspacman a cada una de las power pills
	
	@Override
	/**
	 * Recoje toda la información necesaria
	 */
	public void parseInput() {
		
		this.positions = new FVector(4);
		this.distanceToMsPacman = new FVector(4);	
		this.edible = new BVector(4);
		this.actives = new BVector(4);
		
		this.HPs = game.getPacmanNumberOfLivesRemaining();
		this.score = game.getScore();
		this.level = game.getCurrentLevel();
		
		//Para cada ghost recoje si son comestibles, sus posiciones, si están o no
		//en la caja y su distancia mspacman
		for(GHOST g : GHOST.values()) {
			this.distanceToMsPacman.values[g.ordinal()] = -1;
			if(game.getGhostLairTime(g) <= 0) {
				this.actives.values[g.ordinal()] = true;
				int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(g), 
						game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
				this.distanceToMsPacman.values[g.ordinal()] = path.length;
				this.positions.values[g.ordinal()] = game.getGhostCurrentNodeIndex(g);
			}
			else
				this.actives.values[g.ordinal()] = false;
			
			this.edible.values[g.ordinal()] = (game.getGhostEdibleTime(g) > 0);
			
		}
		int minDist = 153;
		for(int pPill : game.getActivePowerPillsIndices()) {
			int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), pPill, game.getPacmanLastMoveMade());
			int dist = path.length;
			if(dist < minDist) dist = minDist;
		}
		this.distanceMsPacmanPpill = minDist;
	}

	@Override
	/**
	 * Crea una descripción de un caso y modifica sus variables de clase
	 */
	public CBRQuery getQuery() {
		GhostsDescription description = new GhostsDescription();
		
		description.score = this.score;
		description.level = this.level;
		description.HPs = this.HPs;
		description.positions = this.positions;
		
		description.distanceToMsPacman = this.distanceToMsPacman;
		description.edible = this.edible;
		description.actives = this.actives;

		description.distanceMsPacmanPpill = this.distanceMsPacmanPpill;
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
}
