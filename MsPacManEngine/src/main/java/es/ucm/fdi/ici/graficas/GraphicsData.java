package es.ucm.fdi.ici.graficas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphicsData {
	
	private int intentos;
	private Vector<EpisodeData> v;
	private List<List<String>>  dataLines;
	private List<String>dataTimes;
	private List<String> dataAvg;
	
	
	public GraphicsData(int intentos) {
		this.intentos=intentos;
		this.v=new Vector<EpisodeData>();
		this.dataLines = new ArrayList<>();
		this.dataTimes = new ArrayList<>();
		this.dataAvg = new ArrayList<>();
	}

	public void addEpisode(EpisodeData d) {
		// TODO Auto-generated method stub
		this.v.add(d);
		
	}
	public void compute() {
		//pasar a un archivo csv todos los datos
		for(EpisodeData e:v) {
			//Resultados luchas
			dataLines.addAll(e.getData());
			//Tiempos en cada episodio
			dataTimes.add(e.getAvgTime());
			//Medias knn 
			dataAvg.addAll(e.getAvgList());
			
		}
		getCSV();
		getTimeCSV();
		getAvgCSV();
	}
	/*public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}*/
	public void getAvgCSV()  {
		File csvOutputFile = new File("avgknn.csv");

        try (PrintWriter pw = new PrintWriter(new FileWriter(csvOutputFile))) {
            dataAvg.forEach(line -> {
                pw.println(line);
            });
        } catch (IOException e) {
            e.printStackTrace();
        
	    }
	}
	public void getTimeCSV()  {
		File csvOutputFile = new File("tiempos.csv");

        try (PrintWriter pw = new PrintWriter(new FileWriter(csvOutputFile))) {
            dataTimes.forEach(line -> {
                pw.println(line);
            });
        } catch (IOException e) {
            e.printStackTrace();
        
	    }
	}
	public void getCSV()  {
		File csvOutputFile = new File("resultados.csv");

        try (PrintWriter pw = new PrintWriter(new FileWriter(csvOutputFile))) {
            dataLines.forEach(line -> {
                String csvLine = convertToCSV(line);
                pw.println(csvLine);
            });
        } catch (IOException e) {
            e.printStackTrace();
        
	    }
	    //assertTrue(csvOutputFile.exists());
	}
	private  String convertToCSV(List<String> data) {
        return String.join(",", data);
    }
	
	/*private String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}*/
}
