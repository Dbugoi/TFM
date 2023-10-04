package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.AdaptorMoveIntMap;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.FieldsToStringParser;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	Integer id;
	
	private GHOST ghost;
	
	private Integer score;
	private Integer time;
	private Integer level;

	private Integer edibleTime;

	private AdaptorMoveIntMap distToPacman;
	private Integer distPacmanToGhost;
	private MOVE movePacmanToGhost;

	private AdaptorMoveIntMap distToJunction;

	private AdaptorMoveIntMap distToPPill;
	private Integer distPacmanToPPill;
	private MOVE movePacmanToPPill;

	private AdaptorMoveIntMap distToEdible;
	private Integer distPacmanToEdible;
	private MOVE movePacmanToEdible;
	// distToChasing

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}

	public GHOST getGhost() {
		return ghost;
	}

	public void setGhost(GHOST ghost) {
		this.ghost = ghost;
	}

	public AdaptorMoveIntMap getDistToJunction() {
		return distToJunction;
	}

	public void setDistToJunction(AdaptorMoveIntMap distToJunction) {
		this.distToJunction = distToJunction;
	}

	public Integer getEdibleTime() {
		return edibleTime;
	}

	public void setEdibleTime(Integer edibleTime) {
		this.edibleTime = edibleTime;
	}

	@Override
	public String toString() {
		return FieldsToStringParser.parse(this);
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

	public AdaptorMoveIntMap getDistToPacman() {
		return distToPacman;
	}

	public void setDistToPacman(AdaptorMoveIntMap distToPacman) {
		this.distToPacman = distToPacman;
	}

	public AdaptorMoveIntMap getDistToPPill() {
		return distToPPill;
	}

	public void setDistToPPill(AdaptorMoveIntMap distToPPill) {
		this.distToPPill = distToPPill;
	}

	public Integer getDistPacmanToPPill() {
		return distPacmanToPPill;
	}

	public void setDistPacmanToPPill(Integer distPacmanToPPill) {
		this.distPacmanToPPill = distPacmanToPPill;
	}

	public MOVE getMovePacmanToPPill() {
		return movePacmanToPPill;
	}

	public void setMovePacmanToPPill(MOVE movePacmanToPPill) {
		this.movePacmanToPPill = movePacmanToPPill;
	}

	public Integer getDistPacmanToGhost() {
		return distPacmanToGhost;
	}

	public void setDistPacmanToGhost(Integer distPacmanToGhost) {
		this.distPacmanToGhost = distPacmanToGhost;
	}

	public MOVE getMovePacmanToGhost() {
		return movePacmanToGhost;
	}

	public void setMovePacmanToGhost(MOVE movePacmanToGhost) {
		this.movePacmanToGhost = movePacmanToGhost;
	}

	public AdaptorMoveIntMap getDistToEdible() {
		return distToEdible;
	}

	public void setDistToEdible(AdaptorMoveIntMap distToEdible) {
		this.distToEdible = distToEdible;
	}

	public Integer getDistPacmanToEdible() {
		return distPacmanToEdible;
	}

	public void setDistPacmanToEdible(Integer distPacmanToEdible) {
		this.distPacmanToEdible = distPacmanToEdible;
	}

	public MOVE getMovePacmanToEdible() {
		return movePacmanToEdible;
	}

	public void setMovePacmanToEdible(MOVE movePacmanToEdible) {
		this.movePacmanToEdible = movePacmanToEdible;
	}

	
}
