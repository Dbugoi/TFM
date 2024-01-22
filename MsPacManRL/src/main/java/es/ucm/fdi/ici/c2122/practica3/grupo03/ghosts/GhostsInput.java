package es.ucm.fdi.ici.c2122.practica3.grupo03.ghosts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica3.grupo03.utils.Pair;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends RulesInput {
	
	private final int limitfirst = 25;
	private final int limitsecond = 35;

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	
	private double BLINKYedibleTime;
	private double INKYedibleTime;
	private double PINKYedibleTime;
	private double SUEedibleTime;
	
	private boolean BLINKYfirst;
	private boolean INKYfirst;
	private boolean PINKYfirst;
	private boolean SUEfirst;
	
	private boolean BLINKYsecond;
	private boolean INKYsecond;
	private boolean PINKYsecond;
	private boolean SUEsecond;
	
	private boolean BLINKYthird;
	private boolean INKYthird;
	private boolean PINKYthird;
	private boolean SUEthird;
	
	private boolean BLINKYlast;
	private boolean INKYlast;
	private boolean PINKYlast;
	private boolean SUElast;
	
	private boolean BLINKYjail;
	private boolean INKYjail;
	private boolean PINKYjail;
	private boolean SUEjail;
	
	private double distanceBlinky;
	private double distanceInky;
	private double distancePinky;
	private double distanceSue;
	
	private double minPacmanDistancePPill;
	private double mindistanceGhost;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		int pacman = game.getPacmanCurrentNodeIndex();
		
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);
		
		this.BLINKYedibleTime = game.getGhostEdibleTime(GHOST.BLINKY);
		this.INKYedibleTime = game.getGhostEdibleTime(GHOST.INKY);
		this.PINKYedibleTime = game.getGhostEdibleTime(GHOST.PINKY);
		this.SUEedibleTime = game.getGhostEdibleTime(GHOST.SUE);
		
		this.distanceBlinky = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.PATH);
		this.distanceInky = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.INKY), DM.PATH);
		this.distancePinky = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.PATH);
		this.distanceSue = game.getDistance(pacman, game.getGhostCurrentNodeIndex(GHOST.SUE), DM.PATH);
	
		this.minPacmanDistancePPill = Integer.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
		
		
		this.BLINKYjail = false;
		this.INKYjail = false;
		this.PINKYjail = false;
		this.SUEjail = false;
		
		
		List<Pair<GHOST,Double>> arDist = new ArrayList<Pair<GHOST,Double>>();
		this.mindistanceGhost = Integer.MAX_VALUE;
		double distance;
		
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			if(game.getGhostEdibleTime(g) > 0) {
				distance = Integer.MAX_VALUE;
			}
			else if(game.getGhostLairTime(g) > 0) {
				distance = Integer.MAX_VALUE;
				switch(g) {
				case BLINKY:
					this.BLINKYjail = true;
				case INKY:
					this.INKYjail = true;
				case PINKY:
					this.PINKYjail = true;
				case SUE:
					this.SUEjail = true;
				}
			}
			else {
				distance = game.getDistance(pacman, pos, DM.PATH);
			}
			this.mindistanceGhost = Math.min(distance, this.mindistanceGhost);
			arDist.add(new Pair<GHOST,Double>(g, distance));
		}
	
		arDist.sort(new Comparator<Pair<GHOST, Double>>(){
			@Override
			public int compare(Pair<GHOST, Double> o1, Pair<GHOST, Double> o2) {
				return o1.getSecond().compareTo(o2.getSecond());
			}	
		});
		
		
		this.BLINKYfirst = false;
		this.INKYfirst = false;
		this.PINKYfirst = false;
		this.SUEfirst = false;
		
		this.BLINKYsecond = false;
		this.INKYsecond = false;
		this.PINKYsecond = false;
		this.SUEsecond = false;
		
		this.BLINKYthird = false;
		this.INKYthird = false;
		this.PINKYthird = false;
		this.SUEthird = false;
		
		this.BLINKYlast = false;
		this.INKYlast = false;
		this.PINKYlast = false;
		this.SUElast = false;
		
		
		switch(arDist.get(0).getFirst()) {
		case BLINKY:
			this.BLINKYfirst = true;
		case INKY:
			this.INKYfirst = true;
		case PINKY:
			this.PINKYfirst = true;
		case SUE:
			this.SUEfirst = true;
		}
		
		switch(arDist.get(1).getFirst()) {
		case BLINKY:
			this.BLINKYsecond = true;
		case INKY:
			this.INKYsecond = true;
		case PINKY:
			this.PINKYsecond = true;
		case SUE:
			this.SUEsecond = true;
		}
		
		switch(arDist.get(2).getFirst()) {
		case BLINKY:
			this.BLINKYthird = true;
		case INKY:
			this.INKYthird= true;
		case PINKY:
			this.PINKYthird = true;
		case SUE:
			this.SUEthird = true;
		}
		
		switch(arDist.get(3).getFirst()) {
		case BLINKY:
			this.BLINKYlast = true;
		case INKY:
			this.INKYlast = true;
		case PINKY:
			this.PINKYlast = true;
		case SUE:
			this.SUElast = true;
		}
	}



	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(BLINKY (timeLessThanDistance %s)"
				+ "(distanceLessThan20 %s)"
				+ "(distanceLessThan30 %s)"
				+ "(edible %s)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.BLINKYedibleTime < this.distanceBlinky,
				this.distanceBlinky <= limitfirst,
				this.distanceBlinky <= limitsecond,
				this.BLINKYedible,
				this.BLINKYfirst,
				this.BLINKYsecond,
				this.BLINKYthird,
				this.BLINKYlast,
				this.BLINKYjail
		));
		
		facts.add(String.format("(INKY (timeLessThanDistance %s)"
				+ "(distanceLessThan20 %s)"
				+ "(distanceLessThan30 %s)"
				+ "(edible %s)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.INKYedibleTime < this.distanceInky,
				this.distanceInky <= limitfirst,
				this.distanceInky <= limitsecond,
				this.INKYedible,
				this.INKYfirst,
				this.INKYsecond,
				this.INKYthird,
				this.INKYlast,
				this.INKYjail
		));
		
		facts.add(String.format("(PINKY (timeLessThanDistance %s)"
				+ "(distanceLessThan20 %s)"
				+ "(distanceLessThan30 %s)"
				+ "(edible %s)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.PINKYedibleTime < this.distancePinky,
				this.distancePinky <= limitfirst,
				this.distancePinky <= limitsecond,
				this.PINKYedible,
				this.PINKYfirst,
				this.PINKYsecond,
				this.PINKYthird,
				this.PINKYlast,
				this.PINKYjail
		));
		
		facts.add(String.format("(SUE (timeLessThanDistance %s)"
				+ "(distanceLessThan20 %s)"
				+ "(distanceLessThan30 %s)"
				+ "(edible %s)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.SUEedibleTime < this.distanceSue,
				this.distanceSue <= limitfirst, 
				this.distanceSue <= limitsecond,
				this.SUEedible,
				this.SUEfirst,
				this.SUEsecond,
				this.SUEthird,
				this.SUElast,
				this.SUEjail
		));

		facts.add(String.format("(MSPACMAN (mindistanceGhostless20 %s)"
				+ "(mindistancePPillless30 %s))",
				this.mindistanceGhost <= 20 ,
				this.minPacmanDistancePPill <= 30));
		
		return facts;
	}


	
	
}
