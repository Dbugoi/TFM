package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;

/**
 * Clase para filtrar los casos de MsPacman en dos tipos: 
 * cuando al menos uno de los fantasmas es comestibles y cuando no lo es ninguno
 *
 */
public class MsPacmanCaseFilter extends CaseBaseFilter {
	public enum Filter{ 
		EDIBLE,
		NO_EDIBLE
		};
		
	public Filter filter; 
	
	/**
	 * Constructora de clase
	 */
	public MsPacmanCaseFilter(Filter f) {
		filter = f;
	}
}
