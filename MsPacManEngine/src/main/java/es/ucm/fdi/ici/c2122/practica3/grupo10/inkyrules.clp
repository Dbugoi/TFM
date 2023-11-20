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
 
(defrule INKYrunsAway
	(INKY (edible true)) =>  (assert (ACTION (id INKYrunsAway) (priority 60))))

(defrule INKYclosePPillToPacMan
	(INKY (edible false))
	(MSPACMAN (mindistancePPill ?d))
	(INKY (nearPPD ?n))
	(test (<= ?d 30))
	(test (< ?n ?d))
	=>  
	(assert (ACTION (id GHOSTcortarPPill) (info "Inky corta PPill") (priority 60))) )

(defrule INKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d))
	(test (<= ?d 30)) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "MSPacMan cerca PPill") (priority 60))) )
	
	
(defrule INKYrunsAwayFromCloseGhost
	(INKY (nearGhostDistance ?d))
	(test (<= ?d 15))
	=>  
	(assert (ACTION (id GHOSTrunsAwayFromMsPacManGhosts) (info "Inky cerca de otro fantasma") (priority 60))) )
	
(defrule INKYchases
	(INKY (edible false)) 
	(INKY (dist_to_pacman ?dt))
	(INKY (dist_from_pacman ?df))
	(test (<= ?dt 80))
	(test (eq ?df ?dt))
	=> 
	(assert (ACTION (id INKYchases) (info "INKY --> perseguir") (priority 60) )))	

(defrule GHOSTpreAttackInterseccion_4
	(INKY (edible false))
	(INKY (inIntersection true)) 
	(INKY (dist_to_pacman ?dt))
	(test ( >= ?dt 200))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_4) (info "INKY preataca fase 4") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_3
	(INKY (edible false)) 
	(INKY (inIntersection true)) 
	(INKY (dist_to_pacman ?dt))
	(test ( >= ?dt 150))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_3) (info "INKY preataca fase 3") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_2
	(INKY (edible false)) 
	(INKY (inIntersection true)) 
	(INKY (dist_to_pacman ?dt))
	(test ( >= ?dt 100))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_2) (info "INKY preataca fase 2") (priority 60))) )
	
	
	
(defrule GHOSTpreAttackInterseccion_1
	(INKY (edible false)) 
	(INKY (inIntersection true)) 
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_1) (info "INKY preataca fase 1") (priority 60))) )
	
	
	