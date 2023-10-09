package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo08.BVector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.FVector;

public class MsPacManDescription implements CaseComponent {

	//id del componente
	public Integer id;

	//score de la partida
	public Integer score;
	//nivel en el que se juega
	public Integer level;
	//Vidas de MsPacman restantes
	public Integer HPs;
	//Posicion en la que se encuentra MsPacman
	public Integer pos;

	//Cada vector tiene para cada direccion
	//Distancia a los fantasmas no comestibles mas cercanos en cada direccion
	public FVector ghostDistances = new FVector(4);
	//Distancia a la PPill mas cercana en cada direccion
	public FVector powerPillDistances = new FVector(4);
	//Distancia a la pill mas cercana en cada direccion
	public FVector pillDistances = new FVector(4);
	//Distancia a los fantasmas comestibles mas cercanos en cada direccion
	public FVector edibleGhostsDistances = new FVector(4);

	public FVector getGhostDistances() {
		return ghostDistances;
	}
	
	public void setGhostDistances(FVector ghostDistances) {
		this.ghostDistances = ghostDistances;
	}
	
	public FVector getPowerPillDistances() {
		return powerPillDistances;
	}
	
	public void setPowerPillDistances(FVector powerPillDistances) {
		this.powerPillDistances = powerPillDistances;
	}
	
	public FVector getPillDistances() {
		return pillDistances;
	}
	
	public void setPillDistances(FVector pillDistances) {
		this.pillDistances = pillDistances;
	}
	
	public Integer getHPs() {
		return HPs;
	}

	public void setHPs(Integer hPs) {
		HPs = hPs;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public FVector getEdibleGhostsDistances() {
		return edibleGhostsDistances;
	}

	public void setEdibleGhostsDistances(FVector edibleGhosts) {
		this.edibleGhostsDistances = edibleGhosts;
	}
	
	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() {
		
		return "MsPacManDescription [id=" + id + ", score=" + score + ", level=" + level + ", Hps=" + HPs + ", pos=" + pos +", ghostDistances="
				+ ghostDistances.toString() + ", powerPillDistances=" + powerPillDistances.toString() + ", pillDistances=" 
				+ pillDistances.toString()  + ", edibleGhosts=" + edibleGhostsDistances.toString() + "]";
	}


	
	

}
