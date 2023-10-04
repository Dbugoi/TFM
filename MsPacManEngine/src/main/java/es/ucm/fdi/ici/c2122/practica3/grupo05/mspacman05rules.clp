;FACTS ASSERTED BY GAME INPUT

;Basic facts
(deftemplate MSPACMAN 
    (slot Chase (type SYMBOL))
	(slot RunAway (type SYMBOL))
	(slot RunAwayFromCage (type SYMBOL))
	(slot Patrol (type SYMBOL)) 
	(slot MsPacManReappearsAction (type SYMBOL)) 
 )
 ;Precise facts
 (deftemplate PRECISE_FACT_CHASE
    (slot Chase (type SYMBOL))
    (slot BestMoveToEatChaseGhost (type SYMBOL))
	(slot ChaseGhostAction (type SYMBOL))
	(slot EatPowerPillActionChase (type SYMBOL))
	(slot NoGoodMovesChaseGhostAction (type SYMBOL)) 
	(slot NoGoodMovesChaseGhostAction (type SYMBOL))
	(slot info (default "")) 
 )
 
 (deftemplate PRECISE_FACT_RUNAWAYCAGE
    (slot RunAwayFromCage (type SYMBOL))
   (slot RunAwayCageAction (type SYMBOL))
   (slot info (default ""))
 )
  
 (deftemplate PRECISE_FACT_RUNAWAY
  	(slot ChasingGhostNear (type SYMBOL))
  	(slot BetterEatPowerPill (type SYMBOL))
    (slot RunAway (type SYMBOL))
   	(slot noPillNoGoodMovesEatState (type SYMBOL))
    (slot eatPowerPillStateRun (type SYMBOL))
    (slot EscapeNearDeathEatAction (type SYMBOL))
    (slot NoPillGoodMovesAction (type SYMBOL))
    (slot EatPillGoodMoveAction (type SYMBOL))
    (slot RandomGoodMoveEatAction (type SYMBOL)) 
    (slot BetterGoToPill (type SYMBOL))
    (slot ChasingClose (type SYMBOL))
    (slot info (default ""))
 )
 
 (deftemplate PRECISE_FACT_PATROL
    (slot Patrol (type SYMBOL)) 
    (slot EatPillGoodMoveActionPatrol (type SYMBOL))
    (slot LatterMovesPatrolAction (type SYMBOL))
    (slot PatrolAction (type SYMBOL))
    (slot NoGoodMovesPatrolAction (type SYMBOL))
    (slot EscapeNearDeathEatActionPatrol (type SYMBOL))
    (slot NoPillNoGoodMovesEatAction (type SYMBOL))
    (slot NoPillGoodMovesActionPatrol (type SYMBOL))
    (slot RandomGoodMoveEatActionPatrol (type SYMBOL))
    (slot BetterGoToPill (type SYMBOL))
    (slot info (default ""))
 )
 
 (deftemplate PRECISE_FACT_MSPACMANREAPPEARS
    (slot MsPacManReappearsAction (type SYMBOL))
    (slot MsPacManReappears (type SYMBOL))
    (slot info (default ""))
 )
 
 (deftemplate BASIC_INFORMATION
    (slot mindistanceChasingGhostOrChasingGhostInCage (type NUMBER))
    (slot mindistancePowerPill (type NUMBER))
 )
 
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default ""))  (slot priority (type NUMBER) ) ; mandatory slots 
) 
   
;RULES 

;BASICRULES

(defrule ChasingGhostsAreNear
	(BASIC_INFORMATION (mindistanceChasingGhostOrChasingGhostInCage ?d)) (test (<= ?d 66)) 
	=>  
	(assert (PRECISE_FACT_RUNAWAY (ChasingGhostNear true)(info "Ghost near MsPacMan distance<=65")
			)
	)
)

(defrule NearPowerPill
	(BASIC_INFORMATION (mindistancePowerPill ?d)) (test (<= ?d 301)) 
	=>  
	(assert (PRECISE_FACT_RUNAWAY (BetterEatPowerPill true)(info "MsPacMan has to ear PowerPill")
			)
	)
)



(defrule BasicChase
	(MSPACMAN (Chase true))
	=>  
	(assert (PRECISE_FACT_CHASE (Chase true) (info "Edible Ghost exists")
			)
	)
)
(defrule BasicRunAway
	(MSPACMAN (RunAway true))
	=>  
	(assert (PRECISE_FACT_RUNAWAY (RunAway true)(info "MsPacMan is better that MsPacMan runs away")
			)
	)
)


(defrule BasicRunAwayFromCage
	(MSPACMAN (RunAwayFromCage true))
	=>  
	(assert (PRECISE_FACT_RUNAWAYCAGE (RunAwayFromCage true) (info "Danger in Cage, Ghosts getting out and MsPacMan is near")
			)
	)
)
(defrule BasicPatrol
	(MSPACMAN (Patrol true))
	=>  
	(assert (PRECISE_FACT_PATROL (Patrol true) (info "edible ghosts does not exists and chasing ghost are not near by")
			)
	)
)

(defrule BasicMsPacManReappears
	(MSPACMAN (MsPacManReappearsAction true))
	=>  
	(assert (PRECISE_FACT_MSPACMANREAPPEARS (MsPacManReappears true) (info "MsPacMan was eaten, is going to reappear")
			)
	)
)





;CHASE

(defrule BestMoveToEatChaseGhostAction
	(PRECISE_FACT_CHASE (BestMoveToEatChaseGhost true))(PRECISE_FACT_CHASE (Chase true))
	=>  
	(assert (ACTION (id BestMoveToEatChaseGhost) (info "MsPacMan has best move towards edible ghost") (priority 150) 
			)
	)
)
	
(defrule ChaseGhostAction
	(PRECISE_FACT_CHASE (ChaseGhostAction true))(PRECISE_FACT_CHASE (Chase true))
	=>  
	(assert (ACTION (id ChaseGhostAction) (info "MsPacMan is going to chase edible ghost") (priority 140) 
				)
	)
)

(defrule EatPowerPillActionChase
	(PRECISE_FACT_CHASE (EatPowerPillActionChase true))(PRECISE_FACT_CHASE (Chase true))(PRECISE_FACT_RUNAWAY (BetterEatPowerPill true))
	=>  
	(assert (ACTION (id EatPowerPillActionChase) (info "MsPacMan is going to eat Powe Pill ghost") (priority 130) 
				)
	)
)

(defrule NoGoodMovesChaseGhostAction
	(PRECISE_FACT_CHASE (NoGoodMovesChaseGhostAction true))(PRECISE_FACT_CHASE (Chase true))
	=>  
	(assert (ACTION (id NoGoodMovesChaseGhostAction) (info "MsPacMan is going to chase edible ghost, no good moves") (priority 125) 
				)
	)
)

;CAGE
(defrule RunAwayCageAction
	(PRECISE_FACT_RUNAWAYCAGE (RunAwayCageAction true))(PRECISE_FACT_RUNAWAYCAGE (RunAwayFromCage true))
	=>  
	(assert (ACTION (id RunAwayCageAction) (info "MsPacMan is going to run away from cage") (priority 120) 
				)
	)
)

;RUNNING
(defrule noPillNoGoodMovesEatState
	(PRECISE_FACT_RUNAWAY (noPillNoGoodMovesEatState true))(PRECISE_FACT_RUNAWAY (RunAway true))(PRECISE_FACT_RUNAWAY (ChasingGhostNear true))
	=>  
	(assert (ACTION (id noPillNoGoodMovesEatState) (info "MsPacMan is running away, no pill no good moves") (priority 120) 
				)
	)
)

(defrule eatPowerPillStateRun
	(PRECISE_FACT_RUNAWAY (eatPowerPillStateRun true))(PRECISE_FACT_RUNAWAY (RunAway true))(PRECISE_FACT_RUNAWAY (ChasingGhostNear true))(PRECISE_FACT_RUNAWAY (BetterEatPowerPill true))
	=>  
	(assert (ACTION (id eatPowerPillStateRun) (info "MsPacMan is running away, eat PP") (priority 110) 
				)
	)
)

(defrule EscapeNearDeathEatAction
	(PRECISE_FACT_RUNAWAY (EscapeNearDeathEatAction true))(PRECISE_FACT_RUNAWAY (RunAway true))(PRECISE_FACT_RUNAWAY (ChasingGhostNear true))
	=>  
	(assert (ACTION (id EscapeNearDeathEatAction) (info "MsPacMan is running away, escape near death") (priority 105) 
				)
	)
)

(defrule NoPillGoodMovesAction
	(PRECISE_FACT_RUNAWAY (NoPillGoodMovesAction true))(PRECISE_FACT_RUNAWAY (RunAway true))(PRECISE_FACT_RUNAWAY (ChasingGhostNear true))
	=>  
	(assert (ACTION (id NoPillGoodMovesAction) (info "MsPacMan is running away, no pill GOOD moves") (priority 100) 
				)
	)
)

(defrule EatPillGoodMoveAction
	(PRECISE_FACT_RUNAWAY (EatPillGoodMoveAction true))(PRECISE_FACT_RUNAWAY (RunAway true))(PRECISE_FACT_RUNAWAY (ChasingGhostNear true))
	=>  
	(assert (ACTION (id EatPillGoodMoveAction) (info "MsPacMan is running away, eat pill GOOD move") (priority 90) 
				)
	)
)

(defrule RandomGoodMoveEatAction
	(PRECISE_FACT_RUNAWAY (RandomGoodMoveEatAction true))(PRECISE_FACT_RUNAWAY (RunAway true))(PRECISE_FACT_RUNAWAY (ChasingGhostNear true))
	=>  
	(assert (ACTION (id RandomGoodMoveEatAction) (info "MsPacMan is running away, random") (priority 80) 
				)
	)
)

;Patrol

(defrule EatPillGoodMoveActionPatrol
	(PRECISE_FACT_PATROL (EatPillGoodMoveActionPatrol true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id EatPillGoodMoveActionPatrol) (info "MsPacMan is patroling , eat pill with GOOD move") (priority 70) 
				)
	)
)

(defrule LatterMovesPatrolAction
	(PRECISE_FACT_PATROL (LatterMovesPatrolAction true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id LatterMovesPatrolAction) (info "MsPacMan patroling, latter moves") (priority 65) 
				)
	)
)

(defrule PatrolAction
	(PRECISE_FACT_PATROL (PatrolAction true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id PatrolAction) (info "MsPacMan is patroling, basic, good") (priority 60) 
				)
	)
)

(defrule NoGoodMovesPatrolAction
	(PRECISE_FACT_PATROL (NoGoodMovesPatrolAction true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id NoGoodMovesPatrolAction) (info "MsPacMan patroling, no good moves") (priority 55) 
				)
	)
)

(defrule BetterGoToPillAction
	(PRECISE_FACT_PATROL (BetterGoToPill true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id BetterGoToPill) (info "MsPacMan better goes to pill") (priority 52) 
				)
	)
)
	
(defrule EscapeNearDeathEatActionPatrol
	(PRECISE_FACT_PATROL (EscapeNearDeathEatActionPatrol true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id EscapeNearDeathEatActionPatrol) (info "MsPacMan is patroling, escape  death") (priority 50) 
				)
	)
)

(defrule NoPillNoGoodMovesEatAction
	(PRECISE_FACT_PATROL (NoPillNoGoodMovesEatAction true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id NoPillNoGoodMovesEatAction) (info "MsPacMan is patroling, no good moves, no pill") (priority 45) 
				)
	)
)

(defrule NoPillGoodMovesActionPatrol
	(PRECISE_FACT_PATROL (NoPillGoodMovesActionPatrol true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id NoPillGoodMovesActionPatrol) (info "MsPacMan is patroling, no pill , GOOD moves") (priority 40) 
				)
	)
)

(defrule RandomGoodMoveEatActionPatrol
	(PRECISE_FACT_PATROL (RandomGoodMoveEatActionPatrol true))(PRECISE_FACT_PATROL (Patrol true))
	=>  
	(assert (ACTION (id RandomGoodMoveEatActionPatrol) (info "MsPacMan is patroling, random") (priority 30) 
				)
	)
)

(defrule MsPacManReappearsAction
	(PRECISE_FACT_MSPACMANREAPPEARS (MsPacManReappearsAction true))(PRECISE_FACT_MSPACMANREAPPEARS (MsPacManReappears true))
	=>  
	(assert (ACTION (id MsPacManReappearsAction) (info "MsPacMan reappears") (priority 20) 
				)
	)
)
	