package es.ucm.fdi.ici.graficas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import es.ucm.fdi.ici.Scores.ScorePair;
import pacman.game.util.Stats;

public class EpisodeData {
	
	private long totalTime;
	private int episode;
	private int cicles;
	private double avgTime;
	
	
	Stats[][]stats;
	
	public EpisodeData(int episode,int cicles) {
		this.episode=episode;
		this.cicles=cicles;
	}

	public void addScores(Stats[][] stats) {
		this.stats=stats;
	}

	public void addTime(long t) {
		this.totalTime=t;
	}
	
	public void computeData() {
		//calculateTimePerCicle();
		this.avgTime=this.totalTime/this.cicles;
		
	}
	
	
		//Borrar
	/*private void calculateTimePerCicle() {
		this.avgTime=this.totalTime/this.cicles;
	}*/
	public String getAvgTime() {
		return episode +","+ this.avgTime;
	}
	public List<List<String>> getData() {
		List<List<String>> dataLines = new ArrayList<>();
		
		int i=0;
		for(Stats[] result_pacman : stats)
        {
			List<String>line = new ArrayList<>();
			line.add(Integer.toString(episode)); //episodio
			line.add(Integer.toString(i)); //numero mspacman
        	for(Stats s: result_pacman)
        	{
        		line.add(Double.toString(s.getAverage()));  //añadir resultado mspacman-ghost en orden
        	}	
        	dataLines.add(line);
        	i++;
        } 
		
		return dataLines;
		
	}
	
}
