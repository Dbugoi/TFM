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
;RULES 

(defrule SUErunsAway
	(SUE (edible true)) 
	=>  
	(assert (ACTION (id SUErunsAway) (info "Comestible --> huir")  (priority 60))))
	
(defrule SUEclosePPillToPacMan
	(SUE (edible false))
	(MSPACMAN (mindistancePPill ?d))
	(SUE (nearPPD ?n))
	(test (<= ?d 30))
	(test (< ?n ?d))
	=>  
	(assert (ACTION (id GHOSTcortarPPill) (info "Sue corta PPill") (priority 60))) )
	
(defrule SUErunsAwayMSPACMANclosePPill
	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
	=>  
	(assert (ACTION (id SUErunsAway) (info "MSPacMan cerca PPill") (priority 60))) )
	
(defrule SUErunsAwayFromCloseGhost
	(SUE (nearGhostDistance ?d))
	(test (<= ?d 15)) 
	=>  
	(assert (ACTION (id GHOSTrunsAwayFromMsPacManGhosts) (info "Sue cerca de otro fantasma") (priority 60))) )
	
(defrule SUEchases
	(SUE (edible false)) 
	(SUE (dist_to_pacman ?dt))
	(SUE (dist_from_pacman ?df))
	(test (<= ?dt 80))
	(test (eq ?df ?dt))
	=> 
	(assert (ACTION (id SUEchases) (info "SUE --> perseguir")  (priority 60))))	

(defrule GHOSTpreAttackInterseccion_4
	(SUE (edible false)) 
	(SUE (inIntersection true)) 
	(SUE (dist_to_pacman ?dt))
	(test ( >= ?dt 200))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_4) (info "SUE preataca fase 4") (priority 60))) )

(defrule GHOSTpreAttackInterseccion_3
	(SUE (edible false)) 
	(SUE (inIntersection true)) 
	(SUE (dist_to_pacman ?dt))
	(test ( >= ?dt 150))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_3) (info "SUE preataca fase 3") (priority 60))) )
	
	
(defrule GHOSTpreAttackInterseccion_2
	(SUE (edible false)) 
	(SUE (inIntersection true)) 
	(SUE (dist_to_pacman ?dt))
	(test ( >= ?dt 100))
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_2) (info "SUE preataca fase 2") (priority 60))) )
	
	(defrule GHOSTpreAttackInterseccion_1
	(SUE (edible false)) 
	(SUE (inIntersection true)) 
	=>  
	(assert (ACTION (id GHOSTpreAttackInterseccion_1) (info "SUE preataca fase 1") (priority 60))) )
	
	
