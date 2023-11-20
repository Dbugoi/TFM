;+---------------------------------------------------------------------------------+
;|                                FACTS                                            | 
;+---------------------------------------------------------------------------------+ 
(deftemplate INKY
	(slot edible (type SYMBOL))
    (slot nearbyGhost (type SYMBOL)) 
    (slot minInkyDistancePPill (type NUMBER))
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
(defrule INKYrunsAwayToNearGhost
	(or (and (COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (INKY (edible true))
        (INKY (nearbyGhost true)))
	=>  
	(assert (ACTION (id INKYrunsAwayToGhost) (info "Inky huye de MsPacman hacia fantasma") (priority 50)))
)

(defrule INKYrunsAwaySpread
	(or (and (COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (INKY (edible true)))
	=>  
	(assert (ACTION (id INKYrunsAwaySpread) (info "Inky huye dispersandose") (priority 40)))
)
	
(defrule INKYJaqueMate
	(COMMON (isCheckMate true)) 
	=> 
	(assert (ACTION (id INKYJaqueMate) (info "Inky Jaque Mate") (priority 30)))
)	

(defrule INKYPowerPill
	(COMMON (minPacmanDistancePPill ?d)) (INKY (minInkyDistancePPill ?i)) (test (> ?d ?i)) (test (< ?d 60))
	=> 
	(assert (ACTION (id INKYPowerPill) (info "Inky power pill") (priority 10)))
)	

(defrule INKYBehaviourPINKY
	(or (and (COMMON (minPacmanDistancePPill ?d)) (test (>= ?d 60))		
		) 
        (and (COMMON (minPacmanDistancePPill ?d)) (INKY (minInkyDistancePPill ?i)) (test (<= ?d ?i))        
        )
    )
	=>  
	(assert (ACTION (id INKYBehaviourPINKY) (info "Inky va a cerrar camino") (priority 5)))
)