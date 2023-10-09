package es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo06.ghosts.GhostsDescription;
import pacman.game.Constants.MOVE;

public class GhostsDescription implements CaseComponent{
	Integer id;	
	
	Integer score;

	Integer PacManNode;
	MOVE PacManLastMove;
	Integer PacManToPPill;
	
	Integer CloseNode;	// nodo del fantasma
	Integer CloseDistance;	// distancia a pacman
	Boolean Closeedible;	// estado del fantasma
	MOVE CloseMove;	// ultimo movimiento del fantasma
	Integer CloseDistanceToGhost;	// distancia al fantasma mas cercano

	Integer MidCNode;
	Integer MidCDistance;
	Boolean MidCedible;
	MOVE MidCMove;
	Integer MidCDistanceToGhost;
	
	Integer MidFNode;
	Integer MidFDistance;
	Boolean MidFedible;
	MOVE MidFMove;
	Integer MidFDistanceToGhost;

	Integer FarNode;
	Integer FarDistance;
	Boolean Faredible;
	MOVE FarMove;
	Integer FarDistanceToGhost;
	
	Integer nearestPPill;
	Integer nearestPill;
	
	Integer lives;
	Integer level;
	Integer time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	
	public Integer getCloseDistance() {
		return CloseDistance;
	}

	public void setCloseDistance(Integer closeDistance) {
		CloseDistance = closeDistance;
	}

	public Integer getMidCDistance() {
		return MidCDistance;
	}

	public void setMidCDistance(Integer midcDistance) {
		MidCDistance = midcDistance;
	}

	public Integer getMidFDistance() {
		return MidFDistance;
	}

	public void setMidFDistance(Integer midfDistance) {
		MidFDistance = midfDistance;
	}

	public Integer getFarDistance() {
		return FarDistance;
	}

	public void setFarDistance(Integer farDistance) {
		FarDistance = farDistance;
	}

	public Boolean getCloseedible() {
		return Closeedible;
	}

	public void setCloseedible(Boolean closeedible) {
		Closeedible = closeedible;
	}

	public Boolean getMidCedible() {
		return MidCedible;
	}

	public void setMidCedible(Boolean midcedible) {
		MidCedible = midcedible;
	}

	public Boolean getMidFedible() {
		return MidFedible;
	}

	public void setMidFedible(Boolean midfedible) {
		MidFedible = midfedible;
	}

	public Boolean getFaredible() {
		return Faredible;
	}

	public void setFaredible(Boolean faredible) {
		Faredible = faredible;
	}

	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}
	
	public Integer getNearestPill() {
		return nearestPill;
	}
	
	public void setNearestPill(Integer nearestPill) {
		this.nearestPill = nearestPill;
	}

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}
	

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public MOVE getLastMove() {
		return PacManLastMove;
	}

	public void setPacManLastMove(MOVE lastMove) {
		this.PacManLastMove = lastMove;
	}
	
	public Integer getPacManToPPill() {
		return PacManToPPill;
	}

	public void setPacManToPPill(Integer pacManToPPill) {
		PacManToPPill = pacManToPPill;
	}
	
	public Integer getCloseNode() {
		return CloseNode;
	}

	public void setCloseNode(Integer closeNode) {
		CloseNode = closeNode;
	}

	public Integer getMidCNode() {
		return MidCNode;
	}

	public void setMidCNode(Integer midcNode) {
		MidCNode = midcNode;
	}

	public Integer getMidFNode() {
		return MidFNode;
	}

	public void setMidFNode(Integer midfNode) {
		MidFNode = midfNode;
	}

	public Integer getFarNode() {
		return FarNode;
	}

	public void setFarNode(Integer farNode) {
		FarNode = farNode;
	}

	public Integer getPacManNode() {
		return PacManNode;
	}

	public void setPacManNode(Integer pacManNode) {
		PacManNode = pacManNode;
	}

	public MOVE getPacManLastMove() {
		return PacManLastMove;
	}

	public MOVE getCloseMove() {
		return CloseMove;
	}

	public void setCloseMove(MOVE closeMove) {
		CloseMove = closeMove;
	}

	public MOVE getMidCMove() {
		return MidCMove;
	}

	public void setMidCMove(MOVE midcMove) {
		MidCMove = midcMove;
	}

	public MOVE getMidFMove() {
		return MidFMove;
	}

	public void setMidFMove(MOVE midfMove) {
		MidFMove = midfMove;
	}

	public MOVE getFarMove() {
		return FarMove;
	}

	public void setFarMove(MOVE farMove) {
		FarMove = farMove;
	}
	
	public Integer getCloseDistanceToGhost() {
		return CloseDistanceToGhost;
	}

	public void setCloseDistanceToGhost(Integer closeDistanceToGhost) {
		CloseDistanceToGhost = closeDistanceToGhost;
	}
	
	public Integer getMidCDistanceToGhost() {
		return MidCDistanceToGhost;
	}

	public void setMidCDistanceToGhost(Integer midcDistanceToGhost) {
		MidCDistanceToGhost = midcDistanceToGhost;
	}
	
	public Integer getMidFDistanceToGhost() {
		return MidFDistanceToGhost;
	}

	public void setMidFDistanceToGhost(Integer midfDistanceToGhost) {
		MidFDistanceToGhost = midfDistanceToGhost;
	}
	
	public Integer getFarDistanceToGhost() {
		return FarDistanceToGhost;
	}

	public void setFarDistanceToGhost(Integer farDistanceToGhost) {
		FarDistanceToGhost = farDistanceToGhost;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostsDescription.class);
	}

	@Override
	public String toString() {
		return "GhostsDescription [\n"
				+ "id= " + id 
				+ ", score= " + score 
				+ ", CloseDistance= " + CloseDistance
				+ ", MidCDistance= " + MidCDistance 
				+ ", MidFDistance= " + MidFDistance
				+ ", FarDistance= " + FarDistance
				+ ", Closeedible= " + Closeedible
				+ ", MidCedible= " + MidCedible
				+ ", MidFedible= " + MidFedible
				+ ", Faredible= " + Faredible
				+ ", nearestPPill= " + nearestPPill 
				+ ", nearestPill= " + nearestPill 
				+ ", lives= " + lives
				+ ", time= " + time 
				+ ", level= " + level
				+ ", PacManLastMove= " + PacManLastMove
				+ ", CloseMove= " + CloseMove		
				+ ", MidCMove= " + MidCMove		
				+ ", MidFMove= " + MidFMove		
				+ ", FarMove= " + FarMove	
				+ ", CloseDistanceToGhost= " + CloseDistanceToGhost
				+ ", MidCDistanceToGhost= " + MidCDistanceToGhost
				+ ", MidFDistanceToGhost= " + MidFDistanceToGhost
				+ ", FaristanceToGhost= " + FarDistanceToGhost
				+ "\n]" ;
	}
}
