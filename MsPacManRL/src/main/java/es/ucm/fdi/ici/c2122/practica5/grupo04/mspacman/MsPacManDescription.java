package es.ucm.fdi.ici.c2122.practica5.grupo04.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	Integer posPacman;
	Integer BLINKYghostPos;
	Integer PINKYghostPos;
	Integer INKYghostPos;
	Integer SUEghostPos;
	MOVE BLINKYghostMove;
	MOVE PINKYghostMove;
	MOVE INKYghostMove;
	MOVE SUEghostMove;
	MOVE lastPacmanDirection;
	Boolean BLINKYghostEdible;
	Boolean PINKYghostEdible;
	Boolean INKYghostEdible;
	Boolean SUEghostEdible;
	Integer ppillsPos1;
	Integer ppillsPos2;
	Integer ppillsPos3;
	Integer ppillsPos4;
	Integer closestPill;
	Integer nextJunctionPills1; // Arriba, derecha, abajo, izquierda
	Integer nextJunctionPills2; // -1 si no hay o no se puede ir
	Integer nextJunctionPills3;
	Integer nextJunctionPills4;
	Integer closestPillDist;
	Integer closestPPillDist;
	Float danger;  
	Integer health;
	Integer score;
	Integer time;
	
	public Integer getNextJunctionPills1() {
		return nextJunctionPills1;
	}

	public void setNextJunctionPills1(Integer nextJunctionPills1) {
		this.nextJunctionPills1 = nextJunctionPills1;
	}

	public Integer getNextJunctionPills2() {
		return nextJunctionPills2;
	}

	public void setNextJunctionPills2(Integer nextJunctionPills2) {
		this.nextJunctionPills2 = nextJunctionPills2;
	}

	public Integer getNextJunctionPills3() {
		return nextJunctionPills3;
	}

	public void setNextJunctionPills3(Integer nextJunctionPills3) {
		this.nextJunctionPills3 = nextJunctionPills3;
	}

	public Integer getNextJunctionPills4() {
		return nextJunctionPills4;
	}

	public void setNextJunctionPills4(Integer nextJunctionPills4) {
		this.nextJunctionPills4 = nextJunctionPills4;
	}

	public Integer getClosestPillDist() {
		return closestPillDist;
	}

	public void setClosestPillDist(Integer closestPillDist) {
		this.closestPillDist = closestPillDist;
	}
	
	
	public Integer getBLINKYghostPos() {
		return BLINKYghostPos;
	}

	public void setBLINKYghostPos(Integer bLINKYghostPos) {
		BLINKYghostPos = bLINKYghostPos;
	}

	public Integer getPINKYghostPos() {
		return PINKYghostPos;
	}

	public void setPINKYghostPos(Integer pINKYghostPos) {
		PINKYghostPos = pINKYghostPos;
	}

	public Integer getINKYghostPos() {
		return INKYghostPos;
	}

	public void setINKYghostPos(Integer iNKYghostPos) {
		INKYghostPos = iNKYghostPos;
	}

	public Integer getSUEghostPos() {
		return SUEghostPos;
	}

	public void setSUEghostPos(Integer sUEghostPos) {
		SUEghostPos = sUEghostPos;
	}

	public MOVE getBLINKYghostMove() {
		return BLINKYghostMove;
	}

	public void setBLINKYghostMove(MOVE bLINKYghostMove) {
		BLINKYghostMove = bLINKYghostMove;
	}

	public MOVE getPINKYghostMove() {
		return PINKYghostMove;
	}

	public void setPINKYghostMove(MOVE pINKYghostMove) {
		PINKYghostMove = pINKYghostMove;
	}

	public MOVE getINKYghostMove() {
		return INKYghostMove;
	}

	public void setINKYghostMove(MOVE iNKYghostMove) {
		INKYghostMove = iNKYghostMove;
	}

	public MOVE getSUEghostMove() {
		return SUEghostMove;
	}

	public void setSUEghostMove(MOVE sUEghostMove) {
		SUEghostMove = sUEghostMove;
	}

	public Boolean getBLINKYghostEdible() {
		return BLINKYghostEdible;
	}

	public void setBLINKYghostEdible(Boolean bLINKYghostEdible) {
		BLINKYghostEdible = bLINKYghostEdible;
	}

	public Boolean getPINKYghostEdible() {
		return PINKYghostEdible;
	}

	public void setPINKYghostEdible(Boolean pINKYghostEdible) {
		PINKYghostEdible = pINKYghostEdible;
	}

	public Boolean getINKYghostEdible() {
		return INKYghostEdible;
	}

	public void setINKYghostEdible(Boolean iNKYghostEdible) {
		INKYghostEdible = iNKYghostEdible;
	}

	public Boolean getSUEghostEdible() {
		return SUEghostEdible;
	}

	public void setSUEghostEdible(Boolean sUEghostEdible) {
		SUEghostEdible = sUEghostEdible;
	}

	public Integer getPpillsPos1() {
		return ppillsPos1;
	}

	public void setPpillsPos1(Integer ppillsPos1) {
		this.ppillsPos1 = ppillsPos1;
	}

	public Integer getPpillsPos2() {
		return ppillsPos2;
	}

	public void setPpillsPos2(Integer ppillsPos2) {
		this.ppillsPos2 = ppillsPos2;
	}

	public Integer getPpillsPos3() {
		return ppillsPos3;
	}

	public void setPpillsPos3(Integer ppillsPos3) {
		this.ppillsPos3 = ppillsPos3;
	}

	public Integer getPpillsPos4() {
		return ppillsPos4;
	}

	public void setPpillsPos4(Integer ppillsPos4) {
		this.ppillsPos4 = ppillsPos4;
	}

	public Integer getPosPacman() {
		return posPacman;
	}

	public void setPosPacman(Integer posPacman) {
		this.posPacman = posPacman;
	}

	
	public MOVE getLastPacmanDirection() {
		return lastPacmanDirection;
	}

	public void setLastPacmanDirection(MOVE lastPacmanDirection) {
		this.lastPacmanDirection = lastPacmanDirection;
	}

	

	public Integer getClosestPill() {
		return closestPill;
	}

	public void setClosestPill(Integer closestPill) {
		this.closestPill = closestPill;
	}

	public Integer getClosestPPillDist() {
		return closestPPillDist;
	}

	public void setClosestPPillDist(Integer closestPPillDist) {
		this.closestPPillDist = closestPPillDist;
	}

	public Float getDanger() {
		return danger;
	}

	public void setDanger(Float danger) {
		this.danger = danger;
	}

	public Integer getHealth() {
		return health;
	}

	public void setHealth(Integer health) {
		this.health = health;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", posPacman=" + posPacman + ", lastPacmanDirection=" + lastPacmanDirection + 
				", BLINKYghostPos=" + BLINKYghostPos + ", PINKYghostPos=" + PINKYghostPos + ", INKYghostPos=" + INKYghostPos + ", SUEghostPos=" + SUEghostPos +
				", BLINKYghostMove=" + BLINKYghostMove+ ", PINKYghostMove=" + PINKYghostMove + ", INKYghostMove=" + INKYghostMove + ", SUEghostMove=" + SUEghostMove +
				", BLINKYghostEdible=" + BLINKYghostEdible + ", PINKYghostEdible=" + PINKYghostEdible + ", INKYghostEdible=" + INKYghostEdible + ", SUEghostEdible=" + SUEghostEdible +
				", ppillsPos1=" + ppillsPos1 + ", ppillsPos2=" + ppillsPos2 + ", ppillsPos3=" + ppillsPos3 + ", ppillsPos4=" + ppillsPos4 +
				", closestPill=" + closestPill + ", closestPPillDist=" + closestPPillDist + ", score=" + score + ", time=" + time +
				", closestPillDist=" + closestPillDist + ", nextJunctionPills1=" + nextJunctionPills1 + ", nextJunctionPills2=" + nextJunctionPills2 + ", nextJunctionPills3=" + nextJunctionPills3 + ", nextJunctionPills4=" + nextJunctionPills4 +
				", danger=" + danger + ", health=" + health + "]";
	}


	
	

}
