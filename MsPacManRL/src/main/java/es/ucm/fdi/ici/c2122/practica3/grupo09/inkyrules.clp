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

(defrule INKYanyGhostNonEdibleANDclosestToPacman 
	(INKY (edible true)) 
	(INKY (everyoneEdible false))
	(INKY (closestGhost true))
	=>
	(assert (ACTION (id INKYlureToGhost) (info "Mas cercano y fantasma no comestible --> lureToGhost") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule INKYanyGhostNonEdibleANDfarFromPacman 
	(INKY (edible true))
	(INKY (everyoneEdible false))
	(INKY (closestGhost false))
	=>
	(assert (ACTION (id INKYrunsAway) (info "Lejos y fantasma no comestible --> huir") (priority 1))))

;---------------------------------------


;++++++++++++++++++++++++++++++++++++++++++++++++++
; HUIR TODOS
;--------------------------------------------------

;**ATRAER A MSPACMAN**

(defrule INKYeveryGhostEdibleANDclosestToPacman 
	(INKY (edible true))
	(INKY (everyoneEdible true))
	(INKY (closestGhost true))
	=>
	(assert (ACTION (id INKYlureToLiberty) (info "Mas cercano y fantasma no comestible --> lureToLiberty") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule INKYeveryGhostEdibleANDfarFromPacman 
	(INKY (edible true)) 
	(INKY (everyoneEdible true))
	(INKY (closestGhost false))
	=>
	(assert (ACTION (id INKYrunsAway) (info "Todos comestibles --> huir") (priority 1))))


;--------------------------------------------------

;++++++++++++++++++++++++++++++++++++++++++++++++++
; PERSEGUIR
;--------------------------------------------------

;** IR A POR MSPACMAN**

(defrule INKYghostNonEdibleANDclosestToPacman 
	(INKY (edible false))
	(INKY (canKillPacman true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id INKYchase) (info "Mas cercano --> chasePacman") (priority 1))))

;** EMBOSCAR EVITANDO OTROS FANTASMAS**

(defrule INKYghostNonEdibleANDprio 
	(INKY (edible false)) 
	(INKY (canKillPacman false))
	(INKY (closerToPill false))
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id INKYambush) (info "Lejos y prio --> Ambush") (priority 1))))

;** INTERCEPTAR PILDORA


(defrule INKYghostNonEdibleANDnotPrio 
	(INKY (edible false)) 
	(INKY (closerToPill true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id INKYintercept) (info "Lejos y not prio --> Intercept") (priority 1))))


;--------------------------------------------------
