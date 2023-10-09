package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.CBRengine;


import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Devuelve la similitud de dos coordenadas (X,Y) en una distancia dada
 * 
 */
public class EuclidianSimilarityInterval implements LocalSimilarityFunction {

	/** Interval */
	double _interval;

	/**
	 * Constructor.
	 */
	public EuclidianSimilarityInterval(double interval) {
		_interval = interval;
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param o1
	 *            Number
	 * @param o2
	 *            Number
	 * @return result of apply the similarity function.
	 */
	public double compute(Object[] o1, Object[] o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1[0] instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o1[1] instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2[0] instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		if (!(o2[1] instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());


		Number ix1 = (Number) o1[0];
		Number ix2 = (Number) o2[0];
		Number iy1 = (Number) o1[1];
		Number iy2 = (Number) o2[1];
		
		double v1 = Math.sqrt( Math.pow((double) ix1 - (double)ix2, 2) + Math.pow((double) iy1 - (double)iy2, 2));

		return 1 - ((double) (v1)/ _interval);
	}
	
	/** Applicable to Integer */
	public boolean isApplicable(Object[] o1, Object[] o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return (o2[0] instanceof Number)&&(o2[1] instanceof Number);
		else if(o2==null)
			return (o1[0] instanceof Number)&&(o1[1] instanceof Number);
		else
			return (o1[0] instanceof Number)&&(o2[0] instanceof Number)&&(o1[1] instanceof Number)&&(o2[1] instanceof Number);
	}

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

}