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
(defrule PINKYinLair
	(PINKY (inLair true))
	=>
	(assert (ACTION (id PINKYinLair) (info "PINKY in lair") (priority 10)
			)
	)
)

(defrule PINKYambushPacman
	(PINKY  (ambushPacman true))
	=>
	(assert (ACTION (id PINKYambushPacman) (info "PINKY tries to ambush MsPacman") (priority 60)
			)
	)
)

(defrule PINKYgoToBestCorner
	(PINKY (goToBestCorner true))
	=>
	(assert (ACTION (id PINKYgoToBestCorner) (info "PINKY goes to best corner") (priority 50)
			)
	)
)

(defrule PINKYfollowProtector
	(PINKY (followProtector true))
	=>
	(assert (ACTION (id PINKYfollowProtector) (info "PINKY follows protector") (priority 30)
			)
	)
)

(defrule PINKYmoveToKill
	(PINKY (moveToKill true))
	=>
	(assert (ACTION (id PINKYmoveToKill) (info "PINKY moves to kill MsPacman") (priority 20)
			)
	)
)

(defrule PINKYkillPacMan
	(PINKY (killPacMan true))
	=>
	(assert (ACTION (id PINKYkillPacMan) (info "PINKY kills MsPacman") (priority 40)
			)
	)
)

(defrule PINKYblockPowerPill
	(PINKY (blockPowerPill true))
	=>
	(assert (ACTION (id PINKYblockPowerPill) (info "PINKY blocks powerpill") (priority 40)
			)
	)
)

(defrule PINKYdefendEdibleGhost
	(PINKY (defendEdibleGhost true))
	=>
	(assert (ACTION (id PINKYdefendEdibleGhost) (info "PINKY defends edible ghost") (priority 50)
			)
	)
)