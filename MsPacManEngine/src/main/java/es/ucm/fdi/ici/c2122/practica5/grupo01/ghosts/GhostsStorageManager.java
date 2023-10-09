package es.ucm.fdi.ici.c2122.practica5.grupo01.ghosts;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class GhostsStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	NNConfig simConfig;
	
	private final static int TIME_WINDOW = 10;
	
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
	
	public void setSimConfig(NNConfig simConfig) {
		this.simConfig = simConfig;
	}
	
	private void reviseCase(CBRCase bCase) {
		GhostsDescription description = (GhostsDescription)bCase.getDescription();

		int resultValue = 0;
		//Choose how to set these
		int lvChange = game.getCurrentLevel() - description.getLevel();
		
		resultValue -= 500*lvChange; // -500 al score por cada nivel: no queremos esto
		
		int livesChanged = description.getLives()-  game.getPacmanNumberOfLivesRemaining(); // diferencia de vidas
		
		if(livesChanged == 0) {
			if(game.getGhostLairTime(description.getGhostType()) == 0) {
				if(game.getGhostEdibleTime(description.getGhostType()) == 0) { // si el fantasma NO es comestible
					// restamos distancia actual con la que esta guardada en el description. Si distancia actual < description entonces devuelve valor negativo
					resultValue -= (50+(game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(description.getGhostType()), game.getPacmanLastMoveMade(), DM.PATH) - Double.valueOf(description.getPosPacman().split(" ")[0])));
				}else { // si el fantasma es comestible: añadimos distancia actual con la que esta guardada en el description
					resultValue += (game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(description.getGhostType()), game.getPacmanLastMoveMade(), DM.PATH) - Double.valueOf(description.getPosPacman().split(" ")[0]) - 20);
				}
			}else {
				resultValue -= 400; // si esta en la guarida ha sido comido: -400
			}
		}
		
		resultValue += 300*livesChanged; // si pacman pierde vidas entonces aumentamos score
		
		
		
		GhostsResult result = (GhostsResult)bCase.getResult();
		result.setScore(resultValue);	
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		GhostsCaseBaseFilter filtro = new GhostsCaseBaseFilter(
				((GhostsDescription) bCase.getDescription()).getLevel(),
				((GhostsDescription) bCase.getDescription()).getPosGhost()
		);
		if(this.caseBase.getCases(filtro).size() < 1000) {
			boolean stored = false;
			if(!caseBase.getCases(filtro).isEmpty()) {
				Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(filtro), bCase, simConfig);
				
				RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
				if(first.getEval() < 0.95 && (((GhostsResult)bCase.getResult()).score > 200)) { // si el mejor caso tiene buena puntuacion
					
					StoreCasesMethod.storeCase(this.caseBase, bCase); // guardamos el caso
					stored = true;
				}
			}else {
				
				if(((GhostsResult)bCase.getResult()).score > 200) {
					StoreCasesMethod.storeCase(this.caseBase, bCase);
					stored = true;
				}
			}
			if(stored) {
				for(CBRCase caso : this.buffer) {
					fixID(caso); // arreglamos caso cada vez que guardamos caso.
				}
			}
			
		}
		
	}
	
	
	/**The id of the case we are saving will be wrong unless we add one to it everytime a case gets saved 
	this is due to the fact that we don't save all the cases just the ones that are good so the id that the case 
	enters into the buffer depends on the quantity of cases we save from the ones before it in the buffer.
	
	*/
	public void fixID(CBRCase caso) {
		((GhostsDescription)caso.getDescription()).setId(
				((GhostsDescription)caso.getDescription()).getId() + 1
			);
			
		((GhostsResult)caso.getResult()).setId(
				((GhostsResult)caso.getResult()).getId() + 1
		);
			
		((GhostsSolution)caso.getSolution()).setId(
				((GhostsSolution)caso.getSolution()).getId() + 1
		);
	}

	public void close() {
		
		Vector<CBRCase> buffer2 = new Vector<CBRCase>(this.buffer);
		for(CBRCase oldCase: buffer2)
		{
			this.buffer.remove(0);
			reviseCase(oldCase);
			retainCase(oldCase);
		}
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
