package es.ucm.fdi.ici.c2122.practica5.grupo08;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Devuelve la media de similitud entre las posiciones de un FVector
 */
public class EqualPositions implements LocalSimilarityFunction {
	
	/**
	 * Aplica la función de similitud
	 * @param o1 Objeto 1 a comparar (presumiblemente un vector de posiciones)
	 * @param o2 Objeto 2 a comparar (presumiblemente un vector de posiciones)
	 * @return Valor de la similitud entre ambos vectores de posiciones (entre 0 y 1)
	 */
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
    	if ((caseObject == null) || (queryObject == null))
			return 0;
		if (!(caseObject instanceof FVector))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
		if (!(queryObject instanceof FVector))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
		
		FVector caseO = (FVector) caseObject;		
		FVector queryO = (FVector) queryObject;
        
		Integer acum = 0;
		for(int i=0; i < caseO.values.length; i++) {
			Integer distance = Math.abs((int)caseO.values[i] - (int)queryO.values[i]);
			if(distance < 15) acum += 1;	
		}
		return acum/caseO.values.length;
    }
    
    /** Aplicable solo a FVector */
	public boolean isApplicable(Object o1, Object o2)
	{
		if (!(o1 instanceof FVector))
			return false;		
		if (!(o2 instanceof FVector))
			return false;
		
		return true;
	}
}
