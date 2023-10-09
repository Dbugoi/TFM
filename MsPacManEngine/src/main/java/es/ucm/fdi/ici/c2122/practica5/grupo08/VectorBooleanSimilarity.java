package es.ucm.fdi.ici.c2122.practica5.grupo08;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Devuelve la similitud entre dos vectores de booleanos
 */
public class VectorBooleanSimilarity implements LocalSimilarityFunction{
	
	/**
	 * Aplica la función de similitud
	 * @param o1 Objeto 1 a comparar (BVector)
	 * @param o2 Objeto 2 a comparar (BVector)
	 * @return un valor entre 0 y 1 indicando la similitud entre los vectores
	 */
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		
		if ((caseObject == null) || (queryObject == null))
			return 0;
		if (!(caseObject instanceof BVector))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
		if (!(queryObject instanceof BVector))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
		
		BVector caseO = (BVector) caseObject;		
		BVector queryO = (BVector) queryObject;
		
		float dAtribute[] = new float[caseO.size];
		float media = 0;
		for(int i = 0; i< dAtribute.length; ++i) {
			//Funcion equal
			if(caseO.values[i] == queryO.values[i])
				dAtribute[i] = 1;
			else
				dAtribute[i] = 0;
			//media
			media += dAtribute[i];
		}

		return media/caseO.size;
	}

	@Override
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof BVector;
		else if(o2==null)
			return o1 instanceof BVector;
		else
			return (o1 instanceof BVector)&&(o2 instanceof BVector);
	}
}
