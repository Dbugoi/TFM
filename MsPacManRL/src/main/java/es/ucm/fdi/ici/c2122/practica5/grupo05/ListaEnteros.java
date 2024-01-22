package es.ucm.fdi.ici.c2122.practica5.grupo05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

public class ListaEnteros implements TypeAdaptor {
	
	List<Integer> list = new ArrayList<Integer>();
	
	@Override
	public void fromString(String s) throws Exception {
		// TODO Auto-generated method stub
		list = Arrays.asList(s.split(",")).stream()
				.map(Integer::parseInt)
				.collect(Collectors.toList());
	
	}
	public String toString() {
		return list.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
		
	}

}
