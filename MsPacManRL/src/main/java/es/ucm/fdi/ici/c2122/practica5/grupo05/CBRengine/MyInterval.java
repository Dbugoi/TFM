package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;

public class MyInterval extends Interval {

    public MyInterval(double interval) {
        super(interval);
    }

    @Override
    public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
        if ((o1 == null) || (o2 == null))
            throw new NoApplicableSimilarityFunctionException(this.getClass(), null);

        Number n1 = castToNumber(o1);
        Number n2 = castToNumber(o2);

        if ((int) n1 == Integer.MAX_VALUE && (int) n2 == Integer.MAX_VALUE)
            return 1.0;
        else if ((int) n1 == Integer.MAX_VALUE || (int) n2 == Integer.MAX_VALUE)
            return 0.0;
        else
            return super.compute(o1, o2);
    }

    private Number castToNumber(Object o)
            throws NoApplicableSimilarityFunctionException {
        try {
            return (Number) o;
        } catch (ClassCastException e) {
            throw new NoApplicableSimilarityFunctionException(this.getClass(), o.getClass());
        }
    }
}
