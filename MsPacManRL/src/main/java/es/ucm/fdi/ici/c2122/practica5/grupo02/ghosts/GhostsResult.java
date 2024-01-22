package es.ucm.fdi.ici.c2122.practica5.grupo02.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostsResult implements CaseComponent {

	Integer id;
	
	Integer scoreWeight;
	Integer livesLost;

	Boolean hasGhostBeenEaten;
	
	Boolean isGhostEdible;
	Integer ghostDst;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScoreWeight() {
		return scoreWeight;
	}

	public void setScoreWeight(Integer scoreWeight) {
		this.scoreWeight = scoreWeight;
	}
	
	public Integer getLivesLost() {
		return livesLost;
	}

	public void setLivesLost(Integer livesLost) {
		this.livesLost = livesLost;
	}

	public Boolean getHasGhostBeenEaten() {
		return hasGhostBeenEaten;
	}

	public void setHasGhostBeenEaten(Boolean hasGhostBeenEaten) {
		this.hasGhostBeenEaten = hasGhostBeenEaten;
	}
	
	public Boolean getIsGhostEdible() {
		return isGhostEdible;
	}
	
	public void setIsGhostEdible(Boolean isGhostEdible) {
		this.isGhostEdible = isGhostEdible;
	}
	
	public Integer getGhostDst() {
		return ghostDst;
	}

	public void setGhostDst(Integer ghostDst) {
		this.ghostDst = ghostDst;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsResult.class);
	}
	
	@Override
	public String toString() {
		return "GhostsResult [id=" + id + ", scoreWeight=" + scoreWeight + ", livesLost=" + livesLost +
				", hasGhostBeenEaten=" + hasGhostBeenEaten + ", isGhostEdible=" + isGhostEdible + 
				", ghostDst=" + ghostDst + "]";
	}

}
