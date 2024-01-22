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
(defrule PINKYrunsAway
	(PINKY (edible true))
	=>  
	(assert (ACTION (id PINKYRunsAway)
			(info "Comestible --> huir")
			(priority 4)
			(runAwayType 1))) )

(defrule PINKYrunsAwayMSPACMANclosePPill
	(PINKY (edible false)) 
	(MSPACMAN (pacmanDistancePowerPill ?p))
	(CONSTANTS (mindistancePPill ?m))
	(test (<= ?p ?m)) 
	=>  
	(assert (ACTION (id PINKYRunsAway)
			(info "MsPacman cerca de PowerPill --> huir")
			(priority 5)
			(runAwayType 1))) )
	
(defrule PINKYrunsAwayToBLINKY
	(PINKY (edible true))
	(BLINKY (outOfLair true))
	(BLINKY (edible false))
	=>  
	(assert (ACTION (id PINKYRunsAway)
			(info "Comestible & BLINKY No Comestible --> huir a SUE")
			(priority 7)
			(runAwayType 2))) )

(defrule PINKYrunsAwayToINKY
	(PINKY (edible true))
	(INKY (outOfLair true))
	(INKY (edible false))
	=>  
	(assert (ACTION (id PINKYRunsAway)
			(info "Comestible & INKY No Comestible --> huir a INKY")
			(priority 7)
			(runAwayType 2))) )

(defrule PINKYrunsAwayToSUE
	(PINKY (edible true))
	(SUE (outOfLair true))
	(SUE (edible false))
	=>  
	(assert (ACTION (id PINKYRunsAway)
			(info "Comestible & SUE No Comestible --> huir a SUE")
			(priority 7)
			(runAwayType 2))) )
			
(defrule PINKYrunAlternative
	(PINKY (edible true))
	(PINKY (anotherGhostInPath true))
	=>  
	(assert (ACTION (id PINKYRunsAway)
			(info "Comestible & Ghost en camino de huida --> huir por otro camino")
			(priority 6)
			(runAwayType 3))) )

(defrule PINKYchases
	(PINKY (edible false))
	(PINKY (outOfLair true))
	(PINKY (distanceToPacman ?d))
	(CONSTANTS (ghostChaseDistance ?g))
	(test (<= ?d ?g))
	=> 
	(assert (ACTION (id PINKYChase)
			(info "No comestible --> perseguir")
			(priority 2)
			(chaseType 1))) )

(defrule PINKYchasesAlternative
	(PINKY (edible false))
	(PINKY (outOfLair true))
	(PINKY (distanceToPacman ?d))
	(PINKY (chaseCount ?c))
	(CONSTANTS (ghostChaseDistance ?g))
	(CONSTANTS (minIntersectionsBeforeChange ?m))
	(test (<= ?d ?g))
	(test (> ?c ?m))
	=> 
	(assert (ACTION (id PINKYChase)
			(info "No comestible --> perseguir distinto camino")
			(priority 3)
			(chaseType 2))) )	
	
(defrule Mole
	(PINKY (edible false))
	(PINKY (outOfLair true))
	(PINKY (distanceToPacman ?d))
	(CONSTANTS (minPredictionDistance ?g))
	(MSPACMAN (pacmanDistancePowerPill ?p))
	(test (> ?d ?g))
	(test (> ?p 20))
	=> 
	(assert (ACTION (id PINKYMole)
			(info "Mole predicts road with more pills")
			(priority 1))) )