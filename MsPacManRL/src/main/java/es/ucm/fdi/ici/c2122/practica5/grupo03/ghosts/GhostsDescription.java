package es.ucm.fdi.ici.c2122.practica5.grupo03.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.CbrVector;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	Integer id;
	
	Integer level;
	
	CbrVector DistanciaClosestGhostDirecction; // UP(0) , DOWN(1), LEFT(2), RIGHT(3)
	
	
	CbrVector DistanciaGhosts; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	CbrVector DistanciaClosestPPillDirecction; // UP(0) , DOWN(1), LEFT(2), RIGHT(3)
	
	CbrVector DistanciaGhostsClosestPPill; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	MOVE PacManLastMove;
	
	CbrVector GhostLastMove; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	CbrVector EdibleTimeGhosts; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)
	
	CbrVector JailTimeGhosts; // BLINKY(0) , PINKY(1), INKY(2), SUE(3)

	Integer score;
	
	Integer PillsLeft;
	
	//Integer time;
	
	

	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CbrVector getDistanciaClosestGhostDirecction() {
		return DistanciaClosestGhostDirecction;
	}

	public void setDistanciaClosestGhostDirecction(CbrVector distanciaClosestGhostDirecction) {
		DistanciaClosestGhostDirecction = distanciaClosestGhostDirecction;
	}

	public CbrVector getDistanciaGhosts() {
		return DistanciaGhosts;
	}

	public void setDistanciaGhosts(CbrVector distanciaGhosts) {
		DistanciaGhosts = distanciaGhosts;
	}

	public CbrVector getDistanciaClosestPPillDirecction() {
		return DistanciaClosestPPillDirecction;
	}

	public void setDistanciaClosestPPillDirecction(CbrVector distanciaClosestPPillDirecction) {
		DistanciaClosestPPillDirecction = distanciaClosestPPillDirecction;
	}

	public CbrVector getDistanciaGhostsClosestPPill() {
		return DistanciaGhostsClosestPPill;
	}

	public void setDistanciaGhostsClosestPPill(CbrVector distanciaGhostsClosestPPill) {
		DistanciaGhostsClosestPPill = distanciaGhostsClosestPPill;
	}

	public MOVE getPacManLastMove() {
		return PacManLastMove;
	}

	public void setPacManLastMove(MOVE pacManLastMove) {
		PacManLastMove = pacManLastMove;
	}

	public CbrVector getGhostLastMove() {
		return GhostLastMove;
	}

	public void setGhostLastMove(CbrVector ghostLastMove) {
		GhostLastMove = ghostLastMove;
	}

	public CbrVector getEdibleTimeGhosts() {
		return EdibleTimeGhosts;
	}

	public void setEdibleTimeGhosts(CbrVector edibleTimeGhosts) {
		EdibleTimeGhosts = edibleTimeGhosts;
	}

	public CbrVector getJailTimeGhosts() {
		return JailTimeGhosts;
	}

	public void setJailTimeGhosts(CbrVector jailTimeGhosts) {
		JailTimeGhosts = jailTimeGhosts;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getMapid() {
		return level;
	}

	public void setMapid(Integer mapid) {
		this.level = mapid;
	}

	public Integer getPillsLeft() {
		return PillsLeft;
	}

	public void setPillsLeft(Integer pillsLeft) {
		PillsLeft = pillsLeft;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", Mapid=" + level +
				", DistanciaClosestGhostDirection=" + DistanciaClosestGhostDirecction.toString() +
				", DistanciaGhost=" + DistanciaGhosts.toString() +
				", DistanciaClosestPPillDirecction=" + DistanciaClosestPPillDirecction.toString() +
				", DistanciaGhostsClosestPPill=" + DistanciaGhostsClosestPPill.toString() +
				", PacManLastMove=" + PacManLastMove +
				", GhostLastMove=" + GhostLastMove.toString() +
				", EdibleTimeGhosts=" + EdibleTimeGhosts.toString() +
				", JailTimeGhosts=" + JailTimeGhosts.toString() +
				", score=" + score + 
				", GhostName=" + ", PillsLeft=" + PillsLeft + "]";
	}
	

}
