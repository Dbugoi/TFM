package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class MsPacManSolution implements CaseComponent {
	public Integer id;
	//Movimiento a realizar
	public MOVE action;
	
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
		return new Attribute("id", MsPacManSolution.class);
	}
	
	@Override
	public String toString() {
		return "MsPacManSolution [id=" + id + ", action=" + action + "]";
	}  
	
	
	
}
