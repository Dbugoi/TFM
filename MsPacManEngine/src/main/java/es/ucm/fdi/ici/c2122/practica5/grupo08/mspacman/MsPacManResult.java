package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManResult implements CaseComponent {

	public Integer id;
	//puntos MsPacman
	public Integer score;		
	//si ha muerto en los ticks siguientes
	public Boolean hasDied;	
	
	//valor entre [-1,1] para saber cuan de bueno es el caso
	public Float result;		

	public Float getResult() {
		return result;
	}

	public void setResult(Float result) {
		this.result = result;
	}

	public Boolean getHasDied() {
		return hasDied;
	}

	public void setHasDied(Boolean hasDied) {
		this.hasDied = hasDied;
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

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManResult.class);
	}
	
	@Override
	public String toString() {
		return "MsPacManResult [id=" + id + ", score=" + score + ", hasDied=" + hasDied + ", result=" + result +"]";
	} 
	

	

}
