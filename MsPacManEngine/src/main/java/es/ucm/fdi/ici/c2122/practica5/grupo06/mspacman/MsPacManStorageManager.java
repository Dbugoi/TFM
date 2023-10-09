package es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.*;
import pacman.game.Constants.*;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	
	// Los NN vecinos mas similares
	Collection<RetrievalResult> mostSimilarCases;

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void setSimilarCases(Collection<RetrievalResult> cases) {
		mostSimilarCases = cases;
	}
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size() < TIME_WINDOW)
			return;
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase);
		
	}
	
	
	private void reviseCase(CBRCase bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		
		int scoreDif = game.getScore() - description.getScore();
		int livesDif = game.getPacmanNumberOfLivesRemaining() - description.getLives();
		// considerar las distancias y el estado de los fantasmas
		
		result.setScore(scoreDif);
		result.setLives(livesDif);
		
		int delta, descNode, index, nowNode;	
		
		// CLOSEST
		descNode = description.getCloseNode();
		index = Utils.getClosestGhostToPacMan(game);
		if(index != -1)
			nowNode = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			nowNode = -1;
		if(descNode == - 1 || nowNode == -1 || game.getGhostLairTime(GHOST.values()[index]) > 0) delta = 0;
		else {
			delta = (int)(description.getCloseDistance() - game.getDistance(game.getPacmanCurrentNodeIndex(), nowNode, DM.PATH));
			if(game.isGhostEdible(GHOST.values()[index])) delta *= -1;
		}
		result.setCloseDelta(delta);
		
		// MIDDLEST
		descNode = description.getMidNode();
		index = Utils.getMiddlestGhostToPacMan(game);
		if(index != -1)
			nowNode = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			nowNode = -1;
		if(descNode == - 1 || nowNode == -1 || game.getGhostLairTime(GHOST.values()[index]) > 0) delta = 0;
		else {
			delta = (int)(description.getMidDistance() - game.getDistance(game.getPacmanCurrentNodeIndex(), nowNode, DM.PATH));
			if(game.isGhostEdible(GHOST.values()[index])) delta *= -1;
		}
		result.setMidDelta(delta);
		
		// FARTHEST
		descNode = description.getFarNode();
		index = Utils.getFarthestGhostToPacMan(game);
		if(index != -1)
			nowNode = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			nowNode = -1;
		if(descNode == - 1 || nowNode == -1 || game.getGhostLairTime(GHOST.values()[index]) > 0) delta = 0;
		else {
			delta = (int)(description.getFarDistance() - game.getDistance(game.getPacmanCurrentNodeIndex(), nowNode, DM.PATH));
			if(game.isGhostEdible(GHOST.values()[index])) delta *= -1;
		}
		result.setFarDelta(delta);
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		MsPacManResult res = (MsPacManResult) bCase.getResult();
		
		MsPacmanCaseBaseFilter filter = new MsPacmanCaseBaseFilter(bCase);
		if(caseBase.getCases(filter).isEmpty()) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		else {
			RetrievalResult mostSimilarCase = mostSimilarCases.iterator().next();
			if(mostSimilarCase.getEval() <= 0.9)	// Si no es muy similar
				StoreCasesMethod.storeCase(this.caseBase, bCase);	// Si es muy similar, pero mejor
			else if(res.getFitness() > ((MsPacManResult)mostSimilarCase.get_case().getResult()).getFitness()) {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
				((MsPacmanTreeCaseBase)caseBase).forgetCase(mostSimilarCase.get_case());;
			}
		}
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
