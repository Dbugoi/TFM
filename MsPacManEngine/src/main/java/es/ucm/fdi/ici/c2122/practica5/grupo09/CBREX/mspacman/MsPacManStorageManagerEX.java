package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacManStorageManagerEX {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	
	private final int MIN_SCORE_TO_PASS = 75;
	
	//Cada 3 intersecciones se revisan los casos
	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManagerEX()
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
	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		
		reviseCase(bCase);
		if(needToRetain(bCase))
			retainCase(bCase);
		
	}
	
	private boolean needToRetain(CBRCase bCase) {
		MsPacManResultEX result = (MsPacManResultEX)bCase.getResult();
		MsPacManDescriptionEX description = (MsPacManDescriptionEX)bCase.getDescription();
				
		if(description.getLevel() < game.getCurrentLevel()) return true;
		
		if(result.getLife() > 0) return false;
		
		if(result.getScore() < MIN_SCORE_TO_PASS) return false;
		
		return true;
	}

	private void reviseCase(CBRCase bCase) {
		MsPacManDescriptionEX description = (MsPacManDescriptionEX)bCase.getDescription();

		int resultScore =  game.getScore() -  description.getScore();
		
		int resultLives = description.getLives() - game.getPacmanNumberOfLivesRemaining(); 
		
		Integer[] distances = computeGhosts();
		Integer[] auxDistances = distances.clone();
		
		sort(distances);
		
		Boolean[] edibles = computeEdibles();
				
		computeNearestEdible(edibles, distances, auxDistances);
		
		int nearestPPill = computePPill(description.getNearestPPill()); //DIFERENCIA DE DISTANCIA A LA PPILL
		
		MsPacManResultEX result = (MsPacManResultEX)bCase.getResult();
		
		
		result.setScore(resultScore);
		result.setLife(resultLives);
		result.setGhostsDist(distances);
		result.setGhostsEdible(edibles);
		result.setNearestPPillDist(nearestPPill);
	}

	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			if(needToRetain(oldCase))
				retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
	
	private Integer[] computeGhosts() {
		Integer[] distances = new Integer[4];
		for (GHOST g: GHOST.values()) {
			distances[g.ordinal()] = (int) game.getDistance(game.getGhostCurrentNodeIndex(g), 
					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), DM.PATH);
		}
		
		return distances;
	}
	
	private void computeNearestEdible(Boolean[] edibleGhost, Integer[] ghostsDist, Integer[] auxGhostsDist) {
		Boolean[] auxEdible = edibleGhost.clone();
		
		for(int i = 0; i < 4; i++) {
			int e = 0;
			while(e < 4 && ghostsDist[i] != auxGhostsDist[e]) {
				e++;
			}
			
			if(e > 3) e = 3;
			
			edibleGhost[i] = auxEdible[e];
		}
		
	}
	
	private void sort(Integer[] array) {
		int n = array.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (array[j] > array[j+1]){
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
	}
	
	private int computePPill(int ppill) {
		if(game.getNumberOfActivePowerPills() <= 0)
			return -1;
		
		for(int node: game.getActivePowerPillsIndices()) {
			if(game.getPowerPillIndex(node) == ppill) {
				return (int)game.getDistance(game.getPacmanCurrentNodeIndex(), node, DM.PATH);
			}
		}
		
		return -1;
	}

	private Boolean[] computeEdibles() {
		Boolean[] edibles = new Boolean[4];
		
		for(GHOST g: GHOST.values()) {
			edibles[g.ordinal()] = game.isGhostEdible(g);
		}
		
		return edibles;
	}
}
