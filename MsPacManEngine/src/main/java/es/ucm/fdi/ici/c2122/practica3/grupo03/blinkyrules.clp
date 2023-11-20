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
(defrule BLINKYrunsAwayFromPacman1
	(BLINKY (edible false) (first false)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id BLINKYrunsAwayFromPacman) (info "MSPacMan cerca PPill"))) )
	
(defrule BLINKYrunsAwayFromPacman2
	(BLINKY (edible true)) (BLINKY (first false))
	=>  
	(assert (ACTION (id BLINKYrunsAwayFromPacman) (info "No soy el mas cercano --> huir de pacman") )))
	
	
;Reglas si soy el mas cercano
(defrule BLINKYrunsAwayFromGhost1
	(BLINKY (edible true) (first true)) (MSPACMAN (mindistanceGhostless20 true)) 
	=>  
	(assert (ACTION (id BLINKYrunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost"))))
	
(defrule BLINKYrunsAwayFromGhost2
	(BLINKY (edible false) (first true) (distanceLessThan20 true)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id BLINKYrunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
;Regalse si a pacman no le da tiempo a comerme

(defrule BLINKYstaysNearbyPacman
	(BLINKY (edible true) (timeLessThanDistance true)) 
	=>  
	(assert (ACTION (id BLINKYstaysNearbyPacman) (info "Pacman no le da tiempo a comerme --> Me mantengo en los alrededores") )))
	
	
;CHASE RULES

(defrule BLINKYchases
	(BLINKY (edible false) (first true) (distanceLessThan20 true))
	=> 
	(assert (ACTION (id BLINKYchases) (info "No comestible, mas cercano y pacman a menos de 20 de distancia --> perseguir") )))
	
(defrule BLINKYgoestoPacManin201
	(BLINKY (edible false) (first true)) 
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin20) (info "No comestible y mas cercano a pacman--> cortar paso en 20 movimientos") )))	

(defrule BLINKYgoestoPacManin202
	(BLINKY (edible false) (second true) (distanceLessThan20 true)) 
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin20) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule BLINKYgoestoPacManin203
	(BLINKY (edible false) (second true))
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule BLINKYgoestoPacManin301
	(BLINKY (edible false) (third true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule BLINKYgoestoPacManin302
	(BLINKY (edible false) (third true))
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin40) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule BLINKYgoestoPacManin303
	(BLINKY (edible false) (last true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin40) (info "No comestible y soy de los dos fantasmas mas lejanos--> cortar paso en 30 movimientos") )))
	
(defrule BLINKYgoestoPacManin304
	(BLINKY (edible false) (last true))
	=> 
	(assert (ACTION (id BLINKYgoestoPacManin50) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
		
	