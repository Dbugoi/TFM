;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot interceptPP (type SYMBOL))
	(slot eatPacman (type SYMBOL))
	(slot edible (type SYMBOL))
	(slot defender (type SYMBOL))
	(slot dontchaseothers (type SYMBOL))
	(slot protect_node (type NUMBER))
	(slot enclose_node (type NUMBER))
)
	
(deftemplate INKY
	(slot interceptPP (type SYMBOL))
	(slot eatPacman (type SYMBOL))
	(slot edible (type SYMBOL))
	(slot defender (type SYMBOL))
	(slot dontchaseothers (type SYMBOL))
	(slot protect_node (type NUMBER))
	(slot enclose_node (type NUMBER))
)
	
(deftemplate PINKY
	(slot interceptPP (type SYMBOL))
	(slot eatPacman (type SYMBOL))
	(slot edible (type SYMBOL))
	(slot defender (type SYMBOL))
	(slot dontchaseothers (type SYMBOL))
	(slot protect_node (type NUMBER))
	(slot enclose_node (type NUMBER))
)

(deftemplate SUE
	(slot interceptPP (type SYMBOL))
	(slot eatPacman (type SYMBOL))
	(slot edible (type SYMBOL))
	(slot defender (type SYMBOL))
	(slot dontchaseothers (type SYMBOL))
	(slot protect_node (type NUMBER))
	(slot enclose_node (type NUMBER))
)
	
(deftemplate MSPACMAN
	(slot closestPP (type NUMBER))
	(slot numPP 	(type NUMBER))
	(slot someoneedible 	(type SYMBOL))
)
	
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
	(slot node_info (type NUMBER)) ; Extra slot for the runaway action
	(slot defender (type SYMBOL))
	(slot nmove (type SYMBOL))
) 


;RULES
(defrule BLINKYdispersarse
	(BLINKY (edible true))
	(INKY (edible true))
	(PINKY (edible true))
	(SUE (edible true))
	=>
	(assert
		(ACTION (id BLINKYdispersarse) (info "dispersarse") (priority 20) 
		)
	)
) 
(defrule BLINKYdontchaseothers
	(BLINKY (dontchaseothers ?nm))
	(test (neq ?nm NEUTRAL))
	=>
	(assert
		(ACTION (id BLINKYdontchaseothers) (info "dontchaseothers") (priority 69) (nmove ?nm)
		)
	)
) 
(defrule BLINKYinterceptPP
	(MSPACMAN (numPP ?n)(someoneedible false)(closestPP ?pp))
	(test (> ?n 0))
	(BLINKY (interceptPP true))
	=>
	(assert
		(ACTION (id BLINKYinterceptPP) (info "Puede interceptarpp") (priority 50) (node_info ?pp)
		)
	)
) 
(defrule BLINKYenclose
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (> ?n 0))
	(BLINKY (interceptPP false)(edible false) (enclose_node ?e))
	(test (> ?e -1))
	=>
	(assert
		(ACTION (id BLINKYenclose) (info "enclose") (priority 39) (node_info ?e)
		)
	)
) 
(defrule BLINKYdontChase
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (> ?n 0))
	(BLINKY (interceptPP false)(edible false))
	(INKY (interceptPP false))
	(PINKY (interceptPP false))
	(SUE (interceptPP false))
	=>
	(assert
		(ACTION (id BLINKYdontChase) (info "dont chase") (priority 40) 
		)
	)
) 
(defrule BLINKYgoToYourDefender
	(BLINKY (edible true)(defender ?d))
	(test (neq ?d null))
	=>
	(assert
		(ACTION (id BLINKYgoToYourDefender) (info "goToYourDefender") (priority 80) (defender ?d)
		)
	)
) 
(defrule BLINKYrun
	(BLINKY (edible true)(defender ?d))
	(test (eq ?d null))
	=>
	(assert
		(ACTION (id BLINKYrun) (info "run") (priority 80) (defender ?d)
		)
	)
) 
(defrule BLINKYprotectOthers
	(BLINKY (edible false) (protect_node ?p))
	(test (> ?p -1))
	=>
	(assert
		(ACTION (id BLINKYprotectOthers) (info "protectOthers") (priority 70)  (node_info ?p)
		)
	)
)
(defrule BLINKYnoPills
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (eq ?n 0))
	=>
	(assert
		(ACTION (id BLINKYnoPills) (info "no pills") (priority 50)
		)
	)
) 
(defrule BLINKYeatPacman
	(BLINKY (eatPacman ?n))
	(test (neq ?n NEUTRAL))
	=>
	(assert
		(ACTION (id BLINKYeatPacman) (info "eat pacman") (priority 100) (node_info ?n)
		)
	)
) 
	
	