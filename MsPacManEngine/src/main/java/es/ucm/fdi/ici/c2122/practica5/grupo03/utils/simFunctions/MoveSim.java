package es.ucm.fdi.ici.c2122.practica5.grupo03.utils.simFunctions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import pacman.game.Constants.MOVE;

public class MoveSim implements LocalSimilarityFunction {
	
	static final List<MOVE> m1 = new ArrayList<MOVE>() {
		{
			add(MOVE.UP);
			add(MOVE.DOWN);
			add(MOVE.LEFT);
			add(MOVE.RIGHT);
			add(MOVE.NEUTRAL);
		}
	};
	
	static final double[] sims = {
		1,0,0.25,0.25,
		0,1,0.25,0.25,
		0.25,0.25,1,0,
		0.25,0.25,0,1,
		0.25,0.25,0.25,0.25
	};
	
	
	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		MOVE caso = (MOVE) caseObject;
		MOVE query = (MOVE) queryObject;
		
		return sims[m1.indexOf(caso) * 3 + m1.indexOf(query)];
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return true;
	}

}
