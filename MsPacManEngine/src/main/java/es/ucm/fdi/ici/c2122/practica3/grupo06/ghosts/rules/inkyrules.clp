;FACTS ASSERTED BY GAME INPUT

(deftemplate BLINKY
    (slot distanceToGhost (type NUMBER))
    (slot distanceToPacMan (type NUMBER))
    (slot distanceToPacManIntersection (type NUMBER))
    (slot lairTime (type NUMBER))
    (slot edible (type SYMBOL))
    (slot quadrant (type NUMBER))
    (slot canKill (type SYMBOL))
)

(deftemplate INKY
    (slot distanceToGhost (type NUMBER))
    (slot distanceToPacMan (type NUMBER))
    (slot distanceToPacManIntersection (type NUMBER))
    (slot lairTime (type NUMBER))
    (slot edible (type SYMBOL))
    (slot quadrant (type NUMBER))
    (slot canKill (type SYMBOL))
)

(deftemplate PINKY
    (slot distanceToGhost (type NUMBER))
    (slot distanceToPacMan (type NUMBER))
    (slot distanceToPacManIntersection (type NUMBER))
    (slot lairTime (type NUMBER))
    (slot edible (type SYMBOL))
    (slot quadrant (type NUMBER))
    (slot canKill (type SYMBOL))
)

(deftemplate SUE
    (slot distanceToGhost (type NUMBER))
    (slot distanceToPacMan (type NUMBER))
    (slot distanceToPacManIntersection (type NUMBER))
    (slot lairTime (type NUMBER))
    (slot edible (type SYMBOL))
    (slot quadrant (type NUMBER))
    (slot destinyQuadrant (type NUMBER))
    (slot canKill (type SYMBOL))
)

(deftemplate MSPACMAN 
    (slot distanceToPPill (type NUMBER)) 
)

(deftemplate GAMEINFO 
    (slot numPPills (type NUMBER)) 
)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (default 0))
) 
   
;RULES 
(defrule INKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(INKY (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "MSPacMan cerca PPill e Inky huye"))) 
)

(defrule INKYdisperseMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(INKY (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id INKYdisperses) (info "MSPacMan cerca PPill e Inky se dispersa"))) 
)

(defrule INKYrunsAwayBLINKYedible
	(INKY (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id INKYrunsAway) (info "Inky comestible huye"))) 
)
	
(defrule INKYdisperseyBLINKYedible
	(INKY (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id INKYdisperses) (info "Inky comestible se dispersa"))) 
)	

(defrule INKYcanKill
	(INKY (canKill true)) 
	=>  
	(assert (ACTION (id INKYkills) (info "Inky va a matar a MsPacman"))) 
)	

(defrule INKYcutoff
	=>
	(assert (ACTION (id INKYcutoff) (info "Inky va a la interseccion mas cercana"))) 
)	