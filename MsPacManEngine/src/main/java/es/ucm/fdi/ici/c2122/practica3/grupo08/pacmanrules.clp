;+---------------------------------------------------------------------------------+
;|                                FACTS                                            | 
;+---------------------------------------------------------------------------------+ 
(deftemplate PACMAN
	(slot isAnyGhostToEat (type SYMBOL))
    (slot isAnyGhostToEatWhileFlee (type SYMBOL))
	(slot canReachPPillBeforeghosts (type SYMBOL))
	(slot isAnyGroupOfOfEdibleGhost (type SYMBOL))
	(slot isAnyNearChasingGhost (type SYMBOL))
	(slot isAnyPossibleMove (type SYMBOL))
)

  
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))
) 
;+---------------------------------------------------------------------------------+
;|                                 REGLAS                                          | 
;+---------------------------------------------------------------------------------+   
;RULES

;------------------------------------HUIR------------------------------------------

(defrule PACMANFleeToEdibleGhost
	(PACMAN (isAnyNearChasingGhost true))
	(PACMAN (isAnyGhostToEatWhileFlee true))
	=>  
	(assert (ACTION (id PACMANFleeToEdibleGhost) (info "MsPacman huye hacia fantasma comestible") (priority 80))) 
)

(defrule PACMANFleeToPPill
	(PACMAN (isAnyNearChasingGhost true))
	(PACMAN (canReachPPillBeforeghosts true))
	=>  
	(assert (ACTION (id PACMANFleeToPPill) (info "MsPacman huye hacia power pill") (priority 70)))
)
	
(defrule PACMANFleeToPill
	(PACMAN (isAnyNearChasingGhost true))
	(PACMAN (isAnyPossibleMove true))
	=> 
	(assert (ACTION (id PACMANFleeToPill) (info "MsPacman huye hacia pill") (priority 60)))
)	

(defrule PACMANFleeToFurthestGhost
	(PACMAN (isAnyNearChasingGhost true))
	(PACMAN (isAnyPossibleMove false))
	=> 
	(assert (ACTION (id PACMANFleeToFurthestGhost) (info "MsPacman escapa") (priority 50)))
)	

;-----------------------------COMER FANTASMA----------------------------------

(defrule PACMANGoToEdibleGhostGroup
	(PACMAN (isAnyGroupOfOfEdibleGhost true))
	=> 
	(assert (ACTION (id PACMANGoToEdibleGhostGroup) (info "MsPacman va hacia grupo de fantasmas comestibles") (priority 40) ))
)	
		
(defrule PACMANGoToEdibleGhost
	(PACMAN (isAnyGhostToEat true))
	=> 
	(assert (ACTION (id PACMANGoToEdibleGhost) (info "MsPacman va hacia fantasma comestible") (priority 30)))
)

;-------------------------------COMER PILLS-----------------------------------

(defrule PACMANGoToPillAvoidingPowerPill
	(PACMAN (isAnyNearChasingGhost false))
	=> 
	(assert (ACTION (id PACMANGoToPillAvoidingPowerPill) (info "MsPacman come pill") (priority 20)))
)
	