package es.ucm.fdi.ici.c2122.practica3.grupo02;

import java.io.File;
import java.util.Iterator;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class Retetest {

	public static void main(String args[]) {
		String RULES_FILE = "inkyrules.clp";
		String rulesFile = String.format("%s%s", GameConstants.RULES_PATH, RULES_FILE);
		Rete jess = new Rete();
		try {
			jess.batch(rulesFile);
			jess.reset();
			jess.run();
			Iterator<?> it = jess.listFacts();
			while(it.hasNext()){ 
			    Fact dd = (Fact)it.next();
			    System.out.println(dd.toString());
			}
		} catch (JessException e) {
			e.printStackTrace();
		}
	}
}
