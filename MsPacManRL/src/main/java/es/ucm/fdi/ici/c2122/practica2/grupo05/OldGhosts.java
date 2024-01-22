package es.ucm.fdi.ici.c2122.practica2.grupo05;

import java.awt.Dimension;
import java.util.EnumMap;
import es.ucm.fdi.ici.c2122.practica2.grupo05.ghosts.GhostInput;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.OutOfCaseAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.evitarAtaque.BlockPPillsAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.evitarAtaque.CloseToCaseAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir.AvoidEdiblesGhostAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir.AvoidPPillsAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir.DisperseGhosts;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir.RandomAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.huir.SearchProtectorAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.perseguir.MoveTowardsPacManAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.perseguir.ProtectEdibleAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.actions.perseguir.SearchAlternativePathAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.EatenGhostTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.EatenMsPacManTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.GhostChaseTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.GhostsEdibleTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.MsPacManCloseToPPilTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.MsPacManFarFromPPilTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.OutOfCaseTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.SmallEdibleTimeTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.evitarAtaque.GoToBlockPPillsTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.evitarAtaque.GoToCloseToCaseTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir.GoToAvoidEdiblesTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir.GoToAvoidPPillsTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir.GoToGhostProtectorTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.huir.GoToRandomActionTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.perseguir.GoToAlternativePathTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.perseguir.GoToDefendEdibleTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.oldghost.transitions.perseguir.GoToPerseguirTransition;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.game.Game;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class OldGhosts extends GhostController {

	EnumMap<GHOST, FSM> fsms;
	GraphFSMObserver obHuir;
	GraphFSMObserver obPerseguir;
	GraphFSMObserver obEvitarAtaque;

	public OldGhosts() {
		setName("Ghosts 05");
		fsms = new EnumMap<GHOST, FSM>(GHOST.class);
		for (GHOST ghost : GHOST.values()) {
			FSM fsm = FMSGhostGeneral(ghost);
			fsms.put(ghost, fsm);
		}
	}

	@Override
	public void preCompute(String opponent) {
		for (FSM fsm : fsms.values())
			fsm.reset();
	}

	public FSM FMSGhostGeneral(GHOST ghost) {
		FSM fsm = new FSM(ghost.name());
		
		GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
		fsm.addObserver(graphObserver);
		//fsm.addObserver(new ConsoleFSMObserver(ghost.name()));

		CompoundState perseguir = perseguir(ghost);
		// CompoundState huir = huir(ghost);
		SimpleState huir = new SimpleState(new DisperseGhosts(ghost));
		CompoundState evitarAtaque = evitarAtaque(ghost);
		SimpleState outOfCase = new SimpleState(new OutOfCaseAction(ghost));

		fsm.add(perseguir, new MsPacManCloseToPPilTransition(ghost), evitarAtaque);
		fsm.add(perseguir, new GhostsEdibleTransition(ghost, "FromPerseguir"), huir);
		fsm.add(perseguir, new EatenMsPacManTransition(), outOfCase);
		
		fsm.add(huir, new GhostChaseTransition(ghost), perseguir);
		fsm.add(huir, new SmallEdibleTimeTransition(ghost), perseguir);
		fsm.add(huir, new EatenGhostTransition(ghost), outOfCase);
		
		fsm.add(evitarAtaque, new MsPacManFarFromPPilTransition(ghost), perseguir);
		fsm.add(evitarAtaque, new GhostsEdibleTransition(ghost, "FromEvitarAtaque"), huir);
		
		fsm.add(outOfCase, new OutOfCaseTransition(ghost), perseguir);
		
		fsm.ready(outOfCase);
		
		
		graphObserver.showInFrame(new Dimension(800,600));
		
		return fsm;
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> result = new EnumMap<GHOST, MOVE>(GHOST.class);

		GhostInput in = new GhostInput(game);

		for (GHOST ghost : GHOST.values()) {
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}

		return result;
	}

	private CompoundState evitarAtaque(GHOST ghost) {
		FSM fsm = new FSM("Evitar ataque");
		fsm.addObserver(obEvitarAtaque = new GraphFSMObserver("evitar ataque"));

		SimpleState bloquear = new SimpleState(new BlockPPillsAction(ghost));
		SimpleState irCarcel = new SimpleState(new CloseToCaseAction(ghost));

		fsm.add(bloquear, new GoToCloseToCaseTransition(ghost), irCarcel);

		fsm.add(irCarcel, new GoToBlockPPillsTransition(ghost), bloquear);

		fsm.ready(bloquear); // TODO

		return new CompoundState("Evitar ataque", fsm);
	}

	private CompoundState perseguir(GHOST ghost) {
		FSM fsm = new FSM("Perseguir");
		fsm.addObserver(obHuir = new GraphFSMObserver("perseguir"));

		SimpleState atacar = new SimpleState(new MoveTowardsPacManAction(ghost));
		//SimpleState defender = new SimpleState(new ProtectEdibleAction(ghost));
		SimpleState alternativo = new SimpleState(new SearchAlternativePathAction(ghost));

		//fsm.add(atacar, new GoToDefendEdibleTransition(ghost, "FromAtacar"), defender);
		fsm.add(atacar, new GoToAlternativePathTransition(ghost, "FromAtacar"), alternativo);

		//fsm.add(defender, new GoToPerseguirTransition(ghost, "FromDefender"), atacar);
		//fsm.add(defender, new GoToAlternativePathTransition(ghost, "FromDefender"), alternativo);

		fsm.add(alternativo, new GoToPerseguirTransition(ghost, "FromAlternativo"), atacar);
		//fsm.add(alternativo, new GoToDefendEdibleTransition(ghost, "FromAlternativo"), defender);

		fsm.ready(atacar); // TODO

		return new CompoundState("Perseguir", fsm);
	}

	private CompoundState huir(GHOST ghost) {
		FSM fsm = new FSM("Huir");
		fsm.addObserver(obPerseguir = new GraphFSMObserver("huir"));

		SimpleState avoidEdible = new SimpleState(new AvoidEdiblesGhostAction(ghost));
		SimpleState avoidPPill = new SimpleState(new AvoidPPillsAction(ghost));
		SimpleState random = new SimpleState(new RandomAction(ghost));
		SimpleState searchProtector = new SimpleState(new SearchProtectorAction(ghost));

		fsm.add(avoidEdible, new GoToAvoidPPillsTransition(ghost, "FromAvoidEdible"), avoidPPill);
		fsm.add(avoidEdible, new GoToGhostProtectorTransition(ghost, "FromAvoidEdible"), searchProtector);
		fsm.add(avoidEdible, new GoToRandomActionTransition(ghost, "FromAvoidEdible"), random);

		fsm.add(avoidPPill, new GoToAvoidEdiblesTransition(ghost, "FromAvoidPPill"), avoidEdible);
		fsm.add(avoidPPill, new GoToGhostProtectorTransition(ghost, "FromAvoidPPill"), searchProtector);
		fsm.add(avoidPPill, new GoToRandomActionTransition(ghost, "FromAvoidPPill"), random);

		fsm.add(searchProtector, new GoToAvoidEdiblesTransition(ghost, "FromSearchProtector"), avoidEdible);
		fsm.add(searchProtector, new GoToAvoidPPillsTransition(ghost, "FromSearchProtector"), avoidPPill);
		fsm.add(searchProtector, new GoToRandomActionTransition(ghost, "FromSearchProtector"), random);

		fsm.add(random, new GoToAvoidEdiblesTransition(ghost, "FromRandom"), avoidEdible);
		fsm.add(random, new GoToAvoidPPillsTransition(ghost, "FromRandom"), avoidPPill);
		fsm.add(random, new GoToGhostProtectorTransition(ghost, "FromRandom"), searchProtector);

		fsm.ready(searchProtector); // TODO

		return new CompoundState("Huir", fsm);
	}

}
