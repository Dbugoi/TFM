package es.ucm.fdi.ici.c2122.practica5.grupo08;

import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

/**
 * Clase que implementa un vector del tipo boolean que se puede seralizar a string y deserializar de string
 */
public class BVector implements TypeAdaptor {

	public boolean [] values;
	public int size;
	
	/**
	 * Contructor
	 * @param size tamaño para inicializar el array de valores
	 */
	public BVector(int size) {
		this.size = size;
		values = new boolean[size];
	}
	
	/**
	 * Constructora por defecto
	 */
	public BVector() {
	}

	/**
	 * Deserializa el objeto desde un string
	 */
	@Override
	public void fromString(String content) throws Exception {

		//Los componentes estan separados por As
		String[] splited = content.split("A");
		size = splited.length; //Inicializamos el size
		
		values = new boolean[size];
		
		//Inicializamos cada valor
		for(int i=0; i<size; i++)
			this.values[i] = Boolean.parseBoolean(splited[i]);
	}

	/**
	 * Serializa el objeto a un string
	 */
	@Override
	public String toString() {

		String aux = "";
		
		//concatenamos cada caracter seguido del separador si no es el ultimo
		for(int i=0; i<size; i++) {
			aux+= Boolean.toString(this.values[i]);
			if(i<size-1) aux +=  "A"; //Cada componente se separa con una A
		}
			
		
		
		return aux;
	}
	
}
