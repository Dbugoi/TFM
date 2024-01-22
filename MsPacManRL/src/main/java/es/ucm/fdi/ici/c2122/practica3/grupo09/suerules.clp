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

(defrule SUEanyGhostNonEdibleANDclosestToPacman 
	(SUE (edible true)) 
	(SUE (everyoneEdible false))
	(SUE (closestGhost true))
	=>
	(assert (ACTION (id SUElureToGhost) (info "Mas cercano y fantasma no comestible --> lureToGhost") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule SUEanyGhostNonEdibleANDfarFromPacman 
	(SUE (edible true))
	(SUE (everyoneEdible false))
	(SUE (closestGhost false))
	=>
	(assert (ACTION (id SUErunsAway) (info "Lejos y fantasma no comestible --> huir") (priority 1))))

;---------------------------------------


;++++++++++++++++++++++++++++++++++++++++++++++++++
; HUIR TODOS
;--------------------------------------------------

;**ATRAER A MSPACMAN**

(defrule SUEeveryGhostEdibleANDclosestToPacman 
	(SUE (edible true))
	(SUE (everyoneEdible true))
	(SUE (closestGhost true))
	=>
	(assert (ACTION (id SUElureToLiberty) (info "Mas cercano y fantasma no comestible --> lureToLiberty") (priority 1))))

;**HUIR DE MSPACMAN**

(defrule SUEeveryGhostEdibleANDfarFromPacman 
	(SUE (edible true)) 
	(SUE (everyoneEdible true))
	(SUE (closestGhost false))
	=>
	(assert (ACTION (id SUErunsAway) (info "Todos comestibles --> huir") (priority 1))))


;--------------------------------------------------

;++++++++++++++++++++++++++++++++++++++++++++++++++
; PERSEGUIR
;--------------------------------------------------

;** IR A POR MSPACMAN**

(defrule SUEghostNonEdibleANDclosestToPacman 
	(SUE (edible false))
	(SUE (canKillPacman true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id SUEchase) (info "Mas cercano --> chasePacman") (priority 1))))

;** EMBOSCAR EVITANDO OTROS FANTASMAS**

(defrule SUEghostNonEdibleANDprio 
	(SUE (edible false)) 
	(SUE (canKillPacman false))
	(SUE (closerToPill true))
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id SUEambush) (info "Lejos y prio --> Ambush") (priority 1))))

;** INTERCEPTAR PILDORA


(defrule SUEghostNonEdibleANDnotPrio 
	(SUE (edible false)) 
	(SUE (closerToPill true)) 
	(MSPACMAN (closeToPill false))
	=>
	(assert (ACTION (id SUEintercept) (info "Lejos y not prio --> Intercept") (priority 1))))


;--------------------------------------------------
