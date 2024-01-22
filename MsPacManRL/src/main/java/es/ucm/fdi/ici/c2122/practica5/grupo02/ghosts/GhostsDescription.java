package es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	Integer id;
	
	Integer time;
	Integer score;
	Integer lives;

	Integer mspacmanPos;
	Integer mspacmanLastMove;

	Integer[] nearestGhostsDistance;
	Integer nearestGhostsDistance0;
	Integer nearestGhostsDistance1;
	Integer nearestGhostsDistance2;
	Integer nearestGhostsDistance3;
	
	Boolean[] nearestGhostsEdible;
	Boolean nearestGhostsEdible0;
	Boolean nearestGhostsEdible1;
	Boolean nearestGhostsEdible2;
	Boolean nearestGhostsEdible3;
	
	MOVE[] nearestGhostsLastMoves;
	MOVE nearestGhostsLastMoves0;
	MOVE nearestGhostsLastMoves1;
	MOVE nearestGhostsLastMoves2;
	MOVE nearestGhostsLastMoves3;

	Integer ghostIndex;	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getTime() {
		return time;
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getLives() {
		return lives;
	}
	
	public void setLives(Integer lives) {
		this.lives = lives;
	}
	
	public Integer getMspacmanPos() {
		return mspacmanPos;
	}
	
	public void setMspacmanPos(Integer mspacmanPos) {
		this.mspacmanPos = mspacmanPos;
	}
	
	public Integer getMspacmanLastMove() {
		return mspacmanLastMove;
	}
	
	public void setMspacmanLastMove(Integer mspacmanLastMove) {
		this.mspacmanLastMove = mspacmanLastMove;
	}
	
	public Integer getNearestGhostsDistance(int idx) {
		return nearestGhostsDistance[idx];
	}

	public Integer getNearestGhostsDistance0() {
		return nearestGhostsDistance0;
	}

	public Integer getNearestGhostsDistance1() {
		return nearestGhostsDistance1;
	}

	public Integer getNearestGhostsDistance2() {
		return nearestGhostsDistance2;
	}

	public Integer getNearestGhostsDistance3() {
		return nearestGhostsDistance3;
	}
	
	public void setNearestGhostsDistance0(Integer nearestGhostsDistance0) {
		this.nearestGhostsDistance0 = nearestGhostsDistance0;
	}

	public void setNearestGhostsDistance1(Integer nearestGhostsDistance1) {
		this.nearestGhostsDistance1 = nearestGhostsDistance1;
	}

	public void setNearestGhostsDistance2(Integer nearestGhostsDistance2) {
		this.nearestGhostsDistance2 = nearestGhostsDistance2;
	}

	public void setNearestGhostsDistance3(Integer nearestGhostsDistance3) {
		this.nearestGhostsDistance3 = nearestGhostsDistance3;
	}

	public void setNearestGhostsDistance(Integer[] nearestGhostsDistance) {
		this.nearestGhostsDistance = nearestGhostsDistance;
		this.nearestGhostsDistance0 = nearestGhostsDistance[0];
		this.nearestGhostsDistance1 = nearestGhostsDistance[1];
		this.nearestGhostsDistance2 = nearestGhostsDistance[2];
		this.nearestGhostsDistance3 = nearestGhostsDistance[3];
	}
	
	public Boolean getNearestGhostsEdible(int idx) {
		return nearestGhostsEdible[idx];
	}
	
	public Boolean getNearestGhostsEdible0() {
		return nearestGhostsEdible0;
	}

	public Boolean getNearestGhostsEdible1() {
		return nearestGhostsEdible1;
	}

	public Boolean getNearestGhostsEdible2() {
		return nearestGhostsEdible2;
	}

	public Boolean getNearestGhostsEdible3() {
		return nearestGhostsEdible3;
	}

	public void setNearestGhostsEdible0(Boolean nearestGhostsEdible0) {
		this.nearestGhostsEdible0 = nearestGhostsEdible0;
	}

	public void setNearestGhostsEdible1(Boolean nearestGhostsEdible1) {
		this.nearestGhostsEdible1 = nearestGhostsEdible1;
	}

	public void setNearestGhostsEdible2(Boolean nearestGhostsEdible2) {
		this.nearestGhostsEdible2 = nearestGhostsEdible2;
	}

	public void setNearestGhostsEdible3(Boolean nearestGhostsEdible3) {
		this.nearestGhostsEdible3 = nearestGhostsEdible3;
	}
	
	public void setNearestGhostsEdible(Boolean[] nearestGhostsEdible) {
		this.nearestGhostsEdible = nearestGhostsEdible;
		this.nearestGhostsEdible0 = nearestGhostsEdible[0];
		this.nearestGhostsEdible1 = nearestGhostsEdible[1];
		this.nearestGhostsEdible2 = nearestGhostsEdible[2];
		this.nearestGhostsEdible3 = nearestGhostsEdible[3];
	}

	public MOVE getNearestGhostsLastMove(int idx) {
		return nearestGhostsLastMoves[idx];
	}
	
	public MOVE getNearestGhostsLastMoves0() {
		return nearestGhostsLastMoves0;
	}

	public MOVE getNearestGhostsLastMoves1() {
		return nearestGhostsLastMoves1;
	}

	public MOVE getNearestGhostsLastMoves2() {
		return nearestGhostsLastMoves2;
	}

	public MOVE getNearestGhostsLastMoves3() {
		return nearestGhostsLastMoves3;
	}
	
	public void setNearestGhostsLastMoves0(MOVE nearestGhostsLastMoves0) {
		this.nearestGhostsLastMoves0 = nearestGhostsLastMoves0;
	}

	public void setNearestGhostsLastMoves1(MOVE nearestGhostsLastMoves1) {
		this.nearestGhostsLastMoves1 = nearestGhostsLastMoves1;
	}

	public void setNearestGhostsLastMoves2(MOVE nearestGhostsLastMoves2) {
		this.nearestGhostsLastMoves2 = nearestGhostsLastMoves2;
	}

	public void setNearestGhostsLastMoves3(MOVE nearestGhostsLastMoves3) {
		this.nearestGhostsLastMoves3 = nearestGhostsLastMoves3;
	}

	public void setNearestGhostsLastMoves(MOVE[] nearestGhostsLastMoves) {
		this.nearestGhostsLastMoves = nearestGhostsLastMoves;
		this.nearestGhostsLastMoves0 = nearestGhostsLastMoves[0];
		this.nearestGhostsLastMoves1 = nearestGhostsLastMoves[1];
		this.nearestGhostsLastMoves2 = nearestGhostsLastMoves[2];
		this.nearestGhostsLastMoves3 = nearestGhostsLastMoves[3];
	}

	public Integer[] getNearestGhostsDistance() {
		return nearestGhostsDistance;
	}
	
	public Boolean[] getNearestGhostsEdible() {
		return nearestGhostsEdible;
	}

	public MOVE[] getNearestGhostsLastMoves() {
		return nearestGhostsLastMoves;
	}
	
	public void setGhostIndex(Integer ghostIdx) {
		this.ghostIndex = ghostIdx;
	}
	
	public Integer getGhostIndex() {
		return ghostIndex;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}

	@Override
	public String toString() {
		return "GhostsDescription [id=" + id + ", time=" + time + ", score=" + score +  ", lives=" + lives + ", mspacmanPos=" 
				+ mspacmanPos + ", mspacmanLastMove=" + mspacmanLastMove + ", nearestGhostsDistance0=" + nearestGhostsDistance0 
				+ ", nearestGhostsDistance1=" + nearestGhostsDistance1 + ", nearestGhostsDistance2=" + nearestGhostsDistance2 
				+ ", nearestGhostsDistance3=" + nearestGhostsDistance3 + ", nearestGhostsEdible0=" + nearestGhostsEdible0 
				+ ", nearestGhostsEdible1=" + nearestGhostsEdible1 + ", nearestGhostsEdible2=" + nearestGhostsEdible2 
				+ ", nearestGhostsEdible3=" + nearestGhostsEdible3 + ", nearestGhostsLastMoves0=" + nearestGhostsLastMoves0 
				+ ", nearestGhostsLastMoves1=" + nearestGhostsLastMoves1 + ", nearestGhostsLastMoves2=" + nearestGhostsLastMoves2 
				+ ", nearestGhostsLastMoves3=" + nearestGhostsLastMoves3 + ", ghostIndex =" + ghostIndex + "]";
	}	

}
