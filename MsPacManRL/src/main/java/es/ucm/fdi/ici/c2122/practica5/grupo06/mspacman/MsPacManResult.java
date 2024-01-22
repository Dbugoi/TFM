package es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManResult;

public class MsPacManResult implements CaseComponent {

	Integer id;
	
	Integer score;
	Integer lives;
	
	Integer CloseDelta;
	Integer MidDelta;
	Integer FarDelta;
	
	public Integer getId() {
		return id;
	}

	public Integer getScore() {
		return score;
	}

	public Integer getLives() {
		return lives;
	}

	public Integer getCloseDelta() {
		return CloseDelta;
	}

	public Integer getMidDelta() {
		return MidDelta;
	}

	public Integer getFarDelta() {
		return FarDelta;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	public void setCloseDelta(Integer closeDelta) {
		CloseDelta = closeDelta;
	}

	public void setMidDelta(Integer midDelta) {
		MidDelta = midDelta;
	}

	public void setFarDelta(Integer farDelta) {
		FarDelta = farDelta;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManResult.class);
	}

	public double getFitness() {
		return score + (lives * 2000) + (CloseDelta * 0.5) + (MidDelta * 0.1) + (FarDelta * 0);
	}
	
	@Override
	public String toString() {
		return "MsPacManResult ["
				+ "id=" + id 
				+ ", score=" + score 
				+ ", Close Delta" + CloseDelta 
				+ ", Mid delta" + MidDelta 
				+ ", Far delta" + FarDelta 
				+ "]";
	} 
}
