package es.ucm.fdi.ici.c2122.practica5.grupo10.mspacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	Integer score;
	//Menor distancia fantasma por cada lado
	Integer ghostDistance1;
	Integer ghostDistance2;
	Integer ghostDistance3;
	Integer ghostDistance4;
	//Menor distancia fantasma comestible por cada lado
	Integer edibleDistance1;
	Integer edibleDistance2;
	Integer edibleDistance3;
	Integer edibleDistance4;
	//Menor distancia pills por cada lado
	Integer pillsDistance1;
	Integer pillsDistance2;
	Integer pillsDistance3;
	Integer pillsDistance4;
	//Menor distancia power pills por cada lado
	Integer powerPillsDistance1;
	Integer powerPillsDistance2;
	Integer powerPillsDistance3;
	Integer powerPillsDistance4;
	String validDirections; //Direcctiones validas, string de 4 caracteres,
							//cada posicion representa una direccion 0 = invalid, 1 = valid
	Integer lives; //vida para +- puntos al resultado

	
	
	
	public MOVE getRandomMove() {
		Random rnd = new Random();
		ArrayList<MOVE> possibleMoves = new ArrayList<MOVE>();
		for (int i = 0; i < validDirections.length(); i++) {
			if (validDirections.charAt(i) == '1') {
				possibleMoves.add(MOVE.values()[i]);
			}
		}
		return possibleMoves.get(rnd.nextInt(possibleMoves.size()));
		
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
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", score=" + score 
				+ ", ghostDistance1=" + ghostDistance1
				+ ", ghostDistance2=" + ghostDistance2
				+ ", ghostDistance3=" + ghostDistance3
				+ ", ghostDistance4=" + ghostDistance4
				+ ", edibleDistance1=" + edibleDistance1
				+ ", edibleDistance2=" + edibleDistance2
				+ ", edibleDistance3=" + edibleDistance3
				+ ", edibleDistance4=" + edibleDistance4
				+ ", pillsDistance1=" + pillsDistance1
				+ ", pillsDistance2=" + pillsDistance2
				+ ", pillsDistance3=" + pillsDistance3
				+ ", pillsDistance4=" + pillsDistance4
				+ ", powerPillsDistance1=" + powerPillsDistance1
				+ ", powerPillsDistance2=" + powerPillsDistance2
				+ ", powerPillsDistance3=" + powerPillsDistance3
				+ ", powerPillsDistance4=" + powerPillsDistance4
				+ ", validDirections=" + validDirections + "]";
	}

	public String getValidDirections() {
		return validDirections;
	}

	public void setValidDirections(String lastMove) {
		this.validDirections = lastMove;
	}

	public Integer getGhostDistance1() {
		return ghostDistance1;
	}

	public void setGhostDistance1(Integer ghostDistance1) {
		this.ghostDistance1 = ghostDistance1;
	}

	public Integer getGhostDistance2() {
		return ghostDistance2;
	}

	public void setGhostDistance2(Integer ghostDistance2) {
		this.ghostDistance2 = ghostDistance2;
	}

	public Integer getGhostDistance3() {
		return ghostDistance3;
	}

	public void setGhostDistance3(Integer ghostDistance3) {
		this.ghostDistance3 = ghostDistance3;
	}

	public Integer getGhostDistance4() {
		return ghostDistance4;
	}

	public void setGhostDistance4(Integer ghostDistance4) {
		this.ghostDistance4 = ghostDistance4;
	}

	public Integer getEdibleDistance1() {
		return edibleDistance1;
	}

	public void setEdibleDistance1(Integer edibleDistance1) {
		this.edibleDistance1 = edibleDistance1;
	}

	public Integer getEdibleDistance2() {
		return edibleDistance2;
	}

	public void setEdibleDistance2(Integer edibleDistance2) {
		this.edibleDistance2 = edibleDistance2;
	}

	public Integer getEdibleDistance3() {
		return edibleDistance3;
	}

	public void setEdibleDistance3(Integer edibleDistance3) {
		this.edibleDistance3 = edibleDistance3;
	}

	public Integer getEdibleDistance4() {
		return edibleDistance4;
	}

	public void setEdibleDistance4(Integer edibleDistance4) {
		this.edibleDistance4 = edibleDistance4;
	}

	public Integer getPillsDistance1() {
		return pillsDistance1;
	}

	public void setPillsDistance1(Integer pillsDistance1) {
		this.pillsDistance1 = pillsDistance1;
	}

	public Integer getPillsDistance2() {
		return pillsDistance2;
	}

	public void setPillsDistance2(Integer pillsDistance2) {
		this.pillsDistance2 = pillsDistance2;
	}

	public Integer getPillsDistance3() {
		return pillsDistance3;
	}

	public void setPillsDistance3(Integer pillsDistance3) {
		this.pillsDistance3 = pillsDistance3;
	}

	public Integer getPillsDistance4() {
		return pillsDistance4;
	}

	public void setPillsDistance4(Integer pillsDistance4) {
		this.pillsDistance4 = pillsDistance4;
	}

	public Integer getPowerPillsDistance1() {
		return powerPillsDistance1;
	}

	public void setPowerPillsDistance1(Integer powerPillsDistance1) {
		this.powerPillsDistance1 = powerPillsDistance1;
	}

	public Integer getPowerPillsDistance2() {
		return powerPillsDistance2;
	}

	public void setPowerPillsDistance2(Integer powerPillsDistance2) {
		this.powerPillsDistance2 = powerPillsDistance2;
	}

	public Integer getPowerPillsDistance3() {
		return powerPillsDistance3;
	}

	public void setPowerPillsDistance3(Integer powerPillsDistance3) {
		this.powerPillsDistance3 = powerPillsDistance3;
	}

	public Integer getPowerPillsDistance4() {
		return powerPillsDistance4;
	}

	public void setPowerPillsDistance4(Integer powerPillsDistance4) {
		this.powerPillsDistance4 = powerPillsDistance4;
	}

	public Integer getLives() {
		return this.lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}
}
