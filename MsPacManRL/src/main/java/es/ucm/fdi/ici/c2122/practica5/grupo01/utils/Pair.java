package es.ucm.fdi.ici.c2122.practica5.grupo01.utils;

public class Pair<Z> implements Comparable<Pair<Z>>{
	public Double a;
	public Z b;
	
	public Pair(Double a, Z b){
		this.a = a;
		this.b = b;
	}

	@Override
	public int compareTo(Pair<Z> other) {
		return this.a.compareTo(other.a);
	
	}
}
