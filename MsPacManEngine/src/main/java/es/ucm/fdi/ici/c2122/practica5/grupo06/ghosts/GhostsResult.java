package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManResult;

public class GhostsResult implements CaseComponent{
	Integer id;
	Integer score;

	Integer PacmanLives;
	
	Integer Closedied, Closedelta,
			MidCdied, MidCdelta,
			MidFdied, MidFdelta,
			Fardied, Fardelta;
	
	public Integer getClosedelta() {
		return Closedelta;
	}

	public void setClosedelta(Integer closedelta) {
		Closedelta = closedelta;
	}

	public Integer getMidCdelta() {
		return MidFdelta;
	}

	public void setMidCdelta(Integer midcdelta) {
		MidCdelta = midcdelta;
	}

	public Integer getMidFdelta() {
		return MidFdelta;
	}

	public void setMidFdelta(Integer midfdelta) {
		MidFdelta = midfdelta;
	}

	public Integer getFardelta() {
		return Fardelta;
	}

	public void setFardelta(Integer fardelta) {
		Fardelta = fardelta;
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
	
	public void setPacmanLives(Integer pacmanLives) {
		PacmanLives = pacmanLives;
	}
	
	public Integer getPacmanLives() {
		return PacmanLives;
	}

	public Integer getClosedied() {
		return Closedied;
	}

	public Integer getMidCdied() {
		return MidCdied;
	}

	public Integer getMidFdied() {
		return MidFdied;
	}

	public Integer getFardied() {
		return Fardied;
	}
	
	public void setClosedied(Integer closedied) {
		Closedied = closedied;
	}
	
	public void setMidCdied(Integer midcdied) {
		MidCdied = midcdied;
	}
	
	public void setMidFdied(Integer midfdied) {
		MidFdied = midfdied;
	}
	
	public void setFardied(Integer fardied) {
		Fardied = fardied;
	}

	public double getFitness() {
		return this.score + (this.PacmanLives * 2000) 
						  + (this.Closedied * 500)
						  + (this.MidCdied * 500)
						  + (this.MidFdied * 500)
						  + (this.Fardied * 500);
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsResult.class);
	}
	
	@Override
	public String toString() {
		return "GhostsResult [id=" + id 
				+ ", score=" + score 
				+ ", PacmanLives=" + PacmanLives 
				+ ", BLINKYdied=" + Closedied 
				+ ", PINKYdied=" + MidCdied 
				+ ", INKYdied=" + MidFdied 
				+ ", SUEdied=" + Fardied + "]";
	} 
	

}
