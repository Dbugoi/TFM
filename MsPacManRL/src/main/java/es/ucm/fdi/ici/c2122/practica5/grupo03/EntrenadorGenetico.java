package es.ucm.fdi.ici.c2122.practica5.grupo03;
/*
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.ElitisticListPopulation;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.apache.commons.math3.genetics.OrderedCrossover;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.genetics.RandomKey;
import org.apache.commons.math3.genetics.RandomKeyMutation;
import org.apache.commons.math3.genetics.StoppingCondition;
import org.apache.commons.math3.genetics.TournamentSelection;
*/


public class EntrenadorGenetico {
/*	
	private static int SAFE_VALUE = 100;
	private static int NUM_GENERATIONS = 1;
	private static ExecutorTestGenetico et = new ExecutorTestGenetico();
	
	static class CromosomaCBR extends RandomKey<Double> {

		public CromosomaCBR(List<Double> representation) throws InvalidRepresentationException {
			super(representation);
		}

		@Override
		public double fitness() {
			// TODO Auto-generated method stub
			return et.ejecutar(SAFE_VALUE, new Vector<Double>(this.decode(this.getRepresentation())));
		}

		@Override
		public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
			// TODO Auto-generated method stub
			return new CromosomaCBR(chromosomeRepresentation);
		}
		
		public String Strin() {
			String s = "";
			s += "Pacman [";
			
			List<Double> l = this.decode(this.getRepresentation());
			for(int i = 0; i < l.size()/2 ; i++) {
				if(i == l.size()/2 - 1) {
					s+= l.get(i) + "]";
				}
				else {
					s += l.get(i).toString() + ", ";					
				}
			}
			
			s+= "\nGhosts [";
			for(int i = l.size()/2 ; i < l.size(); i++) {
				if(i == l.size()/2 - 1) {
					s+= l.get(i) + "]";
				}
				else {
					s += l.get(i).toString() + ", ";					
				}
			}
			
			return s;
		
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GeneticAlgorithm ga = new GeneticAlgorithm(
				//Cruce
				new OrderedCrossover<Double>()
				//Double
				,0.9
				//Mutation
				, new RandomKeyMutation()
				//Double
				, 0.1
				//Selection
				, new TournamentSelection(4)
				);
		
		Population p0 = getInitialPopulation();
		//StoppingCondition sc2 = new FixedGenerationCount(NUM_GENERATIONS);
		StoppingCondition sc = new StoppingCondition() {
			
			int veces = 0;
			
			@Override
			public boolean isSatisfied(Population population) {
				// TODO Auto-generated method stub
				veces ++;
				if (veces == NUM_GENERATIONS) {
					veces --;
					return true;
				}
				else {
					return false;
				}
			}
			
			
		};
		
		Population pf = ga.evolve(p0, sc);
		
		System.out.println(((CromosomaCBR) pf.getFittestChromosome()).Strin());
		
	}
	
	public static Population getInitialPopulation() {
		Population p = new ElitisticListPopulation(50, 0.1);
		
		Random r = new Random();
		for(int i = 0; i < 50; i++) {
			Vector<Double> v = new Vector<Double>(24);
			double s1 = 0;
			double s2 = 0;
			for(int k = 0; k < 12; k++) {
				double d = r.nextDouble();
				s1+= d;
				v.add(d);
			}
			for(int k = 12; k < 24; k++) {
				double d = r.nextDouble();
				s2 += d;
				v.add(d);
			}
			
			for(int k = 0; k < 12; k++) {
				v.set(k, v.get(k)/s1);
				v.set(k + 12, v.get(k+12)/s2);
			}
			p.addChromosome(new CromosomaCBR(v));
		}
		System.out.println("Pop tiene " + p.getPopulationSize() + " tam");
		return p;
	}
*/	
	

}
