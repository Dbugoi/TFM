package es.ucm.fdi.ici.c2122.practica5.grupo02;

import java.io.File;
import java.awt.Color;

public class GameConstants {

	public final static String CONNECTOR_FILE_PATH = "es" + File.separator + "ucm" + File.separator + "fdi" + File.separator
			+ "ici" + File.separator + "c2122" + File.separator + "practica5" + File.separator + "grupo02" + File.separator;

	public final static String CASE_BASE_PATH = "cbrdata" + File.separator + "grupo02" + File.separator;
	
	public final static int NUM_CASES = 5;
	
	public final static int MIN_DIST = 8;
	
	public final static double MIN_SIMILARITY = 0.55;
	
	public final static double SIM_TOO_CLOSE = 0.95;
	
	public final static int MAX_SCORE_WEIGHT = 100;

	public static boolean DEBUG = false;
	
}