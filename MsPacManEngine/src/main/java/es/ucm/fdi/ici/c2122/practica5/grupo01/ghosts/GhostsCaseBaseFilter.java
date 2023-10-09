package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

public class GhostsCaseBaseFilter extends CaseBaseFilter {

	private int nivel, junction;
	
	public GhostsCaseBaseFilter(int nivel, int junction) {
		this.nivel = nivel;
		this.junction = junction;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getJunction() {
		return junction;
	}

	public void setJunction(int junction) {
		this.junction = junction;
	}
	
}
