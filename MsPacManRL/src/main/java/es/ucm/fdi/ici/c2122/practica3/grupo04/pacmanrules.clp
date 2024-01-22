;FACTS ASSERTED BY GAME INPUT
(deftemplate MSPACMAN
	(slot alreadyDead (type SYMBOL)) ;Si esta muerta ya	
	(slot fleeing (type SYMBOL)) ;Si esta huyendo
    (slot ghostEdible (type SYMBOL)) ;Si hay fantasmas comestibles
	(slot canEatGroup (type SYMBOL)) ;Si hay un grupo comestible
	(slot groupNearbyClosestPPill (type SYMBOL)) ;Si hay un grupo cerca de la PPill mas cercana
	(slot closestPillDist (type NUMBER)) ;Distancia a la Pill mas cercana
	(slot closestPPillDist (type NUMBER)) ;Distancia a la que esta la closest PPill
	(slot groupNearbyPPill (type NUMBER)) ;Indice de la PPill con un grupo cerca, -1 si no
	(slot closestGhostDist (type NUMBER)) ;Distancia del fantasma al pacman, -1 si no hay
)

(deftemplate VARIABLES 
    (slot tooFarPPillDist (type NUMBER))
    (slot closeToPPillDist (type NUMBER))
	(slot avoidPPillDist (type NUMBER))
	(slot maxPillDist (type NUMBER))
)

(deftemplate VERSION
	(slot version (type NUMBER))
)

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
) 

(deffacts variables-hardclipped
	(VARIABLES (tooFarPPillDist 100) (avoidPPillDist 35) (maxPillDist 50))
)

(deffacts hechos-asertados
	(VERSION (version 18))
	;(MSPACMAN (pacmanClosestPPillDist 20)(closestPacmanPPill 20) (closestEdibleGhost 20))
	;(PINKY (edible false) (cell false) (pacDist 10) (closestPPillDist 10) (danger 10) (pacmanSeparation 10) (canGhostEat false) 
	;(canGhostTrap false) (withinTheConeBack false) (withinTheConeFront true) (distanceToEdibleGhost 10)) 
)

; CHASE
(defrule ChaseGhostRule
	(MSPACMAN (fleeing false) (ghostEdible true) (canEatGroup false))
	=>  
	(assert (ACTION (id PACMANchaseGhost) (info "Pacman va a comer un fantasma")(priority 1))) )

(defrule ChaseGroupRule
	(MSPACMAN (fleeing false) (ghostEdible true) (canEatGroup false))
	=>  
	(assert (ACTION (id PACMANchaseGroup) (info "Pacman va a comer un grupo")(priority 1))) )

(defrule AvoidBeingFarFromPPillRule
	(MSPACMAN (fleeing false) (closestPPillDist ?ppill))
	(VARIABLES (tooFarPPillDist ?maxDist))
	(test (>= ?ppill ?maxDist)) 
	=>  
	(assert (ACTION (id PACMANavoidBeingFarFromPPill) (info "Pacman esta lejos de una PPill y se acerca")(priority 1))) )

(defrule GetCloseToPPillRule
	(MSPACMAN (fleeing false) (groupNearbyPPill ?ppill))
	(test (> ?ppill -1)) 
	=>  
	(assert (ACTION (id PACMANgetCloseToPPill) (info "Pacman va hacia una PPill con un grupo cercano")(priority 1))) )

(defrule AvoidPPillRule
	(MSPACMAN (fleeing false) (closestPPillDist ?PPillDist) (closestGhostDist ?ghostDist))
	(VARIABLES (avoidPPillDist ?maxDist))
	(test (or (eq ?ghostDist -1) (and (<= ?PPillDist ?maxDist) (< ?ghostDist ?maxDist))))
	=>  
	(assert (ACTION (id PACMANavoidPPill) (info "Pacman evita comer una PPill")(priority 0))) )

(defrule GoToClosestPPillChaseRule
	(MSPACMAN (fleeing false) (groupNearbyClosestPPill true) (closestPPillDist ?PPillDist))
	(VARIABLES (avoidPPillDist ?MaxDist))
	(test (< ?PPillDist ?MaxDist))
	=>  
	(assert (ACTION (id PACMANgoToClosestPPill) (info "Pacman come una PPill")(priority 1))) )


(defrule GoToClosestPillChaseRule
	(MSPACMAN (fleeing false))
	;Ultimo caso, no hay condiciones adicionales
	=>  
	(assert (ACTION (id PACMANgoToClosestPill) (info "Pacman va a comer una Pill") (priority 0))) )

; FLEE
(defrule GoPathWithMorePillsRule
	(MSPACMAN (alreadyDead true))
	=>  
	(assert (ACTION (id PACMANgoPathWithMorePills) (info "Pacman se va a morir y va a por pills") (priority 2))) )

(defrule GoToClosestPPillFleeRule
	(MSPACMAN (alreadyDead true))
	=>  
	(assert (ACTION (id PACMANgoToClosestPPill) (info "Pacman come una PPill") (priority 2))) )

(defrule GoToClosestPillFleeRule
	(MSPACMAN (alreadyDead ?ded) (ghostEdible ?eat))
	(test(or(eq ?ded true) (eq ?eat false)))
	=>  
	(assert (ACTION (id PACMANgoToClosestPill) (info "Pacman va a comer una Pill") (priority 2))) )

(defrule EatWhileFleeingRule
	(MSPACMAN (ghostEdible true) (canEatGroup false))
	=>  
	(assert (ACTION (id PACMANchaseGhost) (info "Pacman se come a uno de paso") (priority 2))) )

(defrule GoToSafeAreaRule
	=>  
	(assert (ACTION (id PACMANgoToSafeArea) (info "Pacman huye de los fantasmas") (priority 2))) )