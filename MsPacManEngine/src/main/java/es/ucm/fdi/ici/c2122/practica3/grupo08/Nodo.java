package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.EnumMap;

import pacman.game.Constants.MOVE;

public class Nodo {
	
	private boolean _visited;
	private boolean _closed;
	private int _index;
	private int _level;
	private EnumMap<MOVE, Integer> _caminos = new EnumMap<MOVE, Integer>(MOVE.class);
	
	//Inicializamos el indice y todos los posibles caminos a -1 por defecto
	Nodo(int index){
		_visited = false;
		_index = index;
		_level = 0;
		_closed = false;
		
		for(MOVE mov : MOVE.values()) {
			_caminos.put(mov, -1);
		}
	}
	
	//Getter del index asociado al nodo
	public int getIndex() {
		return _index;
	}
	
	//Getter del indice de la siguiente interseccion alcanzable dado un movimiento
	public int getNextJunction(MOVE move) {
		return _caminos.get(move);
	}
	
	//Setter del indice del nodo
	public void setIndex(int index) {
		_index = index;
	}
	
	//Setter del indice del nodo al que se llega cogiendo el camino dado
	public void setPath(MOVE move, int nextIndex) {
		_caminos.put(move, nextIndex);
	}
	
	
	public void setLevel(int level) {
		_level = level;
	}
	
	public int getLevel() {
		return _level;
	}
	
	public void setVisited(boolean visited) {
		_visited = visited;
	}
	
	public boolean isVisited() {
		return _visited;
	}
	
	public void setClosed(boolean closed) {
		this._closed = closed;
	}
	
	public boolean isClosed() {
		return this._closed;
	}
}
