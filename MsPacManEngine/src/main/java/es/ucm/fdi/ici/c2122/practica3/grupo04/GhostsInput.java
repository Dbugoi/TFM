package es.ucm.fdi.ici.c2122.practica3.grupo04;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica1.grupo04.Ghosts;
import es.ucm.fdi.ici.c2122.practica3.grupo04.common.CommonMethodsGhosts;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants;
import pacman.game.Game;

public class GhostsInput extends RulesInput {

	private boolean[] ghostEdible;
	private double[] ghostLairTime;
	private double[] ghostPacmanDist;
	private double[] ghostClosestPPillDist;
	private double[] dangerLevel;
	private double[] pacmanGhostDist;
	private boolean[] canGhostEat;
	private boolean[] canGhostTrap;
	private boolean[] withinTheConeBack;
	private boolean[] withinTheConeFront;
	private int[] distanceToEdibleGhost;
	private double pacmanClosestPPillDist;
	private int closestPacmanPPill;
	private int closestEdibleGhost;
	
	/*private int maxDistChase = 30;
	private int maxDistTrapPacmanPPill = 100;
	private int maxDistSaveEdible = 100;*/

	public GhostsInput(Game game) {
		super(game);

	}

	@Override
	public void parseInput() {
		this.ghostEdible = new boolean[4];
		this.ghostLairTime = new double[4];
		this.ghostPacmanDist = new double[4];
		this.ghostClosestPPillDist = new double[4];
		this.dangerLevel = new double[4];
		this.pacmanGhostDist = new double[4];
		this.canGhostEat = new boolean[4];
		this.canGhostTrap = new boolean[4];
		this.withinTheConeBack= new boolean[4];
		this.withinTheConeFront= new boolean[4];
		this.distanceToEdibleGhost = new int[4];
		int mspacman = game.getPacmanCurrentNodeIndex();
		float 	dangerMult = 1.2f,
				sweepAngle = 20,
				sweepDist = 32;
		GHOST gEdible = CommonMethodsGhosts.getClosestEdibleGhost(game, 10000);
		closestEdibleGhost = gEdible != null ? gEdible.ordinal() : -1;
		closestPacmanPPill = CommonMethodsGhosts.getClosestPPill(game, mspacman);
		pacmanClosestPPillDist = closestPacmanPPill != -1 ? game.getShortestPathDistance(mspacman, closestPacmanPPill, game.getPacmanLastMoveMade()) : 1000000;
		for(GHOST g : GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			this.ghostEdible[g.ordinal()] = game.isGhostEdible(g); 
			this.ghostLairTime[g.ordinal()] = game.getGhostLairTime(g);
			this.ghostPacmanDist[g.ordinal()] = game.getGhostLairTime(g) <= 0 ? game.getDistance(mspacman, pos, game.getGhostLastMoveMade(g), Constants.DM.PATH) : 1000;
			this.ghostClosestPPillDist[g.ordinal()] = closestPacmanPPill != -1 ? game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), closestPacmanPPill, game.getGhostLastMoveMade(g)) : 10000;
			this.canGhostEat[g.ordinal()] = checkCanEat(game, g,pos,mspacman);
			this.canGhostTrap[g.ordinal()] = checkCanTrap(game, g,pos,mspacman);
			this.dangerLevel[g.ordinal()] = CommonMethodsGhosts.dangerLevel(game, pos, dangerMult, g);
			this.withinTheConeBack[g.ordinal()] = CommonMethodsGhosts.withinTheCone(this, sweepDist, sweepAngle, g, game.getPacmanLastMoveMade().opposite());
			this.withinTheConeFront[g.ordinal()] = CommonMethodsGhosts.withinTheCone(this, sweepDist, sweepAngle, g, game.getPacmanLastMoveMade());
			this.pacmanGhostDist[g.ordinal()] = game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			this.distanceToEdibleGhost[g.ordinal()] = closestEdibleGhost != - 1 ? game.getShortestPathDistance(pos, closestEdibleGhost, game.getGhostLastMoveMade(g)) : -1; 
		}		
	}



	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		for(GHOST g : GHOST.values()) {
			facts.add(String.format("(%s (edible %s) (cell %s) (pacDist %s) (closestPPillDist %s) (danger %s) (pacmanSeparation %s) (canGhostEat %s) (canGhostTrap %s) (withinTheConeBack %s) (withinTheConeFront %s) (distanceToEdibleGhost %s))", g.name(),
						this.ghostEdible[g.ordinal()], 
						(int)this.ghostLairTime[g.ordinal()] > 0,
						(int)this.ghostPacmanDist[g.ordinal()],
						(int)this.ghostClosestPPillDist[g.ordinal()],
						(int)this.dangerLevel[g.ordinal()],
						(int)this.pacmanGhostDist[g.ordinal()],
						this.canGhostEat[g.ordinal()],
						this.canGhostTrap[g.ordinal()],
						this.withinTheConeBack[g.ordinal()],
						this.withinTheConeFront[g.ordinal()],
						this.distanceToEdibleGhost[g.ordinal()]));				
		}
		facts.add(String.format("(MSPACMAN (pacmanClosestPPillDist %d) (closestPacmanPPill %d) (closestEdibleGhost %d))", 
				(int)this.pacmanClosestPPillDist,
				(int)this.closestPacmanPPill,
				(int)this.closestEdibleGhost));
		
		return facts;
	}

	// Comprueba si puede encerrar al pacman
	private boolean checkCanTrap(Game game , GHOST ghostType, int pos, int mspacmanPos) {
		if(game.getGhostLairTime(ghostType) > 0 )return false;
		//Si puedo llegar antes a la interseccion mas cercana de mspacman que ella, voy a esa interseccion
		int nextPacmanJunct=mspacmanClosestJunction(game);

		int pacmanDistToJunct = game.getShortestPathDistance(mspacmanPos, nextPacmanJunct, game.getPacmanLastMoveMade());

		if( game.getShortestPathDistance(pos, nextPacmanJunct, game.getGhostLastMoveMade(ghostType)) < pacmanDistToJunct) {
			return true;
		}

		return false;
	}

	//devuelve la proxima interseccion de msPacMan
	private int mspacmanClosestJunction(Game game) {

		int junct =-1;
		int junctDist = Integer.MAX_VALUE;
		int[] juncs = game.getJunctionIndices();
		for(int j:juncs) {
			int tempDist=game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), j, game.getPacmanLastMoveMade());
			if( tempDist < junctDist) {
				junctDist = tempDist;
				junct = j;
			}
		}
		return junct;
	}
	// Comprueba si el fantasma es capaz de comer al pacman
	private boolean checkCanEat(Game game , GHOST ghostType, int pos, int mspacmanPos) {	

		int nextPacmanJunct=mspacmanClosestJunction(game);

		//Si puedo llegar a mspacman antes de que llegue a la siguiente interseccion, voy hacia ella
		if(nextPacmanJunct==pos ) {
			//moves.put(ghostType, game.getApproximateNextMoveTowardsTarget( pos, mspacmanPos, game.getGhostLastMoveMade(ghostType), Constants.DM.PATH));
			return true;
		}


		return false;
	}

	//	public boolean[] getGhostEdible() {
	//		return ghostEdible;
	//	}
	//	
	//	public double[] getGhostLairTime() {
	//		return ghostLairTime;
	//	}
	//	
	//	public double[] getGhostPacmanDist() {
	//		return ghostPacmanDist;
	//	}



}
