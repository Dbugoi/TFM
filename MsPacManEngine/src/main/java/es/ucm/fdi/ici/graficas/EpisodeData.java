package es.ucm.fdi.ici.graficas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import es.ucm.fdi.ici.Scores.ScorePair;
import pacman.game.util.Stats;

public class EpisodeData {
	
	private int totalTime;
	private int episode;
	private int cicles;
	private int avgTime;
	
	
	Stats[][]stats;
	
	public EpisodeData(int episode) {
		this.episode=episode;
	}

	public void addScores(Stats[][] stats) {
		this.stats=stats;
	}

	
	
	public void computeData() {
		calculateTimePerCicle();
		
	}
	
	

	private void calculateTimePerCicle() {
		this.avgTime=this.totalTime/this.cicles;
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
	/*
	public String getData() {
		String res="";
		int i=0;
		for(Stats[] result_pacman : stats)
        {
			String aux=";";
        	for(Stats s: result_pacman)
        	{
        		aux+=Double.toString(s.getAverage())+";";
        		//System.out.print(s.getAverage()+";");
        	}
        	
        	
        	i++;
        }      
		
		return res;
		
	}*/
	
}
