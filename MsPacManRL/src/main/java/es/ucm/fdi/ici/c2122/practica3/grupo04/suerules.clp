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
	;(SUE (edible false) (cell false) (pacDist 10) (closestPPillDist 10) (danger 10) (pacmanSeparation 10) (canGhostEat false) 
	;(canGhostTrap false) (withinTheConeBack false) (withinTheConeFront true) (distanceToEdibleGhost 10)) 
)
   
;RULES 
;CHASE
(defrule ChasePacmanRule
	(MSPACMAN (pacmanClosestPPillDist ?d))
	(SUE (cell false) (edible false) (distanceToEdibleGhost ?distanceEdibleG))
	(VARIABLES (maxDistChase ?maxChase))
	(test  ( or (<= ?d ?maxChase) (not(> ?distanceEdibleG -1)))) 
	=>  
	(assert (ACTION (id SUEgoToPacmanInter) (info "SUE persigue a Pacman ") (priority 9))
	)
)

;prioritario, sabes que te lo vas a comer
(defrule ChasePacmanRule2
	(SUE (cell false) (canGhostEat true) (edible false))	 
	=>  
	(assert (ACTION (id SUEchases) (info "SUE persigue a Pacman y lo noquea") (priority 15))
	)
)

;TRAP segundo mas prioritario
(defrule GoToPacmanInterRule
	(SUE (cell false) (canGhostTrap true))
	=>  
	(assert (ACTION (id SUEgoToPacmanInter) (info "SUE encierra a Pacman ") (priority 14))) )

(defrule GoToEdibleGhostRule
	(SUE (cell false) (edible false) (distanceToEdibleGhost ?c)) 
	(VARIABLES (maxDistSaveEdible ?maxSee))
	(MSPACMAN (closestEdibleGhost ?d)) (test (>= ?d 2)) (test (<= ?c ?maxSee))
	=>  
	(assert (ACTION (id SUEgoesToEdibleGhost) (info "SUE protege a fantasma comestible") (priority 13))))


(defrule GoToSafestInterRule
	(SUE (edible false) (cell false) (danger ?d)) 
	(VARIABLES (dangerThreshold ?maxDank))
	(test (> ?d ?maxDank))
	=> 
	(assert (ACTION (id SUEgoesToSafestInter) (info "SUE esta en una posicion peligrosa") (priority 11))))

(defrule GoToClosestPPillRule
	(SUE (edible false) (closestPPillDist ?c)) 
	(VARIABLES (maxDistTrapPacmanPPill ?maxTrap))
	(MSPACMAN (closestPacmanPPill ?d))  (test (< ?c ?maxTrap)) (test (> ?d ?c ))
	=> 
	(assert (ACTION (id SUEgoesToClosestPPill) (info "SUE va a PPill mas cercana") (priority 10))))
(defrule CopyPacman
	(SUE (cell false) (withinTheConeBack true) (edible false))
	=>
	(assert (ACTION (id SUEcopies) (info "SUE copia a Pacman") (priority 10))
	)
)
(defrule SeparatePacmanRule
	(SUE (edible false) (cell false) (withinTheConeBack true))
	=> 
	(assert (ACTION (id SUEseparatesPacman) (info "SUE se separa de Pacman") (priority 10))))

(defrule GoNextPacmanJunctionRule
	(SUE (edible false) (cell false) (withinTheConeFront true))
	=> 
	(assert (ACTION (id SUEgoToPacmanInter) (info "SUE se pone en la siguiente interseccion de Pacman") (priority 10))))


;FLEE
;la principal de huir
(defrule RunAwayToAliveGhostRule
	(SUE (cell false) (edible true) (pacDist ?GhostToPacDist) (closestPPillDist ?PPillDist) (pacmanSeparation ?PacmanToGhostDist))
	(VARIABLES (fleeDistance ?fleeDist))
	(test (and (>= ?fleeDist ?PacmanToGhostDist ) ( and (>= ?GhostToPacDist ?PPillDist) (>= ?PPillDist ?fleeDist) ) ))
	=>  
	(assert (ACTION (id SUErunAwayToAliveGhost) (info "SUE huye a otro vivo no comestible")(priority 13))) )
;huye tambien si es comestible
(defrule RunAwayToAliveGhostRule2
	(SUE (cell false) (edible true))
	=>  
	(assert (ACTION (id SUErunAwayToAliveGhost) (info "SUE huye a otro vivo no comestible")(priority 12))) )

(defrule RunAwayFromPacmanRule
	(SUE (cell false) (pacmanSeparation ?d) (edible true))
	(VARIABLES (fleeDistance ?fleeDist))
	(test (< ?d ?fleeDist))
	=>  
	(assert (ACTION (id SUErunAwayFromPacman) (info "SUE huye de Pacman") (priority 12)))
)