package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealVector;

public class NumOptAlgorithms {
	
	private static double eps = 1E-6;
	
	static RealVector gradientDescent(IFunction f, int maxIter, RealVector startValues, boolean useBisection){
		RealVector x = startValues.copy();
		
		for(int i = 0; i < maxIter; i++) {
			// mnozimo pomak s -1 pa ga kasnije dodajemo umjesto oduzimamo
			RealVector dx = f.getGradient(x).mapMultiply(-1);
			
			// trazimo lambdu, radimo korak
			double lambda = getLambda(f, x, dx, useBisection);
			x = x.add(dx.mapMultiply(lambda));
			
			System.out.print((i + 1) + ". iteracija. Trenutno rješenje: [");
			for (int j = 0; j < startValues.getDimension() - 1; j++) {
				System.out.print(x.getEntry(j) + " ");
			}			
			System.out.print(x.getEntry(startValues.getDimension() - 1));
			System.out.print("]\n");
			// uvjet zaustavljanja: norma gradijenta je priblizno nula
			if (f.getGradient(x).getNorm() < eps) {
				System.out.println("Norma gradijenta je priblizno nula. Pronaden minimum");
				break;
			}
		}
		
		return x;
    }
	
	private static double getLambda(IFunction f, RealVector x, RealVector d, boolean useBisection) {
		if (!useBisection) {
			return 0.01;
		}
		double lambdaLower = 0;
		double lambdaUpper = 1;		
		double lambda;		
		double dtheta;
		
		RealVector temp = x.copy();
		
		// prvo trazimo gornju granicu lambde tako da ju udvostrucujemo dok gradijent ne postane pozitivan
		while(f.getGradient(temp).dotProduct(d) < 0) {
			lambdaUpper *= 2;
			temp = x.add(d.mapMultiply(lambdaUpper));
		}
		
		temp = x.copy();
		
		do {
			lambda = (lambdaLower + lambdaUpper) / 2;
			// temp = x - dx * lambda
			temp = x.add(d.mapMultiply(lambda));
			dtheta = f.getGradient(temp).dotProduct(d);
			
			if (Math.abs(dtheta) < eps) {
				return lambda;
			}
			if (dtheta < 0)	{
				lambdaLower = lambda;
			} 
			else if (dtheta > 0) {
				lambdaUpper = lambda;
			}
		} while (Math.abs(dtheta) > eps);
		
		return lambda;
	}
}
