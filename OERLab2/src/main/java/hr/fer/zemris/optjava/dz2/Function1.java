package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Function1 implements IFunction {

	// f(x1,x2) = x1^2 + (x2-1)^2
	
	@Override
    public double getValue(RealVector values) {
		double x1 = values.getEntry(0);
		double x2 = values.getEntry(1);
    
		// funkcija je x1^2 + (x2 - 1)^2
		double result = Math.pow(x1, 2) + Math.pow((x2 - 1), 2);
		return result;
    }

    @Override
    public RealVector getGradient(RealVector values) {    	
    	double x1 = values.getEntry(0);
		double x2 = values.getEntry(1);
    	
    	// gradijent funkcije je [(2 * x1), (2 * x2 - 2)]
    	double[] vectorData = {2 * x1, 2 * x2 - 2};
    	RealVector gradient = MatrixUtils.createRealVector(vectorData);
    	
    	return gradient;
    }

	@Override
	public int getNumberOfVariables() {
		return 2;
	}

}
