;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot edible (type SYMBOL)) 
	(slot everyoneEdible(type SYMBOL)) 
	(slot closestGhost (type SYMBOL)) 
	(slot thereIsPPill (type SYMBOL)) 
	(slot closerToPill (type SYMBOL)) 
	(slot canKillPacman (type SYMBOL)))
	
(deftemplate INKY
	(slot edible (type SYMBOL)) 
	(slot everyoneEdible(type SYMBOL)) 
	(slot closestGhost (type SYMBOL)) 
	(slot thereIsPPill (type SYMBOL)) 
	(slot closerToPill (type SYMBOL)) 
	(slot canKillPacman (type SYMBOL)))
	
(deftemplate PINKY
	(slot edible (type SYMBOL)) 
	(slot everyoneEdible(type SYMBOL)) 
	(slot closestGhost (type SYMBOL)) 
	(slot thereIsPPill (type SYMBOL)) 
	(slot closerToPill (type SYMBOL)) 
	(slot canKillPacman (type SYMBOL)))
	
(deftemplate SUE
	(slot edible (type SYMBOL)) 
	(slot everyoneEdible(type SYMBOL)) 
	(slot closestGhost (type SYMBOL)) 
	(slot thereIsPPill (type SYMBOL)) 
	(slot closerToPill (type SYMBOL)) 
	(slot canKillPacman (type SYMBOL)))

(deftemplate MSPACMAN 
	(slot closeToPill (type SYMBOL)))

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))) 
   
;RULES 
;(defrule BLINKYrunsAwayMSPACMANclosePPill
;	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
;	=>  
;	(assert (ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill"))) )

;+++++++++++++++++++++++++++++++++++++++
;HUIR CON FANTASMAS NO COMESTIBLES VIVOS
;---------------------------------------

;**ATRAER A MSPACMAN HACIA EL FANTASMA MAS CERCANO**

(defrule PINKYanyGhostNonEdibleANDclosestToPacman 
	(PINKY (edible true)) 
	(PINKY (everyoneEdible false))
	(PINKY (closestGhost true))
	=>
	(assert (ACTION (id PINKYlureToGhost) (info "Mas cercano y fantasma no comestible --> lureToGhost") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule PINKYanyGhostNonEdibleANDfarFromPacman 
	(PINKY (edible true))
	(PINKY (everyoneEdible false))
	(PINKY (closestGhost false))
	=>
	(assert (ACTION (id PINKYrunsAway) (info "Lejos y fantasma no comestible --> huir") (priority 1))))

;---------------------------------------


;++++++++++++++++++++++++++++++++++++++++++++++++++
; HUIR TODOS
;--------------------------------------------------

;**ATRAER A MSPACMAN**

(defrule PINKYeveryGhostEdibleANDclosestToPacman 
	(PINKY (edible true))
	(PINKY (everyoneEdible true))
	(PINKY (closestGhost true))
	=>
	(assert (ACTION (id PINKYlureToLiberty) (info "Mas cercano y fantasma no comestible --> lureToLiberty") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule PINKYeveryGhostEdibleANDfarFromPacman 
	(PINKY (edible true)) 
	(PINKY (everyoneEdible true))
	(PINKY (closestGhost false))
	=>
	(assert (ACTION (id PINKYrunsAway) (info "Todos comestibles --> huir") (priority 1))))


;--------------------------------------------------

;++++++++++++++++++++++++++++++++++++++++++++++++++
; PERSEGUIR
;--------------------------------------------------

;** IR A POR MSPACMAN**

(defrule PINKYghostNonEdibleANDclosestToPacman 
	(PINKY (edible false))
	(PINKY (canKillPacman true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id PINKYchase) (info "Mas cercano --> chasePacman") (priority 1))))

;** EMBOSCAR EVITANDO OTROS FANTASMAS**

(defrule PINKYghostNonEdibleANDprio 
	(PINKY (edible false)) 
	(PINKY (canKillPacman false))
	(PINKY (closerToPill false))
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id PINKYambush) (info "Lejos y prio --> Ambush") (priority 1))))

;** INTERCEPTAR PILDORA


(defrule PINKYghostNonEdibleANDnotPrio 
	(PINKY (edible false)) 
	(PINKY (closerToPill true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id PINKYintercept) (info "Lejos y not prio --> Intercept") (priority 1))))


;--------------------------------------------------
