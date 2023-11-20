;FACTS ASSERTED BY GAME INPUT
(deftemplate CLOSESTPP
	(slot index (type INTEGER))
)
(deftemplate REACHABLEGHOST
	(slot anyedible (type SYMBOL))
	(slot anysleeping (type SYMBOL))
)
	  
;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER) ) ; mandatory slots
) 
;RULES 
(defrule eatPPRule
	(REACHABLEGHOST (anyedible false)(anysleeping false))
	(CLOSESTPP (index ?i))
	(test (neq ?i -1))
	=>  
	(assert (ACTION (id eatPP) (info "eat power pill") (priority 90))
	) 
)
(defrule eatPRule
	(REACHABLEGHOST (anyedible false)(anysleeping true))
	(CLOSESTPP (index ?i))
	(test (neq ?i -1))
	=>  
	(assert (ACTION (id eatP) (info "eat pills")(priority 10))
	) 
)
(defrule eatPRule
	(REACHABLEGHOST (anyedible false))
	(CLOSESTPP (index ?i))
	(test (eq ?i -1))
	=>  
	(assert (ACTION (id eatP) (info "eat pills")(priority 10))
	) 
)
(defrule chaseRule
	(REACHABLEGHOST (anyedible true))
	=>  
	(assert (ACTION (id chaseGhost) (info "chaseGhost") (priority 100))
	) 
)