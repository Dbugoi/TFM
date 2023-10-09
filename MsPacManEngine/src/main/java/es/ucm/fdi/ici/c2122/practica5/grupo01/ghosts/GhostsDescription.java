package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	Integer id;
	
	Integer score;
	Integer time;
	String nearestPPill;
	Integer posGhost;
	
	//Para poder sacar el score
	GHOST ghostType;
	
	String posGhost1;
	String posGhost2;
	String posGhost3;
	String posPacman;
	
	String timeGhost1;
	String timeGhost2;
	String timeGhost3;
	
	Integer timeGhost;
	MOVE lastGhostMove;
	
	Integer lives;
	Integer level;
	
	
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
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



	public Integer getPosGhost() {
		return posGhost;
	}



	public void setPosGhost(Integer posGhost) {
		this.posGhost = posGhost;
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



	public String getPosPacman() {
		return posPacman;
	}



	public void setPosPacman(String posPacman) {
		this.posPacman = posPacman;
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



	public Integer getTimeGhost() {
		return timeGhost;
	}



	public void setTimeGhost(Integer timeGhost) {
		this.timeGhost = timeGhost;
	}



	public MOVE getLastGhostMove() {
		return lastGhostMove;
	}



	public void setLastGhostMove(MOVE lastGhostMove) {
		this.lastGhostMove = lastGhostMove;
	}



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



	public GHOST getGhostType() {
		return ghostType;
	}



	public void setGhostType(GHOST ghostType) {
		this.ghostType = ghostType;
	}



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GhostsDescription [id=");
		builder.append(id);
		builder.append(", score="); // Puntuacion,
		builder.append(score);
		builder.append(", time="); // tiempo,
		builder.append(time);
		builder.append(", nearestPPill="); // power pill mas cercana
		builder.append(nearestPPill);
		builder.append(", posGhost="); // posicion de este fantasma
		builder.append(posGhost);
		builder.append(", ghostType="); // que fantasma es
		builder.append(ghostType);
		builder.append(", posGhost1="); // posicion fantasma mas cercano
		builder.append(posGhost1);
		builder.append(", posGhost2=");
		builder.append(posGhost2);
		builder.append(", posGhost3=");
		builder.append(posGhost3);
		builder.append(", posPacman="); // posicion pacman
		builder.append(posPacman);
		builder.append(", timeGhost1="); // tiempo comestibilidad de fantasma mas cercano
		builder.append(timeGhost1);
		builder.append(", timeGhost2=");
		builder.append(timeGhost2);
		builder.append(", timeGhost3=");
		builder.append(timeGhost3);
		builder.append(", timeGhost="); // tiempo comestibilidad de este fantasma
		builder.append(timeGhost);
		builder.append(", lastGhostMove="); // ultimo movimiento de este fantasma
		builder.append(lastGhostMove);
		builder.append(", lives="); // vidas
		builder.append(lives);
		builder.append(", level="); // nivel de juego
		builder.append(level);
		builder.append("]");
		return builder.toString();
	}


	
	
	
}
