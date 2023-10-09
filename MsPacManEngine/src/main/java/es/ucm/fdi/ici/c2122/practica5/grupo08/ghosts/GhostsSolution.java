package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo08.MOVEVector;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsSolution implements CaseComponent {
	public Integer id;
	
	public MOVEVector actions = new MOVEVector(4);
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public MOVEVector getActions() {
		return actions;
	}
	public void setActions(MOVEVector actions) {
		this.actions = actions;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsSolution.class);
	}
	
	@Override
	public String toString() {
		return "GhostsSolution [id=" + id + ", actions=" + actions + "]";
	}  
	
	
	
}
