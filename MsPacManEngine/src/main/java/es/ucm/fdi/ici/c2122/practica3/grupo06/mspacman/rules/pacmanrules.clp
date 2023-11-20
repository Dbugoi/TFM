;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN 
    (slot numberOfNormalGhostsInRange (type NUMBER)) 
    (slot isPowerPillEaten (type SYMBOL))
    (slot numberOfPillsInRange (type NUMBER)) 
    (slot numberOfGhostsInDirectionPPill (type NUMBER)) 
 	(slot numberOfGhostsInDirectionEdibleGhost (type NUMBER)) 
 	(slot numberOfGhostsInDirectionPill (type NUMBER))
)
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (default 0))
) 
   
;RULES 
;SI PowerPillEaten = false, no hay fantasmas y hay pills en region ENTONCES comer pill en region
(defrule EatPillsInRegion
	(MSPACMAN (isPowerPillEaten false) (numberOfNormalGhostsInRange ?n) (numberOfPillsInRange ?p)) 
	(test (and (= ?n 0) (> ?p 0)))
	=>  
	(assert (ACTION (id eatPillsRegion) (info "MSPacMan come pills en región"))) 
)

;SI PowerPillEaten = false, no hay fantasmas y no hay pills en region ENTONCES comer pill en  otra region
(defrule EatPillsInOtherRegion
	(MSPACMAN (isPowerPillEaten false) (numberOfNormalGhostsInRange ?n) (numberOfPillsInRange ?p))
	(test (and (= ?n 0) (= ?p 0)))
	=>  
	(assert (ACTION (id eatPillsOtherRegion) (info "MsPacMan come pills en otra región") ))
)
	
;SI PowerPillEaten = true y no hay fantasmas normales en rango y no hay fantasmas en la dirección del fantasma comestible ENTONCES ir a fantasma comestible
(defrule EatGhost
	(MSPACMAN (isPowerPillEaten true)  (numberOfNormalGhostsInRange ?n) )
	(test (= ?n 0))
	=>  
	(assert (ACTION (id eatGhost) (info "MsPacMan come fantasma comestible") ))
)
	
	
;SI PowerPillEaten = true y hay fantasmas normales en rango y hay fantasmas en la dirección del fantasma comestible ENTONCES huir del fantasma hacia fantasma comestible
(defrule RunAwayToEdibleGhost
	(MSPACMAN (isPowerPillEaten true)  (numberOfNormalGhostsInRange ?n) (numberOfGhostsInDirectionEdibleGhost ?p))
	(test  (and(> ?n 0)(> ?p 0)))
	=>  
	(assert (ACTION (id runAwayToEdibleGhost) (info "MsPacMan huye del fantasma hacia el fantasma comestible") ))
)

;SI PowerPillEaten = true y hay fantasmas normales en rango y hay fantasmas en la dirección del fantasma comestible ENTONCES huir del fantasma hacia fantasma comestible
(defrule EatCautious
	(MSPACMAN (isPowerPillEaten true)  (numberOfNormalGhostsInRange ?n) (numberOfGhostsInDirectionEdibleGhost ?p))
	(test  (and(> ?n 0)(= ?p 0)))
	=>  
	(assert (ACTION (id eatGhost) (info "MsPacMan come fantasma comestible") ))
)	
	
;SI PowerPillEaten = false y hay 1 fantasma ENTONCES huye del fantasma hacia la pill más cercana
(defrule RunAwayToPill
	(MSPACMAN (isPowerPillEaten false) (numberOfNormalGhostsInRange ?n))
	(test (= ?n 1)) 
	=>  
	(assert (ACTION (id runAwayToPill) (info "MsPacMan huye del fantasma hacia la pill más cercana") ))
)
	
;SI PowerPillEaten = false, hay más de 1 fantasma y no hay fantasmas en la dirección de la power pill ENTONCES va a la power pill
(defrule EatPPill
	(MSPACMAN (isPowerPillEaten false) (numberOfNormalGhostsInRange ?n)(numberOfGhostsInDirectionPPill ?p))
	(test (and (> ?n 1)(= ?p 0))) 
	=>  
	(assert (ACTION (id eatPPill) (info "MsPacMan huye del fantasma va a PPill") ))
)
		
;SI PowerPillEaten = false, hay más de 1 fantasma y hay fantasmas en la dirección de la power pill ENTONCES huye hacia la power pill
(defrule RunAwaytoPPill
	(MSPACMAN (isPowerPillEaten false) (numberOfNormalGhostsInRange ?n)(numberOfGhostsInDirectionPPill ?p))
	(test (and (> ?n 1)(> ?p 0))) 
	=>  
	(assert (ACTION (id runAwayToPoPill) (info "MsPacMan huye del fantasma hacia PPill") ))
)
		
	
