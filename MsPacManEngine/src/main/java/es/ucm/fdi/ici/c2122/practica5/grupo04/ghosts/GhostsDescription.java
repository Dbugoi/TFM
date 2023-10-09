package es.ucm.fdi.ici.c2122.practica5.grupo04.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	private Integer id;
	
	private Integer pacmanPos;
	private Integer ghostPos;
	private MOVE ghostMove;
	private MOVE lastPacmanMove;
	private Boolean ghostEdible;
	private Integer ghostPos1;
	private MOVE ghostMove1;
	private Boolean ghostEdible1;
	private Integer ghostPos2;
	private MOVE ghostMove2;
	private Boolean ghostEdible2;
	private Integer ghostPos3;
	private MOVE ghostMove3;
	private Boolean ghostEdible3;
	private Integer ppillsPos1;
	private Integer ppillsPos2;
	private Integer ppillsPos3;
	private Integer ppillsPos4;
	private Integer closestPill;
	private Integer nextJunctionPills1; // Arriba, derecha, abajo, izquierda
	private Integer nextJunctionPills2; // -1 si no hay o no se puede ir
	private Integer nextJunctionPills3;
	private Integer nextJunctionPills4;
	private Integer closestPillDist;
	private Integer closestPPillDist;
	private Float danger;  
	private Integer health;
	private Integer score;
	private Integer time;
	
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

	public Integer getPacmanPos() {
		return pacmanPos;
	}

	public void setPacmanPos(Integer pacmanPos) {
		this.pacmanPos = pacmanPos;
	}

	public Integer getGhostPos() {
		return ghostPos;
	}

	public void setGhostPos(Integer ghostPos) {
		this.ghostPos = ghostPos;
	}
	
	public MOVE getLastPacmanMove() {
		return lastPacmanMove;
	}

	public void setLastPacmanMove(MOVE lastPacmanDirection) {
		this.lastPacmanMove = lastPacmanDirection;
	}

	public MOVE getGhostMove() {
		return ghostMove;
	}

	public void setGhostMove(MOVE ghostMove) {
		this.ghostMove = ghostMove;
	}
	
	public Integer getGhostPos1() {
		return ghostPos1;
	}

	public void setGhostPos1(Integer ghostPos1) {
		this.ghostPos1 = ghostPos1;
	}

	public MOVE getGhostMove1() {
		return ghostMove1;
	}

	public void setGhostMove1(MOVE ghostMove1) {
		this.ghostMove1 = ghostMove1;
	}

	public Boolean getGhostEdible1() {
		return ghostEdible1;
	}

	public void setGhostEdible1(Boolean ghostEdible1) {
		this.ghostEdible1 = ghostEdible1;
	}

	public Integer getGhostPos2() {
		return ghostPos2;
	}

	public void setGhostPos2(Integer ghostPos2) {
		this.ghostPos2 = ghostPos2;
	}

	public MOVE getGhostMove2() {
		return ghostMove2;
	}

	public void setGhostMove2(MOVE ghostMove2) {
		this.ghostMove2 = ghostMove2;
	}

	public Boolean getGhostEdible2() {
		return ghostEdible2;
	}

	public void setGhostEdible2(Boolean ghostEdible2) {
		this.ghostEdible2 = ghostEdible2;
	}

	public Integer getGhostPos3() {
		return ghostPos3;
	}

	public void setGhostPos3(Integer ghostPos3) {
		this.ghostPos3 = ghostPos3;
	}

	public MOVE getGhostMove3() {
		return ghostMove3;
	}

	public void setGhostMove3(MOVE ghostMove3) {
		this.ghostMove3 = ghostMove3;
	}

	public Boolean getGhostEdible3() {
		return ghostEdible3;
	}

	public void setGhostEdible3(Boolean ghostEdible3) {
		this.ghostEdible3 = ghostEdible3;
	}

	public Boolean getGhostEdible() {
		return ghostEdible;
	}

	public void setGhostEdible(Boolean ghostEdible) {
		this.ghostEdible = ghostEdible;
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
		return new Attribute("id", GhostsDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", posPacman=" + pacmanPos + ", lastPacmanDirection=" + lastPacmanMove + 
				", ppillsPos1=" + ppillsPos1 + ", ppillsPos2=" + ppillsPos2 + ", ppillsPos3=" + ppillsPos3 + ", ppillsPos4=" + ppillsPos4 +
				", closestPill=" + closestPill + ", closestPPillDist=" + closestPPillDist + ", score=" + score + ", time=" + time +
				", closestPillDist=" + closestPillDist + ", nextJunctionPills1=" + nextJunctionPills1 + ", nextJunctionPills2=" + nextJunctionPills2 + ", nextJunctionPills3=" + nextJunctionPills3 + ", nextJunctionPills4=" + nextJunctionPills4 +
				", danger=" + danger + ", health=" + health + "]";
	}
}
