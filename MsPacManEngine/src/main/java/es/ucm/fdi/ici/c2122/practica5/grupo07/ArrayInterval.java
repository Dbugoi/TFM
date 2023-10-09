package es.ucm.fdi.ici.c2122.practica5.grupo07;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.connector.plaintextutils.PlainTextTypeConverter;
public class ArrayInterval implements LocalSimilarityFunction {

	double _interval;

	/**
	 * Constructor.
	 */
	public ArrayInterval(double interval) {
		_interval = interval;
	}
	
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		if ((caseObject == null) || (queryObject == null))
			return 0;

		ArrayList<Double>i1=((ArrayTypeAdaptor)caseObject).getArray();
		ArrayList<Double>i2=((ArrayTypeAdaptor)queryObject).getArray();
		double r=1.0;

		for(int i=0;i<4;i++) {
			double v1 = (double) i1.get(i);
			double v2 = (double) i2.get(i);
			r-=((double) Math.abs(v1 - v2) / _interval)/4;
		}
		return r;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
