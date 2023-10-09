package es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import pacman.game.Constants.DM;
import pacman.game.Game;

/* 
 * Devuelve la similitud de dos nodos del juego, comparando la distancia entre ambos nodos,
 * Si esta distancia es mayor que un limite, se consideran completamente diferentes*/
public class GameDistance implements LocalSimilarityFunction {
	
	// To compute distances
	Game _game;
	
	// MaxDistance
	double _threshold;
	
	public GameDistance(Game game, double threshold) {
		_game = game;
		_threshold = threshold;
	}

	@Override
	public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		Number i1 = (Number) o1;
		Number i2 = (Number) o2;
		
		int v1 = i1.intValue();
		int v2 = i2.intValue();
		
		if(v1 == -1 || v2 == -1 || v1 >= _game.getNumberOfNodes() || v2 >= _game.getNumberOfNodes())
			return v1 == v2 ? 1.0 : 0.0;
		return 1 - Math.min((_game.getDistance(v1, v2, DM.PATH) / _threshold), 1.0);
	}

	@Override
	public boolean isApplicable(Object o1, Object o2) {
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof Number;
		else if(o2==null)
			return o1 instanceof Number;
		else
			return (o1 instanceof Number)&&(o2 instanceof Number);
	}
	
}
