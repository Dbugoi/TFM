package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;

public class GhostsDescriptionEX implements CaseComponent {
	Integer id;
	
	Integer level;
	Integer score;
	Integer time;
	Integer lives;
	
	Integer msPacmanPos;
	Integer msPacmanMove;
	
	Integer ghostDistance;
	
	Boolean wasEaten;
	Boolean isEdible;
	
	GHOST ghostType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	public Integer getMsPacmanPos() {
		return msPacmanPos;
	}

	public void setMsPacmanPos(Integer msPacmanPos) {
		this.msPacmanPos = msPacmanPos;
	}

	public Integer getMsPacmanMove() {
		return msPacmanMove;
	}

	public void setMsPacmanMove(Integer msPacmanMove) {
		this.msPacmanMove = msPacmanMove;
	}

	public Integer getGhostDistance() {
		return ghostDistance;
	}

	public void setGhostDistance(Integer ghostDistance) {
		this.ghostDistance = ghostDistance;
	}

	public Boolean getWasEaten() {
		return wasEaten;
	}

	public void setWasEaten(Boolean wasEaten) {
		this.wasEaten = wasEaten;
	}

	public Boolean getIsEdible() {
		return isEdible;
	}

	public void setIsEdible(Boolean isEdible) {
		this.isEdible = isEdible;
	}

	public GHOST getGhostType() {
		return ghostType;
	}

	public void setGhostType(GHOST ghostType) {
		this.ghostType = ghostType;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescriptionEX.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + 
		", level=" + level + 
		", score=" + score + 
		", time=" + time +
		", lives=" + lives +

		", msPacmanPos=" + msPacmanPos +
		", msPacmanMove=" + msPacmanMove +
		
		", ghostDistance=" + ghostDistance +
		", wasEaten=" + wasEaten +
		", isEdible=" + isEdible +
		", ghostType=" + ghostType +"]";
	}


	
	

}
