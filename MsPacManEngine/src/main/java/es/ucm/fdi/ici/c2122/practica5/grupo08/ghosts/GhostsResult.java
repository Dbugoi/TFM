package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo08.FVector;

public class GhostsResult implements CaseComponent {

	public Integer id;
	public Integer score;		//puntos MsPacman
	public Boolean hasDied;	//si ha muerto en los ticks siguientes
	
	public FVector result = new FVector(4);		//valor entre [-1,1] para saber cuan de bueno es el caso

	public FVector getResult() {
		return result;
	}

	public void setResult(FVector result) {
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
		return new Attribute("id", GhostsResult.class);
	}
	
	@Override
	public String toString() {
		return "GhostsResult [id=" + id + ", score=" + score + ", hasDied=" + hasDied + ", score=" + result.toString() + "]";
	} 
		
}
