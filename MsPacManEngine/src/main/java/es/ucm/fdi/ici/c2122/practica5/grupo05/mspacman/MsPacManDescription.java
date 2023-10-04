package es.ucm.fdi.ici.c2122.practica5.grupo05.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo05.ListaEnteros;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.FieldsToStringParser;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	private Integer id;
	
	private Integer score;
	private Integer time;
	private Integer level;
	private Integer numEdible;
	private Integer lives;
	/*
	private String lDistPill;
	private String lDistPowerPill;
	private String lEdibleGhost;
	private String lChasingGhost;
	private String lTimeEdibleGhost;
	private String lLastMoveGhost;
	private String lDistJunction;
	private MOVE lastMove;
	*/
	//String currentMove;
	
	


	private Integer distPillUp, distPillDown, distPillLeft, distPillRight;
	

	private Integer distPowerPillUp, distPowerPillDown, distPowerPillLeft, distPowerPillRight;
	private Integer distEdibleUp, distEdibleDown, distEdibleLeft, distEdibleRight;
	private Integer distChasingUp, distChasingDown, distChasingLeft, distChasingRight;
	private Integer timeEdibleUp, timeEdibleDown, timeEdibleLeft, timeEdibleRight;
	private Integer distJunctionUp, distJunctionDown, distJunctionLeft, distJunctionRight;

	private Integer distJunction_ChasingUp, distJunction_ChasingDown, distJunction_ChasingLeft,
	distJunction_ChasingRight;
	

	private Integer lastMoveGhostUp, lastMoveGhostDown, lastMoveGhostLeft, lastMoveGhostRight;
	
	private Integer lastMoveMsPacman;
	
	public Integer getNumEdible() {
		return numEdible;
	}

	public void setNumEdible(Integer numEdible) {
		this.numEdible = numEdible;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	
	public Integer getDistPillUp() {
		return distPillUp;
	}

	public void setDistPillUp(Integer distPillUp) {
		this.distPillUp = distPillUp;
	}

	public Integer getDistPillDown() {
		return distPillDown;
	}

	public void setDistPillDown(Integer distPillDown) {
		this.distPillDown = distPillDown;
	}

	public Integer getDistPillLeft() {
		return distPillLeft;
	}

	public void setDistPillLeft(Integer distPillLeft) {
		this.distPillLeft = distPillLeft;
	}

	public Integer getDistPillRight() {
		return distPillRight;
	}

	public void setDistPillRight(Integer distPillRight) {
		this.distPillRight = distPillRight;
	}

	public Integer getDistPowerPillUp() {
		return distPowerPillUp;
	}

	public void setDistPowerPillUp(Integer distPowerPillUp) {
		this.distPowerPillUp = distPowerPillUp;
	}

	public Integer getDistPowerPillDown() {
		return distPowerPillDown;
	}

	public void setDistPowerPillDown(Integer distPowerPillDown) {
		this.distPowerPillDown = distPowerPillDown;
	}

	public Integer getDistPowerPillLeft() {
		return distPowerPillLeft;
	}

	public void setDistPowerPillLeft(Integer distPowerPillLeft) {
		this.distPowerPillLeft = distPowerPillLeft;
	}

	public Integer getDistPowerPillRight() {
		return distPowerPillRight;
	}

	public void setDistPowerPillRight(Integer distPowerPillRight) {
		this.distPowerPillRight = distPowerPillRight;
	}

	public Integer getDistEdibleUp() {
		return distEdibleUp;
	}

	public void setDistEdibleUp(Integer distEdibleUp) {
		this.distEdibleUp = distEdibleUp;
	}

	public Integer getDistEdibleDown() {
		return distEdibleDown;
	}

	public void setDistEdibleDown(Integer distEdibleDown) {
		this.distEdibleDown = distEdibleDown;
	}

	public Integer getDistEdibleLeft() {
		return distEdibleLeft;
	}

	public void setDistEdibleLeft(Integer distEdibleLeft) {
		this.distEdibleLeft = distEdibleLeft;
	}

	public Integer getDistEdibleRight() {
		return distEdibleRight;
	}

	public void setDistEdibleRight(Integer distEdibleRight) {
		this.distEdibleRight = distEdibleRight;
	}

	public Integer getDistChasingUp() {
		return distChasingUp;
	}

	public void setDistChasingUp(Integer distChasingUp) {
		this.distChasingUp = distChasingUp;
	}

	public Integer getDistChasingDown() {
		return distChasingDown;
	}

	public void setDistChasingDown(Integer distChasingDown) {
		this.distChasingDown = distChasingDown;
	}

	public Integer getDistChasingLeft() {
		return distChasingLeft;
	}

	public void setDistChasingLeft(Integer distChasingLeft) {
		this.distChasingLeft = distChasingLeft;
	}

	public Integer getDistChasingRight() {
		return distChasingRight;
	}

	public void setDistChasingRight(Integer distChasingRight) {
		this.distChasingRight = distChasingRight;
	}

	public Integer getTimeEdibleUp() {
		return timeEdibleUp;
	}

	public void setTimeEdibleUp(Integer distTimeEdibleUp) {
		this.timeEdibleUp = distTimeEdibleUp;
	}

	public Integer getTimeEdibleDown() {
		return timeEdibleDown;
	}

	public void setTimeEdibleDown(Integer distTimeEdibleDown) {
		this.timeEdibleDown = distTimeEdibleDown;
	}

	public Integer getTimeEdibleLeft() {
		return timeEdibleLeft;
	}

	public void setTimeEdibleLeft(Integer distTimeEdibleLeft) {
		this.timeEdibleLeft = distTimeEdibleLeft;
	}

	public Integer getTimeEdibleRight() {
		return timeEdibleRight;
	}

	public void setTimeEdibleRight(Integer distTimeEdibleRight) {
		this.timeEdibleRight = distTimeEdibleRight;
	}

	public Integer getDistJunctionUp() {
		return distJunctionUp;
	}

	public void setDistJunctionUp(Integer distJunctionUp) {
		this.distJunctionUp = distJunctionUp;
	}

	public Integer getDistJunctionDown() {
		return distJunctionDown;
	}

	public void setDistJunctionDown(Integer distJunctionDown) {
		this.distJunctionDown = distJunctionDown;
	}

	public Integer getDistJunctionLeft() {
		return distJunctionLeft;
	}

	public void setDistJunctionLeft(Integer distJunctionLeft) {
		this.distJunctionLeft = distJunctionLeft;
	}

	public Integer getDistJunctionRight() {
		return distJunctionRight;
	}

	public void setDistJunctionRight(Integer distJunctionRight) {
		this.distJunctionRight = distJunctionRight;
	}

	

	public Integer getLastMoveMsPacman() {
		return lastMoveMsPacman;
	}

	public void setLastMoveMsPacman(Integer lastMoveMsPacman) {
		this.lastMoveMsPacman = lastMoveMsPacman;
	}

	public Integer getDistJunction_ChasingUp() {
		return distJunction_ChasingUp;
	}

	public void setDistJunction_ChasingUp(Integer distJunction_ChasingUp) {
		this.distJunction_ChasingUp = distJunction_ChasingUp;
	}

	public Integer getDistJunction_ChasingDown() {
		return distJunction_ChasingDown;
	}

	public void setDistJunction_ChasingDown(Integer distJunction_ChasingDown) {
		this.distJunction_ChasingDown = distJunction_ChasingDown;
	}

	public Integer getDistJunction_ChasingLeft() {
		return distJunction_ChasingLeft;
	}

	public void setDistJunction_ChasingLeft(Integer distJunction_ChasingLeft) {
		this.distJunction_ChasingLeft = distJunction_ChasingLeft;
	}

	public Integer getDistJunction_ChasingRight() {
		return distJunction_ChasingRight;
	}

	public void setDistJunction_ChasingRight(Integer distJunction_ChasingRight) {
		this.distJunction_ChasingRight = distJunction_ChasingRight;
	}
	
	public Integer getLastMoveGhostUp() {
		return lastMoveGhostUp;
	}

	public void setLastMoveGhostUp(Integer lastMoveGhostUP) {
		this.lastMoveGhostUp = lastMoveGhostUP;
	}

	public Integer getLastMoveGhostDown() {
		return lastMoveGhostDown;
	}

	public void setLastMoveGhostDown(Integer lastMoveGhostDown) {
		this.lastMoveGhostDown = lastMoveGhostDown;
	}

	public Integer getLastMoveGhostLeft() {
		return lastMoveGhostLeft;
	}

	public void setLastMoveGhostLeft(Integer lastMoveGhostLeft) {
		this.lastMoveGhostLeft = lastMoveGhostLeft;
	}

	public Integer getLastMoveGhostRight() {
		return lastMoveGhostRight;
	}

	public void setLastMoveGhostRight(Integer lastMoveGhostRight) {
		this.lastMoveGhostRight = lastMoveGhostRight;
	}


	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() { //arreglar
		return FieldsToStringParser.parse(this);
	}


}
