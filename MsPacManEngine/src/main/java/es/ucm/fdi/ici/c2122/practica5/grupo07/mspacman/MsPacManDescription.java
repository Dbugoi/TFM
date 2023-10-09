package es.ucm.fdi.ici.c2122.practica5.grupo07.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2122.practica5.grupo07.ArrayTypeAdaptor;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	Double score;
	Integer time;
	Double nearestGhost;
	Double oldscore;

	ArrayTypeAdaptor danger;
	ArrayTypeAdaptor edible;

	ArrayTypeAdaptor edible34;
	
	MOVE lastmove;
	
	public Double getOldscore() {
		return oldscore;
	}

	public void setOldscore(Double oldscore) {
		this.oldscore = oldscore;
	}


	
	public MOVE getLastmove() {
		return lastmove;
	}

	public void setLastmove(MOVE lastmove) {
		this.lastmove = lastmove;
	}


	public ArrayTypeAdaptor getEdible34() {
		return edible34;
	}

	public void setEdible34(ArrayTypeAdaptor edible34) {
		this.edible34 = edible34;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}


	public Double getNearestGhost() {
		return nearestGhost;
	}

	public void setNearestGhost(Double nearestGhost) {
		this.nearestGhost = nearestGhost;
	}



	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	public ArrayTypeAdaptor getDanger() {
		return danger;
	}

	public void setDanger(ArrayTypeAdaptor danger) {
		this.danger = danger;
	}

	public ArrayTypeAdaptor getEdible() {
		return edible;
	}

	public void setEdible(ArrayTypeAdaptor edible) {
		this.edible = edible;
	}

	@Override
	public String toString() {
		/*StringBuilder str = new StringBuilder();
		for(int i=0;i<4;i++) {
			str.append(Double.toString(danger[i]));
			if(i!=3)str.append(" ");
		}str.append("\n");
		for(int i=0;i<4;i++) {
			str.append(Double.toString(edible[i]));
			if(i!=3)str.append(" ");
		}str.append("\n");
		return str.toString();*/
		return "MsPacManDescription [id=" + id + ", score=" + score + ", danger=" + danger + ", edible=" + edible + ", lastmove=" + lastmove+"]";
		//return "MsPacManDescription [id=" + id + ", score=" + score + ", time=" + time + ", nearestPPill="
		//		+ nearestPPill + ", nearestGhost=" + nearestGhost + ", edibleGhost=" + edibleGhost + "]";
	}


	
	

}
