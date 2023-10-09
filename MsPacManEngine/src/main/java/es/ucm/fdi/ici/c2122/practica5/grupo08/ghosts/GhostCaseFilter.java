package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

/*Clase que guarda la información para el tipo de filtro que queremos hacer*/
public class GhostCaseFilter extends CaseBaseFilter {
	//Tipo de filtro
	public enum Filter{ 
		EDIBLE,
		NO_EDIBLE
		};
		
	public Filter filter; 
	
	public GhostCaseFilter(Filter f) {
		filter = f;
	}
}
