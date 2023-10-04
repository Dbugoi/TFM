package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

/**
 * {@link EnumMap} que implementa la interfaz {@link TypeAdaptor}.
 */
public abstract class AdaptorEnumMap<K extends Enum<K>, V> extends EnumMap<K, V> implements TypeAdaptor {
    private static final String SEPARATOR = "@";
    
    private final Class<K> keyType;
    private final Function<String, V> stringToValueParser;

    /**
     * Construye un {@link AdaptorEnumMap} a partir de la clase {@link K}.
     * También se necesita una función para convertir el String resultante de llamar a
     * {@link #toString()} de vuelta a su valor original para valores de tipo {@link V}.
     * 
     * @param keyType
     * @param stringToValueParser
     */
    AdaptorEnumMap(Class<K> keyType, Function<String, V> stringToValueParser) {
        super(keyType);
        this.keyType = keyType;
        this.stringToValueParser = stringToValueParser;
    }

    /**
     * Cambia los valores de este mapa de acuerdo al String dado. Este tiene que tener el mismo
     * formato que generaría una llamada a {@link AdaptorEnumMap#toString()}.
     * <p>
     * <b>BUG:</b> da problemas si el string dado como argumento contiene algunos caracteres que se
     * usan para delimitar claves y valores ('=' y ',') pero no se usan para ello. Por ejemplo, si
     * para una hipotética clave "GATO" se tiene como valor "=gato", "gat,o" o "ga=to", el
     * comportamiento de este método no será el esperado.
     * 
     * @param content
     * @throws Exception
     */
    @Override
    public void fromString(String content) throws Exception {
        String[] innerContent;
        try {
            if (content.charAt(0) != '{' || content.charAt(content.length() - 1) != '}')
                throw new IllegalArgumentException("Does not start with '{' and end with '}'");

            this.clear();
            if (content.equals("{}")) // mapa vacío
                return;

            innerContent = content.substring(1, content.length() - 1).split(SEPARATOR);
            for (String keyValueString : innerContent) {
                String[] pair = keyValueString.split("=");
                K key = Enum.valueOf(keyType, pair[0]);
                V value = stringToValueParser.apply(pair[1]);
                this.put(key, value);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("String is either too small or has wrong format", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("String has wrong format", e);
        }
    }

    @Override
    public String toString() {
        List<String> str = new ArrayList<>();
        for (Map.Entry<K, V> entry : this.entrySet()) {
            str.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return String.format("{%s}", String.join(SEPARATOR, str));
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            AdaptorEnumMap<K, V> other = ((AdaptorEnumMap<K, V>) o);
            return keyType == other.keyType;
        }
        return false;
    }

}
