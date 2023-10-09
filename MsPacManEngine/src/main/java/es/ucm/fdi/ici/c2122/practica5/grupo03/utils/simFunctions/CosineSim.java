package es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils.CbrVector;

public class CosineSim implements LocalSimilarityFunction {

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		// TODO Auto-generated method stub
		CbrVector casos = (CbrVector) caseObject;
		CbrVector querys = (CbrVector) queryObject;
		
		Integer[] caso = (Integer[]) casos.getVector();
		Integer[] query = (Integer[]) querys.getVector();
		
		double module1 = 0;
		double module2 = 0;
		double scalarprod = 0;
		int i = 0;
		for(i = 0; i < 4; i++) {
			module1 += caso[i] * caso[i];
			module2 += query[i] * query[i];
			scalarprod += caso[i] * query[i];
		}
		
		return (scalarprod / (module1 * module2));
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
