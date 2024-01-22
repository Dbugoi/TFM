;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot cell (type SYMBOL))
	(slot pacDist (type NUMBER))
	(slot closestPPillDist (type NUMBER))
	(slot danger (type NUMBER))
	(slot pacmanSeparation (type NUMBER))
	(slot canGhostEat (type SYMBOL))
	(slot canGhostTrap (type SYMBOL))
	(slot withinTheConeBack (type SYMBOL))
	(slot withinTheConeFront (type SYMBOL))
	(slot distanceToEdibleGhost (type NUMBER)))

(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot cell (type SYMBOL))
	(slot pacDist (type NUMBER))
	(slot closestPPillDist (type NUMBER))
	(slot danger (type NUMBER))
	(slot pacmanSeparation (type NUMBER))
	(slot canGhostEat (type SYMBOL))
	(slot canGhostTrap (type SYMBOL))
	(slot withinTheConeBack (type SYMBOL))
	(slot withinTheConeFront (type SYMBOL))
	(slot distanceToEdibleGhost (type NUMBER)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot cell (type SYMBOL))
	(slot pacDist (type NUMBER))
	(slot closestPPillDist (type NUMBER))
	(slot danger (type NUMBER))
	(slot pacmanSeparation (type NUMBER))
	(slot canGhostEat (type SYMBOL))
	(slot canGhostTrap (type SYMBOL))
	(slot withinTheConeBack (type SYMBOL))
	(slot withinTheConeFront (type SYMBOL))
	(slot distanceToEdibleGhost (type NUMBER)))

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot cell (type SYMBOL))
	(slot pacDist (type NUMBER))
	(slot closestPPillDist (type NUMBER))
	(slot danger (type NUMBER))
	(slot pacmanSeparation (type NUMBER))
	(slot canGhostEat (type SYMBOL))
	(slot canGhostTrap (type SYMBOL))
	(slot withinTheConeBack (type SYMBOL))
	(slot withinTheConeFront (type SYMBOL))
	(slot distanceToEdibleGhost (type NUMBER)))
	
(deftemplate MSPACMAN 
    (slot pacmanClosestPPillDist (type NUMBER))
    (slot closestPacmanPPill (type NUMBER))
    (slot closestEdibleGhost (type NUMBER)))

(deftemplate VARIABLES 
    (slot maxDistChase (type NUMBER))
	(slot maxDistTrapPacmanPPill (type NUMBER))
	(slot maxDistSaveEdible (type NUMBER))
	(slot dangerThreshold (type NUMBER))
	(slot fleeDistance (type NUMBER))
	(slot dangerousDistance (type NUMBER))
)

(deftemplate VERSION
	(slot version (type NUMBER))
)

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
) 

(deffacts variables-hardclipped
	(VARIABLES (maxDistChase 30) (maxDistTrapPacmanPPill 100) (maxDistSaveEdible 100) (dangerThreshold 0.45)
	 (fleeDistance 35) (dangerousDistance 51))
)

(deffacts hechos-asertados
	(VERSION (version 17))
	;(MSPACMAN (pacmanClosestPPillDist 20)(closestPacmanPPill 20) (closestEdibleGhost 20))
	;(INKY (edible false) (cell false) (pacDist 10) (closestPPillDist 10) (danger 10) (pacmanSeparation 10) (canGhostEat false) 
	;(canGhostTrap false) (withinTheConeBack false) (withinTheConeFront true) (distanceToEdibleGhost 10)) 
)
   
;RULES 
;CHASE
(defrule ChasePacmanRule
	(MSPACMAN (pacmanClosestPPillDist ?d))
	(INKY (cell false) (edible false) (distanceToEdibleGhost ?distanceEdibleG))
	(VARIABLES (maxDistChase ?maxChase))
	(test  ( or (<= ?d ?maxChase) (not(> ?distanceEdibleG -1)))) 
	=>  
	(assert (ACTION (id INKYgoToPacmanInter) (info "INKY persigue a Pacman ") (priority 9))
	)
)
;prioritario, sabes que te lo vas a comer
(defrule ChasePacmanRule2
	(INKY (canGhostEat true) (edible false))	 
	=>  
	(assert (ACTION (id INKYchases) (info "INKY persigue a Pacman y lo noquea") (priority 15))
	)
)

;TRAP segundo mas prioritario
(defrule GoToPacmanInterRule
	(INKY (cell false) (canGhostTrap true))
	=>  
	(assert (ACTION (id INKYgoToPacmanInter) (info "INKY encierra a Pacman ") (priority 14))) )

(defrule GoToEdibleGhostRule
	(INKY (cell false) (edible false) (distanceToEdibleGhost ?c)) 
	(VARIABLES (maxDistSaveEdible ?maxSee))
	(MSPACMAN (closestEdibleGhost ?d)) (test (>= ?d 2)) (test (<= ?c ?maxSee))
	=>  
	(assert (ACTION (id INKYgoesToEdibleGhost) (info "INKY protege a fantasma comestible") (priority 13))))
(defrule GoToSafestInterRule
	(INKY (edible false) (cell false) (danger ?d)) 
	(VARIABLES (dangerThreshold ?maxDank))
	(test (> ?d ?maxDank))
	=> 
	(assert (ACTION (id INKYgoesToSafestInter) (info "INKY esta en una posicion peligrosa") (priority 11))))

(defrule GoToClosestPPillRule
	(INKY (edible false) (closestPPillDist ?c)) 
	(VARIABLES (maxDistTrapPacmanPPill ?maxTrap))
	(MSPACMAN (closestPacmanPPill ?d))  (test (< ?c ?maxTrap)) (test (> ?d ?c ))
	=> 
	(assert (ACTION (id INKYgoesToClosestPPill) (info "INKY va a PPill mas cercana") (priority 10))))
(defrule CopyPacman
	(INKY (cell false) (withinTheConeBack true) (edible false))
	=>
	(assert (ACTION (id INKYcopies) (info "INKY copia a Pacman") (priority 10))
	)
)
(defrule GoNextPacmanJunctionRule
	(INKY (edible false) (cell false) (withinTheConeFront true))
	=> 
	(assert (ACTION (id INKYgoToPacmanInter) (info "INKY se pone en la siguiente interseccion de Pacman") (priority 10))))
(defrule SeparatePacmanRule
	(INKY (edible false) (cell false) (withinTheConeBack true))
	=> 
	(assert (ACTION (id INKYseparatesPacman) (info "INKY se separa de Pacman") (priority 10))))

;FLEE
;la principal de huir
(defrule RunAwayToAliveGhostRule
	(INKY (cell false) (edible true) (pacDist ?GhostToPacDist) (closestPPillDist ?PPillDist) (pacmanSeparation ?PacmanToGhostDist))
	(VARIABLES (fleeDistance ?fleeDist))
	(test (and (>= ?fleeDist ?PacmanToGhostDist) ( and (>= ?GhostToPacDist ?PPillDist) (>= ?PPillDist ?fleeDist) ) ))
	=>  
	(assert (ACTION (id INKYrunAwayToAliveGhost) (info "INKY huye a otro vivo")(priority 13))) )
;huye tambien si es comestible
(defrule RunAwayToAliveGhostRule2
	(INKY (cell false) (edible true))
	=>  
	(assert (ACTION (id INKYrunAwayToAliveGhost) (info "INKY huye a otro vivo")(priority 12))) )

(defrule RunAwayFromPacmanRule
	(INKY (cell false) (pacmanSeparation ?d) (edible true))
	(VARIABLES (fleeDistance ?fleeDist))
	(test (< ?d ?fleeDist))
	=>  
	(assert (ACTION (id INKYrunAwayFromPacman) (info "INKY huye de Pacman") (priority 12)))
)