package es.ucm.fdi.ici.c2122.practica5.grupo10.CBRengine;


import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * MODIFIED INTERVAL SIMILARITY FUNCTION
 * This function returns the similarity of two number inside an interval.
 * If both values are MAX, -1 is returned (in order to omit it in the average)
 * If one value is MAX, 0 is returned (in order to have lowest similarity)
 * 
 * Now it works with Number values.
 */
public class DistanceSim implements LocalSimilarityFunction {
	
	final int MAX = 200;
	/** Interval */
	double _interval;
	/**
	 * Constructor.
	 */
	public DistanceSim(double interval) {
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
	public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());


		Number i1 = (Number) o1;
		Number i2 = (Number) o2;
		
		double v1 = i1.doubleValue();
		double v2 = i2.doubleValue();
		if (v1 == v2 && v1 == MAX) return -1;
		if (v1 == 999 || v2 == MAX) return 0;
		else return 1 - ((double) Math.abs(v1 - v2) / _interval);
	}
	
	/** Applicable to Integer */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof Number;
		else if(o2==null)
			return o1 instanceof Number;
		else
			return (o1 instanceof Number)&&(o2 instanceof Number);
	}

}
