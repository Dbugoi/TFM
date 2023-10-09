package es.ucm.fdi.ici.c2122.practica5.grupo01.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.Pair;
import es.ucm.fdi.ici.c2122.practica5.grupo01.utils.SortedArrayList;
import pacman.game.Constants.MOVE;

public class TimeEdible implements LocalSimilarityFunction{
	
	

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {		
		
		double similarity = 0;
		String elem1 = (String) caseObject;
		String elem2 = (String) queryObject;
		
		String parse1[] = elem1.split(" ");
		String parse2[] = elem2.split(" ");
		//distancias
		double dist1 = Double.valueOf(parse1[0]);  
		double dist2 = Double.valueOf(parse2[0]);
		//tiempo comestible
		int time1 = Integer.valueOf(parse1[1]);
		int time2 = Integer.valueOf(parse2[1]);
		
		
		if(dist1 > 150 && dist2 > 150) {
			similarity = 1;
		}else if(dist2 < 70) {
			
			if(time1 == 0 && time2 == 0) {
				similarity = 1;
			}else if(time1 != 0 && time2 != 0) {
				Interval in = new Interval(200);
				similarity = in.compute(time1, time2);
			}
			
		}else if(dist2 <= 150) {
			Interval in = new Interval(200);
			similarity = in.compute(time1, time2);
		}
		
		return similarity;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
