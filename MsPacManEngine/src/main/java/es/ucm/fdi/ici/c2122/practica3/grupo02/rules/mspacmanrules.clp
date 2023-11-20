;---------FACTS ASSERTED BY GAME INPUT-------------------------------
(deftemplate BLINKY
	(slot edible (type SYMBOL)) 
	)
	
(deftemplate INKY
	(slot edible (type SYMBOL)) 
	)
	
(deftemplate PINKY
	(slot edible (type SYMBOL)) 
	)

(deftemplate SUE
	(slot edible (type SYMBOL)) 
	)
	
(deftemplate MSPACMAN
	(slot caminoBloqueado (type SYMBOL))
	(slot puedeComer (type SYMBOL))
	(slot tiempoComerFantasma (type NUMBER))
	(slot pPillMasCercana (type SYMBOL))
	)
	
(deftemplate CONSTANTS
	(slot tiempoMinimoComerFantasma (type NUMBER)) )
    
;--------DEFINITION OF THE ACTION FACT---------------------------------
(deftemplate ACTION
	(slot id) 
	(slot info (default "")) 
	(slot priority (type NUMBER) )) 
   
;----------------RULES-------------------------------------------------	
(defrule EatGhosts
	(MSPACMAN (puedeComer true)
			  (tiempoComerFantasma ?t)
			  )
	(CONSTANTS (tiempoMinimoComerFantasma ?m))
	(test (> ?t ?m))
	=>  
	(assert (ACTION (id ChaseGhost) 
					(info "MSPacMan cazador de fantasmas") 
					(priority 3) )) 
					)
					
(defrule EatPills
	(MSPACMAN (caminoBloqueado false))
	=>  
	(assert (ACTION (id ReachClosestPill) 
					(info "MSPacMan farmeando") 
					(priority 1) )) 
					)	

(defrule EatPowerPill
	(MSPACMAN (caminoBloqueado true) (pPillMasCercana true))
	=>  
	(assert (ACTION (id ReachClosestPowerPill) 
					(info "MsPacMan va a por PPill") 
					(priority 6) )) 
					)

(defrule Runaway
	(MSPACMAN (caminoBloqueado true))
	=>  
	(assert (ACTION (id RunawayFromClosestGhost) 
					(info "MSPacMan bloqueado --> Huir") 
					(priority 6) )) 
					)	
	
	