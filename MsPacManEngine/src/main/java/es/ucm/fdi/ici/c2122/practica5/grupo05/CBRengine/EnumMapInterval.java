package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import java.util.EnumMap;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;

public class EnumMapInterval<E extends Enum<E>> extends MyInterval {
    private final Class<E> enum1;

    public EnumMapInterval(Class<E> enum1, double interval) {
        super(interval);
        this.enum1 = enum1;
    }

    /**
     * Usa {@link MyInterval} para calcular la similitud de cada par de números de los mapas recibidos
     * como argumento y devuelve la media de dichas similitudes.
     */
    @Override
    public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
        if ((o1 == null) || (o2 == null))
            throw new NoApplicableSimilarityFunctionException(this.getClass(), null);

        EnumMap<E, Number> map1 = castToEnumMap(o1);
        EnumMap<E, Number> map2 = castToEnumMap(o2);
        double sum = 0;
        int count = 0;

        for (E e : enum1.getEnumConstants()) {
            sum += super.compute(map1.get(e), map2.get(e));
            count++;
        }

        // Si enum1 no tiene ningún valor, entonces los mapas map1 y map2 están vacíos,
        // por lo que serían iguales
        return (count == 0) ? 1.0 : sum / count;
    }

    private EnumMap<E, Number> castToEnumMap(Object o)
            throws NoApplicableSimilarityFunctionException {
        try {
            return (EnumMap<E, Number>) o;
        } catch (ClassCastException e) {
            throw new NoApplicableSimilarityFunctionException(this.getClass(), o.getClass());
        }
    }

    @Override
    public boolean isApplicable(Object o1, Object o2) {
        try {
            if ((o1 == null) && (o2 == null))
                return true;
            else if (o1 == null) {
                castToEnumMap(o2);
                return true;
            } else if (o2 == null) {
                castToEnumMap(o1);
                return true;
            } else {
                castToEnumMap(o1);
                castToEnumMap(o2);
                return true;
            }
        } catch (NoApplicableSimilarityFunctionException e) {
            return false;
        }
    }
}
