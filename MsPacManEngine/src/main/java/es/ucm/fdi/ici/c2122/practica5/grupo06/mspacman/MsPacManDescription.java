package es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManDescription;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	Integer id;	
	Integer score;
	
	Integer MsPacManNode;
	MOVE lastMove;
	
	Integer CloseNode;
	Boolean CloseEdible;
	MOVE CloseMove;
	Integer CloseDistance;
	
	Integer MidNode;
	Boolean MidEdible;
	MOVE MidMove;
	Integer MidDistance;
	
	Integer FarNode;
	Boolean FarEdible;
	MOVE FarMove;
	Integer FarDistance;
	
	Integer nearestPPill;
	Integer nearestPill;

	Integer lives;
	Integer time;
	Integer level;

	public Integer getId() {
		return id;
	}

	public Integer getScore() {
		return score;
	}

	public Integer getMsPacManNode() {
		return MsPacManNode;
	}

	public MOVE getLastMove() {
		return lastMove;
	}

	public Integer getCloseNode() {
		return CloseNode;
	}

	public Boolean getCloseEdible() {
		return CloseEdible;
	}

	public MOVE getCloseMove() {
		return CloseMove;
	}

	public Integer getCloseDistance() {
		return CloseDistance;
	}

	public Integer getMidNode() {
		return MidNode;
	}

	public Boolean getMidEdible() {
		return MidEdible;
	}

	public MOVE getMidMove() {
		return MidMove;
	}

	public Integer getMidDistance() {
		return MidDistance;
	}

	public Integer getFarNode() {
		return FarNode;
	}

	public Boolean getFarEdible() {
		return FarEdible;
	}

	public MOVE getFarMove() {
		return FarMove;
	}

	public Integer getFarDistance() {
		return FarDistance;
	}

	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public Integer getNearestPill() {
		return nearestPill;
	}

	public Integer getLives() {
		return lives;
	}

	public Integer getTime() {
		return time;
	}

	public Integer getLevel() {
		return level;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public void setMsPacManNode(Integer msPacManNode) {
		MsPacManNode = msPacManNode;
	}

	public void setLastMove(MOVE lastMove) {
		this.lastMove = lastMove;
	}

	public void setCloseNode(Integer closeNode) {
		CloseNode = closeNode;
	}

	public void setCloseEdible(Boolean closeEdible) {
		CloseEdible = closeEdible;
	}

	public void setCloseMove(MOVE closeMove) {
		CloseMove = closeMove;
	}

	public void setCloseDistance(Integer closeDistance) {
		CloseDistance = closeDistance;
	}

	public void setMidNode(Integer midNode) {
		MidNode = midNode;
	}

	public void setMidEdible(Boolean midEdible) {
		MidEdible = midEdible;
	}

	public void setMidMove(MOVE midMove) {
		MidMove = midMove;
	}

	public void setMidDistance(Integer midDistance) {
		MidDistance = midDistance;
	}

	public void setFarNode(Integer farNode) {
		FarNode = farNode;
	}

	public void setFarEdible(Boolean farEdible) {
		FarEdible = farEdible;
	}

	public void setFarMove(MOVE farMove) {
		FarMove = farMove;
	}

	public void setFarDistance(Integer farDistance) {
		FarDistance = farDistance;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}

	public void setNearestPill(Integer nearestPill) {
		this.nearestPill = nearestPill;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public Attribute getIdAttribute() {	return new Attribute("id", MsPacManDescription.class); }

	@Override
	public String toString() {
		return "MsPacManDescription [\n"
				+ "id=" + id 
				+ ", score="       	+ score 
				+ ", MsPacManNode=" + MsPacManNode 
				+ ", lastMove="     + lastMove	
				+ ", CloseNode="    + CloseNode
				+ ", CloseEdible="  + CloseEdible
				+ ", CloseMove="    + CloseMove
				+ ", CloseDistance="+ CloseDistance
				+ ", MidNode="     	+ MidNode
				+ ", MidEdible="   	+ MidEdible
				+ ", MidMove="     	+ MidMove
				+ ", MidDistance="	+ MidDistance
				+ ", FarNode="     	+ FarNode
				+ ", FarEdible="   	+ FarEdible
				+ ", FarMove="     	+ FarMove
				+ ", FarDistance="	+ FarDistance
				+ ", nearestPPill=" + nearestPPill 
				+ ", nearestPPill=" + nearestPill 
				+ ", lives="        + lives
				+ ", time="         + time 
				+ ", level="        + level
				+ "\n]" ;
	}
}
