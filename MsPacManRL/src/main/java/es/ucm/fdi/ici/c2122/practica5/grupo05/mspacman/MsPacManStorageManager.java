package es.ucm.fdi.ici.c2122.practica5.grupo05.mspacman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.xmlbeans.impl.inst2xsd.RussianDollStrategy;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.ici.c2122.practica3.grupo05.utils.PathDistance;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	Vector<CBRCase> bufferEdible;
	Vector<CBRCase> bufferNotEdible;
	private Boolean mspacmanDied=false, loop=false;

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

	
	public void reviseAndRetain(CBRCase newCase)
	{			
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		boolean bad = reviseCase(bCase);
		retainCase(bCase);
		
	}
	
	private Boolean reviseCase(CBRCase bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int currentScore = game.getScore();
		int resultValue = currentScore - oldScore;
		int msDied= description.getLives() - game.getPacmanNumberOfLivesRemaining();

		MsPacManResult result = (MsPacManResult)bCase.getResult();
		MsPacManSolution sol = (MsPacManSolution)bCase.getSolution();
		MOVE m = sol.getAction();
		int chasingGhostGain = chasingGhostGoingToWin(description,m); // mientras mayor mejor para mspacman si es negativo (muy malo)
		
		
		double mspacmanIsgoingtoEat = mspacmanGoingToEat(description, m); // meintras mayor mejor
		mspacmanIsgoingtoEat = (mspacmanIsgoingtoEat>500 || mspacmanIsgoingtoEat<-500 ) ? 0 : mspacmanIsgoingtoEat;
		double encierro = posibleEncierro(description);
		double ppEficiencia = ppEficiencia(description);

		double eatPP = eatPP (description, m);
		eatPP = (eatPP>0 ) ? 0 : eatPP/3;
		eatPP = (eatPP<-500|| eatPP>500 ) ? 0 : eatPP;
		
		double eatPill = eatPill(description, m);
		eatPill=400-eatPill;
		
		double ghost = ghostTooClose(description, m);
		if(ghost>0) {
		ghost= 200 - ghost;
		ghost = (ghost<-500 ) ? 0 : ghost;
		}
		//+ chasingGhostGain + mspacmanIsgoingtoEat*5 - encierro*100 + eatPill*5+ eatPP + ghost
		resultValue = (int) ( resultValue );
		resultValue = (int) ( chasingGhostGain + mspacmanIsgoingtoEat - encierro*100 + ppEficiencia + eatPP + eatPill - ghost);
		
		if(msDied>0) {
			resultValue=-500;
			mspacmanDied=true;
		}
		else
			mspacmanDied=false;
		if(currentScore - oldScore==0)
			loop=true;
		else
			loop=false;
		result.setScore(resultValue);
		
		return mspacmanDied || resultValue<0;
	}
	
	
	
	
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//aplicar politica guardando solo casos buenos
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		StoreCasesMethod.storeCase(this.caseBase, bCase);
		

	}
	
	 
	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			boolean b = reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
	
	private int ghostTooClose(MsPacManDescription description, MOVE m){// si ghost llega antes a junction que mspacman
		
		if(m.equals(MOVE.UP))
			return description.getDistChasingUp()
				*((description.getLastMoveGhostUp().equals(MOVE.UP)) ? 0 : 1);
		else if(m.equals(MOVE.DOWN))
			return description.getDistChasingDown()*
				((description.getLastMoveGhostDown().equals(MOVE.DOWN)) ? 0 : 1);
		else if(m.equals(MOVE.LEFT))
			return description.getDistChasingLeft()*
				((description.getLastMoveGhostLeft().equals(MOVE.LEFT)) ? 0 : 1);
		else if(m.equals(MOVE.RIGHT))
			return description.getDistChasingRight()*
				((description.getLastMoveGhostRight().equals(MOVE.RIGHT)) ? 0 : 1);
		return 0;

	}
	
private int chasingGhostGoingToWin(MsPacManDescription description, MOVE m) {// si ghost llega antes a junction que mspacman
		
		if(m.equals(MOVE.UP))
			return description.getDistJunction_ChasingUp() - description.getDistJunctionUp()*
				((description.getLastMoveGhostUp().equals(MOVE.UP)) ? 0 : 1);
		else if(m.equals(MOVE.DOWN))
			return description.getDistJunction_ChasingDown() - description.getDistJunctionDown()*
				((description.getLastMoveGhostDown().equals(MOVE.DOWN)) ? 0 : 1);
		else if(m.equals(MOVE.LEFT))
			return description.getDistJunction_ChasingLeft() - description.getDistJunctionLeft()*
				((description.getLastMoveGhostLeft().equals(MOVE.LEFT)) ? 0 : 1);
		else if(m.equals(MOVE.RIGHT))
			return description.getDistJunction_ChasingRight() - description.getDistJunctionRight()*
				((description.getLastMoveGhostRight().equals(MOVE.RIGHT)) ? 0 : 1);
		return 0;

	}
	
	private double mspacmanGoingToEat(MsPacManDescription description, MOVE m) { //mientras mayor mejor
		if(m.equals(MOVE.UP))
			return description.getTimeEdibleUp() - description.getDistEdibleUp() + (150 - description.getDistEdibleUp())* 
					((description.getDistEdibleUp()>0) ? 1 : 0);
		else if(m.equals(MOVE.DOWN))
			return description.getTimeEdibleDown() - description.getDistEdibleDown() + (150 - description.getDistEdibleDown()) * 
					((description.getTimeEdibleDown()>0) ? 1 : 0);
		else if(m.equals(MOVE.LEFT))
			return description.getTimeEdibleLeft() - description.getDistEdibleLeft() + (150 - description.getDistEdibleLeft())* 
					((description.getTimeEdibleLeft()>0) ? 1 : 0);
		else if(m.equals(MOVE.RIGHT))
			return description.getTimeEdibleRight() - description.getDistEdibleRight() + (150  - description.getDistEdibleRight())
			* ((description.getTimeEdibleRight()>0) ? 1 : 0);
		return 0;

	}
	
	private double  posibleEncierro(MsPacManDescription description) { //que tan peligrosa es una direcion
		int j = 0;
		if(description.getDistJunctionUp()<=75)
			j++;
		if(description.getDistJunctionDown()<=75)
			j++;
		if(description.getDistJunctionLeft()<=75)
			j++;
		if(description.getDistJunctionRight()<=75)
			j++;
		
		int rutasSeguras=0;
		if(description.getDistChasingUp()>description.getDistJunctionUp()) //no hay ghost en ruta hacia arriba
			rutasSeguras++;
		if(description.getDistChasingDown()>description.getDistJunctionDown()) //no hay ghost en ruta hacia arriba
			rutasSeguras++;
		if(description.getDistChasingLeft()>description.getDistJunctionLeft()) //no hay ghost en ruta hacia arriba
			rutasSeguras++;
		if(description.getDistChasingRight()>description.getDistJunctionRight()) //no hay ghost en ruta hacia arriba
			rutasSeguras++;
		
		/*
		if(description.getDistChasingUp()>description.getDistJunction_ChasingUp()) //no hay ghost en ruta hacia arriba
			rutasSeguras= rutasSeguras +2;
		if(description.getDistChasingDown()>description.getDistJunctionDown()) //no hay ghost en ruta hacia arriba
			rutasSeguras= rutasSeguras +2;
		if(description.getDistChasingLeft()>description.getDistJunctionLeft()) //no hay ghost en ruta hacia arriba
			rutasSeguras= rutasSeguras +2;
		if(description.getDistChasingRight()>description.getDistJunctionRight()) //no hay ghost en ruta hacia arriba
			rutasSeguras= rutasSeguras +2;
			*/
		if(j==0)
			return 0;
		if(rutasSeguras>4)
			return (j - rutasSeguras)/j;
		return (j - rutasSeguras)/j;
	}
	
	private double ppEficiencia(MsPacManDescription description) { //mientras mayor mejor

		int tiempoQueQueda = Math.max(description.getTimeEdibleUp(), 
				Math.max(description.getTimeEdibleDown(), Math.max(description.getTimeEdibleLeft(), 
						description.getTimeEdibleRight())));
		
		if(tiempoQueQueda==0 || description.getNumEdible()==0)
			return 0;
		 
		return tiempoQueQueda/150;
	}
	
	private double eatPP (MsPacManDescription description, MOVE m) {
		if(m.equals(MOVE.UP))
			if(description.getDistPowerPillUp()<=75 && description.getDistChasingUp()<=75)
				return description.getDistChasingUp() - description.getDistPowerPillUp();
			else
				return - description.getDistChasingUp();
		else if(m.equals(MOVE.DOWN))
			if(description.getDistPowerPillDown()<=75&& description.getDistChasingDown()<=75)
				return description.getDistChasingDown() - description.getDistPowerPillDown();
			else
				return - description.getDistChasingDown();
		else if(m.equals(MOVE.LEFT))
			if(description.getDistPowerPillLeft()<=75&& description.getDistChasingLeft()<=75)
				return description.getDistChasingLeft() - description.getDistPowerPillLeft();
			else
				return - description.getDistChasingLeft();
		else if(m.equals(MOVE.RIGHT))
			if(description.getDistPowerPillUp()<=75&& description.getDistChasingRight()<=75)
				return description.getDistChasingRight() - description.getDistPowerPillRight();
			else
				return - description.getDistChasingRight();
		return 0;
	}
	
	private double eatPill (MsPacManDescription description, MOVE m) {
		if(m.equals(MOVE.UP))
			return description.getDistPillUp();
		else if(m.equals(MOVE.DOWN))
			return description.getDistPillDown();
		else if(m.equals(MOVE.LEFT))
			return description.getDistPillLeft();
		else if(m.equals(MOVE.RIGHT))
			return description.getDistPillRight();
		return 0;
	}
}
