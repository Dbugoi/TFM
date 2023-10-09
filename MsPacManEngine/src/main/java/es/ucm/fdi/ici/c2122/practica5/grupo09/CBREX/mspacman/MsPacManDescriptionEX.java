package es.ucm.fdi.ici.c2122.practica5.grupo09.CBREX.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;

public class MsPacManDescriptionEX implements CaseComponent {
	Integer id;
	
	Integer level;
	Integer score;
	Integer time;
	Integer lives;
	
	Integer msPacmanPos;
	Integer msPacmanMove;

	Integer nearestGhost1;
	Integer nearestGhost2;
	Integer nearestGhost3;
	Integer nearestGhost4;
	
	Boolean nearestEdible1;
	Boolean nearestEdible2;
	Boolean nearestEdible3;
	Boolean nearestEdible4;
	
	Integer INKYlair;
	Integer PINKYlair;
	Integer BLINKYlair;
	Integer SUElair;
	
	Integer INKYdir;
	Integer PINKYdir;
	Integer BLINKYdir;
	Integer SUEdir;
	
	Integer nearestPPill;
	Integer remainingPills;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}


	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getMsPacmanPos() {
		return msPacmanPos;
	}

	public void setMsPacmanPos(Integer msPacmanPos) {
		this.msPacmanPos = msPacmanPos;
	}

	public Integer getNearestGhost1() {
		return nearestGhost1;
	}

	public void setNearestGhost1(Integer nearestGhost1) {
		this.nearestGhost1 = nearestGhost1;
	}

	public Integer getNearestGhost2() {
		return nearestGhost2;
	}

	public void setNearestGhost2(Integer nearestGhost2) {
		this.nearestGhost2 = nearestGhost2;
	}

	public Integer getNearestGhost3() {
		return nearestGhost3;
	}

	public void setNearestGhost3(Integer nearestGhost3) {
		this.nearestGhost3 = nearestGhost3;
	}

	public Integer getNearestGhost4() {
		return nearestGhost4;
	}

	public void setNearestGhost4(Integer nearestGhost4) {
		this.nearestGhost4 = nearestGhost4;
	}

	public Boolean getNearestEdible1() {
		return nearestEdible1;
	}

	public void setNearestEdible1(Boolean nearestEdible1) {
		this.nearestEdible1 = nearestEdible1;
	}

	public Boolean getNearestEdible2() {
		return nearestEdible2;
	}

	public void setNearestEdible2(Boolean nearestEdible2) {
		this.nearestEdible2 = nearestEdible2;
	}

	public Boolean getNearestEdible3() {
		return nearestEdible3;
	}

	public void setNearestEdible3(Boolean nearestEdible3) {
		this.nearestEdible3 = nearestEdible3;
	}

	public Boolean getNearestEdible4() {
		return nearestEdible4;
	}

	public void setNearestEdible4(Boolean nearestEdible4) {
		this.nearestEdible4 = nearestEdible4;
	}

	public Integer getINKYdir() {
		return INKYdir;
	}

	public void setINKYdir(Integer iNKYdir) {
		INKYdir = iNKYdir;
	}

	public Integer getPINKYdir() {
		return PINKYdir;
	}

	public void setPINKYdir(Integer pINKYdir) {
		PINKYdir = pINKYdir;
	}

	public Integer getBLINKYdir() {
		return BLINKYdir;
	}

	public void setBLINKYdir(Integer bLINKYdir) {
		BLINKYdir = bLINKYdir;
	}

	public Integer getSUEdir() {
		return SUEdir;
	}

	public void setSUEdir(Integer sUEdir) {
		SUEdir = sUEdir;
	}

	public Integer getMsPacmanMove() {
		return msPacmanMove;
	}

	public void setMsPacmanMove(Integer msPacmanMove) {
		this.msPacmanMove = msPacmanMove;
	}

	
	public Integer getINKYlair() {
		return INKYlair;
	}

	public void setINKYlair(Integer iNKYlair) {
		INKYlair = iNKYlair;
	}

	public Integer getPINKYlair() {
		return PINKYlair;
	}

	public void setPINKYlair(Integer pINKYlair) {
		PINKYlair = pINKYlair;
	}

	public Integer getBLINKYlair() {
		return BLINKYlair;
	}

	public void setBLINKYlair(Integer bLINKYlair) {
		BLINKYlair = bLINKYlair;
	}

	public Integer getSUElair() {
		return SUElair;
	}

	public void setSUElair(Integer sUElair) {
		SUElair = sUElair;
	}

	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}

	public Integer getRemainingPills() {
		return remainingPills;
	}

	public void setRemainingPills(Integer remainingPills) {
		this.remainingPills = remainingPills;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescriptionEX.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + 
		", level=" + level + 
		", score=" + score + 
		", time=" + time +
		", lives=" + lives + 
		
		", msPacmanPos=" + msPacmanPos + " " + 
		", msPacmanMove=" + msPacmanMove +
		
		", ghostsPos=" + nearestGhost1 + " " + nearestGhost2 + " " + nearestGhost3 + " " + nearestGhost4 + " " + 
		", ghostsEdible=" + nearestEdible1 + " " + nearestEdible2 + " " + nearestEdible3 + " " + nearestEdible4 + " " + 
		", ghostLairTime=" + INKYlair + " " + PINKYlair + " " + BLINKYlair + " " + SUElair + " " + 
		", ghostsDir=" + INKYdir + " " + PINKYdir + " " + BLINKYdir + " " + SUEdir + " " + 
		//", ghostsDist=" + INKYdist + " " + PINKYdist + " " + BLINKYdist + " " + SUEdist + " " + 
		
		", nearestPPill=" + nearestPPill + 
		", remainingPills=" + remainingPills + "]";
	}

	public void setGhostsPos(Integer[] ghostsPos) {
		setNearestGhost1(ghostsPos[0]);
		setNearestGhost2(ghostsPos[1]);
		setNearestGhost3(ghostsPos[2]);
		setNearestGhost4(ghostsPos[3]);
	}

	public void setGhostLairTime(Integer[] ghostLairTime) {
		setBLINKYlair(ghostLairTime[0]);
		setPINKYlair(ghostLairTime[1]);
		setINKYlair(ghostLairTime[2]);
		setSUElair(ghostLairTime[3]);
	}

	public void setEdibleGhost(Boolean[] ghostEdible) {
		setNearestEdible1(ghostEdible[0]);
		setNearestEdible2(ghostEdible[1]);
		setNearestEdible3(ghostEdible[2]);
		setNearestEdible4(ghostEdible[3]);
	}

	public void setGhostsDir(Integer[] ghostDir) {
		setBLINKYdir(ghostDir[0]);
		setPINKYdir(ghostDir[1]);
		setINKYdir(ghostDir[2]);
		setSUEdir(ghostDir[3]);
	}

//	public void setGhostsDist(Integer[] ghostsDist) {
//		setBLINKYdist(ghostsDist[0]);
//		setPINKYdist(ghostsDist[1]);
//		setINKYdist(ghostsDist[2]);
//		setSUEdist(ghostsDist[3]);
//	}


	
	

}
