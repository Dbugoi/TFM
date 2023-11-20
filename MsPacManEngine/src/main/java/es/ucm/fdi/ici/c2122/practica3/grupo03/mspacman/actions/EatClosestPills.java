package es.ucm.fdi.ici.c2122.practica3.grupo03.mspacman.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatClosestPills implements RulesAction{

	public EatClosestPills() {
	}
	
	@Override
	public String getActionId() {
		return "EatClosestPills";
	}

	@Override
	public MOVE execute(Game game) {
		// TODO Auto-generated method stub
		int pacman= game.getPacmanCurrentNodeIndex();
		int [] pills= game.getActivePillsIndices();
		int pill = game.getClosestNodeIndexFromNodeIndex(pacman, pills, DM.PATH);
		
		double pilldst= game.getDistance(pacman, pill, DM.PATH);
		double dstClosest=Double.MAX_VALUE;
		double dstSecond=Double.MAX_VALUE;
		double dstThird=Double.MAX_VALUE;
		double dstFarthest=Double.MAX_VALUE;
		
		GHOST closest=null;
		GHOST second=null;
		GHOST third=null;
		GHOST furthest=null;
		
		for(GHOST g: GHOST.values()) {
			double dst= game.getDistance(game.getGhostCurrentNodeIndex(g), pacman,DM.PATH);
			if(dstClosest>dst) {
				dstFarthest=dstThird;
				dstThird=dstSecond;
				dstSecond=dstClosest;
				dstClosest=dst;

				furthest=third;
				third=second;
				second=closest;
				closest=g;
			}
			else if(dst<dstSecond) {
				dstFarthest=dstThird;
				dstThird=dstSecond;
				dstSecond=dst;
				
				furthest=third;
				third=second;
				second=g;
			}
			else if(dst<dstThird) {
				dstFarthest=dstThird;
				dstThird=dst;
				
				furthest=third;
				third=g;
			}
			else {
				dstFarthest=dst;
				furthest=g;
			}
			
		}
	
		if(pilldst < dstClosest) {
			return game.getNextMoveTowardsTarget(pacman, pill, DM.PATH);
		}
		else if(game.isGhostEdible(closest)) {
			return game.getNextMoveTowardsTarget(pacman, game.getGhostCurrentNodeIndex(closest), DM.PATH);
		}
		else {
			return game.getNextMoveAwayFromTarget(pacman,game.getGhostCurrentNodeIndex(closest), DM.PATH);
		}
	}

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}

}
