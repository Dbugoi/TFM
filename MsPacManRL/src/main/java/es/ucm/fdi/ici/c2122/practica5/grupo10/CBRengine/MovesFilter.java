package es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

public class MovesFilter extends CaseBaseFilter{
	private String _filter;
	
	public MovesFilter(String filter) {
		_filter = filter;
	}
	
	public String getFilter() {
		return _filter;
	}
}
