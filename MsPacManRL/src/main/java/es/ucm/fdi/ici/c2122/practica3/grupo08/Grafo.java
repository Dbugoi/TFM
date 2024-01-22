package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;

public class Grafo {
	
	private ArrayList<Nodo> _grafo;
	
	Grafo(int tamGrafo){
		_grafo = new ArrayList<Nodo>(tamGrafo);
		
		//Inicializar por defecto los nodos del grafo
		for(int i = 0; i < tamGrafo; ++i) {
			_grafo.add(new Nodo(-1));
		}
	}
	
	public ArrayList<Nodo> getGraph() {
		return _grafo;
	}
	
	public void addNode(Nodo node) {
		_grafo.add(node);
	}
	
	public Nodo getNode(int i) {
		return _grafo.get(i);
	}
	
	public Nodo getJunction(int junctionIndex) {
		for(Nodo node : _grafo) {
			if(node.getIndex() == junctionIndex)
				return node;
		}
		return null;
	}
	
	public int getSize() {
		return _grafo.size();
	}
	
	public boolean contains(Nodo node) {
		return _grafo.contains(node);
	}
}
