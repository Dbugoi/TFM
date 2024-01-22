;FACTS ASSERTED BY GAME INPUT
(deftemplate BLINKY
	(slot inLair (type SYMBOL))
	(slot ambushPacman (type SYMBOL))
	(slot goToBestCorner (type SYMBOL))
	(slot followProtector (type SYMBOL))
	(slot moveToKill (type SYMBOL))
	(slot killPacMan (type SYMBOL))
	(slot blockPowerPill (type SYMBOL))
	(slot defendEdibleGhost (type SYMBOL)))

(deftemplate INKY
	(slot inLair (type SYMBOL))
	(slot ambushPacman (type SYMBOL))
	(slot goToBestCorner (type SYMBOL))
	(slot followProtector (type SYMBOL))
	(slot moveToKill (type SYMBOL))
	(slot killPacMan (type SYMBOL))
	(slot blockPowerPill (type SYMBOL))
	(slot defendEdibleGhost (type SYMBOL)))

(deftemplate PINKY
	(slot inLair (type SYMBOL))
	(slot ambushPacman (type SYMBOL))
	(slot goToBestCorner (type SYMBOL))
	(slot followProtector (type SYMBOL))
	(slot moveToKill (type SYMBOL))
	(slot killPacMan (type SYMBOL))
	(slot blockPowerPill (type SYMBOL))
	(slot defendEdibleGhost (type SYMBOL)))

(deftemplate SUE
	(slot inLair (type SYMBOL))
	(slot ambushPacman (type SYMBOL))
	(slot goToBestCorner (type SYMBOL))
	(slot followProtector (type SYMBOL))
	(slot moveToKill (type SYMBOL))
	(slot killPacMan (type SYMBOL))
	(slot blockPowerPill (type SYMBOL))
	(slot defendEdibleGhost (type SYMBOL)))

;DEFINITION OF THE ACTION FACT
(deftemplate ACTION
	(slot id) (slot info (default "")) (slot priority (type NUMBER)))







;RULES
(defrule INKYinLair
	(INKY (inLair true))
	=>
	(assert (ACTION (id INKYinLair) (info "INKY in lair") (priority 10)
			)
	)
)

(defrule INKYambushPacman
	(INKY  (ambushPacman true))
	=>
	(assert (ACTION (id INKYambushPacman) (info "INKY tries to ambush MsPacman") (priority 60)
			)
	)
)

(defrule INKYgoToBestCorner
	(INKY (goToBestCorner true))
	=>
	(assert (ACTION (id INKYgoToBestCorner) (info "INKY goes to best corner") (priority 50)
			)
	)
)

(defrule INKYfollowProtector
	(INKY (followProtector true))
	=>
	(assert (ACTION (id INKYfollowProtector) (info "INKY follows protector") (priority 30)
			)
	)
)

(defrule INKYmoveToKill
	(INKY (moveToKill true))
	=>
	(assert (ACTION (id INKYmoveToKill) (info "INKY moves to kill MsPacman") (priority 20)
			)
	)
)

(defrule INKYkillPacMan
	(INKY (killPacMan true))
	=>
	(assert (ACTION (id INKYkillPacMan) (info "INKY kills MsPacman") (priority 40)
			)
	)
)

(defrule INKYblockPowerPill
	(INKY (blockPowerPill true))
	=>
	(assert (ACTION (id INKYblockPowerPill) (info "INKY blocks powerpill") (priority 40)
			)
	)
)

(defrule INKYdefendEdibleGhost
	(INKY (defendEdibleGhost true))
	=>
	(assert (ACTION (id INKYdefendEdibleGhost) (info "INKY defends edible ghost") (priority 50)
			)
	)
)