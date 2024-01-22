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
(defrule PINKYdispersarse
	(BLINKY (edible true))
	(INKY (edible true))
	(PINKY (edible true))
	(SUE (edible true))
	=>
	(assert
		(ACTION (id PINKYdispersarse) (info "dispersarse") (priority 20) 
		)
	)
) 
(defrule PINKYdontchaseothers
	(PINKY (dontchaseothers ?nm))
	(test (neq ?nm NEUTRAL))
	=>
	(assert
		(ACTION (id PINKYdontchaseothers) (info "dontchaseothers") (priority 69) (nmove ?nm)
		)
	)
) 
(defrule PINKYinterceptPP
	(MSPACMAN (numPP ?n)(someoneedible false)(closestPP ?pp))
	(test (> ?n 0))
	(PINKY (interceptPP true))
	=>
	(assert
		(ACTION (id PINKYinterceptPP) (info "Puede interceptarpp") (priority 50) (node_info ?pp)
		)
	)
) 
(defrule PINKYenclose
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (> ?n 0))
	(PINKY (interceptPP false)(edible false)(enclose_node ?e))
	(test (> ?e -1))
	=>
	(assert
		(ACTION (id PINKYenclose) (info "enclose") (priority 39) (node_info ?e)
		)
	)
) 
(defrule PINKYdontChase
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (> ?n 0))
	(BLINKY (interceptPP false))
	(INKY (interceptPP false))
	(PINKY (interceptPP false)(edible false))
	(SUE (interceptPP false))
	=>
	(assert
		(ACTION (id PINKYdontChase) (info "dont chase") (priority 40) 
		)
	)
) 
(defrule PINKYgoToYourDefender
	(PINKY (edible true)(defender ?d))
	(test (neq ?d null))
	=>
	(assert
		(ACTION (id PINKYgoToYourDefender) (info "goToYourDefender") (priority 80) (defender ?d)
		)
	)
) 
(defrule PINKYrun
	(PINKY (edible true)(defender ?d))
	(test (eq ?d null))
	=>
	(assert
		(ACTION (id PINKYrun) (info "run") (priority 80) (defender ?d)
		)
	)
) 
(defrule PINKYprotectOthers
	(BLINKY (edible false) (protect_node ?p))
	(test (> ?p -1))
	=>
	(assert
		(ACTION (id PINKYprotectOthers) (info "protectOthers") (priority 70)  (node_info ?p)
		)
	)
)  
(defrule PINKYnoPills
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (eq ?n 0))
	=>
	(assert
		(ACTION (id PINKYnoPills) (info "no pills") (priority 50)
		)
	)
) 
(defrule PINKYeatPacman
	(PINKY (eatPacman ?n))
	(test (neq ?n NEUTRAL))
	=>
	(assert
		(ACTION (id PINKYeatPacman) (info "eat pacman") (priority 100) (node_info ?n)
		)
	)
) 