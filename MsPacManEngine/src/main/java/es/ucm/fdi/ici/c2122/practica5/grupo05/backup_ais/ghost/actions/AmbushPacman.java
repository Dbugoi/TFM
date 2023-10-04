package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.actions;

import es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.coordinator.GhostsCoordinator;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AmbushPacman implements RulesAction {
    private final GHOST ghost;
    private final GhostsCoordinator coordinator;

    public AmbushPacman(GHOST ghost, GhostsCoordinator coordinator) {
        this.ghost = ghost;
        this.coordinator = coordinator;
    }

    @Override
    public String getActionId() {
        return "Try to ambush MsPacman";
    }

    @Override
    public MOVE execute(Game game) {
        return coordinator.requestAmbushMove(game, ghost);
    }

	@Override
	public void parseFact(Fact actionFact) {
		// TODO Auto-generated method stub
	}

}
