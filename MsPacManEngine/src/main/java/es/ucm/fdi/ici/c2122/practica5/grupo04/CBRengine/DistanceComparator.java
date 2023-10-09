package es.ucm.fdi.ici.c2122.practica5.grupo04.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import pacman.game.Game;

public class DistanceComparator implements LocalSimilarityFunction {

	double _maxDistance;
	Game _game;
	
	public DistanceComparator(Game game, double maxDistance) {
		_maxDistance = maxDistance;
		_game = game;
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
		return 1 - (_game.getShortestPathDistance(v1, v2) / _maxDistance);
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
