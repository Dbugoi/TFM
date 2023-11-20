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
(defrule PINKYrunsAwayFromPacman1
	(PINKY (edible false) (first false)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id PINKYrunsAwayFromPacman) (info "MSPacMan cerca PPill"))) )
	
(defrule PINKYrunsAwayFromPacman2
	(PINKY (edible true)) (PINKY (first false))
	=>  
	(assert (ACTION (id PINKYrunsAwayFromPacman) (info "No soy el mas cercano --> huir de pacman") )))
	
	
;Reglas si soy el mas cercano
(defrule PINKYrunsAwayFromGhost1
	(PINKY (edible true) (first true)) (MSPACMAN (mindistanceGhostless20 true))
	=>  
	(assert (ACTION (id PINKYrunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
(defrule PINKYrunsAwayFromGhost2
	(PINKY (edible false) (first true) (distanceLessThan20 true)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id PINKYrunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
;Regalse si a pacman no le da tiempo a comerme

(defrule PINKYstaysNearbyPacman
	(PINKY (edible true) (timeLessThanDistance true)) 
	=>  
	(assert (ACTION (id PINKYstaysNearbyPacman) (info "Pacman no le da tiempo a comerme --> Me mantengo en los alrededores") )))
	
	
;CHASE RULES

(defrule PINKYchases
	(PINKY (edible false) (first true) (distanceLessThan20 true))
	=> 
	(assert (ACTION (id PINKYchases) (info "No comestible, mas cercano y pacman a menos de 20 de distancia --> perseguir") )))
	
(defrule PINKYgoestoPacManin201
	(PINKY (edible false) (first true)) 
	=> 
	(assert (ACTION (id PINKYgoestoPacManin20) (info "No comestible y mas cercano a pacman--> cortar paso en 20 movimientos") )))	

(defrule PINKYgoestoPacManin202
	(PINKY (edible false) (second true) (distanceLessThan20 true)) 
	=> 
	(assert (ACTION (id PINKYgoestoPacManin20) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule PINKYgoestoPacManin203
	(PINKY (edible false) (second true))
	=> 
	(assert (ACTION (id PINKYgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule PINKYgoestoPacManin301
	(PINKY (edible false) (third true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id PINKYgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule PINKYgoestoPacManin302
	(PINKY (edible false) (third true))
	=> 
	(assert (ACTION (id PINKYgoestoPacManin40) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule PINKYgoestoPacManin303
	(PINKY (edible false) (last true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id PINKYgoestoPacManin40) (info "No comestible y soy de los dos fantasmas mas lejanos--> cortar paso en 30 movimientos") )))
	
(defrule PINKYgoestoPacManin304
	(PINKY (edible false) (last true))
	=> 
	(assert (ACTION (id PINKYgoestoPacManin50) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
	