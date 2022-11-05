package hr.fer.zemris.optjava.dz3;

import org.apache.commons.math3.linear.RealVector;
import java.util.Random;

public class SimulatedAnnealing {
	
	private static double T = 100.0;
	private static double min_T = 0.01;
	private static int maxIter = 20000;
	private static double alpha = 0.99;
	private static double beta = 0.01;
	
	public static RealVector solve(IFunction system, RealVector startingPoint, boolean minimize, boolean verbose) throws InterruptedException {
		Random rand = new Random();
		System.out.println("Početna točka: " + startingPoint.toString() +" Pogreška u toj točki: " + system.getValue(startingPoint));		
		
		RealVector currentPoint = startingPoint.copy();
		
		// outer loop - mijenja temperaturu
		while(T > min_T) {
			// inner loop - za fiksnu temperaturu radi innerIter pretraga
			for(int innerIter = 1; innerIter <= maxIter; innerIter++) {
				
				double E1 = system.getValue(currentPoint);				
				RealVector neighborPoint = getNeighborhood(currentPoint);
				double E2 = system.getValue(neighborPoint);
				
				double deltaE = E2 - E1;
				if (verbose) System.out.println("Pogreška u trenutnoj točki: " + E1 + " Pogreška u susjednoj točki: " + E2 + " Razlika pogreške: " + deltaE);
				
				if (deltaE < 0) {
					if (verbose) System.out.println("dE manji od 0, prihvaća se novo rješenje");
					currentPoint = neighborPoint.copy();
				}
				else if(deltaE == 0) {
					// nije se promijenila pogreska
				}
				
				else {
					double P = Math.exp(-deltaE / T);
					double R = rand.nextDouble();
					if (P >= R) {
						if (verbose) System.out.println("dE veći od 0 ali prihvaća se novo rješenje (P = " + P + ", R = " + R + ")");
						currentPoint = neighborPoint.copy();
					} else {
						if (verbose) System.out.println("dE veći od 0 ali ne prihvaća se novo rješenje (P = " + P + ", R = " + R + ")");
						currentPoint = currentPoint.copy();
					}
				}
			}						
			System.out.println("T = " + T + " | Trenutno rješenje: " + currentPoint.toString() + " | Pogreška: " + system.getValue(currentPoint));
			
			T = geometricCooling(T);
		}		
		RealVector minimum = currentPoint.copy();
		
		return minimum;
	}
	
	private static RealVector getNeighborhood(RealVector x) {
		Random rand = new Random();		
		int randInt = rand.nextInt(x.getDimension());
		RealVector new_x = x.copy();
		// koeficijent d (pozicija 3) je u eksponentu pa njega necemo previse mijenjati
		if (randInt == 3) {
			new_x.addToEntry(randInt, rand.nextDouble() * 0.02 - 0.01);
		} else {
			new_x.addToEntry(randInt, rand.nextDouble() * 0.2 - 0.1);
		}
		return new_x;
	}
	
	private static double geometricCooling(double T) {
		return alpha * T;
	}
	
	private static double linearCooling(double T) {
		return T - beta;
	}
}
