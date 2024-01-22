;FACTS ASSERTED BY GAME INPUT

(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot distance (type NUMBER))
	(slot first (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third (type SYMBOL))
	(slot last (type SYMBOL))
	(slot jail (type SYMBOL))
)

(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot distance (type NUMBER))
	(slot first (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third (type SYMBOL))
	(slot last (type SYMBOL))
	(slot jail (type SYMBOL))
)

(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot distance (type NUMBER))
	(slot first (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third (type SYMBOL))
	(slot last (type SYMBOL))
	(slot jail (type SYMBOL))
)

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot distance (type NUMBER))
	(slot first (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third (type SYMBOL))
	(slot last (type SYMBOL))
	(slot jail (type SYMBOL))
)
   
(deftemplate MSPACMAN
	
	(slot firstEdible  (type SYMBOL))
	(slot secondEdible (type SYMBOL))
	(slot thirdEdible  (type SYMBOL))
	(slot lastEdible   (type SYMBOL))

	(slot distanceToClosestPPill (type NUMBER))
	(slot distanceToSecondPPill  (type NUMBER))
	(slot distanceToThirdPPill   (type NUMBER))
	(slot distanceToFurthestPPill (type NUMBER))
	
	(slot distanceToClosestGhost (type NUMBER))
	(slot distanceToSecondGhost  (type NUMBER))
	(slot distanceToThirdGhost 	 (type NUMBER))
	(slot distanceToFurthestGhost (type NUMBER))
	
	(slot diferenceClosestsandSecond (type NUMBER))
)
    

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot priority (default 1)) (slot info (default ""))) 
   
;RULES 

;RUN AWAY RULES

(defrule GoToClosestPowerPillWithAllGhostsNearPacman
	(MSPACMAN (distanceToClosestPPill  ?n)) (BLINKY(distance ?b)) (INKY(distance ?i )) (PINKY(distance ?p )) (SUE(distance ?s))
	(test (> ?n ?i)) (test (> ?n ?b)) (test (> ?n ?p)) (test (> ?n ?s)) (test (!= ?n -1)) 
	=>
	(assert (ACTION (id GoToPowerPill) (info "estoy mas cerca que todos los fantasmas")))
)


(defrule GoToSecondPowerPill
	(MSPACMAN (distanceToSecondPPill  ?n)) (BLINKY(distance ?b)) (INKY(distance ?i )) (PINKY(distance ?p )) (SUE(distance ?s))
	(test (> ?n ?i)) (test (> ?n ?b)) (test (> ?n ?p)) (test (> ?n ?s)) (test (!= ?n -1))
	=>
	(assert (ACTION (id GoToPowerPill) (info "estoy mas cerca que todos los fantasmas") ))
)

(defrule FleeFromBLINKY
	(BLINKY (first true) (edible false) (distance ?d)) (test( > ?d 135))
	=>
	(assert (ACTION (id FleeFromClosestGhost) (info "BLINKY esta muy cerca" )))
)

(defrule FleeFromPINKY
	
	(PINKY (first true) (edible false) (distance ?d)) (test( > ?d 135))
	=>
	(assert (ACTION (id FleeFromClosestGhost) (info "PINKY esta muy cerca") ))
)
(defrule FleeFromINKY
	
	(INKY (first true) (edible false) (distance ?d)) (test( > ?d 135))
	=>
	(assert (ACTION (id FleeFromClosestGhost) (info "INKY esta muy cerca") ))	
)

(defrule FleeFromSUE
	(SUE (first true) (edible false) (distance ?d)) (test( > ?d 135))
	=>
	(assert (ACTION (id FleeFromClosestGhost) (info "SUE esta muy cerca") ))
)

(defrule ChaseClosest
	(MSPACMAN (firstEdible true) (secondEdible true) (thirdEdible true) (lastEdible true)) 
	=>
	(assert (ACTION (id ChaseClosestGhost) (info "Todos son comestibles") ))	
)
(defrule ChaseClosestAlt
	(MSPACMAN (firstEdible true) (secondEdible true) (distanceToThirdGhost ?d)) (test(> ?d 40)) 
	=>
	(assert (ACTION (id ChaseClosestGhost) (info "Todos son comestibles") ))
)

(defrule ChaseSecond
	(MSPACMAN (firstEdible false) (secondEdible true) (diferenceClosestsandSecond ?d)) (test (> ?d 40)) 
	=>
	(assert (ACTION (id ChaseSecond) (info "comer al segundo es seguro")))

)

(defrule EatPillsWithGhostsInJail
	(SUE(jail true)) (INKY (jail true)) (PINKY (jail true)) (BLINKY (jail true)) 
	=>
	(assert (ACTION (id EatClosestPills) (info "eating pills is safe")))
)

(defrule EatPillsWithGhostsNonEdibleFar
	(MSPACMAN (distanceToClosestGhost ?a) (distanceToSecondGhost ?b)) (test (> ?a 60)) (test(> ?b 70))
	=>
	(assert (ACTION (id EatClosestPills) (info "ghost are far")))
)
