package es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;

public class TimeGhostEdible implements LocalSimilarityFunction{
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		double similarity = 0;
		Integer elem1 = (Integer) caseObject;
		Integer elem2 = (Integer) queryObject;
		
		if(elem1 == 0 && elem2 == 0) { //Si los dos son no comestibles similitud = 1
			similarity = 1;
		}else if(elem1 != 0 && elem2 != 0) { //Si los dos son no comestibles un intervalo a 200
			Interval in = new Interval(200);
			similarity = in.compute(elem1, elem2);
		}
		//En otro caso devolvemos similitud 0
			
		return similarity;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}

