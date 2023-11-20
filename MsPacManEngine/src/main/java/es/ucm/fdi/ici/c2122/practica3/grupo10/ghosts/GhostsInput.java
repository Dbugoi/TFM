package es.ucm.fdi.ici.c2122.practica3.grupo10.ghosts;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends RulesInput {

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	private double minPacmanDistancePPill;
	private int minPacmanDistancePPillPosition;
	private GHOST BLINKYnearGhost; //nos devuelve el fantasma que esta mas cerca de BLINKI	
	private GHOST INKYnearGhost; 
	private GHOST PINKYnearGhost;
	private GHOST SUEnearGhost; 
	private double BLINKYnearestGhostDistance; //nos devuelve la distancia del fantasma que esta mas cerca de BLINKI
	private double INKYnearestGhostDistance; 
	private double PINKYnearestGhostDistance;
	private double SUEnearestGhostDistance;
	private double BLINKYlairTime; //nos devuelve la distancia del fantasma que esta mas cerca de BLINKI
	private double INKYlairTime; 
	private double PINKYlairTime;
	private double SUElairTime;
	private double distance_BLINKY_PACMAN;
	private double distance_PACMAN_BLINKY;
	private double distance_PINKY_PACMAN;
	private double distance_PACMAN_PINKY;
	private double distance_INKY_PACMAN;
	private double distance_PACMAN_INKY;
	private double distance_SUE_PACMAN;
	private double distance_PACMAN_SUE;
	private int BLINKYnearestPPillDistance;
	private int INKYnearestPPillDistance;
	private int PINKYnearestPPillDistance;
	private int SUEnearestPPillDistance;
	private boolean BLINKYinIntersection;
	private boolean INKYinIntersection;
	private boolean PINKYinIntersection;
	private boolean SUEinIntersection;


	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);

		this.BLINKYnearGhost = ghostNearGhost(GHOST.BLINKY);
		this.INKYnearGhost = ghostNearGhost(GHOST.INKY);
		this.PINKYnearGhost = ghostNearGhost(GHOST.PINKY);
		this.SUEnearGhost = ghostNearGhost(GHOST.SUE);

		this.BLINKYlairTime = game.getGhostLairTime(GHOST.BLINKY);
		this.INKYlairTime = game.getGhostLairTime(GHOST.INKY);
		this.PINKYlairTime = game.getGhostLairTime(GHOST.PINKY);
		this.SUElairTime = game.getGhostLairTime(GHOST.SUE);
		
		this.BLINKYinIntersection = game.isJunction(game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		this.INKYinIntersection = game.isJunction(game.getGhostCurrentNodeIndex(GHOST.INKY));
		this.PINKYinIntersection = game.isJunction(game.getGhostCurrentNodeIndex(GHOST.PINKY));
		this.SUEinIntersection = game.isJunction(game.getGhostCurrentNodeIndex(GHOST.SUE));

		int pacman = game.getPacmanCurrentNodeIndex();
		if(this.BLINKYlairTime <=0) {
			this.distance_BLINKY_PACMAN = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.BLINKY), pacman, game.getGhostLastMoveMade(GHOST.BLINKY));
			this.distance_PACMAN_BLINKY = game.getShortestPathDistance(pacman,game.getGhostCurrentNodeIndex(GHOST.BLINKY), game.getPacmanLastMoveMade());
		}
		if(this.INKYlairTime <=0) {

			this.distance_INKY_PACMAN = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.INKY), pacman, game.getGhostLastMoveMade(GHOST.INKY));
			this.distance_PACMAN_INKY = game.getShortestPathDistance(pacman,game.getGhostCurrentNodeIndex(GHOST.INKY), game.getPacmanLastMoveMade());
		}
		if(this.PINKYlairTime <=0) {
			this.distance_PINKY_PACMAN = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.PINKY), pacman, game.getGhostLastMoveMade(GHOST.PINKY));
			this.distance_PACMAN_PINKY = game.getShortestPathDistance(pacman,game.getGhostCurrentNodeIndex(GHOST.PINKY), game.getPacmanLastMoveMade());
		}
		if(this.SUElairTime <=0) {

			this.distance_SUE_PACMAN = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), pacman, game.getGhostLastMoveMade(GHOST.SUE));
			this.distance_PACMAN_SUE = game.getShortestPathDistance(pacman,game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanLastMoveMade());
		}
		
		

		this.minPacmanDistancePPill = Double.MAX_VALUE;
		int [] ppills = game.getActivePowerPillsIndices();
		
		BLINKYnearestPPillDistance = 10000;
		INKYnearestPPillDistance = 10000;
		PINKYnearestPPillDistance = 10000;
		SUEnearestPPillDistance = 10000;
		
		if(ppills.length > 0) {
			for(int ppill : ppills) {
				// Calculamos la PPill más cercana a cada ghost
				for (GHOST g : GHOST.values()) {
					int distanceGhost = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), ppill, game.getGhostLastMoveMade(g));
					if (g == GHOST.BLINKY) {
						if (this.BLINKYnearestPPillDistance > distanceGhost) {
							this.BLINKYnearestPPillDistance = distanceGhost;
						}
					}
					else if (g == GHOST.INKY) {
						if (this.INKYnearestPPillDistance > distanceGhost) {
							this.INKYnearestPPillDistance = distanceGhost;
						}
					}
					else if (g == GHOST.PINKY) {
						if (this.PINKYnearestPPillDistance > distanceGhost) {
							this.PINKYnearestPPillDistance = distanceGhost;
						}
					}
					else {
						if (this.SUEnearestPPillDistance > distanceGhost) {
							this.SUEnearestPPillDistance = distanceGhost;
						}
					}
				}
				// PPill mas cercana a MsPacMan
				double distance = game.getShortestPathDistance(pacman, ppill,game.getPacmanLastMoveMade());
				if(distance < this.minPacmanDistancePPill) {
					this.minPacmanDistancePPill = distance;
					this.minPacmanDistancePPillPosition = ppill;
				}

			}
		}


	}

	private GHOST ghostNearGhost(GHOST g) {
		GHOST ret =null;
		double min_dist = Double.MAX_VALUE;
		for(GHOST ghost: GHOST.values()) {
			if(ghost!=g) {
				double dist = game.getDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(g),DM.EUCLID);
				if(dist < min_dist) {
					min_dist = dist;
					ret=ghost;
				}
			}
		}
		
		switch(g) {
		case BLINKY:
			this.BLINKYnearestGhostDistance=min_dist;
			this.BLINKYnearGhost=ret;
			break;
		case INKY:
			this.INKYnearestGhostDistance=min_dist;
			this.INKYnearGhost=ret;
			break;
		case PINKY:
			this.PINKYnearestGhostDistance=min_dist;
			this.PINKYnearGhost=ret;
			break;
		case SUE:
			this.SUEnearestGhostDistance=min_dist;
			this.SUEnearGhost=ret;
			break;
		default:
			System.out.println("error");
		}
		return ret;
	}



	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		// Facts de BLINKY
		facts.add(String.format("(BLINKY (edible %s) (nearGhost %s) (nearGhostDistance %d) (lairTime %d) "
				+ "(dist_to_pacman %d) (dist_from_pacman %d) (nearPPD %d) (inIntersection %s))",
				this.BLINKYedible, this.BLINKYnearGhost, (int) this.BLINKYnearestGhostDistance, (int) this.BLINKYlairTime, 
				(int)this.distance_BLINKY_PACMAN, (int)this.distance_PACMAN_BLINKY, this.BLINKYnearestPPillDistance, this.BLINKYinIntersection));
		// Facts de INKY
		facts.add(String.format("(INKY (edible %s) (nearGhost %s) (nearGhostDistance %d) (lairTime %d) "
				+ "(dist_to_pacman %d) (dist_from_pacman %d) (nearPPD %d) (inIntersection %s))",
				this.INKYedible, this.INKYnearGhost, (int)this.INKYnearestGhostDistance, (int)this.INKYlairTime, 
				(int)this.distance_INKY_PACMAN, (int)this.distance_PACMAN_INKY, this.INKYnearestPPillDistance, this.INKYinIntersection));
		// Facts de PINKY
		facts.add(String.format("(PINKY (edible %s) (nearGhost %s) (nearGhostDistance %d) (lairTime %d) "
				+ "(dist_to_pacman %d) (dist_from_pacman %d) (nearPPD %d) (inIntersection %s))",
				this.PINKYedible, this.PINKYnearGhost, (int)this.PINKYnearestGhostDistance, (int)this.PINKYlairTime, 
				(int)this.distance_PINKY_PACMAN, (int)this.distance_PACMAN_PINKY, this.PINKYnearestPPillDistance, this.PINKYinIntersection));
		// Facts de SUE
		facts.add(String.format("(SUE (edible %s) (nearGhost %s) (nearGhostDistance %d) (lairTime %d) "
				+ "(dist_to_pacman %d) (dist_from_pacman %d) (nearPPD %d) (inIntersection %s))",
				this.SUEedible, this.SUEnearGhost, (int)this.SUEnearestGhostDistance, (int)this.SUElairTime, 
				(int)this.distance_SUE_PACMAN, (int)this.distance_PACMAN_SUE, this.SUEnearestPPillDistance, this.SUEinIntersection));
		// Facts de MSPACMAN
		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", (int)this.minPacmanDistancePPill));
		

		return facts;
	}




}
