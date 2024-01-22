package es.ucm.fdi.ici.c2122.practica5.grupo08;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Devuelve 1 si ambas posiciones son iguales, 0 en caso contrario
 */
public class EqualPosition implements LocalSimilarityFunction {
	
	/**
	 * Aplica la función de similitud
	 * @param o1 Objeto 1 a comparar (presumiblemente una posicion)
	 * @param o2 Objeto 2 a comparar (presumiblemente una posicion)
	 * @return 1 si ambas posiciones son iguales, 0 en caso contrario
	 */
	@Override
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
    	if ((caseObject == null) || (queryObject == null))
			return 0;
		if (!(caseObject instanceof Integer))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
		if (!(queryObject instanceof Integer))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
		
		Integer caseO = (Integer) caseObject;		
		Integer queryO = (Integer) queryObject;
        
		Integer distance = Math.abs(caseO - queryO);
		
		if(distance <= 1) return 1;
		
        return 0;
    }
    
    /** Aplicable a cualquier clase */
	public boolean isApplicable(Object o1, Object o2)
	{
		return true;
	}
}
