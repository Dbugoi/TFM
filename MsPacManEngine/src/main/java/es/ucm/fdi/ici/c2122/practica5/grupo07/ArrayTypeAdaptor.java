package es.ucm.fdi.ici.c2122.practica5.grupo07;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

public class ArrayTypeAdaptor implements TypeAdaptor {
	ArrayList<Double> array;
	public ArrayList<Double> getArray() {return array;}
	public ArrayTypeAdaptor() {
		
	}
	public ArrayTypeAdaptor(ArrayList<Double>arr) {
		array=arr;
	}
	public String toString() {
		StringBuilder str=new StringBuilder(); 
		for(int i=0;i<array.size();i++) {
			str.append(array.get(i));
			if(i!=array.size()-1)str.append(" ");
			
		}
		return str.toString();
	}

	/**
	 * Reads the type from a string.
	 * 
	 * @param content
	 */
	public void fromString(String content) throws Exception{
		String[] a = content.split(" ");
		array= new ArrayList<Double>();
		for(String s:a) {
			array.add(Double.parseDouble(s));
		}
	}
	
	/**
	 * You must define this method to avoid problems with the data base connector (Hibernate)
	 */
	public boolean equals(Object o) {
		return true;
	}

}
