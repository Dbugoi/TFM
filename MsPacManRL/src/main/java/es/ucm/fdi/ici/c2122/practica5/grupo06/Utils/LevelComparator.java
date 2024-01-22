package es.ucm.fdi.ici.c2122.practica5.grupo06.Utils;

import java.util.Comparator;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManDescription;

public class LevelComparator implements Comparator<CBRCase> {

	@Override
	public int compare(CBRCase o1, CBRCase o2) {	// Devuelve 1 si es el mismo nivel
		int level1 = ((MsPacManDescription)o1.getDescription()).getLevel();
		int level2 = ((MsPacManDescription)o1.getDescription()).getLevel();
		return level1 < level2 ? -1 : (level1 > level2 ? 1 : 0);
	}

}
