;+---------------------------------------------------------------------------------+
;|                                FACTS                                            | 
;+---------------------------------------------------------------------------------+ 
(deftemplate SUE
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL))
    (slot minSueDistanceMsPacman (type NUMBER))
)

(deftemplate PINKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
    (slot minPinkyDistanceToNextJunction (type NUMBER))
	(slot minPacmanDistanceToNextJunction (type NUMBER))
)

(deftemplate BLINKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
)

(deftemplate INKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
    (slot minInkyDistancePPill (type NUMBER))
)

(deftemplate COMMON
	(slot anyGhostEdible (type SYMBOL))
    (slot minPacmanDistancePPill (type NUMBER))
	(slot isCheckMate (type SYMBOL))
)

  
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER)) 
) 

;+---------------------------------------------------------------------------------+
;|                                 REGLAS                                          | 
;+---------------------------------------------------------------------------------+   
;RULES 
(defrule SUErunsAwayToNearGhost
	(or (and (COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (SUE (edible true))
        (SUE (nearbyGhost true)))
	=>  
	(assert (ACTION (id SUErunsAwayToGhost) (info "Sue huye de MsPacman hacia fantasma") (priority 50))) 
)

(defrule SUErunsAwaySpread
	(or (and (COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (SUE (edible true)))
	=>  
	(assert (ACTION (id SUErunsAwaySpread) (info "Sue huye dispersandose") (priority 40)))
)
	
(defrule SUEJaqueMate
	(COMMON (isCheckMate true)) 
	=> 
	(assert (ACTION (id SUEJaqueMate) (info "Sue Jaque Mate") (priority 30)))
)		

(defrule SUEPerseguir
	(SUE (minSueDistanceMsPacman ?d)) (test (> ?d 60))
	=> 
	(assert (ACTION (id SUEPerseguir) (info "Sue va hacia MsPacman") (priority 10)))
)

(defrule SUERandom
	(SUE (minSueDistanceMsPacman ?d)) (test (<= ?d 60))
	=> 
	(assert (ACTION (id SUERandom) (info "Sue se mueve aleatoriamente") (priority 5)))
)
	
	