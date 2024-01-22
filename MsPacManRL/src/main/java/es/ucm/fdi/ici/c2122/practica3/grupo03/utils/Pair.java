package es.ucm.fdi.ici.c2122.practica3.grupo03.utils;

public class Pair <T,Z> {
	
	private T first;
	private Z second;
	
	public Pair(T f, Z s) {
		first = f;
		second = s;
		
	}
	
	public T getFirst() {
		return first;
	}
	
	public Z getSecond() {
		return second;
	}
	
	public void setFirst(T t) {
		first = t;
	}
	
	public void setSecond(Z z) {
		second = z;
	}
	
}
