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
(defrule SUErunsAwayFromPacman1
	(SUE (edible false) (first false)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id SUErunsAwayFromPacman) (info "MSPacMan cerca PPill"))) )
	
(defrule SUErunsAwayFromPacman2
	(SUE (edible true)) (SUE (first false))
	=>  
	(assert (ACTION (id SUErunsAwayFromPacman) (info "No soy el mas cercano --> huir de pacman") )))
	
	
;Reglas si soy el mas cercano
(defrule SUErunsAwayFromGhost1
	(SUE (edible true) (first true)) (MSPACMAN (mindistanceGhostless20 true)) 
	=>  
	(assert (ACTION (id SUErunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
(defrule SUErunsAwayFromGhost2
	(SUE (edible false) (first true) (distanceLessThan20 true)) (MSPACMAN (mindistancePPillless30 true))
	=>  
	(assert (ACTION (id SUErunsAwayFromGhost) (info "Soy el mas cercano y me persigue --> huir de Ghost") )))
	
;Regalse si a pacman no le da tiempo a comerme

(defrule SUEstaysNearbyPacman
	(SUE (edible true) (timeLessThanDistance true)) 
	=>  
	(assert (ACTION (id SUEstaysNearbyPacman) (info "Pacman no le da tiempo a comerme --> Me mantengo en los alrededores") )))
	
	
;CHASE RULES

(defrule SUEchases
	(SUE (edible false) (first true) (distanceLessThan20 true))
	=> 
	(assert (ACTION (id SUEchases) (info "No comestible, mas cercano y pacman a menos de 20 de distancia --> perseguir") )))
	
(defrule SUEgoestoPacManin201
	(SUE (edible false) (first true)) 
	=> 
	(assert (ACTION (id SUEgoestoPacManin20) (info "No comestible y mas cercano a pacman--> cortar paso en 20 movimientos") )))	

(defrule SUEgoestoPacManin202
	(SUE (edible false) (second true) (distanceLessThan20 true)) 
	=> 
	(assert (ACTION (id SUEgoestoPacManin20) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule SUEgoestoPacManin203
	(SUE (edible false) (second true))
	=> 
	(assert (ACTION (id SUEgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule SUEgoestoPacManin301
	(SUE (edible false) (third true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id SUEgoestoPacManin30) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule SUEgoestoPacManin302
	(SUE (edible false) (third true))
	=> 
	(assert (ACTION (id SUEgoestoPacManin40) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))
	
(defrule SUEgoestoPacManin303
	(SUE (edible false) (last true) (distanceLessThan30 true)) 
	=> 
	(assert (ACTION (id SUEgoestoPacManin40) (info "No comestible y soy de los dos fantasmas mas lejanos--> cortar paso en 30 movimientos") )))
	
(defrule SUEgoestoPacManin304
	(SUE (edible false) (last true))
	=> 
	(assert (ACTION (id SUEgoestoPacManin50) (info "No comestible y segundo mas cercano a pacman--> cortar paso en 20 movimientos") )))

	
	