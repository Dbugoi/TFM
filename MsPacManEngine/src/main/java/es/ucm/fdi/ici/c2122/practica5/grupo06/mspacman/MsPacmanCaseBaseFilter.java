package es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

public class MsPacmanCaseBaseFilter extends CaseBaseFilter {
	private int level;
	private int numEdible;
	
	public MsPacmanCaseBaseFilter(){
	}
	
	public MsPacmanCaseBaseFilter(CBRQuery query){
		MsPacManDescription res = (MsPacManDescription) query.getDescription();
		setLevel(res.getLevel());
		setNumEdible((res.getCloseEdible() ? 1 : 0) 
				+ (res.getMidEdible() ? 1 : 0) 
				+ (res.getFarEdible() ? 1 : 0));
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
