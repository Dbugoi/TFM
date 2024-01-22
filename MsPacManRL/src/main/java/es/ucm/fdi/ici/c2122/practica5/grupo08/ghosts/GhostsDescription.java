package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo08.BVector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.FVector;

public class GhostsDescription implements CaseComponent {

	public Integer id;	//id

	public Integer HPs;	//vida de MsPacman

	public Integer score;	//puntacion
	public Integer level;	//nivel de juego
	
	public FVector positions;	//posiciones de los fantasmas

	public FVector distanceToMsPacman = new FVector(4);	//distancias a MsPacman de cada uno de los fantamas
	public BVector edible = new BVector(4);				//si los fantamas son o no comestibles
	public BVector actives = new BVector(4);			//si los fantasmas se encuentran en la caja o no

	public Integer distanceMsPacmanPpill;	//distancia de MsPacMan a la power pill más cecana

	public BVector getActives() {
		return actives;
	}

	public void setActives(BVector actives) {
		this.actives = actives;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHPs() {
		return HPs;
	}

	public void setHPs(Integer hPs) {
		HPs = hPs;
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

	public FVector getPositions() {
		return positions;
	}

	public void setPositions(FVector positions) {
		this.positions = positions;
	}
	
	public FVector getDistanceToMsPacman() {
		return distanceToMsPacman;
	}

	public void setDistanceToMsPacman(FVector distanceToMsPacman) {
		this.distanceToMsPacman = distanceToMsPacman;
	}

	public BVector getEdible() {
		return edible;
	}

	public void setEdible(BVector edible) {
		this.edible = edible;
	}

	public Integer getDistanceMsPacmanPpill() {
		return distanceMsPacmanPpill;
	}

	public void setDistanceMsPacmanPpill(Integer distanceMsPacmanPpill) {
		this.distanceMsPacmanPpill = distanceMsPacmanPpill;
	}



	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}

	@Override
	public String toString() {
		
		return "MsPacManDescription [id=" + id + "HPs=" + HPs + ", score=" + score + ", level=" + level + ", positions=" + positions.toString() +
				", distanceToMsPacman= " +  distanceToMsPacman.toString() + ", edible: " + edible.toString() +  ", actives: " + actives.toString() +
				", distanceMsPacmanPpill: " + distanceMsPacmanPpill +"]";
	}


	
	

}
