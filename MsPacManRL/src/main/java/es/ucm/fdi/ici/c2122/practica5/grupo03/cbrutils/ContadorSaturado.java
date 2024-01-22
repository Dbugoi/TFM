package es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils;


public class ContadorSaturado {
	
	private final static int INIT = 2;
	
	private static final int[] numCasos = {0,1,2,3,4};
	
	int count;
	double m1;
	double m2;
	
	
	public ContadorSaturado() {
		count = INIT;
	}
	
	public void up() {
		if(count < 4) {
			count++;
		}
	}
	
	public void down() {
		if(count > 0) {
			count--;
		}
	}
	
	public Pair<Pair<BasedeCasos,Integer>, Pair<BasedeCasos,Integer>> getcount() {
		Pair<BasedeCasos,Integer> p1 = new Pair<BasedeCasos,Integer>(BasedeCasos.GENERAL, numCasos[count]);
		Pair<BasedeCasos,Integer> p2 = new Pair<BasedeCasos,Integer>(BasedeCasos.ESPECIFICA, 4 - numCasos[count]);
		return new Pair<Pair<BasedeCasos,Integer>, Pair<BasedeCasos,Integer>>(p1,p2);
	}
	
}
