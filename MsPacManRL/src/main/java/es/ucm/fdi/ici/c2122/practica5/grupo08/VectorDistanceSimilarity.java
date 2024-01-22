package es.ucm.fdi.ici.c2122.practica5.grupo08;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Devuelve la similitud entre dos vectores de floats
 */
public class VectorDistanceSimilarity implements LocalSimilarityFunction{
	
	/** Interval */
	float _mapDistance;

	/**
	 * Constructor.
	 */
	public VectorDistanceSimilarity(float interval) {
		_mapDistance = interval;
	}

	/**
	 * Aplica la función de similitud
	 * @param o1 Objeto 1 a comparar (FVector)
	 * @param o2 Objeto 2 a comparar (FVector)
	 * @return un valor entre 0 y 1 indicando la similitud entre los vectores
	 */
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		
		if ((caseObject == null) || (queryObject == null))
			return 0;
		if (!(caseObject instanceof FVector))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
		if (!(queryObject instanceof FVector))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
		
		FVector caseO = (FVector) caseObject;		
		FVector queryO = (FVector) queryObject;
		
		float dAtribute[] = new float[caseO.size];
		float media = 0;
		for(int i = 0; i< dAtribute.length; ++i) {
			float normal;
			if((caseO.values[i] == -1 && queryO.values[i] != -1) || (caseO.values[i] != -1 && queryO.values[i] == -1)) {
				normal = 1;
			}
			//Resta de distancia normalizada		
			else normal = (Math.abs(Math.abs(caseO.values[i]) - Math.abs(queryO.values[i])))/_mapDistance;
			if(normal > 1) normal = 1;

			//Calculamos la inversa. Cuanto menor sea la diferencia entre las distancias, mayor es la similitud
			dAtribute[i] =  1 - normal;
			media += dAtribute[i];
		}
		
		//media
		return media/caseO.size;
	}

	@Override
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof FVector;
		else if(o2==null)
			return o1 instanceof FVector;
		else
			return (o1 instanceof FVector)&&(o2 instanceof FVector);
	}
}
