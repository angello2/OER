package hr.fer.zemris.optjava.dz3;

import org.apache.commons.math3.linear.RealVector;
import java.util.Random;

public class SimulatedAnnealing {
	
	private static double T = 100.0;
	private static double min_T = 0.01;
	private static int maxIter = 20000;
	private static double beta = 0.01;
	private static double alpha = 0.99;
	
	public static RealVector solve(IFunction system, RealVector startingPoint, boolean minimize, boolean verbose) throws InterruptedException {
		Random rand = new Random();
		System.out.println("Početna točka: " + startingPoint.toString() +" Pogreška u toj točki: " + system.getValue(startingPoint));		
		
		RealVector currentPoint = startingPoint.copy();
		
		// outer loop - mijenja temperaturu
		while(T > min_T) {
			// inner loop - za fiksnu temperaturu radi innerIter pretraga
			for(int innerIter = 1; innerIter <= maxIter; innerIter++) {				
				// Thread.sleep(500);
				
				double E1 = system.getValue(currentPoint);				
				RealVector neighborPoint = getNeighborhood(currentPoint);
				double E2 = system.getValue(neighborPoint);
				
				double deltaE = E2 - E1;
				System.out.println("Pogreška u trenutnoj točki: " + E1 + " Pogreška u susjednoj točki: " + E2 + " Razlika pogreške: " + deltaE);
				
				if (deltaE < 0) {
					System.out.println("dE ispod 0, prihvaćam novo rješenje");
					currentPoint = neighborPoint.copy();
				}
				else if(deltaE == 0) {
					System.out.println("Pogreška se nije promijenila");
				}
				else {
					double P = Math.exp(-deltaE / T);
					double R = rand.nextDouble();
					if (P > R) {
						currentPoint = neighborPoint.copy();
					} else {
						currentPoint = currentPoint.copy();
					}
				}
			}			
			T = geometricCooling(T);
		}		
		RealVector minimum = currentPoint.copy();
		
		return minimum;
	}
	
	private static RealVector getNeighborhood(RealVector x) {
		Random rand = new Random();		
		int randInt = rand.nextInt(x.getDimension());
		RealVector new_x = x.copy();
		// koeficijent d je u eksponentu pa njega necemo previse mijenjati
		if (randInt == 3) {
			new_x.addToEntry(randInt, rand.nextDouble() * (0.001 + 0.001) - 0.001);
		} else {
			new_x.addToEntry(randInt, rand.nextDouble() * (0.1 + 0.1) - 0.1);
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
