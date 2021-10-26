package hr.fer.zemris.optjava.dz2;

import org.apache.commons.math3.linear.*;

public interface IFunction {
	int getNumberOfVariables();
	double getValue(RealVector values);
	RealVector getGradient(RealVector values);
}
