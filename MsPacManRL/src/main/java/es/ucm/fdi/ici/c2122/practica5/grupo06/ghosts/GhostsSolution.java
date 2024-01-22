package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class GhostsSolution implements CaseComponent {
	Integer id;
	
	MOVE Closeaction,
		MidCaction, 
		MidFaction,
		Faraction;
	
	
	public MOVE getCloseaction() {
		return Closeaction;
	}
	public void setCloseaction(MOVE closeaction) {
		Closeaction = closeaction;
	}
	public MOVE getMidCaction() {
		return MidCaction;
	}
	public void setMidCaction(MOVE midCaction) {
		MidCaction = midCaction;
	}
	public MOVE getMidFaction() {
		return MidFaction;
	}
	public void setMidFaction(MOVE midFaction) {
		MidFaction = midFaction;
	}
	public MOVE getFaraction() {
		return Faraction;
	}
	public void setFaraction(MOVE faraction) {
		Faraction = faraction;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	// devuelve un array de movimientos, ordenados de mas a menos cercano
	public MOVE[] getActions() {
		MOVE[] actions = new MOVE[4];
		actions[0] = Closeaction;
		actions[1] = MidCaction;
		actions[2] = MidFaction;
		actions[3] = Faraction;

		return actions;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsSolution.class);
	}
	
	@Override
	public String toString() {
		return "GhostsSolution [id=" + id + 
				", Closeaction=" + Closeaction +
				", MidCaction=" + MidCaction +
				", MidFaction=" + MidFaction +
				", Faraction=" + Faraction +"]";
	}  
}
