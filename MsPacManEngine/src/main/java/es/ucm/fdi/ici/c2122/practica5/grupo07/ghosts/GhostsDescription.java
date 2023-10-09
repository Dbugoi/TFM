package es.ucm.fdi.ici.c2122.practica5.grupo07.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ArrayTypeAdaptor;
import pacman.game.Constants.MOVE;


public class GhostsDescription implements CaseComponent {

	Integer id;
	
	Double score;
	Integer time;

	Double oldscore;
	Boolean edible;
	Double pacmamppdistance;
	ArrayTypeAdaptor pacmanProximity;
	MOVE lastmove;
	public Double getPacmamppdistance() {
		return pacmamppdistance;
	}

	public void setPacmamppdistance(Double pacmamppdistance) {
		this.pacmamppdistance = pacmamppdistance;
	}



	
	public Boolean getEdible() {
		return edible;
	}

	public void setEdible(Boolean edible) {
		this.edible = edible;
	}



	
	public Double getOldscore() {
		return oldscore;
	}

	public void setOldscore(Double oldscore) {
		this.oldscore = oldscore;
	}

	public ArrayTypeAdaptor getPacmanProximity() {
		return pacmanProximity;
	}

	public void setPacmanProximity(ArrayTypeAdaptor pacmanProximity) {
		this.pacmanProximity = pacmanProximity;
	}

	public MOVE getLastmove() {
		return lastmove;
	}

	public void setLastmove(MOVE lastmove) {
		this.lastmove = lastmove;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}



	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}



	@Override
	public String toString() {
	
		return "GhostDescription [id=" + id + ", score=" + score + ", pacmanProximity=" + pacmanProximity + ", lastmove=" + lastmove+ ", edible=" + edible+"]";
	}


	
	

}