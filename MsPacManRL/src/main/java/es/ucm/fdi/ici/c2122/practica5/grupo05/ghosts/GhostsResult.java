package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.FieldsToStringParser;

public class GhostsResult implements CaseComponent {

	Integer id;
	Integer score;
	
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

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsResult.class);
	}
	
	@Override
	public String toString() {
		return FieldsToStringParser.parse(this);
	} 
	
	

}