package es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman;
import static pacman.game.Constants.EDIBLE_TIME;
import static pacman.game.Constants.EDIBLE_TIME_REDUCTION;
import static pacman.game.Constants.LEVEL_RESET_REDUCTION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import es.ucm.fdi.ici.c2122.practica3.grupo06.GameUtils;
import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {
	public static final int tileSize       = 5;
	public static final int mapWidth       = tileSize * 26;
	public static final int mapHeight      = tileSize * 29;
	public static final int quadrantWidth  = tileSize *  mapWidth/2;
	public static final int quadrantHeight = tileSize *  mapHeight/2;
	public static final int limit          = tileSize * 10;
	public static final int closerLimit    = tileSize * 6; //distance from pacman to a pPill to start avoiding it.
	public static final int pPillBanLimit  = 12;
	
	private int pacmanNode = 0;
	private int nEdibleGhostNode = 0;
	private MOVE lastMoveMade = MOVE.NEUTRAL;
	private MsPacManFlags mpf;
	
	private List<Integer> normalGhostsInRange       = new ArrayList<>();
	private List<Integer> ghostsInPpill       = new ArrayList<>();
	private List<Integer> ghostsInEdibleGhost = new ArrayList<>();
	private List<Integer> ghostsInPill        = new ArrayList<>();
	
	/* SE USA?
	 * 
	private int PpillGhostNode = 0;
	private int Ghd = 0;
	private int Ppd_ppill = 0;
	private int Ppd_edibleGhost = 0;
	*/

	boolean init = false;

	public MsPacManInput (Game game, MsPacManFlags t) {
		super(game);
		init = true;
		mpf = t;
		parseInput();
	}
	
	@Override
	public void parseInput() {
		if (init) {
			pacmanNode   = game.getPacmanCurrentNodeIndex();
			//validMoves   = getValidMovesBase(game);
			lastMoveMade = game.getPacmanLastMoveMade();
			normalGhostsInRange = GameUtils.getInstance().getNormalGhostsInRange(game); // region != rango
			nEdibleGhostNode = GameUtils.getInstance().getNearest(game,GameUtils.getInstance().getEdibleGhosts(game));
			
			updateFlags(game);
			/*SE USA?
			 * 
			PpillGhostNode = GameUtils.getInstance().getNearest(game,ghostsInPpill);
			if(PpillGhostNode != -1) {
				Ghd = game.getShortestPathDistance(pacmanNode, PpillGhostNode, lastMoveMade);
				Ppd_edibleGhost = game.getShortestPathDistance(pacmanNode, nEdibleGhostNode, lastMoveMade);
				Ppd_ppill = game.getShortestPathDistance(pacmanNode, GameUtils.getInstance().getNearestPowerPillIndex(game), lastMoveMade);
			}
			*/
			
			//fantasmas en dirección a la pPill
			ghostsInPpill = GameUtils.getInstance().getNormalGhostsInDirection(game, 
					GameUtils.getInstance().getNextMoveTowardsTarget(game, 
							GameUtils.getInstance().getNearestPowerPillIndex(game)));
			//fantasmas en dirección al fantasma comestible
			ghostsInEdibleGhost = GameUtils.getInstance().getNormalGhostsInDirection(game, 
					GameUtils.getInstance().getNextMoveTowardsTarget(game, nEdibleGhostNode));
			//fantasmas en dirección a la pill más cercana
			ghostsInPill = GameUtils.getInstance().getNormalGhostsInDirection(game, 
					GameUtils.getInstance().getNextMoveTowardsTarget(game, 
							GameUtils.getInstance().getNearestPillIndex(game)));
		}
	}
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(MSPACMAN (numberOfNormalGhostsInRange %s)" 
				+ "(isPowerPillEaten %s)"
				+ "(numberOfPillsInRange %s)"
				+ "(numberOfGhostsInDirectionPPill %s)"
				+ "(numberOfGhostsInDirectionEdibleGhost %s)"
				+ "(numberOfGhostsInDirectionPill %s))",
				this.normalGhostsInRange.size(), this.mpf.pPillEaten, GameUtils.getInstance().getPillsInRegion(game).size(),
				this.ghostsInPpill.size(),  this.ghostsInEdibleGhost.size(), this.ghostsInPill.size() ));
		return facts;
	}
	
	
	//FLAGS
	private void updateFlags(Game game) {
		boolean wasPPillEaten = game.wasPowerPillEaten();
		boolean onRange = nEdibleGhostNode == -1 ? false : game.getShortestPathDistance(
			pacmanNode, nEdibleGhostNode, lastMoveMade) <= 15 * tileSize;
		int resetTimerValue = (int) (EDIBLE_TIME * (Math.pow(EDIBLE_TIME_REDUCTION, game.getCurrentLevel() % LEVEL_RESET_REDUCTION))) -10;
		for (GHOST g : GHOST.values()) { if (game.wasGhostEaten(g)) { mpf.ghostsEaten++; } }
		
		if (wasPPillEaten || mpf.edibleTimer > 0 && onRange && mpf.ghostsEaten <= GHOST.values().length && !game.wasPacManEaten()) {
			mpf.pPillEaten  = true;
			if (wasPPillEaten) { 
				mpf.edibleTimer = resetTimerValue;
				mpf.ghostsEaten = 0;
			}
		}
		else if (game.wasPacManEaten() || mpf.edibleTimer < 0) {
			mpf.pPillEaten  = false;
			mpf.edibleTimer = 0;
			mpf.ghostsEaten = 0;
		}
		
		if (mpf.pPillEaten) { mpf.edibleTimer--; }
	}
	
//Get entities in region ==========================================================================================			

	
//Get nearest entities ==========================================================================================	
	public int numberOfAliveGhostsInRange() { return normalGhostsInRange.size(); }
	public boolean getPowerPillEaten() { return mpf.pPillEaten; }
	public int numberOfPillsInRange() { return GameUtils.getInstance().getPillsInRegion(game).size(); }
	
	public int numberOfGhostsInDirectionPPill() { return ghostsInPpill.size(); }
	public int numberOfGhostsInDirectionEdibleGhost() { return ghostsInEdibleGhost.size(); }
	public int numberOfGhostsInDirectionPill() { return ghostsInPill.size(); }
	
	public int getNearestGhostInDirectionPPill() { return GameUtils.getInstance().getNearest(game, ghostsInPpill); }
	public int getNearestGhostInDirectionEdibleGhost() { return GameUtils.getInstance().getNearest(game, ghostsInEdibleGhost); }
	public int getNearestGhostInDirectionPill() { return GameUtils.getInstance().getNearest(game, ghostsInPill); }
	
	/* NO SE USA ?
	 
	public int getPpd_ppill() {return Ppd_ppill;}
	public int getPpd_edibleGhost() {return Ppd_edibleGhost;}
	public int distanceComparison(int distanceToTarget) {return Ghd - distanceToTarget;}
	*/
	
}

