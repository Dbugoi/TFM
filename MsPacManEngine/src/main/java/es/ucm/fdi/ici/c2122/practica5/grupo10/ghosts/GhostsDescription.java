package es.ucm.fdi.ici.c2122.practica5.grupo10.ghosts;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent {

	Integer id;
	GHOST ghost;
	
	//Integer pos_mspacman;
	//Integer pos_ghost;
	
	Integer distance_to_mspacman_up;
	Integer distance_to_mspacman_down;
	Integer distance_to_mspacman_left;
	Integer distance_to_mspacman_right;
	
	Integer distance_to_ghosts_up;
	Integer distance_to_ghosts_down;
	Integer distance_to_ghosts_left;
	Integer distance_to_ghosts_right;
	
	Integer edible_time_ghost;
	Integer edible_time_ghost_2;
	Integer edible_time_ghost_3;
	Integer edible_time_ghost_4;
	
	Integer distance_nearest_ppill_mspacman;
	Integer distance_ghost_to_nearest_ppill;
	Integer Score;
	MOVE lastMovePacman;
	MOVE lastMoveGhost;
	Integer MsPacmanDeaths;
	
	Boolean MsPacManEatMe;
	Boolean eatedMsPacman;
	
	//Integer GhostDeaths;
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id",GhostsDescription.class);
	}
	
	@Override
	public String toString() {
		return "GhostDescription [id=" + id + ", ghost=" + ghost +  ", distance_to_mspacman_up=" + distance_to_mspacman_up + ", distance_to_mspacman_down=" + distance_to_mspacman_down +
				 ", distance_to_mspacman_left=" + distance_to_mspacman_left + ", distance_to_mspacman_right=" + distance_to_mspacman_right + ", distance_to_ghosts_up="+ distance_to_ghosts_up + ", distance_to_ghosts_down=" + distance_to_ghosts_down +
				  ", distance_to_ghosts_left=" + distance_to_ghosts_left + ", distance_to_ghosts_right=" + distance_to_ghosts_right + ", edible_time_ghost=" + edible_time_ghost + ", distance_nearest_ppill_mspacman="+ distance_nearest_ppill_mspacman + ", distance_ghost_to_nearest_ppill=" + distance_ghost_to_nearest_ppill +
				  ", Score=" + Score + ", lastMovePacman=" + lastMovePacman + ", lastMoveGhost=" + lastMoveGhost + ", MsPacmanDeaths="+ MsPacmanDeaths + ", MsPacManEatMe=" + MsPacManEatMe + ", eatedMsPacman=" + eatedMsPacman +"]";

	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Boolean getMsPacManEatMe() {
		return MsPacManEatMe;
	}

	public void setMsPacManEatMe(Boolean msPacManEatMe) {
		MsPacManEatMe = msPacManEatMe;
	}

	public Boolean getEatedMsPacman() {
		return eatedMsPacman;
	}

	public void setEatedMsPacman(Boolean eatedMsPacman) {
		this.eatedMsPacman = eatedMsPacman;
	}

	public GHOST getGhost() {
		return ghost;
	}

	public void setGhost(GHOST ghost) {
		this.ghost = ghost;
	}


	public Integer getDistance_to_mspacman_up() {
		return distance_to_mspacman_up;
	}

	public void setDistance_to_mspacman_up(Integer distance_to_mspacman_up) {
		this.distance_to_mspacman_up = distance_to_mspacman_up;
	}

	public Integer getDistance_to_mspacman_down() {
		return distance_to_mspacman_down;
	}

	public void setDistance_to_mspacman_down(Integer distance_to_mspacman_down) {
		this.distance_to_mspacman_down = distance_to_mspacman_down;
	}

	public Integer getDistance_to_mspacman_left() {
		return distance_to_mspacman_left;
	}

	public void setDistance_to_mspacman_left(Integer distance_to_mspacman_left) {
		this.distance_to_mspacman_left = distance_to_mspacman_left;
	}

	public Integer getDistance_to_mspacman_right() {
		return distance_to_mspacman_right;
	}

	public void setDistance_to_mspacman_right(Integer distance_to_mspacman_right) {
		this.distance_to_mspacman_right = distance_to_mspacman_right;
	}

	public Integer getDistance_to_ghosts_up() {
		return distance_to_ghosts_up;
	}

	public void setDistance_to_ghosts_up(Integer distance_to_ghosts_up) {
		this.distance_to_ghosts_up = distance_to_ghosts_up;
	}

	public Integer getDistance_to_ghosts_down() {
		return distance_to_ghosts_down;
	}

	public void setDistance_to_ghosts_down(Integer distance_to_ghosts_down) {
		this.distance_to_ghosts_down = distance_to_ghosts_down;
	}

	public Integer getDistance_to_ghosts_left() {
		return distance_to_ghosts_left;
	}

	public void setDistance_to_ghosts_left(Integer distance_to_ghosts_left) {
		this.distance_to_ghosts_left = distance_to_ghosts_left;
	}

	public Integer getDistance_to_ghosts_right() {
		return distance_to_ghosts_right;
	}

	public void setDistance_to_ghosts_right(Integer distance_to_ghosts_right) {
		this.distance_to_ghosts_right = distance_to_ghosts_right;
	}

	public Integer getEdible_time_ghost() {
		return edible_time_ghost;
	}

	public void setEdible_time_ghost(Integer edible_time_ghost) {
		this.edible_time_ghost = edible_time_ghost;
	}

	public Integer getEdible_time_ghost_2() {
		return edible_time_ghost_2;
	}

	public void setEdible_time_ghost_2(Integer edible_time_ghost_2) {
		this.edible_time_ghost_2 = edible_time_ghost_2;
	}

	public Integer getEdible_time_ghost_3() {
		return edible_time_ghost_3;
	}

	public void setEdible_time_ghost_3(Integer edible_time_ghost_3) {
		this.edible_time_ghost_3 = edible_time_ghost_3;
	}

	public Integer getEdible_time_ghost_4() {
		return edible_time_ghost_4;
	}

	public void setEdible_time_ghost_4(Integer edible_time_ghost_4) {
		this.edible_time_ghost_4 = edible_time_ghost_4;
	}

	public Integer getDistance_nearest_ppill_mspacman() {
		return distance_nearest_ppill_mspacman;
	}

	public void setDistance_nearest_ppill_mspacman(Integer distance_nearest_ppill_mspacman) {
		this.distance_nearest_ppill_mspacman = distance_nearest_ppill_mspacman;
	}

	public Integer getDistance_ghost_to_nearest_ppill() {
		return distance_ghost_to_nearest_ppill;
	}

	public void setDistance_ghost_to_nearest_ppill(Integer distance_ghost_to_nearest_ppill) {
		this.distance_ghost_to_nearest_ppill = distance_ghost_to_nearest_ppill;
	}


	public Integer getScore() {
		return Score;
	}

	public void setScore(Integer score) {
		Score = score;
	}

	public MOVE getLastMovePacman() {
		return lastMovePacman;
	}

	public void setLastMovePacman(MOVE lastMovePacman) {
		this.lastMovePacman = lastMovePacman;
	}

	public MOVE getLastMoveGhost() {
		return lastMoveGhost;
	}

	public void setLastMoveGhost(MOVE lastMoveGhost) {
		this.lastMoveGhost = lastMoveGhost;
	}

	public Integer getMsPacmanDeaths() {
		return MsPacmanDeaths;
	}

	public void setMsPacmanDeaths(Integer msPacmanDeaths) {
		MsPacmanDeaths = msPacmanDeaths;
	}

	


}
