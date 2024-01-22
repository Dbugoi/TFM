package es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostsResult implements CaseComponent {
	Integer id;
	Double score;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double resultValue) {
		this.score = resultValue;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsResult.class);
	}
	
	@Override
	public String toString() {
		return "GhostsResult [id=" + id + ", score=" + score + "]";
	} 
	
	
}
