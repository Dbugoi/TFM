package es.ucm.fdi.ici.c2122.practica2.grupo05;

import java.awt.Dimension;
import java.util.EnumMap;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostsCoordinator;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.InLair;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.huir.FollowProtector;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.huir.GoToBestCorner;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.kill.KillPacman;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.kill.MoveToKillPacman;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.actions.perseguir.AmbushPacman;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.CanKillTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.IsInDangerTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.IsInLairTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.NoLongerInLairTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.NotInDangerTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.huir.EdibleMayFollowProtectorGhostSafelyTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.huir.HasToFleeTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghostsfsm.transitions.kill.CanKillInNextMoveTransition;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
    private EnumMap<GHOST, FSM> fsms;
    private GhostsCoordinator coordinator;

    public Ghosts() {
        setName("Ghosts 05");
        coordinator = new GhostsCoordinator();
        fsms = new EnumMap<>(GHOST.class);
        for (GHOST ghost : GHOST.values()) {
            FSM fsm = fsmGhostGeneral(ghost);
            fsms.put(ghost, fsm);
        }
    }

    @Override
    public void preCompute(String opponent) {
        for (FSM fsm : fsms.values())
            fsm.reset();
    }

    private FSM fsmGhostGeneral(GHOST ghost) {
        FSM fsm = new FSM(ghost.name());

        GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
        fsm.addObserver(graphObserver);

        CompoundState huir = new CompoundState("Huir", huirFSM(ghost));
        CompoundState kill = new CompoundState("Kill", killFSM(ghost));
        SimpleState perseguir = new SimpleState(new AmbushPacman(ghost, coordinator));
        SimpleState inLair = new SimpleState(new InLair());

        fsm.add(perseguir, new IsInDangerTransition(ghost), huir);
        fsm.add(huir, new NotInDangerTransition(ghost), perseguir);

        fsm.add(perseguir, new CanKillTransition(ghost, "from Perseguir"), kill);
        fsm.add(huir, new CanKillTransition(ghost, "from Huir"), kill);

        fsm.add(huir, new IsInLairTransition(ghost, "from Huir"), inLair);
        fsm.add(kill, new IsInLairTransition(ghost, "from Kill"), inLair);
        fsm.add(perseguir, new IsInLairTransition(ghost, "from Perseguir"), inLair);

        fsm.add(inLair, new NoLongerInLairTransition(ghost), perseguir);

        fsm.ready(inLair);

        //graphObserver.showInFrame(new Dimension(800,600));

        return fsm;
    }

    private FSM huirFSM(GHOST ghost) {
        FSM fsm = new FSM(ghost.name() + " (huir)");

        SimpleState goToBestCorner = new SimpleState(new GoToBestCorner(ghost, coordinator));
        SimpleState followProtector = new SimpleState(new FollowProtector(ghost));
        //SimpleState moveAway = new SimpleState(new MoveAwayFromCloseEdibleGhosts(ghost));

        //fsm.add(moveAway, new AlwaysTrueTransition(), goToBestCorner);
        fsm.add(goToBestCorner, new EdibleMayFollowProtectorGhostSafelyTransition(ghost),
                followProtector);
        fsm.add(followProtector, new HasToFleeTransition(ghost), goToBestCorner);

        //fsm.ready(moveAway);
        fsm.ready(goToBestCorner);

        return fsm;
    }

    private FSM killFSM(GHOST ghost) {
        FSM fsm = new FSM(ghost.name() + " (kill)");

        SimpleState killPacman = new SimpleState(new KillPacman(ghost));
        SimpleState moveToKillPacman = new SimpleState(new MoveToKillPacman(ghost));

        fsm.add(moveToKillPacman, new CanKillInNextMoveTransition(ghost), killPacman);
        fsm.ready(moveToKillPacman);

        return fsm;
    }

    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        EnumMap<GHOST, MOVE> result = new EnumMap<>(GHOST.class);

        GhostInput in = new GhostInput(game);

        for (GHOST ghost : GHOST.values()) {
            FSM fsm = fsms.get(ghost);
            MOVE move = fsm.run(in);
            result.put(ghost, move);
        }

        return result;
    }
}
