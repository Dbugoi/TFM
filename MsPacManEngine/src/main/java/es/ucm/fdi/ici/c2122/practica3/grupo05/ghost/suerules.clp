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
(defrule SUEinLair
	(SUE (inLair true))
	=>
	(assert (ACTION (id SUEinLair) (info "SUE in lair") (priority 10)
			)
	)
)

(defrule SUEambushPacman
	(SUE  (ambushPacman true))
	=>
	(assert (ACTION (id SUEambushPacman) (info "SUE tries to ambush MsPacman") (priority 60)
			)
	)
)

(defrule SUEgoToBestCorner
	(SUE (goToBestCorner true))
	=>
	(assert (ACTION (id SUEgoToBestCorner) (info "SUE goes to best corner") (priority 50)
			)
	)
)

(defrule SUEfollowProtector
	(SUE (followProtector true))
	=>
	(assert (ACTION (id SUEfollowProtector) (info "SUE follows protector") (priority 30)
			)
	)
)

(defrule SUEmoveToKill
	(SUE (moveToKill true))
	=>
	(assert (ACTION (id SUEmoveToKill) (info "SUE moves to kill MsPacman") (priority 20)
			)
	)
)

(defrule SUEkillPacMan
	(SUE (killPacMan true))
	=>
	(assert (ACTION (id SUEkillPacMan) (info "SUE kills MsPacman") (priority 40)
			)
	)
)

(defrule SUEblockPowerPill
	(SUE (blockPowerPill true))
	=>
	(assert (ACTION (id SUEblockPowerPill) (info "SUE blocks powerpill") (priority 40)
			)
	)
)

(defrule SUEdefendEdibleGhost
	(SUE (defendEdibleGhost true))
	=>
	(assert (ACTION (id SUEdefendEdibleGhost) (info "SUE defends edible ghost") (priority 50)
			)
	)
)