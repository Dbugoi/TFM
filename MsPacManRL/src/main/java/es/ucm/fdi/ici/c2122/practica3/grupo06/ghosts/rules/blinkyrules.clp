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
(defrule BLINKYrunsAwayMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(BLINKY (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill y Blinky huye"))) 
)

(defrule BLINKYdisperseMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(BLINKY (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id BLINKYdisperses) (info "MSPacMan cerca PPill y Blinky se dispersa"))) 
)

(defrule BLINKYrunsAwayBLINKYedible
	(BLINKY (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id BLINKYrunsAway) (info "Blinky comestible y huye"))) 
)
	
(defrule BLINKYdisperseyBLINKYedible
	(BLINKY (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id BLINKYdisperses) (info "Blinky comestible y se dispersa"))) 
)	

(defrule BLINKYcanKill
	(BLINKY (canKill true)) 
	=>  
	(assert (ACTION (id BLINKYkills) (info "Blinky va a matar a  MsPacman"))) 
)	

(defrule BLINKYchases
  	=>
	(assert (ACTION (id BLINKYchases) (info "Blinky persigue"))) 
)	