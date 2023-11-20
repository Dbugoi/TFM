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
(defrule SUEdispersarse
	(BLINKY (edible true))
	(INKY (edible true))
	(PINKY (edible true))
	(SUE (edible true))
	=>
	(assert
		(ACTION (id SUEdispersarse) (info "dispersarse") (priority 20) 
		)
	)
) 
(defrule SUEdontchaseothers
	(SUE (dontchaseothers ?nm))
	(test (neq ?nm NEUTRAL))
	=>
	(assert
		(ACTION (id SUEdontchaseothers) (info "dontchaseothers") (priority 69) (nmove ?nm)
		)
	)
) 
(defrule SUEinterceptPP
	(MSPACMAN (numPP ?n)(someoneedible false)(closestPP ?pp))
	(test (> ?n 0))
	(SUE (interceptPP true))
	=>
	(assert
		(ACTION (id SUEinterceptPP) (info "Puede interceptarpp") (priority 50) (node_info ?pp)
		)
	)
) 
(defrule SUEenclose
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (> ?n 0))
	(SUE (interceptPP false)(edible false)(enclose_node ?e))
	(test (> ?e -1))
	=>
	(assert
		(ACTION (id SUEenclose) (info "enclose") (priority 39) (node_info ?e)
		)
	)
) 
(defrule SUEdontChase
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (> ?n 0))
	(BLINKY (interceptPP false))
	(INKY (interceptPP false))
	(PINKY (interceptPP false))
	(SUE (interceptPP false)(edible false))
	=>
	(assert
		(ACTION (id SUEdontChase) (info "dont chase") (priority 40) 
		)
	)
) 
(defrule SUEgoToYourDefender
	(SUE (edible true)(defender ?d))
	(test (neq ?d null))
	=>
	(assert
		(ACTION (id SUEgoToYourDefender) (info "goToYourDefender") (priority 80) (defender ?d)
		)
	)
) 
(defrule SUErun
	(SUE (edible true)(defender ?d))
	(test (eq ?d null))
	=>
	(assert
		(ACTION (id SUErun) (info "run") (priority 80) (defender ?d)
		)
	)
) 
(defrule SUEprotectOthers
	(BLINKY (edible false) (protect_node ?p))
	(test (> ?p -1))
	=>
	(assert
		(ACTION (id SUEprotectOthers) (info "protectOthers") (priority 70)  (node_info ?p)
		)
	)
) 
(defrule SUEnoPills
	(MSPACMAN (numPP ?n)(someoneedible false))
	(test (eq ?n 0))
	=>
	(assert
		(ACTION (id SUEnoPills) (info "no pills") (priority 50)
		)
	)
) 
(defrule SUEeatPacman
	(SUE (eatPacman ?n))
	(test (neq ?n NEUTRAL))
	=>
	(assert
		(ACTION (id SUEeatPacman) (info "eat pacman") (priority 100) (node_info ?n)
		)
	)
) 