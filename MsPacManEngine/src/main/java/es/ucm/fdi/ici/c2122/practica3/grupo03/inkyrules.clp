;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot first  (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third  (type SYMBOL))
	(slot last   (type SYMBOL))
	(slot jail (type SYMBOL))
	(slot timeLessThanDistance (type SYMBOL))
	(slot distanceLessThan20 (type SYMBOL))
	(slot distanceLessThan30 (type SYMBOL))
)

(deftemplate INKY
	(slot edible (type SYMBOL))
	(slot first  (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third  (type SYMBOL))
	(slot last   (type SYMBOL))
	(slot jail (type SYMBOL))
	(slot timeLessThanDistance (type SYMBOL))
	(slot distanceLessThan20 (type SYMBOL))
	(slot distanceLessThan30 (type SYMBOL))
)

(deftemplate PINKY
	(slot edible (type SYMBOL))
	(slot first  (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third  (type SYMBOL))
	(slot last   (type SYMBOL))
	(slot jail (type SYMBOL))
	(slot timeLessThanDistance (type SYMBOL))
	(slot distanceLessThan20 (type SYMBOL))
	(slot distanceLessThan30 (type SYMBOL))
)

(deftemplate SUE
	(slot edible (type SYMBOL))
	(slot first  (type SYMBOL))
	(slot second (type SYMBOL))
	(slot third  (type SYMBOL))
	(slot last   (type SYMBOL))
	(slot jail (type SYMBOL))
	(slot timeLessThanDistance (type SYMBOL))
	(slot distanceLessThan20 (type SYMBOL))
	(slot distanceLessThan30 (type SYMBOL))
)
   
(deftemplate MSPACMAN
	(slot mindistanceGhostless20       (type SYMBOL))
	(slot mindistancePPillless30       (type SYMBOL))
) 

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot priority (default 1)) (slot info (default "")) ) 
   
;RULES 

;RUN AWAY RULES

;Reglas si no soy el mas cercano
(defrule INKYrunsAwayFromPacman1
	(INKY (edible false) (first false)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id INKYrunsAwayFromPacman) (info "MSPacMan cerca PPill"))) )
	
(defrule INKYrunsAwayFromPacman2
	(INKY (edible true)) (INKY (first false))
	=>  
	(assert (ACTION (id INKYrunsAwayFromPacman) (info "No soy el mas cercano --> huir de pacman") )))
	
	
;Reglas si soy el mas cercano
(defrule INKYrunsAwayFromGhost1
	(INKY (edible true) (first true)) (MSPACMAN (mindistanceGhostless20 true))
	=>  
	(assert (ACTION (id INKYrunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
(defrule INKYrunsAwayFromGhost2
	(INKY (edible false) (first true) (distanceLessThan20 true)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id INKYrunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
;Regalse si a pacman no le da tiempo a comerme

(defrule INKYstaysNearbyPacman
	(INKY (edible true) (timeLessThanDistance true)) 
	=>  
	(assert (ACTION (id INKYstaysNearbyPacman) (info "Pacman no le da tiempo a comerme --> Me mantengo en los alrededores") )))
	
	
;CHASE RULES

(defrule INKYchases
	(INKY (edible false) (first true) (distanceLessThan20 true))
	=> 
	(assert (ACTION (id INKYchases) (info "No comestible, mas cercano y pacman a menos de 20 de distancia --> perseguir") )))

	
(defrule INKYgoestoPacManin201
	(INKY (edible false) (first true)) 
	=> 
	(assert (ACTION (id INKYgoestoPacManin20) (info "No comestible y mas cercano a pacman--> cortar paso en 20 movimientos") )))	

(defrule INKYgoestoPacManin202
	(INKY (edible false) (second true) (distanceLessThan20 true)) 
	=> 
	(assert (ACTION (id INKYgoestoPacManin20) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule INKYgoestoPacManin203
	(INKY (edible false) (second true))
	=> 
	(assert (ACTION (id INKYgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule PINKYgoestoPacManin301
	(INKY (edible false) (third true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id INKYgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule PINKYgoestoPacManin302
	(PINKY (edible false) (third true))
	=> 
	(assert (ACTION (id PINKYgoestoPacManin40) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule INKYgoestoPacManin303
	(INKY (edible false) (last true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id INKYgoestoPacManin40) (info "No comestible y soy de los dos fantasmas mas lejanos--> cortar paso en 30 movimientos") )))
	
(defrule INKYgoestoPacManin304
	(INKY (edible false) (last true))
	=> 
	(assert (ACTION (id INKYgoestoPacManin50) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	