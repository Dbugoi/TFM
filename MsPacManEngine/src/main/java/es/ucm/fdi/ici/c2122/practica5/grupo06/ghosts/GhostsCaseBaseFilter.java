package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

public class GhostsCaseBaseFilter extends CaseBaseFilter {
	private int level;
	private int numEdible;
	
	public GhostsCaseBaseFilter(){
	}
	
	public GhostsCaseBaseFilter(CBRQuery query){
		GhostsDescription res = (GhostsDescription) query.getDescription();
		setLevel(res.getLevel());
		setNumEdible((res.getCloseedible() ? 1 : 0) + (res.getMidCedible() ? 1 : 0) +
				(res.getMidFedible() ? 1 : 0) + (res.getFaredible() ? 1 : 0));
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNumEdible() {
		return numEdible;
	}

	public void setNumEdible(int numEdible) {
		this.numEdible = numEdible;
	}
	
}
