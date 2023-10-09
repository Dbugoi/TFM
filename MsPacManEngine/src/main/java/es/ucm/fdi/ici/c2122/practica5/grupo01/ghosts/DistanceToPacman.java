package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import pacman.game.Constants.MOVE;

public class DistanceToPacman implements LocalSimilarityFunction{

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		
		double similarity = 0; // evaluamos en esta funcion la distancia del fantasma hasta mspacman
		String elem1 = (String) caseObject;
		String elem2 = (String) queryObject;
		
		String parse1[] = elem1.split(" ");
		String parse2[] = elem2.split(" ");
		
		double dist1 = Double.valueOf(parse1[0]);  
		double dist2 = Double.valueOf(parse2[0]);
		
		MOVE m1 = MOVE.valueOf(parse1[1]);
		MOVE m2 = MOVE.valueOf(parse2[1]);
		
		if(m1 == m2) {
			Interval in = new Interval(200);
			similarity = in.compute(dist2, dist1);
		}

		
		return similarity;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}



}
