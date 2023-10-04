package es.ucm.fdi.ici.c2122.practica2.grupo05;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.BestMoveToEatChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.ChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.EatPillGoodMoveAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.EatPillPatrolAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.EatPillsAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.EatPowerPillAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.EscapeNearDeathEatAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.LatterMovesPatrolAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.MsPacManReappearsAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.NoGoodMovesChaseGhostAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.NoGoodMovesPatrolAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.NoPillGoodMovesAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.NoPillNoGoodMovesEatAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.PatrolAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.RandomGoodMoveEatAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.actions.RunAwayCageAction;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.BasicPatrolTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.BestMoveToEatGoForEdibleGhostTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.BestTowardsGoForEdibleGhostTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.EatPillTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.EatPowerPillTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.EscapeNearDeathEatTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.GoForEdibleGhostTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.LatterMovesPatrolTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.MsPacManDiesTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.NoGoodMovesGoForEdibleGhostTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.NoGoodMovesGoForPatrolTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.NoPillGoodMoveTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.NoPillNoGoodMoveTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.PatrolTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.PillGoodMovePatrolTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.PillGoodMoveTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.RandomGoodMoveTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.RunAwayFromCageTransition;
import es.ucm.fdi.ici.c2122.practica2.grupo05.mspacman.transitions.RunAwayFromChasingGhostTransition;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class NearestPillPacMan.
 */
public class MsPacMan extends PacmanController {

	FSM fsm;
	public MsPacMan() {
		setName("MsPacMan 05");
		
    	fsm = new FSM("MsPacMan");
    	
    	//GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
    	//fsm.addObserver(observer);
    	
    	
    	SimpleState patrolState = new SimpleState("patrolState", new PatrolAction());
    	SimpleState chaseGhostState = new SimpleState("chaseGhostState", new ChaseGhostAction());
    	SimpleState NoGoodMovesChaseGhostState = new SimpleState("NoGoodMovesChaseGhostState", new NoGoodMovesChaseGhostAction());
    	SimpleState eatPowerPillState = new SimpleState("eatPowerPillState", new EatPowerPillAction());
    	SimpleState BestMoveToEatChaseGhostActionState = new SimpleState("BestMoveToEatChaseGhostActionState", new BestMoveToEatChaseGhostAction());
    	
    	//SimpleState eatPillsState = new SimpleState("eatPillsState", new EatPillsAction());
    	SimpleState msPacManReappearsAction = new SimpleState("msPacManReappearsAction", new MsPacManReappearsAction());
    	SimpleState runAwayCageAction = new SimpleState("runAwayCageAction",new RunAwayCageAction());
    	
    	SimpleState escapeNearDeathEatState=new SimpleState("escapeNearDeathEatState",new EscapeNearDeathEatAction());
    	SimpleState noPillNoGoodMovesEatState= new SimpleState("noPillNoGoodMovesState",new NoPillNoGoodMovesEatAction());
    	SimpleState noPillGoodMovesEatState= new SimpleState("noPillGoodMovesState",new NoPillGoodMovesAction());
    	SimpleState pillGoodMovesEatState= new SimpleState("pillGoodMovesEatState",new EatPillGoodMoveAction());
    	SimpleState randomGoodMovesState= new SimpleState("randomGoodMovesState",new RandomGoodMoveEatAction());
    	SimpleState NoGoodMovesPatrolState = new SimpleState("NoGoodMovesPatrolState", new NoGoodMovesPatrolAction());
    	SimpleState LatterMovesPatrolState = new SimpleState("LatterMovesPatrolState", new LatterMovesPatrolAction());
    	SimpleState EatPillPatrolState = new SimpleState("EatPillPatrolState", new EatPillPatrolAction());
    	//Transition eatPillTransition = new EatPillTransition();
 
    	//CHASE GHOST /PERSEGUIR A UN GHOST
    	FSM cfsm1 = new FSM("ChaseGhosts");
   
    	//BestMoveToEatChaseGhostActionState: Ir por ruta  para comer la mayor cantidad  de ghost comestible
    	//chaseGhostState: Ir por ruta  que me acerca m�s a todos los Ghost comestibles
    	//eatPowerPillState:Comer PowerPills (debe estar en Good Moves)
    	//NoGoodMovesChaseGhostState: Ir por ruta  que me aleja m�s de todos los Chasing Ghost
    	 
    	cfsm1.add(chaseGhostState, new BestMoveToEatGoForEdibleGhostTransition("ChaseGhosts"), BestMoveToEatChaseGhostActionState);
    	cfsm1.add(chaseGhostState, new NoGoodMovesGoForEdibleGhostTransition("ChaseGhosts"), NoGoodMovesChaseGhostState);
    	cfsm1.add(chaseGhostState, new EatPowerPillTransition("ChaseGhosts"), eatPowerPillState);
    	
    	cfsm1.add(eatPowerPillState, new BestMoveToEatGoForEdibleGhostTransition("eatPowerPillState"), BestMoveToEatChaseGhostActionState);
    	cfsm1.add(eatPowerPillState,  new BestTowardsGoForEdibleGhostTransition("eatPowerPillState"), chaseGhostState);
    	cfsm1.add(eatPowerPillState,  new NoGoodMovesGoForEdibleGhostTransition("eatPowerPillState"), NoGoodMovesChaseGhostState);
    	
    	cfsm1.add(NoGoodMovesChaseGhostState, new BestMoveToEatGoForEdibleGhostTransition("NoGoodMovesChaseGhostState"), BestMoveToEatChaseGhostActionState);
    	cfsm1.add(NoGoodMovesChaseGhostState, new BestTowardsGoForEdibleGhostTransition("NoGoodMovesChaseGhostState"), chaseGhostState);
    	cfsm1.add(NoGoodMovesChaseGhostState, new EatPowerPillTransition("NoGoodMovesChaseGhostState"), eatPowerPillState);
    	
    	cfsm1.add(BestMoveToEatChaseGhostActionState, new BestTowardsGoForEdibleGhostTransition("BestMoveToEatChaseGhostActionState"), chaseGhostState);
    	cfsm1.add(BestMoveToEatChaseGhostActionState, new EatPowerPillTransition("BestMoveToEatChaseGhostActionState"), eatPowerPillState);
    	cfsm1.add(BestMoveToEatChaseGhostActionState, new NoGoodMovesGoForEdibleGhostTransition("BestMoveToEatChaseGhostActionState"), NoGoodMovesChaseGhostState);
    	
    	
    	cfsm1.ready(BestMoveToEatChaseGhostActionState);
    	CompoundState chase_Ghosts = new CompoundState("chase_Ghosts", cfsm1);
    	
    	FSM cfsm2 = new FSM("RunAwayGhosts");
    	
    	//escapeNearDeathEatState: No hay buenos movimientos posibles, hay chasing ghost cerca y no hay PowerPill cercana. Buscar que movimiento de escapar de los ghost se repite m�s
    	//noPillNoGoodMovesEatState:  hay chasing ghost cerca y no hay pill cercana. Buscar que movimiento me aleja m�s de los Chasing Ghost
    	//pillGoodMovesEatState: Comer Pills, el movimiento debe estar en los good Moves
    	//eatPowerPillState:Comer PowerPills 
    	//RandomGoodMoveEatAction:  de los good Moves, escoger uno random que no va a una PPill (si se puede)
    	//noPillGoodMovesEatState:  hay chasing ghost cerca y no hay pill cercana. Buscar que movimiento me aleja m�s de los Chasing Ghost que est� en los Good Moves
    	
    	cfsm2.add(escapeNearDeathEatState, new EatPowerPillTransition("escapeNearDeathEatState"), eatPowerPillState);
    	cfsm2.add(escapeNearDeathEatState, new NoPillNoGoodMoveTransition("escapeNearDeathEatState"), noPillNoGoodMovesEatState);
    	cfsm2.add(escapeNearDeathEatState,  new NoPillGoodMoveTransition("escapeNearDeathEatState"), noPillGoodMovesEatState);
    	cfsm2.add(escapeNearDeathEatState, new PillGoodMoveTransition("escapeNearDeathEatState"), pillGoodMovesEatState);
    	cfsm2.add(escapeNearDeathEatState, new RandomGoodMoveTransition("escapeNearDeathEatState"), randomGoodMovesState);
    	
    	cfsm2.add(noPillNoGoodMovesEatState, new EatPowerPillTransition("noPillNoGoodMovesEatState"), eatPowerPillState);
    	cfsm2.add(noPillNoGoodMovesEatState, new EscapeNearDeathEatTransition("noPillNoGoodMovesEatState"), escapeNearDeathEatState);
    	cfsm2.add(noPillNoGoodMovesEatState, new NoPillGoodMoveTransition("noPillNoGoodMovesEatState"), noPillGoodMovesEatState);
    	cfsm2.add(noPillNoGoodMovesEatState, new PillGoodMoveTransition("noPillNoGoodMovesEatState"), pillGoodMovesEatState);
    	cfsm2.add(noPillNoGoodMovesEatState, new RandomGoodMoveTransition("noPillNoGoodMovesEatState"), randomGoodMovesState);
    	
    	
    	cfsm2.add(noPillGoodMovesEatState, new EatPowerPillTransition("noPillGoodMovesEatState"), eatPowerPillState);
    	cfsm2.add(noPillGoodMovesEatState, new EscapeNearDeathEatTransition("noPillGoodMovesEatState"), escapeNearDeathEatState);
    	cfsm2.add(noPillGoodMovesEatState,  new NoPillNoGoodMoveTransition("noPillGoodMovesEatState"), noPillNoGoodMovesEatState);
    	cfsm2.add(noPillGoodMovesEatState,new PillGoodMoveTransition("noPillGoodMovesEatState"), pillGoodMovesEatState);
    	cfsm2.add(noPillGoodMovesEatState, new RandomGoodMoveTransition("noPillGoodMovesEatState"), randomGoodMovesState);
    	
    	cfsm2.add(pillGoodMovesEatState, new EatPowerPillTransition("pillGoodMovesEatState"), eatPowerPillState);
    	cfsm2.add(pillGoodMovesEatState,  new EscapeNearDeathEatTransition("pillGoodMovesEatState"), escapeNearDeathEatState);
    	cfsm2.add(pillGoodMovesEatState,  new NoPillNoGoodMoveTransition("pillGoodMovesEatState"), noPillNoGoodMovesEatState);
    	cfsm2.add(pillGoodMovesEatState, new NoPillGoodMoveTransition("pillGoodMovesEatState"), noPillGoodMovesEatState);
    	cfsm2.add(pillGoodMovesEatState, new RandomGoodMoveTransition("pillGoodMovesEatState"), randomGoodMovesState);
    	
    	
    	cfsm2.add(randomGoodMovesState, new EatPowerPillTransition("randomGoodMovesState"), eatPowerPillState);
    	cfsm2.add(randomGoodMovesState,   new EscapeNearDeathEatTransition("randomGoodMovesState"), escapeNearDeathEatState);
    	cfsm2.add(randomGoodMovesState,  new NoPillNoGoodMoveTransition("randomGoodMovesState"), noPillNoGoodMovesEatState);
    	cfsm2.add(randomGoodMovesState,  new NoPillGoodMoveTransition("randomGoodMovesState"), noPillGoodMovesEatState);
    	cfsm2.add(randomGoodMovesState, new PillGoodMoveTransition("randomGoodMovesState"), pillGoodMovesEatState);
    	
    	
    	//EatPowerPill->EatPill
    	cfsm2.add(eatPowerPillState, new EscapeNearDeathEatTransition("eatPowerPillState"), escapeNearDeathEatState);
    	cfsm2.add(eatPowerPillState, new NoPillNoGoodMoveTransition("eatPowerPillState"), noPillNoGoodMovesEatState);
    	cfsm2.add(eatPowerPillState, new NoPillGoodMoveTransition("eatPowerPillState"), noPillGoodMovesEatState);
    	cfsm2.add(eatPowerPillState, new PillGoodMoveTransition("eatPowerPillState"), pillGoodMovesEatState);
    	cfsm2.add(eatPowerPillState, new RandomGoodMoveTransition("eatPowerPillState"), randomGoodMovesState);
    	
    	//cfsm2.ready(noPillNoGoodMovesEatState); 4400; noPGM 4300; pillGM 4300;  random 4365; eatPP
    	cfsm2.ready(noPillNoGoodMovesEatState);
    	CompoundState runAway_Ghosts = new CompoundState("runAway_Ghosts", cfsm2);
    	
    	FSM cfsm3 = new FSM("Patrol");
    	//LatterMovesPatrolState: Movimento en escalera para evitar que rodeen al MsPacMan
    	//patrolState: mejor movimiento al vigilar. Puede ser alejarse o de los Chasing Ghost, acerca a los comestibles o random. Debe estar en los Good Moves
    	//escapeNearDeathEatState: No hay buenos movimientos posibles, hay chasing ghost cerca y no hay PowerPill cercana. Buscar que movimiento de escapar de los ghost se repite m�s
    	//noPillNoGoodMovesEatState:  hay chasing ghost cerca y no hay pill cercana. Buscar que movimiento me aleja m�s de los Chasing Ghost
    	//randomGoodMovesState: Random Action dentro de los Good Moves
    	//EatPillPatrolState: Comer Pills (debe estar en Good Moves el movimiento)
    	//NoGoodMovesPatrolState: mejor movimiento para alejarse de los Chasing Ghost
    	
    	//Patrol-> EatPills
    	cfsm3.add(LatterMovesPatrolState, new PatrolTransition("escapeNearDeathEatState"), patrolState);
    	cfsm3.add(LatterMovesPatrolState,  new EscapeNearDeathEatTransition("NoGoodMovesPatrolState"), escapeNearDeathEatState);
    	cfsm3.add(LatterMovesPatrolState, new PillGoodMovePatrolTransition("escapeNearDeathEatState"), EatPillPatrolState);
    	cfsm3.add(LatterMovesPatrolState,new NoPillNoGoodMoveTransition("NoGoodMovesPatrolState"), noPillNoGoodMovesEatState);
    	cfsm3.add(LatterMovesPatrolState, new NoPillGoodMoveTransition("NoGoodMovesPatrolState"), noPillGoodMovesEatState);
    	cfsm3.add(LatterMovesPatrolState,  new PillGoodMoveTransition("NoGoodMovesPatrolState"), pillGoodMovesEatState);
    	cfsm3.add(LatterMovesPatrolState, new RandomGoodMoveTransition("NoGoodMovesPatrolState"), randomGoodMovesState);
    	
    	cfsm3.add(patrolState, new NoGoodMovesGoForPatrolTransition("patrolState"), NoGoodMovesPatrolState);
    	cfsm3.add(patrolState, new LatterMovesPatrolTransition("escapeNearDeathEatState"), LatterMovesPatrolState);
    	cfsm3.add(patrolState,  new EscapeNearDeathEatTransition("patrolState"), escapeNearDeathEatState);
    	cfsm3.add(patrolState, new PillGoodMovePatrolTransition("escapeNearDeathEatState"), EatPillPatrolState);
    	cfsm3.add(patrolState,new NoPillNoGoodMoveTransition("patrolState"), noPillNoGoodMovesEatState);
    	cfsm3.add(patrolState, new NoPillGoodMoveTransition("patrolState"), noPillGoodMovesEatState);
    	cfsm3.add(patrolState,  new PillGoodMoveTransition("patrolState"), pillGoodMovesEatState);
    	cfsm3.add(patrolState, new RandomGoodMoveTransition("patrolState"), randomGoodMovesState);
    	cfsm3.add(patrolState, new NoGoodMovesGoForPatrolTransition("patrolState"), NoGoodMovesPatrolState);
    	
    	cfsm3.add(NoGoodMovesPatrolState, new PatrolTransition("escapeNearDeathEatState"), patrolState);
    	cfsm3.add(NoGoodMovesPatrolState, new LatterMovesPatrolTransition("escapeNearDeathEatState"), LatterMovesPatrolState);
    	cfsm3.add(NoGoodMovesPatrolState,  new EscapeNearDeathEatTransition("NoGoodMovesPatrolState"), escapeNearDeathEatState);
    	cfsm3.add(NoGoodMovesPatrolState, new PillGoodMovePatrolTransition("escapeNearDeathEatState"), EatPillPatrolState);
    	cfsm3.add(NoGoodMovesPatrolState,new NoPillNoGoodMoveTransition("NoGoodMovesPatrolState"), noPillNoGoodMovesEatState);
    	cfsm3.add(NoGoodMovesPatrolState, new NoPillGoodMoveTransition("NoGoodMovesPatrolState"), noPillGoodMovesEatState);
    	cfsm3.add(NoGoodMovesPatrolState,  new PillGoodMoveTransition("NoGoodMovesPatrolState"), pillGoodMovesEatState);
    	cfsm3.add(NoGoodMovesPatrolState, new RandomGoodMoveTransition("NoGoodMovesPatrolState"), randomGoodMovesState);
    	
    	cfsm3.add(EatPillPatrolState, new PatrolTransition("escapeNearDeathEatState"), patrolState);
    	cfsm3.add(EatPillPatrolState, new LatterMovesPatrolTransition("escapeNearDeathEatState"), LatterMovesPatrolState);
    	cfsm3.add(EatPillPatrolState,  new EscapeNearDeathEatTransition("NoGoodMovesPatrolState"), escapeNearDeathEatState);
    	cfsm3.add(EatPillPatrolState,new NoPillNoGoodMoveTransition("NoGoodMovesPatrolState"), noPillNoGoodMovesEatState);
    	cfsm3.add(EatPillPatrolState, new NoPillGoodMoveTransition("NoGoodMovesPatrolState"), noPillGoodMovesEatState);
    	cfsm3.add(EatPillPatrolState,  new PillGoodMoveTransition("NoGoodMovesPatrolState"), pillGoodMovesEatState);
    	cfsm3.add(EatPillPatrolState, new RandomGoodMoveTransition("NoGoodMovesPatrolState"), randomGoodMovesState);
    	
    	
    	//EatPills->Latter moves
    	cfsm3.add(escapeNearDeathEatState, new LatterMovesPatrolTransition("escapeNearDeathEatState"), LatterMovesPatrolState);
    	cfsm3.add(noPillNoGoodMovesEatState, new LatterMovesPatrolTransition("noPillNoGoodMovesEatState"), LatterMovesPatrolState);
    	cfsm3.add(noPillGoodMovesEatState, new LatterMovesPatrolTransition("noPillGoodMovesEatState"), LatterMovesPatrolState);
    	cfsm3.add(pillGoodMovesEatState, new LatterMovesPatrolTransition("pillGoodMovesEatState"), LatterMovesPatrolState);
    	cfsm3.add(randomGoodMovesState, new LatterMovesPatrolTransition("randomGoodMovesState"), LatterMovesPatrolState);
    	
    	//EatPills->Patrol
    	cfsm3.add(escapeNearDeathEatState, new PatrolTransition("escapeNearDeathEatState"), patrolState);
    	cfsm3.add(noPillNoGoodMovesEatState, new PatrolTransition("noPillNoGoodMovesEatState"), patrolState);
    	cfsm3.add(noPillGoodMovesEatState, new PatrolTransition("noPillGoodMovesEatState"), patrolState);
    	cfsm3.add(pillGoodMovesEatState, new PatrolTransition("pillGoodMovesEatState"), patrolState);
    	cfsm3.add(randomGoodMovesState, new PatrolTransition("randomGoodMovesState"), patrolState);
    	
       	//EatPills->PatrolNo GoodMoves
    	cfsm3.add(escapeNearDeathEatState, new NoGoodMovesGoForPatrolTransition("escapeNearDeathEatState"), NoGoodMovesPatrolState);
    	cfsm3.add(noPillNoGoodMovesEatState, new NoGoodMovesGoForPatrolTransition("noPillNoGoodMovesEatState"), NoGoodMovesPatrolState);
    	cfsm3.add(noPillGoodMovesEatState, new NoGoodMovesGoForPatrolTransition("noPillGoodMovesEatState"), NoGoodMovesPatrolState);
    	cfsm3.add(pillGoodMovesEatState, new NoGoodMovesGoForPatrolTransition("pillGoodMovesEatState"), NoGoodMovesPatrolState);
    	cfsm3.add(randomGoodMovesState, new NoGoodMovesGoForPatrolTransition("randomGoodMovesState"), NoGoodMovesPatrolState);
    	
     	//EatPills->Eat Piill
    	cfsm3.add(escapeNearDeathEatState, new PillGoodMovePatrolTransition("escapeNearDeathEatState"), EatPillPatrolState);
    	cfsm3.add(noPillNoGoodMovesEatState, new PillGoodMovePatrolTransition("noPillNoGoodMovesEatState"), EatPillPatrolState);
    	cfsm3.add(noPillGoodMovesEatState, new PillGoodMovePatrolTransition("noPillGoodMovesEatState"), EatPillPatrolState);
    	cfsm3.add(pillGoodMovesEatState, new PillGoodMovePatrolTransition("pillGoodMovesEatState"), EatPillPatrolState);
    	cfsm3.add(randomGoodMovesState, new PillGoodMovePatrolTransition("randomGoodMovesState"), EatPillPatrolState);
    
    	
    	//EatPills entre ellos
    	cfsm3.add(escapeNearDeathEatState, new NoPillNoGoodMoveTransition("escapeNearDeathEatStatePatrol"), noPillNoGoodMovesEatState);
    	cfsm3.add(escapeNearDeathEatState,  new NoPillGoodMoveTransition("escapeNearDeathEatStatePatrol"), noPillGoodMovesEatState);
    	cfsm3.add(escapeNearDeathEatState, new PillGoodMoveTransition("escapeNearDeathEatStatePatrol"), pillGoodMovesEatState);
    	cfsm3.add(escapeNearDeathEatState, new RandomGoodMoveTransition("escapeNearDeathEatStatePatrol"), randomGoodMovesState);
    	
    	
    	cfsm3.add(noPillNoGoodMovesEatState, new EscapeNearDeathEatTransition("noPillNoGoodMovesEatStatePatrol"), escapeNearDeathEatState);
    	cfsm3.add(noPillNoGoodMovesEatState, new NoPillGoodMoveTransition("noPillNoGoodMovesEatStatePatrol"), noPillGoodMovesEatState);
    	cfsm3.add(noPillNoGoodMovesEatState, new PillGoodMoveTransition("noPillNoGoodMovesEatStatePatrol"), pillGoodMovesEatState);
    	cfsm3.add(noPillNoGoodMovesEatState, new RandomGoodMoveTransition("noPillNoGoodMovesEatStatePatrol"), randomGoodMovesState);
    	
    	
    	cfsm3.add(noPillGoodMovesEatState, new EscapeNearDeathEatTransition("noPillGoodMovesEatStatePatrol"), escapeNearDeathEatState);
    	cfsm3.add(noPillGoodMovesEatState,  new NoPillNoGoodMoveTransition("noPillGoodMovesEatStatePatrol"), noPillNoGoodMovesEatState);
    	cfsm3.add(noPillGoodMovesEatState,new PillGoodMoveTransition("noPillGoodMovesEatStatePatrol"), pillGoodMovesEatState);
    	cfsm3.add(noPillGoodMovesEatState, new RandomGoodMoveTransition("noPillGoodMovesEatStatePatrol"), randomGoodMovesState);
    	
    	cfsm3.add(pillGoodMovesEatState,  new EscapeNearDeathEatTransition("pillGoodMovesEatStatePatrol"), escapeNearDeathEatState);
    	cfsm3.add(pillGoodMovesEatState,  new NoPillNoGoodMoveTransition("pillGoodMovesEatStatePatrol"), noPillNoGoodMovesEatState);
    	cfsm3.add(pillGoodMovesEatState, new NoPillGoodMoveTransition("pillGoodMovesEatStatePatrol"), noPillGoodMovesEatState);
    	cfsm3.add(pillGoodMovesEatState, new RandomGoodMoveTransition("pillGoodMovesEatStatePatrol"), randomGoodMovesState);
    	
    	
    	cfsm3.add(randomGoodMovesState,   new EscapeNearDeathEatTransition("randomGoodMovesStatePatrol"), escapeNearDeathEatState);
    	cfsm3.add(randomGoodMovesState,  new NoPillNoGoodMoveTransition("randomGoodMovesStatePatrol"), noPillNoGoodMovesEatState);
    	cfsm3.add(randomGoodMovesState,  new NoPillGoodMoveTransition("randomGoodMovesStatePatrol"), noPillGoodMovesEatState);
    	cfsm3.add(randomGoodMovesState, new PillGoodMoveTransition("randomGoodMovesStatePatrol"), pillGoodMovesEatState);
    	
    	
    	
    	cfsm3.ready(pillGoodMovesEatState);
    	CompoundState patrol = new CompoundState("patrol", cfsm3);
    	
    	fsm.add(msPacManReappearsAction, new GoForEdibleGhostTransition("msPacManReappears"), chase_Ghosts);
    	fsm.add(msPacManReappearsAction, new RunAwayFromCageTransition("msPacManReappears"), runAwayCageAction);
    	fsm.add(msPacManReappearsAction, new RunAwayFromChasingGhostTransition("msPacManReappears"), runAway_Ghosts);
    	fsm.add(msPacManReappearsAction, new BasicPatrolTransition("msPacManReappears"), patrol);
    	
    	//En Perseguir
    	fsm.add(chase_Ghosts, new BasicPatrolTransition("chase_Ghosts"), patrol);
    	fsm.add(chase_Ghosts,  new RunAwayFromChasingGhostTransition("chase_Ghosts"), runAway_Ghosts);
    	fsm.add(chase_Ghosts,  new MsPacManDiesTransition("chase_Ghosts"), msPacManReappearsAction);
    	
    	//En Huir Ghosts
    	fsm.add(runAway_Ghosts,  new GoForEdibleGhostTransition("runAway_Ghosts"), chase_Ghosts);
    	fsm.add(runAway_Ghosts,  new RunAwayFromCageTransition("runAway_Ghosts"), runAwayCageAction);
    	fsm.add(runAway_Ghosts, new BasicPatrolTransition("runAway_Ghosts"), patrol);
    	fsm.add(runAway_Ghosts,  new MsPacManDiesTransition("runAway_Ghosts"), msPacManReappearsAction);
    	
    	//En Huir Cage: Hay un ghost en la c�rcel a punto de salir, y msPacMan esta cerca. MsPacMan tratar� de alejarse con el mejor movimiento posible
    	fsm.add(runAwayCageAction,  new GoForEdibleGhostTransition("runAwayCage"), chase_Ghosts);
    	fsm.add(runAwayCageAction, new RunAwayFromChasingGhostTransition("runAwayCage"), runAway_Ghosts);
    	fsm.add(runAwayCageAction, new BasicPatrolTransition("runAwayCage"), patrol);
    	fsm.add(runAwayCageAction,  new MsPacManDiesTransition("runAwayCage"), msPacManReappearsAction);
    	
    	//En Patrol Cage
       	fsm.add(patrol,   new GoForEdibleGhostTransition("patrol"), chase_Ghosts);
    	fsm.add(patrol, new RunAwayFromChasingGhostTransition("patrol"), runAway_Ghosts);
    	fsm.add(patrol,  new RunAwayFromCageTransition("patrol"), runAwayCageAction);
    	fsm.add(patrol,  new MsPacManDiesTransition("patrol"), msPacManReappearsAction);
    	
    	fsm.ready(msPacManReappearsAction);
    	
    	/*
    	JFrame frame = new JFrame();
    	JPanel main = new JPanel();
    	main.setLayout(new BorderLayout());
    	main.add(observer.getAsPanel(true, null), BorderLayout.CENTER);
    	//main.add(c1observer.getAsPanel(true, null), BorderLayout.SOUTH);
    	frame.getContentPane().add(main);
    	frame.pack();
    	frame.setVisible(true);
    	*/
	}
	
	
	public void preCompute(String opponent) {
    		fsm.reset();
    }
	
	
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	int i=0;
    	if(game.isJunction(game.getPacmanCurrentNodeIndex()))
    		i=1;
       	Input in = new MsPacManInput(game); 
    	return fsm.run(in);
    }
    
    
}