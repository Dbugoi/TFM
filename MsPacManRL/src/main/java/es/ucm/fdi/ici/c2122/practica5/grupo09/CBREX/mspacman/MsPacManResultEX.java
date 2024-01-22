package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;

public class MsPacManResultEX implements CaseComponent {

	Integer id;
	Integer score;
	Integer life;
	
	Integer nearestPPillDist;
	
	Integer nearestGhost1;
	Integer nearestGhost2;
	Integer nearestGhost3;
	Integer nearestGhost4;
	
	Boolean nearestEdible1;
	Boolean nearestEdible2;
	Boolean nearestEdible3;
	Boolean nearestEdible4;
	
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
	
	public Integer getLife() {
		return life;
	}

	public void setLife(Integer life) {
		this.life = life;
	}

	public Integer getNearestPPillDist() {
		return nearestPPillDist;
	}

	public void setNearestPPillDist(Integer nearestPPillDist) {
		this.nearestPPillDist = nearestPPillDist;
	}
	
	public Integer getNearestGhost1() {
		return nearestGhost1;
	}

	public void setNearestGhost1(Integer nearestGhost1) {
		this.nearestGhost1 = nearestGhost1;
	}

	public Integer getNearestGhost2() {
		return nearestGhost2;
	}

	public void setNearestGhost2(Integer nearestGhost2) {
		this.nearestGhost2 = nearestGhost2;
	}

	public Integer getNearestGhost3() {
		return nearestGhost3;
	}

	public void setNearestGhost3(Integer nearestGhost3) {
		this.nearestGhost3 = nearestGhost3;
	}

	public Integer getNearestGhost4() {
		return nearestGhost4;
	}

	public void setNearestGhost4(Integer nearestGhost4) {
		this.nearestGhost4 = nearestGhost4;
	}

	public Boolean getNearestEdible1() {
		return nearestEdible1;
	}

	public void setNearestEdible1(Boolean nearestEdible1) {
		this.nearestEdible1 = nearestEdible1;
	}

	public Boolean getNearestEdible2() {
		return nearestEdible2;
	}

	public void setNearestEdible2(Boolean nearestEdible2) {
		this.nearestEdible2 = nearestEdible2;
	}

	public Boolean getNearestEdible3() {
		return nearestEdible3;
	}

	public void setNearestEdible3(Boolean nearestEdible3) {
		this.nearestEdible3 = nearestEdible3;
	}

	public Boolean getNearestEdible4() {
		return nearestEdible4;
	}

	public void setNearestEdible4(Boolean nearestEdible4) {
		this.nearestEdible4 = nearestEdible4;
	}

	public void setGhostsDist(Integer[] ghostsDist) {
		setNearestGhost1(ghostsDist[0]);
		setNearestGhost2(ghostsDist[1]);
		setNearestGhost3(ghostsDist[2]);
		setNearestGhost4(ghostsDist[3]);
	}

	public void setGhostsEdible(Boolean[] ghostsEdible) {
		setNearestEdible1(ghostsEdible[0]);
		setNearestEdible2(ghostsEdible[1]);
		setNearestEdible3(ghostsEdible[2]);
		setNearestEdible4(ghostsEdible[3]);
	}
	
	public int getGhostDist(int i) {
		if(i == 0) return nearestGhost1;
		if(i == 1) return nearestGhost2;
		if(i == 2) return nearestGhost3;
		if(i == 3) return nearestGhost4;

		
		return -1;
	}
	
	public boolean getGhostEdible(int i) {
		if(i == 0) return nearestEdible1;
		if(i == 1) return nearestEdible2;
		if(i == 2) return nearestEdible3;
		if(i == 3) return nearestEdible4;
		
		return false;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManResultEX.class);
	}
	
	@Override
	public String toString() {
		return "MsPacManResult [id=" + id + " " +
				", score=" + score + " " +
				", life=" + life + " " +
				", nearestPPillDist=" + nearestPPillDist + " " +
				", ghostsDist=" + nearestGhost1 + " " + nearestGhost2 + " " + nearestGhost3 + " " + nearestGhost4 + " " +
				", ghostsEdible=" + nearestEdible1 + " " + nearestEdible2 + " " + nearestEdible3 + " " + nearestEdible4 +
				"]";
	} 
	
	

}
