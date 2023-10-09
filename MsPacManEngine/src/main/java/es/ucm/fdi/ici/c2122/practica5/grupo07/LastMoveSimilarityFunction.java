package es.ucm.fdi.ici.c2122.practica5.grupo07;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import pacman.game.Constants.MOVE;

public class LastMoveSimilarityFunction implements LocalSimilarityFunction{

	@Override
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		MOVE casel=(MOVE)caseObject;
		MOVE queryl=(MOVE)queryObject;
		return casel.equals(queryl)?1.0:0.0;
	}

	@Override
	public boolean isApplicable(Object caseObject, Object queryObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
