package es.ucm.fdi.ici.c2122.practica3.grupo08;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends es.ucm.fdi.ici.rules.RulesInput {

	private double dist = 55.;	//distancia para comprobar
	
	private double _distanceToMsPacMan = 60;
	
	private ArrayList<GHOST> _nearChasingGhosts;
	
	private ArrayList<MOVE> _possibleMoves;
	
	private boolean _isAnyGhostToEat;
	
	private boolean _isAnyGhostToEatWhileFlee;
	
	private boolean _canReachPPillBeforeghosts;
	
	private boolean _isAnyGroupOfOfEdibleGhost;
	
	
	private boolean _isAnyNearChasingGhost;
	
	private boolean _isAnyPossibleMove;
	
	public MsPacManInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		dist = 55;
		_distanceToMsPacMan = 60;
		checkNearChasingGhost();
		checkPossibleMoves();
		checkGhost();
		isAnyGhostToEat();
		checkGhostToEatWhileFlee();
		checkPills();
		this._isAnyNearChasingGhost = !this._nearChasingGhosts.isEmpty();
		this._isAnyPossibleMove = !this._possibleMoves.isEmpty();		
	}
	
	public void isAnyGhostToEat() {
		GHOST nearestGhost = getNearestGhost(game, true);
		this._isAnyGhostToEat = (nearestGhost != null && game.getGhostEdibleTime(nearestGhost) >= 0.5);
	}
	
	//Comprueba si existen powerpills a las que moverse y llegar antes que cualquier otro fantasma
	private void checkPills() {	
		int[] activePowerPills = game.getActivePowerPillsIndices();
		int minDistance = 999999;
		int closestPowerPill = -1;
		//Miramos las powerpills activas y cercanas
		for(int powerPill : activePowerPills) {
			int powerPillDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), powerPill, game.getPacmanLastMoveMade());
			if(powerPillDistance < minDistance) {
				minDistance = powerPillDistance;
				closestPowerPill = powerPill;
			}
		}
		//En caso de haber encontrado una power pill cercana miramos si llegamos antes que un fantasma
		if(closestPowerPill != -1) {
			this._canReachPPillBeforeghosts = true;
			for (GHOST ghostType : GHOST.values()) {
				int ghostDistanceToPill = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), 
						closestPowerPill, 
						game.getGhostLastMoveMade(ghostType));
				int pacmanDistanceToPill = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), 
						closestPowerPill, 
						game.getPacmanLastMoveMade());
				
				//Si la PP no es alcanzable antes que un fantasma, MsPacman huye de ellos
				if(ghostDistanceToPill <= pacmanDistanceToPill) this._canReachPPillBeforeghosts = false;
			}
		}
		else this._canReachPPillBeforeghosts = false;
	}
	
	//Devuelve una lista de los fantasmas que se encuentran cerca de MsPacman y son comibles
	private void getAllNearEdibleGhosts(Game game, ArrayList<GHOST> ghostList) {
		for (GHOST ghostType : GHOST.values()) {
			//Si el fantasma esta activo y no es comestible
			if(game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
				//Se calcula su distancia con MsPacman
				double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				//Si la distancia se considera peligrosa segun el limite
				if(ghostDistance <= dist) {
					ghostList.add(ghostType);
				}
			}
		}
	}
	
	//Añade los fantasmas cercanos que puedes comer a MsPacMan
	private void checkNearChasingGhost() {
		this._nearChasingGhosts = new ArrayList<GHOST>();
		for (GHOST ghostType : GHOST.values()) {
			//Si el fantasma esta activo y no es comestible
			if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
				//Se calcula su distancia con MsPacman
				double ghostDistance = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				//Si la distancia se considera peligrosa segun el limite
				if(ghostDistance <= dist) {
					this._nearChasingGhosts.add(ghostType);
				}
			}
		}
	}
	
	//Comprueba dado los fantasmas que pueden comer a MsPacMan los mejores posibles movimientos 
	//que no le lleve a ningún fantama
	private void checkPossibleMoves() {
		
		this._possibleMoves = new ArrayList<MOVE>();
		//Se recogen los posibles movimientos que puede hacer MsPacMan
		for(MOVE possibleM : game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade())) {
			this._possibleMoves.add(possibleM);
		}
		
		//Se quitan de los posibles movimientos aquellos que conducen a un fantasma cercano
		for(GHOST ghost : this._nearChasingGhosts) {
			if(game.isGhostEdible(ghost)) continue;
			int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost));
			if(path.length < 55) {
				//GameView.addPoints(game, Color.CYAN, path);
				this._possibleMoves.remove(game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH));
			}
		}
	}//checkPossibleMoves
	
	//Devuelve el fantasma más cercano. Este será comible o no dependiendo del valor entrante
	private GHOST getNearestGhost(Game game, boolean edible) {
		GHOST nearestGhost = null;
		int minDistance = 9999999;
		
		//Se recorren los fantasmas
		for (GHOST ghostType : GHOST.values()) {
			//Si el fantasma es comestible o no (segun el parametro) y esta activo
			if(game.isGhostEdible(ghostType) == edible && game.getGhostLairTime(ghostType) <= 0) {
				//Se calcula su distancia con MsPacman
				int actualDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade());
				//Si es el mas cercano se guarda
				if(actualDistance < minDistance) {
					nearestGhost = ghostType;
					minDistance = actualDistance;
				}
			}
		}
		return nearestGhost;
	}
	
	//Comprueba si existe algún fantasma comestible dentro de los posibles movimientos de MsPacMan
	private void checkGhostToEatWhileFlee() {
		//Vemos si hay algún camino que lleva a algun fantasma-----------------------
		double shortestDistance = 999999.;
		this._isAnyGhostToEatWhileFlee = false;
		ArrayList<GHOST> nearEdibleGhosts = new ArrayList<GHOST>();
		getAllNearEdibleGhosts(game, nearEdibleGhosts);
		//Se recorren los fantasmas cercanos
		for(GHOST ghost : nearEdibleGhosts) {
			//Buscamos dentro de nuestros posibles movimientos el que nos lleve al fantasma mas cercana
			int[] path = game.getShortestPath(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade());
			if(path.length < shortestDistance) {
				MOVE moveToNeighbour = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path[path.length - 1], DM.PATH);
				if(_possibleMoves.contains(moveToNeighbour)) {
					shortestDistance = dist;
					this._isAnyGhostToEatWhileFlee = true;
				}					
			}
		}	
	} // checkGhostToEatWhileFlee
	
	//Mira si se puede perseguir a los fantasmas en grupo
	private void checkGhost() {
		//------------------------Check para perseguir a los fantamas en grupo---------------------------------
		
		ArrayList<GHOST>[] numberGhost = new ArrayList[4];
		
		//Miramos primero si hay más de un fantamas
		int edibleGhost = 0;
		for(GHOST ghostType : GHOST.values()) {
			if(game.getGhostLairTime(ghostType) <= 0 && game.isGhostEdible(ghostType)) 
				edibleGhost++;
		}	
		if(edibleGhost > 1) {
			//Mirar si hay algun grupo de fantamas al que comer.
			//Para ello vemos para cada fantama a que distancia esta del resto de fantasmas.
			//Dependiendo de si se encuentra cerca o lejos habrá o no grupo.
			for(GHOST ghostType : GHOST.values()) {
				numberGhost[ghostType.ordinal()] = new ArrayList<GHOST>();
				if(game.getGhostLairTime(ghostType) <= 0 && game.isGhostEdible(ghostType)) { //Si el fantasma es comestible
					int[] path  = game.getShortestPath(game.getPacmanCurrentNodeIndex(), 
							game.getGhostCurrentNodeIndex(ghostType), game.getPacmanLastMoveMade());
					MOVE move = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), path[path.length -1], 
							game.getPacmanLastMoveMade(), DM.PATH);
					//Vemos si el ghost se encuentra relativamente cerca de msPacman
					if(path.length < _distanceToMsPacMan) 
					{
						for(GHOST otherGhost : GHOST.values()) {
							if(otherGhost != ghostType) {
								double distanceToOtherGhost = game.getDistance(game.getGhostCurrentNodeIndex(ghostType)
										,game.getGhostCurrentNodeIndex(otherGhost), DM.EUCLID);
								if(distanceToOtherGhost < 15) {
									numberGhost[ghostType.ordinal()].add(otherGhost);
								}
							}
						}			
					}			
				}
			}
			//Miramos si existe algún grupo
			int max = 0;
			boolean isGroup = false;
			for(GHOST ghostType : GHOST.values()) {
				int actualGroup = numberGhost[ghostType.ordinal()].size();
				if(actualGroup > max) {
					isGroup = true;
					max = actualGroup;
				}			
			}
			_isAnyGroupOfOfEdibleGhost = isGroup;
		}
		else _isAnyGroupOfOfEdibleGhost = false;
		
	}//checkGhost
	
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add(String.format("(PACMAN (isAnyGhostToEat %s)"
				+ " (isAnyGhostToEatWhileFlee %s)"
				+ " (canReachPPillBeforeghosts %s)"
				+ " (isAnyGroupOfOfEdibleGhost %s)"
				+ " (isAnyNearChasingGhost %s)"
				+ " (isAnyPossibleMove %s))",
				this._isAnyGhostToEat,
				this._isAnyGhostToEatWhileFlee,
				this._canReachPPillBeforeghosts,
				this._isAnyGroupOfOfEdibleGhost,
				this._isAnyNearChasingGhost,
				this._isAnyPossibleMove));
		
		return facts;
	}
}
