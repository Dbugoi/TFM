package es.ucm.fdi.ici.c2122.practica5.grupo03.cbrutils;

import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;
import pacman.game.Constants.MOVE;

public class CbrVector implements TypeAdaptor {

	Object[] vector;
	
	public CbrVector(){
		this.vector = null;
	}
	
	public CbrVector(Object[] vector){
		this.vector = vector;
	}
	
	public Object get(int pos){
		return vector[pos];
	}
	

	public Object[] getVector() {
		return vector;
	}


	public String toString(){
		String s = "[";
		for(Object i: vector) {
			s = s + i + ";";
		}
		
		s = s.substring(0, s.length() - 1);
		
		s = s + "]";
		
		return s;
	}
	
	@Override
	public void fromString(String content) throws Exception {
		
		String[] arry = content.substring(1, content.length() - 1).split(";");
		
		Integer[] vectorI = new Integer[4];
		MOVE[] vectorM = new MOVE[4];
		
		int i = 0;
		
		for(String s: arry) {
			
			switch (s) {
				case "UP":
					vectorM[i] = MOVE.UP;
					break;
					
				case "DOWN":
					vectorM[i] = MOVE.DOWN;
					break;
					
				case "LEFT":
					vectorM[i] = MOVE.LEFT;
					break;
					
				case "RIGHT":
					vectorM[i] = MOVE.RIGHT;
					break;
					
				case "NEUTRAL":
					vectorM[i] = MOVE.NEUTRAL;
					break;
					
				default:
					vectorI[i] = Integer.valueOf(s);
					break;
			}
			i++;
		}
		
		if(vectorI[0] != null) {
			vector = vectorI;
		}
		else {
			vector = vectorM;
		}
		
	}

}
