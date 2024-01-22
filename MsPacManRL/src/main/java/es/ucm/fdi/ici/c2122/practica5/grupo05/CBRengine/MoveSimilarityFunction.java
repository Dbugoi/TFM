package es.ucm.fdi.ici.c2122.practica5.grupo05.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;
import pacman.game.Constants.MOVE;

public class MoveSimilarityFunction implements LocalSimilarityFunction {

    @Override
    public double compute(Object caseObj, Object queryObj)
            throws NoApplicableSimilarityFunctionException {
        if (caseObj == null || queryObj == null)
            throw new NoApplicableSimilarityFunctionException(this.getClass(), null);
        if (!(caseObj instanceof MOVE))
            throw new NoApplicableSimilarityFunctionException(this.getClass(), caseObj.getClass());
        if (!(queryObj instanceof MOVE))
            throw new NoApplicableSimilarityFunctionException(this.getClass(), queryObj.getClass());

        MOVE caseMove = (MOVE) caseObj;
        MOVE queryMove = (MOVE) queryObj;

        if (caseMove == queryMove)
            return 1.0;
        else if (caseMove.opposite() == queryMove)
            return 0.0;
        else
            return 0.25;
    }

    @Override
    public boolean isApplicable(Object caseObject, Object queryObject) {
        if (caseObject == null || queryObject == null)
            return false;
        return (caseObject instanceof MOVE) && (queryObject instanceof MOVE);
    }

}
