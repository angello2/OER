package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.*;

public interface IFunction {
	int getNumberOfVariables();
	double value(RealMatrix matrix);
	RealMatrix getGradient(RealMatrix matrix);
}
