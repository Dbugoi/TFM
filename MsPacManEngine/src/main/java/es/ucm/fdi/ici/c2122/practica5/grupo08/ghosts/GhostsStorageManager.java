package es.ucm.fdi.ici.c2122.practica5.grupo08.ghosts;

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
import es.ucm.fdi.ici.c2122.practica5.grupo08.FVector;
import es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman.MsPacManDescription;
import es.ucm.fdi.ici.c2122.practica5.grupo08.mspacman.MsPacManResult;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	
	NNConfig simConfig;

	private final static int TIME_WINDOW = 3;
	
	/**
	 * Constructora de la clase
	 */
	public GhostsStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	//----------------SETTERS-------------------------
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
	//-------------------------------------------------
	/**
	 * Si ha pasado el tiempo de tamanio de la ventana suficiente
	 * revisa el caso dado.
	 * @param newCase
	 */
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
	/**
	 * Revisa el caso dado. Para ello mira las puntuaciones obtenidas en el 
	 * paso de los ticks y si mspacman o algún ghost ha muerto en el proceso.
	 * @param bCase
	 */
	private void reviseCase(CBRCase bCase) {
		
		GhostsDescription description = (GhostsDescription)bCase.getDescription();
		//Puntuacion
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		//Vida
		int oldHps = description.getHPs();
		int currentHPs = game.getPacmanNumberOfLivesRemaining();
		//Resta de puntuacion
		int resultValue = currentScore - oldScore;
		Boolean hpsRemaning =  (currentHPs - oldHps) < 0;
		
		GhostsResult result = (GhostsResult)bCase.getResult();
		result.setScore(resultValue);	
		result.setHasDied(hpsRemaning);

		//check de resultado
		float checkPoint = 1-(resultValue/200.0f); //200 es el valor de puntos cuando se come 1 fantama
		if(checkPoint > 0.8)	//Si ha obtenido muchos puntos de golpe asignamos un resultado muy negativo
			checkPoint = -1;
		if(result.getHasDied())	//Si mspacmana ha muerto en los ticks asignamos un resultado muy positivo
			checkPoint = 1.0f;
		
		FVector resultados = new FVector(4);
		//Para cada fantasma asignamos el valor del resultado en caso de que no haya muerto. Si el fantasma ha muerto en el 
		//transcuso de los ticks asignamos un resultado negativo para ese fantasma
		for(int i = 0; i < 4; i++){
			if(game.getGhostLairTime(GHOST.values()[i]) > 0 && description.actives.values[i])
				resultados.values[i] = -1;
			else
				resultados.values[i] = checkPoint;
		}
		
		result.setResult(resultados);
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
		
		if(similarity < 0.80) {
			//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
			StoreCasesMethod.storeCase(this.caseBase, bCase);
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
