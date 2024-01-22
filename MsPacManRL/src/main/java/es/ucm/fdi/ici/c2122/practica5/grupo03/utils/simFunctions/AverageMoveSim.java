package es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.CbrVector;
import pacman.game.Constants.MOVE;

public class AverageMoveSim implements LocalSimilarityFunction {

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		// TODO Auto-generated method stub
		
		CbrVector casos = (CbrVector) caseObject;
		CbrVector querys = (CbrVector) queryObject;
		
		MOVE[] caso = (MOVE[]) casos.getVector();
		MOVE[] query = (MOVE[]) querys.getVector();
		
		double sum = 0;
		int i = 0;
		for(i = 0; i < 4; i++) {
			sum += (new MoveSim()).compute(caso[i], query[i]);
		}
		
		return (sum / 4);
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
