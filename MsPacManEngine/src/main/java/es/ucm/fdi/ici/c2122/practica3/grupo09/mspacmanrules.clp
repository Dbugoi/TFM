;FACTS ASSERTED BY GAME INPUT

(deftemplate MSPACMAN 
	(slot closeToPowerPill (type SYMBOL)) 
	(slot closeToNonEdibleGhost (type SYMBOL)) 
	(slot closeToEdibleGhost (type SYMBOL))
	(slot existPowerPill(type SYMBOL)))
    
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER))) 
   
;RULES 
;(defrule BLINKYrunsAwayMSPACMANclosePPill
;	(MSPACMAN (mindistancePPill ?d)) (test (<= ?d 30)) 
;	=>  
;	(assert (ACTION (id BLINKYrunsAway) (info "MSPacMan cerca PPill"))))


;+++++++++++++++++++++++++++++++++++++++
; BUSCAR PILL
;---------------------------------------

;**IR A POWERPILL**

(defrule MSPACMANgoToPowerPillANDNoGhostsNear
	(MSPACMAN (closeToPowerPill true)) 
	(MSPACMAN (closeToNonEdibleGhost false))
	(MSPACMAN (closeToEdibleGhost false))
	=>
	(assert (ACTION (id MSPACMANgoToNearestPowerPill) (info "Seguro y cerca de PPill --> go to PPill") (priority 1))))

;------------------------------------------

;**IR A PILL**
(defrule MSPACMANgoToPillANDNoGhostsNear
	(MSPACMAN (closeToPowerPill false)) 
	(MSPACMAN (closeToNonEdibleGhost false))
	(MSPACMAN (closeToEdibleGhost false))
	=>
	(assert (ACTION (id MSPACMANgoToNearestPill) (info "Seguro y cerca de PPill --> go to PPill") (priority 1))))


;+++++++++++++++++++++++++++++++++++++++
; PERSEGUIR FANTASMAS
;---------------------------------------

;**PERSEGUIR FANTASMAS**

(defrule MSPACMANchaseNearestGhostANDNoGhostsNear
	(MSPACMAN (closeToNonEdibleGhost false))
	(MSPACMAN (closeToEdibleGhost true))	
	=>
	(assert (ACTION (id MSPACMANchaseNearestGhost) (info "Seguro y cerca de fantasma comestible --> go to fantasma") (priority 1))))

	
;+++++++++++++++++++++++++++++++++++++++
; HUIR
;---------------------------------------

;**EXISTE PPILL - HUIR HACIA PPILL**

(defrule MSPACMANfleeToPowerPill
	(MSPACMAN (closeToNonEdibleGhost true))
	(MSPACMAN (existPowerPill true))
	=>
	(assert (ACTION (id MSPACMANfleeToPowerPill) (info "En peligro y hay power pills --> go to ppill") (priority 1))))
	
;**NO EXISTE PPILL - HUIR POR CAMINO CON MAS PILLS**

(defrule MSPACMANfleeToPill
	(MSPACMAN (closeToNonEdibleGhost true))
	(MSPACMAN (existPowerPill false))
	=>
	(assert (ACTION (id MSPACMANfleeToPathWithMorePills) (info "En peligro y no hay power pills --> go to max pills") (priority 1))))
		
	
	