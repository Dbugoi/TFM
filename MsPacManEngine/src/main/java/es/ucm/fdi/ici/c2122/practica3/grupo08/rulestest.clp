; example clp file for debugging

(deftemplate BLINKY
	(slot edible (type SYMBOL))
	(slot edibleTime (type NUMBER))
	(slot position (type SYMBOL))
)

(deftemplate ACTION
	(slot id) 
	(slot info (default "")) 
	(slot ghost (type SYMBOL)) 
) 


(deffacts hechos-asertados
	(BLINKY (edible false) (edibleTime 20) (position UP))
)

(defrule regla1
	(BLINKY (edible false) (edibleTime ?t))
	(test (> ?t 5))
	=>
	(assert (ACTION (id correr) (ghost BLINKY)))	
)
