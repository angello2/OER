package hr.fer.zemris.optjava.dz5;

import org.apache.commons.math3.linear.*;

public interface IFunction {
	public int getNumberOfVariables();
	public double getValue(RealVector values);	
}
