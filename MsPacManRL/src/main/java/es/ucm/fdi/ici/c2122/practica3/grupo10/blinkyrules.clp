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
	(slot id) (slot info (default "")) (slot priority (type NUMBER)) ) 
   
;---------------------------- RULES ---------------------------------



(defrule BLINKYrunsAway
	(BLINKY (edible true))
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "BLINKY comestible --> huir")  (priority 60))))
	

(defrule BLINKYclosePPillToPacMan
	(BLINKY (edible false))
	(MSPACMAN (mindistancePPill ?d))
	(BLINKY (nearPPD ?n))
	(test (<= ?d 30))
	(test (< ?n ?d))
	=>  
	(assert (ACTION (id GHOSTcortarPPill) (info "BLinky corta PPill") (priority 60))) )

(defrule BLINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d))
	(test (<= ?d 30)) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill") (priority 60))) )

	
(defrule BLINKYrunsAwayFromCloseGhost
	(BLINKY (nearGhostDistance ?d))
	(test (<= ?d 15)) 
	=>  
	(assert (ACTION (id GHOSTrunsAwayFromMsPacManGhosts) (info "Blinky cerca de otro fantasma") (priority 60))) )


(defrule BLINKYchases
	(BLINKY (edible false)) 
	(BLINKY (dist_to_pacman ?dt))
	(BLINKY (dist_from_pacman ?df))
	(test (<= ?dt 80))
	(test (eq ?df ?dt))
	=> 
	(assert (ACTION (id BLINKYchases) (info "BLINKI --> perseguir") (priority 60) )))

	
(defrule GHOSTpreAttackInterseccion_4
	(BLINKY (edible false))
	(BLINKY (inIntersection true))  
	(BLINKY (dist_to_pacman ?dt))
	(test ( >= ?dt 200))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_4) (info "BLINKY preataca fase 4") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_3
	(BLINKY (edible false)) 
	(BLINKY (inIntersection true)) 
	(BLINKY (dist_to_pacman ?dt))
	(test ( >= ?dt 150))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_3) (info "BLINKY preataca fase 3") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_2
	(BLINKY (edible false)) 
	(BLINKY (inIntersection true)) 
	(BLINKY (dist_to_pacman ?dt))
	(test ( >= ?dt 100))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_2) (info "BLINKY preataca fase 2") (priority 60))) )
	
	
(defrule GHOSTpreAttackInterseccion_1
	(BLINKY (edible false)) 
	(BLINKY (inIntersection true)) 
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_1) (info "BLINKY preataca fase 1") (priority 60))) )
	
	