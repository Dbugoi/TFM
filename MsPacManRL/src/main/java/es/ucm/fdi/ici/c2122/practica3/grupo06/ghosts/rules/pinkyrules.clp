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
(defrule PINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(PINKY (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "MSPacMan cerca PPill y Pinky huye"))) 
)

(defrule PINKYdisperseMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(PINKY (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id PINKYdisperses) (info "MSPacMan cerca PPill y Pinky se dispersa"))) 
)

(defrule PINKYrunsAwayPINKYedible
	(PINKY (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id PINKYrunsAway) (info "Pinky comestible y huye"))) 
)
	
(defrule PINKYdisperseyPINKYedible
	(PINKY (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id PINKYdisperses) (info "Pinky comestible se dispersa"))) 
)	

(defrule PINKYcanKill
	(PINKY (canKill true)) 
	=>  
	(assert (ACTION (id PINKYkills) (info "Pinky va a matar a MsPacman"))) 
)

(defrule PINKYinytercepts
	(PINKY (edible false)) 
	=>  
	(assert (ACTION (id PINKYintercepts) (info "Pinky intercepta Power Pill"))) 
)	