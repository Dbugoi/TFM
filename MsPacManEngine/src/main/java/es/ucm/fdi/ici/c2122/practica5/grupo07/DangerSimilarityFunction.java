package es.ucm.fdi.ici.c2122.practica5.grupo07;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

public class DangerSimilarityFunction implements LocalSimilarityFunction {

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		ArrayList<Double>casel=((ArrayTypeAdaptor)caseObject).getArray();
		ArrayList<Double>queryl=((ArrayTypeAdaptor)queryObject).getArray();

		int aux=0;
		for(int i=0;i<casel.size();i++) {
			if(casel.get(i).equals(queryl.get(i)) )aux++;
			//System.out.println(casel.get(i)+"  "+queryl.get(i)+"   "+(casel.get(i).equals(queryl.get(i)) ));
		}
		//System.out.println(aux);
		return aux==4?1.0:0.0;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
