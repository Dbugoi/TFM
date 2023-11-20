;+---------------------------------------------------------------------------------+
;|                                FACTS                                            | 
;+---------------------------------------------------------------------------------+ 
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
(defrule PINKYrunsAwayToNearGhost
	(or (and(COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30)))
        (PINKY (edible true)))
        (PINKY (nearbyGhost true))
	=>  
	(assert (ACTION (id PINKYrunsAwayToGhost) (info "Pinky huye de MsPacman hacia fantasma") (priority 50))) 
)

(defrule PINKYrunsAwaySpread
	(or (and(COMMON (minPacmanDistancePPill ?d)) (test (<= ?d 30))) 
        (PINKY (edible true)))
	=>  
	(assert (ACTION (id PINKYrunsAwaySpread) (info "Pinky huye dispersandose") (priority 40)))
)
	
(defrule PINKYJaqueMate
	(COMMON (isCheckMate true)) 
	=> 
	(assert (ACTION (id PINKYJaqueMate) (info "Pinky Jaque Mate") (priority 30) ))
)	

(defrule PINKYCerrarCamino
	(COMMON (anyGhostEdible false))
	=>
	(assert (ACTION (id PINKYCerrarCamino) (info "Pinky va a cerrar camino") (priority 10)))
)	
	
	