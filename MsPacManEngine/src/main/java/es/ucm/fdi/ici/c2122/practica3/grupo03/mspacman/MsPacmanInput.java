package es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica3.grupo03.utils.Pair;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacmanInput extends RulesInput {

	private boolean BLINKYedible;
	private boolean INKYedible;
	private boolean PINKYedible;
	private boolean SUEedible;
	
	private double BLINKYdistance;
	private double INKYdistance;
	private double PINKYdistance;
	private double SUEdistance;
	
	private Map<GHOST, ArrayList<Boolean>> mapa;
	
	private double closestGhostdistance;
	private double secondGhostdistance;
	private double thirdGhostdistance;
	private double farthestGhostdistance;
	
	
	private int closestPowerPill;
	private int secondPowerPill;
	private int thirdPowerPill;
	private int farthestPowerPill;

	private double closestPowerPillDistance;
	private double secondPowerPillDistance;
	private double thirdPowerPillDistance;
	private double farthestPowerPillDistance;
	
	
	private boolean closestedible;
	private boolean secondedible;
	private boolean thirdedible;
	private boolean farthestedible;
	
	private GHOST closestGhost;
	private GHOST secondGhost;
	private GHOST thirdGhost;
	private GHOST farthestGhost;
	
	public MsPacmanInput(Game game) {
		super(game);
	}
	
	public void parseInput() {
		mapa = new HashMap<GHOST,ArrayList<Boolean>>();
		mapa.put(GHOST.BLINKY, new ArrayList<Boolean>());
		mapa.put(GHOST.INKY, new ArrayList<Boolean>());
		mapa.put(GHOST.PINKY, new ArrayList<Boolean>());
		mapa.put(GHOST.SUE, new ArrayList<Boolean>());
		
		for(Entry<GHOST, ArrayList<Boolean>> e : mapa.entrySet()) {
			for(int i = 0; i < 4; i++) {
				e.getValue().add(false);
			}
		}
		getGhostInfo();
		getPowerPillsInfo();

	}
	
	/*private void getGhostAlt() {
		int pacman= game.getPacmanCurrentNodeIndex();
		
		for(GHOST g: GHOST.values()) {
			double dst= game.getDistance(game.getGhostCurrentNodeIndex(g), pacman,DM.PATH);
			if(closestGhostdistance>dst) {
				farthestGhostdistance=thirdGhostdistance;
				thirdGhostdistance=secondGhostdistance;
				secondGhostdistance=closestGhostdistance;
				closestGhostdistance=dst;

				farthestGhost=thirdGhost;
				thirdGhost=secondGhost;
				secondGhost=closestGhost;
				closestGhost=g;
			}
			else if(dst < secondGhostdistance) {
				farthestGhostdistance =thirdGhostdistance;
				thirdGhostdistance=secondGhostdistance;
				secondGhostdistance=dst;
				
				farthestGhost=thirdGhost;
				thirdGhost=secondGhost;
				secondGhost=g;
			}
			else if(dst<thirdGhostdistance) {
				farthestGhostdistance=thirdGhostdistance;
				thirdGhostdistance=dst;
				
				farthestGhost=thirdGhost;
				thirdGhost=g;
			}
			else {
				farthestGhostdistance=dst;
				farthestGhost=g;
			}
			
		}
	}*/
	
	private void getGhostInfo() {
		
		this.BLINKYedible = game.isGhostEdible(GHOST.BLINKY);
		this.INKYedible = game.isGhostEdible(GHOST.INKY);
		this.PINKYedible = game.isGhostEdible(GHOST.PINKY);
		this.SUEedible = game.isGhostEdible(GHOST.SUE);
		
		MOVE lastPMove = game.getPacmanLastMoveMade();
		int pNode = game.getPacmanCurrentNodeIndex();
		DM dmp = DM.PATH;
		
		if(lastPMove!=null) {
		
		this.BLINKYdistance = (game.getGhostLairTime(GHOST.BLINKY) > 0)? Double.MAX_VALUE
				: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.BLINKY), lastPMove, dmp);
		this.INKYdistance = (game.getGhostLairTime(GHOST.INKY) > 0)? Double.MAX_VALUE
				: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.INKY), lastPMove, dmp);
		this.PINKYdistance = (game.getGhostLairTime(GHOST.PINKY) > 0)? Double.MAX_VALUE
				: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.PINKY), lastPMove, dmp);
		this.SUEdistance = (game.getGhostLairTime(GHOST.SUE) > 0)? Double.MAX_VALUE 
				: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.SUE), lastPMove, dmp);
		}
		else {
			this.BLINKYdistance = (game.getGhostLairTime(GHOST.BLINKY) > 0)? Double.MAX_VALUE
			: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.BLINKY), dmp);
			this.INKYdistance = (game.getGhostLairTime(GHOST.INKY) > 0)? Double.MAX_VALUE
					: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.INKY), dmp);
			this.PINKYdistance = (game.getGhostLairTime(GHOST.PINKY) > 0)? Double.MAX_VALUE
					: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.PINKY), dmp);
			this.SUEdistance = (game.getGhostLairTime(GHOST.SUE) > 0)? Double.MAX_VALUE
					: game.getDistance(pNode, game.getGhostCurrentNodeIndex(GHOST.SUE), dmp);
			
		}
		
		List<Pair<GHOST,Double>> l = new ArrayList<Pair<GHOST,Double>>();
		
		for(GHOST g : GHOST.values()) {
			int gnode = game.getGhostCurrentNodeIndex(g);
			MOVE mv = game.getGhostLastMoveMade(g);
			if(mv!=null) {
				l.add(new Pair<GHOST,Double>(g, game.getDistance(gnode, pNode, mv, dmp)));
			}
			else {
				l.add(new Pair<GHOST,Double>(g, game.getDistance(gnode, pNode, dmp)));
			}
		}
		
		l.sort(new Comparator<Pair<GHOST,Double>>(){

			@Override
			public int compare(Pair<GHOST, Double> arg0, Pair<GHOST, Double> arg1) {
				// TODO Auto-generated method stub
				return arg0.getSecond().compareTo(arg1.getSecond());
			}
			
		});
		
		for(int i = 0; i < l.size(); i++) {
			GHOST g = GHOST.values()[i];
			
			ArrayList<Boolean> ar = mapa.get(g);
			ar.set(i, true);
		}
		
		double dstClosest=Double.MAX_VALUE;
		double dstSecond=Double.MAX_VALUE;
		double dstThird=Double.MAX_VALUE;
		double dstFarthest=Double.MAX_VALUE;
		
		int pacmanNode= game.getPacmanCurrentNodeIndex();
		for(GHOST g: GHOST.values()) {
			double dst= game.getDistance(pacmanNode, game.getGhostCurrentNodeIndex(g), DM.PATH);
			if(dstClosest>dst) {
				dstFarthest=dstThird;
				farthestGhost=thirdGhost;
				dstThird=dstSecond;
				thirdGhost=secondGhost;
				dstSecond=dstClosest;
				secondGhost=closestGhost;
				dstClosest=dst;
				closestGhost=g;
			}
			else if(dst<dstSecond) {
				dstFarthest=dstThird;
				thirdGhost=secondGhost;
				dstThird=dstSecond;
				secondGhost=g;
				dstSecond=dst;
			}
			else if(dst<dstThird) {
				dstFarthest=dstThird;
				farthestGhost=thirdGhost;
				dstThird=dst;
				thirdGhost=g;
			}
			else {
				dstFarthest=dst;
				farthestGhost=g;
			}
			
		}
		this.closestGhostdistance=dstClosest;
		this.secondGhostdistance=dstSecond;
		this.thirdGhostdistance=dstThird;
		this.farthestGhostdistance=dstFarthest;
		
		
		this.closestedible = ((this.closestGhost == null) || (game.getGhostLairTime(this.closestGhost) > 0))? false
				: game.isGhostEdible(this.closestGhost);
		this.secondedible = ((this.secondGhost == null) || (game.getGhostLairTime(this.secondGhost) > 0))? false
				: game.isGhostEdible(this.secondGhost);
		this.thirdedible = ((this.thirdGhost == null) || (game.getGhostLairTime(this.thirdGhost) > 0))? false
				: game.isGhostEdible(this.thirdGhost);
		this.farthestedible = ((this.farthestGhost == null) || (game.getGhostLairTime(this.farthestGhost) > 0))? false
				: game.isGhostEdible(this.farthestGhost);

	}
	
	private void getPowerPillsInfo() {
		int [] powerPills= game.getActivePowerPillsIndices();
		if(powerPills.length>0) {
		int pacmanNode= game.getPacmanCurrentNodeIndex();
		closestPowerPill= game.getClosestNodeIndexFromNodeIndex(pacmanNode, powerPills, DM.PATH);
		farthestPowerPill=game.getFarthestNodeIndexFromNodeIndex(pacmanNode, powerPills, DM.PATH);
		int third= -1;
		int second= -1;
		
		if(powerPills.length>2) {
		for(int i=0;i<powerPills.length; i++) {
			if(powerPills[i]!=closestPowerPill && powerPills[i]!=farthestPowerPill) {
				if(second==-1) {
					second=powerPills[i];
				}
				else
					third=powerPills[i];
			}
		}
		
		if(game.getDistance(pacmanNode, second, DM.PATH) > game.getDistance(pacmanNode, third, DM.PATH)) {
			int aux= second;
			second=third;
			third=aux;
		}
		
		secondPowerPill=second;
		thirdPowerPill=third;
		this.secondPowerPillDistance = game.getDistance(pacmanNode, secondPowerPill,DM.PATH );
		this.thirdPowerPillDistance = game.getDistance(pacmanNode, thirdPowerPill,DM.PATH );
		}
		this.closestPowerPillDistance = game.getDistance(pacmanNode, closestPowerPill,DM.PATH );
		this.farthestPowerPillDistance = game.getDistance(pacmanNode, farthestPowerPill,DM.PATH );
		}
	}

	
	/* Por poner pero no se si se pueden pasar cosas de tipo object
	
	private GHOST closestGhost;
	private GHOST secondGhost;
	private GHOST thirdGhost;
	private GHOST farthestGhost;
	
	private int closestGhostNode;
	private int secondGhostNode;
	private int thirdGhostNode;
	private int farthestGhostNode;*/


	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(BLINKY (edible %s)"
				+ "(distance %d)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.BLINKYedible,
				(int)this.BLINKYdistance,
				mapa.get(GHOST.BLINKY).get(0),
				mapa.get(GHOST.BLINKY).get(1),
				mapa.get(GHOST.BLINKY).get(2),
				mapa.get(GHOST.BLINKY).get(3),
				(game.getGhostLairTime(GHOST.BLINKY) > 0)
				));
		facts.add(String.format("(INKY (edible %s)"
				+ "(distance %d)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))",
				this.INKYedible,
				(int)this.INKYdistance,
				mapa.get(GHOST.INKY).get(0),
				mapa.get(GHOST.INKY).get(1),
				mapa.get(GHOST.INKY).get(2),
				mapa.get(GHOST.INKY).get(3),
				(game.getGhostLairTime(GHOST.INKY) > 0)
				));
		facts.add(String.format("(PINKY (edible %s)"
				+ "(distance %d)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.PINKYedible,
				(int)this.PINKYdistance,
				mapa.get(GHOST.PINKY).get(0),
				mapa.get(GHOST.PINKY).get(1),
				mapa.get(GHOST.PINKY).get(2),
				mapa.get(GHOST.PINKY).get(3),
				(game.getGhostLairTime(GHOST.PINKY) > 0)
				));
		facts.add(String.format("(SUE (edible %s)"
				+ "(distance %d)"
				+ "(first %s)"
				+ "(second %s)"
				+ "(third %s)"
				+ "(last %s)"
				+ "(jail %s))", 
				this.SUEedible,
				(int)this.SUEdistance,
				mapa.get(GHOST.SUE).get(0),
				mapa.get(GHOST.SUE).get(1),
				mapa.get(GHOST.SUE).get(2),
				mapa.get(GHOST.SUE).get(3),
				(game.getGhostLairTime(GHOST.SUE) > 0)
				));
		facts.add(String.format("(MSPACMAN (firstEdible %s)"
				+ "(secondEdible %s)"
				+ "(thirdEdible %s)"
				+ "(lastEdible %s)"
				+ "(distanceToClosestGhost %d)"
				+ "(distanceToSecondGhost %d)"
				+ "(distanceToThirdGhost %d)"
				+ "(distanceToFurthestGhost %d)"
				+ "(distanceToClosestPPill %d)"
				+ "(distanceToSecondPPill %d)"
				+ "(distanceToThirdPPill %d)"
				+ "(distanceToFurthestPPill %d)"
				+ "(diferenceClosestsandSecond %d))",
				this.closestedible,
				this.secondedible,
				this.thirdedible,
				this.farthestedible,
				(int)this.secondGhostdistance,
				(int)this.thirdGhostdistance,
				(int)this.farthestGhostdistance,
				(int)this.closestGhostdistance,
				(int)this.secondGhostdistance,
				(int)this.thirdGhostdistance,
				(int)this.farthestGhostdistance,
				(int)this.closestPowerPillDistance,
				(int)this.secondPowerPillDistance,
				(int)this.thirdPowerPillDistance,
				(int)this.farthestPowerPillDistance,
				(int)(this.closestGhostdistance - this.secondGhostdistance)));
	return facts;
	}
}
