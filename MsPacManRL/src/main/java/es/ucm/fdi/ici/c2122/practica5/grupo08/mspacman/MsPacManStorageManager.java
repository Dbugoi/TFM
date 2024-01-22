package es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman;

import java.util.ArrayList;
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

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setSimConfig(NNConfig simConfig) {
		this.simConfig = simConfig;
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
	
	private void reviseCase(CBRCase bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		
		int oldHps = description.getHPs();
		int currentHPs = game.getPacmanNumberOfLivesRemaining();
		
		int resultValue = currentScore - oldScore;
		Boolean hpsRemaning =  (currentHPs - oldHps) < 0;
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);	
		result.setHasDied(hpsRemaning);

		//check de resultado
		float checkPoint = resultValue/200.0f; //200 es el valor de puntos cuando se come 1 fantama
		//Como el valor va de -1 a 1 hacemos un clamp del valor para que no se exceda
		if(checkPoint > 1)
			checkPoint = 1;
		//Si MsPacman ha muerto o no ha conseguido gran puntuacion, lo valoramos como negativo
		if(result.getHasDied() || checkPoint < 0.2)
			checkPoint = -1.0f;
		
		result.setResult(checkPoint);
	}
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), bCase, simConfig);
		
		//Si no ha encontrado casos parecidos lo almacenamos en la base de casos
		if(eval.size() == 0) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
			return;
		}
		
		//cogemos el caso mas parecido al que queremos almacenar
		RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
		CBRCase mostSimilarCase = first.get_case();
		double similarity = first.getEval();
		
		if(similarity < 0.95) {
			//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		}
		else {
			MsPacManResult result = (MsPacManResult)bCase.getResult();
			MsPacManResult mostSimilarCaseResult = (MsPacManResult)mostSimilarCase.getResult();
			
			if(mostSimilarCaseResult.result < result.result) {
				ArrayList<CBRCase> casesToForget = new ArrayList<CBRCase>();
				casesToForget.add(mostSimilarCase);
				caseBase.forgetCases(casesToForget);
				
				StoreCasesMethod.storeCase(this.caseBase, bCase);
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
