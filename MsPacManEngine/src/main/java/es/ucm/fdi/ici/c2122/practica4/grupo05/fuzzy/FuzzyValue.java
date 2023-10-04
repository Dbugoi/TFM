package es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy;

import java.util.Comparator;
import java.util.Objects;

/**
 * Clase que representa una variable y la confianza que tenemos de que sea un valor acertado.
 */
public class FuzzyValue<T> {
    public static final double MAX_CONFIDENCE = 100.0;
    public static final double MIN_CONFIDENCE = 0.0;
    private T value;
    private double confidence;

    /**
     * Crea un {@link FuzzyValue} dado un valor no nulo y una confianza entre 0 y 100.
     * 
     * @param value
     * @param confidence
     */
    public FuzzyValue(T value, double confidence) {
        setConfidence(confidence);
        setValue(value);
    }

    /**
     * @return Devuelve la confianza que tenemos de que este objeto tenga un valor acertado.
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * @param confidence Nueva confianza del valor guardado. Debe estar entre 0 y 100.
     */
    public void setConfidence(double confidence) {
        if (confidence < MIN_CONFIDENCE)
            this.confidence = FuzzyValue.MIN_CONFIDENCE;
        else if (confidence > MAX_CONFIDENCE)
            this.confidence = FuzzyValue.MAX_CONFIDENCE;
        this.confidence = confidence;
    }

    /**
     * @return Devuelve el valor guardado.
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value Nuevo valor no nulo.
     */
    public void setValue(T value) {
        this.value = Objects.requireNonNull(value, "Value must not be null");
    }

    public static <E extends Comparable<E>> Comparator<FuzzyValue<E>> fuzzyComparator() {
        return (a, b) -> {
            int comp = Double.compare(a.getConfidence(), b.getConfidence());
            if (comp != 0)
                return comp;
            else
                return a.getValue().compareTo(b.getValue());
        };
    }

    @Override
    public int hashCode() {
        // autogenerado
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(confidence);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // autogenerado
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FuzzyValue other = (FuzzyValue) obj;
        if (confidence != other.confidence)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }


}
