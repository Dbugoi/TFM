package es.ucm.fdi.ici.c2122.practica5.grupo05.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine.FieldsToStringParser;
import pacman.game.Constants.MOVE;

public class GhostsSolution implements CaseComponent {
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
		return new Attribute("id", GhostsSolution.class);
	}
	
	@Override
	public String toString() {
		return FieldsToStringParser.parse(this);
	}  
	
	
	
}
