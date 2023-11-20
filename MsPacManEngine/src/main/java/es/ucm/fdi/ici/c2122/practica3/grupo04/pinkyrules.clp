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
	;(PINKY (edible false) (cell false) (pacDist 10) (closestPPillDist 10) (danger 10) (pacmanSeparation 10) (canGhostEat false) 
	;(canGhostTrap false) (withinTheConeBack false) (withinTheConeFront true) (distanceToEdibleGhost 10)) 
)
   
;RULES 
;CHASE
(defrule ChasePacmanRule
	(MSPACMAN (pacmanClosestPPillDist ?d))
	(PINKY (cell false) (edible false) (distanceToEdibleGhost ?distanceEdibleG))
	(VARIABLES (maxDistChase ?maxChase))
	(test  ( or (<= ?d ?maxChase) (not(> ?distanceEdibleG -1)))) 
	=>  
	(assert (ACTION (id PINKYgoToPacmanInter) (info "PINKY persigue a Pacman ") (priority 9))
	)
)
;prioritario, sabes que te lo vas a comer
(defrule ChasePacmanRule2
	(PINKY (canGhostEat true) (edible false))	 
	=>  
	(assert (ACTION (id PINKYchases) (info "PINKY persigue a Pacman y lo noquea") (priority 15))
	)
)

;TRAP segundo mas prioritario
(defrule GoToPacmanInterRule
	(PINKY (cell false) (canGhostTrap true))
	=>  
	(assert (ACTION (id PINKYgoToPacmanInter) (info "PINKY encierra a Pacman ") (priority 14))) )

(defrule GoToEdibleGhostRule
	(PINKY (cell false) (edible false) (distanceToEdibleGhost ?c)) 
	(VARIABLES (maxDistSaveEdible ?maxSee))
	(MSPACMAN (closestEdibleGhost ?d)) (test (>= ?d 2)) (test (<= ?c ?maxSee))
	=>  
	(assert (ACTION (id PINKYgoesToEdibleGhost) (info "PINKY protege a fantasma comestible") (priority 13))))

(defrule GoToSafestInterRule
	(PINKY (edible false) (cell false) (danger ?d)) 
	(VARIABLES (dangerThreshold ?maxDank))
	(test (> ?d ?maxDank))
	=> 
	(assert (ACTION (id PINKYgoesToSafestInter) (info "PINKY esta en una posicion peligrosa") (priority 11))))

(defrule GoToClosestPPillRule
	(PINKY (edible false) (closestPPillDist ?c)) 
	(VARIABLES (maxDistTrapPacmanPPill ?maxTrap))
	(MSPACMAN (closestPacmanPPill ?d))  (test (< ?c ?maxTrap)) (test (> ?d ?c ))
	=> 
	(assert (ACTION (id PINKYgoesToClosestPPill) (info "PINKY va a PPill mas cercana") (priority 10))))

(defrule CopyPacman
	(PINKY (cell false) (withinTheConeBack true) (edible false))
	=>
	(assert (ACTION (id PINKYcopies) (info "PINKY copia a Pacman") (priority 10))
	)
)
(defrule GoNextPacmanJunctionRule
	(PINKY (edible false) (cell false) (withinTheConeFront true))
	=> 
	(assert (ACTION (id PINKYgoToPacmanInter) (info "PINKY se pone en la siguiente interseccion de Pacman") (priority 10))))
(defrule SeparatePacmanRule
	(PINKY (edible false) (cell false) (withinTheConeBack true))
	=> 
	(assert (ACTION (id PINKYseparatesPacman) (info "PINKY se separa de Pacman") (priority 10))))

;FLEE
;la principal de huir
(defrule RunAwayToAliveGhostRule
	(PINKY (cell false) (edible true) (pacDist ?GhostToPacDist) (closestPPillDist ?PPillDist) (pacmanSeparation ?PacmanToGhostDist))
	(VARIABLES (fleeDistance ?fleeDist))
	(test (and (>= ?fleeDist ?PacmanToGhostDist) ( and (>= ?GhostToPacDist ?PPillDist) (>= ?PPillDist ?fleeDist) ) ))
	=>  
	(assert (ACTION (id PINKYrunAwayToAliveGhost) (info "PINKY huye a otro vivo")(priority 13))) )
;huye tambien si es comestible
(defrule RunAwayToAliveGhostRule2
	(PINKY (cell false) (edible true))
	=>  
	(assert (ACTION (id PINKYrunAwayToAliveGhost) (info "PINKY huye a otro vivo")(priority 12))) )

(defrule RunAwayFromPacmanRule
	(PINKY (cell false) (pacmanSeparation ?d) (edible true))
	(VARIABLES (fleeDistance ?fleeDist))
	(test (< ?d ?fleeDist))
	=>  
	(assert (ACTION (id PINKYrunAwayFromPacman) (info "PINKY huye de Pacman") (priority 12)))
)