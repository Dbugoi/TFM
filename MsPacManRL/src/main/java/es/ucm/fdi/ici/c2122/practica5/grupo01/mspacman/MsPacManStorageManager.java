package es.ucm.fdi.ici.c2122.practica5.grupo01.mspacman;

import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	NNConfig simConfig;

	private final static int TIME_WINDOW = 5;
	
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
	
	public void setSimConfig(NNConfig simConfig) {
		this.simConfig = simConfig;
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
		// Revision: la solucion es correcta?
		
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = currentScore - oldScore; // Diferencia del score
		
		//Choose how to set these
		int lvChange = game.getCurrentLevel() - description.getLevel();
		
		resultValue += 500*lvChange; // +500 al score por cada nivel más (muy importante)
		
		int livesChanged = description.getLives()- game.getPacmanNumberOfLivesRemaining();
		
		resultValue += -(livesChanged)*200; // -200 al score por cada vida perdida
		
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);	// Score basado en la puntuacion del juego, niveles avanzados y vidas perdidas.
	}
	
	private void retainCase(CBRCase bCase)
	{
		
		//Filtro para determinar donde guardamos el caso
		MsPacManCaseBaseFilter filtro = new MsPacManCaseBaseFilter(
				((MsPacManDescription) bCase.getDescription()).getLevel(),
				((MsPacManDescription) bCase.getDescription()).getPosPacman()
			);
		
		//Si no hemos llegado al límite por junction
		if(this.caseBase.getCases(filtro).size() < 2000) {
			boolean stored = false;
			//Revisamos que no existe un caso muy similar en la case de casos en cuyo caso no lo guardamos
			if(!caseBase.getCases(filtro).isEmpty()) {
				Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(filtro), bCase, simConfig);
				
				RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
				// Si el caso parece favorable, lo guardamos.
				if(first.getEval() < 0.95 && (((MsPacManResult)bCase.getResult()).score > 120)) {
					StoreCasesMethod.storeCase(this.caseBase, bCase);
					stored = true;
				}
				
			
			}else { //Si no hay casos para una junction lo guardamos
				
				if(((MsPacManResult)bCase.getResult()).score > 120) {
					StoreCasesMethod.storeCase(this.caseBase, bCase);
					stored = true;
				}
			}
			if(stored) {
				for(CBRCase caso : this.buffer) {
					fixID(caso); // cada vez que guardamos, llamar fixID
				}
			}
		}
		
		
		
	}
	
	/**The id of the case we are saving will be wrong unless we add one to it everytime a case gets saved 
	this is due to the fact that we don't save all the cases just the ones that are good so the id that the case 
	enters into the buffer depends on the quantity of cases we save from the ones before it in the buffer.
	
	*/
	public void fixID(CBRCase caso) {
		((MsPacManDescription)caso.getDescription()).setId(
				((MsPacManDescription)caso.getDescription()).getId() + 1
			);
			
		((MsPacManResult)caso.getResult()).setId(
				((MsPacManResult)caso.getResult()).getId() + 1
		);
			
		((MsPacManSolution)caso.getSolution()).setId(
				((MsPacManSolution)caso.getSolution()).getId() + 1
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
		//this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
