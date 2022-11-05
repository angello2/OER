package hr.fer.zemris.optjava.dz3;

import org.apache.commons.math3.linear.*;

public interface IFunction {
	int getNumberOfVariables();
	double getValue(RealVector values);
}
