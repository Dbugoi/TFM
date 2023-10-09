package es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine;

import java.util.Map;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Distance implements LocalSimilarityFunction{

	@SuppressWarnings("unchecked")
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		
		double similarity = 0;
		String elem1 = (String) caseObject;
		String elem2 = (String) queryObject;
		
		String parse1[] = elem1.split(" ");
		String parse2[] = elem2.split(" ");
		
		double dist1 = Double.valueOf(parse1[0]);  
		double dist2 = Double.valueOf(parse2[0]);
		
		MOVE m1 = MOVE.valueOf(parse1[1]);
		MOVE m2 = MOVE.valueOf(parse2[1]);
		
		//Si las distancia son grandes no miramos nada más y devolvemos 1
		if(dist1 > 200 && dist2 > 200) {
			similarity = 1;
			
		}else if(dist2 < 100) { //Si está cerca
			
			if(m1 == m2) { //El movimiento relativo tiene que ser el mismo
				Interval in = new Interval(200);
				similarity = in.compute(dist2, dist1);
			}
		}else if(dist2 < 200) { //Si está a media distancia
			if(m1 != m2.opposite()) { //El movimiento relativo no puede ser el opuesto
				Interval in = new Interval(200);
				similarity = in.compute(dist2, dist1);
			}
		}
		
		
		return similarity;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}



}
