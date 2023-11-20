; -- FACTS --

; --- GAME INPUT DATA ---	
(deftemplate BLINKY
	(slot edible (type SYMBOL))
    (slot edTimeLeft (type NUMBER))
    (slot outOfLair (type SYMBOL))
	(slot position (type NUMBER))
	(slot distanceToPacman (type NUMBER))
	(slot anotherGhostInPath (type SYMBOL))
	(slot chaseCount (type NUMBER)) )

(deftemplate INKY
	(slot edible (type SYMBOL))
    (slot edTimeLeft (type NUMBER))
    (slot outOfLair (type SYMBOL))
	(slot position (type NUMBER))
	(slot distanceToPacman (type NUMBER))
	(slot anotherGhostInPath (type SYMBOL))
	(slot chaseCount (type NUMBER)) )

(deftemplate PINKY
	(slot edible (type SYMBOL))
    (slot edTimeLeft (type NUMBER))
    (slot outOfLair (type SYMBOL))
	(slot position (type NUMBER))
	(slot distanceToPacman (type NUMBER))
	(slot anotherGhostInPath (type SYMBOL))
	(slot chaseCount (type NUMBER)) )

(deftemplate SUE
	(slot edible (type SYMBOL))
    (slot edTimeLeft (type NUMBER))
    (slot outOfLair (type SYMBOL))
	(slot position (type NUMBER))
	(slot distanceToPacman (type NUMBER))
	(slot anotherGhostInPath (type SYMBOL))
	(slot chaseCount (type NUMBER)) )

(deftemplate MSPACMAN 
    (slot pacmanDistancePowerPill (type NUMBER))
	(slot nextPillPacManBySeer (type NUMBER)) )
	
(deftemplate CONSTANTS
	(slot ghostChaseDistance (type NUMBER))
	(slot mindistancePPill (type NUMBER))
	(slot minPredictionDistance (type NUMBER))
	(slot minIntersectionsBeforeChange (type NUMBER)) )

; --- ACTION ---
(deftemplate ACTION
	(slot id)
	(slot info (default ""))
	(slot priority (type NUMBER))
	(slot runAwayType (type NUMBER))
	(slot chaseType (type NUMBER)) )   
	
; -- RULES --
(defrule BLINKYrunsAway
	(BLINKY (edible true))
	=>  
	(assert (ACTION (id BLINKYRunsAway)
			(info "Comestible --> huir")
			(priority 4)
			(runAwayType 1))) )

(defrule BLINKYrunsAwayMSPACMANclosePPill
	(BLINKY (edible false))
	(MSPACMAN (pacmanDistancePowerPill ?p))
	(CONSTANTS (mindistancePPill ?m))
	(test (<= ?p ?m)) 
	=>  
	(assert (ACTION (id BLINKYRunsAway)
			(info "MsPacman cerca de PowerPill --> huir")
			(priority 5)
			(runAwayType 1))) )
	
(defrule BLINKYrunsAwayToPINKY
	(BLINKY (edible true))
	(PINKY (outOfLair true))
	(PINKY (edible false))
	=>  
	(assert (ACTION (id BLINKYRunsAway)
			(info "Comestible & PINKY No Comestible --> huir a SUE")
			(priority 6)
			(runAwayType 2))) )

(defrule BLINKYrunsAwayToINKY
	(BLINKY (edible true))
	(INKY (outOfLair true))
	(INKY (edible false))
	=>  
	(assert (ACTION (id BLINKYRunsAway)
			(info "Comestible & INKY No Comestible --> huir a INKY")
			(priority 6)
			(runAwayType 2))) )

(defrule BLINKYrunsAwayToSUE
	(BLINKY (edible true))
	(SUE (outOfLair true))
	(SUE (edible false))
	=>  
	(assert (ACTION (id BLINKYRunsAway)
			(info "Comestible & SUE No Comestible --> huir a SUE")
			(priority 6)
			(runAwayType 2))) )
			
(defrule BLINKYrunAlternative
	(BLINKY (edible true))
	(BLINKY (anotherGhostInPath true))
	=>  
	(assert (ACTION (id BLINKYRunsAway)
			(info "Comestible & Ghost en camino de huida --> huir por otro camino")
			(priority 7)
			(runAwayType 3))) )

(defrule BLINKYchases
	(BLINKY (edible false))
	(BLINKY (outOfLair true))
	(BLINKY (distanceToPacman ?d))
	(CONSTANTS (ghostChaseDistance ?g))
	(test (<= ?d ?g))
	=> 
	(assert (ACTION (id BLINKYChase)
			(info "No comestible --> perseguir")
			(priority 2)
			(chaseType 1))) )	

(defrule BLINKYchasesAlternative
	(BLINKY (edible false))
	(BLINKY (outOfLair true))
	(BLINKY (distanceToPacman ?d))
	(BLINKY (chaseCount ?c))
	(CONSTANTS (ghostChaseDistance ?g))
	(CONSTANTS (minIntersectionsBeforeChange ?m))
	(test (<= ?d ?g))
	(test (> ?c ?m))
	=> 
	(assert (ACTION (id BLINKYChase)
			(info "No comestible --> perseguir distinto camino")
			(priority 3)
			(chaseType 2))) )	

(defrule Seer
	(BLINKY (edible false))
	(BLINKY (outOfLair true))
	(BLINKY (distanceToPacman ?d))
	(CONSTANTS (minPredictionDistance ?g))
	(MSPACMAN (pacmanDistancePowerPill ?p))
	(test (> ?d ?g))
	(test (> ?p 20))
	=> 
	(assert (ACTION (id BLINKYSeer)
			(info "Seer predicts pill")
			(priority 1))) )
	
	