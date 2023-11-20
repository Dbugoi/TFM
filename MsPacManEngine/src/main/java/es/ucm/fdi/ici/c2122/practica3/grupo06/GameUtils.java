package es.ucm.fdi.ici.c2122.practica3.grupo06;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.c2122.practica3.grupo06.mspacman.MsPacManInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GameUtils {
	
	// Atributos
	public final static int tileSize       = 5;
	public final int mapWidth       = tileSize * 26;
	public final int mapHeight      = tileSize * 29;
	public final int quadrantWidth  = tileSize *  mapWidth/2;
	public final int quadrantHeight = tileSize *  mapHeight/2;
	static final int pPillBanLimit  = 12;
	static final int closerLimit    = tileSize * 6; //distance from pacman to a pPill to start avoiding it.
	
	// Enum para diferenciar los cuadrantes
	public enum Quadrant{
		NorthWest(0,0),
		SouthWest(0,1),
		NorthEast(1,0),
		SouthEast(1,1);
		
		private final int x;
		private final int y;
		
		Quadrant(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public List<Integer> getCoordinates(){
			List<Integer> ret = new ArrayList<>();
			ret.add(x);
			ret.add(y);
			return ret;
		}
	}
	
	// Singleton
	private static GameUtils instance = null;
	
	public static GameUtils getInstance() {
		if(instance == null) {
			instance = new GameUtils();
		}
		return instance;
	}
	
	private GameUtils() {}
	
	
	// Metodos
	
	static final boolean DEBUG = false;
	public static void debugPrint(String text) { if (DEBUG) { System.out.print(text); } }
	
	public GHOST fromIntToGhost(Game game, int node) {
		GHOST ghost = null;
		for (GHOST aux : GHOST.values()) {
			if (game.getGhostCurrentNodeIndex(aux) == node) { ghost = aux; break; }
		}
		
		return ghost;
	}
	
	/**
    * Obtiene los movimientos válidos de pacman suponiendo una intersección tipo X y que no puede cambiar de sentido
    * NOTA: Este método se utiliza para mejorar visualmente el método getMove()
    *
    * @param game Una referencia a game
    * 
    * @return Devuelve una lista con los movimientos válidos de pacman suponiendo una intersección tipo X y que no puede cambiar de sentido.
    */
	public List<MOVE> getValidMovesBase(Game game){
		List<MOVE> validMoves = new ArrayList<>();
		for (MOVE m : MOVE.values()) {
			if (game.getNeighbour(game.getPacmanCurrentNodeIndex(), m) != -1) { validMoves.add(m); }
		}
		removeElementWithValue(validMoves,game.getPacmanLastMoveMade().opposite());
		return validMoves;
	}
	
    /**
     * Dado un valor T y una lista, elimina el elemento de dicha lista cuyo valor coincida con el parámetro
     *
     * @param list
     * @param value
     */
	public <T> void removeElementWithValue(List<T> list, T value) {
		int i = 0;
		boolean found = false;
		while (!found && i < list.size()) {
			if (list.get(i) == value) { list.remove(i); found = true; }
			i++;
		}
	}
	
	/*
	public void removeElementWithValue(MOVE[] list, MOVE value) {
		MOVE[] newList = new MOVE[list.length-1];
        for (int i = 0, j = 0; i < list.length; i++) { 
        	if (list[i] != value) { newList[j++] = list[i]; } 
        }
        
        list = newList;
	}*/
	
	
//general Getters =================================================================================
    /**
     * Comprueba si dos regiones son la misma
     *
     * @param reg_1 Primera región a evaluar (lista de enteros con dos únicos elementos)
     * @param reg_2 Segunda región a evaluar (lista de enteros con dos únicos elementos)
     * 
     * @return Devuelve el resultado de la evaluación
     */
	public boolean inSameRegion(List<Integer> reg_1, List<Integer> reg_2){ 
		return (reg_1.get(0) == reg_2.get(0) && reg_1.get(1) == reg_2.get(1));
	}
	
	
    /**
     * Dado un nodo, devuelve la región a la que pertenece (una región es una lista de dos elementos conteniendo los componentes x e y de dicha región)
     *
     * @param game Una referencia a game
     * @param node El nodo en cuestión
     * 
     * @return Devuelve la región a la que pertenece node.
     */
	public List<Integer> getRegionFromNode(Game game, int node) {
		List<Integer> ret = new ArrayList<>();
		ret.add(game.getNodeXCood(node) / quadrantWidth);
		ret.add(game.getNodeYCood(node) / quadrantHeight);
		return ret;
	}
	
	public Quadrant getQuadrantFromNode(Game game, int node) {
		List<Integer> coords = getRegionFromNode(game, node);
		for(Quadrant q: Quadrant.values()){
			if(q.x == coords.get(0) && q.y == coords.get(1))
				return q;
		}
		// aqui nunca entra, es para probar
		return Quadrant.NorthWest;
	}
	
    /**
     * Dada un array, devuelve el equivalente en formato lista.
     *
     * @param game Una referencia a game
     * @param array
     * 
     * @return Devuelve el equivalente del array en formato lista.
     */
	public <T> List<T> getListFromArray(Game game, T[] array){
		List<T> ret = new ArrayList<>();
		
		for (int i = 0; i < array.length; i++ ) { ret.add(array[i]); }
		return ret;
	}
	
	public List<Integer> getListFromArray(Game game, int[] array){
		List<Integer> ret = new ArrayList<>();
		
		for (int i = 0; i < array.length; i++ ) { ret.add(array[i]); }
		return ret;
	}
	
	
    /**
     * compactación del método game.getNextMoveTowardsTarget utilizando pacmanCurrentNode, pacmanLastMove y DM.PATH.
     * NOTA: Este método se utiliza para ahorrar repeticiones y mejorar visualmente el método getMove()
     *
     * @param game Una referencia a game
     * @param targetNode 
     * 
     * @return Devuelve game.getApproximateNextMoveTowardsTarget(pacmanNode,targetNode,pacmanLastMove,DM.PATH);
     */
	public MOVE getNextMoveTowardsTarget(Game game, int targetNode) {
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		return game.getApproximateNextMoveTowardsTarget(pacmanNode,targetNode,pacmanLastMove,DM.PATH);
	}
	
    /**
     * compactación del método game.getNextMoveAwayFromTarget utilizando pacmanCurrentNode, pacmanLastMove y DM.PATH.
     * NOTA: Este método se utiliza para ahorrar repeticiones y mejorar visualmente el método getMove()
     *
     * @param game Una referencia a game
     * @param targetNode 
     * 
     * @return Devuelve game.getApproximateNextMoveAwayFromTarget(pacmanNode,targetNode,pacmanLastMove,DM.PATH);
     */
	public MOVE getNextMoveAwayFromTarget(Game game, int targetNode) {
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		MOVE pacmanLastMove = game.getPacmanLastMoveMade();
		return game.getApproximateNextMoveAwayFromTarget(pacmanNode,targetNode,pacmanLastMove,DM.PATH);
	}
	
    /**
     * Dada una lista de nodos, devuelve una lista con solo los que están en la misma región que pacman
     * NOTA: Este método se utiliza principalmente de forma auxiliar para los métodos definidos más abajo.
     *
     * @param game Una referencia a game
     * @param nodesList lista de nodos (pueden ser los nodos actuales de los fantasmas, de las p.Pills, de las pills, etc)
     * 
     * @return Devuelve una lista con solo los que están en la misma región que pacman
     */
	public List<Integer> getInRegion(Game game, List<Integer> nodesList) {
		List<Integer> pacmanRegion = getRegionFromNode(game, game.getPacmanCurrentNodeIndex());
		List<Integer> auxRegion = new ArrayList<>();
		List<Integer> ret       = new ArrayList<>();

		for (int i = 0; i < nodesList.size(); i++) {
			auxRegion = getRegionFromNode(game, nodesList.get(i));
			if (inSameRegion(pacmanRegion,auxRegion))  { ret.add(nodesList.get(i)); }
		}
		
		return ret;
	}
	
    /**
     * Obtiene los fantasmas cuyo "nextmoveTowardsTarget" coincide con el movimiento dado.
     * Nota: Los fantasmas comestibles no se incluirán en dicha lista.
     *
     * @param game Una referencia a game
     * @param move movimiento a evaluar
     *  
     * @return Devuelve los fantasmas cuyo "nextmoveTowardsTarget" coincide con el movimiento dado.
     */
	public List<Integer> getGhostsInDirection(Game game, MOVE move) {
		List<Integer> ghostsInPp = new ArrayList<>();
		
		for (GHOST g : GHOST.values()) { //ver si hay fantasma(s) en Pp:
			int ghostNode = game.getGhostCurrentNodeIndex(g);
			MOVE aux = getNextMoveTowardsTarget(game,ghostNode);
			if (aux == move && game.getGhostLairTime(g) <= 0) { ghostsInPp.add(ghostNode); }
		}
		
		return ghostsInPp;
	}
	
	public List<Integer> getNormalGhostsInDirection(Game game, MOVE move){
		List<Integer> ghostsInPp = getGhostsInDirection(game,move);
		for (int i = 0; i < ghostsInPp.size(); i++) { 
			if (game.isGhostEdible(fromIntToGhost(game,ghostsInPp.get(i)))) { ghostsInPp.remove(i); }}
		return ghostsInPp;
	}
	
	public List<Integer> getEdibleGhostsInDirection(Game game, MOVE move){
		List<Integer> ghostsInPp = getGhostsInDirection(game,move);
		for (int i = 0; i < ghostsInPp.size(); i++) { 
			if (!game.isGhostEdible(fromIntToGhost(game,ghostsInPp.get(i)))) { ghostsInPp.remove(i); }}
		return ghostsInPp;
	}
	
	public List<Integer> getPillsInQuadrant(Game game, Quadrant q) {
		List<Integer> activePills =  getListFromArray(game, game.getActivePillsIndices());
		List<Integer> auxRegion = new ArrayList<>();
		List<Integer> quadPills = new ArrayList<>();

		for (int i = 0; i < activePills.size(); i++) {
			auxRegion = getRegionFromNode(game, activePills.get(i));
			if (inSameRegion(q.getCoordinates(),auxRegion))  { quadPills.add(activePills.get(i)); }
		}
		
		return quadPills;
	}
	
	/**
	 * 
	 */
	public int getPillsNumberInQuadrant(Game game, Quadrant q) {
		List<Integer> activePills =  getListFromArray(game, game.getActivePillsIndices());
		List<Integer> auxRegion = new ArrayList<>();
		int ret = 0;

		for (int i = 0; i < activePills.size(); i++) {
			auxRegion = getRegionFromNode(game, activePills.get(i));
			if (inSameRegion(q.getCoordinates(),auxRegion))  { ret++; }
		}
		
		return ret;
	}
	
	public int getNearestPillInQuad(Game game, int originNode, Quadrant q)   { return getNearestFromNode(game, originNode, getPillsInQuadrant(game, q)); }
	public List<Integer> getPillsInRegion(Game game) { return getInRegion(game, getListFromArray(game, game.getActivePillsIndices())); }
	


	
	
//Get nearest entities ==========================================================================================
	
    /**
     * Obtiene la entidad más cercana de una lista dada.
     *
     * @param game Una referencia a game
     * @param list lista a analizar
     *  
     * @return Devuelve la entidad más cercana de una lista dada.
     */
	public int getNearest(Game game, List<Integer> list) {
		int index = -1;
		if (list.size() > 0) {
			int pacmanNode = game.getPacmanCurrentNodeIndex();
			MOVE lastMove = game.getPacmanLastMoveMade();
			int dist = game.getShortestPathDistance(pacmanNode, list.get(0), lastMove);
	
			for (int i = 0; i < list.size(); i++) {
				int auxDist = game.getShortestPathDistance(pacmanNode,list.get(i),lastMove);
				if (dist >= auxDist) {
					dist = auxDist;
					index = list.get(i);
				}
			}
		}

		return index;
	}
	
	public int getNearestFromNode(Game game, int originNode, List<Integer> list) {
		int index = -1;
		if (list.size() > 0) {
			
			int dist = game.getShortestPathDistance(originNode, list.get(0));
			for (int i = 0; i < list.size(); i++) {
				int auxDist = game.getShortestPathDistance(originNode,list.get(i));
				if (dist >= auxDist) {
					dist = auxDist;
					index = list.get(i);
				}
			}
		}

		return index;
	}

	public List<Integer> getEdibleGhosts(Game game) {
		List<Integer> edGhosts = new ArrayList<>();
		for (GHOST g : GHOST.values()) { 
			if (game.isGhostEdible(g)) { edGhosts.add(game.getGhostCurrentNodeIndex(g)); }
		}
		
		return edGhosts;
	}
	
	/**
     * Devuelve una lista con los fantasmas no comestibles que están en un radio de acci�n peligroso
     *
     * @param game Una referencia a game
     *  
     * @return Devuelve una lista con los fantasmas que están en la misma región que pacman
     */
	public List<Integer> getNormalGhostsInRange(Game game) {
		List<Integer> ret = new ArrayList<>();
		for (GHOST ghostType : GHOST.values()) { 
			if (game.isGhostEdible(ghostType)) continue;
			if (game.getGhostLairTime(ghostType) <= 0 &&
				game.getShortestPathDistance(
					game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(ghostType)
				) <= MsPacManInput.limit) { ret.add(game.getGhostCurrentNodeIndex(ghostType)); }
		}
		return ret;
	}
	
	public int getNearestPillIndex(Game game)      { return getNearest(game, getListFromArray(game, game.getActivePillsIndices()));      					}
	public int getNearestPowerPillIndex(Game game) { return getNearest(game, getListFromArray(game, game.getActivePowerPillsIndices())); 					}
	
	public int getNearestPillInRegion(Game game)   { return getNearest(game, getInRegion(game,getListFromArray(game, game.getActivePillsIndices()))); 		}
	public int getNearestPPillInRegion(Game game)  { return getNearest(game, getInRegion(game,getListFromArray(game, game.getActivePowerPillsIndices()))); 	}
	
	public int getPPillInQuad(Game game, Quadrant pQuad) {
		
		List<Integer> pPills = getListFromArray(game, game.getPowerPillIndices());
		for(int ppill: pPills) {
			if(getQuadrantFromNode(game, ppill) == pQuad)
				return ppill;
		}			
		return 0;
	}
	public boolean getPPillInQuadEaten(Game game, Quadrant pQuad) {
		List<Integer> pPills = getListFromArray(game, game.getActivePowerPillsIndices());
		for(int ppill: pPills) {
			if(getQuadrantFromNode(game, ppill) == pQuad)
				return true;
		}			
		return false;
	} 
	
	public int getPPillBanLimit(){ return pPillBanLimit; }
	public int getCloserLimit(){ return closerLimit; }
}
