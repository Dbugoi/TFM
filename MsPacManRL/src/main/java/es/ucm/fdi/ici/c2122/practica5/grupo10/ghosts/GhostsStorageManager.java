package es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts;

import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import pacman.game.Game;

public class GhostsStorageManager {
	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;

	private int number_bad_cases = 0;
	private int number_good_cases = 0;
	
	private final static int TIME_WINDOW = 8;

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

	public void reviseAndRetain(CBRCase newCase,double sim)
	{		

		this.buffer.add(newCase);

		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;

		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		retainCase(bCase, sim);

	}




	private void reviseCase(CBRCase bCase) {

		GhostsDescription description = (GhostsDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		Double resultValue = (double) 0;
		Double score_diference = (double) (currentScore - oldScore);
		int new_mspacman_deaths = 3 - game.getPacmanNumberOfLivesRemaining();


		if(description.getMsPacmanDeaths() < new_mspacman_deaths) {
			int threshold = 60;
			if(description.getEatedMsPacman())
				resultValue+=5000;
			else if(description.distance_to_mspacman_down <= threshold || description.distance_to_mspacman_left <= threshold
					|| description.distance_to_mspacman_up <= threshold || description.distance_to_mspacman_right <= threshold)
				resultValue+=2500;
				
		}

		if(description.getMsPacManEatMe()) {
			resultValue-=5000;
			//resultValue-=score_diference;
		}

		GhostsResult result = (GhostsResult)bCase.getResult();
		result.setScore(resultValue);

	}


	private void retainCase(CBRCase bCase,double similarity)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method

		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)

		GhostsResult result = (GhostsResult)bCase.getResult();
		Double value = result.getScore();

		
		if((similarity < 0.95 && value > (double) 0)) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
			number_good_cases++;
		}
		else if((similarity < 0.95 && value < (double) 0) && number_bad_cases <= 0.4*number_good_cases) {
			StoreCasesMethod.storeCase(this.caseBase, bCase);
			number_bad_cases++;
		}

	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase,0);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}
