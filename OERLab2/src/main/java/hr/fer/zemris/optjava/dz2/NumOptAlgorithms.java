package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.RealVector;

public class NumOptAlgorithms {
	
	private static final double eps = 1E-2;
	
	static RealVector gradientDescent(IFunction f, int maxIter, RealVector startValues){
		RealVector x = startValues.copy();
		
		for(int i = 0; i < maxIter; i++) {
			
			// uvjet zaustavljanja: norma gradijenta je priblizno nula
			if(f.getGradient(x).getNorm() < eps) {
				System.out.println("Norma gradijenta je priblizno nula. Pronaden minimum");
				break;
			}
			
			System.out.println((i + 1) + ". iteracija. Trenutno rješenje: x1 = " + x.getEntry(0) + " x2 = " + x.getEntry(1)+ " vrijednost = " + f.getValue(x));
			
			// vektor pomaka
			RealVector d = f.getGradient(x).mapMultiply(-1);
			
			double lambda = getLambda(f, x, d);
			
			x = x.add(d.mapMultiply(lambda));
		}
		
		return x;
    }
	
	private static double getLambda(IFunction f, RealVector x, RealVector d) {
		double lambdaLower = 0;
		double lambdaUpper = 1;
		
		RealVector temp = x.copy();
		
		while(f.getGradient(temp).dotProduct(d) < 0) {
			lambdaUpper *= 2;
			temp = x.add(d.mapMultiply(lambdaUpper));
		}
		temp = x.copy();
		
		double lambda;		
		double thetaD;
		do {
			lambda = (lambdaLower + lambdaUpper) / 2;
			temp = x.add(d.mapMultiply(lambda));
			thetaD = thetaD(f,temp,d,lambda);
			if(Math.abs(thetaD) < eps) {
				return lambda;
			}
			if(thetaD < 0) {
				lambdaLower = lambda;
			} else {
				lambdaUpper = lambda;
			}
		} while(Math.abs(thetaD) > eps);
		return lambda;		
	}
	
	private static double thetaD(IFunction f, RealVector x, RealVector d, double lambda) {		
		return f.getGradient(x).dotProduct(d);
	}
}
