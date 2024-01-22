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
(defrule SUErunsAwayMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(SUE (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id SUErunsAway) (info "MSPacMan cerca PPill y Sue huye"))) 
)

(defrule SUEdisperseMSPACMANclosePPill
	(MSPACMAN (distanceToPPill ?d)) 
    (test (<= ?d 30)) 
	(SUE (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id SUEdisperses) (info "MSPacMan cerca PPill y Sue se dispersa"))) 
)

(defrule SUErunsAwaySUEedible
	(SUE (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (<= ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id SUErunsAway) (info "Sue comestible y huye"))) 
)
	
(defrule SUEdisperseySUEedible
	(SUE (edible true) (distanceToPacMan ?p) (distanceToGhost ?g)) 
    (test (> ?p (* ?g 2))) 
	=>  
	(assert (ACTION (id SUEdisperses) (info "Sue comestible y se dispersa"))) 
)	

(defrule SUEcanKill
	(SUE (canKill true)) 
	=>  
	(assert (ACTION (id SUEkills) (info "Sue va a matar a  MsPacman"))) 
)	

(defrule SUEguards
    (SUE (quadrant ?qe) (destinyQuadrant ?dq))
    (test (= ?qe ?dq))
	=>  
	(assert (ACTION (id SUEguardsQuad) (info "Sue patrulla el cuadrante destino"))) 
)	

(defrule SUEgoesToDestinyQuad
	=>  
	(assert (ACTION (id SUEtoDestinyQuad) (info "Sue va al cuadrante destino"))) 
)	