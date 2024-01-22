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
(defrule BLINKYinLair
	(BLINKY (inLair true))
	=>
	(assert (ACTION (id BLINKYinLair) (info "BLINKY in lair") (priority 10)
			)
	)
)

(defrule BLINKYambushPacman
	(BLINKY  (ambushPacman true))
	=>
	(assert (ACTION (id BLINKYambushPacman) (info "BLINKY tries to ambush MsPacman") (priority 60)
			)
	)
)

(defrule BLINKYgoToBestCorner
	(BLINKY (goToBestCorner true))
	=>
	(assert (ACTION (id BLINKYgoToBestCorner) (info "BLINKY goes to best corner") (priority 50)
			)
	)
)

(defrule BLINKYfollowProtector
	(BLINKY (followProtector true))
	=>
	(assert (ACTION (id BLINKYfollowProtector) (info "BLINKY follows protector") (priority 30)
			)
	)
)

(defrule BLINKYmoveToKill
	(BLINKY (moveToKill true))
	=>
	(assert (ACTION (id BLINKYmoveToKill) (info "BLINKY moves to kill MsPacman") (priority 20)
			)
	)
)

(defrule BLINKYkillPacMan
	(BLINKY (killPacMan true))
	=>
	(assert (ACTION (id BLINKYkillPacMan) (info "BLINKY kills MsPacman") (priority 40)
			)
	)
)

(defrule BLINKYblockPowerPill
	(BLINKY (blockPowerPill true))
	=>
	(assert (ACTION (id BLINKYblockPowerPill) (info "BLINKY blocks powerpill") (priority 40)
			)
	)
)

(defrule BLINKYdefendEdibleGhost
	(BLINKY (defendEdibleGhost true))
	=>
	(assert (ACTION (id BLINKYdefendEdibleGhost) (info "BLINKY defends edible ghost") (priority 50)
			)
	)
)