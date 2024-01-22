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

(defrule BLINKYanyGhostNonEdibleANDclosestToPacman 
	(BLINKY (edible true)) 
	(BLINKY (everyoneEdible false))
	(BLINKY (closestGhost true))
	=>
	(assert (ACTION (id BLINKYlureToGhost) (info "Mas cercano y fantasma no comestible --> lureToGhost") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule BLINKYanyGhostNonEdibleANDfarFromPacman 
	(BLINKY (edible true))
	(BLINKY (everyoneEdible false))
	(BLINKY (closestGhost false))
	=>
	(assert (ACTION (id BLINKYrunsAway) (info "Lejos y fantasma no comestible --> huir") (priority 1))))

;---------------------------------------


;++++++++++++++++++++++++++++++++++++++++++++++++++
; HUIR TODOS
;--------------------------------------------------

;**ATRAER A MSPACMAN**

(defrule BLINKYeveryGhostEdibleANDclosestToPacman 
	(BLINKY (edible true))
	(BLINKY (everyoneEdible true))
	(BLINKY (closestGhost true))
	=>
	(assert (ACTION (id BLINKYlureToLiberty) (info "Mas cercano y fantasma no comestible --> lureToLiberty") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule BLINKYeveryGhostEdibleANDfarFromPacman 
	(BLINKY (edible true)) 
	(BLINKY (everyoneEdible true))
	(BLINKY (closestGhost false))
	=>
	(assert (ACTION (id BLINKYrunsAway) (info "Todos comestibles --> huir") (priority 1))))


;--------------------------------------------------

;++++++++++++++++++++++++++++++++++++++++++++++++++
; PERSEGUIR
;--------------------------------------------------

;** IR A POR MSPACMAN**

(defrule BLINKYghostNonEdibleANDclosestToPacman 
	(BLINKY (edible false))
	(BLINKY (canKillPacman true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id BLINKYchase) (info "Mas cercano --> chasePacman") (priority 1))))

;** EMBOSCAR EVITANDO OTROS FANTASMAS**

(defrule BLINKYghostNonEdibleANDprio 
	(BLINKY (edible false)) 
	(BLINKY (canKillPacman false))
	(BLINKY (closerToPill false)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id BLINKYambush) (info "Lejos y prio --> Ambush") (priority 1))))

;** INTERCEPTAR PILDORA


(defrule BLINKYghostNonEdibleANDnotPrio 
	(BLINKY (edible false)) 
	(BLINKY (closerToPill true))
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id BLINKYintercept) (info "Lejos y not prio --> Intercept") (priority 3))))


;--------------------------------------------------
