;+---------------------------------------------------------------------------------+
;|                                FACTS                                            | 
;+---------------------------------------------------------------------------------+ 
(deftemplate BLINKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
)

(deftemplate PINKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
    (slot minPinkyDistanceToNextJunction (type NUMBER))
	(slot minPacmanDistanceToNextJunction (type NUMBER))
)

(deftemplate INKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
    (slot minInkyDistancePPill (type NUMBER))
)

(deftemplate SUE
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL))
    (slot minSueDistanceMsPacman (type NUMBER))
)

(deftemplate COMMON
	(slot anyGhostEdible (type SYMBOL))
    (slot minPacmanDistancePPill (type NUMBER))
	(slot isCheckMate (type SYMBOL)) 
)

  
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) 
) 

;+---------------------------------------------------------------------------------+
;|                                 REGLAS                                          | 
;+---------------------------------------------------------------------------------+   
;RULES 
(defrule BLINKYrunsAwayToNearGhost
		(or (and (COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (BLINKY (edible true)))
        (BLINKY (nearbyGhost true))
	=>  
	(assert (ACTION (id BLINKYrunsAwayToGhost) (info "Blinky huye de MsPacman hacia fantasma") (priority 50))) 
)

(defrule BLINKYrunsAwaySpread
	(or (and (COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (BLINKY (edible true)))
	=>  
	(assert (ACTION (id BLINKYrunsAwaySpread) (info "Blinky huye dispersandose") (priority 40)))
)
	
(defrule BLINKYJaqueMate
	(COMMON (isCheckMate true)) 
	=> 
	(assert (ACTION (id BLINKYJaqueMate) (info "Blinky Jaque Mate") (priority 30)))
)	
		
(defrule BLINKYPerseguir 
	(COMMON (anyGhostEdible false)) 
	=> 
	(assert (ACTION (id BLINKYPerseguir) (info "Blinky va a persguir") (priority 10)))
)	
	
	