package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class GhostsSolutionEX implements CaseComponent {
	
	Integer id;
	MOVE action;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MOVE getAction() {
		return action;
	}
	public void setAction(MOVE action) {
		this.action = action;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsSolutionEX.class);
	}
	
	@Override
	public String toString() {
		return "GhostsSolutionEX [id=" + id + ", action=" + action + "]";
	}  
	
	
	
}
