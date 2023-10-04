package es.ucm.fdi.ici.c2122.practica3.grupo05.ghost.actions;

import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PathDistance;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class KillPacman implements RulesAction {
    private final GHOST ghost;

    public KillPacman(GHOST ghost) {
        this.ghost = ghost;
    }

    @Override
    public String getActionId() {
        return "Kill MsPacman";
    }

    @Override
    public MOVE execute(Game game) {
        MOVE chosenMove = MOVE.NEUTRAL;
        int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
        MOVE dontMakeMove = game.getPacmanLastMoveMade();
        
        MOVE lastMove = game.wasGhostEaten(ghost) || game.getTotalTime() == 0
            ? MOVE.NEUTRAL
            : game.getGhostLastMoveMade(ghost);

        MOVE[] availableMoves = game.getPossibleMoves(ghostIndex, lastMove);

        int minDistance = Integer.MAX_VALUE;
        for (MOVE move : availableMoves)
            if (move != dontMakeMove) {
                int neighbour = game.getNeighbour(ghostIndex, move);
                int distance = PathDistance.toPacman(game, neighbour, move);
                if (distance < minDistance) {
                    minDistance = distance;
                    chosenMove = move;
                }
            }
        if (chosenMove == MOVE.NEUTRAL) {
            // throw new IllegalStateException("No move was chosen");
            //System.out.println("No move was chosen");
            return dontMakeMove;
        }

        return chosenMove;
    }

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
		
	}
}
