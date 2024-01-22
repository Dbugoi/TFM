package es.ucm.fdi.ici.c2122.practica5.grupo08;

import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

/**
 * Clase que implementa un vector del tipo float que se puede seralizar a string y deserializar de string
 */
public class FVector implements TypeAdaptor {

	public float [] values;
	public int size;
	
	/**
	 * Contructor
	 * @param size tamaño para inicializar el array de valores
	 */
	public FVector(int size) {
		this.size = size;
		values = new float[size];
	}
	
	/**
	 * Constructora por defecto
	 */
	public FVector() {
	}

	/**
	 * Deserializa el objeto desde un string
	 */
	@Override
	public void fromString(String content) throws Exception {

		//Los componentes estan separados por As
		String[] splited = content.split("A");
		size = splited.length;//Inicializamos el size
		
		values = new float[size];
		
		//Inicializamos cada valor
		for(int i=0; i<size; i++)
			this.values[i] = Float.parseFloat(splited[i]);
	}

	/**
	 * Serializa el objeto a un string
	 */
	@Override
	public String toString() {

		String aux = "";
		
		//concatenamos cada caracter seguido del separador si no es el ultimo
		for(int i=0; i<size; i++) {
			aux+= Float.toString(this.values[i]);
			if(i<size-1) aux +=  "A";//Cada componente se separa con una A
		}
		
		return aux;
	}
	
	@Override
	public boolean equals(Object o){
		return this == o;
	}
	
}
