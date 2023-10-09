package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsDescription;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsResult;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManDescription;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacManResult;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacmanCaseBaseFilter;
import es.ucm.fdi.ici.c2122.practica5.grupo06.mspacman.MsPacmanTreeCaseBase;
import es.ucm.fdi.ici.c2122.practica5.grupo06.CBRengine.Utils;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class GhostsStorageManager {
	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	// Los NN vecinos mas similares
	Collection<RetrievalResult> mostSimilarCases;
	
	private final static int TIME_WINDOW = 3;
	
	public GhostsStorageManager()
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
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase);
		
	}
	
	private void reviseCase(CBRCase bCase) {
		GhostsDescription description = (GhostsDescription)bCase.getDescription();
		// Configuramos la diferencia de puntuacion y vidas
		int scoreDif = game.getScore() - description.getScore();
		int livesDif = game.getPacmanNumberOfLivesRemaining() - description.getLives();
		
		GhostsResult result = (GhostsResult)bCase.getResult();
		result.setScore(scoreDif);
		result.setPacmanLives(livesDif);
		
		// Configuramos si han muerto los fantasmas
		GHOST[] sortedGhost = Utils.getGhostsSortedByDistanceFromPacMan(game);
		boolean estaMuerto = game.getGhostLairTime(sortedGhost[0]) > 0;
		boolean estabaMuerto = description.getCloseNode() == -1;	
		result.setClosedied((!estabaMuerto && estaMuerto) ? 1 : 0);
		
		estaMuerto = game.getGhostLairTime(sortedGhost[1]) > 0;
		estabaMuerto = description.getMidCNode() == -1;		
		result.setMidCdied((!estabaMuerto && estaMuerto) ? 1 : 0);
	
		estaMuerto = game.getGhostLairTime(sortedGhost[2]) > 0;
		estabaMuerto = description.getMidFNode() == -1;	
		result.setMidFdied((!estabaMuerto && estaMuerto) ? 1 : 0);
		
		estaMuerto = game.getGhostLairTime(sortedGhost[3]) > 0;
		estabaMuerto = description.getFarNode() == -1;
		result.setFardied((!estabaMuerto && estaMuerto) ? 1 : 0);
		
		// Configuramos el delta de cada fantasma
		int distance, index, gNode, gNode2;	
		// Close
		gNode = description.getCloseNode();
		index = Utils.getClosestGhostToPacMan(game);
		if(index != -1)
			gNode2 = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			gNode2 = -1;
		if(gNode == - 1 || gNode2 == -1 ||
				game.getGhostLairTime(GHOST.values()[index]) > 0) 
				distance = 0;
		else {
			distance = description.getCloseDistance()
				- game.getShortestPathDistance(gNode2, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.values()[index]));
			if(!game.isGhostEdible(GHOST.values()[index])) distance *= -1;
		}
		result.setClosedelta(distance);
		
		// MidC
		gNode = description.getMidCNode();
		index = Utils.getMiddlestGhostToPacMan(game);
		if(index != -1)
			gNode2 = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			gNode2 = -1;
		if(gNode == - 1 || gNode2 == -1 ||
				game.getGhostLairTime(GHOST.values()[index]) > 0)
				distance = 0;
		else {
			distance = description.getMidCDistance()
				- game.getShortestPathDistance(gNode2, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.values()[index]));
			if(!game.isGhostEdible(GHOST.values()[index])) distance *= -1;
		}
		result.setMidCdelta(distance);
		
		// MidF
		gNode = description.getMidFNode();
		index = Utils.getMiddlefarGhostToPacMan(game);
		if(index != -1)
			gNode2 = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			gNode2 = -1;
		if(gNode == - 1 || gNode2 == -1 ||
				game.getGhostLairTime(GHOST.values()[index]) > 0)
				distance = 0;
		else { 
			distance = description.getMidFDistance()
				- game.getShortestPathDistance(gNode2, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.values()[index]));
			if(!game.isGhostEdible(GHOST.values()[index])) distance *= -1;
		}
		result.setMidFdelta(distance);
		
		// Far
		gNode = description.getFarNode();
		index = Utils.getFarthestGhostToPacMan(game);
		if(index != -1)
			gNode2 = game.getGhostCurrentNodeIndex(GHOST.values()[index]);
		else
			gNode2 = -1;
		if(gNode == - 1 || gNode2 == -1 ||
				game.getGhostLairTime(GHOST.values()[index]) > 0)
				distance = 0;
		else {
			distance = description.getFarDistance()
				- game.getShortestPathDistance(gNode2, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.values()[index]));
			if(!game.isGhostEdible(GHOST.values()[index])) distance *= -1;
		}
		result.setFardelta(distance);
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		GhostsDescription des = (GhostsDescription) bCase.getDescription();
		if (des.getCloseNode() == -1 && des.getMidCNode() == -1 && 
				des.getMidFNode() == -1 && des.getFarNode() == -1) return;	// estan todos los fantasmas en la celda
		
		GhostsResult res = (GhostsResult) bCase.getResult();
		
		GhostsCaseBaseFilter filter = new GhostsCaseBaseFilter(bCase);
		if(caseBase.getCases(filter).isEmpty()) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		else {
			RetrievalResult mostSimilarCase =  mostSimilarCases.iterator().next();
			if(mostSimilarCase.getEval() <= 0.9)	// Si no es muy similar
				StoreCasesMethod.storeCase(this.caseBase, bCase);	// Si es muy similar, pero mejor
			else if(res.getFitness() > ((GhostsResult)mostSimilarCase.get_case().getResult()).getFitness()) {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
				((GhostsTreeCaseBase)caseBase).forgetCase(mostSimilarCase.get_case());;
			}
		}
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
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

