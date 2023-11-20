(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot nearGhost (type SYMBOL))
	(slot nearGhostDistance (type NUMBER))
	(slot dist_to_pacman (type NUMBER))
	(slot dist_from_pacman (type NUMBER))
	(slot lairTime (type NUMBER))
	(slot nearPPD (type NUMBER))
	(slot inIntersection(type SYMBOL)))
	
	
(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot nearGhost (type SYMBOL))
	(slot nearGhostDistance (type NUMBER))
	(slot dist_to_pacman (type NUMBER))
	(slot dist_from_pacman (type NUMBER))
	(slot lairTime (type NUMBER))
	(slot nearPPD (type NUMBER))
	(slot inIntersection(type SYMBOL)))

(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot nearGhost (type SYMBOL))
	(slot nearGhostDistance (type NUMBER))
	(slot dist_to_pacman (type NUMBER))
	(slot dist_from_pacman (type NUMBER))
	(slot lairTime (type NUMBER))
	(slot nearPPD (type NUMBER))
	(slot inIntersection(type SYMBOL)))

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot nearGhost (type SYMBOL))
	(slot nearGhostDistance (type NUMBER))
	(slot dist_to_pacman (type NUMBER))
	(slot dist_from_pacman (type NUMBER))
	(slot lairTime (type NUMBER))
	(slot nearPPD (type NUMBER))
	(slot inIntersection(type SYMBOL)))

(deftemplate MSPACMAN 
    (slot mindistancePPill (type NUMBER)) )
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))) 

(defrule PINKYrunsAway
	(PINKY (edible true)) =>  (assert (ACTION (id PINKYrunsAway) (priority 60))))

(defrule PINKYclosePPillToPacMan
	(PINKY (edible false))
	(MSPACMAN (mindistancePPill ?d))
	(PINKY (nearPPD ?n))
	(test (<= ?d 30))
	(test (< ?n ?d))
	=>  
	(assert (ACTION (id GHOSTcortarPPill) (info "Pinky corta PPill") (priority 60))) )
	

(defrule PINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "MSPacMan cerca PPill") (priority 60))) )

	
(defrule PINKYrunsAwayFromCloseGhost
	(PINKY (nearGhostDistance ?d))
	(test (<= ?d 15)) 
	=>  
	(assert (ACTION (id GHOSTrunsAwayFromMsPacManGhosts) (info "Pinky cerca de otro fantasma") (priority 60))) )
	
(defrule PINKYchases
	(PINKY (edible false)) 
	(PINKY (dist_to_pacman ?dt))
	(PINKY (dist_from_pacman ?df))
	(test (<= ?dt 80))
	(test (eq ?df ?dt))
	=> 
	(assert (ACTION (id PINKYchases) (info "PINKY --> perseguir") (priority 60) )))	
	
	
(defrule GHOSTpreAttackInterseccion_4
	(PINKY (edible false)) 
	(PINKY (inIntersection true)) 
	(PINKY (dist_to_pacman ?dt))
	(test ( >= ?dt 200))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_4) (info "PINKY preataca fase 4") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_3
	(PINKY (edible false)) 
	(PINKY (inIntersection true))
	(PINKY (dist_to_pacman ?dt))
	(test ( >= ?dt 150))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_3) (info "PINKY preataca fase 3") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_2
	(PINKY (edible false)) 
	(PINKY (inIntersection true))
	(PINKY (dist_to_pacman ?dt))
	(test ( >= ?dt 100))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_2) (info "PINKY preataca fase 2") (priority 60))) )
	


	
(defrule GHOSTpreAttackInterseccion_1
	(PINKY (edible false)) 
	(PINKY (inIntersection true))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_1) (info "PINKY preataca fase 1") (priority 60))) )
	
	