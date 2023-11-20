;FACTS ASSERTED BY GAME INPUT
(deftemplate GHOSTS
	(slot edibleSafe (type SYMBOL)))

(deftemplate POWERPILLS
	(slot edibleSafe (type SYMBOL)))
	
(deftemplate PILLS
	(slot edibleSafe (type SYMBOL)))
    
(deftemplate MSPACMAN
	(slot numberPossibleMoves (type NUMBER)) (slot moves (type SYMBOL)) (slot isInJunction (type SYMBOL)))
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot moves (type SYMBOL)) (slot priority (type NUMBER) )) 
   
;RULES 

(defrule MSPACMANcorridor
	(MSPACMAN (isInJunction false))
	=>
	(assert (ACTION (id MSPACMANcorridor) (priority 100) )))
	
(defrule MSPACMANnoMoves
	(MSPACMAN (numberPossibleMoves ?d)) (test (= ?d 0)) 
	=>  
	(assert (ACTION (id MSPACMANnoMoves) (priority 90) )))

(defrule MSPACMANchases
	(GHOSTS (edibleSafe true)) 
	(MSPACMAN (moves ?m))
	=>  
	(assert (ACTION (id MSPACMANchases) (moves ?m) (priority 80) ))) 
	
(defrule MSPACMANpowerPill
	(POWERPILLS (edibleSafe true)) 
	(MSPACMAN (moves ?m))
	=> 
	(assert (ACTION (id MSPACMANpowerPill) (moves ?m) (priority 70) )))	

(defrule MSPACMANPill
	(PILLS (edibleSafe true))
	(MSPACMAN (moves ?m))
	=>
	(assert (ACTION (id MSPACMANpill) (moves ?m) (priority 60) )))

(defrule MSPACMANrandomMove
	(MSPACMAN (numberPossibleMoves ?d)) (test (>= ?d 0))
	(MSPACMAN (moves ?m))
	=>
	(assert (ACTION (id MSPACMANrandomMove) (moves ?m) (priority 50))))

	