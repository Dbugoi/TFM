package es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	Integer score;
	Integer time;
	String nearestPPill; // power pill mas cercana

	Integer posPacman; // posicion pacman
	
	String posGhost1; // Posiciones fantasmas: 1 siendo el más cercano
	String posGhost2;
	String posGhost3;
	String posGhost4;
	
	String timeGhost1; // Tiempos comestibilidad: 1 siendo el fantasma mas cercano
	String timeGhost2;
	String timeGhost3;
	String timeGhost4;
	String closestPill;
	MOVE lastPacmanMove;
	
	Integer lives; // vidas
	Integer level; // nivel

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public String getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(String nearestPPill) {
		this.nearestPPill = nearestPPill;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	public Integer getPosPacman() {
		return posPacman;
	}

	public void setPosPacman(Integer posPacman) {
		this.posPacman = posPacman;
	}


	public MOVE getLastPacmanMove() {
		return lastPacmanMove;
	}

	public void setLastPacmanMove(MOVE lastPacmanMove) {
		this.lastPacmanMove = lastPacmanMove;
	}
	

	public String getPosGhost1() {
		return posGhost1;
	}

	public void setPosGhost1(String posGhost1) {
		this.posGhost1 = posGhost1;
	}

	public String getPosGhost2() {
		return posGhost2;
	}

	public void setPosGhost2(String posGhost2) {
		this.posGhost2 = posGhost2;
	}

	public String getPosGhost3() {
		return posGhost3;
	}

	public void setPosGhost3(String posGhost3) {
		this.posGhost3 = posGhost3;
	}

	public String getPosGhost4() {
		return posGhost4;
	}

	public void setPosGhost4(String posGhost4) {
		this.posGhost4 = posGhost4;
	}

	public String getTimeGhost1() {
		return timeGhost1;
	}

	public void setTimeGhost1(String timeGhost1) {
		this.timeGhost1 = timeGhost1;
	}

	public String getTimeGhost2() {
		return timeGhost2;
	}

	public void setTimeGhost2(String timeGhost2) {
		this.timeGhost2 = timeGhost2;
	}

	public String getTimeGhost3() {
		return timeGhost3;
	}

	public void setTimeGhost3(String timeGhost3) {
		this.timeGhost3 = timeGhost3;
	}

	public String getTimeGhost4() {
		return timeGhost4;
	}

	public void setTimeGhost4(String timeGhost4) {
		this.timeGhost4 = timeGhost4;
	}

	public String getClosestPill() {
		return closestPill;
	}

	public void setClosestPill(String closestPill) {
		this.closestPill = closestPill;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MsPacManDescription [id=");
		builder.append(id);
		builder.append(", score=");
		builder.append(score);
		builder.append(", time=");
		builder.append(time);
		builder.append(", nearestPPill=");
		builder.append(nearestPPill);
		builder.append(", posPacman=");
		builder.append(posPacman);
		builder.append(", posGhost1=");
		builder.append(posGhost1);
		builder.append(", posGhost2=");
		builder.append(posGhost2);
		builder.append(", posGhost3=");
		builder.append(posGhost3);
		builder.append(", posGhost4=");
		builder.append(posGhost4);
		builder.append(", timeGhost1=");
		builder.append(timeGhost1);
		builder.append(", timeGhost2=");
		builder.append(timeGhost2);
		builder.append(", timeGhost3=");
		builder.append(timeGhost3);
		builder.append(", timeGhost4=");
		builder.append(timeGhost4);
		builder.append(", closestPill=");
		builder.append(closestPill);
		builder.append(", lastPacmanMove=");
		builder.append(lastPacmanMove);
		builder.append(", lives=");
		builder.append(lives);
		builder.append(", level=");
		builder.append(level);
		builder.append("]");
		return builder.toString();
	}

	


}
