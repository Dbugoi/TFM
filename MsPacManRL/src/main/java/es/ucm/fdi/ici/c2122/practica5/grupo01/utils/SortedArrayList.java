package es.ucm.fdi.ici.c2122.practica5.grupo01.utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class SortedArrayList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Comparator<E> _cmp;

	public SortedArrayList(Comparator<E> cmp) {
		super();
		_cmp = cmp;
	}

	public SortedArrayList() {
		_cmp = new Comparator<E>() {

			@SuppressWarnings("unchecked")
			@Override
			public int compare(E o1, E o2) {
				return ((Comparable<E>) o1).compareTo(o2);
			}
		};
	}

	@Override
	public boolean add(E e) {

		int j = size() - 1;
		
		while (j >= 0 && _cmp.compare(get(j), e) > 0) {
			j--;
		}
		
		super.add(j + 1, e);
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			add(e);
		}
		return true;
		
	}
	
	
	//Devuelve la posición del hijo que tenemos que mirar
	public int getPos(E e) {
		//Pos determina la posición del hijo que tenemos que elegir
		int pos = 0;
		//Usamos un iterador para minimizar el coste y que esté en O(k) en vez de o(k^2) 
		Iterator<E> i = this.iterator();
		E elem = i.next();
		
		//Si el elemento elem es mayor que e cogemos el siguiente hijo
		while(i.hasNext() && _cmp.compare(e, elem) > 0) {
			elem = i.next();
			pos++;
		}
		
		//Si no quedan elementos y e sigue siendo más grande que elem incrementamos uno más 
		if(_cmp.compare(e, elem) > 0) {
			pos++;
		}
		
		return pos;
	}

	//No tendremos que redefinir ninguna de estas operaciones
	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException("No se puede insertar en un índice de una lista ordenada");
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException("No se puede insertar en un índice de una lista ordenada");
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException("No se puede insertar en un índice de una lista ordenada");
	}

}
