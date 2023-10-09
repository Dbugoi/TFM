package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostsResultEX implements CaseComponent {

	Integer id;
	Integer score;
	Integer life;
	
	Integer ghostDist;
	
	Boolean isEdible;
	Boolean wasEaten;
	
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
	
	public Integer getLife() {
		return life;
	}

	public void setLife(Integer life) {
		this.life = life;
	}
	
	public Integer getGhostDist() {
		return ghostDist;
	}

	public void setGhostDist(Integer ghostDist) {
		this.ghostDist = ghostDist;
	}

	public Boolean getIsEdible() {
		return isEdible;
	}

	public void setIsEdible(Boolean isEdible) {
		this.isEdible = isEdible;
	}

	public Boolean getWasEaten() {
		return wasEaten;
	}

	public void setWasEaten(Boolean wasEaten) {
		this.wasEaten = wasEaten;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsResultEX.class);
	}
	
	@Override
	public String toString() {
		return "GhostsResult [id=" + id + 
				", score=" + score + 
				", ghostDist=" + ghostDist + 
				", isEdible=" + isEdible + 
				", wasEaten=" + wasEaten +  
					"]";
	} 
	
	

}
